/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.zombie_striker.qav;

import java.io.File;
import java.io.IOException;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.util.HotbarMessager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MessagesConfig {
    public static String MENU_OVERVIEW_TITLE = "&6%cartype%:&f Overview";
    public static String MENU_ADD_ALLOWED_TITLE = "&6%cartype%:&f Add To Whitelist";
    public static String MENU_REMOVE_ALLOWED_TITLE = "&6%cartype%:&f Remove From Whitelist";
    public static String MENU_PASSAGER_SEATS_TITLE = "&6%cartype%:&f Empty Seats";
    public static String MENU_FUELTANK_TITLE = "&6%cartype%:&f Check Fuel";
    public static String MENU_SHOP_TITLE = "&6Vehicle shop";
    public static String MENU_GARAGE_TITLE = "&6Your vehicles";
    public static String MENU_OTHER_GARAGE_TITLE = "&6%s's vehicles";
    public static String ICON_ADD_WHITELIST = "&7Add player to whitelist";
    public static String ICON_REMOVE_WHITELIST = "&7Remove player from whitelist";
    public static String ICON_CHECK_FUEL = "&7Check Fueltank";
    public static String ICON_PICKUP = "&7Pickup Vehicle";
    public static String ICON_TRUNK = "&7Open Trunk";
    public static String ICON_HEALTH = "&7Vehicle's Health:";
    public static String ICON_OWNERSHIP = "&7Remove Ownership";
    public static String ICON_ISPUBLIC = "&7Open to the public [%public%&7]";
    public static String ICON_PASSAGERS = "&7Enter a seat";
    public static String ICON_PASSAGERS_FULL = "&7Seat taken by &6&n%name%";
    public static String ICON_PASSAGERS_EMPTY = "&aEmpty";
    public static String ICONLORE_LIST_WHITELIST = "&7Currently whitelisted:";
    public static String ICONLORE_PUBLIC = "&7Public vehicles allow all players to ride as passengers.";
    public static String ICONLORE_PICKUP_OWNER = "&7Only available for owner";
    public static String ICONLORE_PICKUP_TRUNK = "&7All items in trunk will be given to the player or dropped";
    public static String ICONLORE_LIST_FUEL = "&fFuel In tank: ";
    public static String ICONLORE_TRUNK_CONTAINS = "&fContains:";
    public static String ICONLORE_HEALTH_FORMAT = "&c%health% &7/ &c%maxhealth%";
    public static String ICONLORE_COST = "&7Cost: ";
    public static String ICONLORE_currentowner = "&7Current owner: &6&n%owner%";
    public static String ICONLORE_PASSAGERS_DRIVERSEAT = "&aDriver Seat";
    public static String MESSAGE_ADD_PLAYER_WHITELIST = " Added player &6&n%name%&7 to whitelist";
    public static String MESSAGE_REMOVE_PLAYER_WHITELIST = " Removed player &6&n%name%&7 from whitelist";
    public static String MESSAGE_PICKUP_DROPPED = " There were too many items in the trunk. Some items have been dropped to the floor.";
    public static String MESSAGE_BLACKLIST_WORLD = "&c You are not allowed to place vehicles in this world.";
    public static String MESSAGE_BLACKLIST_PLACE = "&c You are not allowed to place vehicles in this place.";
    public static String MESSAGE_NOW_OWN_CAR = " You are now the owner of this &6&n%car%";
    public static String MESSAGE_BOUGHT_CAR = " You have bought &6%car%&7 for &6$%price%";
    public static String MESSAGE_NOT_ENOUGH_MONEY = "&c You do not have enough money!";
    public static String MESSAGE_TOO_MANY_VEHICLES = "&c You have too many vehicles spawned! Pick up some of your vehicles to use this one.";
    public static String MESSAGE_TOO_MANY_VEHICLES_Type = "&c You have spawned too many vehicles!";
    public static String MESSAGE_NO_PERM_DRIVE = "&c You do not have permission to drive this vehicle.";
    public static String MESSAGE_NO_OWNER_NOW = " This vehicle is now public. Anyone can drive it or pick it up.";
    public static String MESSAGE_CannotPickupWhileInVehicle = "&c You cannot pickup vehicles that are being driven.";
    public static String MESSAGE_HOTBAR_OUTOFFUEL = " Your vehicle is out of fuel. Find coal and Shift-click the car";
    public static String MESSAGE_REPAIR = " Your vehicle has been repaired successfully.";
    public static String MESSAGE_ACTIOBAR_MOVE = "&6Vehicle: &f%type% | &6Fuel: &f%fuel% | &6Speed: &f%speed%km/h";
    public static String subcommand_GiveVehicle = " <car> <?:player>: Gives you or another player a car";
    public static String subcommand_SpawnVehicle = " <car> : Spawns a car at your location";
    public static String subcommand_RemoveVehicle = " <car> : Removes all vehicles of a type";
    public static String subcommand_list = " : Get loaded vehicles list";
    public static String subcommand_removeNearbyVehicles = " <distance> : Removes all cars nearby";
    public static String subcommand_setAsPass = " <id> : Sets the player as a passenger for the closest car. (Seats start at 0)";
    public static String subcommand_Shop = " : Opens the vehicle shop";
    public static String subcommand_garage = " : Opens the player's garage";
    public static String subcommand_callbackAll = " <radius> : Callback all vehicles you own in the world";
    public static String subcommand_callback = " <radius> : Callback all vehicles you own within a radius";
    public static String subcommand_addToWhitelist = " <player>: Adds the player to the vehicle's whitelist";
    public static String subcommand_removeFromWhitelist = " <player>: Removes the player to the vehicle's whitelist";
    public static String subcommand_registerfuel = " <ticks of fuel>: Registers the item in the player's main hand as a fuel";
    public static String subcommand_debug = " : ADMIN ONLY: Starts the debug messages.";
    private static File messagesymlfile = new File(QualityArmoryVehicles.getPlugin().getDataFolder(), "messages.yml");
    private static FileConfiguration messagesyml;
    private static boolean forceUpdate;
    public static String COMMANDMESSAGES_RELOAD;
    public static String COMMANDMESSAGES_NO_PERM;
    public static String COMMANDMESSAGES_ONLY_PLAYERs;
    public static String COMMANDMESSAGES_VALID_VEHICLE;
    public static String COMMANDMESSAGES_REMOVE_BUGGED;
    public static String COMMANDMESSAGE_CALLBACKALL;
    public static String COMMANDMESSAGE_CALLBACK;
    public static String COMMANDMESSAGES_TEXTURE;
    public static String COMMANDMESSAGES_NO_VEHICLE;
    public static String COMMANDMESSAGES_WHITELIST_OVERRIDE;
    public static String COOLDOWN;
    public static String NEXT_PAGE;
    public static String PREV_PAGE;
    public static String RESOURCEPACK_TITLE;
    public static String RESOURCEPACK_SUBTITLE;
    public static String RESOURCEPACK_CRASH;

    public static void init() {
        messagesyml = YamlConfiguration.loadConfiguration((File)messagesymlfile);
        subcommand_GiveVehicle = MessagesConfig.a("Commands.givevehicle", subcommand_GiveVehicle);
        subcommand_SpawnVehicle = MessagesConfig.a("Commands.spawnvehicle", subcommand_SpawnVehicle);
        subcommand_removeNearbyVehicles = MessagesConfig.a("Commands.removenearby", subcommand_removeNearbyVehicles);
        subcommand_RemoveVehicle = MessagesConfig.a("Commands.removeVehicle", subcommand_RemoveVehicle);
        subcommand_setAsPass = MessagesConfig.a("Commands.setAsPassagers", subcommand_setAsPass);
        subcommand_Shop = MessagesConfig.a("Commands.Shop", subcommand_Shop);
        subcommand_garage = MessagesConfig.a("Commands.garage", subcommand_garage);
        subcommand_callbackAll = MessagesConfig.a("Commands.callbackAll", subcommand_callbackAll);
        subcommand_callback = MessagesConfig.a("Commands.callback", subcommand_callback);
        subcommand_addToWhitelist = MessagesConfig.a("Commands.addToWhitelist", subcommand_addToWhitelist);
        subcommand_removeFromWhitelist = MessagesConfig.a("Commands.removeFromWhitelist", subcommand_removeFromWhitelist);
        subcommand_registerfuel = MessagesConfig.a("Commands.RegisterFuels", subcommand_registerfuel);
        subcommand_debug = MessagesConfig.a("Commands.debug", subcommand_debug);
        COMMANDMESSAGES_TEXTURE = MessagesConfig.a("Commands.TexturePack", COMMANDMESSAGES_TEXTURE);
        COMMANDMESSAGES_NO_VEHICLE = MessagesConfig.a("Commands.NoVehicle", COMMANDMESSAGES_NO_VEHICLE);
        COMMANDMESSAGES_WHITELIST_OVERRIDE = MessagesConfig.a("Commands.WhitelistOverride", COMMANDMESSAGES_WHITELIST_OVERRIDE);
        MENU_OVERVIEW_TITLE = MessagesConfig.a("Menu.Overview.Title", MENU_OVERVIEW_TITLE);
        MENU_ADD_ALLOWED_TITLE = MessagesConfig.a("Menu.Add_Whitelist.Title", MENU_ADD_ALLOWED_TITLE);
        MENU_REMOVE_ALLOWED_TITLE = MessagesConfig.a("Menu.Remove_Whitelist.Title", MENU_REMOVE_ALLOWED_TITLE);
        MENU_FUELTANK_TITLE = MessagesConfig.a("Menu.Check_FuelTank.Title", MENU_FUELTANK_TITLE);
        MENU_SHOP_TITLE = MessagesConfig.a("Menu.Shop.Title", MENU_SHOP_TITLE);
        MENU_GARAGE_TITLE = MessagesConfig.a("Menu.Garage.Title", MENU_GARAGE_TITLE);
        MENU_OTHER_GARAGE_TITLE = MessagesConfig.a("Menu.Garage.TitleOther", MENU_OTHER_GARAGE_TITLE);
        MENU_PASSAGER_SEATS_TITLE = MessagesConfig.a("Menu.setAsPassager.Title", MENU_PASSAGER_SEATS_TITLE);
        ICON_ADD_WHITELIST = MessagesConfig.a("Icon.Add_Whitelist.Title", ICON_ADD_WHITELIST);
        ICON_REMOVE_WHITELIST = MessagesConfig.a("Icon.Remove_Whitelist.Title", ICON_REMOVE_WHITELIST);
        ICON_CHECK_FUEL = MessagesConfig.a("Icon.CheckFueltank.Title", ICON_CHECK_FUEL);
        ICON_ISPUBLIC = MessagesConfig.a("Icon.Public_Status.Title", ICON_ISPUBLIC);
        ICON_TRUNK = MessagesConfig.a("Icon.Trunk.Title", ICON_TRUNK);
        ICON_PICKUP = MessagesConfig.a("Icon.Pickup.Title", ICON_PICKUP);
        ICON_HEALTH = MessagesConfig.a("Icon.Health.Title", ICON_HEALTH);
        ICON_OWNERSHIP = MessagesConfig.a("Icon.Remove_Ownership.Title", ICON_OWNERSHIP);
        ICON_PASSAGERS = MessagesConfig.a("Icon.SetAsPassager.Title", ICON_PASSAGERS);
        ICON_PASSAGERS_EMPTY = MessagesConfig.a("Icon.PASSAGER.EMPTY.Title", ICON_PASSAGERS_EMPTY);
        ICON_PASSAGERS_FULL = MessagesConfig.a("Icon.PASSAGER.TAKEN.Title", ICON_PASSAGERS_FULL);
        ICONLORE_LIST_FUEL = MessagesConfig.a("Icon.Check_FuelTank.Lore", ICONLORE_LIST_FUEL);
        ICONLORE_currentowner = MessagesConfig.a("Icon.Remove_Ownership.Lore", ICONLORE_currentowner);
        ICONLORE_LIST_WHITELIST = MessagesConfig.a("Icon.Add_Whitelist.Lore", ICONLORE_LIST_WHITELIST);
        ICONLORE_PICKUP_OWNER = MessagesConfig.a("Icon.Pickup.Lore_Owner", ICONLORE_PICKUP_OWNER);
        ICONLORE_PICKUP_TRUNK = MessagesConfig.a("Icon.Pickup.Lore_Trunk", ICONLORE_PICKUP_TRUNK);
        ICONLORE_TRUNK_CONTAINS = MessagesConfig.a("Icon.Trunk.Lore_Contains", ICONLORE_TRUNK_CONTAINS);
        ICONLORE_HEALTH_FORMAT = MessagesConfig.a("Icon.Health.Lore_Format", ICONLORE_HEALTH_FORMAT);
        ICONLORE_PUBLIC = MessagesConfig.a("Icon.Public_Status.Lore_Format", ICONLORE_PUBLIC);
        ICONLORE_PASSAGERS_DRIVERSEAT = MessagesConfig.a("Icon.PASSAGER.DRIVERSEAT.Lore_Format", ICONLORE_PASSAGERS_DRIVERSEAT);
        ICONLORE_COST = MessagesConfig.a("Icon.Shop.Cost", ICONLORE_COST);
        NEXT_PAGE = MessagesConfig.a("Icon.Next", NEXT_PAGE);
        PREV_PAGE = MessagesConfig.a("Icon.Previous", PREV_PAGE);
        MESSAGE_ADD_PLAYER_WHITELIST = MessagesConfig.a("Messages.addplayertowhitelist", MESSAGE_ADD_PLAYER_WHITELIST);
        MESSAGE_REMOVE_PLAYER_WHITELIST = MessagesConfig.a("Messages.removeplayerfromwhitelist", MESSAGE_REMOVE_PLAYER_WHITELIST);
        MESSAGE_PICKUP_DROPPED = MessagesConfig.a("Messages.pickup.dropped", MESSAGE_PICKUP_DROPPED);
        MESSAGE_HOTBAR_OUTOFFUEL = MessagesConfig.a("MessagesHotbar.OutOfFuel", MESSAGE_HOTBAR_OUTOFFUEL);
        MESSAGE_NOT_ENOUGH_MONEY = MessagesConfig.a("Messages.Not_enough_money", MESSAGE_NOT_ENOUGH_MONEY);
        MESSAGE_BOUGHT_CAR = MessagesConfig.a("MessagesHotbar.Bought_car", MESSAGE_BOUGHT_CAR);
        MESSAGE_NO_OWNER_NOW = MessagesConfig.a("Messages.Ownership_Removed", MESSAGE_NO_OWNER_NOW);
        MESSAGE_CannotPickupWhileInVehicle = MessagesConfig.a("Messages.Cannot_Pickup_Vehicle_While_Driving", MESSAGE_CannotPickupWhileInVehicle);
        MESSAGE_NOW_OWN_CAR = MessagesConfig.a("Messages.NewOwnerMessage", MESSAGE_NOW_OWN_CAR);
        MESSAGE_TOO_MANY_VEHICLES = MessagesConfig.a("Messages.ToManyVehicles", MESSAGE_TOO_MANY_VEHICLES);
        MESSAGE_TOO_MANY_VEHICLES_Type = MessagesConfig.a("Messages.ToManyVehiclesType", MESSAGE_TOO_MANY_VEHICLES_Type);
        MESSAGE_BLACKLIST_WORLD = MessagesConfig.a("Messages.BlacklistedWorld_StopPlace", MESSAGE_BLACKLIST_WORLD);
        MESSAGE_BLACKLIST_PLACE = MessagesConfig.a("Messages.BlacklistedPlace_StopPlace", MESSAGE_BLACKLIST_PLACE);
        MESSAGE_NO_PERM_DRIVE = MessagesConfig.a("Messages.NoPermissionToDrive", MESSAGE_NO_PERM_DRIVE);
        COMMANDMESSAGES_NO_PERM = MessagesConfig.a("Commands.NoPermission", COMMANDMESSAGES_NO_PERM);
        COMMANDMESSAGES_ONLY_PLAYERs = MessagesConfig.a("Commands.OnlyPlayers", COMMANDMESSAGES_ONLY_PLAYERs);
        COMMANDMESSAGES_REMOVE_BUGGED = MessagesConfig.a("Messages.removeBugged", COMMANDMESSAGES_REMOVE_BUGGED);
        COMMANDMESSAGE_CALLBACKALL = MessagesConfig.a("Messages.callbackAll", COMMANDMESSAGE_CALLBACKALL);
        COMMANDMESSAGE_CALLBACK = MessagesConfig.a("Messages.callback", COMMANDMESSAGE_CALLBACK);
        MESSAGE_REPAIR = MessagesConfig.a("Messages.repair", MESSAGE_REPAIR);
        MESSAGE_ACTIOBAR_MOVE = MessagesConfig.a("Messages.actionBar", MESSAGE_ACTIOBAR_MOVE);
        COMMANDMESSAGES_RELOAD = MessagesConfig.a("Messages.reload", COMMANDMESSAGES_RELOAD);
        COOLDOWN = MessagesConfig.a("Messages.cooldown", COOLDOWN);
        RESOURCEPACK_TITLE = MessagesConfig.a("Messages.Resourcepack.Title", RESOURCEPACK_TITLE);
        RESOURCEPACK_SUBTITLE = MessagesConfig.a("Messages.Resourcepack.Subtitle", RESOURCEPACK_SUBTITLE);
        RESOURCEPACK_CRASH = MessagesConfig.a("Messages.Resourcepack.Crash", RESOURCEPACK_CRASH);
        MessagesConfig.b();
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)string);
    }

    public static String translatePublic(VehicleEntity vehicleEntity) {
        return ICON_ISPUBLIC.replaceAll("%public%", (vehicleEntity.allowsPassagers ? ChatColor.GREEN + "" : ChatColor.DARK_RED + "") + vehicleEntity.allowsPassagers);
    }

    public static String translateLocked(VehicleEntity vehicleEntity) {
        return ICON_ISPUBLIC.replaceAll("%locked%", (vehicleEntity.allowsPassagers ? ChatColor.GREEN + "" : ChatColor.DARK_RED + "") + vehicleEntity.allowsPassagers);
    }

    public static void sendOutOfFuel(final Player player) {
        new BukkitRunnable(){

            public void run() {
                if (!Main.useChatForMessage) {
                    try {
                        HotbarMessager.sendHotBarMessage(player, MESSAGE_HOTBAR_OUTOFFUEL);
                    } catch (Error | Exception throwable) {}
                } else {
                    player.sendMessage(Main.prefix + MESSAGE_HOTBAR_OUTOFFUEL);
                }
            }
        }.runTaskLater((Plugin)QualityArmoryVehicles.getPlugin(), 0L);
    }

    private static String a(String string, String string2) {
        if (messagesyml.contains(string)) {
            return MessagesConfig.colorize(messagesyml.getString(string));
        }
        messagesyml.set(string, (Object)string2);
        forceUpdate = true;
        return MessagesConfig.colorize(string2);
    }

    private static void b() {
        if (forceUpdate) {
            try {
                messagesyml.save(messagesymlfile);
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    static {
        forceUpdate = false;
        COMMANDMESSAGES_RELOAD = " Reloaded config and vehicle files in &6&n%time%ms";
        COMMANDMESSAGES_NO_PERM = "&c You do not have permission to use this command.";
        COMMANDMESSAGES_ONLY_PLAYERs = "&c Only players can use this command.";
        COMMANDMESSAGES_VALID_VEHICLE = "&c The name provided is not of a registered vehicle.";
        COMMANDMESSAGES_REMOVE_BUGGED = " Removed all bugged vehicles from your world.";
        COMMANDMESSAGE_CALLBACKALL = " &7All &6&n%count%&7 vehicles have been returned to their owners.";
        COMMANDMESSAGE_CALLBACK = " Called back all vehicles within a &6&n%radius%&7 radius of the player.";
        COMMANDMESSAGES_TEXTURE = " Click here to download the resource pack";
        COMMANDMESSAGES_NO_VEHICLE = "&c You have to ride a vehicle to perform this command.";
        COMMANDMESSAGES_WHITELIST_OVERRIDE = " You have toggled whitelist override.";
        COOLDOWN = " You have to wait &6&n%time%ms&7 before performing this action again.";
        NEXT_PAGE = "&aNext Page";
        PREV_PAGE = "&cPrevious Page";
        RESOURCEPACK_TITLE = "&a&lDownloading Resourcepack...";
        RESOURCEPACK_SUBTITLE = "&fAccept the resourcepack to see the custom items";
        RESOURCEPACK_CRASH = " In case the resourcepack crashes your client, reject the request and use &6&n/qa getResourcepack&7 to get the resourcepack.";
    }
}

