/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  dev.triumphteam.nova.holder.StateHolder
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.component.functional;

import dev.triumphteam.nova.holder.StateHolder;
import java.util.concurrent.TimeUnit;
import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

interface BaseFunctionalGuiComponent<P>
extends StateHolder {
    public void withClickHandler(@Nullable ClickHandler<P> var1);

    public void withSimpleClickHandler();

    public void withCompletableFutureClickHandler();

    public void withCompletableFutureClickHandler(long var1, @NotNull TimeUnit var3);
}

