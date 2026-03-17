/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.ClassMetaobject;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Metalevel;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Metaobject;

public class Sample {
    private Metaobject _metaobject;
    private static ClassMetaobject _classobject;

    public Object trap(Object[] objectArray, int n) {
        Metaobject metaobject = this._metaobject;
        if (metaobject == null) {
            return ClassMetaobject.invoke(this, n, objectArray);
        }
        return metaobject.trapMethodcall(n, objectArray);
    }

    public static Object trapStatic(Object[] objectArray, int n) {
        return _classobject.trapMethodcall(n, objectArray);
    }

    public static Object trapRead(Object[] objectArray, String string) {
        if (objectArray[0] == null) {
            return _classobject.trapFieldRead(string);
        }
        return ((Metalevel)objectArray[0])._getMetaobject().trapFieldRead(string);
    }

    public static Object trapWrite(Object[] objectArray, String string) {
        Metalevel metalevel = (Metalevel)objectArray[0];
        if (metalevel == null) {
            _classobject.trapFieldWrite(string, objectArray[1]);
        } else {
            metalevel._getMetaobject().trapFieldWrite(string, objectArray[1]);
        }
        return null;
    }
}

