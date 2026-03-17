/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.settings;

import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import me.zombie_striker.qav.gui.click.handler.SimpleClickHandler;
import me.zombie_striker.qav.gui.component.renderer.DefaultGuiComponentRenderer;
import me.zombie_striker.qav.gui.component.renderer.GuiComponentRenderer;
import org.jetbrains.annotations.NotNull;

public abstract class GuiSettings<P, I, S extends GuiSettings<P, I, S>> {
    private ClickHandler<P> clickHandler = new SimpleClickHandler();
    private GuiComponentRenderer<P, I> componentRenderer = new DefaultGuiComponentRenderer();
    private long spamPreventionDuration = 200L;

    public S clickHandler(@NotNull ClickHandler<P> clickHandler) {
        this.clickHandler = clickHandler;
        return (S)this;
    }

    public S componentRenderer(@NotNull GuiComponentRenderer<P, I> guiComponentRenderer) {
        this.componentRenderer = guiComponentRenderer;
        return (S)this;
    }

    public S spamPreventionDuration(long l) {
        if (l < 0L) {
            throw new IllegalArgumentException("Spam prevention duration cannot be negative!");
        }
        this.spamPreventionDuration = l;
        return (S)this;
    }

    @NotNull
    public ClickHandler<P> getClickHandler() {
        return this.clickHandler;
    }

    @NotNull
    public GuiComponentRenderer<P, I> getComponentRenderer() {
        return this.componentRenderer;
    }

    public long getSpamPreventionDuration() {
        return this.spamPreventionDuration;
    }
}

