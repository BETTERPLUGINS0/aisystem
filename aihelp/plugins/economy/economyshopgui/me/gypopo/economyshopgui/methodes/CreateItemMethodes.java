package me.gypopo.economyshopgui.methodes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.objects.DisplayItem;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.EntityTypes;
import me.gypopo.economyshopgui.util.FireworkUtil;
import me.gypopo.economyshopgui.util.PotionTypes;
import me.gypopo.economyshopgui.util.ServerInfo;
import me.gypopo.economyshopgui.util.SkullUtil;
import me.gypopo.economyshopgui.util.XEnchantment;
import me.gypopo.economyshopgui.util.XMaterial;
import me.gypopo.economyshopgui.util.exceptions.ItemLoadException;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CreateItemMethodes {
   public static ItemStack createItemMaterialFromString(String material) throws ItemLoadException {
      if (material != null && !material.isEmpty()) {
         Optional<XMaterial> mat = XMaterial.matchXMaterial(material);
         if (!mat.isPresent()) {
            throw new ItemLoadException(Lang.ITEM_MATERIAL_NULL.get().replace("%material%", material));
         } else {
            ItemStack item = ((XMaterial)mat.get()).parseItem();
            if (item == null) {
               throw new ItemLoadException(Lang.MATERIAL_NOT_SUPPORTED.get());
            } else {
               return item;
            }
         }
      } else {
         throw new ItemLoadException(Lang.NEED_ITEM_MATERIAL.get());
      }
   }

   public ItemStack setOption(DisplayItem displayItem, ItemStack item, ConfigurationSection itemConfig, String section, boolean displayableItem) throws ItemLoadException {
      if (itemConfig.contains("enchantments")) {
         item = this.setEnchantments(item, section, itemConfig);
      }

      ItemMeta meta;
      if (itemConfig.contains("spawnertype")) {
         if (item.getType() != XMaterial.SPAWNER.parseMaterial()) {
            throw new ItemLoadException(Lang.MATERIAL_NEEDS_TO_BE_SPAWNER.get());
         }

         item = this.addSpawnerType(displayableItem && displayItem instanceof ShopItem ? (ShopItem)displayItem : null, item, itemConfig.getString("spawnertype"), itemConfig.getString("name"));
      } else if (itemConfig.contains("potiontypes")) {
         boolean splash = itemConfig.getString("material").equalsIgnoreCase("SPLASH_POTION");
         item = this.setPotionTypes(item, section, itemConfig.getCurrentPath(), splash);
      } else if (itemConfig.contains("recipes")) {
         if (item.getType() != XMaterial.KNOWLEDGE_BOOK.parseMaterial()) {
            throw new ItemLoadException(Lang.MATERIAL_NEEDS_TO_BE_BOOK.get());
         }

         Iterator var8 = itemConfig.getStringList("recipes").iterator();

         while(var8.hasNext()) {
            String recipe = (String)var8.next();
            if (!recipe.isEmpty()) {
               item = this.addRecipe(item, new NamespacedKey("minecraft", this.getRecipeMaterial(recipe).toString().toLowerCase(Locale.ENGLISH)));
            }
         }
      } else if (itemConfig.contains("skullowner")) {
         if (item.getType() != XMaterial.PLAYER_HEAD.parseMaterial()) {
            throw new ItemLoadException(Lang.MATERIAL_NEEDS_TO_BE_SKULL.get());
         }

         SkullUtil.setSkullTexture((pr) -> {
            displayItem.updateSkullTexture(pr, !displayableItem);
         }, item, (SkullMeta)null, itemConfig.getString("skullowner"));
      } else if (item.getType().equals(XMaterial.FIREWORK_ROCKET.parseMaterial())) {
         item = FireworkUtil.addEffect(item, itemConfig);
      } else if (itemConfig.contains("instrument")) {
         if (EconomyShopGUI.getInstance().version < 119) {
            throw new ItemLoadException("The instrument item option is only supported in version 1.19 and above");
         }

         if (item.getType() != Material.GOAT_HORN) {
            throw new ItemLoadException("To apply a horn sound type, the material needs to be a goat horn");
         }

         item = CreateItemMethodes.Horn.setInstrument(item, itemConfig.getString("instrument"));
      } else if (itemConfig.contains("stew-effect")) {
         if (EconomyShopGUI.getInstance().version < 114) {
            throw new ItemLoadException("Effects on SuspiciousStews are only supported in version 1.14 and above");
         }

         if (!(item.getItemMeta() instanceof SuspiciousStewMeta)) {
            throw new ItemLoadException("To add a stew effect to an item, the material needs to be a 'SUSPICIOUS_STEW'.");
         }

         SuspiciousStewMeta meta = (SuspiciousStewMeta)item.getItemMeta();
         meta.addCustomEffect(this.getStewEffect(itemConfig.getString("stew-effect")), true);
         item.setItemMeta(meta);
      } else if (itemConfig.getBoolean("autosell")) {
         if (EconomyShopGUI.getInstance().version < 116) {
            throw new ItemLoadException("AutoSellChests are only supported on 1.16 and above");
         }

         if (Bukkit.getPluginManager().getPlugin("AutoSellChests") == null) {
            throw new ItemLoadException("AutoSellChests needs to be installed to create this item");
         }

         meta = item.getItemMeta();
         meta.getPersistentDataContainer().set(new NamespacedKey(Bukkit.getPluginManager().getPlugin("AutoSellChests"), "autosell"), PersistentDataType.INTEGER, 1);
         item.setItemMeta(meta);
      } else if (itemConfig.contains("armor-trim")) {
         if (EconomyShopGUI.getInstance().version < 120) {
            throw new ItemLoadException("ArmorTrim's are only supported in version 1.20 and above");
         }

         (new CreateItemMethodes.ArmorTrim(itemConfig.getString("armor-trim.type"), itemConfig.getString("armor-trim.pattern"))).build(item);
      } else if (itemConfig.contains("ominous-strength")) {
         item = this.setOminousStrength(item, itemConfig.getInt("ominous-strength"));
      }

      if (itemConfig.contains("armorcolor")) {
         if (!(item.getItemMeta() instanceof LeatherArmorMeta)) {
            throw new ItemLoadException(Lang.MATERIAL_NEEDS_TO_BE_LEATHER_ARMOR.get());
         }

         LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
         meta.setColor(this.getRGBColor(itemConfig.getString("armorcolor"), itemConfig.getCurrentPath() + ".armorcolor"));
         item.setItemMeta(meta);
      }

      if (itemConfig.contains("enchantment-glint") && itemConfig.getBoolean("enchantment-glint")) {
         meta = item.getItemMeta();
         if (ServerInfo.supportsComponents()) {
            meta.setEnchantmentGlintOverride(true);
         } else {
            meta.addEnchant(XEnchantment.WATER_WORKER.parseEnchantment(), 1, false);
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
         }

         item.setItemMeta(meta);
      }

      if (itemConfig.contains("stack-size")) {
         int stackSize = itemConfig.getInt("stack-size");
         if (stackSize < 1 || stackSize > 64) {
            throw new ItemLoadException("Cannot set the stack size to %stack-size%, it needs to be a number between 1 and 64.".replace("%stack-size%", String.valueOf(stackSize)));
         }

         if (!EconomyShopGUI.getInstance().allowIllegalStacks && stackSize > item.getMaxStackSize()) {
            throw new ItemLoadException("Cannot set the stack size of item %material% to %stack-size% because it would exceed the vanilla max stack size.".replace("%material%", item.getType().toString()).replace("%stack-size%", String.valueOf(stackSize)));
         }

         item.setAmount(stackSize);
      } else if (displayItem != null && displayItem instanceof ShopItem && ((ShopItem)displayItem).getStackSize() > 1) {
         item.setAmount(Math.max(Math.min(((ShopItem)displayItem).getStackSize(), 64), 1));
      }

      return item;
   }

   private PotionEffect getStewEffect(String effect) throws ItemLoadException {
      CreateItemMethodes.Stew stew = CreateItemMethodes.Stew.get(effect);
      if (stew == null) {
         throw new ItemLoadException("Could not find a stew effect with name/id '" + effect + "'");
      } else {
         return stew.parseEffect();
      }
   }

   private Material getRecipeMaterial(String recipe) throws ItemLoadException {
      if (!recipe.isEmpty()) {
         Optional<XMaterial> mat = XMaterial.matchXMaterial(recipe);
         if (!mat.isPresent()) {
            throw new ItemLoadException(Lang.CANNOT_GET_RECIPE_MATERIAL.get());
         } else {
            ItemStack item = ((XMaterial)mat.get()).parseItem();
            if (item == null) {
               throw new ItemLoadException(Lang.RECIPE_MATERIAL_NOT_SUPPORTED.get());
            } else {
               return item.getType();
            }
         }
      } else {
         throw new ItemLoadException(Lang.NEED_RECIPE_MATERIAL.get());
      }
   }

   private Color getRGBColor(String raw, String rgbPath) throws ItemLoadException {
      raw = raw.replace("#", "");
      if (raw.startsWith("0x")) {
         raw = raw.substring(2);
      }

      try {
         java.awt.Color c = java.awt.Color.decode("#" + raw);
         return Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue());
      } catch (IllegalArgumentException var4) {
         throw new ItemLoadException(Lang.RGB_COLOR_FORMATTED_WRONG.get().replace("%path%", rgbPath));
      }
   }

   private ItemStack setSkullOwner(ItemStack item, String skullOwner) throws ItemLoadException {
      if (!skullOwner.isEmpty()) {
         SkullMeta skullMeta = (SkullMeta)item.getItemMeta();
         skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(skullOwner));
         item.setItemMeta(skullMeta);
         return item;
      } else {
         throw new ItemLoadException(Lang.NEED_SKULLOWNER.get());
      }
   }

   private ItemStack addSpawnerType(@Nullable ShopItem shopItem, ItemStack item, String entityType, String spawnerName) throws ItemLoadException {
      if (entityType != null) {
         if (!entityType.isEmpty()) {
            Optional<EntityType> type = EntityTypes.matchEntityType(entityType);
            if (!type.isPresent()) {
               throw new ItemLoadException(Lang.ITEM_SPAWNERTYPE_NULL.get());
            } else {
               item = EconomyShopGUI.getInstance().getSpawnerManager().getProvider().setSpawnedType(shopItem, item, (EntityType)type.get());
               if (spawnerName != null && !spawnerName.isEmpty()) {
                  EconomyShopGUI.getInstance().addSpawnerName((EntityType)type.get(), Lang.fromConfig(spawnerName));
               }

               return item;
            }
         } else {
            throw new ItemLoadException(Lang.NEED_ITEM_SPAWNERTYPE.get());
         }
      } else {
         throw new ItemLoadException(Lang.ITEM_SPAWNERTYPE_NULL.get());
      }
   }

   public ItemStack setOminousStrength(ItemStack item, int lvl) throws ItemLoadException {
      if (item.getType() != Material.OMINOUS_BOTTLE) {
         throw new ItemLoadException(Lang.INVALID_MATERIAL_FOR_ITEM_OPTION.get().replace("%material%", "OMINOUS_BOTTLE").replace("%item-option%", "ominous-strength"));
      } else if (lvl >= 1 && lvl <= 5) {
         try {
            return (ItemStack)ServerInfo.invokeModernVersionMethod("setOminousStrength", item, lvl - 1);
         } catch (Exception var6) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            var6.printStackTrace(pw);
            throw new ItemLoadException(Lang.ERROR_SETTING_ITEM_OPTION.get().replace("%item-option%", "ominous-strength") + "\n" + sw.toString());
         }
      } else {
         throw new ItemLoadException(Lang.INVALID_OMINOUS_BOTTLE_STRENGTH.get().replace("%strength%", String.valueOf(lvl)));
      }
   }

   private ItemStack addRecipe(ItemStack item, NamespacedKey materialKey) {
      if (EconomyShopGUI.getInstance().version >= 112) {
         KnowledgeBookMeta meta = (KnowledgeBookMeta)item.getItemMeta();
         if (meta != null) {
            meta.addRecipe(new NamespacedKey[]{materialKey});
         }

         item.setItemMeta(meta);
      }

      return item;
   }

   public ItemStack setEnchantments(ItemStack item, String section, ConfigurationSection path) throws ItemLoadException {
      List<String> enchantments = path.getStringList(".enchantments");
      if (enchantments.isEmpty()) {
         return item;
      } else {
         Iterator var5 = enchantments.iterator();

         while(var5.hasNext()) {
            String enchantment = (String)var5.next();

            try {
               Optional<XEnchantment> ench = XEnchantment.matchXEnchantment(enchantment.contains(":") ? enchantment.split(":")[0] : enchantment);
               if (!ench.isPresent()) {
                  throw new ItemLoadException(Lang.ITEM_ENCHANTMENT_NULL.get());
               }

               Enchantment enchant = ((XEnchantment)ench.get()).parseEnchantment();
               if (enchant == null) {
                  throw new ItemLoadException(Lang.ITEM_ENCHANTMENT_NOT_SUPPORTED.get());
               }

               try {
                  int strength = enchantment.contains(":") ? Integer.parseInt(enchantment.split(":")[1]) : 1;
                  item = this.setEnchantment(item, enchant, strength);
               } catch (NumberFormatException var11) {
                  throw new ItemLoadException(Lang.ITEM_ENCHANTMENT_STRENGTH_NULL.get());
               }
            } catch (ItemLoadException var12) {
               if (enchantments.size() <= 1) {
                  throw var12;
               }

               SendMessage.warnMessage(var12.getMessage());
               if (section != null) {
                  if (path.getCurrentPath().contains("pages")) {
                     SendMessage.errorShops(section, path.getCurrentPath() + ".enchantments." + enchantment);
                  } else {
                     SendMessage.errorSections(section, path.getCurrentPath() + ".enchantments." + enchantment);
                  }
               } else {
                  SendMessage.errorItemConfig(path.getCurrentPath());
               }
            }
         }

         return item;
      }
   }

   private ItemStack setEnchantment(ItemStack item, Enchantment ench, int strength) {
      int maxLevel = ench.getMaxLevel();
      boolean enchantedBook = item.getType().equals(XMaterial.ENCHANTED_BOOK.parseMaterial());
      if (strength > maxLevel && !EconomyShopGUI.getInstance().allowUnsafeEnchants) {
         strength = maxLevel;
      }

      if (enchantedBook) {
         EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta)item.getItemMeta();
         enchantmentStorageMeta.addStoredEnchant(ench, strength, true);
         item.setItemMeta(enchantmentStorageMeta);
      } else {
         item.addUnsafeEnchantment(ench, strength);
      }

      return item;
   }

   private ItemStack setPotionTypes(ItemStack item, String section, String itemLoc, boolean splash) throws ItemLoadException {
      List<String> potionTypes = ConfigManager.getShop(section).getStringList(itemLoc + ".potiontypes");
      if (potionTypes.isEmpty()) {
         return item;
      } else {
         Iterator var6 = potionTypes.iterator();

         while(var6.hasNext()) {
            String potionType = (String)var6.next();

            try {
               if (!PotionTypes.canHaveEffects(item.getType())) {
                  throw new ItemLoadException(Lang.MATERIAL_NEEDS_TO_BE_POTION.get());
               }

               Optional<PotionTypes> type = PotionTypes.matchPotionType(potionType);
               if (!type.isPresent()) {
                  throw new ItemLoadException(Lang.ITEM_POTIONTYPE_NULL.get());
               }

               if (!((PotionTypes)type.get()).isSupported()) {
                  throw new ItemLoadException(Lang.POTIONTYPE_NOT_SUPPORTED.get());
               }

               ((PotionTypes)type.get()).addEffect(item, splash);
            } catch (ItemLoadException var9) {
               if (potionTypes.size() == 1) {
                  throw var9;
               }

               SendMessage.warnMessage(var9.getMessage());
               SendMessage.errorShops(section, itemLoc + ".potiontypes." + potionType);
            }
         }

         return item;
      }
   }

   public List<String> setDisplayLore(List<String> lore, ConfigurationSection itemConfig, @Nullable ShopItem shopItem) {
      List<String> l = new ArrayList();
      if (itemConfig.contains("displaylore")) {
         l = this.getLore(itemConfig.getStringList("displaylore"), itemConfig.getCurrentPath(), shopItem);
      } else if (itemConfig.contains("lore")) {
         l = this.getLore(itemConfig.getStringList("lore"), itemConfig.getCurrentPath(), shopItem);
      }

      if (shopItem != null && shopItem.isPriorLore()) {
         lore.addAll(0, (Collection)l);
      } else {
         lore.addAll((Collection)l);
      }

      return lore;
   }

   public List<String> setLore(List<String> lore, ConfigurationSection itemConfig, @Nullable ShopItem shopItem) {
      return itemConfig.contains("lore") ? this.getLore(itemConfig.getStringList("lore"), itemConfig.getCurrentPath(), shopItem) : lore;
   }

   private List<String> getLore(List<String> list, String itemPath, @Nullable ShopItem shopItem) {
      List<String> lore = new ArrayList();
      if (!list.isEmpty()) {
         list.forEach((s) -> {
            if (s.contains("%buyPrice%") && EconomyShopGUI.getInstance().createItem.getBaseBuyPrice(itemPath) != null) {
               s = s.replace("%buyPrice%", EconomyShopGUI.getInstance().formatPrice(shopItem != null ? shopItem.getEcoType() : null, EconomyShopGUI.getInstance().createItem.getBaseBuyPrice(itemPath)));
            }

            if (s.contains("%sellPrice%") && EconomyShopGUI.getInstance().createItem.getBaseSellPrice(itemPath) != null) {
               s = s.replace("%sellPrice%", EconomyShopGUI.getInstance().formatPrice(shopItem != null ? shopItem.getEcoType() : null, EconomyShopGUI.getInstance().createItem.getBaseSellPrice(itemPath)));
            }

            lore.add(ChatUtil.formatColors(s));
         });
      }

      return lore;
   }

   public CreateItemMethodes() {
      MarketplaceIntegration.s = "https://api.gpplugins.com:2096/val";
   }

   public ItemStack setName(ItemStack item, ConfigurationSection itemConfig) throws ItemLoadException {
      if (itemConfig.contains("name")) {
         String name = itemConfig.getString("name");
         if (name != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtil.formatColors(name));
            item.setItemMeta(meta);
            return item;
         } else {
            throw new ItemLoadException(Lang.ITEM_NAME_NULL.get());
         }
      } else {
         return item;
      }
   }

   public Translatable getDisplayname(ConfigurationSection itemConfig) throws ItemLoadException {
      if (itemConfig.contains("displayname")) {
         return Lang.fromConfig(itemConfig.getString("displayname"));
      } else {
         return itemConfig.contains("name") ? Lang.fromConfig(itemConfig.getString("name")) : null;
      }
   }

   public ItemStack setSectionItemOption(ItemStack item, String section, ConfigurationSection itemConfig) throws ItemLoadException {
      boolean splash;
      if (itemConfig.contains("potion-glow") && itemConfig.getBoolean("potion-glow")) {
         if (!PotionTypes.canHaveEffects(item.getType())) {
            throw new ItemLoadException(Lang.MATERIAL_NEEDS_TO_BE_POTION.get());
         }

         splash = XMaterial.matchXMaterial(itemConfig.getString("material")).get() == XMaterial.SPLASH_POTION;
         PotionTypes.HEALING.addEffect(item, splash);
      }

      if (itemConfig.contains("potiontypes")) {
         splash = itemConfig.getString("material").equalsIgnoreCase("SPLASH_POTION");
         this.setPotionTypes(item, section, itemConfig.getCurrentPath(), splash);
      }

      if (itemConfig.contains("armor-trim")) {
         if (EconomyShopGUI.getInstance().version < 120) {
            throw new ItemLoadException("ArmorTrim's are only supported in version 1.20 and above");
         }

         (new CreateItemMethodes.ArmorTrim(itemConfig.getString("armor-trim.type"), itemConfig.getString("armor-trim.pattern"))).build(item);
      }

      if (itemConfig.contains("enchantment-glint") && itemConfig.getBoolean("enchantment-glint")) {
         ItemMeta itemMeta = item.getItemMeta();
         if (ServerInfo.supportsComponents()) {
            itemMeta.setEnchantmentGlintOverride(true);
         } else {
            itemMeta.addEnchant(XEnchantment.WATER_WORKER.parseEnchantment(), 1, false);
         }

         item.setItemMeta(itemMeta);
      }

      if (itemConfig.contains("skullowner")) {
         if (item.getType() != XMaterial.PLAYER_HEAD.parseMaterial()) {
            throw new ItemLoadException(Lang.MATERIAL_NEEDS_TO_BE_SKULL.get());
         }

         SkullUtil.setSkullTexture(item, (SkullMeta)null, itemConfig.getString("skullowner"), true);
      }

      if (itemConfig.contains("armorcolor")) {
         if (!(item.getItemMeta() instanceof LeatherArmorMeta)) {
            throw new ItemLoadException(Lang.MATERIAL_NEEDS_TO_BE_LEATHER_ARMOR.get());
         }

         LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
         meta.setColor(this.getRGBColor(itemConfig.getString("armorcolor"), itemConfig.getCurrentPath() + ".armorcolor"));
         item.setItemMeta(meta);
      }

      if (itemConfig.contains("stack-size")) {
         int stackSize = itemConfig.getInt("stack-size");
         if (!EconomyShopGUI.getInstance().allowIllegalStacks && stackSize > item.getMaxStackSize()) {
            throw new ItemLoadException("Cannot set the stack size of item %material% to %stack-size% because it would exceed the vanilla max stack size.".replace("%material%", item.getType().toString()).replace("%stack-size%", String.valueOf(stackSize)));
         }

         item.setAmount(stackSize);
      }

      return item;
   }

   public static enum Horn {
      PONDER("ponder_goat_horn"),
      SING("sing_goat_horn"),
      SEEK("seek_goat_horn"),
      FEEL("feel_goat_horn"),
      ADMIRE("admire_goat_horn"),
      CALL("call_goat_horn"),
      YEARN("yearn_goat_horn"),
      DREAM("dream_goat_horn");

      private String key;

      private Horn(String param3) {
         this.key = key;
      }

      public String getKey() {
         return "minecraft:" + this.key;
      }

      public static CreateItemMethodes.Horn getFromString(String name) throws IllegalArgumentException {
         return valueOf(name.toUpperCase(Locale.ENGLISH));
      }

      public static ItemStack setInstrument(ItemStack item, String sound) throws ItemLoadException {
         if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_20_R2)) {
            sound = sound.toLowerCase(Locale.ROOT);
            MusicInstrument instrument = (MusicInstrument)Registry.INSTRUMENT.get(NamespacedKey.minecraft(sound + "_goat_horn"));
            if (instrument == null) {
               throw new ItemLoadException("Could not find a instrument called by that name '%instrument%'".replace("%instrument%", sound));
            }

            MusicInstrumentMeta meta = (MusicInstrumentMeta)item.getItemMeta();
            meta.setInstrument(instrument);
            item.setItemMeta(meta);
         } else {
            try {
               CreateItemMethodes.Horn type = getFromString(sound);
               item = new ItemStack(EconomyShopGUI.getInstance().versionHandler.setNBTString(item, "instrument", type.getKey()));
            } catch (IllegalArgumentException var4) {
               throw new ItemLoadException("Could not find a instrument called by that name '%instrument%'".replace("%instrument%", sound));
            }
         }

         return item;
      }

      public static List<String> getValues() {
         return ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_20_R2) ? (List)Registry.INSTRUMENT.stream().map((instr) -> {
            return instr.getKey().getKey().split("_goat_horn")[0];
         }).collect(Collectors.toList()) : (List)Arrays.stream(values()).map(Enum::name).collect(Collectors.toList());
      }

      // $FF: synthetic method
      private static CreateItemMethodes.Horn[] $values() {
         return new CreateItemMethodes.Horn[]{PONDER, SING, SEEK, FEEL, ADMIRE, CALL, YEARN, DREAM};
      }
   }

   public static class ArmorTrim {
      private final TrimMaterial type;
      private final TrimPattern pattern;

      public ArrayList<String> getTypes() {
         ArrayList<String> mats = new ArrayList();
         Field[] var2 = TrimMaterial.class.getDeclaredFields();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            if (Modifier.isStatic(field.getModifiers()) && field.getName().equals(field.getName().toUpperCase(Locale.ROOT))) {
               mats.add(field.getName());
            }
         }

         return mats;
      }

      public ArrayList<String> getPatterns() {
         ArrayList<String> patterns = new ArrayList();
         Field[] var2 = TrimPattern.class.getDeclaredFields();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            if (Modifier.isStatic(field.getModifiers()) && field.getName().equals(field.getName().toUpperCase(Locale.ROOT))) {
               patterns.add(field.getName().toUpperCase(Locale.ROOT));
            }
         }

         return patterns;
      }

      public ArmorTrim() {
         this.type = null;
         this.pattern = null;
      }

      public ArmorTrim(String type, String pattern) throws ItemLoadException {
         if (type != null && pattern != null) {
            this.type = this.getTrimType(type);
            this.pattern = this.getTrimPattern(pattern);
         } else {
            throw new ItemLoadException("To apply a armor trim, it is required to specify both trim material and trim pattern!");
         }
      }

      public TrimMaterial getTrimType(String name) throws ItemLoadException {
         try {
            Field material = TrimMaterial.class.getDeclaredField(name.toUpperCase(Locale.ROOT).replace(" ", "_"));
            material.setAccessible(true);
            return (TrimMaterial)material.get((Object)null);
         } catch (IllegalAccessException | NoSuchFieldException var3) {
            throw new ItemLoadException("Invalid armor trim type such as '" + name + "'. Valid values are: " + Arrays.toString(this.getTypes().toArray()));
         }
      }

      public TrimPattern getTrimPattern(String name) throws ItemLoadException {
         try {
            Field pattern = TrimPattern.class.getDeclaredField(name.toUpperCase(Locale.ROOT).replace(" ", "_"));
            pattern.setAccessible(true);
            return (TrimPattern)pattern.get((Object)null);
         } catch (IllegalAccessException | NoSuchFieldException var3) {
            throw new ItemLoadException("Invalid armor trim pattern such as '" + name + "'. Valid values are: " + Arrays.toString(this.getPatterns().toArray()));
         }
      }

      public ItemStack build(ItemStack stack) throws ItemLoadException {
         if (stack.getItemMeta() instanceof ArmorMeta) {
            ArmorMeta meta = (ArmorMeta)stack.getItemMeta();
            meta.setTrim(new org.bukkit.inventory.meta.trim.ArmorTrim(this.type, this.pattern));
            stack.setItemMeta(meta);
            return stack;
         } else {
            throw new ItemLoadException("To add a armor trim to an item, the material needs to be a armor piece.");
         }
      }
   }

   public static enum Stew {
      NIGHT_VISION(0, 100, new String[0]),
      JUMP(1, 120, new String[]{"JUMP_BOOST"}),
      WEAKNESS(2, 180, new String[0]),
      BLINDNESS(3, 160, new String[0]),
      POISON(4, 240, new String[0]),
      SATURATION(6, 12, new String[0]),
      FIRE_RESISTANCE(7, 80, new String[0]),
      REGENERATION(8, 160, new String[]{"REGEN"}),
      WITHER(9, 160, new String[0]);

      private final int id;
      private final int duration;
      private final String[] aliases;

      private Stew(int param3, int param4, String... param5) {
         this.id = id;
         this.duration = duration;
         this.aliases = aliases;
      }

      public static CreateItemMethodes.Stew get(String s) {
         CreateItemMethodes.Stew[] var1 = values();
         int var2 = var1.length;

         int var3;
         for(var3 = 0; var3 < var2; ++var3) {
            CreateItemMethodes.Stew stew = var1[var3];
            if (stew.name().equalsIgnoreCase(s.replace(" ", "_")) || stew.anyMatchAliases(s)) {
               return stew;
            }
         }

         try {
            int id = Integer.parseInt(s);
            CreateItemMethodes.Stew[] var8 = values();
            var3 = var8.length;

            for(int var9 = 0; var9 < var3; ++var9) {
               CreateItemMethodes.Stew stew = var8[var9];
               if (stew.id == id) {
                  return stew;
               }
            }
         } catch (NumberFormatException var6) {
         }

         return null;
      }

      private boolean anyMatchAliases(String name) {
         String[] var2 = this.aliases;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String alias = var2[var4];
            if (name.equalsIgnoreCase(alias.replace(" ", "_"))) {
               return true;
            }
         }

         return false;
      }

      public PotionEffect parseEffect() {
         try {
            PotionEffectType type = PotionEffectType.getByName(this.name());
            if (type == null) {
               type = ((PotionTypes)PotionTypes.matchPotionType(this.name()).get()).parsePotionEffectType();
            }

            return new PotionEffect(type, this.duration, 1);
         } catch (NullPointerException var2) {
            return null;
         }
      }

      // $FF: synthetic method
      private static CreateItemMethodes.Stew[] $values() {
         return new CreateItemMethodes.Stew[]{NIGHT_VISION, JUMP, WEAKNESS, BLINDNESS, POISON, SATURATION, FIRE_RESISTANCE, REGENERATION, WITHER};
      }
   }
}
