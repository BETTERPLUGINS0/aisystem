/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.guis;

import java.util.Set;
import me.zombie_striker.qav.gui.builder.gui.PaginatedBuilder;
import me.zombie_striker.qav.gui.builder.gui.ScrollingBuilder;
import me.zombie_striker.qav.gui.builder.gui.SimpleBuilder;
import me.zombie_striker.qav.gui.builder.gui.StorageBuilder;
import me.zombie_striker.qav.gui.components.GuiType;
import me.zombie_striker.qav.gui.components.InteractionModifier;
import me.zombie_striker.qav.gui.components.ScrollType;
import me.zombie_striker.qav.gui.guis.BaseGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Gui
extends BaseGui {
    public Gui(int n, @NotNull String string, @NotNull Set<InteractionModifier> set) {
        super(n, string, set);
    }

    public Gui(@NotNull GuiType guiType, @NotNull String string, @NotNull Set<InteractionModifier> set) {
        super(guiType, string, set);
    }

    @Deprecated
    public Gui(int n, @NotNull String string) {
        super(n, string);
    }

    @Deprecated
    public Gui(@NotNull String string) {
        super(1, string);
    }

    @Deprecated
    public Gui(@NotNull GuiType guiType, @NotNull String string) {
        super(guiType, string);
    }

    @NotNull
    @Contract(value="_ -> new")
    public static SimpleBuilder gui(@NotNull GuiType guiType) {
        return new SimpleBuilder(guiType);
    }

    @NotNull
    @Contract(value=" -> new")
    public static SimpleBuilder gui() {
        return Gui.gui(GuiType.CHEST);
    }

    @NotNull
    @Contract(value=" -> new")
    public static StorageBuilder storage() {
        return new StorageBuilder();
    }

    @NotNull
    @Contract(value=" -> new")
    public static PaginatedBuilder paginated() {
        return new PaginatedBuilder();
    }

    @NotNull
    @Contract(value="_ -> new")
    public static ScrollingBuilder scrolling(@NotNull ScrollType scrollType) {
        return new ScrollingBuilder(scrollType);
    }

    @NotNull
    @Contract(value=" -> new")
    public static ScrollingBuilder scrolling() {
        return Gui.scrolling(ScrollType.VERTICAL);
    }
}

