package com.demo.service;

import java.util.Map;

import javax.servlet.ServletOutputStream;

public interface ExportExcelService {

	public void exportExcel(ServletOutputStream out, Map<String,Object> reqMap);
	
	public void exportExcel_test(ServletOutputStream out/*, Map<String,Object> reqMap*/);
}
