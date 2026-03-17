/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.component;

import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import org.jetbrains.annotations.Nullable;

public interface GuiComponent<P, I> {
    @Nullable
    default public ClickHandler<P> clickHandler() {
        return null;
    }
}

