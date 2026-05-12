package com.yupi.yuaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * 违禁词检查Advisor
 * 检查用户输入是否包含违禁词，如有则直接返回响应
 */
@Slf4j
public class ProhibitedWordsAdvisor implements CallAdvisor, StreamAdvisor {

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
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain chain) {
        // 从 Prompt 对象中获取用户输入文本
        String userText = chatClientRequest.prompt().getContents();
        if (userText != null) {
            for (String word : prohibitedWords) {
                if (userText.contains(word)) {
                    log.warn("用户输入包含违禁词: {}", word);
                    return createProhibitedResponse();
                }
            }
        }
        return chain.nextCall(chatClientRequest);
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain chain) {
        // 从 Prompt 对象中获取用户输入文本
        String userText = chatClientRequest.prompt().getContents();
        if (userText != null) {
            for (String word : prohibitedWords) {
                if (userText.contains(word)) {
                    log.warn("用户输入包含违禁词: {}", word);
                    return Flux.just(createProhibitedResponse());
                }
            }
        }
        return chain.nextStream(chatClientRequest);
    }

    // 适配你当前版本的正确构建方式
    private ChatClientResponse createProhibitedResponse() {
        String responseText = "您的输入包含违禁词，无法继续处理。请修改后重试。";

        AssistantMessage assistantMessage = new AssistantMessage(responseText);
        Generation generation = new Generation(assistantMessage);
        List<Generation> generations = new ArrayList<>();
        generations.add(generation);

        ChatResponse chatResponse = new ChatResponse(generations);

        // 👇 核心修复：使用无参 builder，再设置 chatResponse
        return ChatClientResponse.builder()
                .chatResponse(chatResponse)
                .build();
    }
}