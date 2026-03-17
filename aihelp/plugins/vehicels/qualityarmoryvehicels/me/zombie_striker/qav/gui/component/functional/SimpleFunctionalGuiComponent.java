/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  dev.triumphteam.nova.State
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.component.functional;

import dev.triumphteam.nova.State;
import java.util.List;
import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import me.zombie_striker.qav.gui.component.GuiComponent;
import me.zombie_striker.qav.gui.component.GuiComponentProducer;
import me.zombie_striker.qav.gui.component.ReactiveGuiComponent;
import me.zombie_striker.qav.gui.component.functional.AbstractFunctionalGuiComponent;
import me.zombie_striker.qav.gui.component.functional.FunctionalGuiComponent;
import me.zombie_striker.qav.gui.component.functional.FunctionalGuiComponentRender;
import me.zombie_striker.qav.gui.container.GuiContainer;
import me.zombie_striker.qav.gui.exception.TriumphGuiException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SimpleFunctionalGuiComponent<P, I>
extends AbstractFunctionalGuiComponent<P>
implements FunctionalGuiComponent<P, I>,
GuiComponentProducer<P, I> {
    private FunctionalGuiComponentRender<P, I> component = null;

    @Override
    public void render(@NotNull FunctionalGuiComponentRender<P, I> functionalGuiComponentRender) {
        this.component = functionalGuiComponentRender;
    }

    @Override
    @NotNull
    public GuiComponent<P, I> asGuiComponent() {
        if (this.component == null) {
            throw new TriumphGuiException("TODO");
        }
        return new ReactiveGuiComponent<P, I>(){

            @Override
            @Nullable
            public ClickHandler<P> clickHandler() {
                return SimpleFunctionalGuiComponent.this.getClickHandler();
            }

            @Override
            public void render(@NotNull @NotNull GuiContainer<@NotNull P, @NotNull I> guiContainer, @NotNull P p) {
                SimpleFunctionalGuiComponent.this.component.render(guiContainer, p);
            }

            @Override
            @NotNull
            public List<State> states() {
                return SimpleFunctionalGuiComponent.this.getStates();
            }
        };
    }
}

