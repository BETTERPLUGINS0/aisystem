/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.jetbrains.annotations.Nullable
 */
package net.advancedplugins.as.impl.utils.hooks;

import com.google.common.collect.ImmutableMap;
import net.advancedplugins.as.impl.utils.FoliaScheduler;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PermissionHook;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import net.advancedplugins.as.impl.utils.hooks.VanishHook;
import net.advancedplugins.as.impl.utils.hooks.holograms.CMIHologramHandler;
import net.advancedplugins.as.impl.utils.hooks.holograms.DecentHologramsHandler;
import net.advancedplugins.as.impl.utils.hooks.holograms.HologramHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.AdvancedChestsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.AdvancedEnchantmentsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.AdvancedSkillsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.AuraSkillsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.BeaconsPlus3Hook;
import net.advancedplugins.as.impl.utils.hooks.plugins.CMIHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.DiscordSRVHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.DynmapHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.EssentialsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.FactionsMCoreHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.FactionsUUIDHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.GeyserHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.GriefDefenderHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.GriefPreventionHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.ItemsAdderHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.LandsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.LuckPermsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.McMMOHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.MythicMobsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.OraxenHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.PlaceholderAPIHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.PremiumVanishHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.ProtectionStonesHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.ResidenceHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.SlimeFunHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.SuperVanishHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.SuperiorSkyblock2Hook;
import net.advancedplugins.as.impl.utils.hooks.plugins.TabHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.TownyChatHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.TownyHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.VaultHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.ViaVersionHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.WorldGuardHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class HooksHandler {
    private static HologramHandler holograms;
    private static JavaPlugin plugin;
    private static ImmutableMap<HookPlugin, PluginHookInstance> pluginHookMap;

    public static void hook(JavaPlugin javaPlugin) {
        if (!pluginHookMap.isEmpty()) {
            pluginHookMap = ImmutableMap.builder().build();
        }
        plugin = javaPlugin;
        HooksHandler.holograms();
        if (HooksHandler.isPluginEnabled(HookPlugin.PROTOCOLLIB.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.PROTOCOLLIB, new PluginHookInstance());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.AURASKILLS.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.AURASKILLS, new AuraSkillsHook(), true);
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.MCMMO.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.MCMMO, new McMMOHook(), true);
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.ADVANCEDENCHANTMENTS.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.ADVANCEDENCHANTMENTS, new AdvancedEnchantmentsHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.ADVANCEDSKILLS.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.ADVANCEDSKILLS, new AdvancedSkillsHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.WORLDGUARD.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.WORLDGUARD, new WorldGuardHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.GRIEFPREVENTION.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.GRIEFPREVENTION, new GriefPreventionHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.GRIEFDEFENDER.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.GRIEFDEFENDER, new GriefDefenderHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.PLACEHOLDERAPI.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.PLACEHOLDERAPI, new PlaceholderAPIHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.SLIMEFUN.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.SLIMEFUN, new SlimeFunHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.MYTHICMOBS.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.MYTHICMOBS, new MythicMobsHook(), true);
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.TOWNY.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.TOWNY, new TownyHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.TOWNYCHAT.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.TOWNYCHAT, new TownyChatHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.LANDS.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.LANDS, new LandsHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.SUPERIORSKYBLOCK2.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.SUPERIORSKYBLOCK2, new SuperiorSkyblock2Hook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.ORAXEN.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.ORAXEN, new OraxenHook((Plugin)javaPlugin), true);
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.PROTECTIONSTONES.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.PROTECTIONSTONES, new ProtectionStonesHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.RESIDENCE.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.RESIDENCE, new ResidenceHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.GEYSER.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.GEYSER, new GeyserHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.DYNMAP.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.DYNMAP, new DynmapHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.ESSENTIALS.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.ESSENTIALS, new EssentialsHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.CMI.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.CMI, new CMIHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.BEACONPLUS3.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.BEACONPLUS3, new BeaconsPlus3Hook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.VAULT.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.VAULT, new VaultHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.LUCKPERMS.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.LUCKPERMS, new LuckPermsHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.VIAVERSION.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.VIAVERSION, new ViaVersionHook());
        }
        if (HooksHandler.isPluginEnabled(HookPlugin.TAB.getPluginName())) {
            HooksHandler.registerNew(HookPlugin.TAB, new TabHook());
        }
        FoliaScheduler.runTaskLater((Plugin)javaPlugin, () -> {
            if (HooksHandler.isPluginEnabled(HookPlugin.FACTIONS.getPluginName())) {
                if (HooksHandler.isPluginEnabled("MassiveCore")) {
                    HooksHandler.registerNew(HookPlugin.FACTIONS, new FactionsMCoreHook());
                } else {
                    HooksHandler.registerNew(HookPlugin.FACTIONS, new FactionsUUIDHook());
                }
            }
            if (HooksHandler.isPluginEnabled(HookPlugin.ITEMSADDER.getPluginName())) {
                HooksHandler.registerNew(HookPlugin.ITEMSADDER, new ItemsAdderHook((Plugin)javaPlugin), true);
            }
            if (HooksHandler.isPluginEnabled(HookPlugin.ADVANCEDCHESTS.getPluginName())) {
                HooksHandler.registerNew(HookPlugin.ADVANCEDCHESTS, new AdvancedChestsHook());
            }
            if (HooksHandler.isPluginEnabled(HookPlugin.PREMIUMVANISH.getPluginName())) {
                HooksHandler.registerNew(HookPlugin.PREMIUMVANISH, new PremiumVanishHook());
            }
            if (HooksHandler.isPluginEnabled(HookPlugin.DISCORDSRV.getPluginName())) {
                HooksHandler.registerNew(HookPlugin.DISCORDSRV, new DiscordSRVHook());
            }
            if (HooksHandler.isPluginEnabled(HookPlugin.SUPERVANISH.getPluginName())) {
                HooksHandler.registerNew(HookPlugin.SUPERVANISH, new SuperVanishHook());
            }
            HooksHandler.sendHookMessage(javaPlugin);
        }, 10L);
    }

    private static void sendHookMessage(JavaPlugin javaPlugin) {
        if (pluginHookMap.isEmpty()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (HookPlugin hookPlugin : pluginHookMap.keySet()) {
            stringBuilder.append(hookPlugin.getPluginName()).append(", ");
        }
        javaPlugin.getLogger().info("Successfully hooked into " + stringBuilder.substring(0, stringBuilder.length() - 2) + ".");
    }

    private static void registerNew(HookPlugin hookPlugin, PluginHookInstance pluginHookInstance) {
        HooksHandler.registerNew(hookPlugin, pluginHookInstance, false);
    }

    private static void registerNew(HookPlugin hookPlugin, PluginHookInstance pluginHookInstance, boolean bl) {
        pluginHookMap = ImmutableMap.builder().putAll(pluginHookMap).put(hookPlugin, pluginHookInstance).build();
        if (bl) {
            plugin.getServer().getPluginManager().registerEvents((Listener)pluginHookInstance, (Plugin)plugin);
        }
    }

    public static PluginHookInstance getHook(HookPlugin hookPlugin) {
        return pluginHookMap.get((Object)hookPlugin);
    }

    private static void holograms() {
        holograms = HooksHandler.isPluginEnabled("CMI") ? new CMIHologramHandler(plugin) : (HooksHandler.isPluginEnabled("DecentHolograms") ? new DecentHologramsHandler(plugin) : (HooksHandler.isPluginEnabled("HolographicDisplays") ? new DecentHologramsHandler(plugin) : new HologramHandler(plugin)));
    }

    private static boolean isPluginEnabled(String string) {
        return Bukkit.getPluginManager().isPluginEnabled(string);
    }

    public static boolean isEnabled(HookPlugin hookPlugin) {
        return pluginHookMap.containsKey((Object)hookPlugin) && HooksHandler.isPluginEnabled(hookPlugin.getPluginName());
    }

    public static boolean isPlayerVanished(Player player) {
        return HooksHandler.getVanishHook() != null && HooksHandler.getVanishHook().isPlayerVanished(player);
    }

    @Nullable
    public static VanishHook getVanishHook() {
        if (HooksHandler.isEnabled(HookPlugin.PREMIUMVANISH)) {
            return (PremiumVanishHook)HooksHandler.getHook(HookPlugin.PREMIUMVANISH);
        }
        if (HooksHandler.isEnabled(HookPlugin.SUPERVANISH)) {
            return (SuperVanishHook)HooksHandler.getHook(HookPlugin.SUPERVANISH);
        }
        if (HooksHandler.isEnabled(HookPlugin.CMI)) {
            return (CMIHook)HooksHandler.getHook(HookPlugin.CMI);
        }
        if (HooksHandler.isEnabled(HookPlugin.ESSENTIALS)) {
            return (EssentialsHook)HooksHandler.getHook(HookPlugin.ESSENTIALS);
        }
        return null;
    }

    @Nullable
    public static PermissionHook getPermissionHook() {
        if (HooksHandler.isEnabled(HookPlugin.LUCKPERMS)) {
            return (LuckPermsHook)HooksHandler.getHook(HookPlugin.LUCKPERMS);
        }
        return (VaultHook)HooksHandler.getHook(HookPlugin.VAULT);
    }

    public static HologramHandler getHolograms() {
        return holograms;
    }

    static {
        pluginHookMap = ImmutableMap.builder().build();
    }
}

