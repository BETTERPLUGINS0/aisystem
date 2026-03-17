/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.builder.gui;

import java.util.function.Consumer;
import me.zombie_striker.qav.gui.builder.gui.BaseGuiBuilder;
import me.zombie_striker.qav.gui.components.GuiType;
import me.zombie_striker.qav.gui.components.util.Legacy;
import me.zombie_striker.qav.gui.guis.Gui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SimpleBuilder
extends BaseGuiBuilder<Gui, SimpleBuilder> {
    private GuiType guiType;

    public SimpleBuilder(@NotNull GuiType guiType) {
        this.guiType = guiType;
    }

    @NotNull
    @Contract(value="_ -> this")
    public SimpleBuilder type(@NotNull GuiType guiType) {
        this.guiType = guiType;
        return this;
    }

    @Override
    @NotNull
    @Contract(value=" -> new")
    public Gui create() {
        String string = Legacy.SERIALIZER.serialize(this.getTitle());
        Gui gui = this.guiType == null || this.guiType == GuiType.CHEST ? new Gui(this.getRows(), string, this.getModifiers()) : new Gui(this.guiType, string, this.getModifiers());
        Consumer consumer = this.getConsumer();
        if (consumer != null) {
            consumer.accept(gui);
        }
        return gui;
    }
}

