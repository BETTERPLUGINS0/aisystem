/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.menus;

import java.util.HashMap;
import net.advancedplugins.as.impl.utils.PlayASound;
import net.advancedplugins.as.impl.utils.menus.AdvancedMenuClick;
import net.advancedplugins.as.impl.utils.menus.item.ClickAction;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedMenusHandler {
    private final HashMap<String, String> paths = new HashMap();
    private static AdvancedMenusHandler instance = null;
    private final HashMap<String, ClickAction> defaultActions = new HashMap();
    private final AdvancedMenuClick clickHandler = new AdvancedMenuClick();

    public AdvancedMenusHandler(JavaPlugin javaPlugin) {
        instance = this;
        this.paths.put("name", "name");
        this.paths.put("size", "size");
        this.paths.put("items", "items");
        this.loadDefaultActions();
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this.clickHandler, (Plugin)javaPlugin);
    }

    private void loadDefaultActions() {
        this.defaultActions.put("CLOSE", (player, advancedMenu, advancedMenuItem, n, clickType) -> player.closeInventory());
        this.defaultActions.put("PREVIOUS_PAGE", (player, advancedMenu, advancedMenuItem, n, clickType) -> {
            advancedMenu.openInventory(advancedMenu.getPage() - 1);
            PlayASound.playSound("ENTITY_EXPERIENCE_ORB_PICKUP", player);
        });
        this.defaultActions.put("NEXT_PAGE", (player, advancedMenu, advancedMenuItem, n, clickType) -> {
            advancedMenu.openInventory(advancedMenu.getPage() + 1);
            PlayASound.playSound("ENTITY_EXPERIENCE_ORB_PICKUP", player);
        });
    }

    public String getPath(String string) {
        return this.paths.getOrDefault(string, string);
    }

    public void unload() {
        HandlerList.unregisterAll((Listener)this.clickHandler);
    }

    public static AdvancedMenusHandler getInstance() {
        return instance;
    }

    public HashMap<String, ClickAction> getDefaultActions() {
        return this.defaultActions;
    }
}

