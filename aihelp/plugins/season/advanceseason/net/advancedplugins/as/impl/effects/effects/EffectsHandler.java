/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.advancedplugins.as.impl.effects.effects.effects.EffectStorage;
import net.advancedplugins.as.impl.effects.effects.effects.utils.TridentShootHandler;
import net.advancedplugins.as.impl.effects.effects.hook.AntiCheatHook;
import net.advancedplugins.as.impl.effects.effects.hook.type.AAC5Hook;
import net.advancedplugins.as.impl.effects.effects.hook.type.AACHook;
import net.advancedplugins.as.impl.effects.effects.hook.type.CNCPHook;
import net.advancedplugins.as.impl.effects.effects.hook.type.IntaveHook;
import net.advancedplugins.as.impl.effects.effects.hook.type.SoaromaHook;
import net.advancedplugins.as.impl.effects.effects.hook.type.SpartanHook;
import net.advancedplugins.as.impl.effects.effects.hook.type.VulcanHook;
import net.advancedplugins.as.impl.effects.effects.mechanics.functions.FunctionsHandler;
import net.advancedplugins.as.impl.effects.effects.mechanics.pointers.PointersHandler;
import net.advancedplugins.as.impl.effects.effects.mechanics.targets.Targets;
import net.advancedplugins.as.impl.effects.effects.mechanics.triggers.TriggerHandler;
import net.advancedplugins.as.impl.effects.effects.mechanics.variables.VariablesHandler;
import net.advancedplugins.as.impl.effects.effects.reader.AbilitiesReader;
import net.advancedplugins.as.impl.effects.patches.ClearCommandEvent;
import net.advancedplugins.as.impl.effects.patches.HatCommandEvent;
import net.advancedplugins.as.impl.effects.patches.HealCommandEvent;
import net.advancedplugins.as.impl.utils.RemoveDeathItems;
import net.advancedplugins.as.impl.utils.economy.EconomyHandler;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.as.impl.utils.protection.ProtectionHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class EffectsHandler {
    private static AbilitiesReader abilitiesReader;
    private static EffectsHandler effectsHandler;
    private static EffectStorage effectStorage;
    private static Targets targets;
    private static VariablesHandler variables;
    private static PointersHandler pointers;
    private static FunctionsHandler functions;
    private static ProtectionHandler protection;
    private static EconomyHandler economyHandler;
    private static TriggerHandler triggerHandler;
    private static String key;
    private static JavaPlugin instance;
    private static boolean debug;
    private static Map<String, AntiCheatHook> antiCheatHooks;

    public EffectsHandler(String string, JavaPlugin javaPlugin, AbilitiesReader abilitiesReader) {
        MinecraftVersion.init();
        effectsHandler = this;
        key = string;
        instance = javaPlugin;
        EffectsHandler.abilitiesReader = abilitiesReader;
        effectStorage = new EffectStorage(javaPlugin);
        targets = new Targets();
        variables = new VariablesHandler(javaPlugin);
        functions = new FunctionsHandler(javaPlugin);
        economyHandler = new EconomyHandler(javaPlugin);
        protection = new ProtectionHandler(javaPlugin);
        triggerHandler = new TriggerHandler(javaPlugin);
        pointers = new PointersHandler(javaPlugin);
        antiCheatHooks = new HashMap<String, AntiCheatHook>();
        if (Bukkit.getPluginManager().isPluginEnabled("CompactNoCheatPlus")) {
            this.registerAntiCheatHooks(new CNCPHook());
        }
        if (Bukkit.getPluginManager().isPluginEnabled("AAC")) {
            this.registerAntiCheatHooks(new AACHook());
        }
        if (Bukkit.getPluginManager().isPluginEnabled("AAC5")) {
            this.registerAntiCheatHooks(new AAC5Hook());
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Intave")) {
            this.registerAntiCheatHooks(new IntaveHook());
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Soaroma")) {
            this.registerAntiCheatHooks(new SoaromaHook());
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Spartan")) {
            this.registerAntiCheatHooks(new SpartanHook());
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Vulcan")) {
            this.registerAntiCheatHooks(new VulcanHook());
        }
        Bukkit.getPluginManager().registerEvents((Listener)new TridentShootHandler(), (Plugin)javaPlugin);
        Bukkit.getPluginManager().registerEvents((Listener)new RemoveDeathItems(), (Plugin)javaPlugin);
        this.registerPatches();
    }

    public void unload() {
        effectStorage.unload();
    }

    public static Targets getTargetHandler() {
        return targets;
    }

    public static VariablesHandler getVariablesHandler() {
        return variables;
    }

    public static FunctionsHandler getFunctionsHandler() {
        return functions;
    }

    public static PointersHandler getPointersHandler() {
        return pointers;
    }

    private void registerAntiCheatHooks(AntiCheatHook ... antiCheatHookArray) {
        Arrays.stream(antiCheatHookArray).forEach(AntiCheatHook::register);
    }

    public static void debug(String string) {
        if (!debug) {
            return;
        }
        Bukkit.broadcastMessage((String)("[EffectsHandler] " + string));
        instance.getLogger().info("[EffectsHandler] " + string);
    }

    public void registerPatches() {
        Bukkit.getPluginManager().registerEvents((Listener)new HealCommandEvent(), (Plugin)instance);
        Bukkit.getPluginManager().registerEvents((Listener)new ClearCommandEvent(), (Plugin)instance);
        Bukkit.getPluginManager().registerEvents((Listener)new HatCommandEvent(), (Plugin)instance);
    }

    public static AbilitiesReader getAbilitiesReader() {
        return abilitiesReader;
    }

    public static EffectsHandler getEffectsHandler() {
        return effectsHandler;
    }

    public static EffectStorage getEffectStorage() {
        return effectStorage;
    }

    public static ProtectionHandler getProtection() {
        return protection;
    }

    public static EconomyHandler getEconomyHandler() {
        return economyHandler;
    }

    public static TriggerHandler getTriggerHandler() {
        return triggerHandler;
    }

    public static String getKey() {
        return key;
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    public static void setDebug(boolean bl) {
        debug = bl;
    }

    public static Map<String, AntiCheatHook> getAntiCheatHooks() {
        return antiCheatHooks;
    }

    static {
        debug = false;
    }
}

