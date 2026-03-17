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
import me.zombie_striker.qav.gui.guis.StorageGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class StorageBuilder
extends BaseGuiBuilder<StorageGui, StorageBuilder> {
    @Override
    @NotNull
    @Contract(value=" -> new")
    public StorageGui create() {
        StorageGui storageGui = new StorageGui(this.getRows(), Legacy.SERIALIZER.serialize(this.getTitle()), this.getModifiers());
        Consumer consumer = this.getConsumer();
        if (consumer != null) {
            consumer.accept(storageGui);
        }
        return storageGui;
    }
}

