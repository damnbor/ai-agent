package com.yupi.yuaiagent.chatMemory;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于Redis持久化的对话记忆
 */
public class RedisBasedChatMemory implements ChatMemory {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String PREFIX = "chat:memory:";

    /**
     * 构造函数
     * @param redisTemplate Redis操作模板
     */
    public RedisBasedChatMemory(RedisTemplate<String, Object> redisTemplate) {
        // 配置RedisTemplate的序列化器
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        // 获取当前对话的所有消息
        List<Message> conversationMessages = getOrCreateConversation(conversationId);
        // 添加新消息
        conversationMessages.addAll(messages);
        // 保存到Redis
        saveConversation(conversationId, conversationMessages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        // 获取所有消息
        List<Message> allMessages = getOrCreateConversation(conversationId);
        // 返回最近的N条消息
        return allMessages.stream()
                .skip(Math.max(0, allMessages.size() - lastN))
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        // 删除Redis中的对话记录
        redisTemplate.delete(getRedisKey(conversationId));
    }

    /**
     * 获取或创建对话
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        // 从Redis获取消息列表
        List<Message> messages = (List<Message>) redisTemplate.opsForValue().get(getRedisKey(conversationId));
        // 如果不存在，返回空列表
        return messages != null ? messages : new ArrayList<>();
    }

    /**
     * 保存对话
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        // 将消息列表保存到Redis
        redisTemplate.opsForValue().set(getRedisKey(conversationId), messages);
    }

    /**
     * 获取Redis键
     */
    private String getRedisKey(String conversationId) {
        return PREFIX + conversationId;
    }
}