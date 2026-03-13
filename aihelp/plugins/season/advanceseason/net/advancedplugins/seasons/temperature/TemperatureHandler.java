/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.EntityEffect
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.persistence.PersistentDataHolder
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.advancedplugins.seasons.temperature;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.DataHandler;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.pdc.PDCHandler;
import net.advancedplugins.as.impl.utils.text.Text;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.AdvancedBiomeBase;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.event.TemperatureChangeEvent;
import net.advancedplugins.seasons.event.TemperatureEvent;
import net.advancedplugins.seasons.handlers.ActionbarHandler;
import net.advancedplugins.seasons.objects.CustomWeatherType;
import net.advancedplugins.seasons.temperature.PlayerTemperature;
import net.advancedplugins.seasons.temperature.TemperatureAbilitiesHandler;
import net.advancedplugins.seasons.utils.PDCKey;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class TemperatureHandler
extends DataHandler {
    private final int baseTemperature;
    private final String formula;
    private final String actionbar;
    private final ImmutableMap<SeasonType, Integer> seasonTemperature;
    private final ImmutableList<String> biomesWithoutWinter;
    private int noWinterBiomeTemperature = 0;
    private int breathVisibility = 0;
    private final TreeMap<Integer, net.advancedplugins.seasons.enums.TemperatureEvent> temperatureEvents = new TreeMap();
    private final ConcurrentHashMap<UUID, PlayerTemperature> playerTemperatureMap = new ConcurrentHashMap();
    private final boolean temperatureEventsEnabled;
    private static ImmutableMap<CustomWeatherType, String> weatherIcon;
    private final boolean temperatureActionbar;
    private final boolean temperatureEventsActionbar;
    private final TemperatureAbilitiesHandler abilities = new TemperatureAbilitiesHandler(this);
    private final HashMap<UUID, Integer> temp = new HashMap();

    public TemperatureHandler(JavaPlugin javaPlugin) {
        super("temperature", javaPlugin);
        this.temperatureActionbar = javaPlugin.getConfig().getBoolean("temperatureActionbar", true);
        this.temperatureEventsActionbar = javaPlugin.getConfig().getBoolean("temperatureEventsActionbar", true);
        this.baseTemperature = this.getInt("base");
        this.formula = this.getString("temperatureFormula");
        this.actionbar = Core.getLocaleHandler().getString("temperature.actionBar");
        this.biomesWithoutWinter = ((ImmutableList.Builder)ImmutableList.builder().addAll(this.getStringList("noWinterBiomes"))).build();
        this.temperatureEventsEnabled = this.getBoolean("temperatureEventsEnabled");
        weatherIcon = ASManager.configToImmutableMap(Core.getLocaleHandler().getFile().getLocaleConfig(), "temperature.icon", CustomWeatherType::valueOf, String.class);
        this.seasonTemperature = ASManager.configToImmutableMap(this.getConfig(), "temperature", SeasonType::valueOf, Integer.class);
        this.noWinterBiomeTemperature = (this.seasonTemperature.get((Object)SeasonType.FALL) + this.seasonTemperature.get((Object)SeasonType.SPRING)) / 2;
        this.loadTemperatureEvents(javaPlugin);
        this.startTemperatureDisplay(javaPlugin);
    }

    @Override
    public void unload() {
        for (int n : this.getActiveTasks()) {
            Bukkit.getServer().getScheduler().cancelTask(n);
        }
    }

    private void startTemperatureDisplay(JavaPlugin javaPlugin) {
        this.addTask(new BukkitRunnable(){

            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!Core.getWorldHandler().isWorldEnabled(player.getWorld().getName())) continue;
                    PlayerTemperature playerTemperature = TemperatureHandler.this.playerTemperatureMap.computeIfAbsent(player.getUniqueId(), uUID -> new PlayerTemperature());
                    int n = playerTemperature.realTemperature;
                    int n2 = TemperatureHandler.this.getPlayerTemperature(player);
                    if (n != n2) {
                        playerTemperature.realTemperature = n2;
                        Bukkit.getScheduler().runTask((Plugin)Core.getInstance(), () -> Bukkit.getPluginManager().callEvent((Event)new TemperatureChangeEvent(player, n, n2)));
                    }
                    if (playerTemperature.init) continue;
                    playerTemperature.init = true;
                    playerTemperature.displayTemperature = playerTemperature.realTemperature;
                }
            }
        }.runTaskTimerAsynchronously((Plugin)javaPlugin, 35L, 35L).getTaskId());
        this.addTask(new BukkitRunnable(){

            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!Core.getWorldHandler().isWorldEnabled(player.getWorld().getName())) continue;
                    PlayerTemperature playerTemperature = TemperatureHandler.this.playerTemperatureMap.computeIfAbsent(player.getUniqueId(), uUID -> new PlayerTemperature());
                    if (!playerTemperature.init || System.currentTimeMillis() - playerTemperature.lastShow < 2500L) continue;
                    if (playerTemperature.displayTemperature < playerTemperature.realTemperature) {
                        ++playerTemperature.displayTemperature;
                    } else if (playerTemperature.displayTemperature > playerTemperature.realTemperature) {
                        --playerTemperature.displayTemperature;
                    }
                    if (!TemperatureHandler.this.temperatureActionbar || !TemperatureHandler.this.canSendActionBar(player)) continue;
                    ASManager.sendActionBar(Text.parsePapi(Text.modify("&e&o &a &r") + TemperatureHandler.this.parseTemperatureDisplay(playerTemperature.getDisplayTemperature(), TemperatureHandler.this.actionbar, player.getWorld().getName(), player), (OfflinePlayer)player), player);
                }
            }
        }.runTaskTimerAsynchronously((Plugin)javaPlugin, 10L, 10L).getTaskId());
        this.addTask(Bukkit.getScheduler().runTaskTimer((Plugin)javaPlugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerTemperature playerTemperature;
                if (!Core.getWorldHandler().isWorldEnabled(player.getWorld().getName()) || (playerTemperature = this.playerTemperatureMap.get(player.getUniqueId())) == null) continue;
                net.advancedplugins.seasons.enums.TemperatureEvent temperatureEvent = this.handlePlayerTemperature(player, playerTemperature.getDisplayTemperature());
                if (temperatureEvent != null) {
                    if (temperatureEvent.equals((Object)playerTemperature.lastEvent)) continue;
                    playerTemperature.lastShow = System.currentTimeMillis();
                    playerTemperature.lastEvent = temperatureEvent;
                    if (!this.temperatureEventsActionbar || temperatureEvent.equals((Object)net.advancedplugins.seasons.enums.TemperatureEvent.BREATH_VISIBILITY)) continue;
                    ASManager.sendActionBar(Text.parsePapi(Core.getLocaleHandler().getString("temperature.eventMessage").replace("%event%", Core.getLocaleHandler().getString("temperature.events." + temperatureEvent.name().toLowerCase())), (OfflinePlayer)player), player);
                    continue;
                }
                if (temperatureEvent != null || playerTemperature.lastEvent == null) continue;
                playerTemperature.lastEvent = null;
            }
        }, 20L, 20L).getTaskId());
    }

    private boolean canSendActionBar(Player player) {
        return ActionbarHandler.canSendActionbar(player);
    }

    public String parseTemperatureDisplay(int n, String string, String string2, Player player) {
        string = string.replace("%icon%", this.getWeatherIcon(string2)).replace("%season%", Core.getLocaleHandler().getString("seasons." + Core.getSeasonHandler().getSeason(string2).getType().name(), ""));
        if (Core.getInstance().getConfig().getBoolean("convertToFahrenheit") || "fahrenheit".equalsIgnoreCase(this.getPlayerUnits(player))) {
            return string.replace("%temperature%", String.valueOf((int)((double)n * 1.8 + 32.0))).replace("%unit%", "F");
        }
        return string.replace("%temperature%", String.valueOf(n)).replace("%unit%", "C");
    }

    private void loadTemperatureEvents(JavaPlugin javaPlugin) {
        for (net.advancedplugins.seasons.enums.TemperatureEvent temperatureEvent : net.advancedplugins.seasons.enums.TemperatureEvent.values()) {
            int n = this.getInt("temperatureEvents." + temperatureEvent.name().toLowerCase(Locale.ROOT) + ".temperature");
            if (temperatureEvent.equals((Object)net.advancedplugins.seasons.enums.TemperatureEvent.BREATH_VISIBILITY)) {
                this.breathVisibility = n;
            }
            this.temperatureEvents.put(n, temperatureEvent);
        }
    }

    public int getPlayerTemperature(Player player) {
        Location location = player.getEyeLocation();
        if (this.temp.containsKey(player.getUniqueId())) {
            return this.temp.get(player.getUniqueId());
        }
        SeasonType seasonType = Core.getSeasonHandler().getSeason(player.getWorld()).getType();
        AdvancedBiomeBase advancedBiomeBase = Core.getBiomesHandler().getAdvancedBiomeAt(location).orElse(null);
        int n = advancedBiomeBase == null ? 0 : advancedBiomeBase.getTemperature();
        double d = location.getBlock().getLightFromBlocks();
        int n2 = this.getSeasonTemperature(seasonType, advancedBiomeBase);
        int n3 = (int)(d * (seasonType.equals((Object)SeasonType.SUMMER) ? 0.8 : Math.max(1.5, d / (double)(n2 < 0 ? 5 : 8))));
        int n4 = (int)Arrays.stream(player.getInventory().getArmorContents()).filter(Objects::nonNull).count();
        int n5 = TemperatureHandler.getConditional(player);
        int n6 = (int)ASManager.parseThroughCalculator(this.formula.replace("%base%", Integer.toString(this.baseTemperature)).replace("%biome%", Integer.toString(n)).replace("%seasonTemp%", Integer.toString(n2)).replace("%lightLevel%", Integer.toString(n3)).replace("%conditionalTemp%", Integer.toString(n5)).replace("%armorItemsCount%", Integer.toString(n4)));
        return n6;
    }

    public int getLocationTemperature(Location location, boolean bl) {
        SeasonType seasonType = Core.getSeasonHandler().getSeason(location.getWorld()).getType();
        AdvancedBiomeBase advancedBiomeBase = Core.getBiomesHandler().getAdvancedBiomeAt(location).orElse(null);
        int n = advancedBiomeBase == null ? 0 : advancedBiomeBase.getTemperature();
        double d = location.getBlock().getLightFromBlocks();
        int n2 = this.getSeasonTemperature(seasonType, advancedBiomeBase);
        int n3 = (int)(d * (seasonType.equals((Object)SeasonType.SUMMER) ? 1.0 : Math.max(1.5, d / (double)(n2 < 0 ? 5 : 8))));
        int n4 = bl ? TemperatureHandler.getConditional(location) : 0;
        int n5 = (int)ASManager.parseThroughCalculator(this.formula.replace("%base%", Integer.toString(this.baseTemperature)).replace("%biome%", Integer.toString(n)).replace("%seasonTemp%", Integer.toString(n2)).replace("%lightLevel%", Integer.toString(n3)).replace("%conditionalTemp%", Integer.toString(n4)).replace("%armorItemsCount%", "0"));
        return n5;
    }

    private int getSeasonTemperature(SeasonType seasonType, AdvancedBiomeBase advancedBiomeBase) {
        if (advancedBiomeBase == null) {
            return 0;
        }
        if (SeasonType.WINTER.equals((Object)seasonType) && this.biomesWithoutWinter.contains(advancedBiomeBase.getName())) {
            return this.noWinterBiomeTemperature;
        }
        return this.seasonTemperature.getOrDefault((Object)seasonType, 0);
    }

    private static int getConditional(Location location) {
        int n = 0;
        World world = location.getWorld();
        boolean bl = ASManager.isDay(world.getTime());
        if (bl) {
            n -= Math.abs(15 - location.getBlock().getLightFromSky()) / 2;
        }
        if (location.getBlock().getType() == Material.WATER) {
            n -= 4;
        }
        if (!world.isClearWeather()) {
            n -= 4;
        }
        if (!bl) {
            n -= 8;
        }
        return n;
    }

    private static int getConditional(Player player) {
        int n = 0;
        boolean bl = ASManager.isDay(player.getWorld().getTime());
        if (bl) {
            n -= Math.abs(15 - player.getLocation().getBlock().getLightFromSky()) / 2;
        }
        if (player.isInWater()) {
            n -= 4;
        }
        if (!player.getWorld().isClearWeather()) {
            n -= 4;
        }
        if (!bl) {
            n -= 8;
        }
        return n;
    }

    private net.advancedplugins.seasons.enums.TemperatureEvent handlePlayerTemperature(Player player, int n) {
        net.advancedplugins.seasons.enums.TemperatureEvent temperatureEvent = this.getEventForTemperature(n);
        if (player.getGameMode().equals((Object)GameMode.SPECTATOR) || player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return null;
        }
        if (HooksHandler.isPlayerVanished(player)) {
            return null;
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE) && !Core.getInstance().getConfig().getBoolean("creativeTemperatureEffects")) {
            return null;
        }
        if (!this.temperatureEventsEnabled) {
            return null;
        }
        if (temperatureEvent == null) {
            return null;
        }
        Bukkit.getScheduler().runTask((Plugin)Core.getInstance(), () -> Bukkit.getPluginManager().callEvent((Event)new TemperatureEvent(player, temperatureEvent.name())));
        switch (temperatureEvent) {
            case FREEZE: {
                player.setFreezeTicks(200);
                player.playSound(player.getLocation(), Sound.BLOCK_SNOW_PLACE, 0.2f, 1.0f);
                break;
            }
            case ICE_FORMING: {
                player.setFreezeTicks(40);
                ASManager.playEffect(Particle.SNOW_SHOVEL.name(), 0.5f, 4, player.getEyeLocation());
                player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_FALL, 0.2f, 1.0f);
                break;
            }
            case SHIVERING: {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 0, true, false));
                player.setFreezeTicks(15);
                player.playSound(player.getLocation(), Sound.ENTITY_STRAY_AMBIENT, 0.2f, 1.0f);
                break;
            }
            case WARMTH: {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 3, true, false));
                break;
            }
            case SWEATING: {
                player.playSound(player.getLocation(), Sound.BLOCK_FURNACE_FIRE_CRACKLE, 0.2f, 1.0f);
                ASManager.playEffect(Particle.WATER_SPLASH.name(), 0.2f, 20, player.getEyeLocation());
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 75, 0, true, false));
                break;
            }
            case HEATSTROKE: {
                player.playSound(player.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 0.5f, 1.0f);
                player.setFireTicks(30);
                player.playEffect(EntityEffect.HURT_BERRY_BUSH);
                ASManager.playEffect(Particle.SMALL_FLAME.name(), 0.2f, 20, player.getEyeLocation());
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0, true, false));
            }
        }
        return temperatureEvent;
    }

    public net.advancedplugins.seasons.enums.TemperatureEvent getEventForTemperature(int n) {
        net.advancedplugins.seasons.enums.TemperatureEvent temperatureEvent = null;
        if (n < 0) {
            for (Map.Entry<Integer, net.advancedplugins.seasons.enums.TemperatureEvent> entry : this.temperatureEvents.entrySet()) {
                if (entry.getKey() >= 0 || n > entry.getKey()) continue;
                temperatureEvent = entry.getValue();
                break;
            }
        } else if (n > 0) {
            for (Map.Entry<Integer, net.advancedplugins.seasons.enums.TemperatureEvent> entry : this.temperatureEvents.entrySet()) {
                if (entry.getKey() <= 0 || n < entry.getKey()) continue;
                temperatureEvent = entry.getValue();
            }
        } else {
            temperatureEvent = this.temperatureEvents.get(0);
        }
        return temperatureEvent;
    }

    public void setTemperature(Player player, int n) {
        if (n == 0) {
            this.temp.remove(player.getUniqueId());
        } else {
            this.temp.put(player.getUniqueId(), n);
        }
    }

    public String getWeatherIcon(String string) {
        try {
            return Text.modify(weatherIcon.get((Object)Core.getWeatherHandler().getWeather(string)));
        } catch (Exception exception) {
            return "";
        }
    }

    public String getPlayerUnits(Player player) {
        return Optional.ofNullable(PDCHandler.getString((PersistentDataHolder)player, PDCKey.TEMPERATURE_UNITS.getKey())).orElse("celsius");
    }

    public int getBreathVisibility() {
        return this.breathVisibility;
    }

    public ConcurrentHashMap<UUID, PlayerTemperature> getPlayerTemperatureMap() {
        return this.playerTemperatureMap;
    }

    public TemperatureAbilitiesHandler getAbilities() {
        return this.abilities;
    }
}

