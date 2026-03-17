/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy;

import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.MethodHandler;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.Proxy;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.ProxyFactory;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.ProxyObject;

class SerializedProxy
implements Serializable {
    private static final long serialVersionUID = 1L;
    private String superClass;
    private String[] interfaces;
    private byte[] filterSignature;
    private MethodHandler handler;

    SerializedProxy(Class<?> clazz, byte[] byArray, MethodHandler methodHandler) {
        this.filterSignature = byArray;
        this.handler = methodHandler;
        this.superClass = clazz.getSuperclass().getName();
        Class<?>[] classArray = clazz.getInterfaces();
        int n = classArray.length;
        this.interfaces = new String[n - 1];
        String string = ProxyObject.class.getName();
        String string2 = Proxy.class.getName();
        for (int i = 0; i < n; ++i) {
            String string3 = classArray[i].getName();
            if (string3.equals(string) || string3.equals(string2)) continue;
            this.interfaces[i] = string3;
        }
    }

    protected Class<?> loadClass(final String string) {
        try {
            return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>(){

                @Override
                public Class<?> run() {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    return Class.forName(string, true, classLoader);
                }
            });
        } catch (PrivilegedActionException privilegedActionException) {
            throw new RuntimeException("cannot load the class: " + string, privilegedActionException.getException());
        }
    }

    Object readResolve() {
        try {
            int n = this.interfaces.length;
            Class[] classArray = new Class[n];
            for (int i = 0; i < n; ++i) {
                classArray[i] = this.loadClass(this.interfaces[i]);
            }
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setSuperclass(this.loadClass(this.superClass));
            proxyFactory.setInterfaces(classArray);
            Proxy proxy = (Proxy)proxyFactory.createClass(this.filterSignature).getConstructor(new Class[0]).newInstance(new Object[0]);
            proxy.setHandler(this.handler);
            return proxy;
        } catch (NoSuchMethodException noSuchMethodException) {
            throw new InvalidClassException(noSuchMethodException.getMessage());
        } catch (InvocationTargetException invocationTargetException) {
            throw new InvalidClassException(invocationTargetException.getMessage());
        } catch (ClassNotFoundException classNotFoundException) {
            throw new InvalidClassException(classNotFoundException.getMessage());
        } catch (InstantiationException instantiationException) {
            throw new InvalidObjectException(instantiationException.getMessage());
        } catch (IllegalAccessException illegalAccessException) {
            throw new InvalidClassException(illegalAccessException.getMessage());
        }
    }
}

