/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils;

import com.google.gson.Gson;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;

@Deprecated
public class GsonWrapper {
    private static Gson gson = new Gson();

    private GsonWrapper() {
    }

    public static String getString(Object object) {
        return gson.toJson(object);
    }

    public static void overwriteGsonInstance(Gson gson) {
        GsonWrapper.gson = gson;
    }

    public static <T> T deserializeJson(String string, Class<T> clazz) {
        try {
            if (string == null) {
                return null;
            }
            Object object = gson.fromJson(string, clazz);
            return clazz.cast(object);
        } catch (Exception exception) {
            throw new NbtApiException("Error while converting json to " + clazz.getName(), exception);
        }
    }
}

