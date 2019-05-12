package com.suo.projectManagement.service.impl;

import com.suo.projectManagement.vo.UnfoldBlindnessResultDownload;
import com.suo.projectManagement.utils.DateTimeUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by wuxu on 2018/6/1.
 */
public class UnfoldblindnessDlView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String header = "attachment; filename=RandomResult.xlsx";
        httpServletResponse.setContentType("application/ms-excel");
        httpServletResponse.setHeader("Content-disposition", header);
        Sheet excelSheet = workbook.createSheet("随机结果");
        //表头格式
        CellStyle headCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headCellStyle.setFont(font);
        headCellStyle.setWrapText(true);
        setExcelHeader(excelSheet, headCellStyle);

        CellStyle rowCellStyle = workbook.createCellStyle();
        rowCellStyle.setWrapText(true);

        List<UnfoldBlindnessResultDownload> unfoldBlindness = (List<UnfoldBlindnessResultDownload>) map.get("unfoldBlindness");
        setExcelRows(excelSheet, rowCellStyle, unfoldBlindness);
    }
    private void setExcelHeader(Sheet excelSheet, CellStyle cellStyle) {
        Row header = excelSheet.createRow(0);
        Cell cell;
        cell = header.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(cellStyle);

        cell = header.createCell(1);
        cell.setCellValue("受试者");
        cell.setCellStyle(cellStyle);

        cell = header.createCell(2);
        cell.setCellValue("研究者");
        cell.setCellStyle(cellStyle);

        cell = header.createCell(3);
        cell.setCellValue("组号");
        cell.setCellStyle(cellStyle);

        cell = header.createCell(4);
        cell.setCellValue("名称");
        cell.setCellStyle(cellStyle);

        cell = header.createCell(5);
        cell.setCellValue("区块号");
        cell.setCellStyle(cellStyle);

        cell = header.createCell(6);
        cell.setCellValue("块内序号");
        cell.setCellStyle(cellStyle);

        cell = header.createCell(7);
        cell.setCellValue("随机序号");
        cell.setCellStyle(cellStyle);

        cell = header.createCell(8);
        cell.setCellValue("取号时间");
        cell.setCellStyle(cellStyle);
    }
    private void setExcelRows(Sheet excelSheet, CellStyle cellStyle, List<UnfoldBlindnessResultDownload> list) {
        int line = 1;

        for (UnfoldBlindnessResultDownload unfoldBlindnessResultDownload : list) {
            int no = line++;
            Row r = excelSheet.createRow(no);

            Cell cell;
            cell = r.createCell(0);
            cell.setCellValue(unfoldBlindnessResultDownload.getId());
            cell.setCellStyle(cellStyle);

            cell = r.createCell(1);
            cell.setCellValue(unfoldBlindnessResultDownload.getSubjectId());
            cell.setCellStyle(cellStyle);

            cell = r.createCell(2);
            cell.setCellValue(unfoldBlindnessResultDownload.getInvestigatorId());
            cell.setCellStyle(cellStyle);

            cell = r.createCell(3);
            cell.setCellValue(unfoldBlindnessResultDownload.getGroupType());
            cell.setCellStyle(cellStyle);

            cell = r.createCell(4);
            cell.setCellValue(unfoldBlindnessResultDownload.getGroupName());
            cell.setCellStyle(cellStyle);

            cell = r.createCell(5);
            cell.setCellValue(unfoldBlindnessResultDownload.getRandomBlockNum());
            cell.setCellStyle(cellStyle);

            cell = r.createCell(6);
            cell.setCellValue(unfoldBlindnessResultDownload.getOrderInBlock());
            cell.setCellStyle(cellStyle);

            cell = r.createCell(7);
            cell.setCellValue(unfoldBlindnessResultDownload.getRnid());
            cell.setCellStyle(cellStyle);

            cell = r.createCell(8);
            String date = DateTimeUtil.dateToString(unfoldBlindnessResultDownload.getOfferDate());
            cell.setCellValue(date);
            cell.setCellStyle(cellStyle);
        }
    }
}
