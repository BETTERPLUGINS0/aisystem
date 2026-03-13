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
 *  org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.util.EulerAngle
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R2.packets;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractPacketBundle;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketModelEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R2.packets.AbstractPacketEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R2.packets.PacketBundle;
import com.mojang.math.Transformation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.EulerAngle;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PacketDisplayEntity
extends AbstractPacketEntity<Display.ItemDisplay>
implements PacketModelEntity {
    private ItemStack leatherHorseArmor;
    private net.minecraft.world.item.ItemStack nmsLeatherHorseArmor;
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
        return new Display.ItemDisplay(EntityTypes.ae, this.getNMSLevel(location));
    }

    @Override
    public void initializeModel(Location location, int modelID) {
        this.itemDisplay = (Display.ItemDisplay)this.entity;
        this.itemDisplay.c(-1);
        this.itemDisplay.b(1);
        try {
            Display.ItemDisplay display = this.itemDisplay;
            Method setPosRotInterpolationDuration = Display.class.getDeclaredMethod("d", Integer.TYPE);
            setPosRotInterpolationDuration.setAccessible(true);
            setPosRotInterpolationDuration.invoke(display, 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.leatherHorseArmor = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta)this.leatherHorseArmor.getItemMeta();
        itemMeta.setCustomModelData(Integer.valueOf(modelID));
        itemMeta.setColor(Color.WHITE);
        this.leatherHorseArmor.setItemMeta((ItemMeta)itemMeta);
        this.nmsLeatherHorseArmor = CraftItemStack.asNMSCopy((ItemStack)this.leatherHorseArmor);
        this.itemDisplay.a(this.nmsLeatherHorseArmor);
    }

    @Override
    public void setHorseLeatherArmorColor(Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)this.leatherHorseArmor.getItemMeta();
        leatherArmorMeta.setColor(color);
        this.leatherHorseArmor.setItemMeta((ItemMeta)leatherArmorMeta);
        this.nmsLeatherHorseArmor = CraftItemStack.asNMSCopy((ItemStack)this.leatherHorseArmor);
        this.itemDisplay.a(this.nmsLeatherHorseArmor);
    }

    @Override
    public void sendLocationAndRotationPacket(Location location, EulerAngle eulerAngle) {
        this.move(location);
        Quaternionf quaternionf = PacketDisplayEntity.eulerToQuaternion(Math.toDegrees(eulerAngle.getX()), Math.toDegrees(eulerAngle.getY()), Math.toDegrees(eulerAngle.getZ()));
        this.rotate(quaternionf);
        this.sendPacket(this.createEntityDataPacket());
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
        packetBundle.addPacket(this.generateMovePacket(location), this.getViewersAsPlayers());
        Quaternionf quaternionf = PacketDisplayEntity.eulerToQuaternion(Math.toDegrees(eulerAngle.getX()), Math.toDegrees(eulerAngle.getY()), Math.toDegrees(eulerAngle.getZ()));
        Transformation transformation = this.getTransformation();
        transformation = new Transformation(transformation.d(), quaternionf, new Vector3f(scaleX, scaleY, scaleZ), transformation.g());
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
        return this.getTransformation().f();
    }

    @Override
    public void setScale(float scale) {
        this.setScale(new Vector3f(scale, scale, scale));
    }

    public void setScale(Vector3f scale) {
        Transformation transformation = this.getTransformation();
        Transformation newTransformation = new Transformation(transformation.d(), transformation.e(), scale, transformation.g());
        this.setTransformation(newTransformation);
    }

    public Vector3f getTranslation() {
        return this.getTransformation().d();
    }

    public void setTranslation(Vector3f translation) {
        Transformation transformation = this.getTransformation();
        Transformation newTransformation = new Transformation(translation, transformation.e(), transformation.f(), transformation.g());
        this.setTransformation(newTransformation);
    }

    public Quaternionf getLeftRotation() {
        return this.getTransformation().e();
    }

    public void setLeftRotation(Quaternionf rotation) {
        Transformation transformation = this.getTransformation();
        Transformation newTransformation = new Transformation(transformation.d(), rotation, transformation.f(), transformation.g());
        this.setTransformation(newTransformation);
    }

    public Quaternionf getRightRotation() {
        return this.getTransformation().g();
    }

    public void setRightRotation(Quaternionf rotation) {
        Transformation transformation = this.getTransformation();
        Transformation newTransformation = new Transformation(transformation.d(), transformation.e(), transformation.f(), rotation);
        this.setTransformation(newTransformation);
    }

    public Transformation getTransformation() {
        Transformation nms = Display.a((DataWatcher)((Display.ItemDisplay)this.entity).al());
        return new Transformation(nms.d(), nms.e(), nms.f(), nms.g());
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

