/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.NamespacedKey
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.seasons.biomes;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.api.biome.ASBiomeData;
import net.advancedplugins.seasons.api.biome.BiomeVisualKey;
import net.advancedplugins.seasons.biomes.AdvancedBiomeBase;
import net.advancedplugins.seasons.biomes.BiomeDescriptor;
import net.advancedplugins.seasons.biomes.BiomeUtils;
import net.advancedplugins.seasons.biomes.SeasonData;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_20_1;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_20_2;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_20_3;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_20_4_Spigot;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_21;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_21_10;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_21_2;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_21_3;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_21_4;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_21_5;
import net.advancedplugins.seasons.biomes.nms.Packets_1_20_1;
import net.advancedplugins.seasons.biomes.nms.Packets_1_20_2;
import net.advancedplugins.seasons.biomes.nms.Packets_1_20_3;
import net.advancedplugins.seasons.biomes.nms.Packets_1_20_4_Spigot;
import net.advancedplugins.seasons.biomes.nms.Packets_1_21;
import net.advancedplugins.seasons.biomes.nms.Packets_1_21_10;
import net.advancedplugins.seasons.biomes.nms.Packets_1_21_2;
import net.advancedplugins.seasons.biomes.nms.Packets_1_21_3;
import net.advancedplugins.seasons.biomes.nms.Packets_1_21_4;
import net.advancedplugins.seasons.biomes.nms.Packets_1_21_5;
import net.advancedplugins.seasons.enums.ColorType;
import net.advancedplugins.seasons.enums.Season;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.handlers.RenderHandler;
import net.advancedplugins.seasons.nms.NMSBiomeHelper;
import net.advancedplugins.seasons.objects.INMSBiome;
import net.advancedplugins.seasons.objects.IProtocolLib;
import net.advancedplugins.seasons.utils.TransitionColors;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BiomesHandler {
    private final ImmutableMap<String, ASBiomeData> biomeData;
    private final ImmutableMap<String, AdvancedBiomeBase> biomeMap;
    private final ImmutableMap<BiomeDescriptor, AdvancedBiomeBase> advancedBiomesBySubBiomes;
    private INMSBiome nmsBiome;
    private IProtocolLib protocolLib;
    private final EnumMap<Season, ImmutableMap<Integer, Integer>> seasonToBiomeMap = new EnumMap(Season.class);
    private ImmutableMap<Integer, String> optimizedIDs;
    private final RenderHandler renderHandler;
    private HashSet<Integer> reported = new HashSet();

    public BiomesHandler(JavaPlugin javaPlugin) {
        javaPlugin.getLogger().warning("Minecraft Version: " + MinecraftVersion.getCurrentVersion().name() + ", is paper: " + MinecraftVersion.isPaper());
        this.loadNMSBiome(javaPlugin);
        ImmutableMap.Builder<String, AdvancedBiomeBase> builder = new ImmutableMap.Builder<String, AdvancedBiomeBase>();
        ImmutableMap.Builder<BiomeDescriptor, AdvancedBiomeBase> builder2 = new ImmutableMap.Builder<BiomeDescriptor, AdvancedBiomeBase>();
        this.loadAllBiomes(javaPlugin, "biomeConfiguration", builder, builder2);
        this.biomeMap = builder.build();
        this.advancedBiomesBySubBiomes = builder2.build();
        ImmutableMap.Builder builder3 = ImmutableMap.builder();
        this.biomeMap.forEach((string, advancedBiomeBase) -> builder3.put(string, this.fromAdvancedBiome((AdvancedBiomeBase)advancedBiomeBase)));
        this.biomeData = builder3.build();
        this.generateBiomes();
        this.generateTransitions();
        this.populateSeasonTransitionMap();
        this.generateOptimizedIDs();
        ASManager.debug("[BiomesHandler] Registered total of " + this.biomeMap.size() + " biomes");
        this.renderHandler = new RenderHandler(this);
        BiomeUtils.init(this);
    }

    public Collection<String> getBiomes() {
        return this.nmsBiome.getBiomes();
    }

    private void generateOptimizedIDs() {
        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        for (BiomeDescriptor biomeDescriptor : NMSBiomeHelper.allBiomeDescriptors()) {
            Integer n = this.nmsBiome.getVanillaBiomeId(biomeDescriptor.name());
            if (n == null) {
                ASManager.debug("NULL ID for Biome " + biomeDescriptor.name());
                continue;
            }
            hashMap.put(n, biomeDescriptor.upperCaseName());
        }
        this.optimizedIDs = ImmutableMap.builder().putAll(hashMap).build();
    }

    public Integer getBiomeId(String string) {
        return ASManager.getKeysByValue(this.optimizedIDs, string.toUpperCase()).stream().findFirst().orElse(null);
    }

    private void loadNMSBiome(JavaPlugin javaPlugin) {
        MinecraftVersion minecraftVersion = MinecraftVersion.getCurrentVersion();
        switch (minecraftVersion) {
            case MC1_20_R1: {
                this.nmsBiome = new NMSBiome_1_20_1();
                this.protocolLib = new Packets_1_20_1(javaPlugin, this, this.nmsBiome);
                break;
            }
            case MC1_20_R2: {
                this.nmsBiome = new NMSBiome_1_20_2();
                this.protocolLib = new Packets_1_20_2(javaPlugin, this, this.nmsBiome);
                break;
            }
            case MC1_20_R3: {
                this.nmsBiome = new NMSBiome_1_20_3();
                this.protocolLib = new Packets_1_20_3(javaPlugin, this, this.nmsBiome);
                break;
            }
            case MC1_20_R4: {
                this.nmsBiome = new NMSBiome_1_20_4_Spigot();
                this.protocolLib = new Packets_1_20_4_Spigot(javaPlugin, this, this.nmsBiome);
                break;
            }
            case MC1_21_R1: {
                this.nmsBiome = new NMSBiome_1_21();
                this.protocolLib = new Packets_1_21(javaPlugin, this, this.nmsBiome);
                break;
            }
            case MC1_21_R2: {
                this.nmsBiome = new NMSBiome_1_21_2();
                this.protocolLib = new Packets_1_21_2(javaPlugin, this, this.nmsBiome);
                break;
            }
            case MC1_21_R3: {
                this.nmsBiome = new NMSBiome_1_21_3();
                this.protocolLib = new Packets_1_21_3(javaPlugin, this, this.nmsBiome);
                break;
            }
            case MC1_21_R4: {
                this.nmsBiome = new NMSBiome_1_21_4();
                this.protocolLib = new Packets_1_21_4(javaPlugin, this, this.nmsBiome);
                break;
            }
            case MC1_21_R5: {
                this.nmsBiome = new NMSBiome_1_21_5();
                this.protocolLib = new Packets_1_21_5(javaPlugin, this, this.nmsBiome);
            }
            case MC1_21_R6: {
                this.nmsBiome = new NMSBiome_1_21_10();
                this.protocolLib = new Packets_1_21_10(javaPlugin, this, this.nmsBiome);
            }
        }
    }

    private INMSBiome loadNMSBiome(AdvancedBiomeBase advancedBiomeBase, SeasonType seasonType, SeasonData seasonData, int n) {
        MinecraftVersion minecraftVersion = MinecraftVersion.getCurrentVersion();
        switch (minecraftVersion) {
            case MC1_20_R1: {
                this.nmsBiome = new NMSBiome_1_20_1(advancedBiomeBase, seasonType, seasonData, n);
                return this.nmsBiome;
            }
            case MC1_20_R2: {
                this.nmsBiome = new NMSBiome_1_20_2(advancedBiomeBase, seasonType, seasonData, n);
                return this.nmsBiome;
            }
            case MC1_20_R3: {
                this.nmsBiome = new NMSBiome_1_20_3(advancedBiomeBase, seasonType, seasonData, n);
                return this.nmsBiome;
            }
            case MC1_20_R4: {
                this.nmsBiome = new NMSBiome_1_20_4_Spigot(advancedBiomeBase, seasonType, seasonData, n);
                return this.nmsBiome;
            }
            case MC1_21_R1: {
                this.nmsBiome = new NMSBiome_1_21(advancedBiomeBase, seasonType, seasonData, n);
                return this.nmsBiome;
            }
            case MC1_21_R2: {
                this.nmsBiome = new NMSBiome_1_21_2(advancedBiomeBase, seasonType, seasonData, n);
                return this.nmsBiome;
            }
            case MC1_21_R3: {
                this.nmsBiome = new NMSBiome_1_21_3(advancedBiomeBase, seasonType, seasonData, n);
                return this.nmsBiome;
            }
            case MC1_21_R4: {
                this.nmsBiome = new NMSBiome_1_21_4(advancedBiomeBase, seasonType, seasonData, n);
                return this.nmsBiome;
            }
            case MC1_21_R5: {
                this.nmsBiome = new NMSBiome_1_21_5(advancedBiomeBase, seasonType, seasonData, n);
                return this.nmsBiome;
            }
            case MC1_21_R6: {
                this.nmsBiome = new NMSBiome_1_21_10(advancedBiomeBase, seasonType, seasonData, n);
                return this.nmsBiome;
            }
        }
        return null;
    }

    /*
     * WARNING - void declaration
     */
    private void generateTransitions() {
        for (Map.Entry entry : this.biomeMap.entrySet()) {
            AdvancedBiomeBase advancedBiomeBase = (AdvancedBiomeBase)entry.getValue();
            SeasonType[] seasonTypeArray = SeasonType.values();
            for (int i = 0; i < seasonTypeArray.length; ++i) {
                void object;
                SeasonType seasonType = seasonTypeArray[i];
                SeasonType seasonType2 = i == seasonTypeArray.length - 1 ? seasonTypeArray[0] : seasonTypeArray[i + 1];
                SeasonData seasonData = advancedBiomeBase.getSeasons().get((Object)seasonType);
                SeasonData seasonData2 = advancedBiomeBase.getSeasons().get((Object)seasonType2);
                HashMap<Integer, INMSBiome> hashMap = new HashMap<Integer, INMSBiome>();
                HashMap<ColorType, String[]> hashMap2 = new HashMap<ColorType, String[]>();
                ColorType[] colorTypeArray = ColorType.values();
                int n = colorTypeArray.length;
                boolean bl = false;
                while (object < n) {
                    ColorType colorType = colorTypeArray[object];
                    String string = seasonData.getColors().get((Object)colorType);
                    String string2 = seasonData2.getColors().get((Object)colorType);
                    String[] stringArray = TransitionColors.getBlendedPhases(string, string2);
                    hashMap2.put(colorType, stringArray);
                    ++object;
                }
                for (int j = 1; j < 4; ++j) {
                    SeasonData seasonData3 = new SeasonData();
                    if (j == 3 && (seasonData2.isSnow() || seasonData2.getWinterFreeze().booleanValue())) {
                        seasonData3.setSnow(true);
                    } else if (j != 3 && (seasonData.isSnow() || seasonData.getWinterFreeze().booleanValue())) {
                        seasonData3.setSnow(true);
                    }
                    for (ColorType colorType : ColorType.values()) {
                        seasonData3.setColor(colorType, ((String[])hashMap2.get((Object)colorType))[j - 1]);
                    }
                    INMSBiome iNMSBiome = this.loadNMSBiome(advancedBiomeBase, seasonType, seasonData3, j);
                    hashMap.put(j, iNMSBiome);
                }
                advancedBiomeBase.getSubbiomes().put(seasonType, hashMap);
            }
        }
    }

    public void loadAllBiomes(JavaPlugin javaPlugin, String string2, ImmutableMap.Builder<String, AdvancedBiomeBase> builder, ImmutableMap.Builder<BiomeDescriptor, AdvancedBiomeBase> builder2) {
        File file2 = new File(javaPlugin.getDataFolder(), string2);
        File[] fileArray = file2.listFiles((file, string) -> string.endsWith(".yml"));
        if (fileArray == null) {
            return;
        }
        HashMap<BiomeDescriptor, AdvancedBiomeBase> hashMap = new HashMap<BiomeDescriptor, AdvancedBiomeBase>();
        HashMap<String, AdvancedBiomeBase> hashMap2 = new HashMap<String, AdvancedBiomeBase>();
        for (File file3 : fileArray) {
            AdvancedBiomeBase advancedBiomeBase = this.loadBiomeFromFile(file3);
            for (BiomeDescriptor biomeDescriptor : advancedBiomeBase.getSubBiomesDescriptorList()) {
                hashMap.put(biomeDescriptor, advancedBiomeBase);
            }
            hashMap2.put(file3.getName().replace(".yml", ""), advancedBiomeBase);
        }
        builder.putAll(hashMap2);
        builder2.putAll(hashMap);
    }

    public AdvancedBiomeBase loadBiomeFromFile(File file) {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        AdvancedBiomeBase advancedBiomeBase = new AdvancedBiomeBase();
        yamlConfiguration.getStringList("biomes").stream().map(string -> BiomeDescriptor.fromString(string, true)).forEach(advancedBiomeBase::addBiomeDescriptor);
        advancedBiomeBase.setTemperature(yamlConfiguration.getInt("temperature"));
        advancedBiomeBase.setName(file.getName().replace(".yml", ""));
        for (String string2 : yamlConfiguration.getConfigurationSection("seasons").getKeys(false)) {
            SeasonData seasonData = new SeasonData();
            SeasonType seasonType = SeasonType.valueOf(string2.toUpperCase());
            for (ColorType colorType : ColorType.values()) {
                if (!yamlConfiguration.contains("seasons." + string2 + "." + colorType.getPath())) {
                    Core.getInstance().getLogger().warning("Failed to load COLOR " + colorType.getPath() + " for " + advancedBiomeBase.getName() + " " + string2);
                    continue;
                }
                seasonData.setColor(colorType, yamlConfiguration.getString("seasons." + string2 + "." + colorType.getPath()));
            }
            seasonData.setSnow(yamlConfiguration.getBoolean("seasons." + string2 + ".snow", false));
            if (yamlConfiguration.contains("seasons." + string2 + ".winter-freeze")) {
                seasonData.setWinterFreeze(yamlConfiguration.getBoolean("seasons." + string2 + ".winter-freeze"));
            }
            advancedBiomeBase.getSeasons().put(seasonType, seasonData);
        }
        return advancedBiomeBase;
    }

    public Integer getBiomeReplacement(Integer n, Season season, boolean bl) {
        if (bl) {
            return this.nmsBiome.getVanillaBiomeId(season.getBedrockBiome().name());
        }
        if (!BiomeUtils.getFallNoMixBiomes().contains(n)) {
            season = season.getRandomTransition();
        }
        if (!BiomeUtils.getDefaultColorBiomes().isEmpty() && BiomeUtils.getDefaultColorBiomes().contains(n)) {
            return null;
        }
        Integer n2 = this.seasonToBiomeMap.get((Object)season).getOrDefault(n, null);
        if (n2 == null) {
            return null;
        }
        return n2;
    }

    private void populateSeasonTransitionMap() {
        for (Season season : Season.values()) {
            HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
            for (AdvancedBiomeBase advancedBiomeBase : this.biomeMap.values()) {
                SeasonType seasonType = BiomeUtils.getSeasonType(season.getSeasonId());
                int n = season.getTransition() == 0 ? advancedBiomeBase.getBiome().get((Object)seasonType).getBiomeID() : advancedBiomeBase.getSubbiomes().get((Object)seasonType).get(season.getTransition()).getBiomeID();
                for (BiomeDescriptor biomeDescriptor : advancedBiomeBase.getSubBiomesDescriptorList()) {
                    int n2 = biomeDescriptor.biomeId();
                    if (!biomeDescriptor.hasValidId()) {
                        ASManager.debug("INVALID ID FOR VANILLA BIOME: " + n2);
                        continue;
                    }
                    hashMap.put(n2, n);
                }
            }
            this.seasonToBiomeMap.put(season, ImmutableMap.copyOf(hashMap));
        }
    }

    private void generateBiomes() {
        for (AdvancedBiomeBase advancedBiomeBase : this.biomeMap.values()) {
            for (SeasonType seasonType : SeasonType.values()) {
                try {
                    SeasonData seasonData = advancedBiomeBase.getSeasons().get((Object)seasonType);
                    INMSBiome iNMSBiome = this.loadNMSBiome(advancedBiomeBase, seasonType, seasonData, 0);
                    advancedBiomeBase.getBiome().put(seasonType, iNMSBiome);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @NotNull
    public Optional<AdvancedBiomeBase> getAdvancedBiome(BiomeDescriptor biomeDescriptor) {
        if (biomeDescriptor == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.advancedBiomesBySubBiomes.get(biomeDescriptor));
    }

    @NotNull
    public Optional<AdvancedBiomeBase> getAdvancedBiomeAt(@NotNull Location location) {
        return this.getAdvancedBiome(this.biomeDescriptorAt(location));
    }

    @NotNull
    public BiomeDescriptor biomeDescriptorAt(@NotNull Location location) {
        return BiomeDescriptor.fromLocation(location);
    }

    public boolean isFrozen(BiomeDescriptor biomeDescriptor, SeasonType seasonType) {
        if (!seasonType.equals((Object)SeasonType.WINTER)) {
            return false;
        }
        return this.getAdvancedBiome(biomeDescriptor).map(advancedBiomeBase -> advancedBiomeBase.getSeasons().get((Object)SeasonType.WINTER).getWinterFreeze() != false && advancedBiomeBase.getSeasons().get((Object)SeasonType.WINTER).isSnow()).orElse(false);
    }

    private ASBiomeData fromAdvancedBiome(AdvancedBiomeBase advancedBiomeBase) {
        NamespacedKey namespacedKey = new NamespacedKey("advancedseasons", advancedBiomeBase.getName());
        List list = advancedBiomeBase.getSubBiomesDescriptorList().stream().map(BiomeDescriptor::getKey).toList();
        Map<String, ASBiomeData.SeasonData> map = advancedBiomeBase.getSeasons().entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<String, ASBiomeData.SeasonData>(((SeasonType)((Object)((Object)entry.getKey()))).name(), this.fromInternal((SeasonData)entry.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ASBiomeData(namespacedKey, list, advancedBiomeBase.getTemperature(), map);
    }

    private ASBiomeData.SeasonData fromInternal(SeasonData seasonData) {
        Map<BiomeVisualKey, String> map = seasonData.getColors().entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<BiomeVisualKey, String>(BiomeVisualKey.valueOf(((ColorType)((Object)((Object)entry.getKey()))).name()), (String)entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ASBiomeData.SeasonData(seasonData.isSnow(), map);
    }

    public ImmutableMap<String, ASBiomeData> getBiomeData() {
        return this.biomeData;
    }

    public ImmutableMap<String, AdvancedBiomeBase> getBiomeMap() {
        return this.biomeMap;
    }

    public ImmutableMap<BiomeDescriptor, AdvancedBiomeBase> getAdvancedBiomesBySubBiomes() {
        return this.advancedBiomesBySubBiomes;
    }

    public INMSBiome getNmsBiome() {
        return this.nmsBiome;
    }

    public IProtocolLib getProtocolLib() {
        return this.protocolLib;
    }

    public EnumMap<Season, ImmutableMap<Integer, Integer>> getSeasonToBiomeMap() {
        return this.seasonToBiomeMap;
    }

    public ImmutableMap<Integer, String> getOptimizedIDs() {
        return this.optimizedIDs;
    }

    public RenderHandler getRenderHandler() {
        return this.renderHandler;
    }

    public HashSet<Integer> getReported() {
        return this.reported;
    }
}

