/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 */
package com.andrei1058.bedwars.arena;

import com.andrei1058.bedwars.BedWars;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

class PlayerGoods {
    private UUID uuid;
    private int level;
    private int foodLevel;
    private double health;
    private double healthscale;
    private float exp;
    private HashMap<ItemStack, Integer> items = new HashMap();
    private List<PotionEffect> potions = new ArrayList<PotionEffect>();
    private ItemStack[] armor;
    private HashMap<ItemStack, Integer> enderchest = new HashMap();
    private GameMode gamemode;
    private boolean allowFlight;
    private boolean flying;
    private String displayName;
    private String tabName;
    private static HashMap<UUID, PlayerGoods> playerGoods = new HashMap();

    PlayerGoods(Player p, boolean prepare) {
        this(p, prepare, false);
    }

    PlayerGoods(Player p, boolean prepare, boolean rejoin) {
        if (PlayerGoods.hasGoods(p)) {
            BedWars.plugin.getLogger().severe(p.getName() + " is already having a PlayerGoods vault :|");
            return;
        }
        this.uuid = p.getUniqueId();
        this.level = p.getLevel();
        this.exp = p.getExp();
        this.health = p.getHealth();
        this.healthscale = p.getHealthScale();
        this.foodLevel = p.getFoodLevel();
        playerGoods.put(p.getUniqueId(), this);
        int x = 0;
        for (ItemStack i : p.getInventory()) {
            if (i != null && i.getType() != Material.AIR) {
                this.items.put(i, x);
            }
            ++x;
        }
        for (PotionEffect ef : p.getActivePotionEffects()) {
            this.potions.add(ef);
            if (!prepare) continue;
            p.removePotionEffect(ef.getType());
        }
        this.armor = p.getInventory().getArmorContents();
        if (!rejoin) {
            int x2 = 0;
            for (ItemStack i : p.getEnderChest()) {
                if (i != null && i.getType() != Material.AIR) {
                    this.enderchest.put(i, x2);
                }
                ++x2;
            }
        }
        this.gamemode = p.getGameMode();
        this.allowFlight = p.getAllowFlight();
        this.flying = p.isFlying();
        this.tabName = p.getPlayerListName();
        this.displayName = p.getDisplayName();
        if (prepare) {
            p.setExp(0.0f);
            p.setLevel(0);
            p.setHealthScale(20.0);
            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            if (!rejoin) {
                p.getEnderChest().clear();
            }
            p.setGameMode(GameMode.SURVIVAL);
            p.setAllowFlight(false);
            p.setFlying(false);
        }
    }

    static boolean hasGoods(Player p) {
        return playerGoods.containsKey(p.getUniqueId());
    }

    static PlayerGoods getPlayerGoods(Player p) {
        return playerGoods.get(p.getUniqueId());
    }

    void restore() {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player == null) {
            return;
        }
        playerGoods.remove(player.getUniqueId());
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setLevel(this.level);
        player.setExp(this.exp);
        player.setHealthScale(this.healthscale);
        try {
            player.setHealth(this.health);
        } catch (Exception e) {
            BedWars.plugin.getLogger().severe("Something went wrong when restoring player health: " + this.health + ". Giving default of: 20");
            player.setHealth(20.0);
        }
        player.setFoodLevel(this.foodLevel);
        if (!this.items.isEmpty()) {
            for (Map.Entry entry : this.items.entrySet()) {
                player.getInventory().setItem(((Integer)entry.getValue()).intValue(), (ItemStack)entry.getKey());
            }
            player.updateInventory();
            this.items.clear();
        }
        if (!this.potions.isEmpty()) {
            for (PotionEffect potionEffect : this.potions) {
                player.addPotionEffect(potionEffect);
            }
            this.potions.clear();
        }
        player.getEnderChest().clear();
        if (!this.enderchest.isEmpty()) {
            for (Map.Entry entry : this.enderchest.entrySet()) {
                player.getEnderChest().setItem(((Integer)entry.getValue()).intValue(), (ItemStack)entry.getKey());
            }
            this.enderchest.clear();
        }
        player.getInventory().setArmorContents(this.armor);
        player.setGameMode(this.gamemode);
        player.setAllowFlight(this.allowFlight);
        player.setFlying(this.flying);
        if (!this.displayName.equals(player.getDisplayName())) {
            player.setDisplayName(this.displayName);
        }
        if (!this.tabName.equals(player.getPlayerListName())) {
            player.setPlayerListName(this.tabName);
        }
        this.uuid = null;
        this.items = null;
        this.potions = null;
        this.armor = null;
        this.enderchest = null;
    }
}

