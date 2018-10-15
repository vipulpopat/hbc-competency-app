package com.hbc.app.util;

import java.util.Map;
import java.util.stream.Collectors;

// Program to clone a Map in Java
public class MapUtils
{
    public static<K,V> Map<K,V> clone(Map<K,V> original) {
        return original.entrySet()
                       .stream()
                       .collect(Collectors.toMap(Map.Entry::getKey,
                                                Map.Entry::getValue));
    }

}