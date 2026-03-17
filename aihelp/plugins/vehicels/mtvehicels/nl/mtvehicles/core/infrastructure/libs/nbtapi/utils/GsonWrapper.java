/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils;

import com.google.gson.Gson;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;

@Deprecated
public class GsonWrapper {
    private static Gson gson = new Gson();

    private GsonWrapper() {
    }

    public static String getString(Object obj) {
        return gson.toJson(obj);
    }

    public static void overwriteGsonInstance(Gson replacement) {
        gson = replacement;
    }

    public static <T> T deserializeJson(String json, Class<T> type) {
        try {
            if (json == null) {
                return null;
            }
            Object obj = gson.fromJson(json, type);
            return type.cast(obj);
        } catch (Exception ex) {
            throw new NbtApiException("Error while converting json to " + type.getName(), ex);
        }
    }
}

