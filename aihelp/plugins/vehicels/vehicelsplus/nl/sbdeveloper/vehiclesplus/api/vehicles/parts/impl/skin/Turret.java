/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.entity.Fireball
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Generated;
import net.md_5.bungee.api.ChatColor;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.vehicles.HolderItemPosition;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.EquipablePart;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.PartTypeName;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Fireball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@PartTypeName(value="turret")
public class Turret
extends EquipablePart {
    private String identifier;
    private float explosionSize;
    private ItemStack ammo;
    @JsonIgnore
    private int cooldown = 0;

    public Turret() {
        this(-0.5, 0.0, 0.0, new ItemBuilder(XMaterial.LEATHER_CHESTPLATE.parseItem()).customModelData(3, itemBuilder -> itemBuilder.durability(3).unbreakable()).armorColor(Color.BLACK).getItemStack(), HolderItemPosition.HEAD, 3.0f, XMaterial.TNT.parseItem());
    }

    public Turret(double d, double d2, double d3, ItemStack itemStack, HolderItemPosition holderItemPosition, float f, ItemStack itemStack2) {
        this(MainUtil.generateRandomString(8), d, d2, d3, itemStack, holderItemPosition, f, itemStack2);
    }

    public Turret(String string, double d, double d2, double d3, ItemStack itemStack, HolderItemPosition holderItemPosition, float f, ItemStack itemStack2) {
        super(d, d2, d3, itemStack, holderItemPosition);
        this.identifier = string;
        this.explosionSize = f;
        this.ammo = itemStack2;
    }

    public boolean shoot() {
        Bukkit.getScheduler().runTask((Plugin)VehiclesPlusPluginManager.getVehiclesPlusPlugin(), () -> {
            if (this.cooldown == 0) {
                final Fireball fireball = (Fireball)this.holder.getWorld().spawn(this.holder.getEyeLocation().add(this.holder.getEyeLocation().getDirection().normalize().multiply(2)), Fireball.class);
                fireball.setIsIncendiary(true);
                fireball.setYield(this.explosionSize);
                Vector vector = this.holder.getLocation().getDirection();
                final Vector vector2 = new Vector(vector.getX(), 0.0, vector.getZ()).normalize().multiply(2);
                new BukkitRunnable(){

                    public void run() {
                        if (!fireball.isDead()) {
                            fireball.setVelocity(vector2);
                        } else {
                            this.cancel();
                        }
                    }
                }.runTaskTimer((Plugin)VehiclesPlusPluginManager.getVehiclesPlusPlugin(), 1L, 1L);
            }
            this.cooldown = 5;
            Bukkit.getScheduler().runTaskLater((Plugin)VehiclesPlusPluginManager.getVehiclesPlusPlugin(), () -> {
                this.cooldown = 0;
            }, 100L);
        });
        return this.cooldown != 0;
    }

    @Override
    public ItemStack getPartGUIItem() {
        return new ItemBuilder(XMaterial.LEATHER_BOOTS).displayname(String.valueOf(ChatColor.GOLD) + "Turret").lore(String.valueOf(ChatColor.GRAY) + "The turret of a tank.").unbreakable().durability(9).hideAllFlags().getItemStack();
    }

    @Override
    public String asString() {
        return String.valueOf(ChatColor.GOLD) + "Explosion size: " + String.valueOf(ChatColor.WHITE) + this.explosionSize + "\n" + String.valueOf(ChatColor.GOLD) + "Ammo: " + String.valueOf(ChatColor.WHITE) + this.ammo.getType().name() + "\n" + String.valueOf(ChatColor.GOLD) + "Rotation offset: " + String.valueOf(ChatColor.WHITE) + this.rotationOffset + "\n";
    }

    @Override
    public void despawnStand() {
        super.despawnStand();
        this.cooldown = 0;
    }

    @Generated
    public String getIdentifier() {
        return this.identifier;
    }

    @Generated
    public float getExplosionSize() {
        return this.explosionSize;
    }

    @Generated
    public ItemStack getAmmo() {
        return this.ammo;
    }

    @Generated
    public int getCooldown() {
        return this.cooldown;
    }

    @Generated
    public void setIdentifier(String string) {
        this.identifier = string;
    }

    @Generated
    public void setExplosionSize(float f) {
        this.explosionSize = f;
    }

    @Generated
    public void setAmmo(ItemStack itemStack) {
        this.ammo = itemStack;
    }

    @JsonIgnore
    @Generated
    public void setCooldown(int n) {
        this.cooldown = n;
    }
}

