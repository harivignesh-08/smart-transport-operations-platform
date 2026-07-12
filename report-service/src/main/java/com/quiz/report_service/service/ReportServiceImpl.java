package com.quiz.report_service.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.quiz.report_service.client.AnalyticsClient;
import com.quiz.report_service.dto.DashboardResponse;
import com.quiz.report_service.dto.KPIsResponse;
import com.quiz.report_service.dto.StatisticsResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final AnalyticsClient analyticsClient;

    public ReportServiceImpl(AnalyticsClient analyticsClient) {
        this.analyticsClient = analyticsClient;
    }

    private DashboardResponse getSafeDashboard(String token) {
        try {
            return analyticsClient.getDashboard(token);
        } catch (Exception e) {
            log.error("Failed to fetch dashboard metrics: {}", e.getMessage());
            return new DashboardResponse(0, 0, 0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
    }

    private StatisticsResponse getSafeStatistics(String token) {
        try {
            return analyticsClient.getStatistics(token);
        } catch (Exception e) {
            log.error("Failed to fetch statistics metrics: {}", e.getMessage());
            return new StatisticsResponse(0, 0.0, 0.0, BigDecimal.ZERO, BigDecimal.ZERO, 0.0);
        }
    }

    private KPIsResponse getSafeKPIs(String token) {
        try {
            return analyticsClient.getKPIs(token);
        } catch (Exception e) {
            log.error("Failed to fetch KPI metrics: {}", e.getMessage());
            return new KPIsResponse(0.0, 0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public byte[] generatePDFReport(String token) {
        DashboardResponse dashboard = getSafeDashboard(token);
        StatisticsResponse stats = getSafeStatistics(token);
        KPIsResponse kpis = getSafeKPIs(token);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Font styles
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Font.BOLD);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Font.BOLD);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.BOLD);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            // Title
            Paragraph title = new Paragraph("Smart Transport Operations Platform - Fleet Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(5);
            document.add(title);

            Paragraph date = new Paragraph("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), subtitleFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20);
            document.add(date);

            // 1. Dashboard section
            document.add(new Paragraph("1. Dashboard Metrics Summary", sectionFont));
            document.add(new Paragraph(" ", regularFont));

            PdfPTable tableDash = new PdfPTable(2);
            tableDash.setWidthPercentage(100);
            tableDash.setSpacingAfter(15);

            addTableCell(tableDash, "Metric", boldFont);
            addTableCell(tableDash, "Value", boldFont);

            addTableCell(tableDash, "Total Vehicles / Active / Available", regularFont);
            addTableCell(tableDash, String.format("%d / %d / %d", dashboard.totalVehicles(), dashboard.activeVehicles(), dashboard.availableVehicles()), regularFont);

            addTableCell(tableDash, "Total Drivers / Available", regularFont);
            addTableCell(tableDash, String.format("%d / %d", dashboard.totalDrivers(), dashboard.availableDrivers()), regularFont);

            addTableCell(tableDash, "Ongoing Active Trips", regularFont);
            addTableCell(tableDash, String.valueOf(dashboard.activeTripsCount()), regularFont);

            addTableCell(tableDash, "Total Fuel Cost", regularFont);
            addTableCell(tableDash, "$" + dashboard.totalFuelCost().toString(), regularFont);

            addTableCell(tableDash, "Total Maintenance Cost", regularFont);
            addTableCell(tableDash, "$" + dashboard.totalMaintenanceCost().toString(), regularFont);

            addTableCell(tableDash, "Total Other Expenses", regularFont);
            addTableCell(tableDash, "$" + dashboard.totalExpenses().toString(), regularFont);

            document.add(tableDash);

            // 2. Statistics section
            document.add(new Paragraph("2. Operation Statistics", sectionFont));
            document.add(new Paragraph(" ", regularFont));

            PdfPTable tableStats = new PdfPTable(2);
            tableStats.setWidthPercentage(100);
            tableStats.setSpacingAfter(15);

            addTableCell(tableStats, "Metric", boldFont);
            addTableCell(tableStats, "Value", boldFont);

            addTableCell(tableStats, "Total Recorded Trips", regularFont);
            addTableCell(tableStats, String.valueOf(stats.totalTrips()), regularFont);

            addTableCell(tableStats, "Total Distance Traveled", regularFont);
            addTableCell(tableStats, String.format("%.2f km", stats.totalDistanceKm()), regularFont);

            addTableCell(tableStats, "Average Distance per Trip", regularFont);
            addTableCell(tableStats, String.format("%.2f km", stats.averageDistanceKm()), regularFont);

            addTableCell(tableStats, "Average Fuel Price per Liter", regularFont);
            addTableCell(tableStats, "$" + stats.averageFuelCostPerLiter().toString(), regularFont);

            addTableCell(tableStats, "Average Expense Amount", regularFont);
            addTableCell(tableStats, "$" + stats.averageExpenseAmount().toString(), regularFont);

            addTableCell(tableStats, "Average Fleet Speed", regularFont);
            addTableCell(tableStats, String.format("%.2f km/h", stats.averageSpeed()), regularFont);

            document.add(tableStats);

            // 3. KPIs section
            document.add(new Paragraph("3. Key Performance Indicators (KPIs)", sectionFont));
            document.add(new Paragraph(" ", regularFont));

            PdfPTable tableKPIs = new PdfPTable(2);
            tableKPIs.setWidthPercentage(100);
            tableKPIs.setSpacingAfter(15);

            addTableCell(tableKPIs, "KPI Description", boldFont);
            addTableCell(tableKPIs, "Score / Rate", boldFont);

            addTableCell(tableKPIs, "Fleet Utilization Rate", regularFont);
            addTableCell(tableKPIs, String.format("%.2f %%", kpis.fleetUtilizationRate()), regularFont);

            addTableCell(tableKPIs, "Average Driver Rating", regularFont);
            addTableCell(tableKPIs, String.format("%.2f / 5.00", kpis.averageDriverRating()), regularFont);

            addTableCell(tableKPIs, "Average Fuel Efficiency", regularFont);
            addTableCell(tableKPIs, String.format("%.2f km/L", kpis.averageFuelEfficiency()), regularFont);

            addTableCell(tableKPIs, "Fleet Operational Cost per KM", regularFont);
            addTableCell(tableKPIs, String.format("$%.2f per km", kpis.costPerKm()), regularFont);

            addTableCell(tableKPIs, "Overall Fleet Safety Score", regularFont);
            addTableCell(tableKPIs, String.format("%.2f %%", kpis.safetyScore()), regularFont);

            document.add(tableKPIs);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate PDF report: {}", e.getMessage());
            throw new RuntimeException("Error during PDF report creation", e);
        }
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    @Override
    public byte[] generateExcelReport(String token) {
        DashboardResponse dashboard = getSafeDashboard(token);
        StatisticsResponse stats = getSafeStatistics(token);
        KPIsResponse kpis = getSafeKPIs(token);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Fleet Operations Report");

            // Fonts & Styles
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.LEFT);

            org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);

            // Title
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Smart Transport Operations Platform Report");
            titleCell.setCellStyle(titleStyle);

            Row subtitleRow = sheet.createRow(1);
            subtitleRow.createCell(0).setCellValue("Generated at: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            int rowIdx = 3;

            // Section 1: Dashboard
            Row sec1Row = sheet.createRow(rowIdx++);
            sec1Row.createCell(0).setCellValue("1. Dashboard Metrics Summary");
            sec1Row.getCell(0).setCellStyle(createSectionHeaderStyle(workbook));

            Row headerRow1 = sheet.createRow(rowIdx++);
            headerRow1.createCell(0).setCellValue("Metric Name");
            headerRow1.createCell(1).setCellValue("Metric Value");
            headerRow1.getCell(0).setCellStyle(headerStyle);
            headerRow1.getCell(1).setCellStyle(headerStyle);

            rowIdx = writeExcelRow(sheet, rowIdx, "Total Vehicles / Active / Available", String.format("%d / %d / %d", dashboard.totalVehicles(), dashboard.activeVehicles(), dashboard.availableVehicles()));
            rowIdx = writeExcelRow(sheet, rowIdx, "Total Drivers / Available", String.format("%d / %d", dashboard.totalDrivers(), dashboard.availableDrivers()));
            rowIdx = writeExcelRow(sheet, rowIdx, "Ongoing Active Trips", String.valueOf(dashboard.activeTripsCount()));
            rowIdx = writeExcelRow(sheet, rowIdx, "Total Fuel Cost", "$" + dashboard.totalFuelCost());
            rowIdx = writeExcelRow(sheet, rowIdx, "Total Maintenance Cost", "$" + dashboard.totalMaintenanceCost());
            rowIdx = writeExcelRow(sheet, rowIdx, "Total Other Expenses", "$" + dashboard.totalExpenses());

            rowIdx++; // Blank spacer

            // Section 2: Stats
            Row sec2Row = sheet.createRow(rowIdx++);
            sec2Row.createCell(0).setCellValue("2. Operation Statistics");
            sec2Row.getCell(0).setCellStyle(createSectionHeaderStyle(workbook));

            Row headerRow2 = sheet.createRow(rowIdx++);
            headerRow2.createCell(0).setCellValue("Statistic Name");
            headerRow2.createCell(1).setCellValue("Statistic Value");
            headerRow2.getCell(0).setCellStyle(headerStyle);
            headerRow2.getCell(1).setCellStyle(headerStyle);

            rowIdx = writeExcelRow(sheet, rowIdx, "Total Recorded Trips", String.valueOf(stats.totalTrips()));
            rowIdx = writeExcelRow(sheet, rowIdx, "Total Distance Traveled", String.format("%.2f km", stats.totalDistanceKm()));
            rowIdx = writeExcelRow(sheet, rowIdx, "Average Distance per Trip", String.format("%.2f km", stats.averageDistanceKm()));
            rowIdx = writeExcelRow(sheet, rowIdx, "Average Fuel Price per Liter", "$" + stats.averageFuelCostPerLiter());
            rowIdx = writeExcelRow(sheet, rowIdx, "Average Expense Amount", "$" + stats.averageExpenseAmount());
            rowIdx = writeExcelRow(sheet, rowIdx, "Average Fleet Speed", String.format("%.2f km/h", stats.averageSpeed()));

            rowIdx++; // Blank spacer

            // Section 3: KPIs
            Row sec3Row = sheet.createRow(rowIdx++);
            sec3Row.createCell(0).setCellValue("3. Key Performance Indicators");
            sec3Row.getCell(0).setCellStyle(createSectionHeaderStyle(workbook));

            Row headerRow3 = sheet.createRow(rowIdx++);
            headerRow3.createCell(0).setCellValue("KPI Description");
            headerRow3.createCell(1).setCellValue("KPI Value");
            headerRow3.getCell(0).setCellStyle(headerStyle);
            headerRow3.getCell(1).setCellStyle(headerStyle);

            rowIdx = writeExcelRow(sheet, rowIdx, "Fleet Utilization Rate", String.format("%.2f %%", kpis.fleetUtilizationRate()));
            rowIdx = writeExcelRow(sheet, rowIdx, "Average Driver Rating", String.format("%.2f / 5.00", kpis.averageDriverRating()));
            rowIdx = writeExcelRow(sheet, rowIdx, "Average Fuel Efficiency", String.format("%.2f km/L", kpis.averageFuelEfficiency()));
            rowIdx = writeExcelRow(sheet, rowIdx, "Fleet Operational Cost per KM", String.format("$%.2f per km", kpis.costPerKm()));
            rowIdx = writeExcelRow(sheet, rowIdx, "Overall Fleet Safety Score", String.format("%.2f %%", kpis.safetyScore()));

            // Auto-size columns
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate Excel report: {}", e.getMessage());
            throw new RuntimeException("Error during Excel report creation", e);
        }
    }

    private CellStyle createSectionHeaderStyle(Workbook workbook) {
        org.apache.poi.ss.usermodel.Font sectionFont = workbook.createFont();
        sectionFont.setBold(true);
        sectionFont.setFontHeightInPoints((short) 13);
        sectionFont.setColor(IndexedColors.DARK_BLUE.getIndex());

        CellStyle sectionStyle = workbook.createCellStyle();
        sectionStyle.setFont(sectionFont);
        return sectionStyle;
    }

    private int writeExcelRow(Sheet sheet, int rowIdx, String name, String value) {
        Row row = sheet.createRow(rowIdx);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(value);
        return rowIdx + 1;
    }

    @Override
    public byte[] generateCSVReport(String token) {
        DashboardResponse dashboard = getSafeDashboard(token);
        StatisticsResponse stats = getSafeStatistics(token);
        KPIsResponse kpis = getSafeKPIs(token);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); PrintWriter writer = new PrintWriter(out)) {
            // Write CSV Header Info
            writer.println("Smart Transport Operations Platform - Operations Report");
            writer.println("Report Generation Date," + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println();

            // Section 1: Dashboard
            writer.println("1. Dashboard Metrics Summary");
            writer.println("Metric,Value");
            writer.println("Total Vehicles," + dashboard.totalVehicles());
            writer.println("Active Vehicles," + dashboard.activeVehicles());
            writer.println("Available Vehicles," + dashboard.availableVehicles());
            writer.println("Total Drivers," + dashboard.totalDrivers());
            writer.println("Available Drivers," + dashboard.availableDrivers());
            writer.println("Ongoing Trips," + dashboard.activeTripsCount());
            writer.println("Total Fuel Cost," + dashboard.totalFuelCost());
            writer.println("Total Maintenance Cost," + dashboard.totalMaintenanceCost());
            writer.println("Total Other Expenses," + dashboard.totalExpenses());
            writer.println();

            // Section 2: Statistics
            writer.println("2. Operations Statistics");
            writer.println("Statistic,Value");
            writer.println("Total Trips," + stats.totalTrips());
            writer.println("Total Distance (km)," + stats.totalDistanceKm());
            writer.println("Average Distance (km)," + stats.averageDistanceKm());
            writer.println("Average Fuel Price ($/L)," + stats.averageFuelCostPerLiter());
            writer.println("Average Expense Amount ($)," + stats.averageExpenseAmount());
            writer.println("Average Fleet Speed (km/h)," + stats.averageSpeed());
            writer.println();

            // Section 3: KPIs
            writer.println("3. Key Performance Indicators (KPIs)");
            writer.println("KPI,Value");
            writer.println("Fleet Utilization Rate (%)," + kpis.fleetUtilizationRate());
            writer.println("Average Driver Rating (1-5)," + kpis.averageDriverRating());
            writer.println("Average Fuel Efficiency (km/L)," + kpis.averageFuelEfficiency());
            writer.println("Operational Cost per KM ($)," + kpis.costPerKm());
            writer.println("Fleet Safety Score (%)," + kpis.safetyScore());

            writer.flush();
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate CSV report: {}", e.getMessage());
            throw new RuntimeException("Error during CSV report creation", e);
        }
    }
}
