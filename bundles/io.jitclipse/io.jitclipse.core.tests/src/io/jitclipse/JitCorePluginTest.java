package io.jitclipse;

import static org.mockito.Mockito.mock;

import org.osgi.framework.BundleContext;

import io.jitclipse.core.JitCorePlugin;
import junit.framework.TestCase;


public class JitCorePluginTest extends TestCase {

	private JitCorePlugin corePlugin;
	private BundleContext bundleContextMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		corePlugin = new JitCorePlugin();
		bundleContextMock = mock(BundleContext.class);
	}

	public void testStart() throws Exception {
		corePlugin.start(bundleContextMock);

		assertEquals(bundleContextMock, corePlugin.getContext());
	}
}