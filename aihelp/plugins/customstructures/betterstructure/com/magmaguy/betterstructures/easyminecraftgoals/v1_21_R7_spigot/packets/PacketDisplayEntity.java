/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.math.Transformation
 *  net.minecraft.network.syncher.DataWatcher
 *  net.minecraft.world.entity.Display
 *  net.minecraft.world.entity.Display$ItemDisplay
 *  net.minecraft.world.entity.EntityTypes
 *  net.minecraft.world.item.ItemStack
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.components.CustomModelDataComponent
 *  org.bukkit.util.EulerAngle
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.packets;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractPacketBundle;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketModelEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.CraftBukkitBridge;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.packets.AbstractPacketEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.packets.PacketBundle;
import com.mojang.math.Transformation;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.util.EulerAngle;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class PacketDisplayEntity
extends AbstractPacketEntity<Display.ItemDisplay>
implements PacketModelEntity {
    private ItemStack carrierItem;
    private net.minecraft.world.item.ItemStack nmsCarrierItem;
    private Display.ItemDisplay itemDisplay;

    public PacketDisplayEntity(Location location) {
        super(location);
    }

    private static Quaternionf eulerToQuaternion(double originalX, double originalY, double originalZ) {
        double yaw = Math.toRadians(originalZ);
        double pitch = Math.toRadians(originalY);
        double roll = Math.toRadians(originalX);
        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);
        double w = cr * cp * cy + sr * sp * sy;
        double x = sr * cp * cy - cr * sp * sy;
        double y = cr * sp * cy + sr * cp * sy;
        double z = cr * cp * sy - sr * sp * cy;
        return new Quaternionf(x, y, z, w);
    }

    @Override
    protected Display.ItemDisplay createEntity(Location location) {
        return new Display.ItemDisplay(EntityTypes.aw, this.getNMSLevel(location));
    }

    @Override
    public void initializeModel(Location location, String modelID) {
        this.itemDisplay = (Display.ItemDisplay)this.entity;
        this.itemDisplay.b(-1);
        this.itemDisplay.a(0);
        CraftBukkitBridge.setDisplayTeleportDuration((Display)this.itemDisplay, 1);
        this.carrierItem = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        ItemMeta meta = this.carrierItem.getItemMeta();
        meta.setItemModel(NamespacedKey.fromString((String)modelID));
        CustomModelDataComponent cmd = meta.getCustomModelDataComponent();
        cmd.setColors(List.of((Object)Color.WHITE));
        meta.setCustomModelDataComponent(cmd);
        this.carrierItem.setItemMeta(meta);
        this.nmsCarrierItem = CraftBukkitBridge.asNMSCopy(this.carrierItem);
        this.itemDisplay.a(this.nmsCarrierItem);
        this.itemDisplay.x(0.0f);
        this.itemDisplay.y(0.0f);
        this.itemDisplay.b(30.0f);
    }

    @Override
    public void setHorseLeatherArmorColor(Color color) {
        if (this.carrierItem == null) {
            return;
        }
        ItemMeta meta = this.carrierItem.getItemMeta();
        CustomModelDataComponent cmd = meta.getCustomModelDataComponent();
        cmd.setColors(List.of((Object)color));
        meta.setCustomModelDataComponent(cmd);
        this.carrierItem.setItemMeta(meta);
        this.nmsCarrierItem = CraftBukkitBridge.asNMSCopy(this.carrierItem);
        this.itemDisplay.a(this.nmsCarrierItem);
    }

    @Override
    public void sendLocationAndRotationPacket(Location location, EulerAngle eulerAngle) {
        this.move(location);
        Quaternionf quaternionf = PacketDisplayEntity.eulerToQuaternion(Math.toDegrees(eulerAngle.getX()), Math.toDegrees(eulerAngle.getY()), Math.toDegrees(eulerAngle.getZ()));
        this.rotate(quaternionf);
        this.sendPacketToAll(this.createEntityDataPacket());
    }

    @Override
    public void sendLocationAndRotationAndScalePacket(Location location, EulerAngle eulerAngle, float scale) {
        this.generateLocationAndRotationAndScalePackets(new PacketBundle(), location, eulerAngle, scale).send();
    }

    @Override
    public void sendLocationAndRotationAndScalePacket(Location location, EulerAngle eulerAngle, float scaleX, float scaleY, float scaleZ) {
        this.generateLocationAndRotationAndScalePackets(new PacketBundle(), location, eulerAngle, scaleX, scaleY, scaleZ).send();
    }

    @Override
    public AbstractPacketBundle generateLocationAndRotationAndScalePackets(AbstractPacketBundle packetBundle, Location location, EulerAngle eulerAngle, float scale) {
        return this.generateLocationAndRotationAndScalePackets(packetBundle, location, eulerAngle, scale, scale, scale);
    }

    @Override
    public AbstractPacketBundle generateLocationAndRotationAndScalePackets(AbstractPacketBundle packetBundle, Location location, EulerAngle eulerAngle, float scaleX, float scaleY, float scaleZ) {
        if (!this.getLocation().getWorld().equals((Object)location.getWorld())) {
            packetBundle.addPacket(this.generateMovePacket(location), this.getViewersAsPlayers());
        } else {
            packetBundle.addPacket(this.generateMovePacket(location), this.getViewersAsPlayers());
        }
        Quaternionf quaternionf = PacketDisplayEntity.eulerToQuaternion(Math.toDegrees(eulerAngle.getX()), Math.toDegrees(eulerAngle.getY()), Math.toDegrees(eulerAngle.getZ()));
        Transformation transformation = this.getTransformation();
        transformation = new Transformation((Vector3fc)new Vector3f(0.0f, 0.0f, 0.0f), (Quaternionfc)quaternionf, (Vector3fc)new Vector3f(scaleX, scaleY, scaleZ), transformation.h());
        ((Display.ItemDisplay)this.entity).a(transformation);
        packetBundle.addPacket(this.createEntityDataPacket(), this.getViewersAsPlayers());
        return packetBundle;
    }

    @Override
    public void displayTo(Player player) {
        super.displayTo(player);
    }

    @Override
    public void displayTo(UUID player) {
        this.displayTo(Bukkit.getPlayer((UUID)player));
    }

    @Override
    public void addViewer(UUID player) {
        super.addViewer(player);
        this.displayTo(player);
    }

    public Vector3f getScale() {
        Vector3fc scale = this.getTransformation().g();
        return new Vector3f(scale.x(), scale.y(), scale.z());
    }

    @Override
    public void setScale(float scale) {
        this.setScale(new Vector3f(scale, scale, scale));
    }

    public void setScale(Vector3f scale) {
        Transformation transformation = this.getTransformation();
        Transformation newTransformation = new Transformation(transformation.e(), transformation.f(), (Vector3fc)scale, transformation.h());
        this.setTransformation(newTransformation);
    }

    public Vector3f getTranslation() {
        Vector3fc translation = this.getTransformation().e();
        return new Vector3f(translation.x(), translation.y(), translation.z());
    }

    public void setTranslation(Vector3f translation) {
        Transformation transformation = this.getTransformation();
        Transformation newTransformation = new Transformation((Vector3fc)translation, transformation.f(), transformation.g(), transformation.h());
        this.setTransformation(newTransformation);
    }

    public Quaternionf getLeftRotation() {
        Quaternionfc leftRotation = this.getTransformation().f();
        return new Quaternionf(leftRotation.x(), leftRotation.y(), leftRotation.z(), leftRotation.w());
    }

    public void setLeftRotation(Quaternionf rotation) {
        Transformation transformation = this.getTransformation();
        Transformation newTransformation = new Transformation(transformation.e(), (Quaternionfc)rotation, transformation.g(), transformation.h());
        this.setTransformation(newTransformation);
    }

    public Quaternionf getRightRotation() {
        Quaternionfc rightRotation = this.getTransformation().h();
        return new Quaternionf(rightRotation.x(), rightRotation.y(), rightRotation.z(), rightRotation.w());
    }

    public void setRightRotation(Quaternionf rotation) {
        Transformation transformation = this.getTransformation();
        Transformation newTransformation = new Transformation(transformation.e(), transformation.f(), transformation.g(), (Quaternionfc)rotation);
        this.setTransformation(newTransformation);
    }

    public Transformation getTransformation() {
        Transformation nms = Display.a((DataWatcher)((Display.ItemDisplay)this.entity).aD());
        return new Transformation(nms.e(), nms.f(), nms.g(), nms.h());
    }

    private void setTransformation(Transformation transformation) {
        ((Display.ItemDisplay)this.entity).a(transformation);
    }

    private void rotate(Quaternionf rotation) {
        if (rotation == null) {
            return;
        }
        this.setLeftRotation(rotation);
    }
}

