package me.gypopo.economyshopgui.versions.v1_19_R2;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.VersionHandler;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.PotionTypes;
import me.gypopo.economyshopgui.util.SkullUtil;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R2.util.CraftChatMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

public final class v1_19_R2 implements VersionHandler {
   public Object emptyList() {
      return new NBTTagList();
   }

   public String toNBT(String s) {
      return CraftChatMessage.fromStringToJSON(s, true);
   }

   public String getLoreAsNBT(ItemStack item) {
      String lore = ((NBTBase)CraftItemStack.asNMSCopy(new ItemStack(item)).u().p("display").c("Lore", 8).get(0)).toString();
      return lore.substring(1).substring(0, lore.length() - 1);
   }

   public Object getNMSLore(ItemStack item) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(item));
      if (nmsStack.t()) {
         NBTTagCompound compound = nmsStack.u();
         NBTTagCompound display = compound.e("display") ? compound.p("display") : new NBTTagCompound();
         return display.e("Lore") ? display.c("Lore", 8) : new NBTTagList();
      } else {
         return new NBTTagList();
      }
   }

   public ItemStack applyNMSLore(ItemStack item, Object lore) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(item));
      if (nmsStack.t()) {
         NBTTagCompound compound = nmsStack.u();
         (compound.e("display") ? compound.p("display") : new NBTTagCompound()).a("Lore", (NBTTagList)lore);
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

   public ItemStack setNBTInt(ItemStack item, String key, Integer value) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.t() ? nmsStack.u() : new NBTTagCompound();
      compound.a(key, value);
      nmsStack.c(compound);
      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public Integer getNBTInt(ItemStack item, String key) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.t() ? nmsStack.u() : new NBTTagCompound();
      return compound.e(key) ? compound.h(key) : null;
   }

   public ItemStack setNBTString(ItemStack item, String key, String value) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.t() ? nmsStack.u() : new NBTTagCompound();
      compound.a(key, value);
      nmsStack.c(compound);
      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public String getNBTString(ItemStack item, String key) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.t() ? nmsStack.u() : new NBTTagCompound();
      return compound.e(key) ? compound.l(key) : null;
   }

   public ItemStack setPages(ItemStack comp, String curAllPages) {
      NBTTagCompound esgui = this.getESGUICompound(comp);
      esgui.a("Pages", curAllPages);
      return this.setESGUICompound(comp, esgui);
   }

   public String getPages(ItemStack comp) {
      NBTTagCompound esgui = this.getESGUICompound(comp);
      return esgui.l("Pages");
   }

   private NBTTagCompound getESGUICompound(ItemStack item) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.t() ? nmsStack.u() : new NBTTagCompound();
      return compound.e("ESGUI") ? compound.p("ESGUI") : new NBTTagCompound();
   }

   private ItemStack setESGUICompound(ItemStack item, NBTTagCompound ESGUICompund) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = nmsStack.t() ? nmsStack.u() : new NBTTagCompound();
      compound.a("ESGUI", ESGUICompund);
      nmsStack.c(compound);
      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public String getTitle(Inventory inv) {
      return "";
   }

   public ItemStack setDisabledBackButton(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      esgui.a("disabledBackButton", true);
      return this.setESGUICompound(item, esgui);
   }

   public boolean isDisabledBackButton(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return esgui.e("disabledBackButton") && esgui.q("disabledBackButton");
   }

   public ItemStack addNBTPrices(ItemStack item, String path) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = esgui.e("Prices") ? esgui.p("Prices") : new NBTTagCompound();
      prices.a("buy", ConfigManager.getShop(path.split("\\.")[0]).getDouble(path + ".buy"));
      prices.a("sell", ConfigManager.getShop(path.split("\\.")[0]).getDouble(path + ".sell"));
      esgui.a("Prices", prices);
      return this.setESGUICompound(item, esgui);
   }

   public double getBuyPrice(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = esgui.p("Prices");
      return prices.k("buy");
   }

   public double getSellPrice(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = esgui.p("Prices");
      return prices.k("sell");
   }

   public ItemStack setItemErrorToItem(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      esgui.a("ItemError", true);
      return this.setESGUICompound(item, esgui);
   }

   public ItemStack getItemInHand(Player p) {
      return p.getInventory().getItemInMainHand();
   }

   public ItemStack setSpawnerType(ItemStack item, String spawnertype) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      esgui.a("SpawnerType", spawnertype);
      return this.setESGUICompound(item, esgui);
   }

   public ItemStack setPotionType(ItemStack item, PotionTypes type) {
      PotionMeta meta = (PotionMeta)item.getItemMeta();
      if (meta.getBasePotionData().getType().getEffectType() == null) {
         meta.setBasePotionData(new PotionData(type.parsePotionType(), type.isExtended(), type.isUpgraded()));
      } else {
         if (!type.hasEffect()) {
            return item;
         }

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
      esgui.a("PathToItem", path);
      return this.setESGUICompound(item, esgui);
   }

   public ItemStack getSpawnerToGive(EntityType spawnerentity) {
      ItemStack spawnertogive = EconomyShopGUI.getInstance().createItem.createSpawner(spawnerentity);
      NBTTagCompound esgui = this.getESGUICompound(spawnertogive);
      esgui.a("SpawnerType", spawnerentity.toString());
      return this.setESGUICompound(spawnertogive, esgui);
   }

   public String getPathToItem(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return esgui.l("PathToItem");
   }

   public Boolean hasItemError(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return !esgui.e("ItemError") ? false : esgui.q("ItemError");
   }

   public String getSpawnerType(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return esgui.e("SpawnerType") && !esgui.l("SpawnerType").isEmpty() ? esgui.l("SpawnerType") : null;
   }

   public boolean isSimilar(ItemStack item1, ItemStack item2, List<String> ignoredNBT) {
      net.minecraft.world.item.ItemStack nmsStack1 = CraftItemStack.asNMSCopy(new ItemStack(item1));
      net.minecraft.world.item.ItemStack nmsStack2 = CraftItemStack.asNMSCopy(new ItemStack(item2));
      if (!nmsStack1.t() && !nmsStack2.t()) {
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
                     NBTTagCompound compound1 = nmsStack1.u();
                     if (compound1 != null) {
                        for(int i = 0; i < tags.length; ++i) {
                           compound1 = compound1.p(tags[i]);
                           if (i == tags.length - 2) {
                              compound1.r(tags[i + 1]);
                           }
                        }
                     }

                     compound2 = nmsStack2.u();
                     continue label58;
                  }

                  nmsStack1.c(ignored);
                  nmsStack2.c(ignored);
               }

               if (item1.getType().equals(Material.PLAYER_HEAD) && !ignoredNBT.contains("SkullOwner") && !this.isSimilarSkull(nmsStack1, nmsStack2)) {
                  return false;
               }

               return CraftItemStack.asBukkitCopy(nmsStack1).isSimilar(CraftItemStack.asBukkitCopy(nmsStack2));
            } while(compound2 == null);

            for(int i = 0; i < tags.length; ++i) {
               compound2 = compound2.p(tags[i]);
               if (i == tags.length - 2) {
                  compound2.r(tags[i + 1]);
               }
            }
         }
      }
   }

   private boolean isSimilarSkull(net.minecraft.world.item.ItemStack item1, net.minecraft.world.item.ItemStack item2) {
      if (item1.t() && item2.t()) {
         try {
            String value1 = null;
            String value2 = null;
            NBTTagList list1 = item1.u().p("SkullOwner").p("Properties").c("textures", 10);
            if (list1 != null && !list1.isEmpty()) {
               for(int i = 0; i < list1.size(); ++i) {
                  NBTTagCompound cp = list1.a(i);
                  if (cp.e("Value")) {
                     value1 = cp.l("Value");
                  }
               }

               if (value1 == null) {
                  return false;
               } else {
                  NBTTagList list2 = item2.u().p("SkullOwner").p("Properties").c("textures", 10);
                  if (list2 != null && !list2.isEmpty()) {
                     for(int i = 0; i < list2.size(); ++i) {
                        NBTTagCompound cp = list2.a(i);
                        if (cp.e("Value")) {
                           value2 = cp.l("Value");
                        }
                     }

                     if (value2 == null) {
                        return false;
                     } else {
                        boolean so = false;
                        if (item1.u().p("SkullOwner").e("Name") && item2.u().p("SkullOwner").e("Name") && item1.u().p("SkullOwner").l("Name").equalsIgnoreCase(item2.u().p("SkullOwner").l("Name"))) {
                           item1.u().r("SkullOwner");
                           item2.u().r("SkullOwner");
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
         } catch (Exception var9) {
            SendMessage.errorMessage("Error occurred while comparing skulls");
            var9.printStackTrace();
            return false;
         }
      } else {
         return true;
      }
   }
}
