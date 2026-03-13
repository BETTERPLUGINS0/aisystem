/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffectType
 */
package com.andrei1058.bedwars.upgrades.upgradeaction;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.upgrades.UpgradeAction;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class PlayerEffectAction
implements UpgradeAction {
    private final PotionEffectType potionEffectType;
    private final int amplifier;
    private int duration;
    private final ApplyType type;

    public PlayerEffectAction(PotionEffectType potionEffectType, int amplifier, int duration, ApplyType type) {
        this.potionEffectType = potionEffectType;
        this.amplifier = amplifier;
        this.type = type;
        this.duration = duration;
        if (duration < 0) {
            this.duration *= -1;
        }
        this.duration = duration == 0 ? Integer.MAX_VALUE : (this.duration *= 20);
    }

    @Override
    public void onBuy(Player player, ITeam bwt) {
        if (this.type == ApplyType.BASE) {
            bwt.addBaseEffect(this.potionEffectType, this.amplifier, this.duration);
        } else if (this.type == ApplyType.TEAM) {
            bwt.addTeamEffect(this.potionEffectType, this.amplifier, this.duration);
        }
    }

    public static enum ApplyType {
        TEAM,
        BASE;

    }
}

