package me.ag4.playershop.files;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
   BROADCAST_PURCHASES("Broadcast-Purchases", "&aYou got &6{price}$ &afrom the playershop."),
   GUI_Price("GUI-Price", "&6{price}$"),
   GUI_Error_Money("GUI-Error-Money", "&cYou don't have enough money"),
   GUI_Item_Buy("GUI-Buy", "&aYou purchased the item for &6{price}$"),
   GUI_Item_Not_Exist_Default("GUI-Item-Not-Exist-Default", "&cItem not Exist Anymore"),
   GUI_INVENTORY_IS_FULL("GUI-Inventory-Is-Full", "&cShop container is full"),
   Price_Set("Price-Setting", "&aPrice set to &6{price}$"),
   Place_Message("Place-Message", "&6&lPlayerShop placed."),
   Break_Message("Break-Message", "&6PlayerShop &cbroken."),
   Error_Permission("Error-Permission", "&cYou don't have Permission"),
   Error_Place("Error-Place", "&cYou cannot place it here"),
   Error_Place_Max("Error-Place_Max", "&cYou cannot place more then {max} shop"),
   Error_Place_Merge("Error-Place-Merge", "&cYou are not allowed merge playershop"),
   Error_Place_WorldGuard_Flag("Error-Place-WorldGuard-Flag", "&cYou are not allowed to place it in this area"),
   Error_Shop_Not_Ready_Owner("Error-Shop-Not-Ready-Owner", "&cShop is not Ready (&eShift + right-click&c)"),
   Error_Shop_Not_Ready_Default("Error-Shop-Not-Ready-Default", "&cShop is not Ready"),
   Error_Price_Enter("Error-Price-Enter", "&cPlease Enter Price"),
   Error_Price_0("Error-Price-0", "&cYou cannot set price to &60$"),
   Error_Item_BlackListed("Error-Item-BlackListed", "&cThe item is blacklisted"),
   Sign_Line_2("Sign-Line2", "&7^^^^^^^^^^^^^^^^"),
   Sign_Line_3("Sign-Line3", "&aEnter Price"),
   Sign_Line_4("Sign-Line4", "&6<Price>"),
   Form_Title("Form-Title", "&ePlayerShop"),
   Form_Input_1("Form-Input-1", "&aPrice"),
   Form_Input_2("Form-Input-2", "&7Enter Price"),
   Placeholder_Shop_Count("Placeholder_Shop_Count", "&6{count}"),
   Placeholder_Shop_Max("Placeholder_Shop_Max", "&6{count}"),
   Placeholder_Shop_Unlimited("Placeholder_Shop_Unlimited", "&6X");

   private String path;
   private String def;
   private static YamlConfiguration LANG;

   private Lang(String param3, String param4) {
      this.path = path;
      this.def = start;
   }

   public static void setFile(YamlConfiguration config) {
      LANG = config;
   }

   public String toString() {
      return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, this.def));
   }

   public String getDefault() {
      return this.def;
   }

   public String getPath() {
      return this.path;
   }

   // $FF: synthetic method
   private static Lang[] $values() {
      return new Lang[]{BROADCAST_PURCHASES, GUI_Price, GUI_Error_Money, GUI_Item_Buy, GUI_Item_Not_Exist_Default, GUI_INVENTORY_IS_FULL, Price_Set, Place_Message, Break_Message, Error_Permission, Error_Place, Error_Place_Max, Error_Place_Merge, Error_Place_WorldGuard_Flag, Error_Shop_Not_Ready_Owner, Error_Shop_Not_Ready_Default, Error_Price_Enter, Error_Price_0, Error_Item_BlackListed, Sign_Line_2, Sign_Line_3, Sign_Line_4, Form_Title, Form_Input_1, Form_Input_2, Placeholder_Shop_Count, Placeholder_Shop_Max, Placeholder_Shop_Unlimited};
   }
}
