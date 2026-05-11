package com.yupi.yuaiagent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WebSearchTool {

    // SearchAPI 的搜索接口地址
    private static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";

    private final String apiKey;

    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    @Tool(description = "Search for information from Baidu Search Engine")
    public String searchWeb(
            @ToolParam(description = "Search query keyword") String query) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", query);
        paramMap.put("api_key", apiKey);
        paramMap.put("engine", "baidu");
        try {
            String response = HttpUtil.get(SEARCH_API_URL, paramMap);
            // 取出返回结果的前 5 条
            JSONObject jsonObject = JSONUtil.parseObj(response);
            // 提取 organic_results 部分
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");

            // 检查 organicResults 是否为 null 或空
            if (organicResults == null || organicResults.isEmpty()) {
                return "No search results found.";
            }

            // 取前 5 条结果，避免索引越界
            int endIndex = Math.min(organicResults.size(), 5);
            List<Object> objects = organicResults.subList(0, endIndex);

            // 拼接搜索结果为字符串，只保留关键信息
            String result = objects.stream().map(obj -> {
                JSONObject tmpJSONObject = (JSONObject) obj;
                // 创建新的 JSON 对象，只包含关键信息
                JSONObject keyInfo = new JSONObject();
                keyInfo.put("title", tmpJSONObject.getStr("title"));
                keyInfo.put("link", tmpJSONObject.getStr("link"));
                keyInfo.put("snippet", tmpJSONObject.getStr("snippet"));
                return keyInfo.toString();
            }).collect(Collectors.joining(","));
            return result;
        } catch (Exception e) {
            return "Error searching Baidu: " + e.getMessage();
        }
    }
}
