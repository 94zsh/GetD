package com.future.getd.ui.bean;

public class AudioItem {
    int type;
    String content;
    int state;

    public AudioItem(int type, String content, int state) {
        this.type = type;
        this.content = content;
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
