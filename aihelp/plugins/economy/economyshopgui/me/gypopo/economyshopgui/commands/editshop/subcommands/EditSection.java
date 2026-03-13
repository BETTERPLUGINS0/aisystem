package me.gypopo.economyshopgui.commands.editshop.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.CreateItemMethodes;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.EconomyType;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.PotionTypes;
import me.gypopo.economyshopgui.util.XMaterial;
import me.gypopo.economyshopgui.util.exceptions.ItemLoadException;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.util.StringUtil;

public class EditSection extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;
   private final List<String> allKeys = Arrays.asList("enable", "item.material", "item.stack-size", "item.hideDefaultLore", "item.armor-trim.type", "item.armor-trim.pattern", "item.name", "item.displayname", "slot", "item.enchantment-glint", "item.skullowner", "item.armorcolor", "hidden", "title", "sub-section", "item.potion-glow", "economy", "display-item");
   private final List<String> listKeys = Arrays.asList("item.lore", "item.potiontypes");
   private final List<String> actions = Arrays.asList("add", "set", "remove");
   private String section;
   private String action;
   private String key;
   private Object value;

   public EditSection(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
   }

   public String getName() {
      return "editsection";
   }

   public String getDescription() {
      return Lang.EDITSHOP_EDIT_SECTION_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_EDIT_SECTION_SUBCOMMAND_SYNTAX.get().getLegacy();
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (args.length <= 1) {
         SendMessage.sendMessage(logger, this.getSyntax());
      } else {
         this.section = this.methods.getSection(logger, args[1]);
         if (this.section != null) {
            if (args.length <= 2) {
               SendMessage.sendMessage(logger, this.getSyntax());
            } else {
               this.action = args[2].toLowerCase(Locale.ENGLISH);
               if (!this.actions.contains(this.action)) {
                  SendMessage.sendMessage(logger, Lang.NO_VALID_ACTION.get().replace("%action%", args[2]).replace("%actions%", Arrays.toString(this.actions.toArray())));
               } else if (args.length <= 3) {
                  SendMessage.sendMessage(logger, this.getSyntax());
               } else {
                  this.key = args[3];
                  Stream var10000 = this.getAvailableKeys().stream();
                  String var10001 = this.key;
                  Objects.requireNonNull(var10001);
                  if (var10000.noneMatch(var10001::equalsIgnoreCase)) {
                     SendMessage.sendMessage(logger, Lang.NO_VALID_KEY.get().replace("%key%", args[3]).replace("%keys%", Arrays.toString(this.getAvailableKeys().toArray())));
                  } else {
                     if (this.action.equalsIgnoreCase("remove")) {
                        var10000 = this.listKeys.stream();
                        var10001 = this.key;
                        Objects.requireNonNull(var10001);
                        if (var10000.noneMatch(var10001::equalsIgnoreCase)) {
                           this.value = null;
                           if (!this.validate(logger)) {
                              return;
                           }

                           this.updateOption(logger);
                           return;
                        }
                     }

                     if (args.length <= 4) {
                        SendMessage.sendMessage(logger, this.getSyntax());
                     } else {
                        StringBuilder value2 = new StringBuilder();

                        for(int i = 4; i < args.length; ++i) {
                           value2.append(args[i]).append(" ");
                        }

                        this.value = value2.substring(0, value2.length() - 1);
                        if (this.validate(logger)) {
                           this.updateOption(logger);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public List<String> getTabCompletion(String[] args) {
      ArrayList completions;
      switch(args.length) {
      case 2:
         if (!args[1].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[1], this.plugin.getShopSections(), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.plugin.getShopSections();
      case 3:
         if (!args[2].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[2], this.actions, completions);
            Collections.sort(completions);
            return completions;
         }

         return this.actions;
      case 4:
         if (this.isSection(args[1]) && this.isAction(args[2])) {
            if (!args[3].isEmpty()) {
               completions = new ArrayList();
               StringUtil.copyPartialMatches(args[3], this.getAvailableKeys(), completions);
               Collections.sort(completions);
               return completions;
            }

            return this.getAvailableKeys();
         }

         return null;
      case 5:
         if (!args[4].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[4], this.getTabcompletionForValue(args[1], args[2], args[3]), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.getTabcompletionForValue(args[1], args[2], args[3]);
      default:
         return null;
      }
   }

   private boolean isSection(String section) {
      if (this.plugin.getShopSections().contains(section)) {
         this.section = section;
         return true;
      } else {
         return false;
      }
   }

   private boolean isAction(String action) {
      if (this.actions.contains(action)) {
         this.action = action;
         return true;
      } else {
         return false;
      }
   }

   private boolean isKey(String key) {
      if (this.getAvailableKeys().contains(key)) {
         this.key = key;
         return true;
      } else {
         return false;
      }
   }

   private List<String> getExamplePlayerNames() {
      List<String> playerNames = new ArrayList();
      this.plugin.getServer().getOnlinePlayers().forEach((p) -> {
         playerNames.add(p.getName());
      });
      return playerNames;
   }

   private List<String> getTabcompletionForValue(String section, String action, String key) {
      if (this.isSection(section) && this.isAction(action) && this.isKey(key)) {
         if (action.equals("remove") && this.listKeys.contains(key)) {
            return this.listKeys.contains(key) ? this.getValues(section, key) : Collections.singletonList(ConfigManager.getSection(section).get(key, "").toString());
         } else {
            byte var5 = -1;
            switch(key.hashCode()) {
            case -1911224770:
               if (key.equals("economy")) {
                  var5 = 16;
               }
               break;
            case -1808984271:
               if (key.equals("item.skullowner")) {
                  var5 = 8;
               }
               break;
            case -1364160526:
               if (key.equals("item.displayname")) {
                  var5 = 2;
               }
               break;
            case -941907550:
               if (key.equals("item.material")) {
                  var5 = 0;
               }
               break;
            case -879976991:
               if (key.equals("item.stack-size")) {
                  var5 = 6;
               }
               break;
            case -789741780:
               if (key.equals("item.potion-glow")) {
                  var5 = 10;
               }
               break;
            case -723632021:
               if (key.equals("item.potiontypes")) {
                  var5 = 7;
               }
               break;
            case -568604865:
               if (key.equals("item.armorcolor")) {
                  var5 = 5;
               }
               break;
            case 483001:
               if (key.equals("item.enchantment-glint")) {
                  var5 = 9;
               }
               break;
            case 3533310:
               if (key.equals("slot")) {
                  var5 = 17;
               }
               break;
            case 42197080:
               if (key.equals("sub-section")) {
                  var5 = 12;
               }
               break;
            case 110371416:
               if (key.equals("title")) {
                  var5 = 4;
               }
               break;
            case 1130834266:
               if (key.equals("item.hideDefaultLore")) {
                  var5 = 11;
               }
               break;
            case 1150971293:
               if (key.equals("item.armor-trim.type")) {
                  var5 = 14;
               }
               break;
            case 1568779582:
               if (key.equals("display-item")) {
                  var5 = 13;
               }
               break;
            case 1923960397:
               if (key.equals("item.armor-trim.pattern")) {
                  var5 = 15;
               }
               break;
            case 2108324081:
               if (key.equals("item.lore")) {
                  var5 = 3;
               }
               break;
            case 2108370054:
               if (key.equals("item.name")) {
                  var5 = 1;
               }
            }

            switch(var5) {
            case 0:
               return this.plugin.getSupportedMatNames();
            case 1:
            case 2:
            case 3:
            case 4:
               return Arrays.asList("#A1672D&lWood", "&1&lArmor", "#1B22E8&lF#1BAAE8&li#1BE8E1&lr#1BE896&le#1BE829&lw#E8DA1B&lo#E88F1B&lr#EB0909&lk#EB09DC&ls", "&7&lStones", "#09EBDC&lCustom#89EB09&lItems");
            case 5:
               return this.methods.getExampleRGBColors();
            case 6:
               List<String> array = new ArrayList();

               for(int i = 1; i < 64; ++i) {
                  array.add(String.valueOf(i));
               }

               return array;
            case 7:
               return PotionTypes.getNames();
            case 8:
               return this.getExamplePlayerNames();
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
               return Arrays.asList("true", "false");
            case 14:
               return (new CreateItemMethodes.ArmorTrim()).getTypes();
            case 15:
               return (new CreateItemMethodes.ArmorTrim()).getPatterns();
            case 16:
               return Arrays.asList("ULTRA_ECONOMY:Gems", "ULTRA_ECONOMY:Crystals", "VAULT", "GEMS_ECONOMY:Sticks", "PLAYER_POINTS");
            case 17:
               return this.methods.getAvailableMainMenuSlots();
            default:
               if (key.startsWith("pages")) {
                  String[] parts = key.split("\\.");
                  if (parts.length >= 2) {
                     String var8 = parts[2];
                     byte var9 = -1;
                     switch(var8.hashCode()) {
                     case -1364287701:
                        if (var8.equals("gui-rows")) {
                           var9 = 0;
                        }
                        break;
                     case 110371416:
                        if (var8.equals("title")) {
                           var9 = 1;
                        }
                     }

                     switch(var9) {
                     case 0:
                        return Arrays.asList("1", "2", "3", "4", "5", "6");
                     case 1:
                        return Arrays.asList("&8&lTools", "&d&lWools", "#34ebb4Utilities", "&f&lBuildingBlocks", "<blue><bold>TradingItems");
                     }
                  }
               }

               return new ArrayList();
            }
         }
      } else {
         return new ArrayList();
      }
   }

   private boolean validate(Object logger) {
      if (this.action.equalsIgnoreCase("remove") && !this.listKeys.contains(this.key) && (this.key.equalsIgnoreCase("item.material") || this.key.equalsIgnoreCase("enable"))) {
         SendMessage.sendMessage(logger, Lang.ACTION_NOT_VALID_ON_KEY.get().replace("%action%", this.action).replace("%key%", this.key));
         return false;
      } else {
         if (this.value != null) {
            String materialName = ConfigManager.getSection(this.section).getString("item.material");
            if (materialName == null || materialName.isEmpty()) {
               SendMessage.sendMessage(logger, Lang.EDIT_ITEM_MATERIAL_INVALID.get().replace("%material%", materialName));
               return false;
            }

            Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(materialName);
            if (!xMaterial.isPresent() || ((XMaterial)xMaterial.get()).parseItem() == null) {
               SendMessage.sendMessage(logger, Lang.EDIT_ITEM_MATERIAL_INVALID.get().replace("%material%", materialName));
               return false;
            }

            Material mat = ((XMaterial)xMaterial.get()).parseItem().getType();
            String var5 = this.key;
            byte var6 = -1;
            switch(var5.hashCode()) {
            case -1911224770:
               if (var5.equals("economy")) {
                  var6 = 8;
               }
               break;
            case -1808984271:
               if (var5.equals("item.skullowner")) {
                  var6 = 19;
               }
               break;
            case -1364160526:
               if (var5.equals("item.displayname")) {
                  var6 = 2;
               }
               break;
            case -1298848381:
               if (var5.equals("enable")) {
                  var6 = 15;
               }
               break;
            case -1217487446:
               if (var5.equals("hidden")) {
                  var6 = 14;
               }
               break;
            case -941907550:
               if (var5.equals("item.material")) {
                  var6 = 0;
               }
               break;
            case -879976991:
               if (var5.equals("item.stack-size")) {
                  var6 = 17;
               }
               break;
            case -789741780:
               if (var5.equals("item.potion-glow")) {
                  var6 = 10;
               }
               break;
            case -723632021:
               if (var5.equals("item.potiontypes")) {
                  var6 = 18;
               }
               break;
            case -568604865:
               if (var5.equals("item.armorcolor")) {
                  var6 = 16;
               }
               break;
            case 483001:
               if (var5.equals("item.enchantment-glint")) {
                  var6 = 9;
               }
               break;
            case 3533310:
               if (var5.equals("slot")) {
                  var6 = 5;
               }
               break;
            case 110371416:
               if (var5.equals("title")) {
                  var6 = 4;
               }
               break;
            case 1130834266:
               if (var5.equals("item.hideDefaultLore")) {
                  var6 = 11;
               }
               break;
            case 1150971293:
               if (var5.equals("item.armor-trim.type")) {
                  var6 = 6;
               }
               break;
            case 1257901565:
               if (var5.equals("item.sub-section")) {
                  var6 = 12;
               }
               break;
            case 1568779582:
               if (var5.equals("display-item")) {
                  var6 = 13;
               }
               break;
            case 1923960397:
               if (var5.equals("item.armor-trim.pattern")) {
                  var6 = 7;
               }
               break;
            case 2108324081:
               if (var5.equals("item.lore")) {
                  var6 = 3;
               }
               break;
            case 2108370054:
               if (var5.equals("item.name")) {
                  var6 = 1;
               }
            }

            switch(var6) {
            case 0:
               return this.methods.getMaterial(logger, this.value.toString()) != null;
            case 1:
            case 2:
            case 3:
            case 4:
               return true;
            case 5:
               this.value = this.methods.getMainMenuSlot(logger, this.value.toString());
               return this.value != null;
            case 6:
               if (!((new ItemStack(mat)).getItemMeta() instanceof ArmorMeta)) {
                  SendMessage.warnMessage(logger, "To add a armor trim to an item, the material needs to be a armor piece.");
                  return false;
               }

               try {
                  (new CreateItemMethodes.ArmorTrim()).getTrimType(this.value.toString());
                  return true;
               } catch (ItemLoadException var16) {
                  SendMessage.warnMessage(logger, var16.getMessage());
                  return false;
               }
            case 7:
               if (!((new ItemStack(mat)).getItemMeta() instanceof ArmorMeta)) {
                  SendMessage.warnMessage(logger, "To add a armor trim to an item, the material needs to be a armor piece.");
                  return false;
               }

               try {
                  (new CreateItemMethodes.ArmorTrim()).getTrimPattern(this.value.toString());
                  return true;
               } catch (ItemLoadException var15) {
                  SendMessage.warnMessage(logger, var15.getMessage());
                  return false;
               }
            case 8:
               EcoType type = EconomyType.getFromString(this.value.toString());
               if (type == null) {
                  SendMessage.warnMessage(logger, "Could not find a economy type such as '" + this.value.toString() + "', valid values are: " + (String)Arrays.stream(EconomyType.values()).filter((t) -> {
                     return t != EconomyType.ITEM && t != EconomyType.LEVELS;
                  }).map(Enum::name).collect(Collectors.joining(",")));
                  return false;
               }

               return true;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
               this.value = Boolean.valueOf(this.value.toString());
               return true;
            case 16:
               try {
                  Color.fromRGB(Integer.parseInt(this.value.toString()));
                  return true;
               } catch (IllegalArgumentException var14) {
                  SendMessage.warnMessage(logger, Lang.RGB_COLOR_FORMATTED_WRONG.get().replace("%path%", "ShopSections." + this.section + "." + this.key));
                  return false;
               }
            case 17:
               try {
                  this.value = Integer.parseInt(this.value.toString());
               } catch (NumberFormatException var13) {
                  SendMessage.warnMessage(logger, Lang.NO_VALID_AMOUNT.get());
                  return false;
               }

               if (this.key.equalsIgnoreCase("item.stack-size") && !this.plugin.allowIllegalStacks && (Integer)this.value < mat.getMaxStackSize()) {
                  SendMessage.warnMessage(logger, "Cannot set the stack size of item %material% to %stack-size% because it would exceed the vanilla max stack size.".replace("%material%", mat.toString()).replace("%stack-size%", this.value.toString()));
                  return false;
               }

               return true;
            case 18:
               if (!PotionTypes.canHaveEffects(mat)) {
                  SendMessage.warnMessage(logger, Lang.MATERIAL_NEEDS_TO_BE_POTION.get());
                  return false;
               }

               Optional<PotionTypes> pot = PotionTypes.matchPotionType(this.value.toString());
               if (!pot.isPresent()) {
                  SendMessage.warnMessage(logger, Lang.ITEM_POTIONTYPE_NULL.get());
                  return false;
               }

               if (!((PotionTypes)pot.get()).isSupported()) {
                  SendMessage.warnMessage(logger, Lang.POTIONTYPE_NOT_SUPPORTED.get());
                  return false;
               }

               return true;
            case 19:
               if (mat != XMaterial.PLAYER_HEAD.parseMaterial()) {
                  SendMessage.warnMessage(logger, Lang.MATERIAL_NEEDS_TO_BE_SKULL.get());
                  return false;
               }

               return true;
            default:
               if (this.key.startsWith("pages")) {
                  String[] parts = this.key.split("\\.");
                  if (parts.length >= 2) {
                     String var10 = parts[2];
                     byte var11 = -1;
                     switch(var10.hashCode()) {
                     case -1364287701:
                        if (var10.equals("gui-rows")) {
                           var11 = 0;
                        }
                        break;
                     case 110371416:
                        if (var10.equals("title")) {
                           var11 = 1;
                        }
                     }

                     switch(var11) {
                     case 0:
                        try {
                           this.value = Integer.parseInt(this.value.toString());
                        } catch (NumberFormatException var17) {
                           SendMessage.warnMessage(logger, Lang.NO_VALID_AMOUNT.get());
                           return false;
                        }

                        if ((Integer)this.value >= 1 && (Integer)this.value <= 6) {
                           return true;
                        }

                        SendMessage.warnMessage(logger, "Invalid inventory rows amount, should be a number between 1-6");
                        return false;
                     case 1:
                        return true;
                     }
                  }
               }
            }
         }

         return true;
      }
   }

   private List<String> getAvailableKeys() {
      try {
         List<String> availableKeys = new ArrayList(this.allKeys);
         if (this.action.equalsIgnoreCase("add")) {
            availableKeys.removeAll(this.getDefined());
            availableKeys.addAll(this.listKeys);
            return availableKeys;
         }

         if (this.action.equalsIgnoreCase("remove")) {
            List<String> definedKeys = this.getDefined();
            definedKeys.addAll(this.getPageKeys());
            return definedKeys;
         }

         if (this.action.equalsIgnoreCase("set")) {
            List<String> l = new ArrayList(this.allKeys);
            l.addAll(this.getPageKeys());
            l.addAll(this.listKeys);
            return l;
         }
      } catch (NullPointerException var3) {
      }

      return new ArrayList();
   }

   private List<String> getDefined() {
      List<String> defined = new ArrayList(ConfigManager.getSection(this.section).getKeys(false));
      if (defined.contains("item")) {
         defined.addAll(ConfigManager.getSection(this.section).getConfigurationSection("item").getKeys(false));
      }

      return defined;
   }

   private List<String> getPageKeys() {
      List<String> keys = new ArrayList();
      Iterator var2 = this.plugin.getConfigManager().getPages(this.section, true).iterator();

      while(var2.hasNext()) {
         String page = (String)var2.next();
         keys.addAll(Arrays.asList("pages." + page + ".title", "pages." + page + ".gui-rows"));
      }

      return keys;
   }

   private List<String> getValues(String section, String key) {
      return ConfigManager.getSection(section).getStringList(key);
   }

   private void updateOption(Object logger) {
      SendMessage.sendMessage(logger, Lang.EDITSHOP_EDITING_SECTION.get());
      if (this.listKeys.contains(this.key)) {
         List values;
         if (this.action.equals("add")) {
            values = this.getValues(this.section, this.key);
            values.add(this.value.toString());
            this.update(values);
         } else if (this.action.equalsIgnoreCase("set")) {
            this.update(Collections.singletonList(this.value.toString()));
         } else if (this.action.equalsIgnoreCase("remove")) {
            values = this.getValues(this.section, this.key);
            if (values.size() == 1) {
               this.update((Object)null);
            } else if (values.size() >= 1) {
               values.remove(this.value.toString());
               this.update(values);
            }
         }
      } else {
         this.update(this.value);
      }

      SendMessage.sendMessage(logger, Lang.EDITSHOP_EDIT_ITEM_SUCCESSFUL.get().replace("%itemPath%", this.key));
      SendMessage.sendMessage(logger, Lang.EDITSHOP_RELOAD_SHOP_TO_SEE_CHANGES.get());
   }

   private <O> void update(O value) {
      if (this.key.startsWith("pages")) {
         ConfigManager.getShop(this.section).set(this.key, value);
         ConfigManager.saveShop(this.section);
      } else {
         ConfigManager.getSection(this.section).set(this.key, value);
         ConfigManager.saveSection(this.section);
      }

   }
}
