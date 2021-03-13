package com.link_intersystems.eclipse.ui.nebula.graph;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IToolTipProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.dataprovider.ISample;
import org.eclipse.nebula.visualization.xygraph.figures.Axis;
import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.Legend;
import org.eclipse.nebula.visualization.xygraph.figures.PlotArea;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.BaseLine;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.ErrorBarType;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.TraceType;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.ZoomType;
import org.eclipse.nebula.visualization.xygraph.linearscale.Range;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.link_intersystems.eclipse.core.runtime.runtime.IAdaptable2;
import com.link_intersystems.math.LinearEquation;
import com.link_intersystems.math.TwoPointLinearEquation;

import io.jitclipse.core.model.ICompilationList;

public abstract class XYGraphViewer extends StructuredViewer {

	private MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter() {

		public void mouseMoved(org.eclipse.draw2d.MouseEvent me) {
			if (graphDataProvider == null) {
				return;
			}

			double sampleXValue = xAxis.getPositionValue(me.x, false);
			double sampleYValue = yAxis.getPositionValue(me.y, false);

			Rectangle xBounds = xAxis.getBounds();
			Range xDataMinMax = graphDataProvider.getXDataMinMax();
			double xDataDiff = xDataMinMax.getUpper() - xDataMinMax.getLower();
			LinearEquation xValueToGraphifs = new TwoPointLinearEquation(xDataDiff, xBounds.width);
			double xDiff = xValueToGraphifs.fY(6);

			Rectangle yBounds = yAxis.getBounds();
			Range yDataMinMax = graphDataProvider.getYDataMinMax();
			double yDataDiff = yDataMinMax.getUpper() - yDataMinMax.getLower();
			LinearEquation yValueToGraphifs = new TwoPointLinearEquation(yDataDiff, yBounds.height);
			double yDiff = yValueToGraphifs.fY(6);

			ISample sampleAt = sampleIndex.getSampleAt(sampleXValue, sampleYValue, xDiff, yDiff);
			setToolTipFor(sampleAt, new Point(me.x, me.y));
		};
	};

	private class ColorRegistryAdapter implements IPropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			themeChanged();
			xyGraph.repaint();
		}

	}

	protected Action zoomInAction = new ZoomTypeAction(this::getXyGraph, ZoomType.ZOOM_IN);
	protected Action zoomOutAction = new ZoomTypeAction(this::getXyGraph, ZoomType.ZOOM_OUT);
	protected Action zoomHorizontalAction = new ZoomTypeAction(this::getXyGraph, ZoomType.HORIZONTAL_ZOOM);
	protected Action zoomVerticalAction = new ZoomTypeAction(this::getXyGraph, ZoomType.VERTICAL_ZOOM);
	protected Action zoomPanningAction = new ZoomTypeAction(this::getXyGraph, ZoomType.PANNING);
	protected Action zoomNoneAction = new ZoomTypeAction(this::getXyGraph, ZoomType.NONE);
	protected Action zoomRubberbandAction = new ZoomTypeAction(this::getXyGraph, ZoomType.RUBBERBAND_ZOOM);

	protected Action saveSnapshotAction = new SaveSnapshotAction(this::getXyGraph);
	protected Action autoScaleAction = new AutoScaleAction(this::getXyGraph);

	private ColorRegistryAdapter colorRegistryAdapter = new ColorRegistryAdapter();
	private LabelProviderAwareAxisFactory labelProviderAxisFactory = new LabelProviderAwareAxisFactory();

	private ColorRegistry colorRegistry;
	private SampleIndex sampleIndex = new SampleIndex();

	private Control control;

	private XYGraph xyGraph;
	private Trace trace;
	private Axis xAxis;
	private Axis yAxis;

	private Object input;

	private CircularBufferDataProvider graphDataProvider;
	private IToolTipProvider toolTipProvider;

	private IFigure toolTipFigure;

	public XYGraphViewer(Composite parent, int style) {
		this(parent, style, JFaceResources.getColorRegistry());
	}

	public XYGraphViewer(Composite parent, int style, ColorRegistry colorRegistry) {
		this.colorRegistry = colorRegistry;
		Canvas canvas = new Canvas(parent, style | SWT.DOUBLE_BUFFERED);
		LightweightSystem lws = new LightweightSystem(canvas);
		IXYGraph toolbarArmedXYGraph = createXYGraph();
		lws.setContents(toolbarArmedXYGraph);

		control = canvas;

		zoomInAction.setActionDefinitionId("someIdDefinition");

		updateGraph();

		colorRegistry.addListener(colorRegistryAdapter);
		canvas.addDisposeListener((c) -> colorRegistry.removeListener(colorRegistryAdapter));
	}

	private void setToolTipFor(ISample sample, Point location) {
		boolean repaint = true;
		if (sample == null || toolTipProvider == null) {
			repaint = toolTipFigure != null;
			toolTipFigure = null;
		} else {
			String toolTipText = toolTipProvider.getToolTipText(sample);
			toolTipFigure = new Label(toolTipText);
			location.x += 10;
			location.y -= 10;
			toolTipFigure.setLocation(location);

			themeChanged();
		}

		if (repaint) {
			xyGraph.repaint();
		}

	}

	private void themeChanged() {
		applyToolTipFigureTheme();
	}

	private void applyToolTipFigureTheme() {
		if (toolTipFigure == null) {
			return;
		}

		Color forground = colorRegistry.get("io.jitclipse.ui.timeline.tooltip.foreground");
		toolTipFigure.setForegroundColor(forground);

		Color background = colorRegistry.get("io.jitclipse.ui.timeline.tooltip.background");
		toolTipFigure.setBackgroundColor(background);

		Color borderColor = forground;
		LineBorder lineBorder = new LineBorder(borderColor);
		MarginBorder marginBorder = new MarginBorder(2, 2, 2, 2);
		CompoundBorder toolTipBorder = new CompoundBorder(lineBorder, marginBorder);
		toolTipFigure.setBorder(toolTipBorder);
		toolTipFigure.setOpaque(true);
	}

	protected XYGraph getXyGraph() {
		return xyGraph;
	}

	private void paintToolTip(Graphics graphics) {
		if (toolTipFigure == null) {
			return;
		}

		Dimension preferredSize = toolTipFigure.getPreferredSize();
		toolTipFigure.setSize(preferredSize);
		Rectangle bounds = xyGraph.getBounds();
		Rectangle toolTipBounds = toolTipFigure.getBounds();
		Point topRight = toolTipBounds.getTopRight();
		int xDiff = topRight.x - bounds.width;
		if (xDiff > 0) {
			toolTipBounds.x -= toolTipBounds.width + 20;
		}

		int yDiff = topRight.y - (bounds.y + bounds.height);
		if (yDiff > 0) {
			toolTipBounds.y -= toolTipBounds.height + 20;
		}

		toolTipFigure.setBounds(toolTipBounds);

		toolTipFigure.paint(graphics);
	}

	public void setxAxisLabelProvider(ILabelProvider xAxisLabelProvider) {
		labelProviderAxisFactory.setxAxisLabelProvider(xAxisLabelProvider);
	}

	public void setyAxisLabelProvider(ILabelProvider yAxisLabelProvider) {
		labelProviderAxisFactory.setyAxisLabelProvider(yAxisLabelProvider);
	}

	private IXYGraph createXYGraph() {
		xyGraph = new XYGraph(labelProviderAxisFactory) {

			@Override
			protected void paintClientArea(Graphics graphics) {
				super.paintClientArea(graphics);
				paintToolTip(graphics);
			}

		};

		xAxis = xyGraph.getPrimaryXAxis();
		xAxis.setAutoScale(true);
		xAxis.setShowMajorGrid(true);
		xAxis.setAutoScaleThreshold(0);
		xAxis.setDateEnabled(true);

		yAxis = xyGraph.getPrimaryYAxis();
		yAxis.setRange(0, 10);
		yAxis.setAutoScale(true);
		yAxis.setShowMajorGrid(true);

		graphDataProvider = new CircularBufferDataProvider(false);
		graphDataProvider.setUpdateDelay(100);
		sampleIndex.setDataProvider(graphDataProvider);

		trace = new Trace("", xAxis, yAxis, graphDataProvider);
		trace.setDataProvider(graphDataProvider);
		trace.setTraceType(TraceType.SOLID_LINE);
		trace.setLineWidth(1);
		trace.setPointStyle(PointStyle.POINT);
		trace.setPointSize(4);
		trace.setBaseLine(BaseLine.NEGATIVE_INFINITY);
		trace.setAreaAlpha(100);
		trace.setAntiAliasing(true);
		trace.setErrorBarEnabled(false);
		trace.setYErrorBarType(ErrorBarType.BOTH);
		trace.setXErrorBarType(ErrorBarType.NONE);
		trace.setErrorBarCapWidth(3);

		xyGraph.addTrace(trace);

		xyGraph.setFocusTraversable(true);
		xyGraph.setRequestFocusEnabled(true);

		xyGraph.getPlotArea().addMouseListener(new MouseListener.Stub() {
			@Override
			public void mousePressed(final MouseEvent me) {
				xyGraph.requestFocus();
			}

		});

		xyGraph.addKeyListener(new KeyListener.Stub() {
			@Override
			public void keyPressed(final KeyEvent ke) {
				if ((ke.getState() == SWT.CONTROL) && (ke.keycode == 'z')) {
					xyGraph.getOperationsManager().undo();
				}
				if ((ke.getState() == SWT.CONTROL) && (ke.keycode == 'y')) {
					xyGraph.getOperationsManager().redo();
				}
				if ((ke.getState() == SWT.CONTROL) && (ke.keycode == 'x')) {
					xyGraph.performAutoScale();
				}
				if ((ke.getState() == SWT.CONTROL) && (ke.keycode == 's')) {
					saveSnapshotAction.run();
				}
				if ((ke.getState() == SWT.CONTROL) && (ke.keycode + 'a' - 97 == 't')) {
					switch (xyGraph.getZoomType()) {
					case RUBBERBAND_ZOOM:
						zoomHorizontalAction.run();
						break;
					case HORIZONTAL_ZOOM:
						zoomVerticalAction.run();
						break;
					case VERTICAL_ZOOM:
						zoomInAction.run();
						break;
					case ZOOM_IN:
						zoomOutAction.run();
						break;
					case ZOOM_OUT:
						zoomPanningAction.run();
						break;
					case PANNING:
						zoomNoneAction.run();
						break;
					case NONE:
						zoomRubberbandAction.run();
						break;
					default:
						break;
					}
				}
			}

		});

		return xyGraph;
	}

	@Override
	protected void inputChanged(Object input, Object oldInput) {
		boolean inputChanged = !Objects.equals(this.input, input);

		this.input = input;

		if (inputChanged) {
			newInput(this.input);
		}
	}

	private void newInput(Object input) {
		ICompilationList compilationList = null;

		if (input instanceof ICompilationList) {
			compilationList = (ICompilationList) input;
		}

		IGraphContentProvider contentProvider = getContentProvider();
		Object[] elements = contentProvider.getElements(compilationList);

		graphDataProvider.clearTrace();

		if (elements != null) {
			for (int i = 0; i < elements.length; i++) {
				Object element = elements[i];
				ISample sample = IAdaptable2.adapt(element, ISample.class);
				if (sample != null) {
					graphDataProvider.addSample(sample);
				}
			}
		}

		updateGraph();

	}

	private void updateGraph() {
		IGraphContentProvider contentProvider = getContentProvider();
		if (contentProvider == null) {
			return;
		}

		Object input = getInput();

		String xAxisTitle = contentProvider.getXAxisTitle(input);
		xAxis.setTitle(xAxisTitle);

		String yAxisTitle = contentProvider.getYAxisTitle(input);
		yAxis.setTitle(yAxisTitle);

		String traceTitle = contentProvider.getTraceTitle(input);

		Legend compilationTraceLegend = xyGraph.getLegend(trace);
		compilationTraceLegend.setVisible(traceTitle != null);
		if (traceTitle != null) {
			trace.setName(traceTitle, true);
		}
	}

	@Override
	public IGraphContentProvider getContentProvider() {
		return (IGraphContentProvider) super.getContentProvider();
	}

	@Override
	public void setContentProvider(IContentProvider provider) {
		if (!(provider instanceof IGraphContentProvider)) {
			throw new IllegalArgumentException("provider must be a " + IGraphContentProvider.class.getSimpleName());
		}
		super.setContentProvider(provider);
		updateGraph();
	}

	@Override
	public Control getControl() {
		return control;
	}

	public Action getZoomInAction() {
		return zoomInAction;
	}

	public Action getZoomOutAction() {
		return zoomOutAction;
	}

	public Action getZoomHorizontalAction() {
		return zoomHorizontalAction;
	}

	public Action getZoomVerticalAction() {
		return zoomVerticalAction;
	}

	public Action getZoomPanningAction() {
		return zoomPanningAction;
	}

	public Action getZoomNoneAction() {
		return zoomNoneAction;
	}

	public Action getZoomRubberbandAction() {
		return zoomRubberbandAction;
	}

	public Action getSaveSnapshotAction() {
		return saveSnapshotAction;
	}

	public Action getAutoScaleAction() {
		return autoScaleAction;
	}

	@Override
	protected Widget doFindInputItem(Object element) {
		return null;
	}

	@Override
	protected Widget doFindItem(Object element) {
		return null;
	}

	@Override
	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected List getSelectionFromWidget() {
		return Arrays.asList();
	}

	@Override
	protected void internalRefresh(Object element) {
	}

	@Override
	public void reveal(Object element) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void setSelectionToWidget(List l, boolean reveal) {
	}

	public void setToolTipProvider(IToolTipProvider toolTipProvider) {
		PlotArea plotArea = xyGraph.getPlotArea();

		if (toolTipProvider != null) {
			plotArea.removeMouseMotionListener(mouseMotionAdapter);
		}

		this.toolTipProvider = toolTipProvider;

		if (toolTipProvider != null) {
			plotArea.addMouseMotionListener(mouseMotionAdapter);
		}

	}

}
