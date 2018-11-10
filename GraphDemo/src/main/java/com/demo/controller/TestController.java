package com.demo.controller;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.service.GraphService;

@RestController
public class TestController {
	
	@Autowired
	private GraphService graphService;
	
	@RequestMapping("/testTurn")
	@ResponseBody
	public Map<String, String> testTurn(@RequestBody Map<String, Object> reqMap) {
		Map<String, String> properttList = (Map<String, String>) reqMap.get("propertyList");
		Set<Entry<String, String>> entrySet = properttList.entrySet();
		for (Entry<String, String> entry : entrySet) {
			System.out.println(entry.getKey() +  "  " + entry.getValue());
		}
		return properttList;
	}
	
	@RequestMapping("/testLineSearch")
	@ResponseBody
	public Map<String, Object> testLineSearch(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> result = graphService.secondSearch(reqMap);
		return result;
	}

}
