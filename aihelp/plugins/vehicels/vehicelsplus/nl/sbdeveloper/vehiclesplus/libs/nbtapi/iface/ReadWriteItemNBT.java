/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.meta.ItemMeta
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface;

import java.util.function.BiConsumer;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableItemNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBT;
import org.bukkit.inventory.meta.ItemMeta;

public interface ReadWriteItemNBT
extends ReadWriteNBT,
ReadableItemNBT {
    public boolean hasCustomNbtData();

    public void clearCustomNBT();

    public void modifyMeta(BiConsumer<ReadableNBT, ItemMeta> var1);

    public <T extends ItemMeta> void modifyMeta(Class<T> var1, BiConsumer<ReadableNBT, T> var2);
}

