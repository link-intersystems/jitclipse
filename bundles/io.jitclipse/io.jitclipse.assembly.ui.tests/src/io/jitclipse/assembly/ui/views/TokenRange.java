package io.jitclipse.assembly.ui.views;

class TokenRange {

	private int start;
	private int end;

	public TokenRange(int start, int end) {
		if (end < start) {
			throw new IllegalArgumentException("start must be lower than end");
		}
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}
}