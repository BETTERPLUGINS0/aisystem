/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntList
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport
 *  net.minecraft.network.protocol.game.PacketPlayOutMount
 *  net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity
 *  net.minecraft.server.level.EntityPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.PositionMoveRotation
 *  net.minecraft.world.level.World
 *  net.minecraft.world.phys.Vec3D
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.packets;

import com.google.common.collect.Sets;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityInterface;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.CraftBukkitBridge;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.packets.PacketBundle;
import it.unimi.dsi.fastutil.ints.IntList;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class AbstractPacketEntity<T extends net.minecraft.world.entity.Entity>
implements PacketEntityInterface {
    protected final T entity;
    private final Set<UUID> viewers = Sets.newConcurrentHashSet();
    private final List<Runnable> removeCallbacks = new LinkedList<Runnable>();
    protected boolean visible = true;
    private final int EntityID;
    private int currentVehicleId = -1;

    protected AbstractPacketEntity(Location location) {
        this.entity = this.createEntity(location);
        this.EntityID = this.entity.aA();
    }

    public List<Player> getViewersAsPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
        for (UUID viewer : this.viewers) {
            Player player = Bukkit.getPlayer((UUID)viewer);
            if (player == null) continue;
            players.add(player);
        }
        return players;
    }

    @Override
    public boolean hasViewers() {
        return !this.viewers.isEmpty();
    }

    protected abstract T createEntity(Location var1);

    @Override
    public void addViewer(UUID player) {
        this.viewers.add(player);
    }

    @Override
    public void removeViewer(UUID player) {
        this.viewers.remove(player);
    }

    @Override
    public void addRemoveCallback(Runnable callback) {
        this.removeCallbacks.add(callback);
    }

    protected World getNMSLevel(Location location) {
        return CraftBukkitBridge.getServerLevel(location);
    }

    public T getNMSEntity() {
        return this.entity;
    }

    @Override
    public <B extends Entity> B getBukkitEntity() {
        return (B)CraftBukkitBridge.getBukkitEntity(this.entity);
    }

    @Override
    public void syncMetadata() {
        this.sendPacketToAll(this.createEntityDataPacket());
    }

    @Override
    public Location getLocation() {
        return new Location(CraftBukkitBridge.getBukkitWorld(this.entity.ao()), this.entity.dP(), this.entity.dR(), this.entity.dV(), this.entity.ec(), this.entity.ee());
    }

    @Override
    public UUID getUniqueId() {
        return this.entity.cY();
    }

    protected Packet<?> createEntityDataPacket() {
        List dataValues = this.entity.aD().c();
        if (dataValues == null) {
            return null;
        }
        return new PacketPlayOutEntityMetadata(this.EntityID, dataValues);
    }

    public Packet<?> generateRemovePacket() {
        return new PacketPlayOutEntityDestroy(new int[]{this.EntityID});
    }

    @Override
    public void remove() {
        this.sendPacketToAll(this.generateRemovePacket());
        this.removeCallbacks.forEach(Runnable::run);
    }

    public Packet<?> generateSetVisiblePacket(boolean visible) {
        this.visible = visible;
        if (visible) {
            return this.createEntityDataPacket();
        }
        return new PacketPlayOutEntityDestroy(new int[]{this.EntityID});
    }

    @Override
    public void setVisible(boolean visible) {
        this.sendPacketToAll(this.generateSetVisiblePacket(visible));
    }

    @Override
    public void hideFrom(UUID uuid) {
        Player player = Bukkit.getPlayer((UUID)uuid);
        if (player == null) {
            this.removeViewer(uuid);
            return;
        }
        this.sendPacketToPlayer(player, new Packet[]{new PacketPlayOutEntityDestroy(new int[]{this.EntityID})});
        this.removeViewer(uuid);
    }

    public void displayTo(Player player) {
        if (this.viewers.contains(player.getUniqueId())) {
            return;
        }
        this.addViewer(player.getUniqueId());
        this.sendPacketToPlayer(player, new Packet[]{new PacketPlayOutSpawnEntity(this.EntityID, this.entity.cY(), this.entity.dP(), this.entity.dR(), this.entity.dV(), this.entity.ee(), this.entity.ec(), this.entity.ay(), 0, new Vec3D(0.0, 0.0, 0.0), 0.0), this.createEntityDataPacket()});
    }

    @Override
    public void displayTo(UUID uuid) {
        Player player = Bukkit.getPlayer((UUID)uuid);
        if (player != null) {
            this.displayTo(player);
        }
    }

    @Override
    public void teleport(Location location) {
        this.entity.b(location.getX(), location.getY(), location.getZ());
        this.sendTeleportPacket();
    }

    protected void sendTeleportPacket() {
        this.sendPacketToAll(this.generateTeleportPacket());
    }

    protected Packet<?> generateTeleportPacket() {
        return new PacketPlayOutEntityTeleport(this.EntityID, new PositionMoveRotation(this.entity.dI(), new Vec3D(0.0, 0.0, 0.0), this.entity.ec(), this.entity.ee()), new HashSet(), true);
    }

    public Packet<?> generateMovePacket(Location location) {
        if (this.viewers.isEmpty()) {
            return null;
        }
        this.entity.a_(location.getX(), location.getY(), location.getZ());
        this.entity.v(location.getYaw());
        this.entity.w(location.getPitch());
        return this.generateTeleportPacket();
    }

    public void move(Location location) {
        this.sendPacketToAll(this.generateMovePacket(location));
    }

    @Override
    public int getEntityId() {
        return this.EntityID;
    }

    @Override
    public void mountTo(int vehicleEntityId) {
        this.currentVehicleId = vehicleEntityId;
        this.sendPacketToAll(this.generateMountPacket(vehicleEntityId, this.EntityID));
    }

    @Override
    public void dismount() {
        if (this.currentVehicleId != -1) {
            this.sendPacketToAll(this.generateMountPacket(this.currentVehicleId, new int[0]));
            this.currentVehicleId = -1;
        }
    }

    protected Packet<?> generateMountPacket(int vehicleEntityId, int ... passengerIds) {
        try {
            PacketPlayOutMount packet = new PacketPlayOutMount(this.entity);
            for (Field field : PacketPlayOutMount.class.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getType() == Integer.TYPE) {
                    field.setInt(packet, vehicleEntityId);
                    continue;
                }
                if (!field.getType().getName().contains("IntList") && field.getType() != int[].class) continue;
                if (field.getType() == int[].class) {
                    field.set(packet, passengerIds);
                    continue;
                }
                field.set(packet, IntList.of((int[])passengerIds));
            }
            return packet;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create mount packet", e);
        }
    }

    protected void sendPacketToPlayer(Player player, Packet<?> ... nmsPackets) {
        EntityPlayer nmsPlayer = CraftBukkitBridge.getServerPlayer(player);
        for (Packet<?> nmsPacket : nmsPackets) {
            if (nmsPacket == null) continue;
            nmsPlayer.g.b(nmsPacket);
        }
    }

    protected void sendPacketToAll(Packet<?> nmsPacket) {
        if (nmsPacket == null) {
            return;
        }
        for (UUID viewer : this.viewers) {
            Player player = Bukkit.getPlayer((UUID)viewer);
            if (player == null) {
                this.viewers.remove(viewer);
                continue;
            }
            this.sendPacketToPlayer(player, nmsPacket);
        }
    }

    @Deprecated
    protected void sendPacket(Player player, Packet<?> ... nmsPackets) {
        this.sendPacketToPlayer(player, nmsPackets);
    }

    @Deprecated
    protected void sendPacket(Packet<?> nmsPacket) {
        this.sendPacketToAll(nmsPacket);
    }

    @Override
    public PacketBundle createPacketBundle() {
        return new PacketBundle();
    }

    protected EntityPlayer getNMSPlayer(Player bukkitPlayer) {
        return CraftBukkitBridge.getServerPlayer(bukkitPlayer);
    }
}

