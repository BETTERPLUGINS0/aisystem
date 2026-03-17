/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.builder;

import java.util.ArrayList;
import java.util.List;
import me.zombie_striker.qav.gui.BaseGui;
import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import me.zombie_striker.qav.gui.component.GuiComponent;
import me.zombie_striker.qav.gui.component.functional.FunctionalGuiComponentBuilder;
import me.zombie_striker.qav.gui.component.functional.SimpleFunctionalGuiComponent;
import me.zombie_striker.qav.gui.component.renderer.GuiComponentRenderer;
import me.zombie_striker.qav.gui.container.type.GuiContainerType;
import me.zombie_striker.qav.gui.exception.TriumphGuiException;
import me.zombie_striker.qav.gui.settings.GuiSettings;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class BaseGuiBuilder<B extends BaseGuiBuilder<B, P, G, I>, P, G extends BaseGui<P>, I> {
    private final GuiSettings<P, I, ?> guiSettings;
    private final GuiContainerType containerType;
    private final List<GuiComponent<P, I>> components = new ArrayList<GuiComponent<P, I>>();
    private ClickHandler<P> clickHandler = null;
    private GuiComponentRenderer<P, I> componentRenderer = null;
    private Component title = null;
    private long spamPreventionDuration = -1L;

    public BaseGuiBuilder(GuiSettings<P, I, ?> guiSettings, @NotNull GuiContainerType guiContainerType) {
        this.guiSettings = guiSettings;
        this.containerType = guiContainerType;
    }

    @Contract(value="_ -> this")
    @NotNull
    public B title(@NotNull Component component) {
        this.title = component;
        return (B)this;
    }

    @Contract(value="_ -> this")
    @NotNull
    public B clickHandler(@NotNull ClickHandler<P> clickHandler) {
        this.clickHandler = clickHandler;
        return (B)this;
    }

    @Contract(value="_ -> this")
    @NotNull
    public B componentRenderer(@NotNull GuiComponentRenderer<P, I> guiComponentRenderer) {
        this.componentRenderer = guiComponentRenderer;
        return (B)this;
    }

    @Contract(value="_ -> this")
    @NotNull
    public B spamPreventionDuration(long l) {
        if (l < 0L) {
            throw new TriumphGuiException("Spam prevention duration cannot be negative!");
        }
        this.spamPreventionDuration = l;
        return (B)this;
    }

    @Contract(value="_ -> this")
    @NotNull
    public B component(@NotNull FunctionalGuiComponentBuilder<P, I> functionalGuiComponentBuilder) {
        SimpleFunctionalGuiComponent simpleFunctionalGuiComponent = new SimpleFunctionalGuiComponent();
        functionalGuiComponentBuilder.accept(simpleFunctionalGuiComponent);
        this.components.add(simpleFunctionalGuiComponent.asGuiComponent());
        return (B)this;
    }

    @Contract(value="_ -> this")
    @NotNull
    public B component(@NotNull GuiComponent<P, I> guiComponent) {
        this.components.add(guiComponent);
        return (B)this;
    }

    public abstract G build();

    @NotNull
    protected GuiContainerType getContainerType() {
        return this.containerType;
    }

    @NotNull
    protected List<GuiComponent<P, I>> getComponents() {
        return this.components;
    }

    @NotNull
    protected ClickHandler<P> getClickHandler() {
        if (this.clickHandler == null) {
            return this.guiSettings.getClickHandler();
        }
        return this.clickHandler;
    }

    @NotNull
    protected GuiComponentRenderer<P, I> getComponentRenderer() {
        if (this.componentRenderer == null) {
            return this.guiSettings.getComponentRenderer();
        }
        return this.componentRenderer;
    }

    @NotNull
    protected Component getTitle() {
        if (this.title == null) {
            throw new TriumphGuiException("Cannot create GUI with empty title!");
        }
        return this.title;
    }

    protected long getSpamPreventionDuration() {
        if (this.spamPreventionDuration < 0L) {
            return this.guiSettings.getSpamPreventionDuration();
        }
        return this.spamPreventionDuration;
    }
}

