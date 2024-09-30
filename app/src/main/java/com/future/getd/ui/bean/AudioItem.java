package com.future.getd.ui.bean;

public class AudioItem {
    int type;
    String content;
    boolean isPlaying;
    String assetName;

    public AudioItem(int type, String content, boolean state,String assetName) {
        this.type = type;
        this.content = content;
        this.isPlaying = state;
        this.assetName = assetName;
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

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
}
