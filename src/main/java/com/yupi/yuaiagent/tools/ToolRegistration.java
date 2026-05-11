package com.yupi.yuaiagent.tools;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集中的工具注册类
 */
@Configuration
public class ToolRegistration {
    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Bean
    public ToolCallback[] allTools(){
        return ToolCallbacks.from(
                new PDFGenerationTool(),
                new ResourceDownloadTool(),
                new WebScrapingTool(),
                new WebSearchTool(searchApiKey),
                new FileOperationTool(),
                new TerminalOperationTool(),
                new TerminateTool()
        );
    }
}
