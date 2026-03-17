/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.component.functional;

import me.zombie_striker.qav.gui.component.functional.BaseFunctionalGuiComponent;
import me.zombie_striker.qav.gui.component.functional.FunctionalGuiComponentRender;
import org.jetbrains.annotations.NotNull;

public interface FunctionalGuiComponent<P, I>
extends BaseFunctionalGuiComponent<P> {
    public void render(@NotNull @NotNull FunctionalGuiComponentRender<@NotNull P, @NotNull I> var1);
}

