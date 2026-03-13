/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.upgrades.trapaction;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.upgrades.TrapAction;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DisenchantAction
implements TrapAction {
    private Enchantment enchantment;
    private ApplyType type;

    public DisenchantAction(Enchantment enchantment, ApplyType type) {
        this.enchantment = enchantment;
        this.type = type;
    }

    @Override
    public String getName() {
        return "disenchant-item";
    }

    @Override
    public void onTrigger(@NotNull Player player, ITeam playerTeam, ITeam targetTeam) {
        block9: {
            block10: {
                block8: {
                    if (this.type != ApplyType.SWORD) break block8;
                    for (ItemStack i : player.getInventory()) {
                        if (BedWars.nms.isSword(i)) {
                            i.removeEnchantment(this.enchantment);
                        }
                        player.updateInventory();
                    }
                    break block9;
                }
                if (this.type != ApplyType.ARMOR) break block10;
                for (ItemStack i : player.getInventory()) {
                    if (BedWars.nms.isArmor(i)) {
                        i.removeEnchantment(this.enchantment);
                    }
                    player.updateInventory();
                }
                for (ItemStack i : player.getInventory().getArmorContents()) {
                    if (BedWars.nms.isArmor(i)) {
                        i.removeEnchantment(this.enchantment);
                    }
                    player.updateInventory();
                }
                break block9;
            }
            if (this.type != ApplyType.BOW) break block9;
            for (ItemStack i : player.getInventory()) {
                if (BedWars.nms.isBow(i)) {
                    i.removeEnchantment(this.enchantment);
                }
                player.updateInventory();
            }
        }
    }

    public static enum ApplyType {
        SWORD,
        ARMOR,
        BOW;

    }
}

