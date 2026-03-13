/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.halloween;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.halloween.HalloweenListener;
import com.andrei1058.bedwars.halloween.shop.PumpkinContent;
import com.andrei1058.bedwars.metrics.MetricsManager;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.shop.main.ShopCategory;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class HalloweenSpecial {
    private static HalloweenSpecial INSTANCE;

    private HalloweenSpecial() {
        PumpkinContent content;
        BedWars.plugin.getLogger().info(String.valueOf(ChatColor.AQUA) + "Loaded Halloween Special <3");
        Bukkit.getPluginManager().registerEvents((Listener)new HalloweenListener(), (Plugin)BedWars.plugin);
        ShopCategory blockCategory = ShopManager.getShop().getCategoryList().stream().filter(category -> category.getName().equals("blocks-category")).findFirst().orElse(null);
        if (blockCategory != null && (content = new PumpkinContent(blockCategory)).isLoaded()) {
            blockCategory.getCategoryContentList().add(content);
        }
    }

    public static void init() {
        boolean enable = BedWars.config.getBoolean("enable-halloween-feature");
        if (enable && INSTANCE == null && HalloweenSpecial.checkAvailabilityDate()) {
            INSTANCE = new HalloweenSpecial();
        }
        MetricsManager.appendPie("halloween_special_enable", () -> String.valueOf(enable));
    }

    protected static boolean checkAvailabilityDate() {
        ZoneId zone = ZoneId.of("Europe/Rome");
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(zone).toLocalDate();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        return month == 10 && day > 21 || month == 11 && day < 2;
    }

    public static HalloweenSpecial getINSTANCE() {
        return INSTANCE;
    }
}

