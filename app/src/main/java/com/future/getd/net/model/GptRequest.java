package com.future.getd.net.model;

import com.google.gson.JsonArray;

import java.util.ArrayList;

public class GptRequest {
    //{
    //    "model": "{{model}}",
    //    "messages": [
    //        {
    //            "role": "system",
    //            "content": "You are a helpful assistant."
    //        },
    //        {
    //            "role": "user",
    //            "content": "安卓"
    //        }
    //    ]
    //}
    String model = "gpt-4";
    ArrayList<Message> messages = new ArrayList<>();
//    Pair("stream", true),


    public GptRequest(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
