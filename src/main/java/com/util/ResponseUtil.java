package com.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendJsonResponse(HttpServletResponse response, Object content) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JsonUtil.serializeObject(content));
    }

    public static void sendSuccessResponse(HttpServletResponse response, String message) throws IOException {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("success", true);
        jsonResponse.put("message", message);
        String json = objectMapper.writeValueAsString(jsonResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
    }

    public static void sendFailureResponse(HttpServletResponse response, String message) throws IOException {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("success", false);
        jsonResponse.put("message", message);
        String json = objectMapper.writeValueAsString(jsonResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
    }
}
