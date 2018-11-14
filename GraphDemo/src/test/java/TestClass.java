import java.io.File;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
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
		File JsonFile = new File("simulationData.json");
		String strFromFile = FileTool.getStrFromFile(JsonFile);
		JSONObject inJsonObject = JSONObject.parseObject(strFromFile);
		System.out.println(inJsonObject.toString());
	}
	
	@Test
	public void testJson2() {
		JSONObject jObject = new JSONObject();
		jObject.put("985", "211");
		jObject.put("985", "214");
		System.out.println(jObject.toString());
	}
	

}
