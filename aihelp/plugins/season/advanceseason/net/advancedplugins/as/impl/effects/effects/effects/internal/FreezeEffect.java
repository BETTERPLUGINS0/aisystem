/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.HashMap;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FreezeEffect
extends AdvancedEffect {
    private final HashMap<UUID, Long> frozenPlayers = new HashMap();

    public FreezeEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "FREEZE", "Freeze Entity", "%e:<TICKS>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        int n = ASManager.parseInt(stringArray[0]);
        if (livingEntity instanceof Player) {
            if (n <= 0) {
                this.frozenPlayers.remove(livingEntity.getUniqueId());
                return true;
            }
            if (this.frozenPlayers.remove(livingEntity.getUniqueId()) == null) {
                this.frozenPlayers.put(livingEntity.getUniqueId(), System.currentTimeMillis() + (long)n * 50L);
            }
            return true;
        }
        livingEntity.setFreezeTicks(n);
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, n, 127, false, false));
        return true;
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent playerMoveEvent) {
        if (this.frozenPlayers.isEmpty()) {
            return;
        }
        if (ASManager.sameBlock(playerMoveEvent.getFrom(), playerMoveEvent.getTo())) {
            return;
        }
        Long l = this.frozenPlayers.get(playerMoveEvent.getPlayer().getUniqueId());
        if (l == null) {
            return;
        }
        if (l < System.currentTimeMillis()) {
            this.frozenPlayers.remove(playerMoveEvent.getPlayer().getUniqueId());
            return;
        }
        playerMoveEvent.setCancelled(true);
        playerMoveEvent.setTo(playerMoveEvent.getFrom());
    }
}

