/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package com.andrei1058.bedwars.listeners.dropshandler;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerDrops {
    private PlayerDrops() {
    }

    public static boolean handlePlayerDrops(IArena arena, Player victim, Player killer, ITeam victimsTeam, ITeam killersTeam, PlayerKillEvent.PlayerKillCause cause, List<ItemStack> inventory) {
        if (arena.getConfig().getBoolean("vanilla-death-drops")) {
            return false;
        }
        if (cause == PlayerKillEvent.PlayerKillCause.PLAYER_PUSH || cause == PlayerKillEvent.PlayerKillCause.PLAYER_PUSH_FINAL) {
            PlayerDrops.dropItems(victim, inventory);
            return true;
        }
        if (killer == null) {
            PlayerDrops.dropItems(victim, inventory);
            return true;
        }
        if (cause.isDespawnable()) {
            PlayerDrops.dropItems(victim, inventory);
            return true;
        }
        if (cause.isPvpLogOut()) {
            PlayerDrops.dropItems(victim, inventory);
            return true;
        }
        if (cause.isFinalKill() && victimsTeam != null) {
            Location dropsLocation = new Location(victim.getWorld(), (double)victimsTeam.getKillDropsLocation().getBlockX(), victimsTeam.getKillDropsLocation().getY(), victimsTeam.getKillDropsLocation().getZ());
            victim.getEnderChest().forEach(item -> {
                if (item != null) {
                    victim.getWorld().dropItemNaturally(dropsLocation, item);
                }
            });
            victim.getEnderChest().clear();
        }
        if (!(victimsTeam == null || victimsTeam.equals(killersTeam) && victim.equals((Object)killer))) {
            if (victimsTeam.isBedDestroyed()) {
                for (ItemStack i : inventory) {
                    if (i == null || i.getType() == Material.AIR || BedWars.nms.isArmor(i) || BedWars.nms.isBow(i) || BedWars.nms.isSword(i) || BedWars.nms.isTool(i) || !BedWars.nms.getShopUpgradeIdentifier(i).trim().isEmpty() || arena.getTeam(killer) == null) continue;
                    Vector vector = victimsTeam.getKillDropsLocation();
                    killer.getWorld().dropItemNaturally(new Location(arena.getWorld(), vector.getX(), vector.getY(), vector.getZ()), i);
                }
            } else {
                if (!arena.isPlayer(killer)) {
                    return true;
                }
                if (arena.isReSpawning(killer)) {
                    return true;
                }
                HashMap<Material, Integer> materialDrops = new HashMap<Material, Integer>();
                for (ItemStack itemStack : inventory) {
                    if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getType() != Material.DIAMOND && itemStack.getType() != Material.EMERALD && itemStack.getType() != Material.IRON_INGOT && itemStack.getType() != Material.GOLD_INGOT) continue;
                    killer.getInventory().addItem(new ItemStack[]{itemStack});
                    if (materialDrops.containsKey(itemStack.getType())) {
                        materialDrops.replace(itemStack.getType(), (Integer)materialDrops.get(itemStack.getType()) + itemStack.getAmount());
                        continue;
                    }
                    materialDrops.put(itemStack.getType(), itemStack.getAmount());
                }
                for (Map.Entry entry : materialDrops.entrySet()) {
                    String msg = "";
                    int amount = (Integer)entry.getValue();
                    switch ((Material)entry.getKey()) {
                        case DIAMOND: {
                            msg = Language.getMsg(killer, Messages.PLAYER_DIE_REWARD_DIAMOND).replace("{meaning}", amount == 1 ? Language.getMsg(killer, Messages.MEANING_DIAMOND_SINGULAR) : Language.getMsg(killer, Messages.MEANING_DIAMOND_PLURAL));
                            break;
                        }
                        case EMERALD: {
                            msg = Language.getMsg(killer, Messages.PLAYER_DIE_REWARD_EMERALD).replace("{meaning}", amount == 1 ? Language.getMsg(killer, Messages.MEANING_EMERALD_SINGULAR) : Language.getMsg(killer, Messages.MEANING_EMERALD_PLURAL));
                            break;
                        }
                        case IRON_INGOT: {
                            msg = Language.getMsg(killer, Messages.PLAYER_DIE_REWARD_IRON).replace("{meaning}", amount == 1 ? Language.getMsg(killer, Messages.MEANING_IRON_SINGULAR) : Language.getMsg(killer, Messages.MEANING_IRON_PLURAL));
                            break;
                        }
                        case GOLD_INGOT: {
                            msg = Language.getMsg(killer, Messages.PLAYER_DIE_REWARD_GOLD).replace("{meaning}", amount == 1 ? Language.getMsg(killer, Messages.MEANING_GOLD_SINGULAR) : Language.getMsg(killer, Messages.MEANING_GOLD_PLURAL));
                        }
                    }
                    killer.sendMessage(msg.replace("{amount}", String.valueOf(amount)));
                }
                materialDrops.clear();
            }
        }
        return true;
    }

    private static void dropItems(Player player, List<ItemStack> inventory) {
        for (ItemStack i : inventory) {
            if (i == null || i.getType() == Material.AIR || i.getType() != Material.DIAMOND && i.getType() != Material.EMERALD && i.getType() != Material.IRON_INGOT && i.getType() != Material.GOLD_INGOT) continue;
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), i);
        }
    }
}

