package io.jitclipse.core.launch.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProcessOutputGrabber implements IOutputGrabber {

	private static final int EOF = -1;
	private Duration wait = Duration.ofSeconds(5L);
	private boolean grabErrorOutput = true;

	@Override
	public void setWait(Duration wait) {
		this.wait = Objects.requireNonNull(wait);
	}

	@Override
	public void setGrabErrorOutput(boolean grabErrorOutput) {
		this.grabErrorOutput = grabErrorOutput;
	}

	@Override
	public String grabOutput(File exec, String... args) throws IOException {
		List<String> completeArgs = new ArrayList<>();
		completeArgs.add(exec.getAbsolutePath());
		completeArgs.addAll(Arrays.asList(args));

		ProcessBuilder pb = new ProcessBuilder(completeArgs);

		File outputFile = File.createTempFile(exec.getName(), "output");
		pb.redirectOutput(outputFile);
		if (grabErrorOutput) {
			pb.redirectError(outputFile);
		}

		StringBuilder sb = new StringBuilder();

		try {
			Process process = pb.start();
			LocalDateTime processStart = LocalDateTime.now();

			while (process.isAlive() && Duration.between(processStart, LocalDateTime.now()).compareTo(wait) < 0) {
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					break;
				}
			}

			if (process.isAlive()) {
				process.destroyForcibly();
			}

			char[] buff = new char[4096];

			try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile)))) {
				int read = EOF;
				while ((read = reader.read(buff)) != EOF) {
					sb.append(buff, 0, read);
				}
			}

		} finally {
			outputFile.delete();
		}

		return sb.toString();
	}

}