package com.demo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.entity.EdgeObj;
import com.demo.entity.VertexObj;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class GraphController {

	@RequestMapping("/getAllGraph")
	@ResponseBody
	public Map<String, List<Object>> getAllGraph() throws IOException {
//		List<Object> li1 = new ArrayList<Object>();
//		List<Object> li2 = new ArrayList<Object>();
		Map<String, List<Object>> result = new HashMap<String, List<Object>>();
//		for (int i = 1; i < 11; i++) {
//			li1.add(new Vertex("u" + i, "n" + i, "d" + i));
//			li2.add(new Vertex("u2" + i, "n2" + i, "d2" + i));
//		}
//		result.put("data", li1);
//		result.put("link", li2);
		File file = new File("datas.json");// 定义一个file对象，用来初始化FileReader
		FileReader reader = new FileReader(file);// 定义一个fileReader对象，用来初始化BufferedReader
		BufferedReader bReader = new BufferedReader(reader);// new一个BufferedReader对象，将文件内容读取到缓存
		StringBuilder sb = new StringBuilder();// 定义一个字符串缓存，将字符串存放缓存中
		String s = "";
		while ((s = bReader.readLine()) != null) {// 逐行读取文件内容，不读取换行符和末尾的空格
			sb.append(s + "\n");// 将读取的字符串添加换行符后累加存放在缓存中
		}
		bReader.close();
		String str = sb.toString();

		ObjectMapper mapper = new ObjectMapper();
		List<Object> vertexs = mapper.readValue(str, new TypeReference<List<VertexObj>>() {
		});
		result.put("datas", vertexs);
		
		 file = new File("datas2.json");
		 reader = new FileReader(file);
		 bReader = new BufferedReader(reader);
		 sb = new StringBuilder();
		 s = "";
		while ((s = bReader.readLine()) != null) {
			sb.append(s + "\n");
		}
		bReader.close();
		 str = sb.toString();

		 mapper = new ObjectMapper();
		List<Object> edges = mapper.readValue(str, new TypeReference<List<EdgeObj>>() {
		});
		
		result.put("links", edges);
		
		return result;

	}

}