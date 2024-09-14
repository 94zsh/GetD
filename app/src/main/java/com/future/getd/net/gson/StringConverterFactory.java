package com.future.getd.net.gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/* loaded from: classes3.dex */
public class StringConverterFactory extends Converter.Factory {

    /* loaded from: classes3.dex */
    public static class StringConverter implements Converter<ResponseBody, String> {
        @Override // retrofit2.Converter
        public String convert(ResponseBody responseBody) throws IOException {
            return responseBody.string();
        }
    }

    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }

    @Override // retrofit2.Converter.Factory
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotationArr, Retrofit retrofit) {
        if (type == String.class) {
            return new StringConverter();
        }
        return null;
    }
}