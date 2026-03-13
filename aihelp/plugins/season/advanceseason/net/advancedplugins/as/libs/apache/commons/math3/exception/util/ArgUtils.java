/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception.util;

import java.util.ArrayList;

public class ArgUtils {
    private ArgUtils() {
    }

    public static Object[] flatten(Object[] objectArray) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        if (objectArray != null) {
            for (Object object : objectArray) {
                if (object instanceof Object[]) {
                    for (Object object2 : ArgUtils.flatten((Object[])object)) {
                        arrayList.add(object2);
                    }
                    continue;
                }
                arrayList.add(object);
            }
        }
        return arrayList.toArray();
    }
}

