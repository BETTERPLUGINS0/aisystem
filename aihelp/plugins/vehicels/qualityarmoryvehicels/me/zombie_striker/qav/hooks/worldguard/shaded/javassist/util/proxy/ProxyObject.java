/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.MethodHandler;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.Proxy;

public interface ProxyObject
extends Proxy {
    @Override
    public void setHandler(MethodHandler var1);

    public MethodHandler getHandler();
}

