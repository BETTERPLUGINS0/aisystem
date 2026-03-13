/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.upgrades.upgradeaction;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.upgrades.UpgradeAction;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class EnchantItemAction
implements UpgradeAction {
    private final Enchantment enchantment;
    private final int amplifier;
    private final ApplyType type;

    public EnchantItemAction(Enchantment enchantment, int amplifier, ApplyType type) {
        this.enchantment = enchantment;
        this.amplifier = amplifier;
        this.type = type;
    }

    @Override
    public void onBuy(Player player, ITeam bwt) {
        if (this.type == ApplyType.ARMOR) {
            bwt.addArmorEnchantment(this.enchantment, this.amplifier);
        } else if (this.type == ApplyType.SWORD) {
            bwt.addSwordEnchantment(this.enchantment, this.amplifier);
        } else if (this.type == ApplyType.BOW) {
            bwt.addBowEnchantment(this.enchantment, this.amplifier);
        }
    }

    public static enum ApplyType {
        SWORD,
        ARMOR,
        BOW;

    }
}

