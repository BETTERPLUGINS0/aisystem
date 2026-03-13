/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.seasons.handlers.sub;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.Vector;

public class SnowPatternUtil {
    private static final int[][] SNOW_PATTERN = new int[][]{{1, 6, 11, 16, 4, 9, 14, 2, 7, 12, 15, 5, 10, 3, 8, 4}, {2, 7, 12, 15, 5, 10, 3, 8, 13, 1, 6, 11, 16, 4, 9, 14}, {3, 8, 13, 1, 6, 11, 16, 4, 9, 14, 2, 7, 12, 15, 5, 1}, {4, 9, 14, 2, 7, 12, 15, 5, 10, 3, 8, 13, 1, 6, 11, 16}, {5, 10, 3, 8, 13, 1, 6, 11, 16, 4, 9, 14, 12, 1, 12, 15}, {6, 11, 16, 4, 9, 14, 2, 7, 12, 15, 5, 10, 3, 8, 13, 1}, {7, 12, 15, 5, 10, 3, 8, 13, 1, 6, 11, 2, 4, 9, 14, 2}, {8, 13, 1, 6, 11, 16, 4, 9, 14, 2, 7, 12, 15, 5, 10, 13}, {9, 14, 2, 7, 12, 15, 5, 10, 3, 8, 13, 1, 6, 11, 16, 4}, {10, 3, 8, 13, 7, 6, 11, 16, 4, 9, 14, 2, 7, 12, 15, 5}, {11, 16, 4, 9, 14, 2, 7, 12, 15, 5, 10, 3, 8, 13, 1, 6}, {12, 15, 5, 10, 3, 8, 13, 1, 6, 11, 16, 4, 9, 14, 2, 12}, {13, 1, 6, 4, 16, 4, 9, 14, 2, 7, 12, 15, 5, 10, 3, 8}, {2, 14, 7, 12, 15, 5, 10, 3, 8, 13, 1, 6, 11, 16, 4, 9}, {15, 5, 10, 3, 8, 13, 1, 6, 11, 16, 4, 9, 14, 2, 7, 12}, {16, 4, 9, 14, 2, 7, 12, 15, 5, 10, 3, 8, 13, 1, 6, 11}};
    private static final int[][] MELT_PATTERN = new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
    public static final int maxStage = 16;
    private static final ImmutableMap<Integer, List<Vector>> STATE_TO_OFFSETS_MAP = SnowPatternUtil.generateStateToOffsetsMap(SNOW_PATTERN, 16);
    public static final ImmutableMap<Integer, List<Vector>> MELT_STATE_TO_OFFSETS_MAP = SnowPatternUtil.generateStateToOffsetsMap(MELT_PATTERN, 1);

    private static ImmutableMap<Integer, List<Vector>> generateStateToOffsetsMap(int[][] nArray, int n) {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (int i = 1; i <= n; ++i) {
            ArrayList<Vector> arrayList = new ArrayList<Vector>();
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    if (nArray[j][k] != i) continue;
                    arrayList.add(new Vector(j, 0, k));
                }
            }
            builder.put(i, arrayList);
        }
        return builder.build();
    }

    public static List<Vector> getOffsetsForStage(int n) {
        return STATE_TO_OFFSETS_MAP.get(n);
    }
}

