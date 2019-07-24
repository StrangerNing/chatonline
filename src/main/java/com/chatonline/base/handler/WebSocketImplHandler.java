package com.chatonline.base.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chatonline.base.constant.LocalConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/7/23
 */

@Service
public class WebSocketImplHandler implements WebSocketHandler {

    private static final List<WebSocketSession> SESSIONS = Collections.synchronizedList(new ArrayList<WebSocketSession>());

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("用户连接成功");
        SESSIONS.add(session);
        String username = (String) session.getAttributes().get(LocalConstant.WEBSOCKET_USERNAME_KEY);
        if (!username.isEmpty()) {
            JSONObject object = new JSONObject();
            object.put("onlineCount",SESSIONS.size());
            userList(object);
            session.sendMessage(new TextMessage(object.toJSONString()));
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        JSONObject msg = JSON.parseObject(message.getPayload().toString());
        JSONObject object = new JSONObject();
        if (msg.getInteger("type") == 1) {
            //给所有人
            object.put("msg",msg.getString("msg"));
            sendMessageToUsers(new TextMessage(object.toJSONString()));
        } else {
            String to = msg.getString("to");
            object.put("msg",msg.getString("msg"));
            sendMessageToUser(to, new TextMessage(object.toJSONString()));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        logger.info("连接异常，关闭连接");
        SESSIONS.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.info("关闭连接{}",closeStatus);
        SESSIONS.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : SESSIONS) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                logger.error("群发消息异常",e);
            }
        }
    }

    public void sendMessageToUser(String username, TextMessage message) {
        for (WebSocketSession user : SESSIONS) {
            if (user.getAttributes().get(LocalConstant.WEBSOCKET_USERNAME_KEY).equals(username)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    logger.error("发送消息异常",e);
                }
            }
        }
    }

    public void userList(JSONObject object) {
        List<String> usernameList = new ArrayList<>();
        for (WebSocketSession session : SESSIONS) {
            usernameList.add((String) session.getAttributes().get(LocalConstant.WEBSOCKET_USERNAME_KEY));
        }
        object.put("userList",usernameList);
    }
}
