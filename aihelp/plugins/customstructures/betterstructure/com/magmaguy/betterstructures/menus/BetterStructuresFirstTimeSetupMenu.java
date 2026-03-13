/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.betterstructures.menus;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.commands.ReloadCommand;
import com.magmaguy.betterstructures.config.DefaultConfig;
import com.magmaguy.betterstructures.config.schematics.SchematicConfig;
import com.magmaguy.magmacore.menus.FirstTimeSetupMenu;
import com.magmaguy.magmacore.menus.MenuButton;
import com.magmaguy.magmacore.util.ItemStackGenerator;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.SpigotMessage;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterStructuresFirstTimeSetupMenu {
    public static void createMenu(Player player) {
        new FirstTimeSetupMenu((JavaPlugin)MetadataHandler.PLUGIN, player, "&2BetterStructures", "&6Nightbreak-powered content setup", BetterStructuresFirstTimeSetupMenu.createInfoItem(), List.of((Object)BetterStructuresFirstTimeSetupMenu.createRecommendedItem(), (Object)BetterStructuresFirstTimeSetupMenu.createManualItem(), (Object)BetterStructuresFirstTimeSetupMenu.createSkipItem()));
    }

    private static MenuButton createInfoItem() {
        return new MenuButton(ItemStackGenerator.generateSkullItemStack("magmaguy", "&2Welcome to BetterStructures!", List.of((Object)"&7Link your Nightbreak account,", (Object)"&7download content in-game,", (Object)"&7and start generating structures quickly."))){

            @Override
            public void onClick(Player player) {
                player.closeInventory();
                Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
                BetterStructuresFirstTimeSetupMenu.sendLink(player, "&2Setup guide: ", "&9&nhttps://nightbreak.io/plugin/betterstructures/#setup", "&7Click to open the BetterStructures setup guide.", "https://nightbreak.io/plugin/betterstructures/#setup");
                BetterStructuresFirstTimeSetupMenu.sendLink(player, "&2Nightbreak account: ", "&9&nhttps://nightbreak.io/account/", "&7Click to open your Nightbreak account page.", "https://nightbreak.io/account/");
                BetterStructuresFirstTimeSetupMenu.sendCommand(player, "&2Content browser: ", "&a/bs setup", "&7Click to open the BetterStructures setup menu.", "/bs setup");
                BetterStructuresFirstTimeSetupMenu.sendCommand(player, "&2Bulk download: ", "&a/bs downloadall", "&7Click to download all available BetterStructures content.", "/bs downloadall");
                BetterStructuresFirstTimeSetupMenu.sendLink(player, "&2Support Discord: ", "&9&nhttps://discord.gg/eSxvPbWYy4", "&7Click to open the BetterStructures support Discord.", "https://discord.gg/eSxvPbWYy4");
                Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
            }
        };
    }

    private static MenuButton createRecommendedItem() {
        return new MenuButton(ItemStackGenerator.generateItemStack(Material.GREEN_STAINED_GLASS_PANE, "&2Recommended Setup", (List<String>)List.of((Object)"&aMarks setup complete.", (Object)"&aGuides you to Nightbreak login and content install."))){

            @Override
            public void onClick(Player player) {
                player.closeInventory();
                DefaultConfig.toggleSetupDone(true);
                Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
                Logger.sendSimpleMessage((CommandSender)player, "&aBetterStructures setup is now marked as complete.");
                BetterStructuresFirstTimeSetupMenu.sendLink(player, "&7Step 1: get your Nightbreak token at ", "&9&nhttps://nightbreak.io/account/", "&7Click to open your Nightbreak account page.", "https://nightbreak.io/account/");
                BetterStructuresFirstTimeSetupMenu.sendCommand(player, "&7Step 2: link it in-game with ", "&a/nightbreaklogin <token>", "&7Click to run the Nightbreak login command.", "/nightbreaklogin ");
                player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&7Step 3: install content with "), SpigotMessage.commandHoverMessage("&a/bs downloadall", "&7Click to download all available BetterStructures content.", "/bs downloadall"), SpigotMessage.simpleMessage(" &7or browse it with "), SpigotMessage.commandHoverMessage("&a/bs setup", "&7Click to open the BetterStructures setup menu.", "/bs setup")});
                Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
            }
        };
    }

    private static MenuButton createManualItem() {
        return new MenuButton(ItemStackGenerator.generateItemStack(Material.YELLOW_STAINED_GLASS_PANE, "&6Manual Setup", (List<String>)List.of((Object)"&eMarks setup complete.", (Object)"&eLeaves content management up to you."))){

            @Override
            public void onClick(Player player) {
                player.closeInventory();
                DefaultConfig.toggleSetupDone(true);
                Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
                Logger.sendSimpleMessage((CommandSender)player, "&6Setup complete. You can manage content manually through &a/bs setup&6.");
                Logger.sendSimpleMessage((CommandSender)player, "&7If you already dropped new files into &8plugins/BetterStructures/imports&7, run &a/bs reload&7.");
                Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
            }
        };
    }

    private static MenuButton createSkipItem() {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldEdit") && !Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) {
            return new MenuButton(ItemStackGenerator.generateItemStack(Material.RED_STAINED_GLASS_PANE, "&cWorldEdit not installed", (List<String>)List.of((Object)"&cBetterStructures needs WorldEdit or FAWE installed."))){

                @Override
                public void onClick(Player player) {
                    player.closeInventory();
                    Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
                    Logger.sendSimpleMessage((CommandSender)player, "&cInstall WorldEdit or FAWE before using BetterStructures.");
                    Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
                }
            };
        }
        return new MenuButton(ItemStackGenerator.generateItemStack(Material.RED_STAINED_GLASS_PANE, "&cUse Current Content", (List<String>)List.of((Object)"&cDismisses the setup prompt and keeps your current content state."))){

            @Override
            public void onClick(Player player) {
                player.closeInventory();
                DefaultConfig.toggleSetupDone(true);
                if (SchematicConfig.getSchematicConfigurations().isEmpty()) {
                    Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
                    Logger.sendSimpleMessage((CommandSender)player, "&eSetup dismissed, but no BetterStructures content is currently installed.");
                    Logger.sendSimpleMessage((CommandSender)player, "&7Use &a/bs setup &7when you're ready to install content.");
                    Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
                } else {
                    Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
                    Logger.sendSimpleMessage((CommandSender)player, "&aSetup complete. BetterStructures will keep using your current installed content.");
                    Logger.sendSimpleMessage((CommandSender)player, "&7Run &a/bs reload &7if you import new content later.");
                    Logger.sendSimpleMessage((CommandSender)player, "&8&m-----------------------------------------------------");
                    ReloadCommand.reload((CommandSender)player);
                }
            }
        };
    }

    private static void sendLink(Player player, String prefix, String display, String hover, String url) {
        player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage(prefix), SpigotMessage.hoverLinkMessage(display, hover, url)});
    }

    private static void sendCommand(Player player, String prefix, String display, String hover, String command) {
        player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage(prefix), SpigotMessage.commandHoverMessage(display, hover, command)});
    }
}

