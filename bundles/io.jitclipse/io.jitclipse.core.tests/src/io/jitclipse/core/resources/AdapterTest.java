package io.jitclipse.core.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.junit.jupiter.api.Test;

import io.jitclipse.core.tests.commons.AbstractJitProjectTest;

class AdapterTest extends AbstractJitProjectTest {

	@Test
	void adaptIHotspotLOgFile() {
		List<IFile> files = hotspotLogFolder.getFiles();
		List<IHotspotLogFile> hostpotLogFiles = files.stream().map(f -> f.getAdapter(IHotspotLogFile.class))
				.filter(Objects::nonNull).map(IHotspotLogFile.class::cast).collect(Collectors.toList());

		assertEquals(1, hostpotLogFiles.size());
	}

}
