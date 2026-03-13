/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WebWalkerEffect
extends AdvancedEffect {
    private final List<UUID> activatedUsers = new ArrayList<UUID>();

    public WebWalkerEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "WEB_WALKER", "Walk through cobwebs fast", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!executionTask.getBuilder().isPermanent()) {
            this.warn(this.getName() + " can only be used as permanent effect.");
            return false;
        }
        if (executionTask.getBuilder().isRemoved()) {
            this.activatedUsers.remove(livingEntity.getUniqueId());
            return true;
        }
        this.activatedUsers.add(livingEntity.getUniqueId());
        return true;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        if (this.activatedUsers.isEmpty()) {
            return;
        }
        if (ASManager.sameBlock(playerMoveEvent.getFrom(), playerMoveEvent.getTo())) {
            return;
        }
        Material material = playerMoveEvent.getTo().getBlock().getType();
        if (!material.name().endsWith("WEB")) {
            return;
        }
        if (!this.activatedUsers.contains(playerMoveEvent.getPlayer().getUniqueId())) {
            return;
        }
        Player player = playerMoveEvent.getPlayer();
        PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.SPEED);
        if (potionEffect != null && potionEffect.getAmplifier() != 8) {
            return;
        }
        player.removePotionEffect(PotionEffectType.SPEED);
        playerMoveEvent.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 8));
    }
}

