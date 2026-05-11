package com.yupi.yuaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 违禁词检查Advisor
 * 检查用户输入是否包含违禁词，如有则直接返回响应
 */
@Slf4j
public class ProhibitedWordsAdvisor implements CallAroundAdvisor {

    private final List<String> prohibitedWords = new ArrayList<>();

    public ProhibitedWordsAdvisor() {
        prohibitedWords.add("阴");
        prohibitedWords.add("根");
        prohibitedWords.add("萎");
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        String userText = advisedRequest.userText();
        if (userText != null) {
            for (String word : prohibitedWords) {
                if (userText.contains(word)) {
                    log.warn("用户输入包含违禁词: {}", word);
                    return createProhibitedResponse(advisedRequest);
                }
            }
        }
        return chain.nextAroundCall(advisedRequest);
    }

    private AdvisedResponse createProhibitedResponse(AdvisedRequest request) {
        String responseText = "您的输入包含违禁词，无法继续处理。请修改后重试。";

        AssistantMessage assistantMessage = new AssistantMessage(responseText);
        Generation generation = new Generation(assistantMessage);
        List<Generation> generations = new ArrayList<>();
        generations.add(generation);

        ChatResponse chatResponse = new ChatResponse(generations);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("prohibited", true);

        return new AdvisedResponse(chatResponse, attributes);
    }
}