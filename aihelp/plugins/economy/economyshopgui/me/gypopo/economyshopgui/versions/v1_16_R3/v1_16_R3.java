package me.gypopo.economyshopgui.versions.v1_16_R3;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.VersionHandler;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.PotionTypes;
import me.gypopo.economyshopgui.util.SkullUtil;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

public final class v1_16_R3 implements VersionHandler {
   public Object emptyList() {
      return new NBTTagList();
   }

   public String toNBT(String s) {
      return CraftChatMessage.fromStringToJSON(s, true);
   }

   public String getLoreAsNBT(ItemStack item) {
      String lore = CraftItemStack.asNMSCopy(new ItemStack(item)).getTag().getCompound("display").getList("Lore", 8).get(0).toString();
      return lore.substring(1).substring(0, lore.length() - 1);
   }

   public Object getNMSLore(ItemStack item) {
      net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(item));
      if (nmsStack.hasTag()) {
         NBTTagCompound compound = nmsStack.getTag();
         NBTTagCompound display = compound.hasKey("display") ? compound.getCompound("display") : new NBTTagCompound();
         return display.hasKey("Lore") ? display.getList("Lore", 8) : new NBTTagList();
      } else {
         return new NBTTagList();
      }
   }

   public ItemStack applyNMSLore(ItemStack item, Object lore) {
      net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(item));
      if (nmsStack.hasTag()) {
         NBTTagCompound compound = nmsStack.getTag();
         (compound.hasKey("display") ? compound.getCompound("display") : new NBTTagCompound()).set("Lore", (NBTTagList)lore);
      }

      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public void setItemLore(Object lore, String s, int i) {
      ((NBTTagList)lore).set(i, NBTTagString.a(s));
   }

   public void addItemLore(Object lore, String s, int i) {
      ((NBTTagList)lore).add(i, NBTTagString.a(s));
   }

   public void addItemLore(Object lore, String s) {
      ((NBTTagList)lore).add(NBTTagString.a(s));
   }

   public ItemStack setNBTString(ItemStack item, String key, String value) {
      net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
      compound.setString(key, value);
      nmsStack.setTag(compound);
      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public String getNBTString(ItemStack item, String key) {
      net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
      return compound.hasKey(key) ? compound.getString(key) : null;
   }

   public ItemStack setNBTInt(ItemStack item, String key, Integer value) {
      net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
      compound.setInt(key, value);
      nmsStack.setTag(compound);
      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public Integer getNBTInt(ItemStack item, String key) {
      net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
      return compound.hasKey(key) ? compound.getInt(key) : null;
   }

   private NBTTagCompound getESGUICompound(ItemStack item) {
      net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
      return compound.hasKey("ESGUI") ? compound.getCompound("ESGUI") : new NBTTagCompound();
   }

   private ItemStack setESGUICompound(ItemStack item, NBTTagCompound ESGUICompund) {
      net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
      compound.set("ESGUI", ESGUICompund);
      nmsStack.setTag(compound);
      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public ItemStack setPages(ItemStack comp, String curAllPages) {
      NBTTagCompound esgui = this.getESGUICompound(comp);
      esgui.setString("Pages", curAllPages);
      return this.setESGUICompound(comp, esgui);
   }

   public String getPages(ItemStack comp) {
      NBTTagCompound esgui = this.getESGUICompound(comp);
      return esgui.getString("Pages");
   }

   public String getTitle(Inventory inv) {
      return "";
   }

   public ItemStack setDisabledBackButton(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      esgui.setBoolean("disabledBackButton", true);
      return this.setESGUICompound(item, esgui);
   }

   public boolean isDisabledBackButton(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return esgui.hasKey("disabledBackButton") && esgui.getBoolean("disabledBackButton");
   }

   public ItemStack addNBTPrices(ItemStack item, String path) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = esgui.hasKey("Prices") ? esgui.getCompound("Prices") : new NBTTagCompound();
      prices.setDouble("buy", ConfigManager.getShop(path.split("\\.")[0]).getDouble(path + ".buy"));
      prices.setDouble("sell", ConfigManager.getShop(path.split("\\.")[0]).getDouble(path + ".sell"));
      esgui.set("Prices", prices);
      return this.setESGUICompound(item, esgui);
   }

   public double getBuyPrice(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = esgui.getCompound("Prices");
      return prices.getDouble("buy");
   }

   public double getSellPrice(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = esgui.getCompound("Prices");
      return prices.getDouble("sell");
   }

   public ItemStack setItemErrorToItem(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      esgui.setBoolean("ItemError", true);
      return this.setESGUICompound(item, esgui);
   }

   public ItemStack getItemInHand(Player p) {
      return p.getInventory().getItemInMainHand();
   }

   public ItemStack setSpawnerType(ItemStack item, String spawnertype) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      esgui.setString("SpawnerType", spawnertype);
      return this.setESGUICompound(item, esgui);
   }

   public ItemStack setPotionType(ItemStack item, PotionTypes type) {
      PotionMeta meta = (PotionMeta)item.getItemMeta();
      if (meta.getBasePotionData().getType().getEffectType() == null) {
         meta.setBasePotionData(new PotionData(type.parsePotionType(), type.isExtended(), type.isUpgraded()));
      } else {
         meta.addCustomEffect(new PotionEffect(type.parsePotionEffectType(), type.getDuration(), type.getAmplifier(), false, true, true), false);
      }

      item.setItemMeta(meta);
      return item;
   }

   public Optional<PotionTypes> getBasePotion(ItemStack item) {
      PotionMeta meta = (PotionMeta)item.getItemMeta();
      if (meta.getBasePotionData().getType().getEffectType() != null) {
         PotionData potion = meta.getBasePotionData();
         return PotionTypes.matchPotionDataLegacy(potion.getType(), potion.isExtended(), potion.isUpgraded());
      } else {
         return Optional.empty();
      }
   }

   public ItemStack setPathToItem(ItemStack item, String path) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      esgui.setString("PathToItem", path);
      return this.setESGUICompound(item, esgui);
   }

   public ItemStack getSpawnerToGive(EntityType spawnerentity) {
      ItemStack spawnertogive = EconomyShopGUI.getInstance().createItem.createSpawner(spawnerentity);
      NBTTagCompound esgui = this.getESGUICompound(spawnertogive);
      esgui.setString("SpawnerType", spawnerentity.toString());
      return this.setESGUICompound(spawnertogive, esgui);
   }

   public String getPathToItem(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return esgui.getString("PathToItem");
   }

   public Boolean hasItemError(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return !esgui.hasKey("ItemError") ? false : esgui.getBoolean("ItemError");
   }

   public String getSpawnerType(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return esgui.hasKey("SpawnerType") && !esgui.getString("SpawnerType").isEmpty() ? esgui.getString("SpawnerType") : null;
   }

   public boolean isSimilar(ItemStack item1, ItemStack item2, List<String> ignoredNBT) {
      net.minecraft.server.v1_16_R3.ItemStack nmsStack1 = CraftItemStack.asNMSCopy(new ItemStack(item1));
      net.minecraft.server.v1_16_R3.ItemStack nmsStack2 = CraftItemStack.asNMSCopy(new ItemStack(item2));
      if (!nmsStack1.hasTag() && !nmsStack2.hasTag()) {
         return item1.isSimilar(item2);
      } else {
         Iterator var6 = ignoredNBT.iterator();

         while(true) {
            String[] tags;
            NBTTagCompound compound2;
            label58:
            do {
               while(var6.hasNext()) {
                  String ignored = (String)var6.next();
                  tags = ignored.split("::");
                  if (tags.length >= 2) {
                     NBTTagCompound compound1 = nmsStack1.getTag();
                     if (compound1 != null) {
                        for(int i = 0; i < tags.length; ++i) {
                           compound1 = compound1.getCompound(tags[i]);
                           if (i == tags.length - 2) {
                              compound1.remove(tags[i + 1]);
                           }
                        }
                     }

                     compound2 = nmsStack2.getTag();
                     continue label58;
                  }

                  nmsStack1.removeTag(ignored);
                  nmsStack2.removeTag(ignored);
               }

               if (item1.getType().equals(Material.PLAYER_HEAD) && !ignoredNBT.contains("SkullOwner") && !this.isSimilarSkull(nmsStack1, nmsStack2)) {
                  return false;
               }

               return CraftItemStack.asBukkitCopy(nmsStack1).isSimilar(CraftItemStack.asBukkitCopy(nmsStack2));
            } while(compound2 == null);

            for(int i = 0; i < tags.length; ++i) {
               compound2 = compound2.getCompound(tags[i]);
               if (i == tags.length - 2) {
                  compound2.remove(tags[i + 1]);
               }
            }
         }
      }
   }

   private boolean isSimilarSkull(net.minecraft.server.v1_16_R3.ItemStack item1, net.minecraft.server.v1_16_R3.ItemStack item2) {
      if (item1.hasTag() && item2.hasTag()) {
         try {
            String value1 = null;
            String value2 = null;
            NBTTagList list1 = item1.getTag().getCompound("SkullOwner").getCompound("Properties").getList("textures", 10);
            if (list1 != null && !list1.isEmpty()) {
               for(int i = 0; i < list1.size(); ++i) {
                  NBTTagCompound cp = list1.getCompound(i);
                  if (cp.hasKey("Value")) {
                     value1 = cp.getString("Value");
                  }
               }

               if (value1 == null) {
                  return false;
               } else {
                  NBTTagList list2 = item2.getTag().getCompound("SkullOwner").getCompound("Properties").getList("textures", 10);
                  if (list2 != null && !list2.isEmpty()) {
                     for(int i = 0; i < list2.size(); ++i) {
                        NBTTagCompound cp = list2.getCompound(i);
                        if (cp.hasKey("Value")) {
                           value2 = cp.getString("Value");
                        }
                     }

                     if (value2 == null) {
                        return false;
                     } else {
                        boolean so = false;
                        if (item1.getTag().getCompound("SkullOwner").hasKey("Name") && item2.getTag().getCompound("SkullOwner").hasKey("Name") && item1.getTag().getCompound("SkullOwner").getString("Name").equals(item2.getTag().getCompound("SkullOwner").getString("Name"))) {
                           item1.getTag().remove("SkullOwner");
                           item2.getTag().remove("SkullOwner");
                           so = true;
                        }

                        return value1.equals(value2) || so && SkullUtil.getTextureURL(value1).equals(value2);
                     }
                  } else {
                     return false;
                  }
               }
            } else {
               return false;
            }
         } catch (NullPointerException var9) {
            SendMessage.errorMessage("Error occurred while comparing skulls");
            var9.printStackTrace();
            return false;
         }
      } else {
         return false;
      }
   }
}
