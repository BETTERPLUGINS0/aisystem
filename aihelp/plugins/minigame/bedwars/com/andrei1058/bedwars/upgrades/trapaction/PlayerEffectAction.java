/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.upgrades.trapaction;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.upgrades.TrapAction;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PlayerEffectAction
implements TrapAction {
    private PotionEffectType potionEffectType;
    private int amplifier;
    private int duration;
    private ApplyType type;

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
    public String getName() {
        return "player-effect";
    }

    @Override
    public void onTrigger(@NotNull Player player, ITeam playerTeam, ITeam targetTeam) {
        if (this.type == ApplyType.TEAM) {
            for (Player p : targetTeam.getMembers()) {
                p.addPotionEffect(new PotionEffect(this.potionEffectType, this.duration, this.amplifier), true);
            }
        } else if (this.type == ApplyType.BASE) {
            for (Player p : targetTeam.getMembers()) {
                if (!(p.getLocation().distance(targetTeam.getBed()) <= (double)targetTeam.getArena().getIslandRadius())) continue;
                p.addPotionEffect(new PotionEffect(this.potionEffectType, this.duration, this.amplifier), true);
            }
        } else if (this.type == ApplyType.ENEMY) {
            player.addPotionEffect(new PotionEffect(this.potionEffectType, this.duration, this.amplifier), true);
        }
    }

    public static enum ApplyType {
        TEAM,
        BASE,
        ENEMY;

    }
}

