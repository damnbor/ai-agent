package com.yupi.yuaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ResourceDownloadToolTest {

    @Test
    public void testDownloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://jsonplaceholder.typicode.com/tower-icon-and-logo-1400x260.png";
        String fileName = "damn.png";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
    }
}
