package io.jitclipse.assembly.ui.views;

import static io.jitclipse.assembly.ui.text.rules.AddressRule.ADDRESS;
import static io.jitclipse.assembly.ui.text.rules.AddressRule.ADDRESS_REF;
import static io.jitclipse.assembly.ui.text.rules.BlockRule.BLOCK;
import static io.jitclipse.assembly.ui.text.rules.CommentRule.COMMENT;
import static io.jitclipse.assembly.ui.text.rules.InstructionRule.INSTRUCTION;
import static io.jitclipse.assembly.ui.views.BufferedOutputStreamFlushDocument.BLOCK_LINE;
import static io.jitclipse.assembly.ui.views.BufferedOutputStreamFlushDocument.COMMENT_LINE;
import static io.jitclipse.assembly.ui.views.BufferedOutputStreamFlushDocument.MOV_INSTR;
import static io.jitclipse.assembly.ui.views.BufferedOutputStreamFlushDocument.MULTI_INSTR_LINE;
import static io.jitclipse.assembly.ui.views.BufferedOutputStreamFlushDocument.SIMPLE_INSTR_LINE;

import java.io.IOException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jitclipse.assembly.ui.text.rules.AssemblyScannerConfig;

class AssemblyPartitionScannerTest {

	private BufferedOutputStreamFlushDocument bufferedOutputStreamFlushDocument;
	private ScannerTestDriver scannerTestDriver;
	private IPartitionTokenScanner scanner;

	@BeforeEach
	public void setup() throws IOException {
		AssemblyScannerConfig assemblyScannerConfig = new AssemblyScannerConfig();

		bufferedOutputStreamFlushDocument = new BufferedOutputStreamFlushDocument();
		scanner = assemblyScannerConfig.configure(bufferedOutputStreamFlushDocument.getDocument());
		bufferedOutputStreamFlushDocument.configureScanner(scanner);

		DocumentScan documentScan = new DocumentScan(scanner, bufferedOutputStreamFlushDocument.getDocument());
		scannerTestDriver = new ScannerTestDriver(documentScan);
	}

	@Test
	void detectMovInstruction() {
		bufferedOutputStreamFlushDocument.configureScanner(scanner, MOV_INSTR);

		scannerTestDriver.expectNextToken("mov", INSTRUCTION);
	}

	@Test
	void detectSimpleInstrLine() throws BadLocationException {
		bufferedOutputStreamFlushDocument.configureScanner(scanner, SIMPLE_INSTR_LINE);

		scannerTestDriver.expectNextToken("0x0000028f8ff1b340", ADDRESS);
		scannerTestDriver.expectNextToken(":");
		scannerTestDriver.exprectWhitespace();
		scannerTestDriver.expectNextToken("mov", INSTRUCTION);
	}

	@Test
	void detectMultiLine() throws BadLocationException {
		bufferedOutputStreamFlushDocument.configureScanner(scanner, MULTI_INSTR_LINE);

		scannerTestDriver.expectNextToken("0x0000028f8ff1b340", ADDRESS);
		scannerTestDriver.expectNextToken(":");
		scannerTestDriver.exprectWhitespace();
		scannerTestDriver.expectNextToken("mov", INSTRUCTION);
		scannerTestDriver.exprectWhitespace();
		scannerTestDriver.expectNextToken("0x8", ADDRESS_REF);
		scannerTestDriver.expectNextTokenString("(%rdx),%r10d");
		scannerTestDriver.exprectWhitespace();

		scannerTestDriver.expectNextToken("0x0000028f8ff1b344", ADDRESS);
		scannerTestDriver.expectNextToken(":");
		scannerTestDriver.exprectWhitespace();
		scannerTestDriver.expectNextToken("movabs", INSTRUCTION);
		scannerTestDriver.exprectWhitespace();
		scannerTestDriver.expectNextToken("$");
		scannerTestDriver.expectNextToken("0x800000000", ADDRESS_REF);
		scannerTestDriver.expectNextTokenString(",%r12");
	}

	@Test
	void detectComment() throws BadLocationException {
		bufferedOutputStreamFlushDocument.configureScanner(scanner, COMMENT_LINE);

		scannerTestDriver.expectNextToken("0x0000028f8ff1b357", ADDRESS);
		scannerTestDriver.expectNextToken(":");
		scannerTestDriver.exprectWhitespace();
		scannerTestDriver.expectNextToken("jne", INSTRUCTION);
		scannerTestDriver.exprectWhitespace();
		scannerTestDriver.expectNextToken("0x0000028f88397480", ADDRESS_REF);
		scannerTestDriver.exprectWhitespace();

		scannerTestDriver.expectNextToken(";   {runtime_call ic_miss_stub}", COMMENT);
	}

	@Test
	void detectBlock() throws BadLocationException {
		bufferedOutputStreamFlushDocument.configureScanner(scanner, BLOCK_LINE);

		scannerTestDriver.expectNextToken("[Entry Point]", BLOCK);
	}

}
