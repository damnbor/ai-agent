package com.yupi.yuaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PgVectorVectorStoreConfigTest {
    @Resource
    VectorStore pgVectorVectorStore;

    @Test
    void pgVectorVectorStore() {
        List<Document> documents = List.of(
                new Document("马拉松是一个严肃的极限运动", Map.of("meta1","meta1")),
                new Document("参加马拉松比赛之前，要做好充足的训练，不要轻视马拉松的严酷。"),
                new Document("我看你小伙子行",Map.of("meta2","meta2")));
        //添加文档
        pgVectorVectorStore.add(documents);
        //相似度查询
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("马拉松是什么").topK(3).build());
        Assertions.assertNotNull(results);
    }
}