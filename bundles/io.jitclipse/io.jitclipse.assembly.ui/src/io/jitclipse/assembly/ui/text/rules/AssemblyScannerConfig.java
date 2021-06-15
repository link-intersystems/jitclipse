package io.jitclipse.assembly.ui.text.rules;

import org.eclipse.swt.graphics.RGB;

import com.link_intersystems.eclipse.ui.jface.text.rules.DefaultWhitespaceDetector;
import com.link_intersystems.eclipse.ui.jface.text.rules.PartitioningScannerConfig;
import com.link_intersystems.eclipse.ui.jface.text.rules.TokenConfig;
import com.link_intersystems.eclipse.ui.jface.text.rules.WhitespacePredicateRule;

/**
 * @author rene.link
 *
 */
public class AssemblyScannerConfig extends PartitioningScannerConfig {

	private RGB assemblyBlock = new RGB(127, 127, 159);
	private RGB assemblyAddress = new RGB(63, 63, 191);
	private RGB assemblyAddressRef = new RGB(42, 0, 255);
	private RGB assemblyInstruction = new RGB(127, 0, 85);
	private RGB assemblyInstructionComment = new RGB(63, 127, 95);
	private RGB assemblyComment = new RGB(63, 127, 95);

	public AssemblyScannerConfig() {
		// TODO
		TokenConfig whitespaceTokenConfig = new TokenConfig(
				new WhitespacePredicateRule(new DefaultWhitespaceDetector()));
		addTokenConfig(whitespaceTokenConfig);

		TokenConfig addressTokenConfig = new TokenConfig(new AddressRule(), AddressRule.ADDRESS);
		addressTokenConfig.setForegroundColor(assemblyAddress);
		addTokenConfig(addressTokenConfig);

		TokenConfig addressRefTokenConfig = new TokenConfig(new AddressRule(), AddressRule.ADDRESS_REF);
		addressRefTokenConfig.setForegroundColor(assemblyAddressRef);
		addTokenConfig(addressRefTokenConfig);

		TokenConfig blockConfig = new TokenConfig(new BlockRule(), BlockRule.BLOCK);
		blockConfig.setForegroundColor(assemblyBlock);
		addTokenConfig(blockConfig);

		TokenConfig instructionTokenConfig = new TokenConfig(new InstructionRule(), InstructionRule.INSTRUCTION);
		instructionTokenConfig.setForegroundColor(assemblyInstruction);
		addTokenConfig(instructionTokenConfig);

		TokenConfig instructionCommentTokenConfig = new TokenConfig(new InstructionCommentRule(), InstructionCommentRule.INSTRUCTION_COMMENT);
		instructionCommentTokenConfig.setForegroundColor(assemblyInstructionComment);
		addTokenConfig(instructionCommentTokenConfig);

		TokenConfig commentTokenConfig = new TokenConfig(new CommentRule(), CommentRule.COMMENT);
		commentTokenConfig.setForegroundColor(assemblyComment);
		addTokenConfig(commentTokenConfig);
	}
}
