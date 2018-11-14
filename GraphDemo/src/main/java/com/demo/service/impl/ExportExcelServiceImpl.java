package com.demo.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.dao.GraphApi;
import com.demo.service.ExportExcelService;
import com.huawei.request.EdgeQueryRspObj;
import com.huawei.request.PropertyRspObj;
import com.huawei.request.VertexQueryRspObj;

@Service("exportExcelService")
public class ExportExcelServiceImpl implements ExportExcelService {
	GraphApi api = GraphApi.getGraphApi_test();

	@Override
	public void exportExcel(ServletOutputStream out, Map<String, Object> reqMap) {
		// 第一步，创建一个workbook，对应一个Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet hssfSheet = workbook.createSheet("vertex");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = hssfSheet.createRow(0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
		// 居中样式
		hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置第一行表头
		String[] titles = { "id", "vertexLabel", "property" };
		HSSFCell hssfCell = null;
		for (int i = 0; i < titles.length; i++) {
			hssfCell = row.createCell(i);// 列索引从0开始
			hssfCell.setCellValue(titles[i]);// 列名1
			hssfCell.setCellStyle(hssfCellStyle);// 列居中显示
		}

		// 获取点的id列表
		List<String> vertexIdList = (List<String>) reqMap.get("vertexIds");
		// 点信息写入表格
		for (int i = 0; i < vertexIdList.size(); i++) {
			String vId = vertexIdList.get(i);
			if (vId != null && vId.startsWith("property")) {
				continue;
			}
			VertexQueryRspObj vertex = api.queryVertex(vId);
			if (null == vertex) {
				continue;
			}
			row = hssfSheet.createRow(i + 1);
			if (null != vertex.getId()) {
				row.createCell(0).setCellValue(vertex.getId());
			}
			if (null != vertex.getVertexLabel()) {
				row.createCell(1).setCellValue(vertex.getVertexLabel());
			}
			List<PropertyRspObj> propertyList = vertex.getPropertyList();
			if (null != propertyList) {
				String allProperty = "";
				for (PropertyRspObj propertyRspObj : propertyList) {
					// name:value, name:value,...
					allProperty += propertyRspObj.getName() + ":" + propertyRspObj.getValue() + ", ";
				}
				row.createCell(2).setCellValue(allProperty);
			}
		}

		hssfSheet = workbook.createSheet("edge");
		row = hssfSheet.createRow(0);
		String[] egdeTitles = { "id", "edgeLabel", "inVertexId", "outVertexId", "property" };
		hssfCell = null;
		for (int i = 0; i < egdeTitles.length; i++) {
			hssfCell = row.createCell(i);// 列索引从0开始
			hssfCell.setCellValue(egdeTitles[i]);// 列名1
			hssfCell.setCellStyle(hssfCellStyle);// 列居中显示
		}
		// 获取点的id列表
		List<String> edgeIdList = (List<String>) reqMap.get("edgeIds");
		// 边信息写入表格
		for (int i = 0; i < edgeIdList.size(); i++) {
			String eId = edgeIdList.get(i);
			if (eId != null && eId.startsWith("property")) {
				continue;
			}
			EdgeQueryRspObj edge = api.queryEdge(eId);
			if (null == edge) {
				continue;
			}
			row = hssfSheet.createRow(i + 1);
			if (null != edge.getId()) {
				row.createCell(0).setCellValue(edge.getId());
			}
			if (null != edge.getEdgeLabel()) {
				row.createCell(1).setCellValue(edge.getEdgeLabel());
			}
			if (null != edge.getInVertexId()) {
				row.createCell(2).setCellValue(edge.getInVertexId());
			}
			if (null != edge.getOutVertexId()) {
				row.createCell(3).setCellValue(edge.getOutVertexId());
			}
			List<PropertyRspObj> propertyList = edge.getPropertyList();
			if (null != propertyList) {
				String allProperty = "";
				for (PropertyRspObj propertyRspObj : propertyList) {
					// name:value, name:value,...
					allProperty += propertyRspObj.getName() + ":" + propertyRspObj.getValue() + ", ";
				}
				row.createCell(4).setCellValue(allProperty);
			}
		}
		// 第七步，将文件输出到客户端浏览器
		try {
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 假数据测试方法
	@Override
	public void exportExcel_test(ServletOutputStream out, Map<String, Object> reqMap) {
//		List<String> vertexIdList = (List<String>) reqMap.get("vertexIds");
//		List<String> edgeIdList = (List<String>) reqMap.get("edgeIds");
//		System.out.println(vertexIdList);
//		System.out.println(edgeIdList);
		// 第一步，创建一个workbook，对应一个Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet hssfSheet = workbook.createSheet("vertex");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = hssfSheet.createRow(0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
		// 居中样式
		hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置第一行表头
		String[] titles = { "id", "vertexLabel", "property" };
		HSSFCell hssfCell = null;
		for (int i = 0; i < titles.length; i++) {
			hssfCell = row.createCell(i);// 列索引从0开始
			hssfCell.setCellValue(titles[i]);// 列名1
			hssfCell.setCellStyle(hssfCellStyle);// 列居中显示
		}

		// 制造数据
		VertexQueryRspObj v1 = new VertexQueryRspObj();
		v1.setId("111");
		v1.setVertexLabel("label1");
		List<PropertyRspObj> propertyList1 = new ArrayList<PropertyRspObj>();
		PropertyRspObj p1 = new PropertyRspObj();
		p1.setName("name1");
		p1.setValue("value1");
		PropertyRspObj p2 = new PropertyRspObj();
		p2.setName("name2");
		p2.setValue("value2");
		propertyList1.add(p1);
		propertyList1.add(p2);
		v1.setPropertyList(propertyList1);

		VertexQueryRspObj v2 = new VertexQueryRspObj();
		v2.setId("222");
		v2.setVertexLabel("label2");
		List<PropertyRspObj> propertyList2 = new ArrayList<PropertyRspObj>();
		PropertyRspObj p3 = new PropertyRspObj();
		p3.setName("name3");
		p3.setValue("value3");
		PropertyRspObj p4 = new PropertyRspObj();
		p4.setName("name4");
		p4.setValue("value4");
		propertyList2.add(p3);
		propertyList2.add(p4);
		v2.setPropertyList(propertyList2);

		List<VertexQueryRspObj> vertexList = new ArrayList<VertexQueryRspObj>();
		vertexList.add(v1);
		vertexList.add(v2);
		// 点信息写入表格

		for (int i = 0; i < vertexList.size(); i++) {
			VertexQueryRspObj vertex = vertexList.get(i);
			row = hssfSheet.createRow(i + 1);
			if (null != vertex.getId()) {
				row.createCell(0).setCellValue(vertex.getId());
			}
			if (null != vertex.getVertexLabel()) {
				row.createCell(1).setCellValue(vertex.getVertexLabel());
			}
			List<PropertyRspObj> propertyList = vertex.getPropertyList();
			if (null != propertyList) {
				String allProperty = "";
				for (PropertyRspObj propertyRspObj : propertyList) {
					// name:value, name:value,...
					allProperty += propertyRspObj.getName() + ":" + propertyRspObj.getValue() + ", ";
				}
				row.createCell(2).setCellValue(allProperty);
			}
		}

		hssfSheet = workbook.createSheet("edge");
		row = hssfSheet.createRow(0);
		String[] egdeTitles = { "id", "edgeLabel", "inVertexId", "outVertexId", "property" };
		hssfCell = null;
		for (int i = 0; i < egdeTitles.length; i++) {
			hssfCell = row.createCell(i);// 列索引从0开始
			hssfCell.setCellValue(egdeTitles[i]);// 列名1
			hssfCell.setCellStyle(hssfCellStyle);// 列居中显示
		}

		EdgeQueryRspObj e1 = new EdgeQueryRspObj();
		e1.setId("333");
		e1.setEdgeLabel("label3");
		e1.setInVertexId("333");
		e1.setOutVertexId("444");
		e1.setPropertyList(propertyList1);

		EdgeQueryRspObj e2 = new EdgeQueryRspObj();
		e2.setId("555");
		e2.setEdgeLabel("label4");
		e2.setInVertexId("555");
		e2.setOutVertexId("666");
		e2.setPropertyList(propertyList2);
		List<EdgeQueryRspObj> edgeList = new ArrayList<EdgeQueryRspObj>();
		edgeList.add(e1);
		edgeList.add(e2);

		// 边信息写入表格
		for (int i = 0; i < edgeList.size(); i++) {
			EdgeQueryRspObj edge = edgeList.get(i);
			row = hssfSheet.createRow(i + 1);
			if (null != edge.getId()) {
				row.createCell(0).setCellValue(edge.getId());
			}
			if (null != edge.getEdgeLabel()) {
				row.createCell(1).setCellValue(edge.getEdgeLabel());
			}
			if (null != edge.getInVertexId()) {
				row.createCell(2).setCellValue(edge.getInVertexId());
			}
			if (null != edge.getOutVertexId()) {
				row.createCell(3).setCellValue(edge.getOutVertexId());
			}
			List<PropertyRspObj> propertyList = edge.getPropertyList();
			if (null != propertyList) {
				String allProperty = "";
				for (PropertyRspObj propertyRspObj : propertyList) {
					// name:value, name:value,...
					allProperty += propertyRspObj.getName() + ":" + propertyRspObj.getValue() + ", ";
				}
				row.createCell(4).setCellValue(allProperty);
			}
		}
		// 第七步，将文件输出到客户端浏览器
		try {
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
