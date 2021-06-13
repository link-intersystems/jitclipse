package io.jitclipse.assembly.ui.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

public class AssemblyDocument {

	private IDocument document;

	public AssemblyDocument(String classpathResource) throws IOException {
		InputStream assemblyInputStream = AssemblyDocument.class.getResourceAsStream(classpathResource);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assemblyInputStream));

		StringBuilder sb = new StringBuilder();
		char[] buff = new char[4096];
		int read = -1;

		while ((read = bufferedReader.read(buff)) > -1) {
			sb.append(buff, 0, read);
		}

		document = new Document(sb.toString());
	}

	public IDocument getDocument() {
		return document;
	}

}
