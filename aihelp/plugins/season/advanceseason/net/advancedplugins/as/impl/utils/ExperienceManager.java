/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ExperienceManager {
    @NotNull
    private final Player player;

    public int getTotalExperience() {
        return ExperienceManager.getExpAtLevel(this.player.getLevel()) + Math.round((float)ExperienceManager.getExpToNext(this.player.getLevel()) * this.player.getExp());
    }

    public static int getExpAtLevel(int n) {
        if (n > 30) {
            return (int)(4.5 * (double)n * (double)n - 162.5 * (double)n + 2220.0);
        }
        if (n > 15) {
            return (int)(2.5 * (double)n * (double)n - 40.5 * (double)n + 360.0);
        }
        return n * n + 6 * n;
    }

    public static double getLevelFromExp(long l) {
        int n = ExperienceManager.getIntLevelFromExp(l);
        float f = (float)l - (float)ExperienceManager.getExpAtLevel(n);
        float f2 = f / (float)ExperienceManager.getExpToNext(n);
        return (double)n + (double)f2;
    }

    public static int getIntLevelFromExp(long l) {
        if (l > 1395L) {
            return (int)((Math.sqrt((double)(72L * l) - 54215.0) + 325.0) / 18.0);
        }
        if (l > 315L) {
            return (int)(Math.sqrt((double)(40L * l) - 7839.0) / 10.0 + 8.1);
        }
        if (l > 0L) {
            return (int)(Math.sqrt((double)l + 9.0) - 3.0);
        }
        return 0;
    }

    private static int getExpToNext(int n) {
        if (n >= 30) {
            return n * 9 - 158;
        }
        if (n >= 15) {
            return n * 5 - 38;
        }
        return n * 2 + 7;
    }

    public void changeExp(int n) {
        if ((n += this.getTotalExperience()) < 0) {
            n = 0;
        }
        double d = ExperienceManager.getLevelFromExp(n);
        int n2 = (int)d;
        this.player.setLevel(n2);
        this.player.setExp((float)(d - (double)n2));
    }

    public void setTotalExperience(int n) {
        ExperienceManager.setTotalExperience(this.player, n);
    }

    public static void setTotalExperience(Player player, int n) {
        if (n < 0) {
            n = 0;
        }
        player.setExp(0.0f);
        player.setLevel(0);
        player.setTotalExperience(0);
        int n2 = n;
        while (n2 > 0) {
            int n3 = ExperienceManager.getExpAtLevel(player.getLevel());
            if ((n2 -= n3) >= 0) {
                player.giveExp(n3);
                continue;
            }
            player.giveExp(n2 += n3);
            n2 = 0;
        }
    }

    public ExperienceManager(@NotNull Player player) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        this.player = player;
    }
}

