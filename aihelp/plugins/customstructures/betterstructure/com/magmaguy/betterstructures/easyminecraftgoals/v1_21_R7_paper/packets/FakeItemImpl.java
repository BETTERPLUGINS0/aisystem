/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.world.entity.Display$ItemDisplay
 *  net.minecraft.world.entity.EntityTypes
 *  net.minecraft.world.item.ItemStack
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Display$Billboard
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.ItemDisplay
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Transformation
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_paper.packets;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeItem;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeItemSettings;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityTracker;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.TrackedPacketEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_paper.CraftBukkitBridge;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_paper.packets.AbstractPacketEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FakeItemImpl
implements FakeItem,
TrackedPacketEntity {
    private final FakeItemSettings settings;
    private Location location;
    private ItemStack currentItemStack;
    private boolean visible = true;
    private final UUID uniqueId = UUID.randomUUID();
    private final Map<UUID, ItemDisplayPacketEntity> playerEntities = new ConcurrentHashMap<UUID, ItemDisplayPacketEntity>();
    private final List<Runnable> removeCallbacks = new ArrayList<Runnable>();
    private int currentVehicleId = -1;
    private Entity vehicleEntity = null;
    private boolean autoTracked = false;
    private boolean valid = true;

    public FakeItemImpl(Location location, FakeItemSettings settings) {
        this.location = location.clone();
        this.settings = new FakeItemSettings(settings);
        this.currentItemStack = settings.getItemStack() != null ? settings.getItemStack().clone() : null;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        this.currentItemStack = itemStack != null ? itemStack.clone() : null;
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            this.applyItemStack(entity);
            entity.syncMetadata();
        }
    }

    @Override
    public ItemStack getItemStack() {
        return this.currentItemStack != null ? this.currentItemStack.clone() : null;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            entity.setVisible(visible);
        }
    }

    @Override
    public void displayTo(Player player) {
        if (player == null) {
            return;
        }
        UUID uuid = player.getUniqueId();
        if (this.playerEntities.containsKey(uuid)) {
            return;
        }
        ItemDisplayPacketEntity entity = this.createItemDisplayEntity(this.location);
        this.playerEntities.put(uuid, entity);
        entity.displayTo(uuid);
        if (this.currentVehicleId != -1) {
            entity.mountTo(this.currentVehicleId);
        }
    }

    @Override
    public void displayTo(UUID uuid) {
        Player player = Bukkit.getPlayer((UUID)uuid);
        if (player != null) {
            this.displayTo(player);
        }
    }

    @Override
    public void hideFrom(Player player) {
        if (player == null) {
            return;
        }
        this.hideFrom(player.getUniqueId());
    }

    @Override
    public void hideFrom(UUID uuid) {
        ItemDisplayPacketEntity entity = this.playerEntities.remove(uuid);
        if (entity != null) {
            entity.remove();
        }
    }

    @Override
    public void teleport(Location location) {
        this.location = location.clone();
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            entity.teleport(location);
        }
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public void remove() {
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            entity.remove();
        }
        this.playerEntities.clear();
        this.removeCallbacks.forEach(Runnable::run);
        this.valid = false;
    }

    @Override
    public boolean hasViewers() {
        return !this.playerEntities.isEmpty();
    }

    @Override
    public void setScale(float scale) {
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            ItemDisplay itemDisplay = (ItemDisplay)entity.getBukkitEntity();
            Transformation transform = itemDisplay.getTransformation();
            itemDisplay.setTransformation(new Transformation(transform.getTranslation(), transform.getLeftRotation(), new Vector3f(scale, scale, scale), transform.getRightRotation()));
            entity.syncMetadata();
        }
    }

    @Override
    public void setBillboard(Display.Billboard billboard) {
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            ItemDisplay itemDisplay = (ItemDisplay)entity.getBukkitEntity();
            itemDisplay.setBillboard(billboard);
            entity.syncMetadata();
        }
    }

    @Override
    public void setGlowing(boolean glowing) {
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            Display.ItemDisplay nmsEntity = (Display.ItemDisplay)entity.getNMSEntity();
            nmsEntity.k(glowing);
            entity.syncMetadata();
        }
    }

    @Override
    public void setCustomName(String name) {
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            Display.ItemDisplay nmsEntity = (Display.ItemDisplay)entity.getNMSEntity();
            if (name != null && !name.isEmpty()) {
                nmsEntity.b((IChatBaseComponent)IChatBaseComponent.b((String)name));
            } else {
                nmsEntity.b(null);
            }
            entity.syncMetadata();
        }
    }

    @Override
    public void setCustomNameVisible(boolean visible) {
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            Display.ItemDisplay nmsEntity = (Display.ItemDisplay)entity.getNMSEntity();
            nmsEntity.p(visible);
            entity.syncMetadata();
        }
    }

    @Override
    public void setYawRotation(float yawDegrees) {
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            ItemDisplay itemDisplay = (ItemDisplay)entity.getBukkitEntity();
            Transformation transform = itemDisplay.getTransformation();
            float radians = (float)Math.toRadians(yawDegrees);
            Quaternionf yawRotation = new Quaternionf().rotateY(radians);
            itemDisplay.setTransformation(new Transformation(transform.getTranslation(), transform.getLeftRotation(), transform.getScale(), yawRotation));
            entity.syncMetadata();
        }
    }

    @Override
    public Location getTrackingLocation() {
        if (this.vehicleEntity != null && this.vehicleEntity.isValid()) {
            return this.vehicleEntity.getLocation();
        }
        return this.location;
    }

    @Override
    public World getWorld() {
        if (this.vehicleEntity != null && this.vehicleEntity.isValid()) {
            return this.vehicleEntity.getWorld();
        }
        return this.location != null ? this.location.getWorld() : null;
    }

    @Override
    public void showToPlayer(Player player) {
        this.displayTo(player);
    }

    @Override
    public void hideFromPlayer(Player player) {
        this.hideFrom(player);
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return player != null && this.playerEntities.containsKey(player.getUniqueId());
    }

    @Override
    public Set<UUID> getCurrentViewers() {
        return new HashSet<UUID>(this.playerEntities.keySet());
    }

    @Override
    public boolean isValid() {
        return this.valid && (this.vehicleEntity == null || this.vehicleEntity.isValid());
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public Entity getVehicle() {
        return this.vehicleEntity;
    }

    @Override
    public void remount() {
        if (this.vehicleEntity != null && this.vehicleEntity.isValid()) {
            this.currentVehicleId = this.vehicleEntity.getEntityId();
            for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
                entity.mountTo(this.currentVehicleId);
            }
        }
    }

    public void attachTo(Entity vehicle) {
        if (vehicle == null) {
            return;
        }
        this.vehicleEntity = vehicle;
        this.currentVehicleId = vehicle.getEntityId();
        this.autoTracked = true;
        this.valid = true;
        PacketEntityTracker.getInstance().register(this);
    }

    public void detach() {
        if (this.autoTracked) {
            PacketEntityTracker.getInstance().unregister(this);
            for (UUID viewerUUID : new HashSet<UUID>(this.playerEntities.keySet())) {
                Player player = Bukkit.getPlayer((UUID)viewerUUID);
                if (player == null) continue;
                this.hideFrom(player);
            }
            this.dismount();
            this.autoTracked = false;
            this.valid = false;
        }
    }

    public void mountTo(Entity vehicle) {
        if (vehicle == null) {
            return;
        }
        this.currentVehicleId = vehicle.getEntityId();
        for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
            entity.mountTo(this.currentVehicleId);
        }
    }

    public void dismount() {
        if (this.currentVehicleId != -1) {
            for (ItemDisplayPacketEntity entity : this.playerEntities.values()) {
                entity.dismount();
            }
            this.currentVehicleId = -1;
            this.vehicleEntity = null;
        }
    }

    public void addRemoveCallback(Runnable callback) {
        this.removeCallbacks.add(callback);
    }

    private ItemDisplayPacketEntity createItemDisplayEntity(Location location) {
        ItemDisplayPacketEntity entity = new ItemDisplayPacketEntity(location);
        ItemDisplay itemDisplay = (ItemDisplay)entity.getBukkitEntity();
        this.applyItemStack(entity);
        itemDisplay.setBillboard(this.settings.getBillboard());
        itemDisplay.setViewRange(this.settings.getViewRange());
        if (this.settings.getScale() != 1.0f) {
            Transformation transform = itemDisplay.getTransformation();
            float scale = this.settings.getScale();
            itemDisplay.setTransformation(new Transformation(transform.getTranslation(), transform.getLeftRotation(), new Vector3f(scale, scale, scale), transform.getRightRotation()));
        }
        if (this.settings.isGlowing()) {
            ((Display.ItemDisplay)entity.getNMSEntity()).k(true);
        }
        if (this.settings.hasCustomName()) {
            ((Display.ItemDisplay)entity.getNMSEntity()).b((IChatBaseComponent)IChatBaseComponent.b((String)this.settings.getCustomName()));
            ((Display.ItemDisplay)entity.getNMSEntity()).p(this.settings.isCustomNameVisible());
        }
        entity.syncMetadata();
        return entity;
    }

    private void applyItemStack(ItemDisplayPacketEntity entity) {
        if (this.currentItemStack != null) {
            net.minecraft.world.item.ItemStack nmsItem = CraftBukkitBridge.asNMSCopy(this.currentItemStack);
            ((Display.ItemDisplay)entity.getNMSEntity()).a(nmsItem);
        }
    }

    private static class ItemDisplayPacketEntity
    extends AbstractPacketEntity<Display.ItemDisplay> {
        public ItemDisplayPacketEntity(Location location) {
            super(location);
        }

        @Override
        protected Display.ItemDisplay createEntity(Location location) {
            return new Display.ItemDisplay(EntityTypes.aw, this.getNMSLevel(location));
        }
    }
}

