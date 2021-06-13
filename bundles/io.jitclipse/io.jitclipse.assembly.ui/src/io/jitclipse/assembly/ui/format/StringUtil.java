package io.jitclipse.assembly.ui.format;

public final class StringUtil {

	private static final char SPACE = ' ';

	private StringUtil() {
	}

	public static String repeat(char c, int count) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < count; i++) {
			builder.append(c);
		}

		return builder.toString();
	}

	public static String rtrim(String string) {
		return string.replaceAll("\\s+$", "");
	}

	public static String alignRight(long num, int width) {
		return pad(Long.toString(num), width, SPACE, true);
	}

	public static String alignRight(String str, int width) {
		return pad(str, width, SPACE, true);
	}

	public static String alignLeft(long num, int width) {
		return pad(Long.toString(num), width, SPACE, false);
	}

	public static String alignLeft(String str, int width) {
		return pad(str, width, SPACE, false);
	}

	public static String pad(String str, int width, char padding, boolean left) {
		StringBuilder sb = new StringBuilder();

		if (str != null) {
			int len = str.length();

			if (!left) {
				sb.append(str);
			}

			if (len < width) {
				for (int i = 0; i < width - len; i++) {
					sb.append(padding);
				}
			}

			if (left) {
				sb.append(str);
			}
		}

		return sb.toString();
	}

}
