package org.hombro.acting.shakespeare.utils;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class ToStringHelper {

    private final Class<?> clazz;
    private final Map<String, String> values;

    public ToStringHelper(Class<?> clazz) {
        this.clazz = clazz;
        values = new HashMap<>();
    }

    public static ToStringHelper forClass(Class<?> clazz){
        return new ToStringHelper(clazz);
    }

    public ToStringHelper with(String k, Object v){
        values.put(k, v.toString());
        return this;
    }

    @Override
    public String toString(){
        return String.format("%s={%s}", clazz.getSimpleName(), values.entrySet().stream()
        .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
        .collect(joining(", ")));
    }
}
