/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.PacketPlayOutEntity$PacketPlayOutRelEntityMoveLook
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport
 *  net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity
 *  net.minecraft.server.level.EntityPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.World
 *  net.minecraft.world.phys.Vec3D
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_20_R1.CraftWorld
 *  org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.packets;

import com.google.common.collect.Sets;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityInterface;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R1.packets.PacketBundle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class AbstractPacketEntity<T extends net.minecraft.world.entity.Entity>
implements PacketEntityInterface {
    protected final T entity;
    private final Set<UUID> viewers = Sets.newConcurrentHashSet();
    private final List<Runnable> removeCallbacks = new LinkedList<Runnable>();
    protected boolean visible = true;

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

    protected AbstractPacketEntity(Location location) {
        this.entity = this.createEntity(location);
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
        return ((CraftWorld)location.getWorld()).getHandle();
    }

    public T getNMSEntity() {
        return this.entity;
    }

    @Override
    public <B extends Entity> B getBukkitEntity() {
        return (B)this.entity.getBukkitEntity();
    }

    @Override
    public void syncMetadata() {
        this.sendPacket(this.createEntityDataPacket());
    }

    @Override
    public void displayTo(UUID uuid) {
        Player player = Bukkit.getPlayer((UUID)uuid);
        if (player != null) {
            this.displayTo(player);
        }
    }

    @Override
    public Location getLocation() {
        return new Location((org.bukkit.World)this.entity.dI().getWorld(), this.entity.dn(), this.entity.dp(), this.entity.dt(), this.entity.dy(), this.entity.dA());
    }

    @Override
    public UUID getUniqueId() {
        return this.entity.ct();
    }

    protected Packet<?> createEntityDataPacket() {
        List dataValues = this.entity.aj().c();
        if (dataValues == null) {
            return null;
        }
        return new PacketPlayOutEntityMetadata(this.entity.af(), dataValues);
    }

    public Packet<?> generateRemovePacket() {
        return new PacketPlayOutEntityDestroy(new int[]{this.entity.af()});
    }

    @Override
    public void remove() {
        this.sendPacket(this.generateRemovePacket());
        this.removeCallbacks.forEach(Runnable::run);
    }

    public Packet<?> generateSetVisiblePacket(boolean visible) {
        this.visible = visible;
        if (visible) {
            return this.createEntityDataPacket();
        }
        return new PacketPlayOutEntityDestroy(new int[]{this.entity.af()});
    }

    @Override
    public void setVisible(boolean visible) {
        this.sendPacket(this.generateSetVisiblePacket(visible));
    }

    public Packet<?> generateHideFromPacket(UUID uuid) {
        if (!this.viewers.contains(uuid)) {
            return null;
        }
        this.removeViewer(uuid);
        Player player = Bukkit.getPlayer((UUID)uuid);
        if (player == null) {
            return null;
        }
        return new PacketPlayOutEntityDestroy(new int[]{this.entity.af()});
    }

    @Override
    public void hideFrom(UUID uuid) {
        this.sendPacket(this.generateHideFromPacket(uuid));
    }

    public List<Packet<?>> generateDisplayToPackets(Player player) {
        ArrayList packets = new ArrayList();
        if (this.viewers.contains(player.getUniqueId())) {
            return packets;
        }
        this.addViewer(player.getUniqueId());
        packets.add((Packet<?>)new PacketPlayOutSpawnEntity(this.entity.af(), this.entity.ct(), this.entity.dn(), this.entity.dp(), this.entity.dt(), this.entity.dA(), this.entity.dy(), this.entity.ae(), 0, new Vec3D(0.0, 0.0, 0.0), 0.0));
        packets.add(this.createEntityDataPacket());
        return packets;
    }

    public void displayTo(Player player) {
        this.generateDisplayToPackets(player).forEach(packet -> this.sendPacket(player, (Packet<?>)packet));
    }

    @Override
    public void teleport(Location location) {
        this.entity.b(location.getX(), location.getY(), location.getZ());
        this.sendTeleportPacket();
    }

    protected void sendTeleportPacket() {
        this.sendPacket(this.generateTeleportPacket());
    }

    protected Packet<?> generateTeleportPacket() {
        return new PacketPlayOutEntityTeleport(this.entity);
    }

    public Packet generateMovePacket(Location location) {
        double deltaZ;
        double deltaY;
        Location oldPos = this.getLocation();
        this.entity.e(location.getX(), location.getY(), location.getZ());
        this.entity.a_(location.getYaw());
        this.entity.b_(location.getPitch());
        Packet movePacket = null;
        if (this.viewers.isEmpty()) {
            return movePacket;
        }
        Location newPos = this.getLocation();
        if (oldPos.getWorld() != newPos.getWorld()) {
            return this.generateTeleportPacket();
        }
        double deltaX = newPos.getX() - oldPos.getX();
        if (deltaX * deltaX + (deltaY = newPos.getY() - oldPos.getY()) * deltaY + (deltaZ = newPos.getZ() - oldPos.getZ()) * deltaZ > 256.0) {
            return this.generateTeleportPacket();
        }
        return new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(this.entity.af(), (short)(deltaX * 4096.0), (short)(deltaY * 4096.0), (short)(deltaZ * 4096.0), (byte)(newPos.getYaw() * 256.0f / 360.0f), (byte)(newPos.getPitch() * 256.0f / 360.0f), true);
    }

    public void move(Location location) {
        this.sendPacket(this.generateMovePacket(location));
    }

    protected void sendPacket(Player player, Packet<?> ... nmsPackets) {
        EntityPlayer nmsPlayer = this.getNMSPlayer(player);
        for (Packet<?> nmsPacket : nmsPackets) {
            if (nmsPacket == null) continue;
            nmsPlayer.c.a(nmsPacket);
        }
    }

    protected void sendPacket(Packet<?> nmsPacket) {
        for (UUID viewer : this.viewers) {
            Player player = Bukkit.getPlayer((UUID)viewer);
            if (player == null) {
                this.viewers.remove(viewer);
                continue;
            }
            this.sendPacket(player, nmsPacket);
        }
    }

    @Override
    public PacketBundle createPacketBundle() {
        return new PacketBundle();
    }

    protected EntityPlayer getNMSPlayer(Player bukkitPlayer) {
        return ((CraftPlayer)bukkitPlayer).getHandle();
    }
}

