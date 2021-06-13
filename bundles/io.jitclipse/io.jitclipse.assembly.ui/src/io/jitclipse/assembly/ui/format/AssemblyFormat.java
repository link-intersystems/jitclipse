package io.jitclipse.assembly.ui.format;

import java.util.List;

import io.jitclipse.core.model.IAssembly;
import io.jitclipse.core.model.IAssemblyBlock;
import io.jitclipse.core.model.IAssemblyInstruction;

public class AssemblyFormat implements Format<IAssembly> {

	private Format<IAssemblyInstruction> instructionFormat;
	private IFormatConfig formatterConfig;

	public AssemblyFormat() {
		this(new DefaultFormatConfig());
	}

	public AssemblyFormat(IFormatConfig formatConfig) {
		this(formatConfig, new InstructionFormat(formatConfig, new LocalLabelAddressFormatter()));
	}

	public AssemblyFormat(IFormatConfig formatterConfig, Format<IAssemblyInstruction> instructionFormat) {
		this.formatterConfig = formatterConfig;
		this.instructionFormat = instructionFormat;
	}

	public CharSequence format(IAssembly assembly) {
		StringBuilder sb = new StringBuilder();

		sb.append(assembly.getHeader());

		List<IAssemblyBlock> blocks = assembly.getBlocks();

		for (IAssemblyBlock block : blocks) {
			sb.append(block.getTitle());
			sb.append(formatterConfig.getNewLine());

			List<IAssemblyInstruction> instructions = block.getInstructions();

			for (int line = 0; line < instructions.size(); line++) {
				IAssemblyInstruction instruction = instructions.get(line);

				CharSequence formattedInstruction = instructionFormat.format(instruction);
				sb.append(formattedInstruction);
				sb.append(formatterConfig.getNewLine());
			}
		}

		return sb;
	}

}
