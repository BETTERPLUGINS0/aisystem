/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.magmacore.menus;

import com.magmaguy.magmacore.MagmaCore;
import com.magmaguy.magmacore.menus.AdvancedMenu;
import com.magmaguy.magmacore.menus.MenuButton;
import com.magmaguy.magmacore.util.ChatColorConverter;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FirstTimeSetupMenu {
    private final List<Integer> archetypeSlots = List.of((Object)11, (Object)15, (Object)13);
    private final JavaPlugin ownerPlugin;
    private final Player player;
    private final List<MenuButton> menuItems;
    private final MenuButton infoItem;

    public FirstTimeSetupMenu(Player player, String title, String subtitle, MenuButton infoButton, List<MenuButton> menuButtons) {
        this(MagmaCore.getInstance().getRequestingPlugin(), player, title, subtitle, infoButton, menuButtons);
    }

    public FirstTimeSetupMenu(JavaPlugin ownerPlugin, Player player, String title, String subtitle, MenuButton infoButton, List<MenuButton> menuButtons) {
        this.ownerPlugin = ownerPlugin;
        this.animateTitle(player, title, subtitle);
        this.player = player;
        this.menuItems = menuButtons;
        this.infoItem = infoButton;
    }

    private void createMenu() {
        AdvancedMenu advancedMenu = new AdvancedMenu(this.player, this.menuItems.isEmpty() ? 9 : 18);
        advancedMenu.addAdvancedMenuItem(4, this.infoItem);
        for (int i = 0; i < this.menuItems.size(); ++i) {
            advancedMenu.addAdvancedMenuItem(this.archetypeSlots.get(i), this.menuItems.get(i));
        }
        advancedMenu.openInventory(this.player);
    }

    private void animateTitle(final Player player, final String title, final String subtitle) {
        final int titleVisible = FirstTimeSetupMenu.visibleLength(title);
        final int subtitleVisible = FirstTimeSetupMenu.visibleLength(subtitle);
        new BukkitRunnable(){
            int counter = 0;

            public void run() {
                ++this.counter;
                if (this.counter > titleVisible + subtitleVisible + 30) {
                    this.cancel();
                    FirstTimeSetupMenu.this.createMenu();
                    return;
                }
                String titleSequence = ChatColorConverter.convert(FirstTimeSetupMenu.visibleSubstring(title, Math.min(this.counter, titleVisible)));
                int subtitleCounter = Math.min(this.counter - titleVisible, subtitleVisible);
                String subtitleSequence = "";
                if (subtitleCounter > 0) {
                    subtitleSequence = ChatColorConverter.convert(FirstTimeSetupMenu.visibleSubstring(subtitle, subtitleCounter));
                }
                player.sendTitle(titleSequence, subtitleSequence, 0, 2, 2);
            }
        }.runTaskTimer((Plugin)this.ownerPlugin, 0L, 1L);
    }

    private static int visibleLength(String text) {
        int count = 0;
        int i = 0;
        while (i < text.length()) {
            int close;
            if (text.charAt(i) == '<' && (text.startsWith("<g:", i) || text.startsWith("<gradient:", i)) && (close = text.indexOf(62, i)) != -1) {
                i = close + 1;
                continue;
            }
            if (text.charAt(i) == '<' && (text.startsWith("</g>", i) || text.startsWith("</gradient>", i))) {
                i += text.startsWith("</g>", i) ? 4 : 11;
                continue;
            }
            if (text.charAt(i) == '<' && text.startsWith("<#", i) && (close = text.indexOf(62, i)) != -1) {
                i = close + 1;
                continue;
            }
            if (text.charAt(i) == '&' && i + 1 < text.length()) {
                if (text.charAt(i + 1) == '#' && i + 8 <= text.length()) {
                    i += 8;
                    continue;
                }
                i += 2;
                continue;
            }
            ++count;
            ++i;
        }
        return count;
    }

    private static String visibleSubstring(String text, int visibleCount) {
        StringBuilder result = new StringBuilder();
        int visible = 0;
        int i = 0;
        boolean inGradient = false;
        String gradientCloseTag = null;
        while (i < text.length() && visible < visibleCount) {
            int close;
            if (text.charAt(i) == '<' && (text.startsWith("<g:", i) || text.startsWith("<gradient:", i)) && (close = text.indexOf(62, i)) != -1) {
                result.append(text, i, close + 1);
                inGradient = true;
                gradientCloseTag = text.contains("</gradient>") ? "</gradient>" : "</g>";
                i = close + 1;
                continue;
            }
            if (text.charAt(i) == '<' && (text.startsWith("</g>", i) || text.startsWith("</gradient>", i))) {
                String closeTag = text.startsWith("</g>", i) ? "</g>" : "</gradient>";
                result.append(closeTag);
                i += closeTag.length();
                inGradient = false;
                gradientCloseTag = null;
                continue;
            }
            if (text.charAt(i) == '<' && text.startsWith("<#", i) && (close = text.indexOf(62, i)) != -1) {
                result.append(text, i, close + 1);
                i = close + 1;
                continue;
            }
            if (text.charAt(i) == '&' && i + 1 < text.length()) {
                if (text.charAt(i + 1) == '#' && i + 8 <= text.length()) {
                    result.append(text, i, i + 8);
                    i += 8;
                    continue;
                }
                result.append(text, i, i + 2);
                i += 2;
                continue;
            }
            result.append(text.charAt(i));
            ++visible;
            ++i;
        }
        if (inGradient && gradientCloseTag != null) {
            result.append(gradientCloseTag);
        }
        return result.toString();
    }
}

