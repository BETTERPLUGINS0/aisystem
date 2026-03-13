/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.factions.FactionsPluginHook;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class AreaUtils {
    private static final List<String> ignoredEntityNamesPre1_20_6 = Arrays.asList("ARMOR_STAND", "ITEM_FRAME", "PAINTING", "LEASH_HITCH", "MINECART", "MINECART_CHEST", "MINECART_COMMAND", "MINECART_FURNACE", "MINECART_HOPPER", "MINECART_MOB_SPAWNER", "MINECART_TNT", "BOAT", "FISHING_HOOK", "DROPPED_ITEM", "ARROW", "SPECTRAL_ARROW", "EGG", "ENDER_PEARL", "ENDER_SIGNAL", "EXPERIENCE_ORB", "FIREBALL", "FIREWORK", "SMALL_FIREBALL", "LLAMA_SPIT", "SNOWBALL", "SPLASH_POTION", "THROWN_EXP_BOTTLE", "WITHER_SKULL", "SHULKER_BULLET", "PRIMED_TNT", "TRIDENT", "DRAGON_FIREBALL", "LIGHTNING", "AREA_EFFECT_CLOUD", "UNKNOWN");
    private static final List<String> ignoredEntityNames1_20_6 = Arrays.asList("ARMOR_STAND", "ITEM_FRAME", "PAINTING", "LEASH_KNOT", "MINECART", "CHEST_MINECART", "COMMAND_BLOCK_MINECART", "FURNACE_MINECART", "HOPPER_MINECART", "SPAWNER_MINECART", "TNT_MINECART", "BOAT", "ITEM", "ARROW", "SPECTRAL_ARROW", "EGG", "ENDER_PEARL", "EXPERIENCE_ORB", "FIREBALL", "FIREWORK_ROCKET", "SMALL_FIREBALL", "LLAMA_SPIT", "SNOWBALL", "POTION", "EXPERIENCE_BOTTLE", "WITHER_SKULL", "SHULKER_BULLET", "TNT", "TRIDENT", "DRAGON_FIREBALL", "LIGHTNING_BOLT", "AREA_EFFECT_CLOUD", "UNKNOWN");
    private static final List<EntityType> ignoredEntities = Collections.unmodifiableList((MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? ignoredEntityNames1_20_6 : ignoredEntityNamesPre1_20_6).stream().map(EntityType::fromName).collect(Collectors.toList()));

    public static List<LivingEntity> getEntitiesInRadius(int n, Entity entity, boolean bl, boolean bl2, boolean bl3) {
        ArrayList<LivingEntity> arrayList = new ArrayList<LivingEntity>();
        if (!bl3) {
            for (Entity entity2 : entity.getNearbyEntities((double)n, (double)n, (double)n)) {
                if (!(entity2 instanceof Player)) continue;
                Player player = (Player)entity2;
                if (bl) {
                    player.setMetadata("ae_ignore", (MetadataValue)new FixedMetadataValue((Plugin)ASManager.getInstance(), (Object)true));
                    EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(entity, (Entity)player, EntityDamageEvent.DamageCause.CUSTOM, 0.0);
                    Bukkit.getPluginManager().callEvent((Event)entityDamageByEntityEvent);
                    player.removeMetadata("ae_ignore", (Plugin)ASManager.getInstance());
                    if (entityDamageByEntityEvent.isCancelled() ? bl2 : !bl2) continue;
                }
                arrayList.add((LivingEntity)player);
            }
        } else {
            for (Entity entity3 : entity.getNearbyEntities((double)n, (double)n, (double)n)) {
                if (!(entity3 instanceof LivingEntity) || entity3 instanceof Player || entity3 instanceof ArmorStand) continue;
                arrayList.add((LivingEntity)entity3);
            }
        }
        return arrayList;
    }

    public static List<LivingEntity> getEntitiesInRadius(int n, Entity entity, RadiusTarget radiusTarget) {
        ArrayList<LivingEntity> arrayList = new ArrayList<LivingEntity>();
        switch (radiusTarget.ordinal()) {
            case 0: {
                for (Entity entity2 : entity.getNearbyEntities((double)n, (double)n, (double)n)) {
                    if (!(entity2 instanceof LivingEntity) || ignoredEntities.contains(entity2.getType())) continue;
                    arrayList.add((LivingEntity)entity2);
                }
                break;
            }
            case 1: {
                for (Entity entity3 : entity.getNearbyEntities((double)n, (double)n, (double)n)) {
                    if (!(entity3 instanceof Player)) continue;
                    arrayList.add((LivingEntity)((Player)entity3));
                }
                break;
            }
            case 2: {
                for (Entity entity4 : entity.getNearbyEntities((double)n, (double)n, (double)n)) {
                    if (!(entity4 instanceof LivingEntity) || !AreaUtils.isDamageable(entity, entity4) || ignoredEntities.contains(entity4.getType()) || entity4 instanceof Player) continue;
                    arrayList.add((LivingEntity)entity4);
                }
                break;
            }
            case 3: {
                for (Entity entity5 : entity.getNearbyEntities((double)n, (double)n, (double)n)) {
                    if (!(entity5 instanceof LivingEntity) || !AreaUtils.isDamageable(entity, entity5) || ignoredEntities.contains(entity5.getType())) continue;
                    arrayList.add((LivingEntity)entity5);
                }
                break;
            }
            case 4: {
                for (Entity entity6 : entity.getNearbyEntities((double)n, (double)n, (double)n)) {
                    if (!(entity6 instanceof LivingEntity) || AreaUtils.isDamageable(entity, entity6) || ignoredEntities.contains(entity6.getType())) continue;
                    arrayList.add((LivingEntity)entity6);
                }
                break;
            }
        }
        return arrayList;
    }

    public static boolean isDamageable(Entity entity, Entity entity2) {
        entity2.setMetadata("ae_ignore", (MetadataValue)new FixedMetadataValue((Plugin)ASManager.getInstance(), (Object)true));
        EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(entity, entity2, EntityDamageEvent.DamageCause.CUSTOM, 0.0);
        Bukkit.getPluginManager().callEvent((Event)entityDamageByEntityEvent);
        entity2.removeMetadata("ae_ignore", (Plugin)ASManager.getInstance());
        if (entity2 instanceof Player) {
            FactionsPluginHook factionsPluginHook;
            String string;
            Player player = (Player)entity2;
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                return false;
            }
            if (entity instanceof Player && HooksHandler.isEnabled(HookPlugin.FACTIONS) && (string = (factionsPluginHook = (FactionsPluginHook)HooksHandler.getHook(HookPlugin.FACTIONS)).getRelation(player, (Player)entity)).equals("member")) {
                return false;
            }
        }
        return !entityDamageByEntityEvent.isCancelled();
    }

    public static enum RadiusTarget {
        ALL,
        PLAYERS,
        MOBS,
        DAMAGEABLE,
        UNDAMAGEABLE;

    }
}

