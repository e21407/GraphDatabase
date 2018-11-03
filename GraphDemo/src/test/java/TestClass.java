import java.io.File;

import org.junit.Test;

public class TestClass {

	private static final String DEFAULT_CONFIG_FILE = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "graphbase.properties";

	@Test
	public void Testdir() {
		String dir = System.getProperty("user.dir") + File.separator + DEFAULT_CONFIG_FILE;
		System.out.println(dir);

	}

}
