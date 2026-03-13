/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import net.advancedplugins.as.impl.effects.armorutils.ArmorListener;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.external.CustomMobDefenseTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.external.TemperatureChangeEventTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.external.TemperatureEventTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ArmorWearTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ArrowHitTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.AttackMobTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.AttackTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.BiteHookTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.BowFireTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.BrewPotionTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.CatchFishTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.CommandTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.DeathTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.DefenseMobProjectileTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.DefenseMobTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.DefenseProjectileTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.DefenseTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.EatTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ElytraFlyDamageTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ElytraFlyTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ExplosionTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.FallDamageTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.FireTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.HoldItemTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.HookEntityTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.HornTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ItemBreakTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.JoinTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.JumpTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.KillMobTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.KillPlayerTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.MiningTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.PassiveDeathTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.QuitTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.RepeatingTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.RightClickEntityTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.RightClickTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.RodCastTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ShieldBlockTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ShootMobTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.ShootTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.StruckTrigger;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal.SwingTrigger;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TriggerHandler {
    private final HashMap<String, AdvancedTrigger> triggerMap = new HashMap();

    public TriggerHandler(JavaPlugin javaPlugin) {
        this.register(javaPlugin, new ArrowHitTrigger());
        this.register(javaPlugin, new AttackMobTrigger());
        this.register(javaPlugin, new AttackTrigger());
        this.register(javaPlugin, new BowFireTrigger());
        this.register(javaPlugin, new BiteHookTrigger());
        this.register(javaPlugin, new BrewPotionTrigger());
        this.register(javaPlugin, new CatchFishTrigger());
        this.register(javaPlugin, new HookEntityTrigger());
        this.register(javaPlugin, new CommandTrigger());
        this.register(javaPlugin, new DeathTrigger());
        this.register(javaPlugin, new DefenseMobProjectileTrigger());
        this.register(javaPlugin, new DefenseMobTrigger());
        this.register(javaPlugin, new DefenseProjectileTrigger());
        this.register(javaPlugin, new DefenseTrigger());
        this.register(javaPlugin, new EatTrigger());
        this.register(javaPlugin, new ElytraFlyTrigger());
        this.register(javaPlugin, new ArmorWearTrigger());
        this.register(javaPlugin, new ExplosionTrigger());
        this.register(javaPlugin, new FallDamageTrigger());
        this.register(javaPlugin, new ElytraFlyDamageTrigger());
        this.register(javaPlugin, new FireTrigger());
        this.register(javaPlugin, new HoldItemTrigger());
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_19_R1)) {
            this.register(javaPlugin, new HornTrigger());
        }
        this.register(javaPlugin, new ItemBreakTrigger());
        this.register(javaPlugin, new ElytraFlyDamageTrigger());
        this.register(javaPlugin, new RightClickEntityTrigger());
        if (MinecraftVersion.isPaper()) {
            this.register(javaPlugin, new JumpTrigger());
        }
        this.register(javaPlugin, new JoinTrigger());
        this.register(javaPlugin, new QuitTrigger());
        this.register(javaPlugin, new KillMobTrigger());
        this.register(javaPlugin, new KillPlayerTrigger());
        this.register(javaPlugin, new MiningTrigger());
        this.register(javaPlugin, new PassiveDeathTrigger());
        this.register(javaPlugin, new RightClickTrigger());
        this.register(javaPlugin, new RodCastTrigger());
        this.register(javaPlugin, new ShieldBlockTrigger());
        this.register(javaPlugin, new ShootMobTrigger());
        this.register(javaPlugin, new ShootTrigger());
        this.register(javaPlugin, new StruckTrigger());
        this.register(javaPlugin, new SwingTrigger());
        this.register(javaPlugin, new RepeatingTrigger());
        if (Bukkit.getPluginManager().isPluginEnabled("AdvancedMobs")) {
            this.registerExternal(javaPlugin, new CustomMobDefenseTrigger(), "AdvancedMobs");
        }
        try {
            if (Bukkit.getPluginManager().isPluginEnabled("AdvancedSeasons") && Class.forName("net.advancedplugins.seasons.event.TemperatureEvent") != null) {
                this.registerExternal(javaPlugin, new TemperatureEventTrigger(), "AdvancedSeasons");
                this.registerExternal(javaPlugin, new TemperatureChangeEventTrigger(), "AdvancedSeasons");
            }
        } catch (Exception exception) {
            // empty catch block
        }
        this.registerListeners(javaPlugin);
    }

    private void registerListeners(JavaPlugin javaPlugin) {
        Bukkit.getPluginManager().registerEvents((Listener)new ArmorListener(), (Plugin)javaPlugin);
    }

    public void registerExternal(JavaPlugin javaPlugin, AdvancedTrigger advancedTrigger, String string) {
        if (!Bukkit.getPluginManager().isPluginEnabled(string)) {
            return;
        }
        this.register(javaPlugin, advancedTrigger);
    }

    public void register(JavaPlugin javaPlugin, AdvancedTrigger advancedTrigger) {
        if (!javaPlugin.equals((Object)EffectsHandler.getInstance())) {
            EffectsHandler.getInstance().getLogger().info(javaPlugin.getName() + " register a new trigger: " + advancedTrigger.getTriggerName());
        }
        this.triggerMap.put(advancedTrigger.getTriggerName(), advancedTrigger);
    }

    public Collection<AdvancedTrigger> getTriggers() {
        return this.triggerMap.values();
    }

    public AdvancedTrigger getTrigger(String string) {
        return this.triggerMap.get(string);
    }

    public boolean isValid(String string) {
        return this.triggerMap.containsKey(string);
    }

    public List<String> getTriggersAsString() {
        return new ArrayList<String>(this.triggerMap.keySet());
    }

    public void disableAllTriggers() {
        this.triggerMap.values().forEach(advancedTrigger -> advancedTrigger.setEnabled(false));
    }
}

