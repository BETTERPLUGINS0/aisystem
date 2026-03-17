/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.MapMeta
 *  org.bukkit.map.MapView
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.builder.item;

import me.zombie_striker.qav.gui.builder.item.BaseItemBuilder;
import me.zombie_striker.qav.gui.components.exception.GuiException;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapBuilder
extends BaseItemBuilder<MapBuilder> {
    private static final Material MAP = Material.MAP;

    MapBuilder() {
        super(new ItemStack(MAP));
    }

    MapBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (itemStack.getType() != MAP) {
            throw new GuiException("MapBuilder requires the material to be a MAP!");
        }
    }

    @Override
    @NotNull
    @Contract(value="_ -> this")
    public MapBuilder color(@Nullable Color color) {
        MapMeta mapMeta = (MapMeta)this.getMeta();
        mapMeta.setColor(color);
        this.setMeta((ItemMeta)mapMeta);
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public MapBuilder locationName(@Nullable String string) {
        MapMeta mapMeta = (MapMeta)this.getMeta();
        mapMeta.setLocationName(string);
        this.setMeta((ItemMeta)mapMeta);
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public MapBuilder scaling(boolean bl) {
        MapMeta mapMeta = (MapMeta)this.getMeta();
        mapMeta.setScaling(bl);
        this.setMeta((ItemMeta)mapMeta);
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public MapBuilder view(@NotNull MapView mapView) {
        MapMeta mapMeta = (MapMeta)this.getMeta();
        mapMeta.setMapView(mapView);
        this.setMeta((ItemMeta)mapMeta);
        return this;
    }
}

