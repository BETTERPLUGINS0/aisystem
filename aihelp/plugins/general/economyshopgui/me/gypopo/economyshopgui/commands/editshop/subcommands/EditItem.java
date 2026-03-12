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
import me.gypopo.economyshopgui.objects.ActionItem;
import me.gypopo.economyshopgui.util.FireworkUtil;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.PotionTypes;
import me.gypopo.economyshopgui.util.XEnchantment;
import me.gypopo.economyshopgui.util.XMaterial;
import me.gypopo.economyshopgui.util.exceptions.ItemLoadException;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.util.StringUtil;

public class EditItem extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;
   private final List<String> allKeys = this.getAllKeys();
   private final List<String> actionKeys = Arrays.asList("material", "displayname", "name", "slot", "stack-size", "action", "skullowner", "armorcolor", "enchantment-glint", "stack-size");
   private final List<String> availableActions = this.getAvailableActions();
   String section;
   String itemLoc;
   String action;
   String key;
   Object value;
   private final List<String> listKeys = this.getListKeys();

   public EditItem(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
   }

   public String getName() {
      return "edititem";
   }

   public String getDescription() {
      return Lang.EDITSHOP_EDIT_ITEM_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_EDIT_ITEM_SUBCOMMAND_SYNTAX.get().getLegacy();
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
               this.itemLoc = this.methods.getItemLoc(logger, this.section, args[2]);
               if (this.itemLoc != null) {
                  if (args.length <= 3) {
                     SendMessage.sendMessage(logger, this.getSyntax());
                  } else {
                     boolean found = false;
                     Iterator var4 = this.availableActions.iterator();

                     while(var4.hasNext()) {
                        String action2 = (String)var4.next();
                        if (args[3].equalsIgnoreCase(action2)) {
                           this.action = action2;
                           found = true;
                           break;
                        }
                     }

                     if (!found) {
                        SendMessage.sendMessage(logger, Lang.NO_VALID_ACTION.get().replace("%action%", args[3]).replace("%actions%", Arrays.toString(this.availableActions.toArray())));
                     } else if (args.length > 4) {
                        this.key = args[4];
                        Stream var10000 = this.getAvailableKeys().stream();
                        String var10001 = this.key;
                        Objects.requireNonNull(var10001);
                        if (var10000.noneMatch(var10001::equalsIgnoreCase)) {
                           SendMessage.sendMessage(logger, Lang.NO_VALID_KEY.get().replace("%key%", args[4]).replace("%keys%", Arrays.toString(this.getAvailableKeys().toArray())));
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

                           if (args.length <= 5) {
                              SendMessage.sendMessage(logger, this.getSyntax());
                           } else {
                              if (args.length > 6) {
                                 StringBuilder value2 = new StringBuilder();

                                 for(int i = 5; i < args.length; ++i) {
                                    value2.append(args[i]).append(" ");
                                 }

                                 this.value = value2.substring(0, value2.length() - 1);
                              } else {
                                 this.value = args[5];
                              }

                              if (this.validate(logger)) {
                                 this.updateOption(logger);
                              }
                           }
                        }
                     } else {
                        SendMessage.sendMessage(logger, this.getSyntax());
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
         if (!this.isSection(args[1])) {
            return null;
         } else {
            if (!args[2].isEmpty()) {
               completions = new ArrayList();
               StringUtil.copyPartialMatches(args[2], this.plugin.getSection(args[1]).getItemLocs(), completions);
               Collections.sort(completions);
               return completions;
            }

            return this.plugin.getSection(args[1]).getItemLocs();
         }
      case 4:
         if (!args[3].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[3], this.getAvailableActions(), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.getAvailableActions();
      case 5:
         if (this.isSection(args[1]) && this.isItemLoc(args[2]) && this.isAction(args[3])) {
            if (!args[4].isEmpty()) {
               completions = new ArrayList();
               StringUtil.copyPartialMatches(args[4], this.getAvailableKeys(), completions);
               Collections.sort(completions);
               return completions;
            }

            return this.getAvailableKeys();
         }

         return null;
      case 6:
         if (!this.isKey(args[4])) {
            return null;
         } else {
            if (!args[5].isEmpty()) {
               completions = new ArrayList();
               StringUtil.copyPartialMatches(args[5], this.getTabcompletionForValue(args[1], args[2], args[3], args[4]), completions);
               Collections.sort(completions);
               return completions;
            }

            return this.getTabcompletionForValue(args[1], args[2], args[3], args[4]);
         }
      default:
         return null;
      }
   }

   private boolean isSection(String section2) {
      if (this.plugin.getShopSections().contains(section2)) {
         this.section = section2;
         return true;
      } else {
         return false;
      }
   }

   private boolean isItemLoc(String itemLoc2) {
      if (this.plugin.getSection(this.section).getItemLocs().contains(itemLoc2)) {
         this.itemLoc = itemLoc2;
         return true;
      } else {
         return false;
      }
   }

   private boolean isAction(String action2) {
      if (this.availableActions.contains(action2)) {
         this.action = action2;
         return true;
      } else {
         return false;
      }
   }

   private boolean isKey(String key2) {
      if (this.getAvailableKeys().contains(key2)) {
         this.key = key2;
         return true;
      } else {
         return false;
      }
   }

   private boolean validate(Object logger) {
      if (this.action.equalsIgnoreCase("add")) {
         if (!this.getAvailableKeys().contains(this.key)) {
            SendMessage.sendMessage(logger, Lang.KEY_ALREADY_DEFINED.get().replace("%key%", this.key));
            return false;
         }
      } else if (this.action.equalsIgnoreCase("remove") && !this.getAvailableKeys().contains(this.key)) {
         SendMessage.sendMessage(logger, Lang.KEY_IS_NOT_FOUND.get().replace("%key%", this.key));
         return false;
      }

      if ((this.action.equalsIgnoreCase("remove") || this.action.equalsIgnoreCase("add")) && !this.listKeys.contains(this.key) && this.key.equalsIgnoreCase("material")) {
         SendMessage.sendMessage(logger, Lang.ACTION_NOT_VALID_ON_KEY.get().replace("%action%", this.action).replace("%key%", this.key));
         return false;
      } else {
         if (this.plugin.getSection(this.section).isActionItem(this.section, this.itemLoc)) {
            ArrayList<String> keys = new ArrayList(this.actionKeys);
            keys.addAll(Arrays.asList("lore", "displaylore", "enchantments", "potiontypes", "item-flags"));
            if (keys.stream().noneMatch((s) -> {
               return s.equals(this.key);
            })) {
               SendMessage.sendMessage(logger, Lang.EDIT_KEY_UNAVAILABLE.get().replace("%key%", this.key));
               return false;
            }
         }

         if (this.value != null) {
            String materialName = ConfigManager.getShop(this.section).getString("pages." + this.itemLoc + ".material");
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
            case -2107943916:
               if (var5.equals("close-menu")) {
                  var6 = 14;
               }
               break;
            case -2099368046:
               if (var5.equals("armor-trim.pattern")) {
                  var6 = 35;
               }
               break;
            case -1993294460:
               if (var5.equals("armorcolor")) {
                  var6 = 19;
               }
               break;
            case -1992012396:
               if (var5.equals("duration")) {
                  var6 = 25;
               }
               break;
            case -1939336506:
               if (var5.equals("potiontypes")) {
                  var6 = 17;
               }
               break;
            case -1695540708:
               if (var5.equals("enchantments")) {
                  var6 = 9;
               }
               break;
            case -1544709512:
               if (var5.equals("armor-trim.type")) {
                  var6 = 34;
               }
               break;
            case -1472485214:
               if (var5.equals("spawnertype")) {
                  var6 = 18;
               }
               break;
            case -1422950858:
               if (var5.equals("action")) {
                  var6 = 36;
               }
               break;
            case -1414304275:
               if (var5.equals("min-sell")) {
                  var6 = 30;
               }
               break;
            case -1354842768:
               if (var5.equals("colors")) {
                  var6 = 22;
               }
               break;
            case -1217487446:
               if (var5.equals("hidden")) {
                  var6 = 12;
               }
               break;
            case -985903647:
               if (var5.equals("fade-colors")) {
                  var6 = 23;
               }
               break;
            case -884540988:
               if (var5.equals("ominous-strength")) {
                  var6 = 8;
               }
               break;
            case -771931656:
               if (var5.equals("flicker")) {
                  var6 = 15;
               }
               break;
            case -683491116:
               if (var5.equals("enchantment-glint")) {
                  var6 = 10;
               }
               break;
            case 97926:
               if (var5.equals("buy")) {
                  var6 = 5;
               }
               break;
            case 3327734:
               if (var5.equals("lore")) {
                  var6 = 3;
               }
               break;
            case 3373707:
               if (var5.equals("name")) {
                  var6 = 2;
               }
               break;
            case 3526482:
               if (var5.equals("sell")) {
                  var6 = 6;
               }
               break;
            case 3533310:
               if (var5.equals("slot")) {
                  var6 = 7;
               }
               break;
            case 109399969:
               if (var5.equals("shape")) {
                  var6 = 24;
               }
               break;
            case 110621190:
               if (var5.equals("trail")) {
                  var6 = 16;
               }
               break;
            case 299066663:
               if (var5.equals("material")) {
                  var6 = 0;
               }
               break;
            case 361892379:
               if (var5.equals("max-sell")) {
                  var6 = 28;
               }
               break;
            case 430232890:
               if (var5.equals("hidePricingLore")) {
                  var6 = 11;
               }
               break;
            case 842942109:
               if (var5.equals("max-buy")) {
                  var6 = 27;
               }
               break;
            case 922706969:
               if (var5.equals("prior-lore")) {
                  var6 = 13;
               }
               break;
            case 1061293430:
               if (var5.equals("skullowner")) {
                  var6 = 21;
               }
               break;
            case 1062740107:
               if (var5.equals("min-buy")) {
                  var6 = 29;
               }
               break;
            case 1082416293:
               if (var5.equals("recipes")) {
                  var6 = 20;
               }
               break;
            case 1395483623:
               if (var5.equals("instrument")) {
                  var6 = 31;
               }
               break;
            case 1531861451:
               if (var5.equals("stew-effect")) {
                  var6 = 33;
               }
               break;
            case 1715056312:
               if (var5.equals("displaylore")) {
                  var6 = 4;
               }
               break;
            case 1715102285:
               if (var5.equals("displayname")) {
                  var6 = 1;
               }
               break;
            case 1970241253:
               if (var5.equals("section")) {
                  var6 = 32;
               }
               break;
            case 1990300710:
               if (var5.equals("stack-size")) {
                  var6 = 26;
               }
            }

            int str;
            switch(var6) {
            case 0:
               if (!this.action.equalsIgnoreCase("remove") && !this.action.equalsIgnoreCase("add")) {
                  return this.methods.getMaterial(logger, this.value.toString()) != null;
               }

               SendMessage.sendMessage(logger, Lang.ACTION_NOT_VALID_ON_KEY.get().replace("%action%", this.action).replace("%key%", this.key));
               return false;
            case 1:
            case 2:
            case 3:
            case 4:
               return true;
            case 5:
            case 6:
               this.value = this.methods.getPrice(logger, this.value);
               return this.value != null;
            case 7:
               try {
                  str = Integer.parseInt(this.value.toString());
                  if (this.plugin.maxShopSize * 45 >= str && 1 <= str) {
                     this.value = str;
                     return true;
                  }

                  throw new NumberFormatException();
               } catch (NumberFormatException var23) {
                  SendMessage.sendMessage(logger, Lang.NO_VALID_SLOT_FOR_ITEM.get().replace("%slot%", this.value.toString()).replace("%maxShopSize%", String.valueOf(this.plugin.maxShopSize * 45)));
                  return false;
               }
            case 8:
               try {
                  str = Integer.parseInt(this.value.toString());
                  if (5 > str && 1 < str) {
                     this.value = str;
                     return true;
                  }

                  throw new NumberFormatException();
               } catch (NumberFormatException var22) {
                  SendMessage.sendMessage(logger, Lang.INVALID_OMINOUS_BOTTLE_STRENGTH.get().replace("%strength%", this.value.toString()));
                  return false;
               }
            case 9:
               String enchantmentName = this.value.toString().contains(":") ? this.value.toString().split(":")[0] : this.value.toString();
               Optional<XEnchantment> ench = XEnchantment.matchXEnchantment(enchantmentName);
               if (!ench.isPresent()) {
                  SendMessage.warnMessage(logger, Lang.ITEM_ENCHANTMENT_NULL.get());
                  return false;
               }

               if (((XEnchantment)ench.get()).parseEnchantment() == null) {
                  SendMessage.warnMessage(logger, Lang.ITEM_ENCHANTMENT_NOT_SUPPORTED.get());
                  return false;
               }

               try {
                  if (this.value.toString().contains(":")) {
                     Integer.parseInt(this.value.toString().split(":")[1]);
                  }

                  return true;
               } catch (NumberFormatException var21) {
                  SendMessage.warnMessage(logger, Lang.ITEM_ENCHANTMENT_STRENGTH_NULL.get());
                  return false;
               }
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
               this.value = Boolean.valueOf(this.value.toString());
               return true;
            case 15:
            case 16:
               if (ConfigManager.getShop(this.section).getStringList("pages." + this.itemLoc + ".colors").isEmpty()) {
                  SendMessage.sendMessage(logger, ChatColor.RED + "To apply a flicker, trail or firework shape you need to give the firework at least one color using the 'colors' option");
                  return false;
               }

               this.value = Boolean.valueOf(this.value.toString());
               return true;
            case 17:
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
            case 18:
               if (mat != XMaterial.SPAWNER.parseMaterial()) {
                  SendMessage.warnMessage(logger, Lang.MATERIAL_NEEDS_TO_BE_SPAWNER.get());
                  return false;
               }

               EntityType[] var26 = EntityType.values();
               int var11 = var26.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  EntityType entity = var26[var12];
                  if (this.value.toString().equalsIgnoreCase(entity.toString())) {
                     return true;
                  }
               }

               SendMessage.warnMessage(logger, Lang.ITEM_SPAWNERTYPE_NULL.get());
               return false;
            case 19:
               try {
                  Color.fromRGB(Integer.parseInt(this.value.toString()));
               } catch (IllegalArgumentException var20) {
                  SendMessage.warnMessage(logger, Lang.RGB_COLOR_FORMATTED_WRONG.get().replace("%path%", this.section + "." + this.itemLoc + "." + this.key));
               }

               return true;
            case 20:
               if (mat != XMaterial.KNOWLEDGE_BOOK.parseMaterial()) {
                  SendMessage.warnMessage(logger, Lang.MATERIAL_NEEDS_TO_BE_BOOK.get());
                  return false;
               }

               if (XMaterial.matchXMaterial(this.value.toString()).isPresent() && ((XMaterial)XMaterial.matchXMaterial(this.value.toString()).get()).parseMaterial() != null) {
                  return true;
               }

               SendMessage.warnMessage(logger, Lang.CANNOT_GET_RECIPE_MATERIAL.get());
               return false;
            case 21:
               if (mat != XMaterial.PLAYER_HEAD.parseMaterial()) {
                  SendMessage.warnMessage(logger, Lang.MATERIAL_NEEDS_TO_BE_SKULL.get());
                  return false;
               }

               return true;
            case 22:
            case 23:
               if (!((List)Arrays.stream(FireworkUtil.FireworkColor.values()).map((color) -> {
                  return color.name();
               }).collect(Collectors.toList())).contains(this.value.toString().toUpperCase(Locale.ENGLISH))) {
                  SendMessage.warnMessage(logger, "Could not get the color '" + this.value.toString() + "'");
                  return false;
               }

               return true;
            case 24:
               if (ConfigManager.getShop(this.section).getStringList("pages." + this.itemLoc + ".colors").isEmpty()) {
                  SendMessage.warnMessage(logger, "To apply a flicker, trail or firework shape you need to give the firework at least one color using the 'colors' option");
                  return false;
               }

               try {
                  Type.valueOf(this.value.toString().toUpperCase(Locale.ENGLISH));
                  return true;
               } catch (IllegalArgumentException var19) {
                  SendMessage.sendMessage(logger, "Could not get the firework shape '" + this.value.toString() + "'");
                  return false;
               }
            case 25:
               try {
                  Integer.parseInt(this.value.toString());
                  return true;
               } catch (NumberFormatException var18) {
                  SendMessage.warnMessage(logger, Lang.NO_VALID_AMOUNT.get());
                  return false;
               }
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
               try {
                  this.value = Integer.parseInt(this.value.toString());
               } catch (NumberFormatException var17) {
                  SendMessage.warnMessage(logger, Lang.NO_VALID_AMOUNT.get());
                  return false;
               }

               if (this.key.equals("stack-size") && !this.plugin.allowIllegalStacks) {
                  if ((Integer)this.value < mat.getMaxStackSize()) {
                     SendMessage.warnMessage(logger, "Cannot set the stack size of item %material% to %stack-size% because it would exceed the vanilla max stack size.".replace("%material%", mat.toString()).replace("%stack-size%", this.value.toString()));
                  }
               } else if (!this.key.equals("min-buy") && !this.key.equals("min-sell")) {
                  if (this.key.equals("max-buy") || this.key.equals("max-sell")) {
                     if (this.key.equals("max-buy")) {
                        if (this.plugin.getShopItem(this.section + "." + this.itemLoc).isMinBuy((Integer)this.value)) {
                           SendMessage.warnMessage(logger, "The " + this.key + " should be larger as the min-buy option of this item.");
                           return false;
                        }
                     } else if (this.key.equals("max-sell") && this.plugin.getShopItem(this.section + "." + this.itemLoc).isMinSell((Integer)this.value)) {
                        SendMessage.warnMessage(logger, "The " + this.key + " should be larger as the min-sell option of this item.");
                        return false;
                     }
                  }
               } else {
                  if ((Integer)this.value < 0) {
                     SendMessage.warnMessage(logger, "The " + this.key + " needs to be a positive value!");
                     return false;
                  }

                  if (this.key.equals("min-buy")) {
                     if ((Integer)this.value < this.plugin.getShopItem(this.section + "." + this.itemLoc).getStackSize()) {
                        SendMessage.warnMessage(logger, "The " + this.key + " should be larger as the stack size of this shop item.");
                        return false;
                     }

                     if (this.plugin.getShopItem(this.section + "." + this.itemLoc).isMaxBuy((Integer)this.value)) {
                        SendMessage.warnMessage(logger, "The " + this.key + " should be smaller as the max-buy option of this item.");
                        return false;
                     }
                  } else if (this.key.equals("min-sell") && this.plugin.getShopItem(this.section + "." + this.itemLoc).isMaxSell((Integer)this.value)) {
                     SendMessage.warnMessage(logger, "The " + this.key + " should be smaller as the max-sell option of this item.");
                     return false;
                  }
               }

               return true;
            case 31:
               if (mat != XMaterial.GOAT_HORN.parseMaterial()) {
                  SendMessage.warnMessage(logger, "To apply a horn sound type, the material needs to be a goat horn");
                  return false;
               }

               try {
                  CreateItemMethodes.Horn.setInstrument(new ItemStack(Material.GOAT_HORN), this.value.toString());
                  return true;
               } catch (ItemLoadException var16) {
                  SendMessage.warnMessage(logger, "Could not find a goat horn instrument like '" + this.value.toString() + "', valid values are:\n" + ChatColor.RED + CreateItemMethodes.Horn.getValues());
                  return false;
               }
            case 32:
               return this.plugin.getShopSections().contains(this.value.toString());
            case 33:
               if (mat != XMaterial.MUSHROOM_STEW.parseMaterial()) {
                  SendMessage.warnMessage(logger, "To add a stew effect to an item, the material needs to be a 'SUSPICIOUS_STEW'.");
                  return false;
               }

               CreateItemMethodes.Stew stew = CreateItemMethodes.Stew.get(this.value.toString());
               if (stew == null) {
                  SendMessage.warnMessage("Could not find a stew effect like '" + this.value.toString() + "', valid values are:\n" + ChatColor.RED + Arrays.stream(CreateItemMethodes.Stew.values()).map(Enum::name).collect(Collectors.toList()));
                  return false;
               }

               return true;
            case 34:
               if (!((new ItemStack(mat)).getItemMeta() instanceof ArmorMeta)) {
                  SendMessage.warnMessage(logger, "To add a armor trim to an item, the material needs to be a armor piece.");
                  return false;
               }

               try {
                  (new CreateItemMethodes.ArmorTrim()).getTrimType(this.value.toString());
                  return true;
               } catch (ItemLoadException var15) {
                  SendMessage.warnMessage(logger, var15.getMessage());
                  return false;
               }
            case 35:
               if (!((new ItemStack(mat)).getItemMeta() instanceof ArmorMeta)) {
                  SendMessage.warnMessage(logger, "To add a armor trim to an item, the material needs to be a armor piece.");
                  return false;
               }

               try {
                  (new CreateItemMethodes.ArmorTrim()).getTrimPattern(this.value.toString());
                  return true;
               } catch (ItemLoadException var14) {
                  SendMessage.warnMessage(logger, var14.getMessage());
                  return false;
               }
            case 36:
               if (Arrays.stream(ActionItem.Action.values()).noneMatch((a) -> {
                  return a.name().equalsIgnoreCase(this.value.toString());
               })) {
                  SendMessage.warnMessage(logger, Lang.INVALID_ITEM_ACTION.get().replace("%action%", this.value.toString()));
                  return false;
               }

               return true;
            }
         }

         return true;
      }
   }

   private List<String> getTabcompletionForValue(String section, String itemLoc, String action, String key) {
      if (this.isSection(section) && this.isItemLoc(itemLoc) && this.isAction(action)) {
         if (action.equalsIgnoreCase("remove") && this.listKeys.contains(key)) {
            return ConfigManager.getShop(this.section).getConfigurationSection("pages." + itemLoc).getStringList(key);
         } else if (action.equalsIgnoreCase("remove")) {
            List<String> values = new ArrayList();
            values.add(ConfigManager.getShop(this.section).get("pages." + itemLoc + "." + key, "").toString());
            return values;
         } else {
            byte var6 = -1;
            switch(key.hashCode()) {
            case -2099368046:
               if (key.equals("armor-trim.pattern")) {
                  var6 = 12;
               }
               break;
            case -1993294460:
               if (key.equals("armorcolor")) {
                  var6 = 10;
               }
               break;
            case -1992012396:
               if (key.equals("duration")) {
                  var6 = 29;
               }
               break;
            case -1939336506:
               if (key.equals("potiontypes")) {
                  var6 = 9;
               }
               break;
            case -1695540708:
               if (key.equals("enchantments")) {
                  var6 = 8;
               }
               break;
            case -1544709512:
               if (key.equals("armor-trim.type")) {
                  var6 = 11;
               }
               break;
            case -1472485214:
               if (key.equals("spawnertype")) {
                  var6 = 13;
               }
               break;
            case -1422950858:
               if (key.equals("action")) {
                  var6 = 34;
               }
               break;
            case -1414304275:
               if (key.equals("min-sell")) {
                  var6 = 25;
               }
               break;
            case -1354842768:
               if (key.equals("colors")) {
                  var6 = 26;
               }
               break;
            case -1217487446:
               if (key.equals("hidden")) {
                  var6 = 19;
               }
               break;
            case -985903647:
               if (key.equals("fade-colors")) {
                  var6 = 27;
               }
               break;
            case -884540988:
               if (key.equals("ominous-strength")) {
                  var6 = 21;
               }
               break;
            case -771931656:
               if (key.equals("flicker")) {
                  var6 = 17;
               }
               break;
            case -683491116:
               if (key.equals("enchantment-glint")) {
                  var6 = 16;
               }
               break;
            case 97926:
               if (key.equals("buy")) {
                  var6 = 6;
               }
               break;
            case 3327734:
               if (key.equals("lore")) {
                  var6 = 4;
               }
               break;
            case 3373707:
               if (key.equals("name")) {
                  var6 = 3;
               }
               break;
            case 3526482:
               if (key.equals("sell")) {
                  var6 = 7;
               }
               break;
            case 3533310:
               if (key.equals("slot")) {
                  var6 = 20;
               }
               break;
            case 109399969:
               if (key.equals("shape")) {
                  var6 = 28;
               }
               break;
            case 110621190:
               if (key.equals("trail")) {
                  var6 = 18;
               }
               break;
            case 299066663:
               if (key.equals("material")) {
                  var6 = 0;
               }
               break;
            case 361892379:
               if (key.equals("max-sell")) {
                  var6 = 23;
               }
               break;
            case 430232890:
               if (key.equals("hidePricingLore")) {
                  var6 = 15;
               }
               break;
            case 842942109:
               if (key.equals("max-buy")) {
                  var6 = 22;
               }
               break;
            case 1061293430:
               if (key.equals("skullowner")) {
                  var6 = 14;
               }
               break;
            case 1062740107:
               if (key.equals("min-buy")) {
                  var6 = 24;
               }
               break;
            case 1082416293:
               if (key.equals("recipes")) {
                  var6 = 1;
               }
               break;
            case 1395483623:
               if (key.equals("instrument")) {
                  var6 = 31;
               }
               break;
            case 1531861451:
               if (key.equals("stew-effect")) {
                  var6 = 33;
               }
               break;
            case 1715056312:
               if (key.equals("displaylore")) {
                  var6 = 5;
               }
               break;
            case 1715102285:
               if (key.equals("displayname")) {
                  var6 = 2;
               }
               break;
            case 1970241253:
               if (key.equals("section")) {
                  var6 = 32;
               }
               break;
            case 1990300710:
               if (key.equals("stack-size")) {
                  var6 = 30;
               }
            }

            switch(var6) {
            case 0:
            case 1:
               return this.plugin.getSupportedMatNames();
            case 2:
            case 3:
            case 4:
            case 5:
               return this.methods.getExampleItemNames();
            case 6:
            case 7:
               return this.plugin.getExamplePrices();
            case 8:
               return XEnchantment.getNames();
            case 9:
               return PotionTypes.getNames();
            case 10:
               return this.methods.getExampleRGBColors();
            case 11:
               return (new CreateItemMethodes.ArmorTrim()).getTypes();
            case 12:
               return (new CreateItemMethodes.ArmorTrim()).getPatterns();
            case 13:
               return this.getEntityNames();
            case 14:
               return this.getExamplePlayerNames();
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
               return Arrays.asList("true", "false");
            case 20:
               return Arrays.asList("1", "9", "73", "108");
            case 21:
               return Arrays.asList("1", "2", "3", "4", "5");
            case 22:
            case 23:
            case 24:
            case 25:
               return Arrays.asList("1", "2", "8", "16", "32", "64", "128");
            case 26:
            case 27:
               return (List)Arrays.stream(FireworkUtil.FireworkColor.values()).map((color) -> {
                  return color.name();
               }).collect(Collectors.toList());
            case 28:
               return (List)Arrays.stream(Type.values()).map((type) -> {
                  return type.name();
               }).collect(Collectors.toList());
            case 29:
               return Arrays.asList("1", "2", "3");
            case 30:
               List<String> array = new ArrayList();

               for(int i = 1; i < 64; ++i) {
                  array.add(String.valueOf(i));
               }

               return array;
            case 31:
               return CreateItemMethodes.Horn.getValues();
            case 32:
               return this.plugin.getShopSections();
            case 33:
               return (List)Arrays.stream(CreateItemMethodes.Stew.values()).map(Enum::name).collect(Collectors.toList());
            case 34:
               return (List)Arrays.stream(ActionItem.Action.values()).map(Enum::name).collect(Collectors.toList());
            default:
               return new ArrayList();
            }
         }
      } else {
         return new ArrayList();
      }
   }

   private List<String> getAvailableKeys() {
      try {
         List<String> availableKeys = new ArrayList(this.plugin.getSection(this.section).isActionItem(this.section, this.itemLoc) ? this.actionKeys : this.allKeys);
         List<String> definedKeys = new ArrayList(ConfigManager.getShop(this.section).getConfigurationSection("pages." + this.itemLoc).getKeys(false));
         if (this.action.equalsIgnoreCase("add")) {
            availableKeys.removeAll(definedKeys);
            availableKeys.addAll(this.plugin.getSection(this.section).isActionItem(this.section, this.itemLoc) ? Arrays.asList("lore", "displaylore", "enchantments", "potiontypes") : this.listKeys);
            return availableKeys;
         }

         if (this.action.equalsIgnoreCase("remove")) {
            return definedKeys;
         }

         if (this.action.equalsIgnoreCase("set")) {
            availableKeys.addAll(this.plugin.getSection(this.section).isActionItem(this.section, this.itemLoc) ? Arrays.asList("lore", "displaylore", "enchantments", "potiontypes") : this.listKeys);
            return availableKeys;
         }
      } catch (NullPointerException var3) {
      }

      return new ArrayList();
   }

   private List<String> getAllKeys() {
      List<String> allKeys = new ArrayList();
      allKeys.add("material");
      allKeys.add("buy");
      allKeys.add("sell");
      allKeys.add("name");
      allKeys.add("displayname");
      allKeys.add("hidePricingLore");
      allKeys.add("slot");
      allKeys.add("stack-size");
      allKeys.add("enchantment-glint");
      allKeys.add("spawnertype");
      allKeys.add("skullowner");
      allKeys.add("armorcolor");
      allKeys.add("duration");
      allKeys.add("armor-trim.pattern");
      allKeys.add("armor-trim.type");
      allKeys.add("flicker");
      allKeys.add("trail");
      allKeys.add("shape");
      allKeys.add("instrument");
      allKeys.add("section");
      allKeys.add("stew-effect");
      allKeys.add("ominous-strength");
      allKeys.add("hidden");
      allKeys.add("max-buy");
      allKeys.add("max-sell");
      allKeys.add("min-buy");
      allKeys.add("min-sell");
      allKeys.add("prior-lore");
      allKeys.add("close-menu");
      allKeys.add("action");
      return allKeys;
   }

   private List<String> getListKeys() {
      List<String> listKeys = new ArrayList();
      listKeys.add("enchantments");
      listKeys.add("potiontypes");
      listKeys.add("lore");
      listKeys.add("displaylore");
      listKeys.add("recipes");
      listKeys.add("colors");
      listKeys.add("fade-colors");
      return listKeys;
   }

   private List<String> getAvailableActions() {
      List<String> actions = new ArrayList();
      actions.add("add");
      actions.add("set");
      actions.add("remove");
      return actions;
   }

   private List<String> getExamplePlayerNames() {
      List<String> playerNames = new ArrayList();
      Iterator var2 = this.plugin.getServer().getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         playerNames.add(player.getName());
      }

      return playerNames;
   }

   private List<String> getEntityNames() {
      List<String> names = new ArrayList();
      EntityType[] var2 = EntityType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EntityType entity = var2[var4];
         if (entity.isAlive()) {
            try {
               EntityType.valueOf(entity.name());
               names.add(entity.name());
            } catch (IllegalArgumentException var7) {
            }
         }
      }

      return names;
   }

   private List<String> getValues(String section, String itemLoc, String key) {
      return ConfigManager.getShop(section).getStringList("pages." + itemLoc + "." + key);
   }

   private void updateOption(Object logger) {
      SendMessage.sendMessage(logger, Lang.EDITSHOP_EDITING_ITEM.get());
      String locateItem = "pages." + this.itemLoc;
      if (this.listKeys.contains(this.key)) {
         List values;
         if (this.action.equalsIgnoreCase("add")) {
            values = this.getValues(this.section, this.itemLoc, this.key);
            values.add(this.value.toString());
            ConfigManager.getShop(this.section).set(locateItem + "." + this.key, values);
         } else if (this.action.equalsIgnoreCase("set")) {
            ConfigManager.getShop(this.section).set(locateItem + "." + this.key, Collections.singletonList(this.value.toString()));
         } else if (this.action.equalsIgnoreCase("remove")) {
            values = this.getValues(this.section, this.itemLoc, this.key);
            if (values.size() == 1) {
               ConfigManager.getShop(this.section).set(locateItem + "." + this.key, (Object)null);
            } else if (values.size() >= 1) {
               values.remove(this.value.toString());
               ConfigManager.getShop(this.section).set(locateItem + "." + this.key, values);
            }
         }
      } else {
         ConfigManager.getShop(this.section).set(locateItem + "." + this.key, this.value);
      }

      ConfigManager.saveShop(this.section);
      SendMessage.sendMessage(logger, Lang.EDITSHOP_EDIT_ITEM_SUCCESSFUL.get().replace("%itemPath%", locateItem));
      SendMessage.sendMessage(logger, Lang.EDITSHOP_RELOAD_SHOP_TO_SEE_CHANGES.get());
   }
}
