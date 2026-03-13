/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Keyed
 *  org.bukkit.Location
 *  org.bukkit.NamespacedKey
 *  org.bukkit.block.Biome
 *  org.bukkit.block.Block
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.seasons.biomes;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.BiomeUtils;
import net.advancedplugins.seasons.nms.NMSBiomeHelper;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class BiomeDescriptor
implements Keyed {
    @NotNull
    private static final Map<NamespacedKey, BiomeDescriptor> CACHE = new ConcurrentHashMap<NamespacedKey, BiomeDescriptor>();
    @NotNull
    private final NamespacedKey biomeKey;
    @NotNull
    private final Biome bukkitBiome;
    private final int biomeId;

    private BiomeDescriptor(@NotNull NamespacedKey namespacedKey) {
        Biome biome;
        this.biomeKey = Objects.requireNonNull(namespacedKey, "biomeKey");
        try {
            biome = Biome.valueOf((String)namespacedKey.getKey().toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            biome = Biome.CUSTOM;
        }
        this.bukkitBiome = biome;
        Integer n = NMSBiomeHelper.nmsBiomeUtil().getVanillaBiomeId(namespacedKey.toString());
        if (n == null && !"minecraft:custom".equals(namespacedKey.toString())) {
            Core.logger().warning("Cannot get id for biome " + String.valueOf(namespacedKey));
        }
        this.biomeId = n == null ? -1 : n;
    }

    @NotNull
    public String name() {
        return this.getKey().getKey();
    }

    @NotNull
    public String upperCaseName() {
        return this.name().toUpperCase();
    }

    @NotNull
    public NamespacedKey getKey() {
        return this.biomeKey;
    }

    @NotNull
    public Biome bukkitBiome() {
        return this.bukkitBiome;
    }

    public boolean hasValidId() {
        return this.biomeId != -1;
    }

    public int biomeId() {
        return this.biomeId;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        BiomeDescriptor biomeDescriptor = (BiomeDescriptor)object;
        return Objects.equals(this.biomeKey, biomeDescriptor.biomeKey);
    }

    public int hashCode() {
        return Objects.hashCode(this.biomeKey);
    }

    @NotNull
    public static BiomeDescriptor fromString(@NotNull String string, boolean bl) {
        if (BiomeUtils.isArtificial(string)) {
            throw new IllegalArgumentException("BiomeDescriptor cannot be used with artificial biomes");
        }
        string = string.toLowerCase(Locale.ROOT);
        String[] stringArray = string.split(":");
        NamespacedKey namespacedKey = switch (stringArray.length) {
            case 1 -> NamespacedKey.minecraft((String)string);
            case 2 -> new NamespacedKey(stringArray[0], stringArray[1]);
            default -> throw new IllegalArgumentException("Invalid key (separator ':' is repeated)");
        };
        BiomeDescriptor biomeDescriptor = CACHE.get(namespacedKey);
        if (biomeDescriptor == null && (biomeDescriptor = new BiomeDescriptor(namespacedKey)).hasValidId() && bl) {
            CACHE.put(namespacedKey, biomeDescriptor);
        }
        return biomeDescriptor;
    }

    @NotNull
    public static BiomeDescriptor createFromString(@NotNull String string) {
        string = string.toLowerCase(Locale.ROOT);
        String[] stringArray = string.split(":");
        NamespacedKey namespacedKey = switch (stringArray.length) {
            case 1 -> NamespacedKey.minecraft((String)string);
            case 2 -> new NamespacedKey(stringArray[0], stringArray[1]);
            default -> throw new IllegalArgumentException("Invalid key (separator ':' is repeated)");
        };
        return new BiomeDescriptor(namespacedKey);
    }

    public static BiomeDescriptor fromBiome(@NotNull Biome biome) {
        try {
            return BiomeDescriptor.fromString(biome.name(), true);
        } catch (Exception exception) {
            ASManager.getInstance().getLogger().warning("Failed to get biome from " + biome.name());
            return null;
        }
    }

    @NotNull
    public static BiomeDescriptor fromLocation(@NotNull Location location) {
        return BiomeDescriptor.fromString(NMSBiomeHelper.nmsBiomeUtil().getBiome(location), true);
    }

    @NotNull
    public static BiomeDescriptor fromBlock(@NotNull Block block) {
        return BiomeDescriptor.fromLocation(block.getLocation());
    }

    static {
        NamespacedKey namespacedKey = NamespacedKey.minecraft((String)"custom");
        CACHE.put(namespacedKey, new BiomeDescriptor(namespacedKey));
    }
}

