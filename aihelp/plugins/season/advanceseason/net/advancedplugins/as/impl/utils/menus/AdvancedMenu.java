/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 */
package net.advancedplugins.as.impl.utils.menus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.menus.AdvancedMenusHandler;
import net.advancedplugins.as.impl.utils.menus.item.AdvancedMenuItem;
import net.advancedplugins.as.impl.utils.menus.item.ClickAction;
import net.advancedplugins.as.impl.utils.menus.item.ClickActionArgs;
import net.advancedplugins.as.impl.utils.text.Replace;
import net.advancedplugins.as.impl.utils.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class AdvancedMenu
implements InventoryHolder {
    private final AdvancedMenusHandler handler = AdvancedMenusHandler.getInstance();
    private Inventory inventory;
    private final LinkedHashMap<Integer, AdvancedMenuItem> itemHashMap = new LinkedHashMap();
    private final LinkedHashMap<Integer, ClickAction> actionMap = new LinkedHashMap();
    private AdvancedMenuItem fillerItem = null;
    private final Player player;
    private Replace replace;
    private int page = 0;
    private String title;
    private final int invSize;
    private int maxPages = -1;
    private ClickAction closeAction = null;
    private final ConfigurationSection section;

    public AdvancedMenu(Player player, ConfigurationSection configurationSection, Replace replace) {
        this.player = player;
        this.title = Text.modify(Text.parsePapi(configurationSection.getString(this.handler.getPath("name")), (OfflinePlayer)player), replace);
        this.invSize = configurationSection.getInt(this.handler.getPath("size"));
        this.replace = replace;
        this.section = configurationSection;
        this.populateItemHashMap(configurationSection, this.itemHashMap, replace);
    }

    public AdvancedMenu(Player player, ConfigurationSection configurationSection, Replace replace, int n) {
        this.player = player;
        this.title = Text.modify(Text.parsePapi(configurationSection.getString(this.handler.getPath("name")), (OfflinePlayer)player), replace);
        this.invSize = configurationSection.getInt(this.handler.getPath("size"));
        this.replace = replace;
        this.section = configurationSection;
        this.maxPages = n;
        this.populateItemHashMap(configurationSection, this.itemHashMap, replace);
    }

    public AdvancedMenu(Player player, String string, int n, Replace replace) {
        this.player = player;
        this.title = Text.modify(Text.parsePapi(string, (OfflinePlayer)player), replace);
        this.invSize = n;
        this.replace = replace;
        this.section = null;
    }

    public void refreshItems() {
        this.itemHashMap.clear();
        this.populateItemHashMap(this.section, this.itemHashMap, this.replace);
    }

    public void openInventory() {
        this.openInventory(null);
    }

    public void openInventory(Integer n) {
        this.inventory = Bukkit.createInventory((InventoryHolder)this, (int)this.invSize, (String)this.title);
        if (n != null) {
            n = Math.max(0, n);
            this.page = n;
            if (this.maxPages != -1) {
                n = Math.min(n, this.maxPages - 1);
            }
        }
        this.itemHashMap.values().forEach(advancedMenuItem -> {
            try {
                advancedMenuItem.addToInventory(this.inventory);
            } catch (Exception exception) {
                ASManager.log("[AdvancedMenu] Error adding item to inventory: [" + advancedMenuItem.getSlots() + "] " + String.valueOf(advancedMenuItem.getItem()));
                exception.printStackTrace();
            }
        });
        if (this.fillerItem != null) {
            ASManager.fillEmptyInventorySlots(this.inventory, this.fillerItem.getItem());
        }
        this.player.openInventory(this.inventory);
    }

    protected void onClick(Player player, int n, ClickType clickType) {
        AdvancedMenuItem advancedMenuItem = this.itemHashMap.get(n);
        if (advancedMenuItem == null) {
            return;
        }
        if (advancedMenuItem.getAction() == null) {
            ClickAction clickAction = this.actionMap.get(n);
            if (clickAction == null) {
                return;
            }
            clickAction.onClick(player, this, advancedMenuItem, n, clickType);
            return;
        }
        String string = advancedMenuItem.getAction();
        if (string.contains(":")) {
            String[] stringArray = string.split(":");
            ClickActionArgs clickActionArgs = (ClickActionArgs)this.handler.getDefaultActions().get(stringArray[0]);
            if (clickActionArgs == null) {
                return;
            }
            clickActionArgs.onClick(player, this, advancedMenuItem, n, clickType, stringArray[1]);
            return;
        }
        ClickAction clickAction = this.handler.getDefaultActions().get(string);
        if (clickAction == null) {
            return;
        }
        clickAction.onClick(player, this, advancedMenuItem, n, clickType);
    }

    protected void onClose(Player player) {
        if (this.closeAction == null) {
            return;
        }
        this.closeAction.onClick(player, this, null, 0, null);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    private void populateItemHashMap(ConfigurationSection configurationSection, HashMap<Integer, AdvancedMenuItem> hashMap, Replace replace) {
        String string = this.handler.getPath("items");
        if (!configurationSection.isConfigurationSection("items")) {
            return;
        }
        ConfigurationSection configurationSection2 = configurationSection.getConfigurationSection(string);
        for (String string2 : configurationSection2.getKeys(false)) {
            this.processItemKey(string2, configurationSection2, hashMap, replace);
        }
    }

    private void processItemKey(String string, ConfigurationSection configurationSection, HashMap<Integer, AdvancedMenuItem> hashMap, Replace replace) {
        this.processItemKey(string, configurationSection.getCurrentPath() + "." + string, configurationSection, hashMap, replace);
    }

    public void processItem(String string, ConfigurationSection configurationSection, Replace replace) {
        this.processItemKey(string, configurationSection.getCurrentPath() + "." + string, configurationSection, this.itemHashMap, replace);
    }

    public void processItem(String string, ConfigurationSection configurationSection) {
        this.processItemKey(string, configurationSection.getCurrentPath() + "." + string, configurationSection, this.itemHashMap, this.replace);
    }

    private void processItemKey(String string, String string2, ConfigurationSection configurationSection, HashMap<Integer, AdvancedMenuItem> hashMap, Replace replace) {
        ConfigurationSection configurationSection2 = configurationSection.getConfigurationSection(string);
        if (string.equalsIgnoreCase("filler")) {
            this.fillerItem = new AdvancedMenuItem(string, configurationSection2, replace);
            return;
        }
        for (int n : ASManager.getSlots(string)) {
            assert (configurationSection2 != null);
            hashMap.put(n, new AdvancedMenuItem(string, configurationSection2, replace));
        }
    }

    public AdvancedMenu addItem(AdvancedMenuItem advancedMenuItem, int ... nArray) {
        if (advancedMenuItem.getSlots().equalsIgnoreCase("null")) {
            advancedMenuItem.setSlots(nArray);
        }
        for (int n : nArray) {
            this.itemHashMap.put(n, advancedMenuItem);
        }
        return this;
    }

    public AdvancedMenu addAction(ClickAction clickAction, int ... nArray) {
        for (int n : nArray) {
            this.actionMap.put(n, clickAction);
        }
        return this;
    }

    public AdvancedMenu addCloseAction(ClickAction clickAction) {
        this.closeAction = clickAction;
        return this;
    }

    public void setFillerItem(AdvancedMenuItem advancedMenuItem) {
        this.fillerItem = advancedMenuItem;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Replace getReplace() {
        return this.replace;
    }

    public void setReplace(Replace replace) {
        this.replace = replace;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int n) {
        this.page = n;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String string) {
        this.title = string;
    }

    public int getInvSize() {
        return this.invSize;
    }

    public void setMaxPages(int n) {
        this.maxPages = n;
    }

    public int getMaxPages() {
        return this.maxPages;
    }
}

