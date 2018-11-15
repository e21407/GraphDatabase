package com.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.synth.SynthSpinnerUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.demo.service.ExportExcelService;
import com.demo.tool.StringTool;

@Controller
public class ExportExcelController {
	@Autowired
	ExportExcelService exportExcelService;

	@RequestMapping(value = "/exportExcel", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void exportExcel(HttpServletResponse response, @RequestBody(required = false) Map<String, Object> reqMap) {
		System.out.println("===========================>Controller: /exportExcel");
		response.setContentType("application/binary;charset=UTF-8");
		//交付时候启用
//		if(reqMap == null) {
//			return "导出失败，请求节点id列表为空";
//		}
		try {
			ServletOutputStream out = response.getOutputStream();
			try {
				// 设置文件头：最后一个参数是设置下载文件名
				response.setHeader("Content-Disposition",
						"attachment;fileName=" + URLEncoder.encode("Graph.xls", "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
//			exportExcelService.exportExcel(out, reqMap);
			exportExcelService.exportExcel_test(out,reqMap);
//			return "export success";
		} catch (Exception e) {
			e.printStackTrace();
//			return "export fail";
		}
	}

}
