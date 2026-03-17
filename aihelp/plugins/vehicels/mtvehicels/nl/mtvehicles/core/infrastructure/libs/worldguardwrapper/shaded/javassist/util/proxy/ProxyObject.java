/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.util.proxy;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.util.proxy.MethodHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.util.proxy.Proxy;

public interface ProxyObject
extends Proxy {
    @Override
    public void setHandler(MethodHandler var1);

    public MethodHandler getHandler();
}

