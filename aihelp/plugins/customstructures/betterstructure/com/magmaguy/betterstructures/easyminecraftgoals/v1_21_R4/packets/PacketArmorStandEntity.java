/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.core.Vector3f
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment
 *  net.minecraft.world.entity.EntityTypes
 *  net.minecraft.world.entity.EnumItemSlot
 *  net.minecraft.world.entity.decoration.EntityArmorStand
 *  net.minecraft.world.item.ItemStack
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.util.EulerAngle
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R4.packets;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractPacketBundle;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketModelEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketTextEntity;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R4.packets.AbstractPacketEntity;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.Vector3f;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.EulerAngle;

public class PacketArmorStandEntity
extends AbstractPacketEntity<EntityArmorStand>
implements PacketModelEntity,
PacketTextEntity {
    private net.minecraft.world.item.ItemStack nmsLeatherHorseArmor;
    private ItemStack leatherHorseArmor;
    private EntityArmorStand armorStand;

    public PacketArmorStandEntity(Location location) {
        super(location);
    }

    @Override
    protected EntityArmorStand createEntity(Location location) {
        return new EntityArmorStand(EntityTypes.g, this.getNMSLevel(location));
    }

    @Override
    public void initializeModel(Location location, String modelID) {
        this.armorStand = (EntityArmorStand)this.entity;
        this.armorStand.k(true);
        this.armorStand.u(true);
        this.leatherHorseArmor = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta)this.leatherHorseArmor.getItemMeta();
        itemMeta.setItemModel(NamespacedKey.fromString((String)modelID));
        itemMeta.setColor(Color.WHITE);
        this.leatherHorseArmor.setItemMeta((ItemMeta)itemMeta);
        this.nmsLeatherHorseArmor = CraftItemStack.asNMSCopy((ItemStack)this.leatherHorseArmor);
        this.armorStand.a(EnumItemSlot.f, this.nmsLeatherHorseArmor);
    }

    @Override
    public void initializeText(Location location) {
        this.armorStand = (EntityArmorStand)this.entity;
        this.armorStand.k(true);
        this.armorStand.u(true);
    }

    @Override
    public void setScale(float scale) {
    }

    @Override
    public void setHorseLeatherArmorColor(Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)this.leatherHorseArmor.getItemMeta();
        leatherArmorMeta.setColor(color);
        this.leatherHorseArmor.setItemMeta((ItemMeta)leatherArmorMeta);
        this.nmsLeatherHorseArmor = CraftItemStack.asNMSCopy((ItemStack)this.leatherHorseArmor);
        this.armorStand.a(EnumItemSlot.f, this.nmsLeatherHorseArmor);
    }

    @Override
    public void sendLocationAndRotationPacket(Location location, EulerAngle eulerAngle) {
        this.move(location);
        this.rotate(eulerAngle);
    }

    @Override
    public void sendLocationAndRotationAndScalePacket(Location location, EulerAngle eulerAngle, float scale) {
        this.sendLocationAndRotationPacket(location, eulerAngle);
    }

    @Override
    public AbstractPacketBundle generateLocationAndRotationAndScalePackets(AbstractPacketBundle packetBundle, Location location, EulerAngle eulerAngle, float scale) {
        packetBundle.addPacket(this.generateMovePacket(location), this.getViewersAsPlayers());
        packetBundle.addPacket(this.createEntityDataPacket(), this.getViewersAsPlayers());
        return packetBundle;
    }

    @Override
    public void displayTo(Player player) {
        super.displayTo(player);
        if (this.nmsLeatherHorseArmor != null) {
            this.sendPacket(player, new Packet[]{new PacketPlayOutEntityEquipment(((EntityArmorStand)this.entity).ao(), List.of((Object)Pair.of((Object)EnumItemSlot.f, (Object)this.nmsLeatherHorseArmor)))});
        }
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

    private void rotate(EulerAngle eulerAngle) {
        if (eulerAngle == null) {
            return;
        }
        ((EntityArmorStand)this.entity).a(new Vector3f((float)Math.toDegrees(eulerAngle.getX()), (float)Math.toDegrees(eulerAngle.getY()), (float)Math.toDegrees(eulerAngle.getZ())));
        this.sendPacket(this.createEntityDataPacket());
    }

    @Override
    public void setText(String text) {
        this.armorStand.o(true);
        this.armorStand.b((IChatBaseComponent)IChatBaseComponent.b((String)text));
        this.sendPacket(this.createEntityDataPacket());
    }

    @Override
    public void setTextVisible(boolean visible) {
        this.armorStand.o(visible);
        this.sendPacket(this.createEntityDataPacket());
    }
}

