package com.future.getd.ui.bean;

public class VoiceToTextBean {
    long time;
    String path;
    String content;

    public VoiceToTextBean() {
    }

    public VoiceToTextBean(long time, String path, String content) {
        this.time = time;
        this.path = path;
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "VoiceToTextBean{" +
                "time=" + time +
                ", path='" + path + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
