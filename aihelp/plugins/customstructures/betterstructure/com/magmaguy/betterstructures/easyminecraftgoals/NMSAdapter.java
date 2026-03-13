/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.entity.Display$Billboard
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.inventory.ItemStack
 */
package com.magmaguy.betterstructures.easyminecraftgoals;

import com.magmaguy.betterstructures.easyminecraftgoals.NMSManager;
import com.magmaguy.betterstructures.easyminecraftgoals.constants.OverridableWanderPriority;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractPacketBundle;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractWanderBackToPoint;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeItem;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeItemSettings;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeText;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeTextSettings;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityInterface;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketModelEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketTextEntity;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class NMSAdapter {
    public abstract boolean move(LivingEntity var1, double var2, Location var4);

    public abstract boolean forcedMove(LivingEntity var1, double var2, Location var4);

    public abstract void universalMove(LivingEntity var1, double var2, Location var4);

    public abstract AbstractWanderBackToPoint wanderBackToPoint(LivingEntity var1, Location var2, double var3, int var5, OverridableWanderPriority var6);

    public abstract boolean setCustomHitbox(Entity var1, float var2, float var3, boolean var4);

    public abstract float getBodyRotation(Entity var1);

    public abstract PacketModelEntity createPacketArmorStandEntity(Location var1);

    public abstract PacketModelEntity createPacketDisplayEntity(Location var1);

    public abstract PacketTextEntity createPacketTextArmorStandEntity(Location var1);

    public abstract void doNotMove(LivingEntity var1);

    public @Nullable AbstractWanderBackToPoint wanderBackToPoint(@NonNull LivingEntity livingEntity, @NonNull Location blockLocation, double maximumDistanceFromPoint, int maxDurationTicks) {
        OverridableWanderPriority overridableWanderPriority;
        try {
            overridableWanderPriority = OverridableWanderPriority.valueOf(livingEntity.getType().name());
        } catch (Exception ex) {
            NMSManager.pluginProvider.getLogger().warning("[EasyMinecraftPathfinding] Attempted to assign return point to entity type " + livingEntity.getType().name() + " which is not a currently accepted entity type!");
            return null;
        }
        return this.wanderBackToPoint(livingEntity, blockLocation, maximumDistanceFromPoint, maxDurationTicks, overridableWanderPriority);
    }

    public boolean canReach(LivingEntity livingEntity, Location location) {
        return false;
    }

    public abstract void setBlockInNativeDataPalette(World var1, int var2, int var3, int var4, BlockData var5, boolean var6);

    public abstract AbstractPacketBundle createPacketBundle();

    public PacketEntityInterface createPacketEntity(EntityType entityType, Location location) {
        throw new UnsupportedOperationException("createPacketEntity is only supported in Minecraft 1.21.11+");
    }

    public abstract FakeText createFakeText(Location var1, FakeTextSettings var2);

    public FakeText.Builder fakeTextBuilder() {
        return new FakeTextBuilderImpl(this);
    }

    public FakeItem createFakeItem(Location location, FakeItemSettings settings) {
        throw new UnsupportedOperationException("createFakeItem is only supported in Minecraft 1.21.4+");
    }

    public FakeItem.Builder fakeItemBuilder() {
        return new FakeItemBuilderImpl(this);
    }

    private static class FakeTextBuilderImpl
    implements FakeText.Builder {
        private final NMSAdapter adapter;
        private final FakeTextSettings settings = new FakeTextSettings();

        FakeTextBuilderImpl(NMSAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public FakeText.Builder text(String text) {
            this.settings.setText(text);
            return this;
        }

        @Override
        public FakeText.Builder backgroundColor(Color color) {
            this.settings.setBackgroundColor(color);
            return this;
        }

        @Override
        public FakeText.Builder backgroundColor(int argb) {
            this.settings.setBackgroundArgb(argb);
            return this;
        }

        @Override
        public FakeText.Builder textOpacity(byte opacity) {
            this.settings.setTextOpacity(opacity);
            return this;
        }

        @Override
        public FakeText.Builder billboard(Display.Billboard billboard) {
            this.settings.setBillboard(billboard);
            return this;
        }

        @Override
        public FakeText.Builder alignment(FakeText.TextAlignment alignment) {
            this.settings.setAlignment(alignment);
            return this;
        }

        @Override
        public FakeText.Builder shadow(boolean shadow) {
            this.settings.setShadow(shadow);
            return this;
        }

        @Override
        public FakeText.Builder seeThrough(boolean seeThrough) {
            this.settings.setSeeThrough(seeThrough);
            return this;
        }

        @Override
        public FakeText.Builder lineWidth(int width) {
            this.settings.setLineWidth(width);
            return this;
        }

        @Override
        public FakeText.Builder viewRange(float range) {
            this.settings.setViewRange(range);
            return this;
        }

        @Override
        public FakeText.Builder scale(float scale) {
            this.settings.setScale(scale);
            return this;
        }

        @Override
        public FakeText.Builder translation(float x, float y, float z) {
            this.settings.setTranslation(x, y, z);
            return this;
        }

        @Override
        public FakeText build(Location location) {
            return this.adapter.createFakeText(location, this.settings);
        }
    }

    private static class FakeItemBuilderImpl
    implements FakeItem.Builder {
        private final NMSAdapter adapter;
        private final FakeItemSettings settings = new FakeItemSettings();

        FakeItemBuilderImpl(NMSAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public FakeItem.Builder itemStack(ItemStack itemStack) {
            this.settings.setItemStack(itemStack);
            return this;
        }

        @Override
        public FakeItem.Builder billboard(Display.Billboard billboard) {
            this.settings.setBillboard(billboard);
            return this;
        }

        @Override
        public FakeItem.Builder scale(float scale) {
            this.settings.setScale(scale);
            return this;
        }

        @Override
        public FakeItem.Builder viewRange(float range) {
            this.settings.setViewRange(range);
            return this;
        }

        @Override
        public FakeItem.Builder glowing(boolean glowing) {
            this.settings.setGlowing(glowing);
            return this;
        }

        @Override
        public FakeItem.Builder customName(String name) {
            this.settings.setCustomName(name);
            return this;
        }

        @Override
        public FakeItem.Builder customNameVisible(boolean visible) {
            this.settings.setCustomNameVisible(visible);
            return this;
        }

        @Override
        public FakeItem build(Location location) {
            return this.adapter.createFakeItem(location, this.settings);
        }
    }
}

