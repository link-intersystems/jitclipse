package io.jitclipse.assembly.ui.format;

class DefaultAddressFormatter implements Format<Long> {

	@Override
	public CharSequence format(Long address) {
		return "0x" + StringUtil.pad(Long.toHexString(address), 16, '0', true);
	}
}