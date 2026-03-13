/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.lumine.mythic.bukkit.MythicBukkit
 *  io.lumine.mythic.bukkit.events.MythicDamageEvent
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicDamageEvent;
import java.util.HashSet;
import java.util.Set;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MythicMobsHook
extends PluginHookInstance
implements Listener {
    private static final Set<Entity> ignoreEnchantsMobs = new HashSet<Entity>();

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.MYTHICMOBS.getPluginName();
    }

    public boolean isMythicMob(LivingEntity livingEntity) {
        return MythicBukkit.inst().getAPIHelper().isMythicMob(livingEntity.getUniqueId());
    }

    @EventHandler(priority=EventPriority.LOWEST)
    private void onMythicDamage(MythicDamageEvent mythicDamageEvent) {
        if (mythicDamageEvent.getDamageMetadata().getIgnoreEnchantments().booleanValue()) {
            ignoreEnchantsMobs.add(mythicDamageEvent.getCaster().getEntity().getBukkitEntity());
            SchedulerUtils.runTaskLater(() -> ignoreEnchantsMobs.remove(mythicDamageEvent.getCaster().getEntity().getBukkitEntity()), 4L);
        }
    }

    public static Set<Entity> getIgnoreEnchantsMobs() {
        return ignoreEnchantsMobs;
    }
}

