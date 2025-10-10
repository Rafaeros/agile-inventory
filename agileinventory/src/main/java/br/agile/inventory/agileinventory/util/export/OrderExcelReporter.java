package br.agile.inventory.agileinventory.util.export;

import br.agile.inventory.agileinventory.dto.OrderRequest;
import br.agile.inventory.agileinventory.dto.OrderMaterialRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class OrderExcelReporter {

    public static byte[] exportOrders(List<OrderRequest> orders) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ordens de Produção");
            int rowNum = 0;

            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"Order ID", "Order Number", "Código Produto", "Descrição Produto", "Qtd Produção", "Código Material", "Descrição Material", "Qtd Material"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            for (OrderRequest order : orders) {
                if (order.getMaterials() == null || order.getMaterials().isEmpty()) {
                    Row row = sheet.createRow(rowNum++);
                    fillRow(row, order, null);
                } else {
                    for (OrderMaterialRequest material : order.getMaterials()) {
                        Row row = sheet.createRow(rowNum++);
                        fillRow(row, order, material);
                    }
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private static void fillRow(Row row, OrderRequest order, OrderMaterialRequest material) {
        int col = 0;
        row.createCell(col++).setCellValue(order.getOrderId());
        row.createCell(col++).setCellValue(order.getOrderNumber());
        row.createCell(col++).setCellValue(order.getCode());
        row.createCell(col++).setCellValue(order.getDescription());
        row.createCell(col++).setCellValue(order.getQuantity());

        if (material != null) {
            row.createCell(col++).setCellValue(material.getCode());
            row.createCell(col++).setCellValue(material.getDescription());
            row.createCell(col++).setCellValue(material.getQuantity());
        } else {
            row.createCell(col++).setCellValue("");
            row.createCell(col++).setCellValue("");
            row.createCell(col++).setCellValue("");
        }
    }
}
