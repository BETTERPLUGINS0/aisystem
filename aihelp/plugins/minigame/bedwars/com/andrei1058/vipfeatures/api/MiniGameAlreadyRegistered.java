/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.vipfeatures.api;

import org.bukkit.plugin.Plugin;

public class MiniGameAlreadyRegistered
extends Throwable {
    public MiniGameAlreadyRegistered(Plugin plugin) {
        super("Cannot register mini-game integration for: " + plugin.getName() + ". Mini-game already registered.");
    }
}

