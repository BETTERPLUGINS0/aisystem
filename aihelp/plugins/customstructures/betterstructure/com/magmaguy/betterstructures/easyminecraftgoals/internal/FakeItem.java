/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Display$Billboard
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface FakeItem {
    public void setItemStack(ItemStack var1);

    public ItemStack getItemStack();

    public void setVisible(boolean var1);

    public void displayTo(Player var1);

    public void displayTo(UUID var1);

    public void hideFrom(Player var1);

    public void hideFrom(UUID var1);

    public void teleport(Location var1);

    public Location getLocation();

    public void remove();

    public boolean hasViewers();

    default public void setScale(float scale) {
    }

    default public void setBillboard(Display.Billboard billboard) {
    }

    default public void setGlowing(boolean glowing) {
    }

    default public void setCustomName(String name) {
    }

    default public void setCustomNameVisible(boolean visible) {
    }

    default public void setYawRotation(float yawDegrees) {
    }

    public static interface Builder {
        public Builder itemStack(ItemStack var1);

        public Builder billboard(Display.Billboard var1);

        public Builder scale(float var1);

        public Builder viewRange(float var1);

        public Builder glowing(boolean var1);

        public Builder customName(String var1);

        public Builder customNameVisible(boolean var1);

        public FakeItem build(Location var1);
    }
}

