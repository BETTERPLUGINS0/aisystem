/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.entity.Display$Billboard
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface FakeText {
    public void setText(String var1);

    public String getText();

    public void setVisible(boolean var1);

    public void displayTo(Player var1);

    public void displayTo(UUID var1);

    public void hideFrom(Player var1);

    public void hideFrom(UUID var1);

    public void teleport(Location var1);

    public Location getLocation();

    public void remove();

    public boolean hasViewers();

    default public void setTextOpacity(byte opacity) {
    }

    default public void setScale(float scale) {
    }

    default public void setBackgroundColor(Color color) {
    }

    default public void setBackgroundColor(int argb) {
    }

    default public void setShadowed(boolean shadow) {
    }

    default public void setSeeThrough(boolean seeThrough) {
    }

    default public void setBillboard(Display.Billboard billboard) {
    }

    default public void mountTo(Entity vehicle) {
    }

    default public void dismount() {
    }

    default public void attachTo(Entity vehicle) {
    }

    default public void detach() {
    }

    default public Entity getVehicle() {
        return null;
    }

    default public boolean isAutoTracked() {
        return false;
    }

    public static enum TextAlignment {
        CENTER,
        LEFT,
        RIGHT;

    }

    public static interface Builder {
        public Builder text(String var1);

        public Builder backgroundColor(Color var1);

        public Builder backgroundColor(int var1);

        public Builder textOpacity(byte var1);

        public Builder billboard(Display.Billboard var1);

        public Builder alignment(TextAlignment var1);

        public Builder shadow(boolean var1);

        public Builder seeThrough(boolean var1);

        public Builder lineWidth(int var1);

        public Builder viewRange(float var1);

        public Builder scale(float var1);

        public Builder translation(float var1, float var2, float var3);

        public FakeText build(Location var1);
    }
}

