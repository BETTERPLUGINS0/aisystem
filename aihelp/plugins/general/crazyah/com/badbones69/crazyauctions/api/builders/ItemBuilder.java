package com.badbones69.crazyauctions.api.builders;

import com.badbones69.crazyauctions.CrazyAuctions;
import com.badbones69.crazyauctions.Methods;
import com.badbones69.crazyauctions.api.support.SkullCreator;
import io.th0rgal.oraxen.api.OraxenItems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import libs.com.ryderbelserion.vital.paper.api.enums.Support;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemBuilder {
   @NotNull
   private static final CrazyAuctions plugin = CrazyAuctions.get();
   private Material material;
   private ItemStack itemStack;
   private int itemAmount;
   private String itemData;
   private String displayName;
   private List<String> displayLore;
   private int itemDamage;
   private boolean hasCustomModelData;
   private int customModelData;
   private String customMaterial;
   private boolean isPotion;
   private Color potionColor;
   private PotionEffectType potionType;
   private int potionDuration;
   private int potionAmplifier;
   private boolean isHash;
   private boolean isURL;
   private boolean isHead;
   private String player;
   private boolean isTippedArrow;
   private boolean isLeatherArmor;
   private boolean isArmor;
   private TrimMaterial trimMaterial;
   private TrimPattern trimPattern;
   private Color armorColor;
   private boolean isBanner;
   private List<Pattern> patterns;
   private boolean isShield;
   private boolean isMap;
   private Color mapColor;
   private boolean isFirework;
   private boolean isFireworkStar;
   private Color fireworkColor;
   private List<Color> fireworkColors;
   private int fireworkPower;
   private boolean isUnbreakable;
   private boolean hideItemFlags;
   private List<ItemFlag> itemFlags;
   private boolean isGlowing;
   private boolean isSpawner;
   private EntityType entityType;
   private String crateName;
   private Player target;
   private Map<String, String> namePlaceholders;
   private Map<String, String> lorePlaceholders;
   private static final Map<String, Color> colors = createMap();

   public ItemBuilder(ItemBuilder itemBuilder) {
      this.material = Material.STONE;
      this.itemStack = null;
      this.itemAmount = 1;
      this.itemData = "";
      this.displayName = "";
      this.displayLore = new ArrayList();
      this.itemDamage = 0;
      this.hasCustomModelData = false;
      this.customModelData = 0;
      this.customMaterial = "";
      this.isPotion = false;
      this.potionColor = Color.RED;
      this.potionType = null;
      this.potionDuration = -1;
      this.potionAmplifier = 1;
      this.isHash = false;
      this.isURL = false;
      this.isHead = false;
      this.player = "";
      this.isTippedArrow = false;
      this.isLeatherArmor = false;
      this.isArmor = false;
      this.trimMaterial = null;
      this.trimPattern = null;
      this.armorColor = Color.RED;
      this.isBanner = false;
      this.patterns = new ArrayList();
      this.isShield = false;
      this.isMap = false;
      this.mapColor = Color.RED;
      this.isFirework = false;
      this.isFireworkStar = false;
      this.fireworkColor = Color.RED;
      this.fireworkColors = new ArrayList();
      this.fireworkPower = 1;
      this.isUnbreakable = false;
      this.hideItemFlags = false;
      this.itemFlags = new ArrayList();
      this.isGlowing = false;
      this.isSpawner = false;
      this.entityType = EntityType.BAT;
      this.crateName = "";
      this.target = null;
      this.namePlaceholders = new HashMap();
      this.lorePlaceholders = new HashMap();
      this.target = itemBuilder.target;
      this.material = itemBuilder.material;
      this.itemStack = itemBuilder.itemStack;
      this.customMaterial = itemBuilder.customMaterial;
      this.itemAmount = itemBuilder.itemAmount;
      this.itemData = itemBuilder.itemData;
      this.displayName = itemBuilder.displayName;
      this.displayLore = itemBuilder.displayLore;
      this.itemDamage = itemBuilder.itemDamage;
      this.hasCustomModelData = itemBuilder.hasCustomModelData;
      this.customModelData = itemBuilder.customModelData;
      this.isPotion = itemBuilder.isPotion;
      this.potionColor = itemBuilder.potionColor;
      this.potionType = itemBuilder.potionType;
      this.potionDuration = itemBuilder.potionDuration;
      this.potionAmplifier = itemBuilder.potionAmplifier;
      this.isHead = itemBuilder.isHead;
      this.isHash = itemBuilder.isHash;
      this.isURL = itemBuilder.isURL;
      this.player = itemBuilder.player;
      this.isTippedArrow = itemBuilder.isTippedArrow;
      this.isLeatherArmor = itemBuilder.isLeatherArmor;
      this.isArmor = itemBuilder.isArmor;
      this.trimMaterial = itemBuilder.trimMaterial;
      this.trimPattern = itemBuilder.trimPattern;
      this.armorColor = itemBuilder.armorColor;
      this.isBanner = itemBuilder.isBanner;
      this.patterns = itemBuilder.patterns;
      this.isShield = itemBuilder.isShield;
      this.isMap = itemBuilder.isMap;
      this.mapColor = itemBuilder.mapColor;
      this.isFirework = itemBuilder.isFirework;
      this.isFireworkStar = itemBuilder.isFireworkStar;
      this.fireworkColor = itemBuilder.fireworkColor;
      this.fireworkColors = itemBuilder.fireworkColors;
      this.fireworkPower = itemBuilder.fireworkPower;
      this.isUnbreakable = itemBuilder.isUnbreakable;
      this.hideItemFlags = itemBuilder.hideItemFlags;
      this.itemFlags = itemBuilder.itemFlags;
      this.isGlowing = itemBuilder.isGlowing;
      this.isSpawner = itemBuilder.isSpawner;
      this.entityType = itemBuilder.entityType;
      this.namePlaceholders = new HashMap(itemBuilder.namePlaceholders);
      this.lorePlaceholders = new HashMap(itemBuilder.lorePlaceholders);
      this.crateName = itemBuilder.crateName;
   }

   public ItemBuilder(ItemStack itemStack) {
      this.material = Material.STONE;
      this.itemStack = null;
      this.itemAmount = 1;
      this.itemData = "";
      this.displayName = "";
      this.displayLore = new ArrayList();
      this.itemDamage = 0;
      this.hasCustomModelData = false;
      this.customModelData = 0;
      this.customMaterial = "";
      this.isPotion = false;
      this.potionColor = Color.RED;
      this.potionType = null;
      this.potionDuration = -1;
      this.potionAmplifier = 1;
      this.isHash = false;
      this.isURL = false;
      this.isHead = false;
      this.player = "";
      this.isTippedArrow = false;
      this.isLeatherArmor = false;
      this.isArmor = false;
      this.trimMaterial = null;
      this.trimPattern = null;
      this.armorColor = Color.RED;
      this.isBanner = false;
      this.patterns = new ArrayList();
      this.isShield = false;
      this.isMap = false;
      this.mapColor = Color.RED;
      this.isFirework = false;
      this.isFireworkStar = false;
      this.fireworkColor = Color.RED;
      this.fireworkColors = new ArrayList();
      this.fireworkPower = 1;
      this.isUnbreakable = false;
      this.hideItemFlags = false;
      this.itemFlags = new ArrayList();
      this.isGlowing = false;
      this.isSpawner = false;
      this.entityType = EntityType.BAT;
      this.crateName = "";
      this.target = null;
      this.namePlaceholders = new HashMap();
      this.lorePlaceholders = new HashMap();
      this.itemStack = itemStack;
      this.material = itemStack.getType();
      switch(this.material) {
      case LEATHER_HELMET:
      case LEATHER_CHESTPLATE:
      case LEATHER_LEGGINGS:
      case LEATHER_BOOTS:
      case LEATHER_HORSE_ARMOR:
         this.isLeatherArmor = true;
         break;
      case POTION:
      case SPLASH_POTION:
      case LINGERING_POTION:
         this.isPotion = true;
         break;
      case FIREWORK_STAR:
         this.isFireworkStar = true;
         break;
      case TIPPED_ARROW:
         this.isTippedArrow = true;
         break;
      case FIREWORK_ROCKET:
         this.isFirework = true;
         break;
      case FILLED_MAP:
         this.isMap = true;
         break;
      case PLAYER_HEAD:
         this.isHead = true;
         break;
      case SPAWNER:
         this.isSpawner = true;
         break;
      case SHIELD:
         this.isShield = true;
      }

      this.itemStack.editMeta((itemMeta) -> {
         if (itemMeta.hasDisplayName()) {
            this.displayName = itemMeta.getDisplayName();
         }

         if (itemMeta.hasLore()) {
            this.displayLore = itemMeta.getLore();
         }

      });
      String name = this.material.name();
      this.isArmor = name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
      this.isBanner = name.endsWith("BANNER");
   }

   public ItemBuilder(ItemStack itemStack, Player target) {
      this.material = Material.STONE;
      this.itemStack = null;
      this.itemAmount = 1;
      this.itemData = "";
      this.displayName = "";
      this.displayLore = new ArrayList();
      this.itemDamage = 0;
      this.hasCustomModelData = false;
      this.customModelData = 0;
      this.customMaterial = "";
      this.isPotion = false;
      this.potionColor = Color.RED;
      this.potionType = null;
      this.potionDuration = -1;
      this.potionAmplifier = 1;
      this.isHash = false;
      this.isURL = false;
      this.isHead = false;
      this.player = "";
      this.isTippedArrow = false;
      this.isLeatherArmor = false;
      this.isArmor = false;
      this.trimMaterial = null;
      this.trimPattern = null;
      this.armorColor = Color.RED;
      this.isBanner = false;
      this.patterns = new ArrayList();
      this.isShield = false;
      this.isMap = false;
      this.mapColor = Color.RED;
      this.isFirework = false;
      this.isFireworkStar = false;
      this.fireworkColor = Color.RED;
      this.fireworkColors = new ArrayList();
      this.fireworkPower = 1;
      this.isUnbreakable = false;
      this.hideItemFlags = false;
      this.itemFlags = new ArrayList();
      this.isGlowing = false;
      this.isSpawner = false;
      this.entityType = EntityType.BAT;
      this.crateName = "";
      this.target = null;
      this.namePlaceholders = new HashMap();
      this.lorePlaceholders = new HashMap();
      this.target = target;
      this.itemStack = itemStack;
      this.material = itemStack.getType();
      switch(this.material) {
      case LEATHER_HELMET:
      case LEATHER_CHESTPLATE:
      case LEATHER_LEGGINGS:
      case LEATHER_BOOTS:
      case LEATHER_HORSE_ARMOR:
         this.isLeatherArmor = true;
         break;
      case POTION:
      case SPLASH_POTION:
      case LINGERING_POTION:
         this.isPotion = true;
         break;
      case FIREWORK_STAR:
         this.isFireworkStar = true;
         break;
      case TIPPED_ARROW:
         this.isTippedArrow = true;
         break;
      case FIREWORK_ROCKET:
         this.isFirework = true;
         break;
      case FILLED_MAP:
         this.isMap = true;
         break;
      case PLAYER_HEAD:
         this.isHead = true;
         break;
      case SPAWNER:
         this.isSpawner = true;
         break;
      case SHIELD:
         this.isShield = true;
      }

      this.itemStack.editMeta((itemMeta) -> {
         if (itemMeta.hasDisplayName()) {
            this.displayName = itemMeta.getDisplayName();
         }

         if (itemMeta.hasLore()) {
            this.displayLore = itemMeta.getLore();
         }

      });
      String name = this.material.name();
      this.isArmor = name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
      this.isBanner = name.endsWith("BANNER");
   }

   public ItemBuilder() {
      this.material = Material.STONE;
      this.itemStack = null;
      this.itemAmount = 1;
      this.itemData = "";
      this.displayName = "";
      this.displayLore = new ArrayList();
      this.itemDamage = 0;
      this.hasCustomModelData = false;
      this.customModelData = 0;
      this.customMaterial = "";
      this.isPotion = false;
      this.potionColor = Color.RED;
      this.potionType = null;
      this.potionDuration = -1;
      this.potionAmplifier = 1;
      this.isHash = false;
      this.isURL = false;
      this.isHead = false;
      this.player = "";
      this.isTippedArrow = false;
      this.isLeatherArmor = false;
      this.isArmor = false;
      this.trimMaterial = null;
      this.trimPattern = null;
      this.armorColor = Color.RED;
      this.isBanner = false;
      this.patterns = new ArrayList();
      this.isShield = false;
      this.isMap = false;
      this.mapColor = Color.RED;
      this.isFirework = false;
      this.isFireworkStar = false;
      this.fireworkColor = Color.RED;
      this.fireworkColors = new ArrayList();
      this.fireworkPower = 1;
      this.isUnbreakable = false;
      this.hideItemFlags = false;
      this.itemFlags = new ArrayList();
      this.isGlowing = false;
      this.isSpawner = false;
      this.entityType = EntityType.BAT;
      this.crateName = "";
      this.target = null;
      this.namePlaceholders = new HashMap();
      this.lorePlaceholders = new HashMap();
   }

   private String parse(String message) {
      return Support.placeholder_api.isEnabled() && this.target != null ? Methods.color(PlaceholderAPI.setPlaceholders(this.target, message)) : Methods.color(message);
   }

   public ItemStack build() {
      if (Support.oraxen.isEnabled()) {
         io.th0rgal.oraxen.items.ItemBuilder oraxenItem = OraxenItems.getItemById(this.customMaterial);
         if (oraxenItem != null) {
            if (this.itemStack != null) {
               this.material = this.itemStack.getType();
               return this.itemStack;
            }

            this.itemStack = oraxenItem.build();
            this.material = this.itemStack.getType();
            return this.itemStack;
         }
      }

      if (this.isHead && this.isHash) {
         if (this.isURL) {
            this.itemStack = SkullCreator.itemWithUrl(this.itemStack, this.player);
         } else {
            this.itemStack = SkullCreator.itemWithBase64(this.itemStack, this.player);
         }
      }

      if (this.itemStack.getType() != Material.AIR) {
         this.getItemStack().setAmount(this.itemAmount);
         this.getItemStack().editMeta((itemMeta) -> {
            if (itemMeta instanceof Damageable) {
               Damageable damageable = (Damageable)itemMeta;
               if (this.itemDamage >= 1) {
                  if (this.itemDamage >= this.material.getMaxDurability()) {
                     damageable.setDamage(this.material.getMaxDurability());
                  } else {
                     damageable.setDamage(this.itemDamage);
                  }
               }
            }

            if (this.isArmor && this.trimPattern != null && this.trimMaterial != null) {
               ArmorMeta armorMeta = (ArmorMeta)itemMeta;
               armorMeta.setTrim(new ArmorTrim(this.trimMaterial, this.trimPattern));
            }

            if (this.isMap) {
               MapMeta mapMeta = (MapMeta)itemMeta;
               mapMeta.setScaling(true);
               if (this.mapColor != null) {
                  mapMeta.setColor(this.mapColor);
               }
            }

            if (this.isPotion || this.isTippedArrow) {
               PotionMeta potionMeta = (PotionMeta)itemMeta;
               if (this.potionType != null) {
                  PotionEffect effect = new PotionEffect(this.potionType, this.potionDuration, this.potionAmplifier);
                  potionMeta.addCustomEffect(effect, true);
                  potionMeta.setBasePotionData(new PotionData(PotionType.valueOf(effect.getType().getName())));
               }

               if (this.potionColor != null) {
                  potionMeta.setColor(this.potionColor);
               }
            }

            if (this.isLeatherArmor && this.armorColor != null) {
               LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)itemMeta;
               leatherArmorMeta.setColor(this.armorColor);
            }

            if (this.isBanner && !this.patterns.isEmpty()) {
               BannerMeta bannerMeta = (BannerMeta)itemMeta;
               bannerMeta.setPatterns(this.patterns);
            }

            if (this.isShield && !this.patterns.isEmpty()) {
               BlockStateMeta shieldMeta = (BlockStateMeta)itemMeta;
               Banner banner = (Banner)shieldMeta.getBlockState();
               banner.setPatterns(this.patterns);
               banner.update();
               shieldMeta.setBlockState(banner);
            }

            if (this.hasCustomModelData) {
               itemMeta.setCustomModelData(this.customModelData);
            }

            List var10000 = this.itemFlags;
            Objects.requireNonNull(itemMeta);
            var10000.forEach((xva$0) -> {
               itemMeta.addItemFlags(new ItemFlag[]{xva$0});
            });
            if (this.hideItemFlags) {
               itemMeta.addItemFlags(ItemFlag.values());
            }

            itemMeta.setUnbreakable(this.isUnbreakable);
            if (this.isGlowing) {
               itemMeta.setEnchantmentGlintOverride(true);
            }

            itemMeta.setDisplayName(this.getUpdatedName());
            itemMeta.setLore(this.getUpdatedLore());
         });
      } else {
         Logger logger = plugin.getLogger();
         logger.warning("Material cannot be of type AIR or null, If you see this.");
         logger.warning("in your console but do not have any invalid items. You can");
         logger.warning("ignore this as we use AIR for some niche cases internally.");
      }

      return this.getItemStack();
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public ItemMeta getItemMeta() {
      return this.itemStack.getItemMeta();
   }

   public ItemBuilder setItemMeta(ItemMeta itemMeta) {
      this.itemStack.setItemMeta(itemMeta);
      return this;
   }

   public ItemBuilder setTarget(Player target) {
      this.target = target;
      return this;
   }

   public String getUpdatedName() {
      String newName = this.displayName;

      String placeholder;
      for(Iterator var2 = this.namePlaceholders.keySet().iterator(); var2.hasNext(); newName = newName.replace(placeholder, (CharSequence)this.namePlaceholders.get(placeholder)).replace(placeholder.toLowerCase(), (CharSequence)this.namePlaceholders.get(placeholder))) {
         placeholder = (String)var2.next();
      }

      return this.parse(newName);
   }

   public String getName() {
      return this.displayName;
   }

   public Material getMaterial() {
      return this.material;
   }

   public ItemBuilder setMaterial(Material material) {
      this.material = material;
      if (this.itemStack == null) {
         this.itemStack = new ItemStack(this.material);
      } else {
         ItemMeta itemMeta = this.itemStack.getItemMeta();
         ItemStack newItemStack = new ItemStack(this.material);
         newItemStack.setItemMeta(itemMeta);
         this.itemStack = newItemStack;
      }

      this.isHead = material == Material.PLAYER_HEAD;
      return this;
   }

   public void setTrimMaterial(TrimMaterial trimMaterial) {
      this.trimMaterial = trimMaterial;
   }

   public void setTrimPattern(TrimPattern trimPattern) {
      this.trimPattern = trimPattern;
   }

   public ItemBuilder setMaterial(String type) {
      if (type != null && !type.isEmpty()) {
         this.customMaterial = type;
         String[] section;
         String name;
         if (type.contains(":")) {
            section = type.split(":");
            type = section[0];
            String metaData = section[1];
            if (metaData.contains("#")) {
               name = metaData.split("#")[1];
               if (this.isInt(name)) {
                  this.hasCustomModelData = true;
                  this.customModelData = Integer.parseInt(name);
               }
            }

            metaData = metaData.replace("#" + this.customModelData, "");
            if (this.isInt(metaData)) {
               this.itemDamage = Integer.parseInt(metaData);
            } else {
               this.potionType = this.getPotionType(PotionEffectType.getByName(metaData)).getEffectType();
               this.potionColor = getColor(metaData);
               this.armorColor = getColor(metaData);
               this.mapColor = getColor(metaData);
               this.fireworkColor = getColor(metaData);
            }
         } else if (type.contains("#")) {
            section = type.split("#");
            type = section[0];
            name = section[1];
            if (this.isInt(name)) {
               this.hasCustomModelData = true;
               this.customModelData = Integer.parseInt(name);
            }
         }

         Material material = Material.matchMaterial(type);
         if (material != null) {
            this.itemStack = new ItemStack(material);
            this.material = this.itemStack.getType();
         } else if (Support.oraxen.isEnabled()) {
            io.th0rgal.oraxen.items.ItemBuilder oraxenItem = OraxenItems.getItemById(this.customMaterial);
            if (oraxenItem != null) {
               this.itemStack = oraxenItem.build();
               this.material = this.itemStack.getType();
               return this;
            }
         }

         switch(this.material) {
         case LEATHER_HELMET:
         case LEATHER_CHESTPLATE:
         case LEATHER_LEGGINGS:
         case LEATHER_BOOTS:
         case LEATHER_HORSE_ARMOR:
            this.isLeatherArmor = true;
            break;
         case POTION:
         case SPLASH_POTION:
         case LINGERING_POTION:
            this.isPotion = true;
            break;
         case FIREWORK_STAR:
            this.isFireworkStar = true;
            break;
         case TIPPED_ARROW:
            this.isTippedArrow = true;
            break;
         case FIREWORK_ROCKET:
            this.isFirework = true;
            break;
         case FILLED_MAP:
            this.isMap = true;
            break;
         case PLAYER_HEAD:
            this.isHead = true;
            break;
         case SPAWNER:
            this.isSpawner = true;
            break;
         case SHIELD:
            this.isShield = true;
         }

         name = this.material.name();
         this.isArmor = name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
         this.isBanner = name.endsWith("BANNER");
         return this;
      } else {
         List.of("Material cannot be null or empty, Output: " + type + ".", "Please take a screenshot of this before asking for support.").forEach((line) -> {
            plugin.getLogger().warning(line);
         });
         this.itemStack = new ItemStack(Material.STONE);
         this.itemStack.editMeta((itemMeta) -> {
            itemMeta.setDisplayName(this.parse("&cAn error has occurred with the item builder."));
         });
         this.material = this.itemStack.getType();
         return this;
      }
   }

   public ItemBuilder setCrateName(String crateName) {
      this.crateName = crateName;
      return this;
   }

   public void setDamage(int damage) {
      this.itemDamage = damage;
   }

   public int getDamage() {
      return this.itemDamage;
   }

   public ItemBuilder setName(String itemName) {
      if (itemName != null) {
         this.displayName = itemName;
      }

      return this;
   }

   public ItemBuilder setNamePlaceholders(Map<String, String> placeholders) {
      this.namePlaceholders = placeholders;
      return this;
   }

   public ItemBuilder addNamePlaceholder(String placeholder, String argument) {
      this.namePlaceholders.put(placeholder, argument);
      return this;
   }

   public ItemBuilder removeNamePlaceholder(String placeholder) {
      this.namePlaceholders.remove(placeholder);
      return this;
   }

   public ItemBuilder setLore(List<String> lore) {
      if (lore != null) {
         this.displayLore.clear();
         Iterator var2 = lore.iterator();

         while(var2.hasNext()) {
            String line = (String)var2.next();
            this.displayLore.add(Methods.color(line));
         }
      }

      return this;
   }

   public ItemBuilder setLore(Player player, List<String> lore) {
      if (lore != null) {
         this.displayLore.clear();
         Iterator var3 = lore.iterator();

         while(var3.hasNext()) {
            String line = (String)var3.next();
            this.displayLore.add(PlaceholderAPI.setPlaceholders(player, Methods.color(line)));
         }
      }

      return this;
   }

   public ItemBuilder addLore(String lore) {
      if (lore != null) {
         this.displayLore.add(Methods.color(lore));
      }

      return this;
   }

   public ItemBuilder setLorePlaceholders(Map<String, String> placeholders) {
      this.lorePlaceholders = placeholders;
      return this;
   }

   public ItemBuilder addLorePlaceholder(String placeholder, String argument) {
      this.lorePlaceholders.put(placeholder, argument);
      return this;
   }

   public List<String> getUpdatedLore() {
      List<String> newLore = new ArrayList();
      Iterator var2 = this.displayLore.iterator();

      while(var2.hasNext()) {
         String item = (String)var2.next();

         String placeholder;
         for(Iterator var4 = this.lorePlaceholders.keySet().iterator(); var4.hasNext(); item = item.replace(placeholder, (CharSequence)this.lorePlaceholders.get(placeholder)).replace(placeholder.toLowerCase(), (CharSequence)this.lorePlaceholders.get(placeholder))) {
            placeholder = (String)var4.next();
         }

         newLore.add(this.parse(item));
      }

      return newLore;
   }

   public ItemBuilder removeLorePlaceholder(String placeholder) {
      this.lorePlaceholders.remove(placeholder);
      return this;
   }

   public ItemBuilder setEntityType(EntityType entityType) {
      this.entityType = entityType;
      return this;
   }

   public ItemBuilder addPatterns(List<String> patterns) {
      patterns.forEach(this::addPatterns);
      return this;
   }

   public ItemBuilder addPattern(Pattern pattern) {
      this.patterns.add(pattern);
      return this;
   }

   public ItemBuilder setPattern(List<Pattern> patterns) {
      this.patterns = patterns;
      return this;
   }

   public ItemBuilder setAmount(Integer amount) {
      this.itemAmount = amount;
      return this;
   }

   public ItemBuilder setPlayerName(String playerName) {
      this.player = playerName;
      if (this.player != null && this.player.length() > 16) {
         this.isHash = true;
         this.isURL = this.player.startsWith("http");
      }

      return this;
   }

   public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments, boolean unsafeEnchantments) {
      enchantments.forEach((enchantment, level) -> {
         this.addEnchantment(enchantment, level, unsafeEnchantments);
      });
      return this;
   }

   public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean unsafeEnchantments) {
      this.getItemStack().editMeta((itemMeta) -> {
         itemMeta.addEnchant(enchantment, level, unsafeEnchantments);
      });
      return this;
   }

   public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
      this.getItemStack().editMeta((itemMeta) -> {
         itemMeta.addEnchant(enchantment, level, false);
      });
      return this;
   }

   public ItemBuilder removeEnchantment(Enchantment enchantment) {
      this.getItemStack().editMeta((itemMeta) -> {
         itemMeta.removeEnchant(enchantment);
      });
      return this;
   }

   public ItemBuilder setFlagsFromStrings(List<String> flagStrings) {
      this.itemFlags.clear();
      Iterator var2 = flagStrings.iterator();

      while(var2.hasNext()) {
         String flagString = (String)var2.next();
         ItemFlag flag = this.getFlag(flagString);
         if (flag != null) {
            this.itemFlags.add(flag);
         }
      }

      return this;
   }

   public ItemBuilder addItemFlags(List<String> flagStrings) {
      Iterator var2 = flagStrings.iterator();

      while(var2.hasNext()) {
         String flagString = (String)var2.next();

         try {
            ItemFlag itemFlag = ItemFlag.valueOf(flagString.toUpperCase());
            this.addItemFlag(itemFlag);
         } catch (Exception var5) {
         }
      }

      return this;
   }

   public ItemBuilder addFlags(String flagString) {
      ItemFlag flag = this.getFlag(flagString);
      if (flag != null) {
         this.itemFlags.add(flag);
      }

      return this;
   }

   public ItemBuilder addItemFlag(ItemFlag itemFlag) {
      if (itemFlag != null) {
         this.itemFlags.add(itemFlag);
      }

      return this;
   }

   public ItemBuilder setItemFlags(List<ItemFlag> itemFlags) {
      this.itemFlags = itemFlags;
      return this;
   }

   public ItemBuilder hideItemFlags(boolean hideItemFlags) {
      this.hideItemFlags = hideItemFlags;
      return this;
   }

   public ItemBuilder setUnbreakable(boolean unbreakable) {
      this.isUnbreakable = unbreakable;
      return this;
   }

   public ItemBuilder setGlow(boolean glow) {
      this.isGlowing = glow;
      return this;
   }

   public ItemBuilder setEnchantments(HashMap<Enchantment, Integer> enchantments) {
      if (enchantments != null) {
         this.getItemStack().editMeta((meta) -> {
            enchantments.forEach((enchantment, amount) -> {
               meta.addEnchant(enchantment, amount, false);
            });
         });
      }

      return this;
   }

   public ItemBuilder addEnchantments(Enchantment enchantment, int level) {
      this.getItemStack().editMeta((meta) -> {
         meta.addEnchant(enchantment, level, false);
      });
      return this;
   }

   public ItemBuilder removeEnchantments(Enchantment enchantment) {
      this.getItemStack().editMeta((meta) -> {
         meta.removeEnchant(enchantment);
      });
      return this;
   }

   public static ItemBuilder convertItemStack(ItemStack item) {
      return (new ItemBuilder(item)).setAmount(item.getAmount()).setEnchantments(new HashMap(item.getEnchantments()));
   }

   public static ItemBuilder convertItemStack(String item) {
      ItemStack itemStack = Methods.fromBase64(item);
      return (new ItemBuilder(itemStack)).setAmount(itemStack.getAmount()).setEnchantments(new HashMap(itemStack.getEnchantments()));
   }

   public static ItemBuilder convertItemStack(ItemStack item, Player player) {
      return (new ItemBuilder(item)).setTarget(player).setAmount(item.getAmount()).setEnchantments(new HashMap(item.getEnchantments()));
   }

   public static ItemBuilder convertString(String itemString) {
      return convertString(itemString, (String)null);
   }

   public static ItemBuilder convertString(String itemString, String placeHolder) {
      ItemBuilder itemBuilder = new ItemBuilder();

      try {
         String[] var3 = itemString.split(", ");
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String optionString = var3[var5];
            String option = optionString.split(":")[0];
            String value = optionString.replace(option + ":", "").replace(option, "");
            String var9 = option.toLowerCase();
            byte var10 = -1;
            switch(var9.hashCode()) {
            case -1413853096:
               if (var9.equals("amount")) {
                  var10 = 2;
               }
               break;
            case -1339126929:
               if (var9.equals("damage")) {
                  var10 = 3;
               }
               break;
            case -1217922446:
               if (var9.equals("trim-material")) {
                  var10 = 8;
               }
               break;
            case -985752863:
               if (var9.equals("player")) {
                  var10 = 5;
               }
               break;
            case -504777632:
               if (var9.equals("unbreakable-item")) {
                  var10 = 6;
               }
               break;
            case 3242771:
               if (var9.equals("item")) {
                  var10 = 0;
               }
               break;
            case 3327734:
               if (var9.equals("lore")) {
                  var10 = 4;
               }
               break;
            case 3373707:
               if (var9.equals("name")) {
                  var10 = 1;
               }
               break;
            case 822542565:
               if (var9.equals("trim-pattern")) {
                  var10 = 7;
               }
            }

            ItemFlag[] var12;
            int var13;
            int var14;
            switch(var10) {
            case 0:
               itemBuilder.setMaterial(value);
               continue;
            case 1:
               itemBuilder.setName(value);
               continue;
            case 2:
               try {
                  itemBuilder.setAmount(Integer.parseInt(value));
               } catch (NumberFormatException var19) {
                  itemBuilder.setAmount(1);
               }
               continue;
            case 3:
               try {
                  itemBuilder.setDamage(Integer.parseInt(value));
               } catch (NumberFormatException var18) {
                  itemBuilder.setDamage(0);
               }
               continue;
            case 4:
               itemBuilder.setLore(Arrays.asList(value.split(",")));
               continue;
            case 5:
               itemBuilder.setPlayerName(value);
               continue;
            case 6:
               if (value.isEmpty() || value.equalsIgnoreCase("true")) {
                  itemBuilder.setUnbreakable(true);
               }
               continue;
            case 7:
               if (!value.isEmpty()) {
                  itemBuilder.setTrimPattern((TrimPattern)Registry.TRIM_PATTERN.get(NamespacedKey.minecraft(value.toLowerCase())));
               }
               continue;
            case 8:
               if (!value.isEmpty()) {
                  itemBuilder.setTrimMaterial((TrimMaterial)Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft(value.toLowerCase())));
               }
               continue;
            default:
               Enchantment enchantment = getEnchantment(option);
               if (enchantment != null) {
                  try {
                     itemBuilder.addEnchantments(enchantment, Integer.parseInt(value));
                  } catch (NumberFormatException var17) {
                     itemBuilder.addEnchantments(enchantment, 1);
                  }
                  continue;
               }

               var12 = ItemFlag.values();
               var13 = var12.length;
               var14 = 0;
            }

            while(var14 < var13) {
               ItemFlag itemFlag = var12[var14];
               if (itemFlag.name().equalsIgnoreCase(option)) {
                  itemBuilder.addItemFlag(itemFlag);
                  break;
               }

               ++var14;
            }

            try {
               PatternType[] var22 = PatternType.values();
               var13 = var22.length;

               for(var14 = 0; var14 < var13; ++var14) {
                  PatternType pattern = var22[var14];
                  if (option.equalsIgnoreCase(pattern.name()) || value.equalsIgnoreCase(pattern.getIdentifier())) {
                     DyeColor color = getDyeColor(value);
                     if (color != null) {
                        itemBuilder.addPattern(new Pattern(color, pattern));
                     }
                     break;
                  }
               }
            } catch (Exception var20) {
            }
         }
      } catch (Exception var21) {
         itemBuilder.setMaterial(Material.RED_TERRACOTTA).setName("&c&lERROR").setLore(Arrays.asList("&cThere is an error", "&cFor : &c" + (placeHolder != null ? placeHolder : "")));
         ((CrazyAuctions)JavaPlugin.getPlugin(CrazyAuctions.class)).getLogger().log(Level.WARNING, "An error has occurred with the item builder: ", var21);
      }

      return itemBuilder;
   }

   public static List<ItemBuilder> convertStringList(List<String> itemStrings) {
      return convertStringList(itemStrings, (String)null);
   }

   public static List<ItemBuilder> convertStringList(List<String> itemStrings, String placeholder) {
      return (List)itemStrings.stream().map((itemString) -> {
         return convertString(itemString, placeholder);
      }).collect(Collectors.toList());
   }

   private PotionType getPotionType(PotionEffectType type) {
      if (type != null) {
         if (type.equals(PotionEffectType.FIRE_RESISTANCE)) {
            return PotionType.FIRE_RESISTANCE;
         }

         if (type.equals(PotionEffectType.INSTANT_DAMAGE)) {
            return PotionType.STRONG_HARMING;
         }

         if (type.equals(PotionEffectType.INSTANT_HEALTH)) {
            return PotionType.HEALING;
         }

         if (type.equals(PotionEffectType.INVISIBILITY)) {
            return PotionType.INVISIBILITY;
         }

         if (type.equals(PotionEffectType.JUMP_BOOST)) {
            return PotionType.LEAPING;
         }

         if (type.equals(PotionEffectType.LUCK)) {
            return PotionType.LUCK;
         }

         if (type.equals(PotionEffectType.NIGHT_VISION)) {
            return PotionType.NIGHT_VISION;
         }

         if (type.equals(PotionEffectType.POISON)) {
            return PotionType.POISON;
         }

         if (type.equals(PotionEffectType.REGENERATION)) {
            return PotionType.REGENERATION;
         }

         if (type.equals(PotionEffectType.SLOWNESS)) {
            return PotionType.SLOWNESS;
         }

         if (type.equals(PotionEffectType.SPEED)) {
            return PotionType.SWIFTNESS;
         }

         if (type.equals(PotionEffectType.STRENGTH)) {
            return PotionType.STRENGTH;
         }

         if (type.equals(PotionEffectType.WATER_BREATHING)) {
            return PotionType.WATER_BREATHING;
         }

         if (type.equals(PotionEffectType.WEAKNESS)) {
            return PotionType.WEAKNESS;
         }
      }

      return null;
   }

   private static Enchantment getEnchantment(String enchantmentName) {
      enchantmentName = stripEnchantmentName(enchantmentName);
      Enchantment[] var1 = Enchantment.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Enchantment enchantment = var1[var3];

         try {
            if (stripEnchantmentName(enchantment.getKey().getKey()).equalsIgnoreCase(enchantmentName)) {
               return enchantment;
            }

            Map<String, String> enchantments = getEnchantmentList();
            if (stripEnchantmentName(enchantment.getName()).equalsIgnoreCase(enchantmentName) || enchantments.get(enchantment.getName()) != null && stripEnchantmentName((String)enchantments.get(enchantment.getName())).equalsIgnoreCase(enchantmentName)) {
               return enchantment;
            }
         } catch (Exception var6) {
         }
      }

      return null;
   }

   private static String stripEnchantmentName(String enchantmentName) {
      return enchantmentName != null ? enchantmentName.replace("-", "").replace("_", "").replace(" ", "") : null;
   }

   private static Map<String, String> getEnchantmentList() {
      Map<String, String> enchantments = new HashMap();
      enchantments.put("ARROW_DAMAGE", "Power");
      enchantments.put("ARROW_FIRE", "Flame");
      enchantments.put("ARROW_INFINITE", "Infinity");
      enchantments.put("ARROW_KNOCKBACK", "Punch");
      enchantments.put("DAMAGE_ALL", "Sharpness");
      enchantments.put("DAMAGE_ARTHROPODS", "Bane_Of_Arthropods");
      enchantments.put("DAMAGE_UNDEAD", "Smite");
      enchantments.put("DEPTH_STRIDER", "Depth_Strider");
      enchantments.put("DIG_SPEED", "Efficiency");
      enchantments.put("DURABILITY", "Unbreaking");
      enchantments.put("FIRE_ASPECT", "Fire_Aspect");
      enchantments.put("KNOCKBACK", "KnockBack");
      enchantments.put("LOOT_BONUS_BLOCKS", "Fortune");
      enchantments.put("LOOT_BONUS_MOBS", "Looting");
      enchantments.put("LUCK", "Luck_Of_The_Sea");
      enchantments.put("LURE", "Lure");
      enchantments.put("OXYGEN", "Respiration");
      enchantments.put("PROTECTION_ENVIRONMENTAL", "Protection");
      enchantments.put("PROTECTION_EXPLOSIONS", "Blast_Protection");
      enchantments.put("PROTECTION_FALL", "Feather_Falling");
      enchantments.put("PROTECTION_FIRE", "Fire_Protection");
      enchantments.put("PROTECTION_PROJECTILE", "Projectile_Protection");
      enchantments.put("SILK_TOUCH", "Silk_Touch");
      enchantments.put("THORNS", "Thorns");
      enchantments.put("WATER_WORKER", "Aqua_Affinity");
      enchantments.put("BINDING_CURSE", "Curse_Of_Binding");
      enchantments.put("MENDING", "Mending");
      enchantments.put("FROST_WALKER", "Frost_Walker");
      enchantments.put("VANISHING_CURSE", "Curse_Of_Vanishing");
      enchantments.put("SWEEPING_EDGE", "Sweeping_Edge");
      enchantments.put("RIPTIDE", "Riptide");
      enchantments.put("CHANNELING", "Channeling");
      enchantments.put("IMPALING", "Impaling");
      enchantments.put("LOYALTY", "Loyalty");
      return enchantments;
   }

   private boolean isInt(String value) {
      try {
         Integer.parseInt(value);
         return true;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   private ItemFlag getFlag(String flagString) {
      ItemFlag[] var2 = ItemFlag.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemFlag flag = var2[var4];
         if (flag.name().equalsIgnoreCase(flagString)) {
            return flag;
         }
      }

      return null;
   }

   private void addPatterns(String stringPattern) {
      try {
         String[] split = stringPattern.split(":");
         PatternType[] var3 = PatternType.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PatternType pattern = var3[var5];
            if (split[0].equalsIgnoreCase(pattern.name()) || split[0].equalsIgnoreCase(pattern.getIdentifier())) {
               DyeColor color = getDyeColor(split[1]);
               if (color != null) {
                  this.addPattern(new Pattern(color, pattern));
               }
               break;
            }
         }
      } catch (Exception var8) {
      }

   }

   @Nullable
   public static DyeColor getDyeColor(@NotNull String value) {
      if (value.isEmpty()) {
         return null;
      } else {
         Color color = getColor(value);
         if (color == null) {
            plugin.getLogger().severe(value + " is not a valid color.");
            return null;
         } else {
            return DyeColor.getByColor(color);
         }
      }
   }

   private static Map<String, Color> createMap() {
      Map<String, Color> map = new HashMap();
      map.put("AQUA", Color.AQUA);
      map.put("BLACK", Color.BLACK);
      map.put("BLUE", Color.BLUE);
      map.put("FUCHSIA", Color.FUCHSIA);
      map.put("GRAY", Color.GRAY);
      map.put("GREEN", Color.GREEN);
      map.put("LIME", Color.LIME);
      map.put("MAROON", Color.MAROON);
      map.put("NAVY", Color.NAVY);
      map.put("OLIVE", Color.OLIVE);
      map.put("ORANGE", Color.ORANGE);
      map.put("PURPLE", Color.PURPLE);
      map.put("RED", Color.RED);
      map.put("SILVER", Color.SILVER);
      map.put("TEAL", Color.TEAL);
      map.put("WHITE", Color.WHITE);
      map.put("YELLOW", Color.YELLOW);
      return map;
   }

   public static Color getColor(String color) {
      if (color != null && !color.isBlank()) {
         Color mappedColor = (Color)colors.get(color.toUpperCase());
         if (mappedColor != null) {
            return mappedColor;
         } else {
            try {
               String[] rgb = color.split(",");
               if (rgb.length != 3) {
                  return null;
               } else {
                  int red = Integer.parseInt(rgb[0]);
                  int green = Integer.parseInt(rgb[1]);
                  int blue = Integer.parseInt(rgb[2]);
                  return Color.fromRGB(red, green, blue);
               }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException var6) {
               return null;
            }
         }
      } else {
         return null;
      }
   }
}
