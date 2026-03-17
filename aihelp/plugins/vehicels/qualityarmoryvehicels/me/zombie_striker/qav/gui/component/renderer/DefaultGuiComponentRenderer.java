/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.component.renderer;

import java.util.Map;
import me.zombie_striker.qav.gui.AbstractGuiView;
import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import me.zombie_striker.qav.gui.component.GuiComponent;
import me.zombie_striker.qav.gui.component.ReactiveGuiComponent;
import me.zombie_striker.qav.gui.component.RenderedComponent;
import me.zombie_striker.qav.gui.component.renderer.GuiComponentRenderer;
import me.zombie_striker.qav.gui.container.MapBackedContainer;
import org.jetbrains.annotations.NotNull;

public final class DefaultGuiComponentRenderer<P, I>
implements GuiComponentRenderer<P, I> {
    @Override
    public void renderComponent(@NotNull P p, @NotNull GuiComponent<P, I> guiComponent, @NotNull AbstractGuiView<P, I> abstractGuiView) {
        ClickHandler<P> clickHandler = guiComponent.clickHandler();
        MapBackedContainer mapBackedContainer = new MapBackedContainer(clickHandler == null ? abstractGuiView.getDefaultClickHandler() : clickHandler, abstractGuiView.getContainerType());
        if (guiComponent instanceof ReactiveGuiComponent) {
            ((ReactiveGuiComponent)guiComponent).render(mapBackedContainer, p);
        }
        Map map = mapBackedContainer.complete();
        RenderedComponent<P, I> renderedComponent = new RenderedComponent<P, I>(guiComponent, map);
        abstractGuiView.completeRendered(renderedComponent);
    }
}

