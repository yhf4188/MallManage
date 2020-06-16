package com.yhf.pointsmanage.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.tools.RedisService;
import com.yhf.pointsmanage.tools.TokenUtil;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private ValueOperations<String, Object> valueOperations;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception{
        if(request.getMethod().equals("OPTIONS")){//判断是否为预检请求（preflight request）
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("token");//获取token
        if(token != null){
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("txdy")).withIssuer("auth0").build();//使用HMAC256解析token
            DecodedJWT jwt = verifier.verify(token);
            boolean result = TokenUtil.verify(token)&&(valueOperations.get(jwt.getClaim("username").asString()).equals(token));//判断token的正确性
            if(result){
                return true;
            }
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try{
            JSONObject json = new JSONObject();
            json.put("msg","token verify fail");
            if(TokenUtil.verify(token))
                json.put("code", Constant.TOKEN_NULL);
            else json.put("code", Constant.TOKEN_ERROR);
            response.getWriter().append(json.toJSONString());//response中添加token判断结果
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(500);
            return false;
        }
        return false;
    }
}
