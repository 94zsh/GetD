package com.future.getd.net.model;

import java.io.File;
import java.util.ArrayList;

public class GptTranscriptions {
    //curl https://api.openai.com/v1/audio/translations \
    //  -H "Authorization: Bearer $OPENAI_API_KEY" \
    //  -H "Content-Type: multipart/form-data" \
    //  -F file="@/path/to/file/german.m4a" \
    //  -F model="whisper-1"

    String model = "whisper-1";
//    File file;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

//    public File getFile() {
//        return file;
//    }

//    public void setFile(File file) {
//        this.file = file;
//    }
}
