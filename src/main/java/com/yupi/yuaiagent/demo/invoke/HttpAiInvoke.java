package com.yupi.yuaiagent.demo.invoke;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;

/**
 * HTTP 调用 AI
 */
public class HttpAiInvoke {
    public static void main(String[] args) {
        // 1. 你的 API-KEY（替换成你自己的）
        String apiKey = TestApiKey.API_KEY;

        // 2. 请求地址
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        // 3. 构造请求体 JSON
        JSONObject body = new JSONObject();
        body.set("model", "qwen-plus");

        // input -> messages
        JSONObject input = new JSONObject();
        JSONObject systemMsg = new JSONObject().set("role", "system").set("content", "You are a helpful assistant.");
        JSONObject userMsg = new JSONObject().set("role", "user").set("content", "你是谁？");
        input.set("messages", new JSONObject[]{systemMsg, userMsg});
        body.set("input", input);

        // parameters
        JSONObject parameters = new JSONObject().set("result_format", "message");
        body.set("parameters", parameters);

        // 4. 发送 POST 请求（Hutool）
        try (HttpResponse response = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(body.toString())
                .timeout(10000)
                .execute()) {

            // 5. 获取并打印结果
            String result = response.body();
            System.out.println("响应结果：");
            System.out.println(new JSONObject(result).toStringPretty()); // 美化输出JSON
        }
    }
}