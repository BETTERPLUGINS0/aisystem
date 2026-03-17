/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.component.functional;

import me.zombie_striker.qav.gui.component.functional.FunctionalGuiComponent;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FunctionalGuiComponentBuilder<P, I> {
    public void accept(@NotNull @NotNull FunctionalGuiComponent<@NotNull P, @NotNull I> var1);
}

