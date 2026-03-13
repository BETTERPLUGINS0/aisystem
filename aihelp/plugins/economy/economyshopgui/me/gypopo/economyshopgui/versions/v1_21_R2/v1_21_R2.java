package me.gypopo.economyshopgui.versions.v1_21_R2;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.VersionHandler;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.JsonUtil;
import me.gypopo.economyshopgui.util.PotionTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.ChestLock;
import net.minecraft.world.food.FoodInfo;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.item.EnumItemRarity;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.component.DebugStickState;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.component.MapDecorations;
import net.minecraft.world.item.component.MapItemColor;
import net.minecraft.world.item.component.MapPostProcessing;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.component.SeededContainerLoot;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.component.UseRemainder;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.crafting.IRecipe;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.block.entity.TileEntityBeehive.c;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R2.util.CraftChatMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class v1_21_R2 implements VersionHandler {
   private static final Field CUSTOM_TAG = getFieldByType(CustomData.class, NBTTagCompound.class);
   private static final Field COMPONENT_MAP = getFieldByType(ItemStack.class, PatchedDataComponentMap.class);
   private static final Field DATA_MAP = getFieldByType(PatchedDataComponentMap.class, Reference2ObjectMap.class);
   private static final Field ITEM_ITEM = getFieldByType(ItemStack.class, Item.class);
   private static final Method ITEM_MATCHER;

   private static Method getMethodByParams(Class<?> clazz, Class<?> returnType, Class<?>... paramTypes) {
      Method[] var3 = clazz.getDeclaredMethods();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method method = var3[var5];
         if (method.getReturnType() == returnType && Arrays.equals(method.getParameterTypes(), paramTypes)) {
            return method;
         }
      }

      return null;
   }

   private static Field getFieldByType(Class<?> clazz, Class<?> fieldType) {
      Field[] var2 = clazz.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (field.getType().equals(fieldType)) {
            return field;
         }
      }

      return null;
   }

   public Object emptyList() {
      return new ArrayList();
   }

   public String toNBT(String s) {
      IChatBaseComponent component = CraftChatMessage.fromString(s, true)[0];
      return CraftChatMessage.toJSON(component);
   }

   public String getLoreAsNBT(org.bukkit.inventory.ItemStack item) {
      PatchedDataComponentMap compound = this.getC(CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(item)));
      String lore = CraftChatMessage.fromComponent((IChatBaseComponent)((ItemLore)this.getOrNull(compound, v1_21_R2.Components.lore)).a().get(0));
      return lore.substring(1).substring(0, lore.length() - 1);
   }

   public Object getNMSLore(org.bukkit.inventory.ItemStack item) {
      ItemStack nmsStack = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(item));
      PatchedDataComponentMap compound = this.getC(nmsStack);
      Object lore = ((ItemLore)this.getOrNull(compound, v1_21_R2.Components.lore)).a();
      return lore != null ? lore : new ArrayList();
   }

   public org.bukkit.inventory.ItemStack applyNMSLore(org.bukkit.inventory.ItemStack item, Object lore) {
      ItemStack nmsStack = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(item));
      if (this.hasTag(nmsStack)) {
         PatchedDataComponentMap compound = this.getC(nmsStack);
         this.set((PatchedDataComponentMap)compound, (DataComponentType)v1_21_R2.Components.lore, (Object)(new ItemLore((List)lore)));
      }

      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public void setItemLore(Object lore, String s, int i) {
      ((List)lore).set(i, CraftChatMessage.fromJSON(s));
   }

   public void addItemLore(Object lore, String s, int i) {
      ((List)lore).add(i, CraftChatMessage.fromJSON(s));
   }

   public void addItemLore(Object lore, String s) {
      ((List)lore).add(CraftChatMessage.fromJSON(s));
   }

   public org.bukkit.inventory.ItemStack setNBTString(org.bukkit.inventory.ItemStack item, String key, String value) {
      ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      PatchedDataComponentMap compound = this.getC(nmsStack);
      NBTTagCompound c;
      if (key.contains("::")) {
         c = this.getCustom(compound);
         NBTTagCompound inner = this.getC(c, key.split("::")[0]);
         this.set(inner, key.split("::")[1], value);
         this.set(c, key.split("::")[0], inner);
      } else {
         c = this.getCustom(compound);
         this.set(c, key, value);
      }

      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public String getNBTString(org.bukkit.inventory.ItemStack item, String key) {
      ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      PatchedDataComponentMap compound = this.getC(nmsStack);
      NBTTagCompound c;
      if (key.contains("::")) {
         c = this.getCustom(compound);
         NBTTagCompound inner = this.getC(c, key.split("::")[0]);
         if (this.hasKey(inner, key.split("::")[1])) {
            return this.getS(inner, key.split("::")[1]);
         }
      } else {
         c = this.getCustom(compound);
         if (this.hasKey(c, key)) {
            return this.getS(c, key);
         }
      }

      return null;
   }

   public org.bukkit.inventory.ItemStack setNBTInt(org.bukkit.inventory.ItemStack item, String key, Integer value) {
      ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      PatchedDataComponentMap compound = this.getC(nmsStack);
      NBTTagCompound c;
      if (key.contains("::")) {
         c = this.getCustom(compound);
         NBTTagCompound inner = this.getOrCreate(c, key.split("::")[0]);
         this.set(inner, key.split("::")[1], value);
         this.set(c, key.split("::")[0], inner);
      } else {
         c = this.getCustom(compound);
         this.set(c, key, value);
      }

      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public Integer getNBTInt(org.bukkit.inventory.ItemStack item, String key) {
      ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      PatchedDataComponentMap compound = this.getC(nmsStack);
      NBTTagCompound c;
      if (key.contains("::")) {
         c = this.getCustom(compound);
         NBTTagCompound inner = this.getC(c, key.split("::")[0]);
         if (this.hasKey(inner, key.split("::")[1])) {
            return this.getI(inner, key.split("::")[1]);
         }
      } else {
         c = this.getCustom(compound);
         if (this.hasKey(c, key)) {
            return this.getI(c, key);
         }
      }

      return null;
   }

   public org.bukkit.inventory.ItemStack setPages(org.bukkit.inventory.ItemStack comp, String curAllPages) {
      NBTTagCompound esgui = this.getESGUICompound(comp);
      this.set(esgui, "Pages", curAllPages);
      return this.setESGUICompound(comp, esgui);
   }

   public String getPages(org.bukkit.inventory.ItemStack comp) {
      NBTTagCompound esgui = this.getESGUICompound(comp);
      return this.getS(esgui, "Pages");
   }

   private NBTTagCompound getC(NBTTagCompound tag, String k) {
      return tag.p(k);
   }

   private NBTTagCompound getC(NBTTagList list, int i) {
      return list.a(i);
   }

   private PatchedDataComponentMap getC(ItemStack item) {
      try {
         return (PatchedDataComponentMap)COMPONENT_MAP.get(item);
      } catch (IllegalAccessException var3) {
         SendMessage.logDebugMessage("Failed to retrieve compounds from item");
         var3.printStackTrace();
         return null;
      }
   }

   private NBTTagCompound getCustom(PatchedDataComponentMap tag) {
      try {
         CustomData c = (CustomData)this.getOrNull(tag, DataComponents.b);
         if (c == null) {
            NBTTagCompound compound = new NBTTagCompound();

            try {
               this.addCustom(tag, compound);
            } catch (Exception var5) {
               SendMessage.errorMessage("Failed to create custom data NBT for item");
               var5.printStackTrace();
            }

            c = (CustomData)this.getOrNull(tag, DataComponents.b);
         }

         return (NBTTagCompound)CUSTOM_TAG.get(c);
      } catch (IllegalAccessException var6) {
         SendMessage.logDebugMessage("Failed to retrieve custom NBT from item");
         var6.printStackTrace();
         return null;
      }
   }

   private void addCustom(PatchedDataComponentMap comp, NBTTagCompound compound) throws Exception {
      Constructor<CustomData> cd = CustomData.class.getDeclaredConstructor(compound.getClass());
      cd.setAccessible(true);
      CustomData data = (CustomData)cd.newInstance(compound);
      this.set((PatchedDataComponentMap)comp, (DataComponentType)DataComponents.b, (Object)data);
   }

   private NBTTagCompound getCustom(CustomData data) throws IllegalAccessException {
      return (NBTTagCompound)CUSTOM_TAG.get(data);
   }

   private NBTTagCompound getCustom(Optional<?> data) throws IllegalAccessException {
      return data != null && data.isPresent() ? this.getCustom((CustomData)data.get()).i() : new NBTTagCompound();
   }

   private void removeCustom(PatchedDataComponentMap tag, String k) {
      NBTTagCompound compound = this.getCustom(tag);
      compound.r(k);
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

   private NBTTagList getOrCreate(NBTTagCompound tag, String k, int o) {
      return tag.e(k) ? tag.c(k, o) : new NBTTagList();
   }

   private NBTTagCompound getOrCreate(NBTTagCompound tag, String k) {
      return tag.e(k) ? tag.p(k) : new NBTTagCompound();
   }

   private boolean hasKey(NBTTagCompound comp, String k) {
      return comp.e(k);
   }

   private <T> T getOrNull(PatchedDataComponentMap tag, DataComponentType<? extends T> k) {
      return tag.a(k);
   }

   private boolean hasTag(ItemStack item) {
      return !item.f();
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

   private void remove(PatchedDataComponentMap tag, DataComponentType<?> k) {
      try {
         Reference2ObjectMap map = (Reference2ObjectMap)DATA_MAP.get(tag);
         map.remove(k);
      } catch (Exception var4) {
         SendMessage.errorMessage("Failed to remove value from data map");
         var4.printStackTrace();
      }

   }

   private void setAccessible(PatchedDataComponentMap tag) {
      try {
         Method SET_ACCESSIBLE = tag.getClass().getDeclaredMethod("h");
         SET_ACCESSIBLE.setAccessible(true);
         SET_ACCESSIBLE.invoke(tag);
      } catch (Exception var3) {
         SendMessage.errorMessage("Failed to make data map accessible");
         var3.printStackTrace();
      }

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

   private <T> T set(PatchedDataComponentMap comp, DataComponentType<? super T> k, T v) {
      return comp.b(k, v);
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

   private void set(ItemStack nmsStack, PatchedDataComponentMap comp) {
      try {
         COMPONENT_MAP.set(nmsStack, comp);
      } catch (IllegalAccessException var4) {
         SendMessage.logDebugMessage("Failed to retrieve compounds from item");
         var4.printStackTrace();
      }

   }

   private NBTTagCompound getESGUICompound(org.bukkit.inventory.ItemStack item) {
      ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      NBTTagCompound compound = this.getCustom(this.getC(nmsStack));
      return this.getOrCreate(compound, "ESGUI");
   }

   private org.bukkit.inventory.ItemStack setESGUICompound(org.bukkit.inventory.ItemStack item, NBTTagCompound ESGUICompund) {
      ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
      PatchedDataComponentMap map = this.getC(nmsStack);
      NBTTagCompound compound = this.getCustom(map);
      this.set(compound, "ESGUI", ESGUICompund);
      return CraftItemStack.asBukkitCopy(nmsStack);
   }

   public String getTitle(Inventory inv) {
      return "";
   }

   public org.bukkit.inventory.ItemStack setDisabledBackButton(org.bukkit.inventory.ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      this.set(esgui, "disabledBackButton", true);
      return this.setESGUICompound(item, esgui);
   }

   public boolean isDisabledBackButton(org.bukkit.inventory.ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return this.hasKey(esgui, "disabledBackButton") && this.getB(esgui, "disabledBackButton");
   }

   public org.bukkit.inventory.ItemStack addNBTPrices(org.bukkit.inventory.ItemStack item, String path) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = this.getOrCreate(esgui, "Prices");
      this.set(prices, "buy", ConfigManager.getShop(path.split("\\.")[0]).getDouble(path + ".buy"));
      this.set(prices, "sell", ConfigManager.getShop(path.split("\\.")[0]).getDouble(path + ".sell"));
      this.set(esgui, "Prices", prices);
      return this.setESGUICompound(item, esgui);
   }

   public double getBuyPrice(org.bukkit.inventory.ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = this.getOrCreate(esgui, "Prices");
      return this.getD(prices, "buy");
   }

   public double getSellPrice(org.bukkit.inventory.ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      NBTTagCompound prices = this.getOrCreate(esgui, "Prices");
      return this.getD(prices, "sell");
   }

   public org.bukkit.inventory.ItemStack setItemErrorToItem(org.bukkit.inventory.ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      this.set(esgui, "ItemError", true);
      return this.setESGUICompound(item, esgui);
   }

   public org.bukkit.inventory.ItemStack getItemInHand(Player p) {
      return p.getInventory().getItemInMainHand();
   }

   public org.bukkit.inventory.ItemStack setSpawnerType(org.bukkit.inventory.ItemStack item, String spawnertype) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      this.set(esgui, "SpawnerType", spawnertype);
      return this.setESGUICompound(item, esgui);
   }

   public org.bukkit.inventory.ItemStack setOminousStrength(org.bukkit.inventory.ItemStack item, Integer lvl) {
      ItemStack stack = CraftItemStack.asNMSCopy(item);
      this.set((PatchedDataComponentMap)this.getC(stack), (DataComponentType)v1_21_R2.Components.ominous_bottle_amplifier, (Object)(new OminousBottleAmplifier(lvl)));
      return CraftItemStack.asBukkitCopy(stack);
   }

   public org.bukkit.inventory.ItemStack setPotionType(org.bukkit.inventory.ItemStack item, PotionTypes type) {
      PotionMeta meta = (PotionMeta)item.getItemMeta();
      if (!meta.hasBasePotionType()) {
         meta.setBasePotionType(type.parsePotionType());
      } else {
         if (!type.hasEffect()) {
            return item;
         }

         meta.addCustomEffect(new PotionEffect(type.parsePotionEffectType(), type.getDuration(), type.getAmplifier(), false, true, true), false);
      }

      item.setItemMeta(meta);
      return item;
   }

   public Optional<PotionTypes> getBasePotion(org.bukkit.inventory.ItemStack item) {
      PotionMeta meta = (PotionMeta)item.getItemMeta();
      return meta.hasBasePotionType() && meta.getBasePotionType().getEffectType() != null ? Optional.of(PotionTypes.matchPotionType(meta.getBasePotionType())) : Optional.empty();
   }

   public org.bukkit.inventory.ItemStack setPathToItem(org.bukkit.inventory.ItemStack item, String path) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      this.set(esgui, "PathToItem", path);
      return this.setESGUICompound(item, esgui);
   }

   public org.bukkit.inventory.ItemStack getSpawnerToGive(EntityType spawnerentity) {
      org.bukkit.inventory.ItemStack spawnertogive = EconomyShopGUI.getInstance().createItem.createSpawner(spawnerentity);
      NBTTagCompound esgui = this.getESGUICompound(spawnertogive);
      this.set(esgui, "SpawnerType", spawnerentity.toString());
      return this.setESGUICompound(spawnertogive, esgui);
   }

   public String getPathToItem(org.bukkit.inventory.ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return this.getS(esgui, "PathToItem");
   }

   public Boolean hasItemError(org.bukkit.inventory.ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return !this.hasKey(esgui, "ItemError") ? false : this.getB(esgui, "ItemError");
   }

   public String getSpawnerType(org.bukkit.inventory.ItemStack item) {
      NBTTagCompound esgui = this.getESGUICompound(item);
      return this.hasKey(esgui, "SpawnerType") && !this.getS(esgui, "SpawnerType").isEmpty() ? this.getS(esgui, "SpawnerType") : null;
   }

   private void remove(NBTTagCompound tag, String k) {
      tag.r(k);
   }

   private void remove(NBTTagCompound compound, String k, String v) {
      try {
         NBTBase base = compound.c(k);
         if (base == null) {
            return;
         }

         switch(base.b()) {
         case 8:
            JSONObject o = JsonUtil.parseJson(this.strip(base.toString()));
            o.remove(v);
            String json = o.toJSONString();
            if (!json.equals("{}")) {
               this.set(compound, k, json);
            } else {
               compound.r(k);
            }
            break;
         case 9:
            ((NBTTagList)base).remove(0);
            if (((NBTTagList)base).isEmpty()) {
               compound.r(k);
            }
            break;
         case 10:
            this.remove((NBTTagCompound)base, v);
            if (((NBTTagCompound)base).g()) {
               compound.r(k);
            }
         }
      } catch (ParseException var7) {
         var7.printStackTrace();
      }

   }

   private String strip(String s) {
      if (s.startsWith("'") && s.endsWith("'")) {
         s = s.substring(1, s.length() - 1);
      }

      if (s.startsWith("\"") && s.endsWith("\"")) {
         s = s.substring(1, s.length() - 1);
      }

      return s;
   }

   private boolean matchCustom(NBTTagCompound custom1, NBTTagCompound custom2, List<String> ignoredNBT) {
      Iterator var4 = ignoredNBT.iterator();

      while(true) {
         while(true) {
            while(var4.hasNext()) {
               String ignored = (String)var4.next();
               String[] tags = ignored.split("::");
               if (tags.length >= 2) {
                  NBTTagCompound nested1 = custom1;

                  for(int i = 0; i < tags.length - 1; ++i) {
                     if (i == tags.length - 2) {
                        this.remove(nested1, tags[i], tags[i + 1]);
                        break;
                     }

                     nested1 = this.getC(nested1, tags[i]);
                  }

                  NBTTagCompound nested2 = custom2;

                  for(int i = 0; i < tags.length - 1; ++i) {
                     if (i == tags.length - 2) {
                        this.remove(nested2, tags[i], tags[i + 1]);
                        break;
                     }

                     nested2 = this.getC(nested2, tags[i]);
                  }
               } else {
                  try {
                     this.remove(custom1, tags[0]);
                     this.remove(custom2, tags[0]);
                  } catch (Exception var10) {
                  }
               }
            }

            if (!Objects.equals(custom1, custom2)) {
               return false;
            }

            return true;
         }
      }
   }

   public boolean isSimilar(org.bukkit.inventory.ItemStack item1, org.bukkit.inventory.ItemStack item2, List<String> ignoredNBT) {
      try {
         ItemStack nmsStack1 = CraftItemStack.asNMSCopy(item1);
         ItemStack nmsStack2 = CraftItemStack.asNMSCopy(item2);
         if (this.hasTag(nmsStack1) && this.hasTag(nmsStack2)) {
            Item item = (Item)ITEM_ITEM.get(nmsStack1);
            if (!(Boolean)ITEM_MATCHER.invoke(nmsStack2, item)) {
               return false;
            }

            PatchedDataComponentMap comp1 = this.getC(nmsStack1);
            PatchedDataComponentMap comp2 = this.getC(nmsStack2);
            Reference2ObjectMap<DataComponentType<?>, Optional<?>> data1 = (Reference2ObjectMap)DATA_MAP.get(comp1);
            Reference2ObjectMap<DataComponentType<?>, Optional<?>> data2 = (Reference2ObjectMap)DATA_MAP.get(comp2);
            Set<DataComponentType<?>> keys = new HashSet(data1.keySet());
            keys.addAll(data2.keySet());
            Iterator var12 = keys.iterator();

            while(var12.hasNext()) {
               DataComponentType<?> type = (DataComponentType)var12.next();
               String key = String.valueOf(type).replace("minecraft:", "");
               if (!ignoredNBT.contains(key)) {
                  if (key.equals("custom_data")) {
                     NBTTagCompound custom1 = this.getCustom((Optional)data1.get(type));
                     NBTTagCompound custom2 = this.getCustom((Optional)data2.get(type));
                     if (!this.matchCustom(custom1, custom2, (List)ignoredNBT.stream().filter((i) -> {
                        return i.startsWith("custom_data::");
                     }).map((i) -> {
                        return i.replace("custom_data::", "");
                     }).collect(Collectors.toList()))) {
                        return false;
                     }
                  } else if (!Objects.equals(data1.get(type), data2.get(type))) {
                     return false;
                  }
               }
            }

            return true;
         }
      } catch (Exception var17) {
         SendMessage.errorMessage("Failed to match NMS item");
         var17.printStackTrace();
      }

      return item1.isSimilar(item2);
   }

   static {
      ITEM_MATCHER = getMethodByParams(ItemStack.class, Boolean.TYPE, Item.class);

      try {
         ITEM_ITEM.setAccessible(true);
         ITEM_MATCHER.setAccessible(true);
         COMPONENT_MAP.setAccessible(true);
         DATA_MAP.setAccessible(true);
         CUSTOM_TAG.setAccessible(true);
      } catch (Exception var1) {
         var1.printStackTrace();
         throw new UnsupportedOperationException("Failed to initialize VersionHandler, this is a bug");
      }
   }

   private static final class Components {
      static final DataComponentType<CustomData> custom_data;
      static final DataComponentType<Integer> max_stack_size;
      static final DataComponentType<Integer> max_damage;
      static final DataComponentType<Integer> damage;
      static final DataComponentType<Unbreakable> unbreakable;
      static final DataComponentType<IChatBaseComponent> custom_name;
      static final DataComponentType<IChatBaseComponent> item_name;
      static final DataComponentType<MinecraftKey> item_model;
      static final DataComponentType<ItemLore> lore;
      static final DataComponentType<EnumItemRarity> rarity;
      static final DataComponentType<ItemEnchantments> enchantments;
      static final DataComponentType<AdventureModePredicate> can_place_on;
      static final DataComponentType<AdventureModePredicate> can_break;
      static final DataComponentType<ItemAttributeModifiers> attribute_modifiers;
      static final DataComponentType<CustomModelData> custom_model_data;
      static final DataComponentType<Unit> hide_additional_tooltip;
      static final DataComponentType<Unit> hide_tooltip;
      static final DataComponentType<Integer> repair_cost;
      static final DataComponentType<Unit> creative_slot_lock;
      static final DataComponentType<Boolean> enchantment_glint_override;
      static final DataComponentType<Unit> intangible_projectile;
      static final DataComponentType<FoodInfo> food;
      static final DataComponentType<Consumable> consumable;
      static final DataComponentType<UseRemainder> use_remainder;
      static final DataComponentType<UseCooldown> use_cooldown;
      static final DataComponentType<DamageResistant> damage_resistant;
      static final DataComponentType<Tool> tool;
      static final DataComponentType<Enchantable> enchantable;
      static final DataComponentType<Equippable> equippable;
      static final DataComponentType<Repairable> repairable;
      static final DataComponentType<Unit> glider;
      static final DataComponentType<MinecraftKey> tooltip_style;
      static final DataComponentType<DeathProtection> death_protection;
      static final DataComponentType<ItemEnchantments> stored_enchantments;
      static final DataComponentType<DyedItemColor> dyed_color;
      static final DataComponentType<MapItemColor> map_color;
      static final DataComponentType<MapId> map_id;
      static final DataComponentType<MapDecorations> map_decorations;
      static final DataComponentType<MapPostProcessing> map_post_processing;
      static final DataComponentType<ChargedProjectiles> charged_projectiles;
      static final DataComponentType<BundleContents> bundle_contents;
      static final DataComponentType<PotionContents> potion_contents;
      static final DataComponentType<SuspiciousStewEffects> suspicious_stew_effects;
      static final DataComponentType<WritableBookContent> writable_book_content;
      static final DataComponentType<WrittenBookContent> written_book_content;
      static final DataComponentType<ArmorTrim> trim;
      static final DataComponentType<DebugStickState> debug_stick_state;
      static final DataComponentType<CustomData> entity_data;
      static final DataComponentType<CustomData> bucket_entity_data;
      static final DataComponentType<CustomData> block_entity_data;
      static final DataComponentType<Holder<Instrument>> instrument;
      static final DataComponentType<OminousBottleAmplifier> ominous_bottle_amplifier;
      static final DataComponentType<JukeboxPlayable> jukebox_playable;
      static final DataComponentType<List<ResourceKey<IRecipe<?>>>> recipes;
      static final DataComponentType<LodestoneTracker> lodestone_tracker;
      static final DataComponentType<FireworkExplosion> firework_explosion;
      static final DataComponentType<Fireworks> fireworks;
      static final DataComponentType<ResolvableProfile> profile;
      static final DataComponentType<MinecraftKey> note_block_sound;
      static final DataComponentType<BannerPatternLayers> banner_patterns;
      static final DataComponentType<EnumColor> base_color;
      static final DataComponentType<PotDecorations> pot_decorations;
      static final DataComponentType<ItemContainerContents> container;
      static final DataComponentType<BlockItemStateProperties> block_state;
      static final DataComponentType<List<c>> bees;
      static final DataComponentType<ChestLock> lock;
      static final DataComponentType<SeededContainerLoot> container_loot;

      public static <T> DataComponentType<T> valueOf(String name, Class<T> clazz) throws Exception {
         Field tag = v1_21_R2.Components.class.getDeclaredField(name);
         tag.setAccessible(true);
         return (DataComponentType)tag.get((Object)null);
      }

      static {
         custom_data = DataComponents.b;
         max_stack_size = DataComponents.c;
         max_damage = DataComponents.d;
         damage = DataComponents.e;
         unbreakable = DataComponents.f;
         custom_name = DataComponents.g;
         item_name = DataComponents.h;
         item_model = DataComponents.i;
         lore = DataComponents.j;
         rarity = DataComponents.k;
         enchantments = DataComponents.l;
         can_place_on = DataComponents.m;
         can_break = DataComponents.n;
         attribute_modifiers = DataComponents.o;
         custom_model_data = DataComponents.p;
         hide_additional_tooltip = DataComponents.q;
         hide_tooltip = DataComponents.r;
         repair_cost = DataComponents.s;
         creative_slot_lock = DataComponents.t;
         enchantment_glint_override = DataComponents.u;
         intangible_projectile = DataComponents.v;
         food = DataComponents.w;
         consumable = DataComponents.x;
         use_remainder = DataComponents.y;
         use_cooldown = DataComponents.z;
         damage_resistant = DataComponents.A;
         tool = DataComponents.B;
         enchantable = DataComponents.C;
         equippable = DataComponents.D;
         repairable = DataComponents.E;
         glider = DataComponents.F;
         tooltip_style = DataComponents.G;
         death_protection = DataComponents.H;
         stored_enchantments = DataComponents.I;
         dyed_color = DataComponents.J;
         map_color = DataComponents.K;
         map_id = DataComponents.L;
         map_decorations = DataComponents.M;
         map_post_processing = DataComponents.N;
         charged_projectiles = DataComponents.O;
         bundle_contents = DataComponents.P;
         potion_contents = DataComponents.Q;
         suspicious_stew_effects = DataComponents.R;
         writable_book_content = DataComponents.S;
         written_book_content = DataComponents.T;
         trim = DataComponents.U;
         debug_stick_state = DataComponents.V;
         entity_data = DataComponents.W;
         bucket_entity_data = DataComponents.X;
         block_entity_data = DataComponents.Y;
         instrument = DataComponents.Z;
         ominous_bottle_amplifier = DataComponents.aa;
         jukebox_playable = DataComponents.ab;
         recipes = DataComponents.ac;
         lodestone_tracker = DataComponents.ad;
         firework_explosion = DataComponents.ae;
         fireworks = DataComponents.af;
         profile = DataComponents.ag;
         note_block_sound = DataComponents.ah;
         banner_patterns = DataComponents.ai;
         base_color = DataComponents.aj;
         pot_decorations = DataComponents.ak;
         container = DataComponents.al;
         block_state = DataComponents.am;
         bees = DataComponents.an;
         lock = DataComponents.ao;
         container_loot = DataComponents.ap;
      }
   }
}
