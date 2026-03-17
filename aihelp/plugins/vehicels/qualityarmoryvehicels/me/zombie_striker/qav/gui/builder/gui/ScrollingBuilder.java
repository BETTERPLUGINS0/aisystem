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
import me.zombie_striker.qav.gui.components.ScrollType;
import me.zombie_striker.qav.gui.components.util.Legacy;
import me.zombie_striker.qav.gui.guis.ScrollingGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ScrollingBuilder
extends BaseGuiBuilder<ScrollingGui, ScrollingBuilder> {
    private ScrollType scrollType;
    private int pageSize = 0;

    public ScrollingBuilder(@NotNull ScrollType scrollType) {
        this.scrollType = scrollType;
    }

    @NotNull
    @Contract(value="_ -> this")
    public ScrollingBuilder scrollType(@NotNull ScrollType scrollType) {
        this.scrollType = scrollType;
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public ScrollingBuilder pageSize(int n) {
        this.pageSize = n;
        return this;
    }

    @Override
    @NotNull
    @Contract(value=" -> new")
    public ScrollingGui create() {
        ScrollingGui scrollingGui = new ScrollingGui(this.getRows(), this.pageSize, Legacy.SERIALIZER.serialize(this.getTitle()), this.scrollType, this.getModifiers());
        Consumer consumer = this.getConsumer();
        if (consumer != null) {
            consumer.accept(scrollingGui);
        }
        return scrollingGui;
    }
}

