package com.future.getd.net.gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/* loaded from: classes3.dex */
public class GsonObjectTypeToken implements ParameterizedType {
    private final Type[] args;
    private final Type owner;
    private final Class<?> raw;

    public GsonObjectTypeToken(Class<?> cls, Type[] typeArr, Type type) {
        this.raw = cls;
        this.args = typeArr == null ? new Type[0] : typeArr;
        this.owner = type;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type[] getActualTypeArguments() {
        return this.args;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getOwnerType() {
        return this.owner;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getRawType() {
        return this.raw;
    }
}