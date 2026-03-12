package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.DyeColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.AxolotlVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.CatCollarComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.CatVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.ChickenVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.CowVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.FoxVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.FrogVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.HorseVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.LlamaVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.MooshroomVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.PaintingVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.ParrotVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.PigVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.RabbitVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.SalmonSizeComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.SheepColorComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.ShulkerColorComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TropicalFishBaseColorComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TropicalFishPatternColorComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TropicalFishPatternComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TypedBlockEntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TypedEntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.VillagerVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.WolfCollarComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.WolfSoundVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.WolfVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.ZombieNautilusVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ArmorTrim;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.BannerLayers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.BundleContents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ChargedProjectiles;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.CustomData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.DebugStickState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.FireworkExplosion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.FoodProperties;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAdventurePredicate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttackRange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttributeModifiers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBees;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBlockStateProperties;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBlocksAttacks;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBreakSound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemConsumable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemContainerContents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemContainerLoot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemCustomModelData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDamageResistant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDamageType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDeathProtection;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDyeColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEquippable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemFireworks;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemInstrument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemJukeboxPlayable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemKineticWeapon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLock;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLore;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemMapDecorations;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemMapPostProcessingState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemMinimumAttackCharge;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemModel;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemPiercingWeapon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemPotionContents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemPotionDurationScale;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProvidesBannerPatterns;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProvidesTrimMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRarity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRecipes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRepairable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemSwingAnimation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTool;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTooltipDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTooltipStyle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUnbreakable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseCooldown;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseRemainder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemWeapon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.LodestoneTracker;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.PotDecorations;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.SuspiciousStewEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.WritableBookContent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.WrittenBookContent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument.Instrument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Dummy;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Collection;

public final class ComponentTypes {
   private static final VersionedRegistry<ComponentType<?>> REGISTRY = new VersionedRegistry("data_component_type");
   public static final ComponentType<NBTCompound> CUSTOM_DATA = define("custom_data", CustomData::read, CustomData::write);
   public static final ComponentType<Integer> MAX_STACK_SIZE = define("max_stack_size", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
   public static final ComponentType<Integer> MAX_DAMAGE = define("max_damage", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
   public static final ComponentType<Integer> DAMAGE = define("damage", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
   public static final ComponentType<ItemUnbreakable> UNBREAKABLE_MODERN = define("unbreakable", ItemUnbreakable::read, ItemUnbreakable::write);
   /** @deprecated */
   @Deprecated
   public static final ComponentType<Boolean> UNBREAKABLE;
   public static final ComponentType<Component> CUSTOM_NAME;
   public static final ComponentType<Component> ITEM_NAME;
   public static final ComponentType<ItemLore> LORE;
   public static final ComponentType<ItemRarity> RARITY;
   public static final ComponentType<ItemEnchantments> ENCHANTMENTS;
   public static final ComponentType<ItemAdventurePredicate> CAN_PLACE_ON;
   public static final ComponentType<ItemAdventurePredicate> CAN_BREAK;
   public static final ComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS;
   public static final ComponentType<ItemCustomModelData> CUSTOM_MODEL_DATA_LISTS;
   /** @deprecated */
   @Deprecated
   public static final ComponentType<Integer> CUSTOM_MODEL_DATA;
   @ApiStatus.Obsolete
   public static final ComponentType<Dummy> HIDE_ADDITIONAL_TOOLTIP;
   @ApiStatus.Obsolete
   public static final ComponentType<Dummy> HIDE_TOOLTIP;
   public static final ComponentType<Integer> REPAIR_COST;
   public static final ComponentType<Dummy> CREATIVE_SLOT_LOCK;
   public static final ComponentType<Boolean> ENCHANTMENT_GLINT_OVERRIDE;
   public static final ComponentType<Dummy> INTANGIBLE_PROJECTILE;
   public static final ComponentType<FoodProperties> FOOD;
   @ApiStatus.Obsolete
   public static final ComponentType<Dummy> FIRE_RESISTANT;
   public static final ComponentType<ItemTool> TOOL;
   public static final ComponentType<ItemEnchantments> STORED_ENCHANTMENTS;
   public static final ComponentType<ItemDyeColor> DYED_COLOR;
   public static final ComponentType<Integer> MAP_COLOR;
   public static final ComponentType<Integer> MAP_ID;
   public static final ComponentType<ItemMapDecorations> MAP_DECORATIONS;
   public static final ComponentType<ItemMapPostProcessingState> MAP_POST_PROCESSING;
   public static final ComponentType<ChargedProjectiles> CHARGED_PROJECTILES;
   public static final ComponentType<BundleContents> BUNDLE_CONTENTS;
   public static final ComponentType<ItemPotionContents> POTION_CONTENTS;
   public static final ComponentType<SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS;
   public static final ComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT;
   public static final ComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT;
   public static final ComponentType<ArmorTrim> TRIM;
   public static final ComponentType<DebugStickState> DEBUG_STICK_STATE;
   public static final ComponentType<TypedEntityData> TYPED_ENTITY_DATA;
   /** @deprecated */
   @Deprecated
   public static final ComponentType<NBTCompound> ENTITY_DATA;
   public static final ComponentType<NBTCompound> BUCKET_ENTITY_DATA;
   public static final ComponentType<TypedBlockEntityData> TYPED_BLOCK_ENTITY_DATA;
   /** @deprecated */
   @Deprecated
   public static final ComponentType<NBTCompound> BLOCK_ENTITY_DATA;
   public static final ComponentType<ItemInstrument> ITEM_INSTRUMENT;
   /** @deprecated */
   @Deprecated
   public static final ComponentType<Instrument> INSTRUMENT;
   public static final ComponentType<Integer> OMINOUS_BOTTLE_AMPLIFIER;
   public static final ComponentType<ItemRecipes> RECIPES;
   public static final ComponentType<LodestoneTracker> LODESTONE_TRACKER;
   public static final ComponentType<FireworkExplosion> FIREWORK_EXPLOSION;
   public static final ComponentType<ItemFireworks> FIREWORKS;
   public static final ComponentType<ItemProfile> PROFILE;
   public static final ComponentType<ResourceLocation> NOTE_BLOCK_SOUND;
   public static final ComponentType<BannerLayers> BANNER_PATTERNS;
   public static final ComponentType<DyeColor> BASE_COLOR;
   public static final ComponentType<PotDecorations> POT_DECORATIONS;
   public static final ComponentType<ItemContainerContents> CONTAINER;
   public static final ComponentType<ItemBlockStateProperties> BLOCK_STATE;
   public static final ComponentType<ItemBees> BEES;
   public static final ComponentType<ItemLock> LOCK;
   public static final ComponentType<ItemContainerLoot> CONTAINER_LOOT;
   public static final ComponentType<ItemJukeboxPlayable> JUKEBOX_PLAYABLE;
   public static final ComponentType<ItemConsumable> CONSUMABLE;
   public static final ComponentType<ItemUseRemainder> USE_REMAINDER;
   public static final ComponentType<ItemUseCooldown> USE_COOLDOWN;
   public static final ComponentType<ItemEnchantable> ENCHANTABLE;
   public static final ComponentType<ItemRepairable> REPAIRABLE;
   public static final ComponentType<ItemModel> ITEM_MODEL;
   public static final ComponentType<ItemDamageResistant> DAMAGE_RESISTANT;
   public static final ComponentType<ItemEquippable> EQUIPPABLE;
   public static final ComponentType<Dummy> GLIDER;
   public static final ComponentType<ItemDeathProtection> DEATH_PROTECTION;
   public static final ComponentType<ItemTooltipStyle> TOOLTIP_STYLE;
   public static final ComponentType<ItemTooltipDisplay> TOOLTIP_DISPLAY;
   public static final ComponentType<ItemWeapon> WEAPON;
   public static final ComponentType<ItemBlocksAttacks> BLOCKS_ATTACKS;
   public static final ComponentType<ItemPotionDurationScale> POTION_DURATION_SCALE;
   public static final ComponentType<ItemProvidesTrimMaterial> PROVIDES_TRIM_MATERIAL;
   public static final ComponentType<ItemProvidesBannerPatterns> PROVIDES_BANNER_PATTERNS;
   public static final ComponentType<ItemBreakSound> BREAK_SOUND;
   public static final ComponentType<VillagerVariantComponent> VILLAGER_VARIANT;
   public static final ComponentType<WolfVariantComponent> WOLF_VARIANT;
   public static final ComponentType<WolfSoundVariantComponent> WOLF_SOUND_VARIANT;
   public static final ComponentType<WolfCollarComponent> WOLF_COLLAR;
   public static final ComponentType<FoxVariantComponent> FOX_VARIANT;
   public static final ComponentType<SalmonSizeComponent> SALMON_SIZE;
   public static final ComponentType<ParrotVariantComponent> PARROT_VARIANT;
   public static final ComponentType<TropicalFishPatternComponent> TROPICAL_FISH_PATTERN;
   public static final ComponentType<TropicalFishBaseColorComponent> TROPICAL_FISH_BASE_COLOR;
   public static final ComponentType<TropicalFishPatternColorComponent> TROPICAL_FISH_PATTERN_COLOR;
   public static final ComponentType<MooshroomVariantComponent> MOOSHROOM_VARIANT;
   public static final ComponentType<RabbitVariantComponent> RABBIT_VARIANT;
   public static final ComponentType<PigVariantComponent> PIG_VARIANT;
   public static final ComponentType<CowVariantComponent> COW_VARIANT;
   public static final ComponentType<ChickenVariantComponent> CHICKEN_VARIANT;
   public static final ComponentType<FrogVariantComponent> FROG_VARIANT;
   public static final ComponentType<HorseVariantComponent> HORSE_VARIANT;
   public static final ComponentType<PaintingVariantComponent> PAINTING_VARIANT;
   public static final ComponentType<LlamaVariantComponent> LLAMA_VARIANT;
   public static final ComponentType<AxolotlVariantComponent> AXOLOTL_VARIANT;
   public static final ComponentType<CatVariantComponent> CAT_VARIANT;
   public static final ComponentType<CatCollarComponent> CAT_COLLAR;
   public static final ComponentType<SheepColorComponent> SHEEP_COLOR;
   public static final ComponentType<ShulkerColorComponent> SHULKER_COLOR;
   public static final ComponentType<ItemUseEffects> USE_EFFECTS;
   public static final ComponentType<ItemMinimumAttackCharge> MINIMUM_ATTACK_CHARGE;
   public static final ComponentType<ItemDamageType> DAMAGE_TYPE;
   public static final ComponentType<ItemAttackRange> ATTACK_RANGE;
   public static final ComponentType<ItemPiercingWeapon> PIERCING_WEAPON;
   public static final ComponentType<ItemKineticWeapon> KINETIC_WEAPON;
   public static final ComponentType<ItemSwingAnimation> SWING_ANIMATION;
   public static final ComponentType<ZombieNautilusVariantComponent> ZOMBIE_NAUTILUS_VARIANT;

   private ComponentTypes() {
   }

   @ApiStatus.Internal
   public static <T> ComponentType<T> define(String key) {
      return define(key, (PacketWrapper.Reader)null, (PacketWrapper.Writer)null);
   }

   @ApiStatus.Internal
   public static <T> ComponentType<T> define(String key, @Nullable PacketWrapper.Reader<T> reader, @Nullable PacketWrapper.Writer<T> writer) {
      return (ComponentType)REGISTRY.define(key, (data) -> {
         return new StaticComponentType(data, reader, writer);
      });
   }

   public static VersionedRegistry<ComponentType<?>> getRegistry() {
      return REGISTRY;
   }

   public static ComponentType<?> getByName(String name) {
      return (ComponentType)REGISTRY.getByName(name);
   }

   public static ComponentType<?> getById(ClientVersion version, int id) {
      return (ComponentType)REGISTRY.getById(version, id);
   }

   public static Collection<ComponentType<?>> values() {
      return REGISTRY.getEntries();
   }

   static {
      UNBREAKABLE = UNBREAKABLE_MODERN.legacyMap(ItemUnbreakable::isShowInTooltip, ItemUnbreakable::new);
      CUSTOM_NAME = define("custom_name", PacketWrapper::readComponent, PacketWrapper::writeComponent);
      ITEM_NAME = define("item_name", PacketWrapper::readComponent, PacketWrapper::writeComponent);
      LORE = define("lore", ItemLore::read, ItemLore::write);
      RARITY = define("rarity", (wrapper) -> {
         return (ItemRarity)wrapper.readEnum((Enum[])ItemRarity.values());
      }, PacketWrapper::writeEnum);
      ENCHANTMENTS = define("enchantments", ItemEnchantments::read, ItemEnchantments::write);
      CAN_PLACE_ON = define("can_place_on", ItemAdventurePredicate::read, ItemAdventurePredicate::write);
      CAN_BREAK = define("can_break", ItemAdventurePredicate::read, ItemAdventurePredicate::write);
      ATTRIBUTE_MODIFIERS = define("attribute_modifiers", ItemAttributeModifiers::read, ItemAttributeModifiers::write);
      CUSTOM_MODEL_DATA_LISTS = define("custom_model_data", ItemCustomModelData::read, ItemCustomModelData::write);
      CUSTOM_MODEL_DATA = CUSTOM_MODEL_DATA_LISTS.legacyMap(ItemCustomModelData::getLegacyId, ItemCustomModelData::new);
      HIDE_ADDITIONAL_TOOLTIP = define("hide_additional_tooltip", Dummy::dummyRead, Dummy::dummyWrite);
      HIDE_TOOLTIP = define("hide_tooltip", Dummy::dummyRead, Dummy::dummyWrite);
      REPAIR_COST = define("repair_cost", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
      CREATIVE_SLOT_LOCK = define("creative_slot_lock", Dummy::dummyRead, Dummy::dummyWrite);
      ENCHANTMENT_GLINT_OVERRIDE = define("enchantment_glint_override", PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
      INTANGIBLE_PROJECTILE = define("intangible_projectile", Dummy::dummyReadNbt, Dummy::dummyWriteNbt);
      FOOD = define("food", FoodProperties::read, FoodProperties::write);
      FIRE_RESISTANT = define("fire_resistant", Dummy::dummyRead, Dummy::dummyWrite);
      TOOL = define("tool", ItemTool::read, ItemTool::write);
      STORED_ENCHANTMENTS = define("stored_enchantments", ItemEnchantments::read, ItemEnchantments::write);
      DYED_COLOR = define("dyed_color", ItemDyeColor::read, ItemDyeColor::write);
      MAP_COLOR = define("map_color", PacketWrapper::readInt, PacketWrapper::writeInt);
      MAP_ID = define("map_id", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
      MAP_DECORATIONS = define("map_decorations", ItemMapDecorations::read, ItemMapDecorations::write);
      MAP_POST_PROCESSING = define("map_post_processing", (wrapper) -> {
         return (ItemMapPostProcessingState)wrapper.readEnum((Enum[])ItemMapPostProcessingState.values());
      }, PacketWrapper::writeEnum);
      CHARGED_PROJECTILES = define("charged_projectiles", ChargedProjectiles::read, ChargedProjectiles::write);
      BUNDLE_CONTENTS = define("bundle_contents", BundleContents::read, BundleContents::write);
      POTION_CONTENTS = define("potion_contents", ItemPotionContents::read, ItemPotionContents::write);
      SUSPICIOUS_STEW_EFFECTS = define("suspicious_stew_effects", SuspiciousStewEffects::read, SuspiciousStewEffects::write);
      WRITABLE_BOOK_CONTENT = define("writable_book_content", WritableBookContent::read, WritableBookContent::write);
      WRITTEN_BOOK_CONTENT = define("written_book_content", WrittenBookContent::read, WrittenBookContent::write);
      TRIM = define("trim", ArmorTrim::read, ArmorTrim::write);
      DEBUG_STICK_STATE = define("debug_stick_state", DebugStickState::read, DebugStickState::write);
      TYPED_ENTITY_DATA = define("entity_data", TypedEntityData::read, TypedEntityData::write);
      ENTITY_DATA = TYPED_ENTITY_DATA.legacyMap(TypedEntityData::getCompound, TypedEntityData::new);
      BUCKET_ENTITY_DATA = define("bucket_entity_data", PacketWrapper::readNBT, PacketWrapper::writeNBT);
      TYPED_BLOCK_ENTITY_DATA = define("block_entity_data", TypedBlockEntityData::read, TypedBlockEntityData::write);
      BLOCK_ENTITY_DATA = TYPED_BLOCK_ENTITY_DATA.legacyMap(TypedBlockEntityData::getCompound, TypedBlockEntityData::new);
      ITEM_INSTRUMENT = define("instrument", ItemInstrument::read, ItemInstrument::write);
      INSTRUMENT = ITEM_INSTRUMENT.legacyMap((inst) -> {
         return (Instrument)inst.getInstrument().getValue();
      }, (inst) -> {
         return new ItemInstrument(new MaybeMappedEntity(inst));
      });
      OMINOUS_BOTTLE_AMPLIFIER = define("ominous_bottle_amplifier", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
      RECIPES = define("recipes", ItemRecipes::read, ItemRecipes::write);
      LODESTONE_TRACKER = define("lodestone_tracker", LodestoneTracker::read, LodestoneTracker::write);
      FIREWORK_EXPLOSION = define("firework_explosion", FireworkExplosion::read, FireworkExplosion::write);
      FIREWORKS = define("fireworks", ItemFireworks::read, ItemFireworks::write);
      PROFILE = define("profile", ItemProfile::read, ItemProfile::write);
      NOTE_BLOCK_SOUND = define("note_block_sound", PacketWrapper::readIdentifier, PacketWrapper::writeIdentifier);
      BANNER_PATTERNS = define("banner_patterns", BannerLayers::read, BannerLayers::write);
      BASE_COLOR = define("base_color", (wrapper) -> {
         return (DyeColor)wrapper.readEnum((Enum[])DyeColor.values());
      }, PacketWrapper::writeEnum);
      POT_DECORATIONS = define("pot_decorations", PotDecorations::read, PotDecorations::write);
      CONTAINER = define("container", ItemContainerContents::read, ItemContainerContents::write);
      BLOCK_STATE = define("block_state", ItemBlockStateProperties::read, ItemBlockStateProperties::write);
      BEES = define("bees", ItemBees::read, ItemBees::write);
      LOCK = define("lock", ItemLock::read, ItemLock::write);
      CONTAINER_LOOT = define("container_loot", ItemContainerLoot::read, ItemContainerLoot::write);
      JUKEBOX_PLAYABLE = define("jukebox_playable", ItemJukeboxPlayable::read, ItemJukeboxPlayable::write);
      CONSUMABLE = define("consumable", ItemConsumable::read, ItemConsumable::write);
      USE_REMAINDER = define("use_remainder", ItemUseRemainder::read, ItemUseRemainder::write);
      USE_COOLDOWN = define("use_cooldown", ItemUseCooldown::read, ItemUseCooldown::write);
      ENCHANTABLE = define("enchantable", ItemEnchantable::read, ItemEnchantable::write);
      REPAIRABLE = define("repairable", ItemRepairable::read, ItemRepairable::write);
      ITEM_MODEL = define("item_model", ItemModel::read, ItemModel::write);
      DAMAGE_RESISTANT = define("damage_resistant", ItemDamageResistant::read, ItemDamageResistant::write);
      EQUIPPABLE = define("equippable", ItemEquippable::read, ItemEquippable::write);
      GLIDER = define("glider", Dummy::dummyRead, Dummy::dummyWrite);
      DEATH_PROTECTION = define("death_protection", ItemDeathProtection::read, ItemDeathProtection::write);
      TOOLTIP_STYLE = define("tooltip_style", ItemTooltipStyle::read, ItemTooltipStyle::write);
      TOOLTIP_DISPLAY = define("tooltip_display", ItemTooltipDisplay::read, ItemTooltipDisplay::write);
      WEAPON = define("weapon", ItemWeapon::read, ItemWeapon::write);
      BLOCKS_ATTACKS = define("blocks_attacks", ItemBlocksAttacks::read, ItemBlocksAttacks::write);
      POTION_DURATION_SCALE = define("potion_duration_scale", ItemPotionDurationScale::read, ItemPotionDurationScale::write);
      PROVIDES_TRIM_MATERIAL = define("provides_trim_material", ItemProvidesTrimMaterial::read, ItemProvidesTrimMaterial::write);
      PROVIDES_BANNER_PATTERNS = define("provides_banner_patterns", ItemProvidesBannerPatterns::read, ItemProvidesBannerPatterns::write);
      BREAK_SOUND = define("break_sound", ItemBreakSound::read, ItemBreakSound::write);
      VILLAGER_VARIANT = define("villager/variant", VillagerVariantComponent::read, VillagerVariantComponent::write);
      WOLF_VARIANT = define("wolf/variant", WolfVariantComponent::read, WolfVariantComponent::write);
      WOLF_SOUND_VARIANT = define("wolf/sound_variant", WolfSoundVariantComponent::read, WolfSoundVariantComponent::write);
      WOLF_COLLAR = define("wolf/collar", WolfCollarComponent::read, WolfCollarComponent::write);
      FOX_VARIANT = define("fox/variant", FoxVariantComponent::read, FoxVariantComponent::write);
      SALMON_SIZE = define("salmon/size", SalmonSizeComponent::read, SalmonSizeComponent::write);
      PARROT_VARIANT = define("parrot/variant", ParrotVariantComponent::read, ParrotVariantComponent::write);
      TROPICAL_FISH_PATTERN = define("tropical_fish/pattern", TropicalFishPatternComponent::read, TropicalFishPatternComponent::write);
      TROPICAL_FISH_BASE_COLOR = define("tropical_fish/base_color", TropicalFishBaseColorComponent::read, TropicalFishBaseColorComponent::write);
      TROPICAL_FISH_PATTERN_COLOR = define("tropical_fish/pattern_color", TropicalFishPatternColorComponent::read, TropicalFishPatternColorComponent::write);
      MOOSHROOM_VARIANT = define("mooshroom/variant", MooshroomVariantComponent::read, MooshroomVariantComponent::write);
      RABBIT_VARIANT = define("rabbit/variant", RabbitVariantComponent::read, RabbitVariantComponent::write);
      PIG_VARIANT = define("pig/variant", PigVariantComponent::read, PigVariantComponent::write);
      COW_VARIANT = define("cow/variant", CowVariantComponent::read, CowVariantComponent::write);
      CHICKEN_VARIANT = define("chicken/variant", ChickenVariantComponent::read, ChickenVariantComponent::write);
      FROG_VARIANT = define("frog/variant", FrogVariantComponent::read, FrogVariantComponent::write);
      HORSE_VARIANT = define("horse/variant", HorseVariantComponent::read, HorseVariantComponent::write);
      PAINTING_VARIANT = define("painting/variant", PaintingVariantComponent::read, PaintingVariantComponent::write);
      LLAMA_VARIANT = define("llama/variant", LlamaVariantComponent::read, LlamaVariantComponent::write);
      AXOLOTL_VARIANT = define("axolotl/variant", AxolotlVariantComponent::read, AxolotlVariantComponent::write);
      CAT_VARIANT = define("cat/variant", CatVariantComponent::read, CatVariantComponent::write);
      CAT_COLLAR = define("cat/collar", CatCollarComponent::read, CatCollarComponent::write);
      SHEEP_COLOR = define("sheep/color", SheepColorComponent::read, SheepColorComponent::write);
      SHULKER_COLOR = define("shulker/color", ShulkerColorComponent::read, ShulkerColorComponent::write);
      USE_EFFECTS = define("use_effects", ItemUseEffects::read, ItemUseEffects::write);
      MINIMUM_ATTACK_CHARGE = define("minimum_attack_charge", ItemMinimumAttackCharge::read, ItemMinimumAttackCharge::write);
      DAMAGE_TYPE = define("damage_type", ItemDamageType::read, ItemDamageType::write);
      ATTACK_RANGE = define("attack_range", ItemAttackRange::read, ItemAttackRange::write);
      PIERCING_WEAPON = define("piercing_weapon", ItemPiercingWeapon::read, ItemPiercingWeapon::write);
      KINETIC_WEAPON = define("kinetic_weapon", ItemKineticWeapon::read, ItemKineticWeapon::write);
      SWING_ANIMATION = define("swing_animation", ItemSwingAnimation::read, ItemSwingAnimation::write);
      ZOMBIE_NAUTILUS_VARIANT = define("zombie_nautilus/variant", ZombieNautilusVariantComponent::read, ZombieNautilusVariantComponent::write);
      REGISTRY.unloadMappings();
   }
}
