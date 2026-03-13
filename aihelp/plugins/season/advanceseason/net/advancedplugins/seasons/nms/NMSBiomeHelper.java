/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.UnmodifiableView
 */
package net.advancedplugins.seasons.nms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.seasons.biomes.BiomeDescriptor;
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
import net.advancedplugins.seasons.objects.INMSBiome;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

@ApiStatus.Experimental
public final class NMSBiomeHelper {
    private static final @UnmodifiableView List<BiomeDescriptor> ALL_BIOME_DESCRIPTORS;
    private static volatile INMSBiome NMS_BIOME_INSTANCE;

    public static @UnmodifiableView List<BiomeDescriptor> allBiomeDescriptors() {
        return ALL_BIOME_DESCRIPTORS;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @NotNull
    public static INMSBiome nmsBiomeUtil() {
        if (NMS_BIOME_INSTANCE != null) return NMS_BIOME_INSTANCE;
        Class<INMSBiome> clazz = INMSBiome.class;
        synchronized (INMSBiome.class) {
            if (NMS_BIOME_INSTANCE != null) return NMS_BIOME_INSTANCE;
            NMS_BIOME_INSTANCE = NMSBiomeHelper.loadNMSBiome();
            // ** MonitorExit[var0] (shouldn't be in output)
            return NMS_BIOME_INSTANCE;
        }
    }

    private static INMSBiome loadNMSBiome() {
        MinecraftVersion minecraftVersion = MinecraftVersion.getCurrentVersion();
        switch (minecraftVersion) {
            case MC1_20_R1: {
                return new NMSBiome_1_20_1();
            }
            case MC1_20_R2: {
                return new NMSBiome_1_20_2();
            }
            case MC1_20_R3: {
                return new NMSBiome_1_20_3();
            }
            case MC1_20_R4: {
                return new NMSBiome_1_20_4_Spigot();
            }
            case MC1_21_R1: {
                return new NMSBiome_1_21();
            }
            case MC1_21_R2: {
                return new NMSBiome_1_21_2();
            }
            case MC1_21_R3: {
                return new NMSBiome_1_21_3();
            }
            case MC1_21_R4: {
                return new NMSBiome_1_21_4();
            }
            case MC1_21_R5: {
                return new NMSBiome_1_21_5();
            }
            case MC1_21_R6: {
                return new NMSBiome_1_21_10();
            }
        }
        throw new IllegalStateException("Unhandled version '" + String.valueOf((Object)minecraftVersion) + "'");
    }

    static {
        ArrayList<BiomeDescriptor> arrayList = new ArrayList<BiomeDescriptor>();
        for (String string : NMSBiomeHelper.nmsBiomeUtil().getBiomes()) {
            arrayList.add(BiomeDescriptor.fromString(string, true));
        }
        ALL_BIOME_DESCRIPTORS = Collections.unmodifiableList(arrayList);
    }
}

