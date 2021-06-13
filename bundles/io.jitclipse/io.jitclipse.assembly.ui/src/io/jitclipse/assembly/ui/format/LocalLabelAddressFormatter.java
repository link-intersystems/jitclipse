package io.jitclipse.assembly.ui.format;

public class LocalLabelAddressFormatter implements Format<Long>  {

	@Override
	public CharSequence format(Long address) {
		StringBuilder builder = new StringBuilder();

		builder.append("0x");
		builder.append(StringUtil.pad(Long.toHexString(address), 16, '0', true));

		return builder;
	}

}
