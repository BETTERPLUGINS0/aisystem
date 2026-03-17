/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.gson;

import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.Field;
import java.util.Locale;

public enum FieldNamingPolicy implements FieldNamingStrategy
{
    IDENTITY{

        @Override
        public String translateName(Field field) {
            return field.getName();
        }
    }
    ,
    UPPER_CAMEL_CASE{

        @Override
        public String translateName(Field field) {
            return 2.upperCaseFirstLetter(field.getName());
        }
    }
    ,
    UPPER_CAMEL_CASE_WITH_SPACES{

        @Override
        public String translateName(Field field) {
            return 3.upperCaseFirstLetter(3.separateCamelCase(field.getName(), " "));
        }
    }
    ,
    LOWER_CASE_WITH_UNDERSCORES{

        @Override
        public String translateName(Field field) {
            return 4.separateCamelCase(field.getName(), "_").toLowerCase(Locale.ENGLISH);
        }
    }
    ,
    LOWER_CASE_WITH_DASHES{

        @Override
        public String translateName(Field field) {
            return 5.separateCamelCase(field.getName(), "-").toLowerCase(Locale.ENGLISH);
        }
    };


    static String separateCamelCase(String string, String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (Character.isUpperCase(c) && stringBuilder.length() != 0) {
                stringBuilder.append(string2);
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    static String upperCaseFirstLetter(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        char c = string.charAt(n);
        while (n < string.length() - 1 && !Character.isLetter(c)) {
            stringBuilder.append(c);
            c = string.charAt(++n);
        }
        if (n == string.length()) {
            return stringBuilder.toString();
        }
        if (!Character.isUpperCase(c)) {
            String string2 = FieldNamingPolicy.modifyString(Character.toUpperCase(c), string, ++n);
            return stringBuilder.append(string2).toString();
        }
        return string;
    }

    private static String modifyString(char c, String string, int n) {
        return n < string.length() ? c + string.substring(n) : String.valueOf(c);
    }
}

