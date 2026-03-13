/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Biome
 */
package net.advancedplugins.seasons.biomes;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.BiomeDescriptor;
import net.advancedplugins.seasons.biomes.BiomesHandler;
import net.advancedplugins.seasons.enums.Season;
import net.advancedplugins.seasons.enums.SeasonType;
import org.bukkit.block.Biome;

public class BiomeUtils {
    private static ImmutableList<Integer> fallNoMixBiomes;
    private static ImmutableList<Biome> noSnowBiomes;
    private static BiomesHandler biomesHandler;
    private static ImmutableList<Integer> defaultColorBiomes;
    private static final ImmutableList<String> snowyBiomes;

    public static void init(BiomesHandler biomesHandler) {
        BiomeUtils.biomesHandler = biomesHandler;
        BiomeUtils.generateFallNoMixBiomes();
        BiomeUtils.generateNoSnowBiomes(biomesHandler);
        BiomeUtils.generateDefaultColorBiomes(biomesHandler);
    }

    private static void generateNoSnowBiomes(BiomesHandler biomesHandler) {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (Biome biome : Biome.values()) {
            if (BiomeUtils.isArtificial(biome)) continue;
            biomesHandler.getAdvancedBiome(BiomeDescriptor.fromBiome(biome)).ifPresent(advancedBiomeBase -> {
                if (advancedBiomeBase.getSeasons().get((Object)SeasonType.WINTER).isSnow()) {
                    return;
                }
                builder.add(biome);
            });
        }
        noSnowBiomes = builder.build();
    }

    private static void generateDefaultColorBiomes(BiomesHandler biomesHandler) {
        defaultColorBiomes = ((ImmutableList.Builder)ImmutableList.builder().addAll((Iterable)Core.getInstance().getConfig().getStringList("defaultColorBiomes").stream().map(biomesHandler::getBiomeId).filter(Objects::nonNull).collect(Collectors.toList()))).build();
    }

    private static void generateFallNoMixBiomes() {
        fallNoMixBiomes = ((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)ImmutableList.builder().add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.SAVANNA_PLATEAU.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.SAVANNA.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.OCEAN.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.RIVER.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.FROZEN_RIVER.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.COLD_OCEAN.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.DEEP_COLD_OCEAN.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.DEEP_FROZEN_OCEAN.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.WARM_OCEAN.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.LUKEWARM_OCEAN.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.DEEP_LUKEWARM_OCEAN.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.BEACH.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.DESERT.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.JUNGLE.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.SPARSE_JUNGLE.name().toLowerCase()))).add(biomesHandler.getNmsBiome().getVanillaBiomeId("minecraft:" + Biome.BAMBOO_JUNGLE.name().toLowerCase()))).build();
    }

    public static Season getTransitionEnum(SeasonType seasonType, int n) {
        if (n > 3) {
            switch (seasonType) {
                case SPRING: {
                    return Season.SUMMER;
                }
                case SUMMER: {
                    return Season.FALL;
                }
                case FALL: {
                    return Season.WINTER;
                }
                case WINTER: {
                    return Season.SPRING;
                }
            }
            return null;
        }
        switch (seasonType) {
            case SPRING: {
                switch (n) {
                    case 0: {
                        return Season.SPRING;
                    }
                    case 1: {
                        return Season.SPRING_TRANSITION_1;
                    }
                    case 2: {
                        return Season.SPRING_TRANSITION_2;
                    }
                    case 3: {
                        return Season.SPRING_TRANSITION_3;
                    }
                }
            }
            case SUMMER: {
                switch (n) {
                    case 0: {
                        return Season.SUMMER;
                    }
                    case 1: {
                        return Season.SUMMER_TRANSITION_1;
                    }
                    case 2: {
                        return Season.SUMMER_TRANSITION_2;
                    }
                    case 3: {
                        return Season.SUMMER_TRANSITION_3;
                    }
                }
            }
            case FALL: {
                switch (n) {
                    case 0: {
                        return Season.FALL;
                    }
                    case 1: {
                        return Season.FALL_TRANSITION_1;
                    }
                    case 2: {
                        return Season.FALL_TRANSITION_2;
                    }
                    case 3: {
                        return Season.FALL_TRANSITION_3;
                    }
                }
            }
            case WINTER: {
                switch (n) {
                    case 0: {
                        return Season.WINTER;
                    }
                    case 1: {
                        return Season.WINTER_TRANSITION_1;
                    }
                    case 2: {
                        return Season.WINTER_TRANSITION_2;
                    }
                    case 3: {
                        return Season.WINTER_TRANSITION_3;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Invalid season or transition");
    }

    public static SeasonType getSeasonType(int n) {
        return Arrays.stream(Season.values()).filter(season -> season.getSeasonId() == n).findFirst().get().getType();
    }

    public static boolean isArtificial(Biome biome) {
        return BiomeUtils.isArtificial(biome.name());
    }

    public static boolean isArtificial(String string) {
        return string.startsWith("advanced");
    }

    public static ImmutableList<Integer> getFallNoMixBiomes() {
        return fallNoMixBiomes;
    }

    public static ImmutableList<Biome> getNoSnowBiomes() {
        return noSnowBiomes;
    }

    public static ImmutableList<Integer> getDefaultColorBiomes() {
        return defaultColorBiomes;
    }

    public static ImmutableList<String> getSnowyBiomes() {
        return snowyBiomes;
    }

    static {
        snowyBiomes = ImmutableList.of("SNOWY_PLAINS", "SNOWY_SLOPES", "SNOWY_BEACH", "SNOWY_TAIGA", "ICE_SPIKES", "FROZEN_RIVER", "FROZEN_PEAKS", "FROZEN_OCEAN", "DEEP_FROZEN_OCEAN");
    }
}

