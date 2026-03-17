/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy;

import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.Proxy;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.ProxyFactory;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.ProxyObject;

public class ProxyObjectOutputStream
extends ObjectOutputStream {
    public ProxyObjectOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    protected void writeClassDescriptor(ObjectStreamClass objectStreamClass) {
        Class<?> clazz = objectStreamClass.forClass();
        if (ProxyFactory.isProxyClass(clazz)) {
            this.writeBoolean(true);
            Class<?> clazz2 = clazz.getSuperclass();
            Class<?>[] classArray = clazz.getInterfaces();
            byte[] byArray = ProxyFactory.getFilterSignature(clazz);
            String string = clazz2.getName();
            this.writeObject(string);
            this.writeInt(classArray.length - 1);
            for (int i = 0; i < classArray.length; ++i) {
                Class<?> clazz3 = classArray[i];
                if (clazz3 == ProxyObject.class || clazz3 == Proxy.class) continue;
                string = classArray[i].getName();
                this.writeObject(string);
            }
            this.writeInt(byArray.length);
            this.write(byArray);
        } else {
            this.writeBoolean(false);
            super.writeClassDescriptor(objectStreamClass);
        }
    }
}

