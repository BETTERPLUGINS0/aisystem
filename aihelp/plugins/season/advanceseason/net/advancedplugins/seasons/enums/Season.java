/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Biome
 */
package net.advancedplugins.seasons.enums;

import java.util.concurrent.ThreadLocalRandom;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.seasons.enums.SeasonType;
import org.bukkit.block.Biome;

public enum Season {
    SPRING(SeasonType.SPRING, 0, 0, Biome.PLAINS),
    SPRING_TRANSITION_1(SeasonType.SPRING, 1, 0, Biome.PLAINS),
    SPRING_TRANSITION_2(SeasonType.SPRING, 2, 0, Biome.PLAINS),
    SPRING_TRANSITION_3(SeasonType.SUMMER, 3, 0, Biome.MUSHROOM_FIELDS),
    SUMMER(SeasonType.SUMMER, 0, 1, Biome.MUSHROOM_FIELDS),
    SUMMER_TRANSITION_1(SeasonType.SUMMER, 1, 1, Biome.MUSHROOM_FIELDS),
    SUMMER_TRANSITION_2(SeasonType.SUMMER, 2, 1, Biome.MUSHROOM_FIELDS),
    SUMMER_TRANSITION_3(SeasonType.FALL, 3, 1, Biome.DESERT),
    FALL(SeasonType.FALL, 0, 2, Biome.DESERT),
    FALL_TRANSITION_1(SeasonType.FALL, 1, 2, Biome.DESERT),
    FALL_TRANSITION_2(SeasonType.FALL, 2, 2, Biome.DESERT),
    FALL_TRANSITION_3(SeasonType.WINTER, 3, 2, Biome.SNOWY_TAIGA),
    WINTER(SeasonType.WINTER, 0, 3, Biome.SNOWY_TAIGA),
    WINTER_TRANSITION_1(SeasonType.WINTER, 1, 3, Biome.SNOWY_TAIGA),
    WINTER_TRANSITION_2(SeasonType.WINTER, 2, 3, Biome.SNOWY_TAIGA),
    WINTER_TRANSITION_3(SeasonType.SPRING, 3, 3, Biome.PLAINS);

    private final int transition;
    private final int seasonId;
    private final SeasonType type;
    private Season[] transitions;
    private final Biome bedrockBiome;

    private Season(SeasonType seasonType, int n2, int n3, Biome biome) {
        this.type = seasonType;
        this.seasonId = n3;
        this.transition = n2;
        this.bedrockBiome = biome;
    }

    public Season getRandomTransition() {
        if (this.transitions == null || this.transitions.length == 0) {
            return this;
        }
        int n = ThreadLocalRandom.current().nextInt(this.transitions.length);
        return this.transitions[n];
    }

    public static void initializeTransitions() {
        Season.FALL.transitions = new Season[]{FALL, FALL_TRANSITION_1, SUMMER_TRANSITION_2, SUMMER_TRANSITION_3};
        Season.FALL_TRANSITION_1.transitions = new Season[]{FALL_TRANSITION_1, FALL_TRANSITION_1, FALL, FALL_TRANSITION_2};
        Season.FALL_TRANSITION_2.transitions = new Season[]{FALL_TRANSITION_2, FALL_TRANSITION_2, FALL_TRANSITION_2, FALL_TRANSITION_1};
        Season.FALL_TRANSITION_3.transitions = new Season[]{FALL_TRANSITION_3, FALL_TRANSITION_3, WINTER, WINTER};
        Season.SUMMER_TRANSITION_3.transitions = new Season[]{SUMMER_TRANSITION_3, SUMMER_TRANSITION_3, SUMMER_TRANSITION_2, SUMMER_TRANSITION_2};
        Season.SPRING_TRANSITION_1.transitions = new Season[]{SPRING, SPRING, SPRING_TRANSITION_1, SPRING_TRANSITION_1};
        Season.SPRING_TRANSITION_2.transitions = new Season[]{SPRING_TRANSITION_1, SPRING_TRANSITION_1, SPRING_TRANSITION_2, SPRING_TRANSITION_2};
        Season.SPRING_TRANSITION_3.transitions = new Season[]{SUMMER, SUMMER, SPRING_TRANSITION_3, SPRING_TRANSITION_3};
    }

    public static Season fromName(String string) {
        for (Season season : Season.values()) {
            if (!season.name().equalsIgnoreCase(string)) continue;
            return season;
        }
        return null;
    }

    public String getName() {
        return ASManager.capitalize(this.name().split("_")[0]);
    }

    public int getTransition() {
        return this.transition;
    }

    public int getSeasonId() {
        return this.seasonId;
    }

    public SeasonType getType() {
        return this.type;
    }

    public Season[] getTransitions() {
        return this.transitions;
    }

    public Biome getBedrockBiome() {
        return this.bedrockBiome;
    }
}

