package io.jitclipse.ui.hotspot;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * A {@link Consumer} that buffers values and delegates these values to the
 * target {@link Consumer} when it is available;
 *
 * @author rene.link
 *
 * @param <T>
 */
public class DeferredConsumer<T> implements Consumer<T> {

	private int valueBufferSize = Integer.MAX_VALUE;

	private Consumer<T> targetConsumer;
	private Queue<T> lastValues = new LinkedList<>();

	@Override
	public synchronized void accept(T t) {
		if (targetConsumer == null) {
			while (this.lastValues.size() > valueBufferSize) {
				this.lastValues.poll();
			}
			this.lastValues.offer(t);
		} else {
			this.lastValues = null;
			targetConsumer.accept(t);
		}
	}

	/**
	 * Sets the size of the values that are buffered until the target is available.
	 * If the buffer is full the oldest entry is removed and the new one is
	 * inserted.
	 *
	 * @param valueBufferSize
	 */
	public void setValueBufferSize(int valueBufferSize) {
		this.valueBufferSize = valueBufferSize;
	}

	public synchronized void setTargetConsumer(Consumer<T> targetConsumer) {
		this.targetConsumer = targetConsumer;

		if (this.targetConsumer != null) {
			lastValues.forEach(this.targetConsumer::accept);
			lastValues.clear();
		}
	}

}
