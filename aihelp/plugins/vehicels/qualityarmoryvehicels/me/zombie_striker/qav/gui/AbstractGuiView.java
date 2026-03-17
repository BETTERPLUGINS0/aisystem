/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  dev.triumphteam.nova.Stateful
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui;

import dev.triumphteam.nova.Stateful;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.zombie_striker.qav.gui.GuiView;
import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import me.zombie_striker.qav.gui.click.processor.ClickProcessor;
import me.zombie_striker.qav.gui.component.GuiComponent;
import me.zombie_striker.qav.gui.component.RenderedComponent;
import me.zombie_striker.qav.gui.component.StatefulGuiComponent;
import me.zombie_striker.qav.gui.component.renderer.GuiComponentRenderer;
import me.zombie_striker.qav.gui.container.type.GuiContainerType;
import me.zombie_striker.qav.gui.item.RenderedGuiItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGuiView<P, I>
implements GuiView<P, I>,
Stateful {
    private final P viewer;
    private final List<GuiComponent<P, I>> components;
    private final GuiComponentRenderer<P, I> renderer;
    private final ClickHandler<P> defaultClickHandler;
    private final GuiContainerType containerType;
    private final ClickProcessor<P, I> clickProcessor;
    private final Map<GuiComponent<P, I>, RenderedComponent<P, I>> renderedComponents = new ConcurrentHashMap<GuiComponent<P, I>, RenderedComponent<P, I>>();
    private final Map<Integer, RenderedGuiItem<P, I>> allRenderedItems = new ConcurrentHashMap<Integer, RenderedGuiItem<P, I>>();

    public AbstractGuiView(@NotNull P p, @NotNull @NotNull List<@NotNull GuiComponent<P, I>> list, @NotNull GuiContainerType guiContainerType, @NotNull GuiComponentRenderer<P, I> guiComponentRenderer, @NotNull ClickHandler<P> clickHandler, @NotNull ClickProcessor<P, I> clickProcessor) {
        this.viewer = p;
        this.components = list;
        this.containerType = guiContainerType;
        this.renderer = guiComponentRenderer;
        this.defaultClickHandler = clickHandler;
        this.clickProcessor = clickProcessor;
    }

    @NotNull
    public P viewer() {
        return this.viewer;
    }

    @NotNull
    public abstract String viewerName();

    @NotNull
    public abstract UUID viewerUuid();

    protected abstract void clearSlot(int var1);

    protected abstract void populateInventory(@NotNull @NotNull Map<Integer, @NotNull RenderedGuiItem<P, I>> var1);

    protected void setup() {
        this.components.forEach(guiComponent -> {
            if (guiComponent instanceof StatefulGuiComponent) {
                ((StatefulGuiComponent)guiComponent).states().forEach(state -> state.addListener((Stateful)this, () -> this.renderer.renderComponent(this.viewer, (GuiComponent<P, I>)guiComponent, this)));
            }
            this.renderer.renderComponent(this.viewer, (GuiComponent<P, I>)guiComponent, this);
        });
    }

    public void completeRendered(@NotNull RenderedComponent<P, I> renderedComponent) {
        GuiComponent<P, I> guiComponent = renderedComponent.component();
        RenderedComponent<P, I> renderedComponent2 = this.renderedComponents.get(guiComponent);
        if (renderedComponent2 != null) {
            renderedComponent2.renderedItems().forEach((n, renderedGuiItem) -> {
                this.clearSlot((int)n);
                this.allRenderedItems.remove(n);
            });
        }
        this.renderedComponents.put(guiComponent, renderedComponent);
        Map<Integer, RenderedGuiItem<P, I>> map = renderedComponent.renderedItems();
        this.allRenderedItems.putAll(map);
        this.populateInventory(map);
    }

    @NotNull
    public ClickProcessor<P, I> getClickProcessor() {
        return this.clickProcessor;
    }

    @Nullable
    public RenderedGuiItem<P, I> getItem(int n) {
        return this.allRenderedItems.get(n);
    }

    @NotNull
    public ClickHandler<P> getDefaultClickHandler() {
        return this.defaultClickHandler;
    }

    @NotNull
    public GuiContainerType getContainerType() {
        return this.containerType;
    }
}

