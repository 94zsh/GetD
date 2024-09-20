package com.future.getd.net.model;

import com.google.gson.JsonArray;

import java.util.List;
//{
//    "id": "chatcmpl-A99ShxKQ0HADrGHs1GVZtPdt2zuhO",
//    "object": "chat.completion",
//    "created": 1726744687,
//    "model": "gpt-4o-2024-05-13",
//    "choices": [
//        {
//            "index": 0,
//            "message": {
//                "role": "assistant",
//                "content": "你好！安卓（Android）是一款由谷歌开发的基于Linux内核的开源操作系统，主要用于智能手机和平板电脑。以下是一些关键点：\n\n1. **开放源代码**：安卓是开源的，其源代码在GitHub的Android Open Source Project (AOSP)上公开。这意味着开发者可以自由地修改和分发安卓系统。\n\n2. **应用生态系统**：安卓拥有丰富的应用生态系统，用户可以通过Google Play商店或者其他第三方应用商店下载和安装各种应用程序。\n\n3. **自定义功能**：安卓系统允许用户和制造商进行高度自定义，用户可以更改系统的主题、图标和设置。\n\n4. **多种设备支持**：除了智能手机和平板电脑，安卓还广泛应用于智能手表（通过Wear OS）、电视（通过Android TV）、汽车（通过Android Auto）等设备。\n\n5. **更新和安全性**：谷歌定期发布安卓系统更新，包括重大系统升级和安全补丁。\n\n如果你有任何关于安卓系统的具体问题或需要帮助的地方，请告诉我！",
//                "refusal": null
//            },
//            "logprobs": null,
//            "finish_reason": "stop"
//        }
//    ],
//    "usage": {
//        "prompt_tokens": 18,
//        "completion_tokens": 242,
//        "total_tokens": 260,
//        "completion_tokens_details": {
//            "reasoning_tokens": 0
//        }
//    },
public class GptResponse {
    String id;
    String object;
    long created;
    String model;
    List<CompletionChoice> choices;
    Usage usage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<CompletionChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<CompletionChoice> choices) {
        this.choices = choices;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "GptResponse{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", model='" + model + '\'' +
                ", choices=" + choices +
                ", usage=" + usage +
                '}';
    }
}

