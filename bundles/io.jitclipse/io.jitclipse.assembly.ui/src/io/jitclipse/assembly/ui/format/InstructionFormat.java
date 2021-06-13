package io.jitclipse.assembly.ui.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import io.jitclipse.core.model.IAssemblyInstruction;

public class InstructionFormat implements Format<IAssemblyInstruction> {

	private static final String S_SAFEPOINT_POLL = "{poll}";
	private static final String S_SAFEPOINT_POLL_RETURN = "{poll_return}";
	private static final String HEX_PREFIX = "0x";
	private static final String HEX_POSTFIX = "h";

	private SortedSet<Long> addresses = new TreeSet<>();
	private final Map<Long, Short> labels = new HashMap<>();
	private long lowest = Long.MAX_VALUE;
	private long highest;

	private IFormatConfig formatConfig;
	private Format<Long> addressFormat;

	public InstructionFormat(IFormatConfig formatConfig, Format<Long> addressFormat) {
		this.formatConfig = formatConfig;
		this.addressFormat = addressFormat;
	}

	public void buildLabels() {
		short next = 0;

		if (lowest != Long.MAX_VALUE) {
			for (Long a : addresses.subSet(lowest, highest + 1)) {
				labels.put(a, next++);
			}
		}

		addresses = null;
	}

	@Override
	public CharSequence format(IAssemblyInstruction instruction) {
		StringBuilder sb = new StringBuilder();

		List<String> commentLines = instruction.getCommentLines();

		if (commentLines.size() == 0) {
			sb.append(doFormat(instruction, 0));
		} else {
			for (int i = 0; i < commentLines.size(); i++) {
				sb.append(doFormat(instruction, i));
			}
		}

		return sb;
	}

	private String doFormat(IAssemblyInstruction instruction, int line) {
		StringBuilder builder = new StringBuilder();

		String annotation = instruction.getAnnotation();

		builder.append(StringUtil.alignLeft(annotation, 0));

		long address = instruction.getAddress();
		CharSequence formattedAddress = addressFormat.format(address);
		builder.append(formattedAddress);

		builder.append(':').append(' ');
		builder.append(formatPrefixes(instruction));

		String mnemonic = instruction.getMnemonic();
		builder.append(mnemonic);

		if (formatConfig.isUseLocalLabels()) {
			CharSequence formattedOperands = formatOperands(instruction);
			builder.append(formattedOperands);
		} else {
			List<String> operands = instruction.getOperands();
			if (operands.size() > 0) {
				builder.append(' ');

				for (String op : operands) {
					builder.append(op).append(',');
				}

				builder.deleteCharAt(builder.length() - 1);
			}
		}

		int lineLength = builder.length();

		List<String> commentLines = instruction.getCommentLines();

		if (commentLines.size() > 0) {
			String comment = commentLines.get(line);

			if (line == 0) {
				// first comment on same line as instruction
				builder.append("  ").append(comment);
			} else {
				// later comments on own line
				builder.delete(0, builder.length());
				builder.append(StringUtil.repeat(' ', lineLength + 2));
				builder.append(comment);
			}

			if (comment.contains(S_SAFEPOINT_POLL) || comment.contains(S_SAFEPOINT_POLL_RETURN)) {
				builder.append(" *** SAFEPOINT POLL ***");
			}

		}

		return StringUtil.rtrim(builder.toString());
	}

	private CharSequence formatPrefixes(IAssemblyInstruction instruction) {
		StringBuilder builder = new StringBuilder();

		List<String> prefixes = instruction.getPrefixes();

		if (!prefixes.isEmpty()) {
			for (String prefix : prefixes) {
				builder.append(prefix);
				builder.append(' ');
			}
		}

		return builder;
	}

	public CharSequence formatOperands(IAssemblyInstruction instruction)

	{
		StringBuilder builder = new StringBuilder();

		final Long address = instructionToLabel(instruction);

		final Short label = labels.get(address);

		if (label != null) {
			builder.append(' ');
			builder.append(String.format("L%04x", label));
		} else {
			final List<String> operands = instruction.getOperands();

			if (operands.size() > 0) {
				builder.append(' ');

				for (String op : operands) {
					builder.append(op).append(',');
				}

				builder.deleteCharAt(builder.length() - 1);
			}
		}

		return builder;
	}

	private Long instructionToLabel(IAssemblyInstruction instruction) {
		final List<String> operands = instruction.getOperands();

		if (instruction.getMnemonic().startsWith("j") && operands.size() == 1) {
			try {
				return getValueFromAddress(operands.get(0));
			} catch (NumberFormatException nfe) {
				// could be Intel format jump to Stub:: reference
			}
		}

		return null;
	}

	public static long getValueFromAddress(final String address) {
		long addressValue = 0;

		if (address != null) {
			String trimmedAddress = address.trim();

			if (trimmedAddress.startsWith(HEX_PREFIX)) {
				trimmedAddress = trimmedAddress.substring(HEX_PREFIX.length());
			}

			if (trimmedAddress.endsWith(HEX_POSTFIX)) {
				trimmedAddress = trimmedAddress.substring(0, trimmedAddress.length() - 1);
			}

			addressValue = Long.parseLong(trimmedAddress, 16);
		}
		return addressValue;
	}
}
