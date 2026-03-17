/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.ProxyFactory;

public class ProxyObjectInputStream
extends ObjectInputStream {
    private ClassLoader loader = Thread.currentThread().getContextClassLoader();

    public ProxyObjectInputStream(InputStream inputStream) {
        super(inputStream);
        if (this.loader == null) {
            this.loader = ClassLoader.getSystemClassLoader();
        }
    }

    public void setClassLoader(ClassLoader classLoader) {
        if (classLoader != null) {
            this.loader = classLoader;
        } else {
            classLoader = ClassLoader.getSystemClassLoader();
        }
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() {
        boolean bl = this.readBoolean();
        if (bl) {
            String string = (String)this.readObject();
            Class<?> clazz = this.loader.loadClass(string);
            int n = this.readInt();
            Class[] classArray = new Class[n];
            for (int i = 0; i < n; ++i) {
                string = (String)this.readObject();
                classArray[i] = this.loader.loadClass(string);
            }
            n = this.readInt();
            byte[] byArray = new byte[n];
            this.read(byArray);
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setUseCache(true);
            proxyFactory.setUseWriteReplace(false);
            proxyFactory.setSuperclass(clazz);
            proxyFactory.setInterfaces(classArray);
            Class<?> clazz2 = proxyFactory.createClass(byArray);
            return ObjectStreamClass.lookup(clazz2);
        }
        return super.readClassDescriptor();
    }
}

