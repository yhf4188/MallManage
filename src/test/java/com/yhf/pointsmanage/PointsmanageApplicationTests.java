package com.yhf.pointsmanage;

import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.controller.UserController;
import com.yhf.pointsmanage.entity.Attribute;
import com.yhf.pointsmanage.entity.Mall;
import com.yhf.pointsmanage.entity.User;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PointsmanageApplication.class})
@AutoConfigureMockMvc
// 指定启动类
class PointsmanageApplicationTests {

    @Autowired
    private MockMvc mvc;
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
    }
    @Test
    public void userControllerTest() throws Exception{
        // 构建请求
        Mall mall=new Mall();
        mall.setId(1);
        mall.setName("兑兑积分");
        mall.setAttribution_impl("http://localhost:8081/user/bindUser");
        Attribute user=new Attribute(0,"yhf","123456","1304039757@qq.com","13961862263",0);
//        Map<String,Object> map=new HashMap<>();
//        map.put("user",user);
//        map.put("mall",mall);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("user",user);
        jsonObject.put("mall",mall);
        String requestJson = JSONObject.toJSONString(jsonObject);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/bindUser")
                .contentType("application/json")
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON);

        // 发送请求，获取请求结果
        ResultActions perform = mvc.perform(request);

        // 请求结果校验
        perform.andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
    }

}
