package com.bread.traveler;

import com.bread.traveler.entity.Users;
import com.bread.traveler.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class TravelerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private UsersService usersService;
    @Autowired
    private EmbeddingModel embeddingModel;

    @Test
    void testEmbedding() {
        List<Users> users = usersService.list();
        List<String> texts = users.stream().map(Users::getPreferencesText).toList();
        List<float[]> embed = embeddingModel.embed(texts);
        for (int i = 0; i < embed.size(); i++) {
            Users user = users.get(i);
            user.setPreferencesEmbedding(embed.get(i));
        }
        usersService.updateBatchById(users);
    }

}
