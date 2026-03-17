/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.zombie_striker.qg.api.QAWeaponDamageEntityEvent
 *  org.bukkit.GameMode
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package me.zombie_striker.qav.hooks;

import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.QAVListener;
import me.zombie_striker.qg.api.QAWeaponDamageEntityEvent;
import org.bukkit.GameMode;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class QualityArmoryListener
implements Listener {
    @EventHandler
    public void onDamage(QAWeaponDamageEntityEvent qAWeaponDamageEntityEvent) {
        if (qAWeaponDamageEntityEvent.getPlayer().getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        Main.DEBUG("QualityArmoryListener: " + qAWeaponDamageEntityEvent.getDamage() + " " + qAWeaponDamageEntityEvent.getPlayer().getName());
        QAVListener.handleDamage((Cancellable)qAWeaponDamageEntityEvent, qAWeaponDamageEntityEvent.getDamaged(), qAWeaponDamageEntityEvent.getDamage(), EntityDamageEvent.DamageCause.CUSTOM, arg_0 -> ((QAWeaponDamageEntityEvent)qAWeaponDamageEntityEvent).setDamage(arg_0));
    }
}

