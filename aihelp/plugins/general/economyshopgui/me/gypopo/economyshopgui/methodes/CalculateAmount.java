package me.gypopo.economyshopgui.methodes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.objects.User;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CalculateAmount {
   private static EconomyShopGUI plugin;

   public CalculateAmount(EconomyShopGUI esgui) {
      plugin = esgui;
   }

   public List<String> loadItemsInOrder(String section, String page, int size) {
      HashMap<Integer, String> staticItems = new HashMap();
      List<String> itemOrder = new ArrayList();
      String path = "pages." + page + ".items";
      Iterator var7 = ConfigManager.getShop(section).getConfigurationSection(path).getKeys(false).iterator();

      while(true) {
         String itemLoc;
         String item;
         do {
            if (!var7.hasNext()) {
               ArrayList toBeRemoved;
               Iterator var15;
               int i;
               if (!staticItems.isEmpty()) {
                  toBeRemoved = new ArrayList(staticItems.keySet());
                  Collections.sort(toBeRemoved);
                  var15 = toBeRemoved.iterator();

                  while(var15.hasNext()) {
                     i = (Integer)var15.next();

                     while(itemOrder.size() < i) {
                        itemOrder.add((Object)null);
                     }

                     itemOrder.add(i, (String)staticItems.get(i));
                  }
               }

               if (itemOrder.size() > size) {
                  toBeRemoved = new ArrayList();

                  for(int i = size; i < itemOrder.size(); ++i) {
                     item = (String)itemOrder.get(i);
                     toBeRemoved.add(i);
                     if (item != null) {
                        SendMessage.warnMessage("Cannot load item '" + item + "' from section " + section + " because it is out of range for page size " + size);
                     }
                  }

                  Collections.reverse(toBeRemoved);
                  var15 = toBeRemoved.iterator();

                  while(var15.hasNext()) {
                     i = (Integer)var15.next();
                     itemOrder.remove(i);
                  }
               } else {
                  while(itemOrder.size() < size) {
                     itemOrder.add((Object)null);
                  }
               }

               return itemOrder;
            }

            itemLoc = (String)var7.next();
         } while(ConfigManager.getShop(section).getBoolean(path + "." + itemLoc + ".hidden"));

         try {
            item = ConfigManager.getShop(section).getString(path + "." + itemLoc + ".slot");
            ArrayList<Integer> slots = this.getSlots(item);
            if (slots != null && slots.stream().allMatch((ix) -> {
               return ix >= 0;
            })) {
               Iterator var11 = slots.iterator();

               while(var11.hasNext()) {
                  int pos;
                  for(pos = (Integer)var11.next(); staticItems.containsKey(pos); ++pos) {
                  }

                  staticItems.put(pos, page + ".items." + itemLoc);
               }
            } else {
               itemOrder.add(page + ".items." + itemLoc);
            }
         } catch (NumberFormatException var13) {
            SendMessage.warnMessage("Invalid slot index for format '" + ConfigManager.getShop(section).getString(path + "." + itemLoc + ".slot") + "' for item '" + section + "." + itemLoc + "'");
         }
      }
   }

   public ArrayList<Integer> getSlots(String slots) throws NumberFormatException {
      if (slots == null) {
         return null;
      } else {
         try {
            return new ArrayList(Collections.singletonList(Integer.parseInt(slots) - 1));
         } catch (NumberFormatException var10) {
            ArrayList<Integer> pos = new ArrayList();
            slots = StringUtils.deleteWhitespace(slots);
            String[] var4 = slots.split(",");
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String index = var4[var6];
               String[] params = index.split("-");
               if (params.length == 2) {
                  for(int i = Integer.parseInt(params[0]) - 1; i < Integer.parseInt(params[1]); ++i) {
                     pos.add(i);
                  }
               } else if (params.length == 1) {
                  pos.add(Integer.parseInt(params[0]) - 1);
               }
            }

            if (!pos.isEmpty()) {
               return pos;
            } else {
               throw new NumberFormatException();
            }
         }
      }
   }

   public static double calculateDiscount(Player player, String section, double price) {
      double discount = 0.0D;
      Iterator var6 = plugin.getDiscounts(section).keySet().iterator();

      while(true) {
         double i;
         do {
            String group;
            do {
               if (!var6.hasNext()) {
                  return (new BigDecimal(price - price / 100.0D * discount)).setScale(2, RoundingMode.HALF_UP).doubleValue();
               }

               group = (String)var6.next();
            } while(!PermissionsCache.hasPermission(player, "EconomyShopGUI.discounts." + group));

            i = (Double)plugin.getDiscounts(section).get(group);
         } while(discount != 0.0D && !(discount < i));

         discount = i;
      }
   }

   public static double calculateMultiplier(Player player, String section, double price) {
      double multiplier = 0.0D;
      Iterator var6 = plugin.getMultipliers(section).keySet().iterator();

      while(true) {
         double i;
         do {
            String group;
            do {
               if (!var6.hasNext()) {
                  return (new BigDecimal(price + price / 100.0D * multiplier)).setScale(2, RoundingMode.HALF_UP).doubleValue();
               }

               group = (String)var6.next();
            } while(!PermissionsCache.hasPermission(player, "EconomyShopGUI.sell-multipliers." + group));

            i = (Double)plugin.getMultipliers(section).get(group);
         } while(multiplier != 0.0D && !(multiplier < i));

         multiplier = i;
      }
   }

   public static List<String> splitLongString(String string) {
      List<String> list = new ArrayList();
      String[] words = string.split(" ");
      String[] colorlessWords = ChatColor.stripColor(string).split(" ");
      int length = 0;
      StringBuilder line = new StringBuilder();

      for(int i = 0; i < colorlessWords.length; ++i) {
         String word = colorlessWords[i];
         length += word.length() + 1;
         if (length < 46 && !word.contains("\n")) {
            if (line.length() != 0) {
               line.append(" ");
            }

            line.append(words[i]);
         } else {
            String l = line.toString();
            list.add(l);
            length = word.length();
            line = new StringBuilder();
            if (!word.contains("\n")) {
               line.append(ChatColor.getLastColors(l)).append(words[i]);
            }
         }
      }

      list.add(line.toString());
      return list;
   }

   public static List<String> getMultipleItemLore(String... lore) {
      List<String> lines = new ArrayList();
      String[] var2 = lore;
      int var3 = lore.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String line = var2[var4];
         if (line.contains("\n")) {
            lines.addAll(Arrays.asList(line.split("\n")));
         } else {
            lines.add(line);
         }
      }

      return lines;
   }

   public Map<Integer, String> getMainMenuItemSlots() {
      int maxSize = plugin.mainMenuSize;
      if (plugin.navBar.isEnableMainNav()) {
         maxSize -= 9;
      }

      ArrayList<Integer> cache = new ArrayList();
      ArrayList<String> notSet = new ArrayList();
      Map<Integer, String> itemSlots = new HashMap();
      Iterator var5 = plugin.getShopSections().iterator();

      while(true) {
         label95:
         while(true) {
            String section;
            do {
               do {
                  if (!var5.hasNext()) {
                     var5 = notSet.iterator();

                     while(var5.hasNext()) {
                        section = (String)var5.next();

                        int place;
                        for(place = 0; cache.contains(place); ++place) {
                        }

                        if (place >= maxSize) {
                           SendMessage.warnMessage(Lang.CANNOT_DISPLAY_SHOPSECTION.get().replace("%shopsection%", section).replace("%maxInvSize%", String.valueOf(maxSize)));
                        } else {
                           itemSlots.put(place, section);
                           cache.add(place);
                           SendMessage.warnMessage("The place of shop " + section + " in the main shop-inventory has automatically been changed to '" + place + "' because it was already used or could not be found.");
                        }
                     }

                     return itemSlots;
                  }

                  section = (String)var5.next();
               } while(ConfigManager.getSection(section).getBoolean("hidden"));
            } while(ConfigManager.getSection(section).getBoolean("sub-section"));

            String value = ConfigManager.getSection(section).getString("slot");
            if (value == null) {
               notSet.add(section);
            } else {
               ArrayList slots;
               try {
                  slots = this.getSlots(value);
               } catch (NumberFormatException var12) {
                  notSet.add(section);
                  continue;
               }

               ArrayList<Integer> valid = new ArrayList();
               Iterator var10 = slots.iterator();

               while(true) {
                  while(true) {
                     while(true) {
                        while(true) {
                           if (!var10.hasNext()) {
                              continue label95;
                           }

                           Integer slot = (Integer)var10.next();
                           if (slot >= 0 && slot < maxSize) {
                              if (cache.contains(slot)) {
                                 if (slots.size() != 1 && !valid.isEmpty()) {
                                    SendMessage.errorMessage("Slot '%slot%' for shop %section% is already used, skipping slot...".replace("%section%", section).replace("%slot%", String.valueOf(slot)));
                                 } else {
                                    notSet.add(section);
                                 }
                              } else {
                                 cache.add(slot);
                                 itemSlots.put(slot, section);
                                 valid.add(slot);
                              }
                           } else if (slots.size() != 1 && !valid.isEmpty()) {
                              SendMessage.errorMessage("Slot '%slot%' for shop %section% out of bounds for 0-%maxInvSize%, skipping slot...".replace("%section%", section).replace("%slot%", String.valueOf(slot)).replace("%maxInvSize%", String.valueOf(maxSize)));
                           } else {
                              SendMessage.errorMessage(Lang.CANNOT_DISPLAY_SHOPSECTION.get().replace("%shopsection%", section).replace("%maxInvSize%", String.valueOf(maxSize)));
                              notSet.add(section);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static int getInvSlotsDef(User user, int def) {
      return plugin.resizeGUI && user.isBedrock() ? 54 : def;
   }

   public static int getInvSlots(User user, int keys) {
      return plugin.resizeGUI && user.isBedrock() ? 54 : getSlots(keys);
   }

   public static int getInvSlots(int size) {
      int[] arr = new int[]{1, 2, 3, 4, 5, 6};
      if (size != 1 && size != 2 && size != 3 && size != 4 && size != 5 && size != 6) {
         int idx = 0;
         int dist = Math.abs(arr[0] - size);

         for(int i = 1; i < arr.length; ++i) {
            int cdist = Math.abs(arr[i] - size);
            if (cdist < dist) {
               idx = i;
               dist = cdist;
            }
         }

         size = arr[idx];
      }

      return size * 9;
   }

   public static Integer getSlots(Integer keys) {
      byte slots;
      if (keys == 9) {
         slots = 18;
      } else if (keys < 9 && keys > 0) {
         slots = 18;
      } else if (keys == 18) {
         slots = 27;
      } else if (keys < 18 && keys > 9) {
         slots = 27;
      } else if (keys == 27) {
         slots = 36;
      } else if (keys < 27 && keys > 18) {
         slots = 36;
      } else if (keys == 36) {
         slots = 45;
      } else if (keys < 36 && keys > 27) {
         slots = 45;
      } else if (keys == 45) {
         slots = 54;
      } else if (keys < 45 && keys > 36) {
         slots = 54;
      } else {
         slots = 54;
      }

      return Integer.valueOf(slots);
   }
}
