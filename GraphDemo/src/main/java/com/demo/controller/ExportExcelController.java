package com.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.service.ExportExcelService;

@Controller
public class ExportExcelController {
	@Autowired
	ExportExcelService exportExcelService;

	@RequestMapping(value = "/exportExcel", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String exportExcel(HttpServletResponse response, @RequestBody(required = false) Map<String, Object> reqMap) {
		response.setContentType("application/binary;charset=UTF-8");
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
			exportExcelService.exportExcel_test(out);
			return "export success";
		} catch (Exception e) {
			e.printStackTrace();
			return "导出信息失败";
		}
	}

}
