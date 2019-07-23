package com.chatonline.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chatonline.base.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/7/23
 */

@ServerEndpoint("/chat/{userId}")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    /**
     * 在线连接数
     */
    private static Integer onlineNum = 0;

    /**
     * 存放每个客户端的Socket对象
     */
    private static ConcurrentHashMap<Long, ChatController> serverSet = new ConcurrentHashMap<>();

    /**
     * 与客户端的连接
     */
    private Session session;

    private Long userId;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        this.session = session;
        serverSet.put(userId, this);
        addOnlineNum();
        logger.info("用户{}连接，当前在线人数为{}", userId, getOnlineNum());
        this.userId = userId;
    }

    @OnClose
    public void onClose() {
        if (serverSet.get(userId) != null) {
            serverSet.remove(userId);
            subOnlineNum();
            logger.info("用户{}断开连接，当前在线人数为{}",userId,getOnlineNum());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (StringUtils.isNotBlank(message)) {
            JSONArray list = JSONArray.parseArray(message);
            for (int i= 0;i<list.size();i++){
                try {
                    JSONObject object = list.getJSONObject(i);
                    String toUserId=object.getString("toUserId");
                    String contentText=object.getString("contentText");
                    object.put("fromUserId",this.userId);
                    if (StringUtils.isNotBlank(toUserId) && StringUtils.isNotBlank(contentText)) {
                        ChatController socket = serverSet.get(toUserId);
                        if (socket!=null){
                            socket.sendMessage(contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error("解析消息失败",e);
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("系统异常",error);
        throw new BusinessException("系统异常");
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendObject(Object object) throws EncodeException{
        try {
            this.session.getBasicRemote().sendObject(object);
        } catch (IOException e) {
            logger.error("发送错误",e);
        }
    }

    public void sendToUser(String message,Long userId) throws IOException {
        if (serverSet.containsKey(userId)) {
            ChatController toUser = serverSet.get(userId);
            toUser.sendMessage(message);
        } else {
            // TODO: 2019/7/23 将消息放到数据库的未读表中
        }
    }

    public void sendToGroup(String message,Long groupId) {
        // TODO: 2019/7/23 获取组内成员
    }

    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        logger.info("发送消息到{}，消息内容{}",userId,message);

    }

    public static synchronized int getOnlineNum() {
        return onlineNum;
    }

    public static synchronized void addOnlineNum() {
        ChatController.onlineNum++;
    }

    public static synchronized void subOnlineNum() {
        ChatController.onlineNum--;
    }
}
