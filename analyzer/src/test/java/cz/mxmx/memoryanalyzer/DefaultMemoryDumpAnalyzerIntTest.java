package cz.mxmx.memoryanalyzer;

import com.google.common.collect.Lists;
import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import cz.mxmx.memoryanalyzer.model.ClassDump;
import cz.mxmx.memoryanalyzer.model.MemoryDump;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.mxmx.memoryanalyzer.TestHelper.NAMESPACE;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DefaultMemoryDumpAnalyzerIntTest {
	private MemoryDumpAnalyzer analyzer;

	@Before
	public void setUp() {
		this.analyzer = TestHelper.getMemoryDumpAnalyzer();
	}

	@Test
	public void testNamespaceCount() throws FileNotFoundException, MemoryDumpAnalysisException {
		Set<String> namespaces = this.analyzer.getNamespaces();
		assertThat(namespaces.size(), is(55));
	}

	@Test
	public void testNamespaceContains() throws FileNotFoundException, MemoryDumpAnalysisException {
		Set<String> namespaces = this.analyzer.getNamespaces();
		assertThat(namespaces, hasItem(NAMESPACE));
	}

	@Test
	public void testNamespaceClasses() throws FileNotFoundException, MemoryDumpAnalysisException {
		MemoryDump dump = this.analyzer.analyze(Lists.newArrayList(NAMESPACE));
		assertThat(dump.getClasses().size(), is(710));
	}

	@Test
	public void testNamespaceInstances() throws FileNotFoundException, MemoryDumpAnalysisException {
		MemoryDump dump = this.analyzer.analyze(Lists.newArrayList(NAMESPACE));
		assertThat(dump.getInstances().size(), is(7707));
	}

	@Test
	public void testNamespaceUserClasses() throws FileNotFoundException, MemoryDumpAnalysisException {
		MemoryDump dump = this.analyzer.analyze(Lists.newArrayList(NAMESPACE));
		assertThat(dump.getUserClasses().size(), is(3));
	}

	@Test
	public void testNamespaceUserClassesContains() throws FileNotFoundException, MemoryDumpAnalysisException {
		MemoryDump dump = this.analyzer.analyze(Lists.newArrayList(NAMESPACE));
		assertThat(dump
						.getUserClasses()
						.values()
						.stream()
						.map(ClassDump::getName)
						.collect(Collectors.toList()),
				hasItems("cz.mxmx.memoryanalyzer.example.App", "cz.mxmx.memoryanalyzer.example.Child", "cz.mxmx.memoryanalyzer.example.Parent")
		);
	}

	@Test
	public void testNamespaceUserInstances() throws FileNotFoundException, MemoryDumpAnalysisException {
		MemoryDump dump = this.analyzer.analyze(Lists.newArrayList(NAMESPACE));
		assertThat(dump.getUserInstances().size(), is(111));
	}

}