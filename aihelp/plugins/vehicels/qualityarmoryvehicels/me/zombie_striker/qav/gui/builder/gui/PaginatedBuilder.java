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
import me.zombie_striker.qav.gui.components.util.Legacy;
import me.zombie_striker.qav.gui.guis.PaginatedGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PaginatedBuilder
extends BaseGuiBuilder<PaginatedGui, PaginatedBuilder> {
    private int pageSize = 0;

    @NotNull
    @Contract(value="_ -> this")
    public PaginatedBuilder pageSize(int n) {
        this.pageSize = n;
        return this;
    }

    @Override
    @NotNull
    @Contract(value=" -> new")
    public PaginatedGui create() {
        PaginatedGui paginatedGui = new PaginatedGui(this.getRows(), this.pageSize, Legacy.SERIALIZER.serialize(this.getTitle()), this.getModifiers());
        Consumer consumer = this.getConsumer();
        if (consumer != null) {
            consumer.accept(paginatedGui);
        }
        return paginatedGui;
    }
}

