package com.bread.traveler;

import cn.hutool.json.JSONObject;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.Users;
import com.bread.traveler.service.UsersService;
import com.bread.traveler.utils.GaoDeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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
    @Autowired
    private GaoDeUtils gaoDeUtils;

    @Test
    void testEmbedding() {
        List<Users> users = usersService.list();
        for (Users user : users) {
            usersService.updateUserPreferences(user.getUserId(), user.getPreferencesText());
        }
    }

    @Test
    void testPOISearch() throws IOException {
        JSONObject results = gaoDeUtils.searchPoi(GaoDeUtils.SearchPoiParam.builder()
                .keywords("北京大学")
                .showFields(List.of(Constant.ShowField.BUSINESS, Constant.ShowField.PHOTOS))
                .pageSize(5)
                .build());
        System.out.println(results);
    }

}
