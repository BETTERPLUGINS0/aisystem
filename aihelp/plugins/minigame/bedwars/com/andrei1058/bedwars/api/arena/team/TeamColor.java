/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.api.arena.team;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public enum TeamColor {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    AQUA,
    WHITE,
    PINK,
    GRAY,
    DARK_GREEN,
    DARK_GRAY;


    public static ChatColor getChatColor(@NotNull String tColor) {
        TeamColor teamColor = TeamColor.valueOf(tColor.toUpperCase());
        ChatColor color = teamColor == PINK ? ChatColor.LIGHT_PURPLE : ChatColor.valueOf((String)teamColor.toString());
        return color;
    }

    public ChatColor chat() {
        TeamColor teamColor = TeamColor.valueOf(this.toString());
        ChatColor color = teamColor == PINK ? ChatColor.LIGHT_PURPLE : ChatColor.valueOf((String)teamColor.toString());
        return color;
    }

    @Deprecated
    public static ChatColor getChatColor(TeamColor teamColor) {
        ChatColor color = teamColor == PINK ? ChatColor.LIGHT_PURPLE : ChatColor.valueOf((String)teamColor.toString());
        return color;
    }

    @Deprecated
    public static DyeColor getDyeColor(@NotNull String teamColor) {
        DyeColor color;
        switch (TeamColor.valueOf(teamColor.toUpperCase()).ordinal()) {
            case 2: {
                color = DyeColor.LIME;
                break;
            }
            case 4: {
                color = DyeColor.LIGHT_BLUE;
                break;
            }
            case 8: {
                color = DyeColor.GREEN;
                break;
            }
            case 9: {
                color = DyeColor.GRAY;
                break;
            }
            default: {
                color = DyeColor.valueOf((String)teamColor.toUpperCase());
            }
        }
        return color;
    }

    public DyeColor dye() {
        DyeColor color;
        switch (this.ordinal()) {
            case 2: {
                color = DyeColor.LIME;
                break;
            }
            case 4: {
                color = DyeColor.LIGHT_BLUE;
                break;
            }
            case 8: {
                color = DyeColor.GREEN;
                break;
            }
            case 9: {
                color = DyeColor.GRAY;
                break;
            }
            default: {
                color = DyeColor.valueOf((String)this.toString());
            }
        }
        return color;
    }

    @Deprecated
    public static byte itemColor(@NotNull TeamColor teamColor) {
        int i = 0;
        switch (teamColor.ordinal()) {
            case 5: {
                break;
            }
            case 6: {
                i = 6;
                break;
            }
            case 0: {
                i = 14;
                break;
            }
            case 4: {
                i = 9;
                break;
            }
            case 2: {
                i = 5;
                break;
            }
            case 8: {
                i = 13;
                break;
            }
            case 3: {
                i = 4;
                break;
            }
            case 1: {
                i = 11;
                break;
            }
            case 7: {
                i = 8;
                break;
            }
            case 9: {
                i = 7;
            }
        }
        return (byte)i;
    }

    public byte itemByte() {
        int i = 0;
        switch (this.ordinal()) {
            case 5: {
                break;
            }
            case 6: {
                i = 6;
                break;
            }
            case 0: {
                i = 14;
                break;
            }
            case 4: {
                i = 9;
                break;
            }
            case 2: {
                i = 5;
                break;
            }
            case 8: {
                i = 13;
                break;
            }
            case 3: {
                i = 4;
                break;
            }
            case 1: {
                i = 11;
                break;
            }
            case 7: {
                i = 8;
                break;
            }
            case 9: {
                i = 7;
            }
        }
        return (byte)i;
    }

    public static String enName(@NotNull String material) {
        String name = "";
        switch (material.toUpperCase()) {
            case "PINK_WOOL": {
                name = "Pink";
                break;
            }
            case "RED_WOOL": {
                name = "Red";
                break;
            }
            case "LIGHT_GRAY_WOOL": {
                name = "Gray";
                break;
            }
            case "BLUE_WOOL": {
                name = "Blue";
                break;
            }
            case "WHITE_WOOL": {
                name = "White";
                break;
            }
            case "LIGHT_BLUE_WOOL": {
                name = "Aqua";
                break;
            }
            case "LIME_WOOL": {
                name = "Green";
                break;
            }
            case "YELLOW_WOOL": {
                name = "Yellow";
                break;
            }
            case "GRAY_WOOL": {
                name = "Dark_Gray";
            }
        }
        return name;
    }

    public static String enName(byte b) {
        String name = "";
        switch (b) {
            case 6: {
                name = "Pink";
                break;
            }
            case 14: {
                name = "Red";
                break;
            }
            case 9: {
                name = "Aqua";
                break;
            }
            case 5: {
                name = "Green";
                break;
            }
            case 4: {
                name = "Yellow";
                break;
            }
            case 11: {
                name = "Blue";
                break;
            }
            case 0: {
                name = "White";
                break;
            }
            case 8: {
                name = "Dark_Gray";
                break;
            }
            case 7: {
                name = "Gray";
            }
        }
        return name;
    }

    @Deprecated
    public static Color getColor(@NotNull TeamColor teamColor) {
        Color color = Color.WHITE;
        switch (teamColor.ordinal()) {
            case 6: {
                color = Color.FUCHSIA;
                break;
            }
            case 7: {
                color = Color.GRAY;
                break;
            }
            case 1: {
                color = Color.BLUE;
                break;
            }
            case 5: {
                break;
            }
            case 8: {
                color = Color.GREEN;
                break;
            }
            case 4: {
                color = Color.AQUA;
                break;
            }
            case 0: {
                color = Color.RED;
                break;
            }
            case 2: {
                color = Color.LIME;
                break;
            }
            case 3: {
                color = Color.YELLOW;
                break;
            }
            case 9: {
                color = Color.fromBGR((int)74, (int)74, (int)74);
            }
        }
        return color;
    }

    public Color bukkitColor() {
        Color color = Color.WHITE;
        switch (this.ordinal()) {
            case 6: {
                color = Color.FUCHSIA;
                break;
            }
            case 7: {
                color = Color.GRAY;
                break;
            }
            case 1: {
                color = Color.BLUE;
                break;
            }
            case 5: {
                break;
            }
            case 8: {
                color = Color.GREEN;
                break;
            }
            case 4: {
                color = Color.AQUA;
                break;
            }
            case 0: {
                color = Color.RED;
                break;
            }
            case 2: {
                color = Color.LIME;
                break;
            }
            case 3: {
                color = Color.YELLOW;
                break;
            }
            case 9: {
                color = Color.fromBGR((int)74, (int)74, (int)74);
            }
        }
        return color;
    }

    @Deprecated
    public static Material getBedBlock(@NotNull TeamColor teamColor) {
        String color = "RED_BED";
        switch (teamColor.ordinal()) {
            case 6: {
                color = "PINK_BED";
                break;
            }
            case 7: {
                color = "LIGHT_GRAY_BED";
                break;
            }
            case 1: {
                color = "BLUE_BED";
                break;
            }
            case 5: {
                color = "WHITE_BED";
                break;
            }
            case 8: {
                color = "GREEN_BED";
                break;
            }
            case 4: {
                color = "LIGHT_BLUE_BED";
                break;
            }
            case 2: {
                color = "LIME_BED";
                break;
            }
            case 3: {
                color = "YELLOW_BED";
                break;
            }
            case 9: {
                color = "GRAY_BED";
            }
        }
        return Material.valueOf((String)color);
    }

    public Material bedMaterial() {
        String color = "RED_BED";
        switch (this.ordinal()) {
            case 6: {
                color = "PINK_BED";
                break;
            }
            case 7: {
                color = "LIGHT_GRAY_BED";
                break;
            }
            case 1: {
                color = "BLUE_BED";
                break;
            }
            case 5: {
                color = "WHITE_BED";
                break;
            }
            case 8: {
                color = "GREEN_BED";
                break;
            }
            case 4: {
                color = "LIGHT_BLUE_BED";
                break;
            }
            case 2: {
                color = "LIME_BED";
                break;
            }
            case 3: {
                color = "YELLOW_BED";
                break;
            }
            case 9: {
                color = "GRAY_BED";
            }
        }
        return Material.valueOf((String)color);
    }

    @Deprecated
    public static Material getGlass(@NotNull TeamColor teamColor) {
        String color = "GLASS";
        switch (teamColor.ordinal()) {
            case 6: {
                color = "PINK_STAINED_GLASS";
                break;
            }
            case 7: {
                color = "LIGHT_GRAY_STAINED_GLASS";
                break;
            }
            case 1: {
                color = "BLUE_STAINED_GLASS";
                break;
            }
            case 5: {
                color = "WHITE_STAINED_GLASS";
                break;
            }
            case 8: {
                color = "GREEN_STAINED_GLASS";
                break;
            }
            case 4: {
                color = "LIGHT_BLUE_STAINED_GLASS";
                break;
            }
            case 2: {
                color = "LIME_STAINED_GLASS";
                break;
            }
            case 3: {
                color = "YELLOW_STAINED_GLASS";
                break;
            }
            case 0: {
                color = "RED_STAINED_GLASS";
                break;
            }
            case 9: {
                color = "GRAY_STAINED_GLASS";
            }
        }
        return Material.valueOf((String)color);
    }

    public Material glassMaterial() {
        String color = "GLASS";
        switch (this.ordinal()) {
            case 6: {
                color = "PINK_STAINED_GLASS";
                break;
            }
            case 7: {
                color = "LIGHT_GRAY_STAINED_GLASS";
                break;
            }
            case 1: {
                color = "BLUE_STAINED_GLASS";
                break;
            }
            case 5: {
                color = "WHITE_STAINED_GLASS";
                break;
            }
            case 8: {
                color = "GREEN_STAINED_GLASS";
                break;
            }
            case 4: {
                color = "LIGHT_BLUE_STAINED_GLASS";
                break;
            }
            case 2: {
                color = "LIME_STAINED_GLASS";
                break;
            }
            case 3: {
                color = "YELLOW_STAINED_GLASS";
                break;
            }
            case 0: {
                color = "RED_STAINED_GLASS";
                break;
            }
            case 9: {
                color = "GRAY_STAINED_GLASS";
            }
        }
        return Material.valueOf((String)color);
    }

    @Deprecated
    public static Material getGlassPane(@NotNull TeamColor teamColor) {
        String color = "GLASS";
        switch (teamColor.ordinal()) {
            case 6: {
                color = "PINK_STAINED_GLASS_PANE";
                break;
            }
            case 7: {
                color = "LIGHT_GRAY_STAINED_GLASS_PANE";
                break;
            }
            case 1: {
                color = "BLUE_STAINED_GLASS_PANE";
                break;
            }
            case 5: {
                color = "WHITE_STAINED_GLASS_PANE";
                break;
            }
            case 8: {
                color = "GREEN_STAINED_GLASS_PANE";
                break;
            }
            case 4: {
                color = "LIGHT_BLUE_STAINED_GLASS_PANE";
                break;
            }
            case 2: {
                color = "LIME_STAINED_GLASS_PANE";
                break;
            }
            case 3: {
                color = "YELLOW_STAINED_GLASS_PANE";
                break;
            }
            case 0: {
                color = "RED_STAINED_GLASS_PANE";
                break;
            }
            case 9: {
                color = "GRAY_STAINED_PANE";
            }
        }
        return Material.valueOf((String)color);
    }

    public Material glassPaneMaterial() {
        String color = "GLASS";
        switch (this.ordinal()) {
            case 6: {
                color = "PINK_STAINED_GLASS_PANE";
                break;
            }
            case 7: {
                color = "LIGHT_GRAY_STAINED_GLASS_PANE";
                break;
            }
            case 1: {
                color = "BLUE_STAINED_GLASS_PANE";
                break;
            }
            case 5: {
                color = "WHITE_STAINED_GLASS_PANE";
                break;
            }
            case 8: {
                color = "GREEN_STAINED_GLASS_PANE";
                break;
            }
            case 4: {
                color = "LIGHT_BLUE_STAINED_GLASS_PANE";
                break;
            }
            case 2: {
                color = "LIME_STAINED_GLASS_PANE";
                break;
            }
            case 3: {
                color = "YELLOW_STAINED_GLASS_PANE";
                break;
            }
            case 0: {
                color = "RED_STAINED_GLASS_PANE";
                break;
            }
            case 9: {
                color = "GRAY_STAINED_PANE";
            }
        }
        return Material.valueOf((String)color);
    }

    @Deprecated
    public static Material getGlazedTerracotta(@NotNull TeamColor teamColor) {
        String color = "ORANGE_TERRACOTTA";
        switch (teamColor.ordinal()) {
            case 6: {
                color = "PINK_TERRACOTTA";
                break;
            }
            case 7: {
                color = "LIGHT_GRAY_TERRACOTTA";
                break;
            }
            case 9: {
                color = "GRAY_TERRACOTTA";
                break;
            }
            case 1: {
                color = "BLUE_TERRACOTTA";
                break;
            }
            case 5: {
                color = "WHITE_TERRACOTTA";
                break;
            }
            case 8: {
                color = "GREEN_TERRACOTTA";
                break;
            }
            case 4: {
                color = "LIGHT_BLUE_TERRACOTTA";
                break;
            }
            case 2: {
                color = "LIME_TERRACOTTA";
                break;
            }
            case 3: {
                color = "YELLOW_TERRACOTTA";
                break;
            }
            case 0: {
                color = "RED_TERRACOTTA";
            }
        }
        return Material.valueOf((String)color);
    }

    public Material glazedTerracottaMaterial() {
        String color = "ORANGE_TERRACOTTA";
        switch (this.ordinal()) {
            case 6: {
                color = "PINK_TERRACOTTA";
                break;
            }
            case 7: {
                color = "LIGHT_GRAY_TERRACOTTA";
                break;
            }
            case 9: {
                color = "GRAY_TERRACOTTA";
                break;
            }
            case 1: {
                color = "BLUE_TERRACOTTA";
                break;
            }
            case 5: {
                color = "WHITE_TERRACOTTA";
                break;
            }
            case 8: {
                color = "GREEN_TERRACOTTA";
                break;
            }
            case 4: {
                color = "LIGHT_BLUE_TERRACOTTA";
                break;
            }
            case 2: {
                color = "LIME_TERRACOTTA";
                break;
            }
            case 3: {
                color = "YELLOW_TERRACOTTA";
                break;
            }
            case 0: {
                color = "RED_TERRACOTTA";
            }
        }
        return Material.valueOf((String)color);
    }

    @Deprecated
    public static Material getWool(@NotNull TeamColor teamColor) {
        String color = "WHITE_WOOL";
        switch (teamColor.ordinal()) {
            case 6: {
                color = "PINK_WOOL";
                break;
            }
            case 7: {
                color = "LIGHT_GRAY_WOOL";
                break;
            }
            case 9: {
                color = "GRAY_WOOL";
                break;
            }
            case 1: {
                color = "BLUE_WOOL";
                break;
            }
            case 5: {
                color = "WHITE_WOOL";
                break;
            }
            case 8: {
                color = "GREEN_WOOL";
                break;
            }
            case 4: {
                color = "LIGHT_BLUE_WOOL";
                break;
            }
            case 2: {
                color = "LIME_WOOL";
                break;
            }
            case 3: {
                color = "YELLOW_WOOL";
                break;
            }
            case 0: {
                color = "RED_WOOL";
            }
        }
        return Material.valueOf((String)color);
    }

    public Material woolMaterial() {
        String color = "WHITE_WOOL";
        switch (this.ordinal()) {
            case 6: {
                color = "PINK_WOOL";
                break;
            }
            case 7: {
                color = "LIGHT_GRAY_WOOL";
                break;
            }
            case 9: {
                color = "GRAY_WOOL";
                break;
            }
            case 1: {
                color = "BLUE_WOOL";
                break;
            }
            case 5: {
                color = "WHITE_WOOL";
                break;
            }
            case 8: {
                color = "GREEN_WOOL";
                break;
            }
            case 4: {
                color = "LIGHT_BLUE_WOOL";
                break;
            }
            case 2: {
                color = "LIME_WOOL";
                break;
            }
            case 3: {
                color = "YELLOW_WOOL";
                break;
            }
            case 0: {
                color = "RED_WOOL";
            }
        }
        return Material.valueOf((String)color);
    }
}

