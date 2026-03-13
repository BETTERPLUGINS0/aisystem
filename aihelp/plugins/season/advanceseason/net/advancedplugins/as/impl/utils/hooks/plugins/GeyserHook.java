/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;

public class GeyserHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.GEYSER.getPluginName();
    }
}

