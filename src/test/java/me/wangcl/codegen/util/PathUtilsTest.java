package me.wangcl.codegen.util;

import me.wangcl.codegen.util.PathUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathUtilsTest {
	private static final String FOLDER = "c:/__temp__/temp/";
	private static final String FILE = "c:/__temp__/temp.txt";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMakeDirectory() {
		File folder = new File(FOLDER);
		assertFalse(folder.exists());

		PathUtils.makeDirectory(FOLDER);
		assertTrue(folder.exists());

		PathUtils.deleteDirectory(FOLDER);
		assertFalse(folder.exists());
	}

	@Test
	public void testMakeFile() {
		File file = new File(FILE);
		assertFalse(file.exists());

		PathUtils.makeFile(FILE);
		assertTrue(file.exists());

		file.delete();
		assertFalse(file.exists());
		PathUtils.deleteDirectory(file.getParentFile());
	}
}
