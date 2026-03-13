/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.upgrades.menu;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.upgrades.MenuContent;
import com.andrei1058.bedwars.upgrades.UpgradesManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuSeparator
implements MenuContent {
    private ItemStack displayItem;
    private String name;
    private List<String> playerCommands = new ArrayList<String>();
    private List<String> consoleCommands = new ArrayList<String>();

    public MenuSeparator(String name, ItemStack displayItem) {
        if (name == null) {
            return;
        }
        this.displayItem = BedWars.nms.addCustomData(displayItem, "MCONT_" + name);
        this.name = name;
        Language.saveIfNotExists(Messages.UPGRADES_SEPARATOR_ITEM_NAME_PATH + name.replace("separator-", ""), "&cName not set");
        Language.saveIfNotExists(Messages.UPGRADES_SEPARATOR_ITEM_LORE_PATH + name.replace("separator-", ""), Collections.singletonList("&cLore not set"));
        if (UpgradesManager.getConfiguration().getYml().getStringList(name + ".on-click.player") != null) {
            this.playerCommands.addAll(UpgradesManager.getConfiguration().getYml().getStringList(name + ".on-click.player"));
        }
        if (UpgradesManager.getConfiguration().getYml().getStringList(name + ".on-click.console") != null) {
            this.consoleCommands.addAll(UpgradesManager.getConfiguration().getYml().getStringList(name + ".on-click.console"));
        }
    }

    @Override
    public ItemStack getDisplayItem(Player player, ITeam team) {
        ItemStack i = new ItemStack(this.displayItem);
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.setDisplayName(Language.getMsg(player, Messages.UPGRADES_SEPARATOR_ITEM_NAME_PATH + this.name.replace("separator-", "")));
            im.setLore(Language.getList(player, Messages.UPGRADES_SEPARATOR_ITEM_LORE_PATH + this.name.replace("separator-", "")));
            im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            i.setItemMeta(im);
        }
        return i;
    }

    @Override
    public void onClick(Player player, ClickType clickType, ITeam team) {
        for (String cmd : this.playerCommands) {
            if (cmd.trim().isEmpty()) continue;
            Bukkit.dispatchCommand((CommandSender)player, (String)cmd.replace("{playername}", player.getName()).replace("{player}", player.getDisplayName()).replace("{team}", team == null ? "null" : team.getDisplayName(Language.getPlayerLanguage(player))));
        }
        for (String cmd : this.consoleCommands) {
            if (cmd.trim().isEmpty()) continue;
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)cmd.replace("{playername}", player.getName()).replace("{player}", player.getDisplayName()).replace("{team}", team == null ? "null" : team.getDisplayName(Language.getPlayerLanguage(player))));
        }
    }

    @Override
    public String getName() {
        return this.name;
    }
}

