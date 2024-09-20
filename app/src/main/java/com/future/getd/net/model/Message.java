package com.future.getd.net.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_message_table")
public class Message {
  public static final String ROLE_USER = "user";
  public static final String ROLE_SYSTEM = "system";
  public static final String ROLE_ASSISTANT = "assistant";
  public String account;
  public String id;

  public String role;

  public String content;
  /**
   * stop结束,其他未结束,
   */
  public String type;
  /**
   * 消息类型 heartBeat 心跳   本地发送 message 消息 normal  接收到的消息  resend  有消息需要重新发送
   */
  public String messageType;
  /**
   * 消息顺序下标
   */
  @PrimaryKey(autoGenerate = true)
  public Integer index;

  public Message(String role, String content) {
    this.role = role;
    this.content = content;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  @Override
  public String toString() {
    return "Message{" +
            "id='" + id + '\'' +
            ", role='" + role + '\'' +
            ", content='" + content + '\'' +
            ", type='" + type + '\'' +
            ", messageType='" + messageType + '\'' +
            ", index=" + index +
            '}';
  }
}
