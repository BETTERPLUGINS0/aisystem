package me.gypopo.economyshopgui.versions.v1_20_R2;

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
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

public class v1_20_R2 implements VersionHandler {
   public Object emptyList() {
      return new NBTTagList();
   }

   public String toNBT(String s) {
      return CraftChatMessage.fromStringToJSON(s, true);
   }

   public String getLoreAsNBT(ItemStack item) {
      String lore = ((NBTBase)CraftItemStack.asNMSCopy(new ItemStack(item)).v().p("display").c("Lore", 8).get(0)).toString();
      return lore.substring(1).substring(0, lore.length() - 1);
   }

   public Object getNMSLore(ItemStack item) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(item));
      if (this.hasTag(nmsStack)) {
         NBTTagCompound compound = this.getC(nmsStack);
         NBTTagCompound display = this.getOrCreate(compound, "display");
         return this.getOrCreate(display, "Lore", 8);
      } else {
         return new NBTTagList();
      }
   }

   public ItemStack applyNMSLore(ItemStack item, Object lore) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(item));
      if (this.hasTag(nmsStack)) {
         NBTTagCompound compound = this.getC(nmsStack);
         if (!this.hasKey(compound, "display")) {
            this.set(compound, "display", new NBTTagCompound());
         }

         this.set(this.getOrCreate(compound, "display"), "Lore", (NBTTagList)lore);
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
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = this.getOrCreate(nmsStack);
      if (key.contains("::")) {
         NBTTagCompound c = this.getOrCreate(compound, key.split("::")[0]);
         this.set(c, key.split("::")[1], value);
         this.set(compound, key.split("::")[0], c);
      } else {
         this.set(compound, key, value);
      }

      this.set(nmsStack, compound);
      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public String getNBTString(ItemStack item, String key) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = this.getOrCreate(nmsStack);
      if (key.contains("::")) {
         NBTTagCompound c = this.getOrCreate(compound, key.split("::")[0]);
         if (this.hasKey(c, key.split("::")[1])) {
            return this.getS(c, key.split("::")[1]);
         }
      } else if (this.hasKey(compound, key)) {
         return this.getS(compound, key);
      }

      return null;
   }

   public ItemStack setNBTInt(ItemStack item, String key, Integer value) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = this.getOrCreate(nmsStack);
      if (key.contains("::")) {
         NBTTagCompound c = this.getOrCreate(compound, key.split("::")[0]);
         this.set(c, key.split("::")[1], value);
         this.set(compound, key.split("::")[0], c);
      } else {
         this.set(compound, key, value);
      }

      this.set(nmsStack, compound);
      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public Integer getNBTInt(ItemStack item, String key) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = this.getOrCreate(nmsStack);
      return this.hasKey(compound, key) ? this.getI(compound, key) : null;
   }

   public ItemStack setPages(ItemStack comp, String curAllPages) {
      NBTTagCompound esgui = this.getESGUICompound(comp);
      this.set(esgui, "Pages", curAllPages);
      return this.setESGUICompound(comp, esgui);
   }

   public String getPages(ItemStack comp) {
      NBTTagCompound esgui = this.getESGUICompound(comp);
      return this.getS(esgui, "Pages");
   }

   private NBTTagCompound getC(NBTTagCompound tag, String k) {
      return tag.p(k);
   }

   private NBTTagCompound getC(NBTTagList list, int i) {
      return list.a(i);
   }

   private NBTTagCompound getC(net.minecraft.world.item.ItemStack item) {
      return item.v();
   }

   private NBTTagList getL(NBTTagCompound tag, String k, int t) {
      return tag.c(k, t);
   }

   private String getS(NBTTagCompound tag, String k) {
      return tag.l(k);
   }

   private double getD(NBTTagCompound tag, String k) {
      return tag.k(k);
   }

   private boolean getB(NBTTagCompound tag, String k) {
      return tag.q(k);
   }

   private int getI(NBTTagCompound tag, String k) {
      return tag.h(k);
   }

   private NBTTagCompound getOrCreate(net.minecraft.world.item.ItemStack nmsStack) {
      return nmsStack.u() ? nmsStack.v() : new NBTTagCompound();
   }

   private NBTTagCompound getOrCreate(NBTTagCompound tag, String k) {
      return tag.e(k) ? tag.p(k) : new NBTTagCompound();
   }

   private NBTTagList getOrCreate(NBTTagCompound tag, String k, int o) {
      return tag.e(k) ? tag.c(k, o) : new NBTTagList();
   }

   private boolean hasKey(NBTTagCompound comp, String k) {
      return comp.e(k);
   }

   private boolean hasTag(net.minecraft.world.item.ItemStack nmsStack) {
      return nmsStack.u();
   }

   private String getString(NBTTagCompound comp, String k) {
      return comp.l(k);
   }

   private void setString(NBTTagCompound comp, String k, String v) {
      comp.a(k, v);
   }

   private void set(NBTTagCompound tag, String k, NBTTagList v) {
      tag.a(k, v);
   }

   private void remove(NBTTagCompound tag, String k) {
      tag.r(k);
   }

   private void remove(net.minecraft.world.item.ItemStack item, String k) {
      item.c(k);
   }

   private void set(NBTTagCompound tag, String k, boolean v) {
      tag.a(k, v);
   }

   private void set(NBTTagCompound tag, String k, double v) {
      tag.a(k, v);
   }

   private void set(NBTTagCompound comp, String k, NBTTagCompound v) {
      comp.a(k, v);
   }

   private void set(NBTTagCompound comp, String k, String v) {
      comp.a(k, v);
   }

   private void set(NBTTagCompound comp, String k, Integer v) {
      comp.a(k, v);
   }

   private void set(NBTTagCompound tag, NBTTagCompound comp) {
      tag.a(comp);
   }

   private void set(net.minecraft.world.item.ItemStack nmsStack, NBTTagCompound comp) {
      nmsStack.c(comp);
   }

   private NBTTagCompound getESGUICompound(ItemStack item) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = this.getOrCreate(nmsStack);
      return this.getOrCreate(compound, "ESGUI");
   }

   private ItemStack setESGUICompound(ItemStack item, NBTTagCompound ESGUICompund) {
      net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = this.getOrCreate(nmsStack);
      this.set(compound, "ESGUI", ESGUICompund);
      this.set(nmsStack, compound);
      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public String getTitle(Inventory inv) {
      return "";
   }

   public ItemStack setDisabledBackButton(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      this.set(esgui, "disabledBackButton", true);
      return this.setESGUICompound(item, esgui);
   }

   public boolean isDisabledBackButton(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return this.hasKey(esgui, "disabledBackButton") && this.getB(esgui, "disabledBackButton");
   }

   public ItemStack addNBTPrices(ItemStack item, String path) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = this.getOrCreate(esgui, "Prices");
      this.set(prices, "buy", ConfigManager.getShop(path.split("\\.")[0]).getDouble(path + ".buy"));
      this.set(prices, "sell", ConfigManager.getShop(path.split("\\.")[0]).getDouble(path + ".sell"));
      this.set(esgui, "Prices", prices);
      return this.setESGUICompound(item, esgui);
   }

   public double getBuyPrice(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = this.getOrCreate(esgui, "Prices");
      return this.getD(prices, "buy");
   }

   public double getSellPrice(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = this.getOrCreate(esgui, "Prices");
      return this.getD(prices, "sell");
   }

   public ItemStack setItemErrorToItem(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      this.set(esgui, "ItemError", true);
      return this.setESGUICompound(item, esgui);
   }

   public ItemStack getItemInHand(Player p) {
      return p.getInventory().getItemInMainHand();
   }

   public ItemStack setSpawnerType(ItemStack item, String spawnertype) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      this.set(esgui, "SpawnerType", spawnertype);
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
      this.set(esgui, "PathToItem", path);
      return this.setESGUICompound(item, esgui);
   }

   public ItemStack getSpawnerToGive(EntityType spawnerentity) {
      ItemStack spawnertogive = EconomyShopGUI.getInstance().createItem.createSpawner(spawnerentity);
      NBTTagCompound esgui = this.getESGUICompound(spawnertogive);
      this.set(esgui, "SpawnerType", spawnerentity.toString());
      return this.setESGUICompound(spawnertogive, esgui);
   }

   public String getPathToItem(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return this.getS(esgui, "PathToItem");
   }

   public Boolean hasItemError(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return !this.hasKey(esgui, "ItemError") ? false : this.getB(esgui, "ItemError");
   }

   public String getSpawnerType(ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return this.hasKey(esgui, "SpawnerType") && !this.getS(esgui, "SpawnerType").isEmpty() ? this.getS(esgui, "SpawnerType") : null;
   }

   public boolean isSimilar(ItemStack item1, ItemStack item2, List<String> ignoredNBT) {
      net.minecraft.world.item.ItemStack nmsStack1 = CraftItemStack.asNMSCopy(new ItemStack(item1));
      net.minecraft.world.item.ItemStack nmsStack2 = CraftItemStack.asNMSCopy(new ItemStack(item2));
      if (!this.hasTag(nmsStack1) && !this.hasTag(nmsStack2)) {
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
                     NBTTagCompound compound1 = this.getC(nmsStack1);
                     if (compound1 != null) {
                        for(int i = 0; i < tags.length; ++i) {
                           compound1 = this.getC(compound1, tags[i]);
                           if (i == tags.length - 2) {
                              this.remove(compound1, tags[i + 1]);
                           }
                        }
                     }

                     compound2 = this.getC(nmsStack2);
                     continue label58;
                  }

                  this.remove(nmsStack1, ignored);
                  this.remove(nmsStack2, ignored);
               }

               if (item1.getType().equals(Material.PLAYER_HEAD) && !ignoredNBT.contains("SkullOwner") && !this.isSimilarSkull(nmsStack1, nmsStack2)) {
                  return false;
               }

               return CraftItemStack.asBukkitCopy(nmsStack1).isSimilar(CraftItemStack.asBukkitCopy(nmsStack2));
            } while(compound2 == null);

            for(int i = 0; i < tags.length; ++i) {
               compound2 = this.getC(compound2, tags[i]);
               if (i == tags.length - 2) {
                  this.remove(compound2, tags[i + 1]);
               }
            }
         }
      }
   }

   private boolean isSimilarSkull(net.minecraft.world.item.ItemStack item1, net.minecraft.world.item.ItemStack item2) {
      if (this.hasTag(item1) && this.hasTag(item2)) {
         try {
            String value1 = null;
            String value2 = null;
            NBTTagList list1 = this.getL(this.getC(this.getC(this.getC(item1), "SkullOwner"), "Properties"), "textures", 10);
            if (list1 != null && !list1.isEmpty()) {
               for(int i = 0; i < list1.size(); ++i) {
                  NBTTagCompound cp = this.getC(list1, i);
                  if (this.hasKey(cp, "Value")) {
                     value1 = this.getS(cp, "Value");
                  }
               }

               if (value1 == null) {
                  return false;
               } else {
                  NBTTagList list2 = this.getL(this.getC(this.getC(this.getC(item2), "SkullOwner"), "Properties"), "textures", 10);
                  if (list2 != null && !list2.isEmpty()) {
                     for(int i = 0; i < list2.size(); ++i) {
                        NBTTagCompound cp = this.getC(list2, i);
                        if (this.hasKey(cp, "Value")) {
                           value2 = this.getS(cp, "Value");
                        }
                     }

                     if (value2 == null) {
                        return false;
                     } else {
                        boolean so = false;
                        if (this.hasKey(this.getC(this.getC(item1), "SkullOwner"), "Name") && this.hasKey(this.getC(this.getC(item2), "SkullOwner"), "Name") && this.getS(this.getC(this.getC(item1), "SkullOwner"), "Name").equalsIgnoreCase(this.getS(this.getC(this.getC(item2), "SkullOwner"), "Name"))) {
                           this.remove(this.getC(item1), "SkullOwner");
                           this.remove(this.getC(item2), "SkullOwner");
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
