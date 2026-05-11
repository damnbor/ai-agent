package com.yupi.yuaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {
    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String filename = "test.pdf";
        String content = "This is a test PDF.";
        String result = tool.generatePDF(filename, content);
        assertNotNull(result);
    }
}