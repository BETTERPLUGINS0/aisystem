/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.click.controller;

import org.jetbrains.annotations.Nullable;

public interface ClickController {
    public boolean isDone();

    public void complete(@Nullable Throwable var1);

    public boolean completingLater();

    public void completingLater(boolean var1);
}

