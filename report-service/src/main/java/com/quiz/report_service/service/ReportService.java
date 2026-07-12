package com.quiz.report_service.service;

public interface ReportService {

    byte[] generatePDFReport(String token);

    byte[] generateExcelReport(String token);

    byte[] generateCSVReport(String token);
}
