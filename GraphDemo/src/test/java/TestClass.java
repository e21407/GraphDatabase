import java.io.File;

import org.json.JSONObject;
import org.junit.Test;

import com.demo.tool.FileTool;

public class TestClass {

	private static final String DEFAULT_CONFIG_FILE = "src" + File.separator + "main" + File.separator + "resources"
			+ File.separator + "graphbase.properties";

	@Test
	public void Testdir() {
		String dir = System.getProperty("user.dir") + File.separator + DEFAULT_CONFIG_FILE;
		System.out.println(dir);

	}

	@Test
	public void testJson() {
		File JsonFile = new File("temp.json");
		String strFromFile = FileTool.getStrFromFile(JsonFile);
		JSONObject jsonObject = new JSONObject(strFromFile);
		Object object = jsonObject.get("msg");
		System.out.println(object);
	}

}
