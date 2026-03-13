/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.ThrownPotion
 *  org.bukkit.entity.Zombie
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.potion.Potion
 *  org.bukkit.potion.PotionType
 */
package com.andrei1058.vipfeatures.api;

import com.andrei1058.vipfeatures.api.IVipFeatures;
import com.andrei1058.vipfeatures.api.event.BlockChangeEvent;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public enum SpellType {
    NONE(null, ""),
    EXPLOSIVE(location -> {
        location.getWorld().createExplosion(location, 2.0f);
        return null;
    }, "vipfeatures.spells.explosive"),
    FIRE(location -> {
        BlockChangeEvent bce = new BlockChangeEvent((Location)location, location.getBlock().getType(), Material.FIRE);
        if (!bce.isCancelled()) {
            location.getBlock().setType(Material.FIRE);
        }
        return null;
    }, "vipfeatures.spells.fire"),
    WEB(location -> {
        IVipFeatures api = (IVipFeatures)Bukkit.getServicesManager().getRegistration(IVipFeatures.class).getProvider();
        BlockChangeEvent bce = new BlockChangeEvent((Location)location, location.getBlock().getType(), Material.valueOf((String)api.getVersionUtil().getForCurrentVersion("WEB", "WEB", "COBWEB")));
        if (!bce.isCancelled()) {
            location.getBlock().setType(Material.valueOf((String)api.getVersionUtil().getForCurrentVersion("WEB", "WEB", "COBWEB")));
        }
        return null;
    }, "vipfeatures.spells.web"),
    ZOMBIE(location -> {
        Zombie zombie = (Zombie)location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setHealth(4.0);
        return zombie;
    }, "vipfeatures.spells.zombie"),
    POISON(location -> {
        Potion potion = new Potion(PotionType.POISON, 1);
        potion.setSplash(true);
        ItemStack iStack = new ItemStack(Material.POTION);
        potion.apply(iStack);
        ThrownPotion thrownPotion = (ThrownPotion)location.getWorld().spawnEntity(location, EntityType.SPLASH_POTION);
        thrownPotion.setItem(iStack);
        return null;
    }, "vipfeatures.spells.poison");

    private final Function<Location, Object> handler;
    private final String permission;

    private SpellType(Function<Location, Object> handler, String permission) {
        this.handler = handler;
        this.permission = permission;
    }

    public void execute(Location location, Player spellOwner) {
        Object o;
        if (this.handler != null && (o = this.handler.apply(location)) != null && o instanceof Zombie) {
            IVipFeatures api = (IVipFeatures)Bukkit.getServicesManager().getRegistration(IVipFeatures.class).getProvider();
            Zombie zombie = (Zombie)o;
            zombie.setMetadata(api.getSpellsUtil().getZombieOwnerMetaKey(), (MetadataValue)new FixedMetadataValue(api.getVipFeatures(), (Object)spellOwner.getName()));
        }
    }

    public String getPermission() {
        return this.permission;
    }
}

