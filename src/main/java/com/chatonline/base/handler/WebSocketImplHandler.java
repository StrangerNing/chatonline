package com.chatonline.base.handler;

import com.alibaba.fastjson.JSONObject;
import com.chatonline.base.constant.LocalConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

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

    private final static List<WebSocketSession> SESSIONS = Collections.synchronizedList(new ArrayList<WebSocketSession>());

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("用户连接成功");
        SESSIONS.add(session);
        String username = (String) session.getAttributes().get(LocalConstant.WEBSOCKET_USERNAME_KEY);
        if (!username.isEmpty()) {
            JSONObject object = new JSONObject();
            object.put("count",SESSIONS.size());
            // TODO: 2019/7/23 传给前端 
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
