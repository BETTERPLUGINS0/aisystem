package libs.com.ryderbelserion.vital.paper.api.builders.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.th0rgal.oraxen.api.OraxenItems;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import libs.com.ryderbelserion.vital.common.VitalAPI;
import libs.com.ryderbelserion.vital.common.api.Provider;
import libs.com.ryderbelserion.vital.common.util.AdvUtil;
import libs.com.ryderbelserion.vital.common.util.StringUtil;
import libs.com.ryderbelserion.vital.paper.api.builders.PlayerBuilder;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiAction;
import libs.com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import libs.com.ryderbelserion.vital.paper.api.enums.Support;
import libs.com.ryderbelserion.vital.paper.util.DyeUtil;
import libs.com.ryderbelserion.vital.paper.util.ItemUtil;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.block.Banner;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.components.FoodComponent.FoodEffect;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.profile.PlayerTextures.SkinModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemBuilder<T extends ItemBuilder<T>> {
   private final VitalAPI api;
   private final NbtBuilder nbt;
   private ItemStack itemStack;
   private static final EnumSet<Material> LEATHER_ARMOR;
   private static final EnumSet<Material> POTIONS;
   private static final EnumSet<Material> BANNERS;
   private static final EnumSet<Material> ARMOR;
   private static final EnumSet<Material> SHULKERS;
   private boolean isCustom;
   @Nullable
   private Color color;
   @NotNull
   private Optional<Number> customModelData;
   private int damage;
   private List<String> displayLore;
   private List<Component> displayComponentLore;
   private Map<String, String> displayLorePlaceholders;
   private String displayName;
   private Component displayComponent;
   private Map<String, String> displayNamePlaceholders;
   @NotNull
   private List<? extends ConfigurationSerializable> effects;
   @NotNull
   private EntityType entityType;
   private List<FoodEffect> foodEffects;
   private boolean canAlwaysEat;
   private int nutritionalValue;
   private float saturation;
   private float eatSeconds;
   private int fireworkPower;
   @NotNull
   private List<ItemFlag> itemFlags;
   @NotNull
   private List<Pattern> patterns;
   @Nullable
   private UUID player;
   @Nullable
   private PotionType potionType;
   @Nullable
   private UUID uuid;
   private String url;
   private boolean isHidingItemFlags;
   private boolean isHidingToolTips;
   private boolean isFireResistant;
   private boolean isUnbreakable;
   private Boolean isGlowing;
   private TrimMaterial trimMaterial;
   private TrimPattern trimPattern;

   public ItemBuilder() {
      this(Material.STONE, 1);
   }

   public ItemBuilder(@NotNull Material material) {
      this(material, 1);
   }

   public ItemBuilder(@NotNull Material material, int amount) {
      this(ItemStack.of(material, amount), true);
   }

   public ItemBuilder(@NotNull ItemStack itemStack, boolean createNewStack) {
      this.api = Provider.getApi();
      this.nbt = new NbtBuilder();
      this.isCustom = false;
      this.color = null;
      this.customModelData = Optional.empty();
      this.damage = 0;
      this.displayLore = new ArrayList();
      this.displayComponentLore = new ArrayList();
      this.displayLorePlaceholders = new HashMap();
      this.displayName = "";
      this.displayComponent = Component.empty();
      this.displayNamePlaceholders = new HashMap();
      this.effects = new ArrayList();
      this.entityType = EntityType.PIG;
      this.foodEffects = new ArrayList();
      this.canAlwaysEat = false;
      this.nutritionalValue = 0;
      this.saturation = 0.0F;
      this.eatSeconds = 0.0F;
      this.fireworkPower = 1;
      this.itemFlags = new ArrayList();
      this.patterns = new ArrayList();
      this.player = null;
      this.potionType = null;
      this.uuid = null;
      this.url = "";
      this.isHidingItemFlags = false;
      this.isHidingToolTips = false;
      this.isFireResistant = false;
      this.isUnbreakable = false;
      this.isGlowing = null;
      this.itemStack = createNewStack ? itemStack.clone() : itemStack;
   }

   public ItemBuilder(@NotNull ItemBuilder<T> itemBuilder) {
      this(itemBuilder, false);
   }

   public ItemBuilder(@NotNull ItemBuilder<T> itemBuilder, boolean createNewStack) {
      this.api = Provider.getApi();
      this.nbt = new NbtBuilder();
      this.isCustom = false;
      this.color = null;
      this.customModelData = Optional.empty();
      this.damage = 0;
      this.displayLore = new ArrayList();
      this.displayComponentLore = new ArrayList();
      this.displayLorePlaceholders = new HashMap();
      this.displayName = "";
      this.displayComponent = Component.empty();
      this.displayNamePlaceholders = new HashMap();
      this.effects = new ArrayList();
      this.entityType = EntityType.PIG;
      this.foodEffects = new ArrayList();
      this.canAlwaysEat = false;
      this.nutritionalValue = 0;
      this.saturation = 0.0F;
      this.eatSeconds = 0.0F;
      this.fireworkPower = 1;
      this.itemFlags = new ArrayList();
      this.patterns = new ArrayList();
      this.player = null;
      this.potionType = null;
      this.uuid = null;
      this.url = "";
      this.isHidingItemFlags = false;
      this.isHidingToolTips = false;
      this.isFireResistant = false;
      this.isUnbreakable = false;
      this.isGlowing = null;
      this.itemStack = createNewStack ? itemBuilder.itemStack.clone() : itemBuilder.itemStack;
      this.isCustom = itemBuilder.isCustom;
      this.customModelData = itemBuilder.customModelData;
      this.damage = itemBuilder.damage;
      this.color = itemBuilder.color;
      this.displayLorePlaceholders = itemBuilder.displayLorePlaceholders;
      this.displayNamePlaceholders = itemBuilder.displayNamePlaceholders;
      this.displayComponentLore = itemBuilder.displayComponentLore;
      this.displayComponent = itemBuilder.displayComponent;
      this.displayLore = itemBuilder.displayLore;
      this.displayName = itemBuilder.displayName;
      this.effects = itemBuilder.effects;
      this.entityType = itemBuilder.entityType;
      this.nutritionalValue = itemBuilder.nutritionalValue;
      this.canAlwaysEat = itemBuilder.canAlwaysEat;
      this.foodEffects = itemBuilder.foodEffects;
      this.saturation = itemBuilder.saturation;
      this.eatSeconds = itemBuilder.eatSeconds;
      this.fireworkPower = itemBuilder.fireworkPower;
      this.itemFlags = itemBuilder.itemFlags;
      this.patterns = itemBuilder.patterns;
      this.player = itemBuilder.player;
      this.url = itemBuilder.url;
      this.uuid = itemBuilder.uuid;
      this.potionType = itemBuilder.potionType;
      this.isHidingItemFlags = itemBuilder.isHidingItemFlags;
      this.isHidingToolTips = itemBuilder.isHidingToolTips;
      this.isFireResistant = itemBuilder.isFireResistant;
      this.isUnbreakable = itemBuilder.isUnbreakable;
      this.isGlowing = itemBuilder.isGlowing;
      this.trimMaterial = itemBuilder.trimMaterial;
      this.trimPattern = itemBuilder.trimPattern;
   }

   public final T apply(Consumer<ItemBuilder<T>> builder) {
      builder.accept(this);
      return this;
   }

   public ItemBuilder(@NotNull ItemStack itemStack) {
      this.api = Provider.getApi();
      this.nbt = new NbtBuilder();
      this.isCustom = false;
      this.color = null;
      this.customModelData = Optional.empty();
      this.damage = 0;
      this.displayLore = new ArrayList();
      this.displayComponentLore = new ArrayList();
      this.displayLorePlaceholders = new HashMap();
      this.displayName = "";
      this.displayComponent = Component.empty();
      this.displayNamePlaceholders = new HashMap();
      this.effects = new ArrayList();
      this.entityType = EntityType.PIG;
      this.foodEffects = new ArrayList();
      this.canAlwaysEat = false;
      this.nutritionalValue = 0;
      this.saturation = 0.0F;
      this.eatSeconds = 0.0F;
      this.fireworkPower = 1;
      this.itemFlags = new ArrayList();
      this.patterns = new ArrayList();
      this.player = null;
      this.potionType = null;
      this.uuid = null;
      this.url = "";
      this.isHidingItemFlags = false;
      this.isHidingToolTips = false;
      this.isFireResistant = false;
      this.isUnbreakable = false;
      this.isGlowing = null;
      this.itemStack = itemStack;
      if (this.hasItemMeta()) {
         ItemMeta itemMeta = this.itemStack.getItemMeta();
         if (itemMeta.hasDisplayName()) {
            this.displayComponent = itemMeta.displayName();
         }

         if (itemMeta.hasLore()) {
            this.displayComponentLore = itemMeta.lore();
         }

         if (itemMeta.hasCustomModelData()) {
            this.customModelData = Optional.of(itemMeta.getCustomModelData());
         }

         int var10001;
         if (itemMeta instanceof Damageable) {
            Damageable damageable = (Damageable)itemMeta;
            var10001 = damageable.getDamage();
         } else {
            var10001 = 1;
         }

         this.damage = var10001;
         if (!this.isFirework() && !this.isFireworkStar()) {
            BlockStateMeta shield;
            if (this.isSpawner()) {
               if (itemMeta instanceof BlockStateMeta) {
                  shield = (BlockStateMeta)itemMeta;
                  CreatureSpawner creatureSpawner = (CreatureSpawner)shield.getBlockState();
                  EntityType type = creatureSpawner.getSpawnedType();
                  if (type != null) {
                     this.entityType = type;
                  }
               }
            } else if (this.isBanner()) {
               if (itemMeta instanceof BannerMeta) {
                  BannerMeta banner = (BannerMeta)itemMeta;
                  this.patterns.addAll(banner.getPatterns());
               }
            } else if (this.isShield()) {
               if (itemMeta instanceof BlockStateMeta) {
                  shield = (BlockStateMeta)itemMeta;
                  Banner banner = (Banner)shield.getBlockState();
                  this.patterns.addAll(banner.getPatterns());
               }
            } else if (this.isPlayerHead()) {
               if (itemMeta instanceof SkullMeta) {
                  SkullMeta skull = (SkullMeta)itemMeta;
                  if (skull.hasOwner()) {
                     OfflinePlayer target = skull.getOwningPlayer();
                     if (target != null) {
                        this.uuid = target.getUniqueId();
                     }
                  }
               }
            } else if (!this.isArrow() && !this.isPotion()) {
               if (this.isLeather()) {
                  if (itemMeta instanceof LeatherArmorMeta) {
                     LeatherArmorMeta armor = (LeatherArmorMeta)itemMeta;
                     this.color = armor.getColor();
                  }
               } else if (this.isMap() && itemMeta instanceof MapMeta) {
                  MapMeta map = (MapMeta)itemMeta;
                  this.color = map.getColor();
               }
            } else if (itemMeta instanceof PotionMeta) {
               PotionMeta potionMeta = (PotionMeta)itemMeta;
               this.color = potionMeta.getColor();
               this.effects = potionMeta.getCustomEffects();
               this.potionType = potionMeta.getBasePotionType();
            }
         } else if (itemMeta instanceof FireworkMeta) {
            FireworkMeta firework = (FireworkMeta)itemMeta;
            this.effects = firework.getEffects();
            this.fireworkPower = firework.getPower();
         }

         this.setHidingToolTips(itemMeta.isHideTooltip()).setFireResistant(itemMeta.isFireResistant()).setHidingItemFlags(itemMeta.getItemFlags().contains(ItemFlag.HIDE_ATTRIBUTES)).setUnbreakable(itemMeta.isUnbreakable());
         if (itemMeta.hasEnchantmentGlintOverride()) {
            this.setGlowing(itemMeta.getEnchantmentGlintOverride());
         }
      }

   }

   @NotNull
   public GuiItem asGuiItem(@Nullable GuiAction<InventoryClickEvent> action) {
      return new GuiItem(this.asItemStack(), action);
   }

   @NotNull
   public GuiItem asGuiItem() {
      return new GuiItem(this.asItemStack(), (GuiAction)null);
   }

   @NotNull
   public ItemStack asItemStack() {
      return this.asItemStack((Consumer)null);
   }

   @NotNull
   public ItemStack asItemStack(@Nullable Consumer<ItemMeta> consumer) {
      if (this.isCustom) {
         return this.itemStack;
      } else {
         this.applyColor().applyEffects().applyEntityType().applyPattern().applySkull().applyTexture().applyDamage();
         if (this.trimPattern != null && this.trimMaterial != null) {
            this.applyTrim(this.trimPattern, this.trimMaterial);
         }

         this.itemStack.editMeta((itemMeta) -> {
            if (consumer != null) {
               consumer.accept(itemMeta);
            }

            String displayName = this.displayName;
            String line;
            if (!displayName.isEmpty()) {
               String key;
               if (!this.displayNamePlaceholders.isEmpty()) {
                  for(Iterator var4 = this.displayNamePlaceholders.entrySet().iterator(); var4.hasNext(); displayName = displayName.replace(key, line)) {
                     Entry<String, String> entry = (Entry)var4.next();
                     key = ((String)entry.getKey()).toLowerCase();
                     line = (String)entry.getValue();
                  }
               }

               itemMeta.displayName(this.displayComponent = AdvUtil.parse(displayName));
            }

            if (!this.displayLore.isEmpty()) {
               boolean isEmpty = this.displayLorePlaceholders.isEmpty();
               List<Component> components = new ArrayList();

               for(Iterator var14 = this.displayLore.iterator(); var14.hasNext(); components.add(AdvUtil.parse(line))) {
                  line = (String)var14.next();
                  String keyx;
                  String value;
                  if (!isEmpty) {
                     for(Iterator var8 = this.displayLorePlaceholders.entrySet().iterator(); var8.hasNext(); line = line.replace(keyx, value)) {
                        Entry<String, String> entryx = (Entry)var8.next();
                        keyx = ((String)entryx.getKey()).toLowerCase();
                        value = (String)entryx.getValue();
                     }
                  }
               }

               itemMeta.lore(this.displayComponentLore = components);
            }

            this.customModelData.ifPresent((number) -> {
               if (number.intValue() != -1) {
                  itemMeta.setCustomModelData(number.intValue());
               }
            });
            if (this.isHidingItemFlags) {
               itemMeta.addItemFlags(ItemFlag.values());
            } else {
               List var10000 = this.itemFlags;
               Objects.requireNonNull(itemMeta);
               var10000.forEach((xva$0) -> {
                  itemMeta.addItemFlags(new ItemFlag[]{xva$0});
               });
            }

            itemMeta.setEnchantmentGlintOverride(this.isGlowing);
            itemMeta.setFireResistant(this.isFireResistant);
            itemMeta.setHideTooltip(this.isHidingToolTips);
            itemMeta.setUnbreakable(this.isUnbreakable);
         });
         return this.itemStack;
      }
   }

   @NotNull
   public final String toBase64() {
      return ItemUtil.toBase64(this.asItemStack());
   }

   @NotNull
   public T fromBase64(@NotNull String base64) {
      return base64.isEmpty() ? new ItemBuilder() : new ItemBuilder(ItemUtil.fromBase64(base64));
   }

   @NotNull
   public T withType(@Nullable Material material) {
      if (material == null) {
         return this;
      } else {
         this.itemStack = this.itemStack.withType(material);
         return this;
      }
   }

   @NotNull
   public T withType(@Nullable Material material, int amount) {
      if (material == null) {
         return this;
      } else {
         this.itemStack = this.itemStack.withType(material);
         this.itemStack.setAmount(amount);
         return this;
      }
   }

   @NotNull
   public T withType(@NotNull String key) {
      if (key.isEmpty()) {
         return this;
      } else {
         if (Support.oraxen.isEnabled()) {
            io.th0rgal.oraxen.items.ItemBuilder oraxen = OraxenItems.getItemById(key);
            if (oraxen != null) {
               this.itemStack = oraxen.build();
            }
         }

         String type = key;
         String[] sections;
         String data;
         if (key.contains(":")) {
            sections = key.split(":");
            type = sections[0];
            data = sections[1];
            if (data.contains("#")) {
               String model = data.split("#")[1];
               this.customModelData = StringUtil.tryParseInt(model);
               if (this.customModelData.isPresent()) {
                  data = data.replace("#" + String.valueOf(this.customModelData.get()), "");
               }
            }

            Optional<Number> damage = StringUtil.tryParseInt(data);
            if (damage.isEmpty()) {
               PotionEffectType potionEffect = ItemUtil.getPotionEffect(data);
               if (potionEffect != null) {
                  this.effects = Collections.singletonList(new PotionEffect(potionEffect, 1, 1));
               }

               this.potionType = ItemUtil.getPotionType(data);
               this.color = data.contains(",") ? DyeUtil.getColor(data) : DyeUtil.getDefaultColor(data);
            } else {
               this.damage = ((Number)damage.get()).intValue();
            }
         } else if (key.contains("#")) {
            sections = key.split("#");
            type = sections[0];
            data = sections[1];
            this.customModelData = StringUtil.tryParseInt(data);
         }

         Material material = ItemUtil.getMaterial(type);
         return material == null ? this : this.withType(material);
      }
   }

   @NotNull
   public T setDisplayLore(@NotNull List<String> displayLore) {
      if (displayLore.isEmpty()) {
         return this;
      } else {
         this.displayLore = displayLore;
         return this;
      }
   }

   @NotNull
   public T addDisplayLore(@NotNull String displayLore) {
      if (displayLore.isEmpty()) {
         return this;
      } else {
         this.displayLore.add(displayLore);
         return this;
      }
   }

   @NotNull
   public T setDisplayName(@NotNull String displayName) {
      if (displayName.isEmpty()) {
         return this;
      } else {
         this.displayName = displayName;
         return this;
      }
   }

   @NotNull
   public T setCustomModelData(int model) {
      if (model == -1) {
         return this;
      } else {
         this.customModelData = Optional.of(model);
         return this;
      }
   }

   @NotNull
   public T setHidingItemFlags(boolean isHidingItemFlags) {
      this.isHidingItemFlags = isHidingItemFlags;
      return this;
   }

   @NotNull
   public T setHidingToolTips(boolean isHidingToolTips) {
      this.isHidingToolTips = isHidingToolTips;
      return this;
   }

   @NotNull
   public T setFireResistant(boolean isFireResistant) {
      this.isFireResistant = isFireResistant;
      return this;
   }

   @NotNull
   public T addFireworkEffect(@NotNull Builder effect) {
      if (!this.isFirework() && !this.isFireworkStar()) {
         return this;
      } else {
         this.effects = Collections.singletonList(effect.build());
         return this;
      }
   }

   @NotNull
   public T setFireworkPower(int fireworkPower) {
      if (!this.isFirework() && !this.isFireworkStar()) {
         return this;
      } else {
         this.fireworkPower = fireworkPower;
         return this;
      }
   }

   @NotNull
   public T setUnbreakable(boolean isUnbreakable) {
      this.isUnbreakable = isUnbreakable;
      return this;
   }

   @NotNull
   public T setGlowing(@Nullable Boolean isGlowing) {
      this.isGlowing = isGlowing;
      return this;
   }

   @NotNull
   public T setAmount(int amount) {
      this.itemStack.setAmount(amount);
      return this;
   }

   @NotNull
   public T addNamePlaceholder(@NotNull String placeholder, @NotNull String value) {
      return this.addPlaceholder(placeholder, value, false);
   }

   @NotNull
   public T setNamePlaceholders(Map<String, String> placeholders) {
      placeholders.forEach(this::addNamePlaceholder);
      return this;
   }

   @NotNull
   public T addLorePlaceholder(@NotNull String placeholder, @NotNull String value) {
      return this.addPlaceholder(placeholder, value, true);
   }

   @NotNull
   public T setLorePlaceholders(Map<String, String> placeholders) {
      placeholders.forEach(this::addLorePlaceholder);
      return this;
   }

   @NotNull
   public T addPattern(@NotNull PatternType type, @NotNull DyeColor color) {
      if (!this.isBanner() && !this.isShield()) {
         return this;
      } else {
         this.patterns.add(new Pattern(color, type));
         return this;
      }
   }

   @NotNull
   public T addPattern(@NotNull String pattern) {
      if (!this.isBanner() && !this.isShield()) {
         return this;
      } else if (!pattern.contains(":")) {
         return this;
      } else {
         String[] sections = pattern.split(":");
         PatternType type = ItemUtil.getPatternType(sections[0].toLowerCase());
         DyeColor color = DyeUtil.getDyeColor(sections[1]);
         return type == null ? this : this.addPattern(type, color);
      }
   }

   @NotNull
   public T addPatterns(@NotNull List<String> patterns) {
      if (!this.isBanner() && !this.isShield()) {
         return this;
      } else {
         patterns.forEach(this::addPattern);
         return this;
      }
   }

   @NotNull
   public T addPotionEffect(@NotNull PotionEffectType type, int duration, int amplifier) {
      if (!this.isArrow() && !this.isPotion()) {
         return this;
      } else {
         this.effects = Collections.singletonList(new PotionEffect(type, duration, amplifier));
         return this;
      }
   }

   @NotNull
   public T setPotionType(@NotNull PotionType potionType) {
      if (!this.isPotion()) {
         return this;
      } else {
         this.potionType = potionType;
         return this;
      }
   }

   @NotNull
   public T setColor(@NotNull Color color) {
      this.color = color;
      return this;
   }

   @NotNull
   public T setCustom(boolean isCustom) {
      this.isCustom = isCustom;
      return this;
   }

   @NotNull
   public T addItemFlag(@NotNull ItemFlag itemFlag) {
      this.itemFlags.add(itemFlag);
      return this;
   }

   @NotNull
   public T setItemFlags(@NotNull List<String> itemFlags) {
      if (itemFlags.isEmpty()) {
         return this;
      } else {
         itemFlags.forEach((flag) -> {
            this.addItemFlag(ItemFlag.valueOf(flag));
         });
         return this;
      }
   }

   @NotNull
   public T removeItemFlag(@NotNull ItemFlag itemFlag) {
      this.itemFlags.remove(itemFlag);
      return this;
   }

   @NotNull
   public T setEntityType(@NotNull EntityType entityType) {
      this.entityType = entityType;
      return this;
   }

   @NotNull
   public T setDamage(int damage) {
      this.damage = damage;
      return this;
   }

   @NotNull
   public T setPersistentDouble(@NotNull NamespacedKey key, double value) {
      this.nbt.setItemStack(this.itemStack).setPersistentDouble(key, value);
      return this;
   }

   @NotNull
   public T setPersistentInteger(@NotNull NamespacedKey key, int value) {
      this.nbt.setItemStack(this.itemStack).setPersistentInteger(key, value);
      return this;
   }

   @NotNull
   public T setPersistentBoolean(@NotNull NamespacedKey key, boolean value) {
      this.nbt.setItemStack(this.itemStack).setPersistentBoolean(key, value);
      return this;
   }

   @NotNull
   public T setPersistentString(@NotNull NamespacedKey key, @NotNull String value) {
      this.nbt.setItemStack(this.itemStack).setPersistentString(key, value);
      return this;
   }

   @NotNull
   public T setPersistentList(@NotNull NamespacedKey key, @NotNull List<String> values) {
      this.nbt.setItemStack(this.itemStack).setPersistentList(key, values);
      return this;
   }

   public final boolean getBoolean(@NotNull NamespacedKey key) {
      return this.nbt.setItemStack(this.itemStack).getBoolean(key);
   }

   public final double getDouble(@NotNull NamespacedKey key) {
      return this.nbt.setItemStack(this.itemStack).getDouble(key);
   }

   public final int getInteger(@NotNull NamespacedKey key) {
      return this.nbt.setItemStack(this.itemStack).getInteger(key);
   }

   @NotNull
   public final List<String> getList(@NotNull NamespacedKey key) {
      return this.nbt.setItemStack(this.itemStack).getList(key);
   }

   @NotNull
   public final String getString(@NotNull NamespacedKey key) {
      return this.nbt.setItemStack(this.itemStack).getString(key);
   }

   @NotNull
   public T removePersistentKey(@Nullable NamespacedKey key) {
      this.nbt.setItemStack(this.itemStack).removePersistentKey(key);
      return this;
   }

   public final boolean hasKey(@NotNull NamespacedKey key) {
      return this.nbt.setItemStack(this.itemStack).hasKey(key);
   }

   @Nullable
   public final UUID getPlayer() {
      return this.player;
   }

   @NotNull
   public T setPlayer(@NotNull Player player) {
      if (player.isEmpty()) {
         return this;
      } else {
         this.player = player.getUniqueId();
         return this;
      }
   }

   @NotNull
   public T setPlayer(@NotNull String player) {
      if (player.isEmpty()) {
         return this;
      } else if (player.length() > 16) {
         this.url = "https://textures.minecraft.net/texture/" + player;
         return this;
      } else {
         PlayerBuilder builder = new PlayerBuilder(player);
         Player target = builder.getPlayer();
         if (target != null) {
            this.uuid = target.getUniqueId();
         } else {
            OfflinePlayer offlineTarget = builder.getOfflinePlayer();
            if (offlineTarget != null) {
               this.uuid = offlineTarget.getUniqueId();
            }
         }

         return this;
      }
   }

   @NotNull
   public T setSkull(@NotNull UUID uuid) {
      if (!this.isPlayerHead()) {
         return this;
      } else {
         this.uuid = uuid;
         return this;
      }
   }

   @NotNull
   public T setSkull(@NotNull String skull, @Nullable HeadDatabaseAPI hdb) {
      if (!skull.isEmpty() && hdb != null) {
         this.itemStack = hdb.isHead(skull) ? hdb.getItemHead(skull) : this.itemStack.withType(Material.PLAYER_HEAD);
         return this;
      } else {
         return this;
      }
   }

   @NotNull
   public T addEnchantment(@NotNull String enchant, int level, boolean ignoreLevelCap) {
      if (this.isCustom) {
         return this;
      } else if (enchant.isEmpty()) {
         return this;
      } else if (level < 0) {
         return this;
      } else {
         Enchantment enchantment = ItemUtil.getEnchantment(enchant);
         if (enchantment == null) {
            return this;
         } else {
            this.itemStack.editMeta((itemMeta) -> {
               if (this.isEnchantedBook()) {
                  if (itemMeta instanceof EnchantmentStorageMeta) {
                     EnchantmentStorageMeta meta = (EnchantmentStorageMeta)itemMeta;
                     meta.addStoredEnchant(enchantment, level, ignoreLevelCap);
                  }

               } else {
                  itemMeta.addEnchant(enchantment, level, ignoreLevelCap);
               }
            });
            return this;
         }
      }
   }

   @NotNull
   public T removeEnchantment(@NotNull String enchant) {
      if (this.isCustom) {
         return this;
      } else if (enchant.isEmpty()) {
         return this;
      } else {
         Enchantment enchantment = ItemUtil.getEnchantment(enchant);
         if (enchantment == null) {
            return this;
         } else if (this.hasItemMeta()) {
            return this;
         } else if (this.isEnchantedBook()) {
            this.itemStack.editMeta((itemMeta) -> {
               if (itemMeta instanceof EnchantmentStorageMeta) {
                  EnchantmentStorageMeta meta = (EnchantmentStorageMeta)itemMeta;
                  if (meta.hasEnchant(enchantment)) {
                     meta.removeEnchant(enchantment);
                  }
               }

            });
            return this;
         } else {
            this.itemStack.editMeta((itemMeta) -> {
               if (itemMeta.hasEnchant(enchantment)) {
                  itemMeta.removeEnchant(enchantment);
               }

            });
            return this;
         }
      }
   }

   @NotNull
   public T addEnchantments(@NotNull Map<String, Integer> enchantments, boolean ignoreLevelCap) {
      if (this.isCustom) {
         return this;
      } else if (enchantments.isEmpty()) {
         return this;
      } else {
         enchantments.forEach((enchantment, level) -> {
            this.addEnchantment(enchantment, level, ignoreLevelCap);
         });
         return this;
      }
   }

   @NotNull
   public T removeEnchantments(@NotNull Set<String> enchantments) {
      if (this.isCustom) {
         return this;
      } else if (enchantments.isEmpty()) {
         return this;
      } else {
         enchantments.forEach(this::removeEnchantment);
         return this;
      }
   }

   @NotNull
   public T applyHiddenItemFlags() {
      if (this.isCustom) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            if (this.isHidingItemFlags) {
               itemMeta.addItemFlags(ItemFlag.values());
            } else {
               List var10000 = this.itemFlags;
               Objects.requireNonNull(itemMeta);
               var10000.forEach((xva$0) -> {
                  itemMeta.addItemFlags(new ItemFlag[]{xva$0});
               });
            }

         });
         return this;
      }
   }

   @NotNull
   public T applyCustomModelData() {
      if (this.isCustom) {
         return this;
      } else if (this.customModelData.isEmpty()) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            itemMeta.setCustomModelData(((Number)this.customModelData.get()).intValue());
         });
         return this;
      }
   }

   @NotNull
   public T applyUnbreakable() {
      if (this.isCustom) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            itemMeta.setUnbreakable(this.isUnbreakable);
         });
         return this;
      }
   }

   @NotNull
   public T applyEntityType() {
      if (this.isCustom) {
         return this;
      } else if (!this.isSpawner()) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            if (itemMeta instanceof BlockStateMeta) {
               BlockStateMeta blockState = (BlockStateMeta)itemMeta;
               CreatureSpawner creatureSpawner = (CreatureSpawner)blockState.getBlockState();
               creatureSpawner.setSpawnedType(this.entityType);
               blockState.setBlockState(creatureSpawner);
            }

         });
         return this;
      }
   }

   @NotNull
   public T applyTrim(@NotNull String pattern, @NotNull String material) {
      if (this.isCustom) {
         return this;
      } else if (!pattern.isEmpty() && !material.isEmpty()) {
         TrimMaterial trimMaterial = ItemUtil.getTrimMaterial(material);
         TrimPattern trimPattern = ItemUtil.getTrimPattern(pattern);
         return trimPattern != null && trimMaterial != null ? this.applyTrim(trimPattern, trimMaterial) : this;
      } else {
         return this;
      }
   }

   @NotNull
   public T applyTrimPattern(@NotNull String pattern) {
      if (this.isCustom) {
         return this;
      } else if (pattern.isEmpty()) {
         return this;
      } else {
         TrimPattern trimPattern = ItemUtil.getTrimPattern(pattern);
         if (trimPattern == null) {
            return this;
         } else {
            this.trimPattern = trimPattern;
            return this;
         }
      }
   }

   @NotNull
   public T applyTrimMaterial(@NotNull String material) {
      if (this.isCustom) {
         return this;
      } else if (material.isEmpty()) {
         return this;
      } else {
         TrimMaterial trimMaterial = ItemUtil.getTrimMaterial(material);
         if (trimMaterial == null) {
            return this;
         } else {
            this.trimMaterial = trimMaterial;
            return this;
         }
      }
   }

   @NotNull
   public T applyPattern() {
      if (this.isCustom) {
         return this;
      } else if (this.patterns.isEmpty()) {
         return this;
      } else {
         if (this.isBanner()) {
            this.itemStack.editMeta((itemMeta) -> {
               if (itemMeta instanceof BannerMeta) {
                  BannerMeta banner = (BannerMeta)itemMeta;
                  banner.setPatterns(this.patterns);
               }

            });
         } else if (this.isShield()) {
            this.itemStack.editMeta((itemMeta) -> {
               if (itemMeta instanceof BlockStateMeta) {
                  BlockStateMeta shield = (BlockStateMeta)itemMeta;
                  Banner banner = (Banner)shield.getBlockState();
                  banner.setPatterns(this.patterns);
                  banner.update();
                  shield.setBlockState(banner);
               }

            });
         }

         return this;
      }
   }

   @NotNull
   public T applyDamage() {
      if (this.isCustom) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            if (itemMeta instanceof Damageable) {
               Damageable damageable = (Damageable)itemMeta;
               if (this.damage <= 0) {
                  return;
               }

               if (this.damage >= this.getType().getMaxDurability()) {
                  damageable.setDamage(this.getType().getMaxDurability());
               } else {
                  damageable.setDamage(this.damage);
               }
            }

         });
         return this;
      }
   }

   @NotNull
   public T applyEffects() {
      if (this.isCustom) {
         return this;
      } else if (this.effects.isEmpty()) {
         return this;
      } else if (!this.isArrow() && !this.isFirework() && !this.isFireworkStar() && !this.isPotion()) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            if (itemMeta instanceof FireworkMeta) {
               FireworkMeta firework = (FireworkMeta)itemMeta;
               this.effects.forEach((effect) -> {
                  firework.addEffect((FireworkEffect)effect);
               });
               firework.setPower(this.fireworkPower);
            } else if (itemMeta instanceof PotionMeta) {
               PotionMeta potion = (PotionMeta)itemMeta;
               this.effects.forEach((effect) -> {
                  potion.addCustomEffect((PotionEffect)effect, true);
               });
               if (this.potionType != null) {
                  potion.setBasePotionType(this.potionType);
               }
            }

         });
         return this;
      }
   }

   @NotNull
   public T applyGlowing() {
      if (this.isCustom) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            itemMeta.setEnchantmentGlintOverride(!itemMeta.hasEnchants() && this.isGlowing);
         });
         return this;
      }
   }

   @NotNull
   public T applyTexture() {
      if (this.isCustom) {
         return this;
      } else if (this.url.isEmpty()) {
         return this;
      } else if (!this.isPlayerHead()) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            if (itemMeta instanceof SkullMeta) {
               SkullMeta skullMeta = (SkullMeta)itemMeta;
               PlayerProfile profile = Bukkit.getServer().createProfile((UUID)null, "");
               profile.setProperty(new ProfileProperty("", ""));
               PlayerTextures textures = profile.getTextures();

               try {
                  textures.setSkin(URI.create(this.url).toURL(), SkinModel.CLASSIC);
               } catch (MalformedURLException var6) {
                  if (this.api.isVerbose()) {
                     JavaPlugin.getProvidingPlugin(ItemBuilder.class).getComponentLogger().error("Failed to set the texture url", var6);
                  }
               }

               profile.setTextures(textures);
               skullMeta.setPlayerProfile(profile);
            }

         });
         return this;
      }
   }

   @NotNull
   public T applySkull() {
      if (this.isCustom) {
         return this;
      } else if (this.uuid == null) {
         return this;
      } else if (!this.isPlayerHead()) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            if (itemMeta instanceof SkullMeta) {
               SkullMeta skull = (SkullMeta)itemMeta;
               if (!skull.hasOwner()) {
                  skull.setOwningPlayer(this.getOfflinePlayer(this.uuid));
               }
            }

         });
         return this;
      }
   }

   @NotNull
   public T applyTrim(@NotNull TrimPattern trimPattern, @NotNull TrimMaterial trimMaterial) {
      if (this.isCustom) {
         return this;
      } else if (!this.isArmor()) {
         return this;
      } else {
         this.itemStack.editMeta((itemMeta) -> {
            if (itemMeta instanceof ArmorMeta) {
               ArmorMeta armorMeta = (ArmorMeta)itemMeta;
               armorMeta.setTrim(new ArmorTrim(trimMaterial, trimPattern));
            }

         });
         return this;
      }
   }

   @NotNull
   public T applyColor() {
      if (this.isCustom) {
         return this;
      } else if (this.color == null) {
         return this;
      } else {
         if (!this.isArrow() && !this.isPotion()) {
            if (this.isLeather()) {
               this.itemStack.editMeta((itemMeta) -> {
                  if (itemMeta instanceof LeatherArmorMeta) {
                     LeatherArmorMeta armor = (LeatherArmorMeta)itemMeta;
                     armor.setColor(this.color);
                  }

               });
            } else if (this.isMap()) {
               this.itemStack.editMeta((itemMeta) -> {
                  if (itemMeta instanceof MapMeta) {
                     MapMeta map = (MapMeta)itemMeta;
                     map.setScaling(true);
                     map.setColor(this.color);
                  }

               });
            }
         } else {
            this.itemStack.editMeta((itemMeta) -> {
               if (itemMeta instanceof PotionMeta) {
                  PotionMeta potion = (PotionMeta)itemMeta;
                  potion.setColor(this.color);
               }

            });
         }

         return this;
      }
   }

   @NotNull
   public final String getStrippedName() {
      return PlainTextComponentSerializer.plainText().serialize(this.itemStack.displayName());
   }

   @NotNull
   public final List<String> getStrippedLore() {
      List<String> lore = new ArrayList();
      this.displayComponentLore.forEach((line) -> {
         lore.add(PlainTextComponentSerializer.plainText().serialize(line));
      });
      return lore;
   }

   @NotNull
   public final List<ItemFlag> getItemFlags() {
      return this.itemFlags;
   }

   @NotNull
   public final List<String> getDisplayLore() {
      return this.displayLore;
   }

   @NotNull
   public final String getDisplayName() {
      return this.displayName;
   }

   public final boolean hasItemMeta() {
      return !this.itemStack.hasItemMeta();
   }

   @NotNull
   public final Material getType() {
      return this.itemStack.getType();
   }

   public final boolean isBanner() {
      return BANNERS.contains(this.getType());
   }

   public final boolean isArmor() {
      return ARMOR.contains(this.getType());
   }

   public final boolean isShulker() {
      return SHULKERS.contains(this.getType());
   }

   public final boolean isLeather() {
      return LEATHER_ARMOR.contains(this.getType());
   }

   public final boolean isPotion() {
      return POTIONS.contains(this.getType());
   }

   public final boolean isEnchantedBook() {
      return this.getType() == Material.ENCHANTED_BOOK;
   }

   public final boolean isPlayerHead() {
      return this.getType() == Material.PLAYER_HEAD;
   }

   public final boolean isFireworkStar() {
      return this.getType() == Material.FIREWORK_STAR;
   }

   public final boolean isFirework() {
      return this.getType() == Material.FIREWORK_ROCKET;
   }

   public final boolean isSpawner() {
      return this.getType() == Material.SPAWNER;
   }

   public final boolean isShield() {
      return this.getType() == Material.SHIELD;
   }

   public final boolean isArrow() {
      return this.getType() == Material.ARROW;
   }

   public final boolean isMap() {
      return this.getType() == Material.MAP;
   }

   public final boolean isHidingItemFlags() {
      return this.isHidingItemFlags;
   }

   public final boolean isHidingToolTips() {
      return this.isHidingToolTips;
   }

   @NotNull
   private T addPlaceholder(@NotNull String placeholder, @NotNull String value, boolean isLore) {
      if (isLore) {
         this.displayLorePlaceholders.put(placeholder, value);
         return this;
      } else {
         this.displayNamePlaceholders.put(placeholder, value);
         return this;
      }
   }

   @NotNull
   private OfflinePlayer getOfflinePlayer(@NotNull UUID uuid) {
      return Bukkit.getServer().getOfflinePlayer(uuid);
   }

   @Nullable
   private Player getPlayer(@NotNull UUID uuid) {
      return Bukkit.getServer().getPlayer(uuid);
   }

   static {
      LEATHER_ARMOR = EnumSet.of(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.LEATHER_HORSE_ARMOR);
      POTIONS = EnumSet.of(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION);
      BANNERS = EnumSet.of(Material.WHITE_BANNER, Material.ORANGE_BANNER, Material.MAGENTA_BANNER, Material.LIGHT_BLUE_BANNER, Material.YELLOW_BANNER, Material.LIME_BANNER, Material.PINK_BANNER, Material.GRAY_BANNER, Material.LIGHT_GRAY_BANNER, Material.CYAN_BANNER, Material.PURPLE_BANNER, Material.BLUE_BANNER, Material.BROWN_BANNER, Material.GREEN_BANNER, Material.RED_BANNER, Material.BLACK_BANNER, Material.WHITE_WALL_BANNER, Material.ORANGE_WALL_BANNER, Material.MAGENTA_WALL_BANNER, Material.LIGHT_BLUE_WALL_BANNER, Material.YELLOW_WALL_BANNER, Material.LIME_WALL_BANNER, Material.PINK_WALL_BANNER, Material.GRAY_WALL_BANNER, Material.LIGHT_GRAY_WALL_BANNER, Material.CYAN_WALL_BANNER, Material.PURPLE_WALL_BANNER, Material.BLUE_WALL_BANNER, Material.BROWN_WALL_BANNER, Material.GREEN_WALL_BANNER, Material.RED_WALL_BANNER, Material.BLACK_WALL_BANNER);
      ARMOR = EnumSet.of(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS);
      SHULKERS = EnumSet.of(Material.SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.LIME_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX);
   }
}
