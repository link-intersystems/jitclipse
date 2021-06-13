package io.jitclipse.assembly.ui.text.rules;

import org.eclipse.swt.graphics.RGB;

import com.link_intersystems.eclipse.ui.jface.text.rules.DefaultWhitespaceDetector;
import com.link_intersystems.eclipse.ui.jface.text.rules.PartitioningScannerConfig;
import com.link_intersystems.eclipse.ui.jface.text.rules.TokenConfig;
import com.link_intersystems.eclipse.ui.jface.text.rules.WhitespacePredicateRule;

public class AssemblyScannerConfig extends PartitioningScannerConfig {

	private RGB assemblyBlock = new RGB(25, 100, 25);
	private RGB assemblyAddress = new RGB(50, 50, 200);
	private RGB assemblyAddressRef = new RGB(110, 110, 255);
	private RGB assemblyInstruction = new RGB(200, 50, 50);
	private RGB assemblyComment = new RGB(150, 150, 150);

	public AssemblyScannerConfig() {

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

		TokenConfig commentTokenConfig = new TokenConfig(new CommentRule(), CommentRule.COMMENT);
		commentTokenConfig.setForegroundColor(assemblyComment);
		addTokenConfig(commentTokenConfig);
	}
}
