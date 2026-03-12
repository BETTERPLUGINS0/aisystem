package me.gypopo.economyshopgui.files;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.lang.TranslatableComponent;
import me.gypopo.economyshopgui.files.lang.TranslatableRaw;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.ConfigUtil;
import me.gypopo.economyshopgui.util.meta.BukkitMeta;
import org.bukkit.configuration.file.FileConfiguration;

public enum Lang {
   UPDATING_SHOP_SETTINGS("Updating Shop settings..."),
   CREATING_LATEST_SHOPS("Creating default shops for the latest version because there was no existing layout found matching server version %version%, please use a supported server version."),
   DEFAULT_SHOPS_CREATED("Default shops layout compatible with %version% created at %path%"),
   LOADED_SHOP_CONFIGURATIONS("Completed loading %total% shop configs from /shops/"),
   DEFAULT_SECTIONS_CREATED("Default sections layout created at %path%"),
   LOADED_SECTION_CONFIGURATIONS("Completed loading %total% section configs from /sections/"),
   CREATING_NEW_SHOP_CONFIG("Creating a new shops config for '%section%' because no shops config was found while there is a sections config. Not removed correctly?"),
   CREATING_NEW_SECTION_CONFIG("Creating a new sections config for '%section%' because no sections config was found while there is a shops config. Not removed correctly?"),
   DISABLED_DUE_NO_VAULT("Disabled due to no Vault dependency found, please install Vault."),
   DISABLED_DUE_NO_ECONOMY("Disabled due to no economy plugin was found, please install a plugin such as EssentialsX!"),
   DISCOUNTS_FEATURE_UNAVAILABLE("Discounts feature cannot be enabled because no permissions plugin was found."),
   UPDATE_AVAILABLE("There is an update available for EconomyShopGUI, you are running %plugin_version% but found %latest_version%."),
   DONE("Done - Took %millis%ms to complete"),
   UPDATING_SECTIONS_CONFIG("Updating sections config..."),
   DEBUG_MODE_ENABLED("Debug mode is enabled."),
   ECONOMY_PROVIDER_NULL("EconomyProvider is null."),
   LANGUAGE_FILE_USING("Using %languagefile% as language file."),
   CANNOT_FIND_LANGUAGE_FILE("Can not find language file!"),
   MINECRAFT_VERSION_USING("Using minecraft version %version%..."),
   LOADING_ITEMS("Loading all items..."),
   COULD_NOT_FIND_VALID_VERSION("Could not find a valid implementation for this server version. This will cause the plugin to not have a version to use and result in lots of items/methods not working. Please use a supported minecraft version."),
   UPDATING_CONFIGS("Updating the configurations because a newer version has been found..."),
   CREATING_BACKUP("Creating a backup from the old %fileName%, the file can be found in the backups folder inside the EconomyShopGUI plugin's folder."),
   ENABLED_PLUGIN_HOOK("Enabled %plugin% hook..."),
   FAILED_PLUGIN_INTEGRATION("Failed to hook into %plugin%, plugin is disabled or not found..."),
   LOADING_SEASON_MODIFIERS("Loading season modifiers from config..."),
   LOADED_SEASON_MODIFIERS("Completed loading %total% modifier(s) for season '%season%' in world '%world%'"),
   NO_SEASON_MODIFIERS_FOUND("No season modifiers found for season %season% in config"),
   SEASON_CHANGE("Detected season change in world '%world%', loading modifiers for season %season%..."),
   ECONOMY_PROVIDERS_LOADED("Completed loading %total% economy provider(s) for all %shops% shop sections."),
   SECTION_ECONOMY_PROVIDER_LOADED("Using '%economy%' as default economy for shop '%shop%'"),
   LOADED_SHOP_STAND_MODULE("Completed loading %count% shop stand(s) from stands.json"),
   SAVING_SHOP_STANDS("Saving %count% shop stands to 'stands.json'..."),
   SHOP_PREFIX("&9&lShop &8&l>> "),
   CONSOLE_PREFIX("&8[&3EconomyShopGUI&8]&r"),
   INVENTORY_MAIN_SHOP_TITLE("&8&lServer Shop"),
   PROFILE("&aProfile"),
   NAME("&6Name"),
   MONEY("&6Money"),
   LEVEL("&6Level"),
   INVENTORY_SELLGUI_TITLE("&8&lSellGUI"),
   INVENTORY_BUYSTACKS_TITLE("&8Choose &lamount of stacks"),
   INVENTORY_HOWMUCHSELL_TITLE("&8Choose &lsell amount"),
   INVENTORY_HOWMUCHBUY_TITLE("&8Choose &lbuy amount"),
   PLUS_ONE("&b+1"),
   MIN_ONE("&5-1"),
   PLUS_SIXTEEN("&b+16"),
   MIN_SIXTEEN("&5-16"),
   PLUS_THIRTY_TWO("&b+32"),
   MIN_THIRTY_TWO("&5-32"),
   SELL("&aClick to sell"),
   SELL_ALL("&cClick to sell all"),
   BUY("&aClick to purchase"),
   BUY_STACKS("&cClick to buy stacks"),
   MAX_AMOUNT_REACHED("&cMax amount reached"),
   MIN_AMOUNT_REACHED("&cMin amount reached"),
   PREVIOUS_PAGE("&dPrevious Page"),
   NEXT_PAGE("&bNext Page"),
   CURRENT_PAGE("&7Page:"),
   SHOPSTAND_BUY_SCREEN_TITLE("&8Buying &a&l> &r&8%item%"),
   SHOPSTAND_SELL_SCREEN_TITLE("&8Selling &c&l> &r&8%item%"),
   SHOPSTAND_CLICK_TO_BUY("&aClick to buy %amount%"),
   SHOPSTAND_CLICK_TO_SELL("&aClick to sell %amount%"),
   SHOPSTAND_TOTAL_PRICE("&cTotal price: &7%price%"),
   DEFAULT_SHOP_STAND_ITEM_NAME("&eShopStand"),
   DEFAULT_SHOP_STAND_ITEM_LORE("&fPlace this item down to create\n&fa new &6ShopStand &fat the placed location\n&ffor shop item &c%item%"),
   SHOP_STANDS_MANAGEMENT_INVENTORY_TITLE("&8&lStand management"),
   SHOP_STAND_INFO_ITEM_NAME("&6&lShop stand info:"),
   SHOP_STAND_INFO_ITEM_LORE("&aStand ID: &f%id%\n&aLocation: &f%location%\n&aShop item: &f%item%\n&aStand type: &f%type%\n&aIs loaded: &f%loaded%"),
   FORCE_RELOAD_STAND_ITEM_NAME("&e&lForce reload stand"),
   FORCE_RELOAD_STAND_ITEM_LORE("&fClicking this will reload the shop stand\n&fwhich will re-create the displayitem and stand."),
   DESTROY_STAND_ITEM_NAME("&4&lDestroy shop stand"),
   DESTROY_STAND_ITEM_LORE("&eClick to destroy this shop stand"),
   RELOADED_SHOP_STAND("&aForcefully reloaded shop stand with ID %id%"),
   SHOP_STAND_BROWSE_INVENTORY_TITLE("&8&lStand list"),
   SHOP_STAND_LOCATION_FORMAT("World '%world%', x%pos_x%, y%pos_y%, z%pos_z%"),
   CREATED_NEW_SHOP_STAND("&aSuccessfully created new ShopStand for &f%item%"),
   ERROR_CREATING_NEW_SHOP_STAND("&cFailed to create a new shop stand: %reason%"),
   CANCEL("&4Cancel"),
   BACK("&4Back"),
   LEFT_CLICK_BUY("\n &a&lBuy Price\n &a■ &f%buyPrice%"),
   SEASONAL_BUY_PRICE("\n &a&lBuy Price\n &a■ &f&m%buyPrice%&r &f%seasonalBuyPrice%"),
   DISCOUNTED_BUY_PRICE("\n &a&lBuy Price\n &a■ &f&m%buyPrice%&r &f%discountedPrice%"),
   RIGHT_CLICK_SELL("\n &c&lSell Price\n &c■ &f%sellPrice%"),
   SEASONAL_SELL_PRICE("\n &c&lSell Price\n &c■ &f&m%sellPrice%&r &f%seasonalSellPrice%"),
   MULTIPLIED_SELL_PRICE("\n &c&lSell Price\n &c■ &f&m%sellPrice%&r &f%multipliedPrice%"),
   MIDDLE_CLICK_SELL_ALL("&bMiddle Click: Sell All"),
   SHIFT_RIGHT_CLICK_SELL_ALL("&bShift + Right Click: Sell All"),
   ITEM_LOCATION_IN_SHOP("&8Section: %shopsection%, Index: %itemLoc%"),
   ITEM_CANNOT_BE_BOUGHT("\n &a&lBuy Price\n &a■ &fCannot be bought"),
   ITEM_CANNOT_BE_SOLD("\n &c&lSell Price\n &c■ &fCannot be sold"),
   ITEM_SEASONAL_PRICE("&e&lSeasonal price %season-icon%"),
   DECORATION_LORE("\n &e&lUsage:"),
   DECORATION_LORE_BUY("&e ╚&e&l» &fLeft click to buy"),
   DECORATION_LORE_SELL_ALL("&e ╚&e&l» &fShift + Right Click to sell all"),
   DECORATION_LORE_SELL("&e ╚&e&l» &fRight click to sell"),
   TIME_REQUIREMENT_LORE("&c&lCan only be purchased between\n&c%minTime% &land &c%maxTime% &lingame time."),
   QUEST_REQUIREMENT_LORE("&c&lRequires to complete quest &b&l%questName%"),
   REGION_REQUIREMENT_LORE("&c&lRequires to be inside region &b&l%regionName%"),
   DISCORD_SRV_PLAYER_TRANSACTION("%player_name% %bought/sold% %amount% x %items% for %price%"),
   SELL_CONFIRMATION_MULTIPLE_ITEMS("&aYou have successfully sold &1%amount% &aitems for &c%amounttopay%"),
   SELL_CONFIRMATION("&aYou have successfully sold &1%amount% x &b%material% &afor &c%amounttopay%"),
   PAY_CONFIRMATION("&aYou have successfully bought &1%amount% x &b%material% &afor &c%amounttopay%"),
   NO_ITEM_TO_BE_SOLD("&cYou do not have any of this item inside your inventory."),
   CANNOT_PURCHASE_ITEM("&cThis item cannot be purchased."),
   CANNOT_SELL_ITEM("&cThis item cannot be sold."),
   PLAYER_MADE_TRANSACTION("%playername% %bought/sold% %amount% x %material% for %amountofmoney% with the %buy/sell-method%."),
   PLAYER_MADE_TRANSACTION_MULTIPLE_ITEMS("%playername% %bought/sold% %items% for %amountofmoney% with the %buy/sell-method%."),
   TRANSACTION_FAILED_NO_SPACE("%playername% tried to make a transaction of %amount% x %material% for %amountofmoney% but the transaction failed because there was not enough space inside the inventory."),
   TIME_REQUIREMENT_NOT_MET("&cThis item can only be bought/sold between &f%minTime% &cand &f%maxTime%&c ingame time, current time is &f%progress%&c. Come back shortly!"),
   QUEST_REQUIREMENT_NOT_MET("&cThis item can only be bought/sold when you completed the quest &b&l%questName%&c, come back once you completed it"),
   REGION_REQUIREMENT_NOT_MET("&cYou may only buy/sell this item inside the region %regionName%"),
   BUY_SCREEN("buy screen"),
   SELL_SCREEN("sell screen"),
   BUYSTACKS_SCREEN("buy stacks screen"),
   SELLALL_SCREEN("sell all screen"),
   SELLALL_COMMAND("sell all command"),
   SELLGUI_SCREEN("sell gui"),
   QUICK_BUY("quick buy screen"),
   QUICK_SELL("quick sell screen"),
   SHOPSTAND_BUY_SCREEN("shopstand buy screen"),
   SHOPSTAND_SELL_SCREEN("shopstand sell screen"),
   AUTO_SELL_CHEST("SellChest"),
   BOUGHT("bought"),
   SOLD("sold"),
   TRANSACTION_SCREEN_TOTAL_PRICE("&3%price%"),
   TRANSACTION_SCREEN_TOTAL_AMOUNT("&5x %amount%"),
   ON_SCREEN_TITLE_SOLD("&a+%price%"),
   ON_SCREEN_TITLE_BOUGHT("&c-%price%"),
   CURRENCYSYMBOL("$"),
   SYMBOL_PRICING_FORMAT("%symbol%%price%"),
   NAMED_PRICING_FORMAT("%price% %currency-format%"),
   XP_CURRENCY_NAME_SINGULAR("XP point"),
   XP_CURRENCY_NAME_PLURAL("XP points"),
   COMMAND_DISABLED_IN_WORLD("&cThis command is disabled inside this world."),
   EDITSHOP_ADD_ITEM_SUBCOMMAND_DESC("&aAdd a item to the shop."),
   EDITSHOP_EDIT_ITEM_SUBCOMMAND_DESC("&aEdit an existing item in the shop."),
   EDITSHOP_REMOVE_ITEM_SUBCOMMAND_DESC("&aDelete a item from the shop."),
   EDITSHOP_ADD_HAND_ITEM_SUBCOMMAND_DESC("&aAdd a item from you hand to the shop."),
   EDITSHOP_IMPORT_SUBCOMMAND_DESC("&aImports files/settings from another location or plugin."),
   EDITSHOP_ADD_SECTION_SUBCOMMAND_DESC("&aAdd a shop section to the shop."),
   EDITSHOP_EDIT_SECTION_SUBCOMMAND_DESC("&aEdit an existing shop section."),
   EDITSHOP_REMOVE_SECTION_SUBCOMMAND_DESC("&aRemoves an existing shop section from the shop."),
   EDITSHOP_MIGRATE_SUBCOMMAND_DESC("&aAllows to migrate data from another location or plugin."),
   EDITSHOP_SHOPSTANDS_SUBCOMMAND_DESC("&aManage existing or create new shop stands"),
   EDITSHOP_SHOPSTANDS_GIVE_SUBCOMMAND_DESC("&aGives a new shop stand item to place down"),
   EDITSHOP_SHOPSTANDS_DESTROY_SUBCOMMAND_DESC("&aDestroys a shop stand at its current location from its ID"),
   EDITSHOP_SHOPSTANDS_BROWSE_SUBCOMMAND_DESC("&aOpens a GUI with all existing shop stands"),
   EDITSHOP_ADD_ITEM_SUBCOMMAND_SYNTAX("&a/editshop additem <section> <material> <buy price> <sell price> [displayname]"),
   EDITSHOP_EDIT_ITEM_SUBCOMMAND_SYNTAX("&a/editshop edititem <section> <index> <action> <key> <value>"),
   EDITSHOP_REMOVE_ITEM_SUBCOMMAND_SYNTAX("&a/editshop deleteitem <section> <index>"),
   EDITSHOP_ADD_HAND_ITEM_SUBCOMMAND_SYNTAX("&a/editshop addhanditem <section> <buy price> <sell price>"),
   EDITSHOP_IMPORT_SUBCOMMAND_SYNTAX("&a/editshop import <plugin> <file>"),
   EDITSHOP_ADD_SECTION_SUBCOMMAND_SYNTAX("&a/editshop addsection <section> <material> <displayname> <place>"),
   EDITSHOP_EDIT_SECTION_SUBCOMMAND_SYNTAX("&a/editshop editsection <section> <action> <key> <value>"),
   EDITSHOP_REMOVE_SECTION_SUBCOMMAND_SYNTAX("&a/editshop deletesection <section>"),
   EDITSHOP_MIGRATE_SUBCOMMAND_SYNTAX("&a/editshop migrate <plugin> <image> [flags]"),
   EDITSHOP_SHOPSTANDS_SUBCOMMAND_SYNTAX("&a/editshop shopstands <give/destroy> ..."),
   EDITSHOP_SHOPSTANDS_GIVE_SUBCOMMAND_SYNTAX("&a/editshop shopstands give <standType> <section> <index>"),
   EDITSHOP_SHOPSTANDS_DESTROY_SUBCOMMAND_SYNTAX("&a/eshop shopstands destroy <standID>"),
   EDITSHOP_SHOPSTANDS_BROWSE_SUBCOMMAND_SYNTAX("&a/eshop shopstands browse"),
   EDITSHOP_ADDING_ITEM("&aAdding new item to shop %shop%..."),
   EDITSHOP_ADD_ITEM_SUCCESSFUL("&aSuccessfully added the item to shop %shop% under %itemPath%."),
   EDITSHOP_ADDING_SECTION("&aAdding new section config for shop %section%..."),
   EDITSHOP_ADD_SECTION_SUCCESSFUL("&aNew section config created at %path%."),
   EDITSHOP_EDITING_ITEM("&aUpdating the item..."),
   EDITSHOP_EDITING_SECTION("&aUpdating section..."),
   EDITSHOP_EDIT_ITEM_SUCCESSFUL("&aSuccessfully updated '%itemPath%'."),
   EDITSHOP_REMOVING_ITEM("&aRemoving the item from the shop..."),
   EDITSHOP_REMOVE_ITEM_SUCCESSFUL("&aSuccessfully removed '%itemPath%' from the shop."),
   EDITSHOP_REMOVING_SECTION("&aRemoving the section from the shop..."),
   EDITSHOP_REMOVED_SECTION("&a&aSuccessfully removed '%itemPath%' from shop."),
   EDITSHOP_RELOAD_SHOP_TO_SEE_CHANGES("&aUse command /sreload to load the changes inside the shop."),
   FILE_REPLACED("&aThe old %fileName% has been replaced with the new file, the old file can be found inside the backups folder if you messed up something."),
   SHOP_STAND_ITEM_RECEIVED("&aYou were given &c%amount%x &aShopStand for shop item &f%item%"),
   SECTION_DOES_NOT_CONTAIN_ITEM("&cSection '%shopsection%' does not contain the item '%itemLoc%'."),
   NO_VALID_ACTION("&cNot a valid action '%action%'. \nAvailable actions: %actions%."),
   NO_VALID_KEY("&cNot a valid key '%key%'. \nAvailable keys: %keys%."),
   EDIT_KEY_UNAVAILABLE("&cCannot edit the '%key%' of this item as it is a action item"),
   ACTION_NOT_VALID_ON_KEY("&cCannot %action% the '%key%' key."),
   EDIT_ITEM_MATERIAL_INVALID("&cThe material from the item you are trying to edit is invalid. '%material%'"),
   KEY_ALREADY_DEFINED("&cThe '%key%' key is already defined on the item, you cannot add this twice."),
   KEY_IS_NOT_FOUND("&cThe '%key%' key cannot be removed from the item because it is not found."),
   NO_VALID_SLOT_FOR_ITEM("&cThe item could not be placed at slot '%slot%'. The slot needs to be 1 - %maxShopSize%."),
   PLUGIN_NOT_FOUND("&cCannot find a plugin specified with that name, please check if we do support this plugin."),
   FILE_NOT_FOUND("&cCannot find a file with name %fileName% inside the plugin folder of %pluginName%."),
   SLOT_ALREADY_IN_USE("&cThe slot for this section is already taken.\nAll available slots: %available-slots%"),
   SLOT_NEEDS_TO_BE_NUMBER("&cThe slot of the item in the main menu needs to be a number."),
   INVALID_SHOP_STAND_TYPE("&cInvalid shop stand type, valid values: %values%"),
   UNKNOWN_SHOP_STAND_ID("&cNo shop stand found with ID &f%id%"),
   STAND_TYPE_NOT_SUPPORTED("&cShop stand type &f%type% &cis not supported in the current minecraft version"),
   CONFIGS_RELOADED("&aAll configs reloaded!"),
   SELLALL_COMMAND_USAGES("&aCommand usage(s):"),
   SELLALL_INVENTORY_COMMAND_USAGE("&7/sellall inventory &f- &7Sells all items in your inventory"),
   SELLALL_HAND_COMMAND_USAGE("&7/sellall hand &8[amount] &f- &7Sells all items in your hand"),
   SELLALL_ITEM_COMMAND_USAGE("&7/sellall <item> &8[amount] &f- &7Sells all items in your inventory matching the given item name"),
   SHOP_ITEM_GIVEN("&aYou were given &c%amount% &ax &6%material%"),
   SHOP_ITEM_RECEIVED("&aYou received &c%amount% &ax shop item from section &f%section% &aand item index &f%itemIndex%"),
   MINIMUM_AMOUNT("&cThe minimum amount to pay a player is %minimumamount%."),
   PAYMENT_COMPLETE("&aPayment complete."),
   PLAYER_RECEIVED_MONEY("&aYou received &c%amounttogive% &afrom &2%playername%&a!"),
   NEED_PLAYER_AND_AMOUNT("&9You need to give a player and a amount of money."),
   NEED_AMOUNT("&9You need to give a amount of money."),
   PAYCOMMAND_EXAMPLE("&9/pay <player> <amount>"),
   GIVEMONEYCOMMAND_EXAMPLE("&9/givemoney <player> <amount>"),
   GIVEMONEY_COMPLETE("&aGivemoney complete."),
   CHECK_BALANCE("&aYou currently have &b%amountofmoney%&a."),
   CHECK_BALANCE_OTHER_PLAYER("&b%playername% &acurrently has &c%amountofmoney%&a."),
   DEFAULT_SPAWNER_NAME("&9&l%spawner-type% &rSpawner"),
   SPAWNER_BROKE_AND_APPLIED_TO_INVENTORY("&aYou successfully mined a &9%spawner-type%&a, the item is applied to your inventory."),
   NEED_SILK_TOUCH("&cYou need to use an silk touch pickaxe to mine this!"),
   JOIN_MESSAGE("&2Welcome, your balance is &c%balance%!"),
   LEVEL_EVENT_MESSAGE("&2[Levels] &5Here you go, &c%amount% &5has been added to your balance!"),
   DISPLAYNAME_NULL("Could not get displayname."),
   ITEM_NAME_NULL("Could not get the name of the item."),
   NEED_ITEM_MATERIAL("Can not create item with a blank material value."),
   ITEM_MATERIAL_NULL("Could not get the item material. If you think this is a bug, please report it to our discord support server which you can find at the plugin page. %material%"),
   ITEM_ENCHANTMENT_NULL("Could not get the item enchantment."),
   NEED_ITEM_ENCHANTMENT("Can not create enchantment with a blank value."),
   ITEM_ENCHANTMENT_NOT_SUPPORTED("This enchantment is not supported in this minecraft version."),
   ITEM_ENCHANTMENT_STRENGTH_NULL("Could not get the strength of the enchantment."),
   ENCHANTMENT_CANNOT_APPLY_TO_ITEM("To apply an enchantment to the item, the material needs to be enchanted book or a tool that fits the enchantment."),
   ITEM_SPAWNERTYPE_NULL("Could not get the spawner type of the item."),
   NEED_ITEM_SPAWNERTYPE("Can not create spawner with a empty spawner-type."),
   MATERIAL_NEEDS_TO_BE_SPAWNER("To apply a Spawner-Type(Entity-Type) to the item, the material of the item needs to be a spawner."),
   ITEM_POTIONTYPE_NULL("Could not get the potion type of the item."),
   NEED_ITEM_POTIONTYPE("Can not create a potiontype with a blank value."),
   POTIONTYPE_NOT_SUPPORTED("This potion type is not supported or doesn't exist."),
   MATERIAL_NEEDS_TO_BE_POTION("To apply an potion effect, the material needs to be a kind of a potion or a tipped arrow."),
   MATERIAL_NEEDS_TO_BE_LEATHER_ARMOR("To apply an color to a armor piece, the material needs to be leather armor."),
   CANT_BIND_MULTIPLE_APPLYMENTS("Can not create an item with multiple applyments like enchantments/spawner types or potion effect types."),
   ITEM_ERROR("&cItem Error, look in the console."),
   MATERIAL_NOT_SUPPORTED("This item material is not supported in this minecraft version."),
   ITEM_NEEDS_BUY_AND_SELL_VALUE("Every item in the shop needs to have a buy/sell value."),
   SELL_VALUE_WRONG("The sell value needs to be a decimal or a number."),
   BUY_VALUE_WRONG("The buy value needs to be a decimal or a number."),
   OPTION_TYPE_CHANGED("Please change option 'type' to option 'spawnertype' to create a spawner in the shop. The name of the option has changed in version 2.2 for clarity."),
   OPTION_META_CHANGED("Please change option 'meta' to option 'displayname' to create a item with a displayname in the shop. The name of the option has changed in version 2.2 for clarity."),
   CANNOT_DISPLAY_SHOPSECTION("Cannot create shop section '%shopsection%' because the slot of the item needs to be in a range from 0 - 45."),
   NEED_RECIPE_MATERIAL("Cannot create the recipe material with a blank value."),
   CANNOT_GET_RECIPE_MATERIAL("Cannot create the recipe material, please check the material name for any spelling mistakes and if it exists."),
   RECIPE_MATERIAL_NOT_SUPPORTED("The given recipe material is not supported in this minecraft version."),
   MATERIAL_NEEDS_TO_BE_BOOK("To apply an recipe to the item, the material of the item needs to be a KnowLedge book."),
   NEED_SKULLOWNER("Cannot create the PlayerHead without a specified player (Skull Owner)."),
   MATERIAL_NEEDS_TO_BE_SKULL("To apply an skull texture (Skull Owner) to the item, the material of the item needs to be a Player Head."),
   INVALID_ITEM_REQUIREMENT_TYPE("Invalid item requirement type for '%type%'"),
   INVALID_ITEM_REQUIREMENT("Invalid item requirement for %requirement%"),
   INVALID_SPAWN_POTENTIAL_WEIGHT("Invalid weight for spawn potential, should be a number"),
   INVALID_SPAWN_POTENTIAL("Invalid entry for spawn potential '%spawn_potential%', should be format of '<entityType>:<weight>'"),
   INVALID_MATERIAL_FOR_ITEM_OPTION("Material of item should be %material% to set %item-option%"),
   INVALID_OMINOUS_BOTTLE_STRENGTH("Invalid strength for ominous bottle, should be between 1 and 5, but got %strength%"),
   ERROR_SETTING_ITEM_OPTION("Failed to set the %item-option% for item with error:"),
   INVALID_ITEM_ACTION("Invalid item action for '%action%'"),
   CANNOT_DO_THAT_ACTION_ITEM("&cCannot do that for a action item"),
   ITEMS_PATH_IN_SHOPS_CONFIG("Item path in shops.yml: %location%"),
   ITEMS_PATH_IN_SECTIONS_CONFIG("Item path in sections.yml: %location%"),
   ITEMS_PATH_IN_CONFIG("Item path in config.yml: %location%"),
   ITEMS_LOCATION_IN_SHOPS("In shops config /shops/%section%.yml at location: %location%"),
   ITEMS_LOCATION_IN_SECTIONS("In sections config /sections/%section%.yml at location: %location%"),
   SPAWNER_COMPATIBILITY_ENABLED("Spawner provider set to %provider% in config"),
   SPAWNER_PROVIDER_FOUND("%provider% found, integrating..."),
   SPAWNER_PROVIDER_NOT_FOUND("%provider% was not found"),
   FAILED_SPAWNER_PROVIDER_INTEGRATION("Failed to integrate with %provider%"),
   USING_EXTERNAL_SPAWNERS("Using %provider% as external spawner provider..."),
   MINEABLESPAWNERS_COMPATIBILITY_ENABLED("Spawner provider set to MineableSpawners in config"),
   MINEABLESPAWNERS_FOUND("MineableSpawners found, integrating..."),
   MINEABLESPAWNERS_NOT_FOUND("MineableSpawners was not found"),
   COULD_NOT_INTEGRATE_WITH_MINEABLESPAWNERS("Could not integrate with MineableSpawners"),
   USING_MINEABLESPAWNERS_SPAWNERS("Using MineableSpawners as spawner provider..."),
   ROSESTACKER_COMPATIBILITY_ENABLED("Spawner provider set to RoseStacker in config"),
   ROSESTACKER_NOT_FOUND("RoseStacker was not found"),
   COULD_NOT_INTEGRATE_WITH_ROSESTACKER("Could not integrate with RoseStacker"),
   ROSESTACKER_FOUND("RoseStacker found, integrating..."),
   USING_ROSESTACKER_SPAWNERS("Using RoseStacker as spawner provider..."),
   ULTIMATESTACKER_COMPATIBILITY_ENABLED("Spawner provider set to UltimateStacker in config"),
   ULTIMATESTACKER_NOT_FOUND("UltimateStacker was not found"),
   COULD_NOT_INTEGRATE_WITH_ULTIMATESTACKER("Could not integrate with UltimateStacker"),
   ULTIMATESTACKER_FOUND("UltimateStacker found, integrating..."),
   USING_ULTIMATESTACKER_SPAWNERS("Using UltimateStacker as spawner provider..."),
   WILDSTACKER_COMPATIBILITY_ENABLED("Spawner provider set to WildStacker in config"),
   WILDSTACKER_NOT_FOUND("WildStacker was not found."),
   WILDSTACKER_FOUND("WildStacker found, integrating..."),
   COULD_NOT_INTEGRATE_WITH_WILDSTACKER("Could not integrate with WildStacker"),
   USING_WILDSTACKER_SPAWNERS("Using WildStacker as spawner provider."),
   SILKSPAWNERS_COMPATIBILITY_ENABLED("SilkSpawners compatibility has been enabled in config."),
   SILKSPAWNERS_NOT_FOUND("SilkSpawners not found, using EconomyShopGUI spawners..."),
   SILKSPAWNERS_FOUND("SilkSpawners found, integrating..."),
   COULD_NOT_INTEGRATE_WITH_SILKSPAWNERS("Could not integrate with SilkSpawners."),
   USING_SILKSPAWNERS_SPAWNERS("Using SilkSpawners spawners..."),
   USING_DEFAULT_SPAWNERS("Using plugin default spawners..."),
   SPAWNER_PROVIDER_DISABLED("Spawner provider disabled in config, disabling..."),
   CANNOT_FIND_DISCOUNTED_SECTION("Cannot add discounts for section '%section%', either the shop section is disabled or not found."),
   DISABLING_PLUGIN("Disabling the plugin..."),
   NOT_SPAWN_ABLE("This entity type is not spawn able. '%spawnertype%'"),
   REAL_PLAYER("You need to be a real player to use that command."),
   PLAYER_NOT_ONLINE("&cThat player does not exist or is not online."),
   ITEM_NULL("&cThe item does not exist."),
   ITEM_CANNOT_BE_SOLD_TO_SERVER("&cThe item (%material%) cannot be sold to the server because the item has a error inside the shop. Please report this issue to server admins or the owner."),
   CANNOT_SELL_AIR("&cYou cannot sell air..."),
   NO_PERMISSIONS("&cYou do not have permissions for that!"),
   NO_PERMISSIONS_TO_OPEN_SHOP("&cYou do not have the required permission to open this shop section."),
   CANNOT_DO_THAT("&4You can not do that!!"),
   MORE_SPACE_NEEDED("&cPlease make more space inside your inventory and then try again."),
   COULD_NOT_STORE_ALL_ITEMS("&c%amount% item(s) couldn't be added to your inventory because it is full."),
   NO_ITEM_FOUND("&cNo items found which can be sold."),
   NOT_ENOUGH_ITEMS_TO_SELL("&cIt is required to sell atleast %min-sell%, you only have %quantity%"),
   NOT_ENOUGH_ITEMS_TO_BUY("&cIt is required to buy atleast %min-buy%"),
   NOT_ENOUGH_SPACE_INSIDE_INVENTORY("&4You do not have enough space in your inventory, dropping the remaining items on ground."),
   CANCELING_TRANSACTION_NO_SPACE("&4You do not have enough space in your inventory, canceling transaction. \nPlease make more room inside your inventory and try again."),
   INSUFFICIENT_MONEY("&cYou do not have enough %currency%."),
   NO_ITEMS_IN_SECTION("Can not open shop section %shopsection% because there are no items found!"),
   COULD_NOT_SAVE_CONFIG("Could not save %fileName% config."),
   RGB_COLOR_FORMATTED_WRONG("RGB colors must be formatted as: '0x000000', '#000000' or '000000' at %path%."),
   NO_SHOP_FOUND("&cCan not find a shop section called by that name!"),
   NO_VALID_AMOUNT("&cNot a valid amount"),
   NO_SHOP_PAGE_FOUND("&cNo shop page found for %page%"),
   RESTART_PLUGIN("Can not open the shop section, try restarting the plugin or join the discord server for support."),
   COULD_NOT_CHECK_FOR_UPDATES("Could not check for updates."),
   ERROR_OCCURRED_WHILE_RELOADING("&4A error has occurred while reloading the plugin, please look inside the console for any error message."),
   INVALID_ITEM_TYPE("&cInvalid item type for %type%"),
   CANNOT_ENTER_SHOP_BANNED_GAMEMODE("&cYou cannot buy/sell items while being in %gamemode%."),
   CANNOT_USE_COMMAND_BANNED_GAMEMODE("&cYou cannot use this command while being in %gamemode%."),
   EXCEPTION_LOADING_FILL_ITEM("A exception occurred while loading shop fill-Item for section '%section%'."),
   CANNOT_LOAD_SEASON_MODIFIER("Failed to load season modifier for '%path%' because there was no loaded section/item found."),
   CANNOT_LOAD_SEASON_MODIFIER_ACTION_ITEM("Cannot load season modifier for '%path%' as the shop item contains a action"),
   INVALID_SEASON_MODIFIER("Invalid modifier found at '%path%', it needs to be a valid percent between -100 and 100 like '-75%', '+25%', ... Got %current%"),
   CANNOT_LOAD_ECONOMY_PROVIDER("Failed to load economy provider for shop '%shop%' with reason '%reason%', using default..."),
   CANNOT_FIND_ECONOMY_PROVIDER("Could not find a economy provider for shop '%shop%' such as '%type%', using default..."),
   CANNOT_FIND_DEFAULT_ECONOMY_PROVIDER("Could not find the economy type specified inside the config.yml such as '%economy%', trying to automatically find supported economy type..."),
   CANNOT_LOAD_DEFAULT_ECONOMY_PROVIDER("Failed to load default economy provider with reason '%reason%', trying to automatically find supported economy provider"),
   REMOVED_SHOP_STAND("&cRemoved shop stand from location %location%"),
   SHOP_STAND_LOAD_FAILED("Failed to load shop stand with ID '%id%': %reason%"),
   SHOP_STAND_UNLOAD_FAILED("Failed to unload shop stand with ID '%id%': %reason%"),
   SHOP_STAND_INSIDE_UNLOADED_CHUNK("Shop stand is inside a unloaded chunk"),
   ITEM_FROM_SHOP_STAND_NOT_FOUND("Cannot find shop item from shop stand '%item%'"),
   BLOCK_FROM_SHOP_STAND_NOT_FOUND("Block type from stand at location %location% does not match with cached block type of '%type%'. Possibly removed from world manually but not from plugin?"),
   INVALID_SHOP_STAND_WORLD("Invalid stand location for world '%world%', does not exist for shop stand ID %id%"),
   INVALID_SHOP_STAND_FORMAT("Invalid stand location with x%pos_x%, y%pos_y%, z%pos_z% for shop stand ID %id%"),
   ERROR_SAVING_SHOP_STANDS("Failed to save shop stands to 'stands.json'"),
   ERROR_LOADING_SHOP_STANDS("Failed to load shop stands from 'stands.json'"),
   ERROR_LOADING_SHOP_STAND("Failed to load shop stand('%stand%') from stands.json with reason: %reason%"),
   ERROR_CREATING_STANDS_STORAGE("Failed to create 'stands.json' storage file"),
   INVALID_STAND_TYPE("Invalid shop stand type for %type%"),
   SHOP_STANDS_MODULE_NOT_ENABLED("Shop stands module not enabled"),
   CANNOT_OPEN_SHOP_STAND("Failed to open shop stand for player %player%: ShopItem(%item%) does not exist!"),
   INVALID_CLICK_TYPE("Invalid click type for %click-type% with entry %click-mapping%, using default..."),
   INVALID_CLICK_ACTION("Invalid click action for %click-action% with entry %click-mapping%, using default...");

   private String def;
   private static String fileName;
   private static File configFile;
   private static FileConfiguration config;
   private static EconomyShopGUI plugin;
   private static HashMap<String, Translatable> cache = new HashMap();

   private Lang(String param3) {
      this.def = def;
   }

   public void reload() {
      if (!cache.isEmpty()) {
         cache.clear();
      }

      plugin = EconomyShopGUI.getInstance();
      plugin.startupReload.checkIfLanguageFilesExist();
      fileName = plugin.badYMLParse != null ? "lang-en.yml" : ConfigManager.getConfig().getString("language-file", "lang-en.yml");
      configFile = new File(plugin.getDataFolder() + File.separator + "LanguageFiles", fileName);
      this.loadLanguageFile();
      if (fileName != null) {
         plugin.getServer().getConsoleSender().sendMessage("§8[§3EconomyShopGUI§8] [§7INFO§8]§r: Using " + fileName + " as language file");
      } else {
         plugin.getServer().getConsoleSender().sendMessage("§8[§3EconomyShopGUI§8] [§4ERROR§8]§r: Can not find language file, using English as default language...");
      }

   }

   public String getKey() {
      return this.name().toLowerCase(Locale.ENGLISH).replace("_", "-").replace("0", ",");
   }

   public void clearMessages() {
      cache.clear();
   }

   public Translatable get() {
      String key = this.getKey();
      if (cache.containsKey(key)) {
         return (Translatable)cache.get(key);
      } else {
         Translatable value;
         try {
            value = fromConfig(config.getString(key, this.def));
         } catch (NullPointerException var4) {
            value = fromConfig(this.def);
         }

         cache.put(key, value);
         return value;
      }
   }

   public String getRaw() {
      try {
         return config.getString(this.getKey(), this.def);
      } catch (NullPointerException var2) {
         return this.def;
      }
   }

   public static Translatable fromConfig(String s) {
      return (Translatable)(plugin.getMetaUtils() instanceof BukkitMeta ? new TranslatableRaw(s) : new TranslatableComponent(s));
   }

   private void loadLanguageFile() {
      File file = new File(plugin.getDataFolder() + "/LanguageFiles/", fileName);
      FileConfiguration c = plugin.loadConfiguration(file, fileName);
      if (c != null) {
         try {
            plugin.saveResource("LanguageFiles" + File.separator + fileName, true);
            FileConfiguration conf = plugin.loadConfiguration(file, fileName);
            if (conf != null) {
               Iterator var4 = c.getKeys(false).iterator();

               while(var4.hasNext()) {
                  String str = (String)var4.next();
                  conf.set(str, c.get(str));
               }

               ConfigUtil.save(conf, file);
               config = conf;
            } else {
               ConfigUtil.save(c, file);
            }
         } catch (Exception var6) {
            SendMessage.logDebugMessage("Failed to automatically update the language file for " + fileName);
            config = plugin.loadConfiguration(file, fileName);
         }

      }
   }

   // $FF: synthetic method
   private static Lang[] $values() {
      return new Lang[]{UPDATING_SHOP_SETTINGS, CREATING_LATEST_SHOPS, DEFAULT_SHOPS_CREATED, LOADED_SHOP_CONFIGURATIONS, DEFAULT_SECTIONS_CREATED, LOADED_SECTION_CONFIGURATIONS, CREATING_NEW_SHOP_CONFIG, CREATING_NEW_SECTION_CONFIG, DISABLED_DUE_NO_VAULT, DISABLED_DUE_NO_ECONOMY, DISCOUNTS_FEATURE_UNAVAILABLE, UPDATE_AVAILABLE, DONE, UPDATING_SECTIONS_CONFIG, DEBUG_MODE_ENABLED, ECONOMY_PROVIDER_NULL, LANGUAGE_FILE_USING, CANNOT_FIND_LANGUAGE_FILE, MINECRAFT_VERSION_USING, LOADING_ITEMS, COULD_NOT_FIND_VALID_VERSION, UPDATING_CONFIGS, CREATING_BACKUP, ENABLED_PLUGIN_HOOK, FAILED_PLUGIN_INTEGRATION, LOADING_SEASON_MODIFIERS, LOADED_SEASON_MODIFIERS, NO_SEASON_MODIFIERS_FOUND, SEASON_CHANGE, ECONOMY_PROVIDERS_LOADED, SECTION_ECONOMY_PROVIDER_LOADED, LOADED_SHOP_STAND_MODULE, SAVING_SHOP_STANDS, SHOP_PREFIX, CONSOLE_PREFIX, INVENTORY_MAIN_SHOP_TITLE, PROFILE, NAME, MONEY, LEVEL, INVENTORY_SELLGUI_TITLE, INVENTORY_BUYSTACKS_TITLE, INVENTORY_HOWMUCHSELL_TITLE, INVENTORY_HOWMUCHBUY_TITLE, PLUS_ONE, MIN_ONE, PLUS_SIXTEEN, MIN_SIXTEEN, PLUS_THIRTY_TWO, MIN_THIRTY_TWO, SELL, SELL_ALL, BUY, BUY_STACKS, MAX_AMOUNT_REACHED, MIN_AMOUNT_REACHED, PREVIOUS_PAGE, NEXT_PAGE, CURRENT_PAGE, SHOPSTAND_BUY_SCREEN_TITLE, SHOPSTAND_SELL_SCREEN_TITLE, SHOPSTAND_CLICK_TO_BUY, SHOPSTAND_CLICK_TO_SELL, SHOPSTAND_TOTAL_PRICE, DEFAULT_SHOP_STAND_ITEM_NAME, DEFAULT_SHOP_STAND_ITEM_LORE, SHOP_STANDS_MANAGEMENT_INVENTORY_TITLE, SHOP_STAND_INFO_ITEM_NAME, SHOP_STAND_INFO_ITEM_LORE, FORCE_RELOAD_STAND_ITEM_NAME, FORCE_RELOAD_STAND_ITEM_LORE, DESTROY_STAND_ITEM_NAME, DESTROY_STAND_ITEM_LORE, RELOADED_SHOP_STAND, SHOP_STAND_BROWSE_INVENTORY_TITLE, SHOP_STAND_LOCATION_FORMAT, CREATED_NEW_SHOP_STAND, ERROR_CREATING_NEW_SHOP_STAND, CANCEL, BACK, LEFT_CLICK_BUY, SEASONAL_BUY_PRICE, DISCOUNTED_BUY_PRICE, RIGHT_CLICK_SELL, SEASONAL_SELL_PRICE, MULTIPLIED_SELL_PRICE, MIDDLE_CLICK_SELL_ALL, SHIFT_RIGHT_CLICK_SELL_ALL, ITEM_LOCATION_IN_SHOP, ITEM_CANNOT_BE_BOUGHT, ITEM_CANNOT_BE_SOLD, ITEM_SEASONAL_PRICE, DECORATION_LORE, DECORATION_LORE_BUY, DECORATION_LORE_SELL_ALL, DECORATION_LORE_SELL, TIME_REQUIREMENT_LORE, QUEST_REQUIREMENT_LORE, REGION_REQUIREMENT_LORE, DISCORD_SRV_PLAYER_TRANSACTION, SELL_CONFIRMATION_MULTIPLE_ITEMS, SELL_CONFIRMATION, PAY_CONFIRMATION, NO_ITEM_TO_BE_SOLD, CANNOT_PURCHASE_ITEM, CANNOT_SELL_ITEM, PLAYER_MADE_TRANSACTION, PLAYER_MADE_TRANSACTION_MULTIPLE_ITEMS, TRANSACTION_FAILED_NO_SPACE, TIME_REQUIREMENT_NOT_MET, QUEST_REQUIREMENT_NOT_MET, REGION_REQUIREMENT_NOT_MET, BUY_SCREEN, SELL_SCREEN, BUYSTACKS_SCREEN, SELLALL_SCREEN, SELLALL_COMMAND, SELLGUI_SCREEN, QUICK_BUY, QUICK_SELL, SHOPSTAND_BUY_SCREEN, SHOPSTAND_SELL_SCREEN, AUTO_SELL_CHEST, BOUGHT, SOLD, TRANSACTION_SCREEN_TOTAL_PRICE, TRANSACTION_SCREEN_TOTAL_AMOUNT, ON_SCREEN_TITLE_SOLD, ON_SCREEN_TITLE_BOUGHT, CURRENCYSYMBOL, SYMBOL_PRICING_FORMAT, NAMED_PRICING_FORMAT, XP_CURRENCY_NAME_SINGULAR, XP_CURRENCY_NAME_PLURAL, COMMAND_DISABLED_IN_WORLD, EDITSHOP_ADD_ITEM_SUBCOMMAND_DESC, EDITSHOP_EDIT_ITEM_SUBCOMMAND_DESC, EDITSHOP_REMOVE_ITEM_SUBCOMMAND_DESC, EDITSHOP_ADD_HAND_ITEM_SUBCOMMAND_DESC, EDITSHOP_IMPORT_SUBCOMMAND_DESC, EDITSHOP_ADD_SECTION_SUBCOMMAND_DESC, EDITSHOP_EDIT_SECTION_SUBCOMMAND_DESC, EDITSHOP_REMOVE_SECTION_SUBCOMMAND_DESC, EDITSHOP_MIGRATE_SUBCOMMAND_DESC, EDITSHOP_SHOPSTANDS_SUBCOMMAND_DESC, EDITSHOP_SHOPSTANDS_GIVE_SUBCOMMAND_DESC, EDITSHOP_SHOPSTANDS_DESTROY_SUBCOMMAND_DESC, EDITSHOP_SHOPSTANDS_BROWSE_SUBCOMMAND_DESC, EDITSHOP_ADD_ITEM_SUBCOMMAND_SYNTAX, EDITSHOP_EDIT_ITEM_SUBCOMMAND_SYNTAX, EDITSHOP_REMOVE_ITEM_SUBCOMMAND_SYNTAX, EDITSHOP_ADD_HAND_ITEM_SUBCOMMAND_SYNTAX, EDITSHOP_IMPORT_SUBCOMMAND_SYNTAX, EDITSHOP_ADD_SECTION_SUBCOMMAND_SYNTAX, EDITSHOP_EDIT_SECTION_SUBCOMMAND_SYNTAX, EDITSHOP_REMOVE_SECTION_SUBCOMMAND_SYNTAX, EDITSHOP_MIGRATE_SUBCOMMAND_SYNTAX, EDITSHOP_SHOPSTANDS_SUBCOMMAND_SYNTAX, EDITSHOP_SHOPSTANDS_GIVE_SUBCOMMAND_SYNTAX, EDITSHOP_SHOPSTANDS_DESTROY_SUBCOMMAND_SYNTAX, EDITSHOP_SHOPSTANDS_BROWSE_SUBCOMMAND_SYNTAX, EDITSHOP_ADDING_ITEM, EDITSHOP_ADD_ITEM_SUCCESSFUL, EDITSHOP_ADDING_SECTION, EDITSHOP_ADD_SECTION_SUCCESSFUL, EDITSHOP_EDITING_ITEM, EDITSHOP_EDITING_SECTION, EDITSHOP_EDIT_ITEM_SUCCESSFUL, EDITSHOP_REMOVING_ITEM, EDITSHOP_REMOVE_ITEM_SUCCESSFUL, EDITSHOP_REMOVING_SECTION, EDITSHOP_REMOVED_SECTION, EDITSHOP_RELOAD_SHOP_TO_SEE_CHANGES, FILE_REPLACED, SHOP_STAND_ITEM_RECEIVED, SECTION_DOES_NOT_CONTAIN_ITEM, NO_VALID_ACTION, NO_VALID_KEY, EDIT_KEY_UNAVAILABLE, ACTION_NOT_VALID_ON_KEY, EDIT_ITEM_MATERIAL_INVALID, KEY_ALREADY_DEFINED, KEY_IS_NOT_FOUND, NO_VALID_SLOT_FOR_ITEM, PLUGIN_NOT_FOUND, FILE_NOT_FOUND, SLOT_ALREADY_IN_USE, SLOT_NEEDS_TO_BE_NUMBER, INVALID_SHOP_STAND_TYPE, UNKNOWN_SHOP_STAND_ID, STAND_TYPE_NOT_SUPPORTED, CONFIGS_RELOADED, SELLALL_COMMAND_USAGES, SELLALL_INVENTORY_COMMAND_USAGE, SELLALL_HAND_COMMAND_USAGE, SELLALL_ITEM_COMMAND_USAGE, SHOP_ITEM_GIVEN, SHOP_ITEM_RECEIVED, MINIMUM_AMOUNT, PAYMENT_COMPLETE, PLAYER_RECEIVED_MONEY, NEED_PLAYER_AND_AMOUNT, NEED_AMOUNT, PAYCOMMAND_EXAMPLE, GIVEMONEYCOMMAND_EXAMPLE, GIVEMONEY_COMPLETE, CHECK_BALANCE, CHECK_BALANCE_OTHER_PLAYER, DEFAULT_SPAWNER_NAME, SPAWNER_BROKE_AND_APPLIED_TO_INVENTORY, NEED_SILK_TOUCH, JOIN_MESSAGE, LEVEL_EVENT_MESSAGE, DISPLAYNAME_NULL, ITEM_NAME_NULL, NEED_ITEM_MATERIAL, ITEM_MATERIAL_NULL, ITEM_ENCHANTMENT_NULL, NEED_ITEM_ENCHANTMENT, ITEM_ENCHANTMENT_NOT_SUPPORTED, ITEM_ENCHANTMENT_STRENGTH_NULL, ENCHANTMENT_CANNOT_APPLY_TO_ITEM, ITEM_SPAWNERTYPE_NULL, NEED_ITEM_SPAWNERTYPE, MATERIAL_NEEDS_TO_BE_SPAWNER, ITEM_POTIONTYPE_NULL, NEED_ITEM_POTIONTYPE, POTIONTYPE_NOT_SUPPORTED, MATERIAL_NEEDS_TO_BE_POTION, MATERIAL_NEEDS_TO_BE_LEATHER_ARMOR, CANT_BIND_MULTIPLE_APPLYMENTS, ITEM_ERROR, MATERIAL_NOT_SUPPORTED, ITEM_NEEDS_BUY_AND_SELL_VALUE, SELL_VALUE_WRONG, BUY_VALUE_WRONG, OPTION_TYPE_CHANGED, OPTION_META_CHANGED, CANNOT_DISPLAY_SHOPSECTION, NEED_RECIPE_MATERIAL, CANNOT_GET_RECIPE_MATERIAL, RECIPE_MATERIAL_NOT_SUPPORTED, MATERIAL_NEEDS_TO_BE_BOOK, NEED_SKULLOWNER, MATERIAL_NEEDS_TO_BE_SKULL, INVALID_ITEM_REQUIREMENT_TYPE, INVALID_ITEM_REQUIREMENT, INVALID_SPAWN_POTENTIAL_WEIGHT, INVALID_SPAWN_POTENTIAL, INVALID_MATERIAL_FOR_ITEM_OPTION, INVALID_OMINOUS_BOTTLE_STRENGTH, ERROR_SETTING_ITEM_OPTION, INVALID_ITEM_ACTION, CANNOT_DO_THAT_ACTION_ITEM, ITEMS_PATH_IN_SHOPS_CONFIG, ITEMS_PATH_IN_SECTIONS_CONFIG, ITEMS_PATH_IN_CONFIG, ITEMS_LOCATION_IN_SHOPS, ITEMS_LOCATION_IN_SECTIONS, SPAWNER_COMPATIBILITY_ENABLED, SPAWNER_PROVIDER_FOUND, SPAWNER_PROVIDER_NOT_FOUND, FAILED_SPAWNER_PROVIDER_INTEGRATION, USING_EXTERNAL_SPAWNERS, MINEABLESPAWNERS_COMPATIBILITY_ENABLED, MINEABLESPAWNERS_FOUND, MINEABLESPAWNERS_NOT_FOUND, COULD_NOT_INTEGRATE_WITH_MINEABLESPAWNERS, USING_MINEABLESPAWNERS_SPAWNERS, ROSESTACKER_COMPATIBILITY_ENABLED, ROSESTACKER_NOT_FOUND, COULD_NOT_INTEGRATE_WITH_ROSESTACKER, ROSESTACKER_FOUND, USING_ROSESTACKER_SPAWNERS, ULTIMATESTACKER_COMPATIBILITY_ENABLED, ULTIMATESTACKER_NOT_FOUND, COULD_NOT_INTEGRATE_WITH_ULTIMATESTACKER, ULTIMATESTACKER_FOUND, USING_ULTIMATESTACKER_SPAWNERS, WILDSTACKER_COMPATIBILITY_ENABLED, WILDSTACKER_NOT_FOUND, WILDSTACKER_FOUND, COULD_NOT_INTEGRATE_WITH_WILDSTACKER, USING_WILDSTACKER_SPAWNERS, SILKSPAWNERS_COMPATIBILITY_ENABLED, SILKSPAWNERS_NOT_FOUND, SILKSPAWNERS_FOUND, COULD_NOT_INTEGRATE_WITH_SILKSPAWNERS, USING_SILKSPAWNERS_SPAWNERS, USING_DEFAULT_SPAWNERS, SPAWNER_PROVIDER_DISABLED, CANNOT_FIND_DISCOUNTED_SECTION, DISABLING_PLUGIN, NOT_SPAWN_ABLE, REAL_PLAYER, PLAYER_NOT_ONLINE, ITEM_NULL, ITEM_CANNOT_BE_SOLD_TO_SERVER, CANNOT_SELL_AIR, NO_PERMISSIONS, NO_PERMISSIONS_TO_OPEN_SHOP, CANNOT_DO_THAT, MORE_SPACE_NEEDED, COULD_NOT_STORE_ALL_ITEMS, NO_ITEM_FOUND, NOT_ENOUGH_ITEMS_TO_SELL, NOT_ENOUGH_ITEMS_TO_BUY, NOT_ENOUGH_SPACE_INSIDE_INVENTORY, CANCELING_TRANSACTION_NO_SPACE, INSUFFICIENT_MONEY, NO_ITEMS_IN_SECTION, COULD_NOT_SAVE_CONFIG, RGB_COLOR_FORMATTED_WRONG, NO_SHOP_FOUND, NO_VALID_AMOUNT, NO_SHOP_PAGE_FOUND, RESTART_PLUGIN, COULD_NOT_CHECK_FOR_UPDATES, ERROR_OCCURRED_WHILE_RELOADING, INVALID_ITEM_TYPE, CANNOT_ENTER_SHOP_BANNED_GAMEMODE, CANNOT_USE_COMMAND_BANNED_GAMEMODE, EXCEPTION_LOADING_FILL_ITEM, CANNOT_LOAD_SEASON_MODIFIER, CANNOT_LOAD_SEASON_MODIFIER_ACTION_ITEM, INVALID_SEASON_MODIFIER, CANNOT_LOAD_ECONOMY_PROVIDER, CANNOT_FIND_ECONOMY_PROVIDER, CANNOT_FIND_DEFAULT_ECONOMY_PROVIDER, CANNOT_LOAD_DEFAULT_ECONOMY_PROVIDER, REMOVED_SHOP_STAND, SHOP_STAND_LOAD_FAILED, SHOP_STAND_UNLOAD_FAILED, SHOP_STAND_INSIDE_UNLOADED_CHUNK, ITEM_FROM_SHOP_STAND_NOT_FOUND, BLOCK_FROM_SHOP_STAND_NOT_FOUND, INVALID_SHOP_STAND_WORLD, INVALID_SHOP_STAND_FORMAT, ERROR_SAVING_SHOP_STANDS, ERROR_LOADING_SHOP_STANDS, ERROR_LOADING_SHOP_STAND, ERROR_CREATING_STANDS_STORAGE, INVALID_STAND_TYPE, SHOP_STANDS_MODULE_NOT_ENABLED, CANNOT_OPEN_SHOP_STAND, INVALID_CLICK_TYPE, INVALID_CLICK_ACTION};
   }
}
