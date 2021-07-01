package com.infogain.gcp.poc.consumer.controller;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.stream.Collectors;

@Slf4j
@WebServlet(value = "/pubsub/authenticated-push")
public class IngestDecodeService extends HttpServlet {

    private final Gson gson = new Gson();
    private final JsonParser jsonParser = new JsonParser();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Message message = getMessage(req);
        log.info("Message received : {}", message.getData());
        resp.setStatus(102);
        super.doPost(req, resp);
    }

    private Message getMessage(HttpServletRequest request) throws IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining("\n"));
        JsonElement jsonRoot = jsonParser.parse(requestBody);
        String messageStr = jsonRoot.getAsJsonObject().get("message").toString();
        Message message = gson.fromJson(messageStr, Message.class);
        // decode from base64
        String decoded = decode(message.getData());
        message.setData(decoded);
        return message;
    }

    private String decode(String data) {
        return new String(Base64.getDecoder().decode(data));
    }
}
