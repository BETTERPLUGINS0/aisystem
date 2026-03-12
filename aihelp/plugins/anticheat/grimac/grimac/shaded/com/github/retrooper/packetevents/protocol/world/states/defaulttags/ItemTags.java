package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.jetbrains.annotations.VisibleForTesting;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ItemTags {
   private static final HashMap<String, ItemTags> byName = new HashMap();
   public static final ItemTags WOOL = bind("wool");
   public static final ItemTags PLANKS = bind("planks");
   public static final ItemTags STONE_BRICKS = bind("stone_bricks");
   public static final ItemTags WOODEN_BUTTONS = bind("wooden_buttons");
   public static final ItemTags STONE_BUTTONS = bind("stone_buttons");
   public static final ItemTags BUTTONS = bind("buttons");
   public static final ItemTags WOOL_CARPETS = bind("carpets");
   public static final ItemTags WOODEN_DOORS = bind("wooden_doors");
   public static final ItemTags WOODEN_STAIRS = bind("wooden_stairs");
   public static final ItemTags WOODEN_SLABS = bind("wooden_slabs");
   public static final ItemTags WOODEN_FENCES = bind("wooden_fences");
   public static final ItemTags FENCE_GATES = bind("fence_gates");
   public static final ItemTags WOODEN_PRESSURE_PLATES = bind("wooden_pressure_plates");
   public static final ItemTags WOODEN_TRAPDOORS = bind("wooden_trapdoors");
   public static final ItemTags DOORS = bind("doors");
   public static final ItemTags SAPLINGS = bind("saplings");
   public static final ItemTags LOGS_THAT_BURN = bind("logs_that_burn");
   public static final ItemTags LOGS = bind("logs");
   public static final ItemTags DARK_OAK_LOGS = bind("dark_oak_logs");
   public static final ItemTags OAK_LOGS = bind("oak_logs");
   public static final ItemTags BIRCH_LOGS = bind("birch_logs");
   public static final ItemTags ACACIA_LOGS = bind("acacia_logs");
   public static final ItemTags CHERRY_LOGS = bind("cherry_logs");
   public static final ItemTags JUNGLE_LOGS = bind("jungle_logs");
   public static final ItemTags SPRUCE_LOGS = bind("spruce_logs");
   public static final ItemTags MANGROVE_LOGS = bind("mangrove_logs");
   public static final ItemTags CRIMSON_STEMS = bind("crimson_stems");
   public static final ItemTags WARPED_STEMS = bind("warped_stems");
   public static final ItemTags BAMBOO_BLOCKS = bind("bamboo_blocks");
   public static final ItemTags WART_BLOCKS = bind("wart_blocks");
   public static final ItemTags BANNERS = bind("banners");
   public static final ItemTags SAND = bind("sand");
   public static final ItemTags SMELTS_TO_GLASS = bind("smelts_to_glass");
   public static final ItemTags STAIRS = bind("stairs");
   public static final ItemTags SLABS = bind("slabs");
   public static final ItemTags WALLS = bind("walls");
   public static final ItemTags ANVIL = bind("anvil");
   public static final ItemTags RAILS = bind("rails");
   public static final ItemTags LEAVES = bind("leaves");
   public static final ItemTags TRAPDOORS = bind("trapdoors");
   public static final ItemTags SMALL_FLOWERS = bind("small_flowers");
   public static final ItemTags BEDS = bind("beds");
   public static final ItemTags FENCES = bind("fences");
   /** @deprecated */
   @Deprecated
   public static final ItemTags TALL_FLOWERS = bind("tall_flowers");
   public static final ItemTags FLOWERS = bind("flowers");
   public static final ItemTags PIGLIN_REPELLENTS = bind("piglin_repellents");
   public static final ItemTags PIGLIN_LOVED = bind("piglin_loved");
   public static final ItemTags IGNORED_BY_PIGLIN_BABIES = bind("ignored_by_piglin_babies");
   public static final ItemTags PIGLIN_FOOD = bind("piglin_food");
   public static final ItemTags FOX_FOOD = bind("fox_food");
   public static final ItemTags GOLD_ORES = bind("gold_ores");
   public static final ItemTags IRON_ORES = bind("iron_ores");
   public static final ItemTags DIAMOND_ORES = bind("diamond_ores");
   public static final ItemTags REDSTONE_ORES = bind("redstone_ores");
   public static final ItemTags LAPIS_ORES = bind("lapis_ores");
   public static final ItemTags COAL_ORES = bind("coal_ores");
   public static final ItemTags EMERALD_ORES = bind("emerald_ores");
   public static final ItemTags COPPER_ORES = bind("copper_ores");
   public static final ItemTags NON_FLAMMABLE_WOOD = bind("non_flammable_wood");
   public static final ItemTags SOUL_FIRE_BASE_BLOCKS = bind("soul_fire_base_blocks");
   public static final ItemTags CANDLES = bind("candles");
   public static final ItemTags DIRT = bind("dirt");
   public static final ItemTags TERRACOTTA = bind("terracotta");
   public static final ItemTags COMPLETES_FIND_TREE_TUTORIAL = bind("completes_find_tree_tutorial");
   public static final ItemTags BOATS = bind("boats");
   public static final ItemTags CHEST_BOATS = bind("chest_boats");
   public static final ItemTags FISHES = bind("fishes");
   public static final ItemTags SIGNS = bind("signs");
   /** @deprecated */
   @Deprecated
   public static final ItemTags MUSIC_DISCS = bind("music_discs");
   public static final ItemTags CREEPER_DROP_MUSIC_DISCS = bind("creeper_drop_music_discs");
   public static final ItemTags COALS = bind("coals");
   public static final ItemTags ARROWS = bind("arrows");
   public static final ItemTags LECTERN_BOOKS = bind("lectern_books");
   public static final ItemTags BOOKSHELF_BOOKS = bind("bookshelf_books");
   public static final ItemTags BEACON_PAYMENT_ITEMS = bind("beacon_payment_items");
   public static final ItemTags STONE_TOOL_MATERIALS = bind("stone_tool_materials");
   public static final ItemTags STONE_CRAFTING_MATERIALS = bind("stone_crafting_materials");
   public static final ItemTags FREEZE_IMMUNE_WEARABLES = bind("freeze_immune_wearables");
   public static final ItemTags DAMPENS_VIBRATIONS = bind("dampens_vibrations");
   public static final ItemTags CLUSTER_MAX_HARVESTABLES = bind("cluster_max_harvestables");
   public static final ItemTags COMPASSES = bind("compasses");
   public static final ItemTags HANGING_SIGNS = bind("hanging_signs");
   public static final ItemTags CREEPER_IGNITERS = bind("creeper_igniters");
   public static final ItemTags NOTEBLOCK_TOP_INSTRUMENTS = bind("noteblock_top_instruments");
   public static final ItemTags TRIMMABLE_ARMOR = bind("trimmable_armor");
   public static final ItemTags TRIM_MATERIALS = bind("trim_materials");
   /** @deprecated */
   @Deprecated
   public static final ItemTags TRIM_TEMPLATES = bind("trim_templates");
   public static final ItemTags SNIFFER_FOOD = bind("sniffer_food");
   public static final ItemTags DECORATED_POT_SHERDS = bind("decorated_pot_sherds");
   public static final ItemTags DECORATED_POT_INGREDIENTS = bind("decorated_pot_ingredients");
   public static final ItemTags SWORDS = bind("swords");
   public static final ItemTags AXES = bind("axes");
   public static final ItemTags HOES = bind("hoes");
   public static final ItemTags PICKAXES = bind("pickaxes");
   public static final ItemTags SHOVELS = bind("shovels");
   public static final ItemTags BREAKS_DECORATED_POTS = bind("breaks_decorated_pots");
   /** @deprecated */
   @Deprecated
   public static final ItemTags TOOLS;
   public static final ItemTags VILLAGER_PLANTABLE_SEEDS;
   public static final ItemTags ARMADILLO_FOOD;
   public static final ItemTags AXOLOTL_FOOD;
   /** @deprecated */
   @Deprecated
   public static final ItemTags AXOLOTL_TEMPT_ITEMS;
   public static final ItemTags BEE_FOOD;
   public static final ItemTags CAMEL_FOOD;
   public static final ItemTags CAT_FOOD;
   public static final ItemTags CHEST_ARMOR;
   public static final ItemTags CHICKEN_FOOD;
   public static final ItemTags COW_FOOD;
   public static final ItemTags DYEABLE;
   public static final ItemTags ENCHANTABLE_ARMOR;
   public static final ItemTags ENCHANTABLE_BOW;
   public static final ItemTags ENCHANTABLE_CHEST_ARMOR;
   public static final ItemTags ENCHANTABLE_CROSSBOW;
   public static final ItemTags ENCHANTABLE_DURABILITY;
   public static final ItemTags ENCHANTABLE_EQUIPPABLE;
   public static final ItemTags ENCHANTABLE_FIRE_ASPECT;
   public static final ItemTags ENCHANTABLE_FISHING;
   public static final ItemTags ENCHANTABLE_FOOT_ARMOR;
   public static final ItemTags ENCHANTABLE_HEAD_ARMOR;
   public static final ItemTags ENCHANTABLE_LEG_ARMOR;
   public static final ItemTags ENCHANTABLE_MACE;
   public static final ItemTags ENCHANTABLE_MINING;
   public static final ItemTags ENCHANTABLE_MINING_LOOT;
   public static final ItemTags ENCHANTABLE_SHARP_WEAPON;
   public static final ItemTags ENCHANTABLE_SWORD;
   public static final ItemTags ENCHANTABLE_TRIDENT;
   public static final ItemTags ENCHANTABLE_VANISHING;
   public static final ItemTags ENCHANTABLE_WEAPON;
   public static final ItemTags FOOT_ARMOR;
   public static final ItemTags FROG_FOOD;
   public static final ItemTags GOAT_FOOD;
   public static final ItemTags HEAD_ARMOR;
   public static final ItemTags HOGLIN_FOOD;
   public static final ItemTags HORSE_FOOD;
   public static final ItemTags HORSE_TEMPT_ITEMS;
   public static final ItemTags LEG_ARMOR;
   public static final ItemTags LLAMA_FOOD;
   public static final ItemTags LLAMA_TEMPT_ITEMS;
   public static final ItemTags MEAT;
   public static final ItemTags OCELOT_FOOD;
   public static final ItemTags PANDA_FOOD;
   public static final ItemTags PARROT_FOOD;
   public static final ItemTags PARROT_POISONOUS_FOOD;
   public static final ItemTags PIG_FOOD;
   public static final ItemTags RABBIT_FOOD;
   public static final ItemTags SHEEP_FOOD;
   public static final ItemTags SKULLS;
   public static final ItemTags STRIDER_FOOD;
   public static final ItemTags STRIDER_TEMPT_ITEMS;
   public static final ItemTags TURTLE_FOOD;
   public static final ItemTags WOLF_FOOD;
   public static final ItemTags PALE_OAK_LOGS;
   public static final ItemTags PIGLIN_SAFE_ARMOR;
   public static final ItemTags DUPLICATES_ALLAYS;
   public static final ItemTags BREWING_FUEL;
   public static final ItemTags SHULKER_BOXES;
   public static final ItemTags IRON_TOOL_MATERIALS;
   public static final ItemTags GOLD_TOOL_MATERIALS;
   public static final ItemTags DIAMOND_TOOL_MATERIALS;
   public static final ItemTags NETHERITE_TOOL_MATERIALS;
   public static final ItemTags REPAIRS_LEATHER_ARMOR;
   public static final ItemTags REPAIRS_CHAIN_ARMOR;
   public static final ItemTags REPAIRS_IRON_ARMOR;
   public static final ItemTags REPAIRS_GOLD_ARMOR;
   public static final ItemTags REPAIRS_DIAMOND_ARMOR;
   public static final ItemTags REPAIRS_NETHERITE_ARMOR;
   public static final ItemTags REPAIRS_TURTLE_HELMET;
   public static final ItemTags REPAIRS_WOLF_ARMOR;
   public static final ItemTags FURNACE_MINECART_FUEL;
   public static final ItemTags BUNDLES;
   public static final ItemTags MAP_INVISIBILITY_EQUIPMENT;
   public static final ItemTags GAZE_DISGUISE_EQUIPMENT;
   public static final ItemTags PANDA_EATS_FROM_GROUND;
   public static final ItemTags WOODEN_TOOL_MATERIALS;
   public static final ItemTags VILLAGER_PICKS_UP;
   public static final ItemTags SKELETON_PREFERRED_WEAPONS;
   public static final ItemTags DROWNED_PREFERRED_WEAPONS;
   public static final ItemTags PIGLIN_PREFERRED_WEAPONS;
   public static final ItemTags PILLAGER_PREFERRED_WEAPONS;
   public static final ItemTags WITHER_SKELETON_DISLIKED_WEAPONS;
   public static final ItemTags EGGS;
   public static final ItemTags BOOK_CLONING_TARGET;
   public static final ItemTags HARNESSES;
   public static final ItemTags HAPPY_GHAST_FOOD;
   public static final ItemTags HAPPY_GHAST_TEMPT_ITEMS;
   public static final ItemTags WOODEN_SHELVES;
   public static final ItemTags COPPER_CHESTS;
   public static final ItemTags LIGHTNING_RODS;
   public static final ItemTags COPPER_GOLEM_STATUES;
   public static final ItemTags COPPER;
   public static final ItemTags CHAINS;
   public static final ItemTags LANTERNS;
   public static final ItemTags BARS;
   public static final ItemTags COPPER_TOOL_MATERIALS;
   public static final ItemTags REPAIRS_COPPER_ARMOR;
   public static final ItemTags SHEARABLE_FROM_COPPER_GOLEM;
   public static final ItemTags ZOMBIE_HORSE_FOOD;
   public static final ItemTags CAMEL_HUSK_FOOD;
   public static final ItemTags NAUTILUS_BUCKET_FOOD;
   public static final ItemTags NAUTILUS_TAMING_ITEMS;
   public static final ItemTags SPEARS;
   public static final ItemTags NAUTILUS_FOOD;
   public static final ItemTags ENCHANTABLE_MELEE_WEAPON;
   public static final ItemTags ENCHANTABLE_SWEEPING;
   public static final ItemTags ENCHANTABLE_LUNGE;
   String name;
   Set<ItemType> states = new HashSet();
   boolean reallyEmpty;

   public ItemTags(final String name) {
      byName.put(name, this);
      this.name = name;
   }

   private static ItemTags bind(final String s) {
      return new ItemTags(s);
   }

   private static void copy(ItemTags src, ItemTags dst) {
      dst.states.addAll(src.states);
   }

   private static void copy(BlockTags tag, ItemTags itemTag) {
      Iterator var2 = tag.getStates().iterator();

      while(var2.hasNext()) {
         StateType state = (StateType)var2.next();
         itemTag.states.add(ItemTypes.getTypePlacingState(state));
      }

      itemTag.states.remove((Object)null);
   }

   private ItemTags add(ItemType... state) {
      Collections.addAll(this.states, state);
      return this;
   }

   private ItemTags addTag(ItemTags tags) {
      if (tags.states.isEmpty()) {
         throw new IllegalArgumentException("Tag " + tags.name + " is empty when adding to " + this.name + ", you (packetevents updater) probably messed up the item tags order!!");
      } else {
         this.states.addAll(tags.states);
         return this;
      }
   }

   public boolean contains(ItemType state) {
      return this.states.contains(state);
   }

   public String getName() {
      return this.name;
   }

   public ItemTags getByName(String name) {
      return (ItemTags)byName.get(name);
   }

   public Set<ItemType> getStates() {
      return this.states;
   }

   @VisibleForTesting
   public boolean isReallyEmpty() {
      return this.reallyEmpty;
   }

   static {
      TOOLS = BREAKS_DECORATED_POTS;
      VILLAGER_PLANTABLE_SEEDS = bind("villager_plantable_seeds");
      ARMADILLO_FOOD = bind("armadillo_food");
      AXOLOTL_FOOD = bind("axolotl_food");
      AXOLOTL_TEMPT_ITEMS = AXOLOTL_FOOD;
      BEE_FOOD = bind("bee_food");
      CAMEL_FOOD = bind("camel_food");
      CAT_FOOD = bind("cat_food");
      CHEST_ARMOR = bind("chest_armor");
      CHICKEN_FOOD = bind("chicken_food");
      COW_FOOD = bind("cow_food");
      DYEABLE = bind("dyeable");
      ENCHANTABLE_ARMOR = bind("enchantable/armor");
      ENCHANTABLE_BOW = bind("enchantable/bow");
      ENCHANTABLE_CHEST_ARMOR = bind("enchantable/chest_armor");
      ENCHANTABLE_CROSSBOW = bind("enchantable/crossbow");
      ENCHANTABLE_DURABILITY = bind("enchantable/durability");
      ENCHANTABLE_EQUIPPABLE = bind("enchantable/equippable");
      ENCHANTABLE_FIRE_ASPECT = bind("enchantable/fire_aspect");
      ENCHANTABLE_FISHING = bind("enchantable/fishing");
      ENCHANTABLE_FOOT_ARMOR = bind("enchantable/foot_armor");
      ENCHANTABLE_HEAD_ARMOR = bind("enchantable/head_armor");
      ENCHANTABLE_LEG_ARMOR = bind("enchantable/leg_armor");
      ENCHANTABLE_MACE = bind("enchantable/mace");
      ENCHANTABLE_MINING = bind("enchantable/mining");
      ENCHANTABLE_MINING_LOOT = bind("enchantable/mining_loot");
      ENCHANTABLE_SHARP_WEAPON = bind("enchantable/sharp_weapon");
      ENCHANTABLE_SWORD = bind("enchantable/sword");
      ENCHANTABLE_TRIDENT = bind("enchantable/trident");
      ENCHANTABLE_VANISHING = bind("enchantable/vanishing");
      ENCHANTABLE_WEAPON = bind("enchantable/weapon");
      FOOT_ARMOR = bind("foot_armor");
      FROG_FOOD = bind("frog_food");
      GOAT_FOOD = bind("goat_food");
      HEAD_ARMOR = bind("head_armor");
      HOGLIN_FOOD = bind("hoglin_food");
      HORSE_FOOD = bind("horse_food");
      HORSE_TEMPT_ITEMS = bind("horse_tempt_items");
      LEG_ARMOR = bind("leg_armor");
      LLAMA_FOOD = bind("llama_food");
      LLAMA_TEMPT_ITEMS = bind("llama_tempt_items");
      MEAT = bind("meat");
      OCELOT_FOOD = bind("ocelot_food");
      PANDA_FOOD = bind("panda_food");
      PARROT_FOOD = bind("parrot_food");
      PARROT_POISONOUS_FOOD = bind("parrot_poisonous_food");
      PIG_FOOD = bind("pig_food");
      RABBIT_FOOD = bind("rabbit_food");
      SHEEP_FOOD = bind("sheep_food");
      SKULLS = bind("skulls");
      STRIDER_FOOD = bind("strider_food");
      STRIDER_TEMPT_ITEMS = bind("strider_tempt_items");
      TURTLE_FOOD = bind("turtle_food");
      WOLF_FOOD = bind("wolf_food");
      PALE_OAK_LOGS = bind("pale_oak_logs");
      PIGLIN_SAFE_ARMOR = bind("piglin_safe_armor");
      DUPLICATES_ALLAYS = bind("duplicates_allays");
      BREWING_FUEL = bind("brewing_fuel");
      SHULKER_BOXES = bind("shulker_boxes");
      IRON_TOOL_MATERIALS = bind("iron_tool_materials");
      GOLD_TOOL_MATERIALS = bind("gold_tool_materials");
      DIAMOND_TOOL_MATERIALS = bind("diamond_tool_materials");
      NETHERITE_TOOL_MATERIALS = bind("netherite_tool_materials");
      REPAIRS_LEATHER_ARMOR = bind("repairs_leather_armor");
      REPAIRS_CHAIN_ARMOR = bind("repairs_chain_armor");
      REPAIRS_IRON_ARMOR = bind("repairs_iron_armor");
      REPAIRS_GOLD_ARMOR = bind("repairs_gold_armor");
      REPAIRS_DIAMOND_ARMOR = bind("repairs_diamond_armor");
      REPAIRS_NETHERITE_ARMOR = bind("repairs_netherite_armor");
      REPAIRS_TURTLE_HELMET = bind("repairs_turtle_helmet");
      REPAIRS_WOLF_ARMOR = bind("repairs_wolf_armor");
      FURNACE_MINECART_FUEL = bind("furnace_minecart_fuel");
      BUNDLES = bind("bundles");
      MAP_INVISIBILITY_EQUIPMENT = bind("map_invisibility_equipment");
      GAZE_DISGUISE_EQUIPMENT = bind("gaze_disguise_equipment");
      PANDA_EATS_FROM_GROUND = bind("panda_eats_from_ground");
      WOODEN_TOOL_MATERIALS = bind("wooden_tool_materials");
      VILLAGER_PICKS_UP = bind("villager_picks_up");
      SKELETON_PREFERRED_WEAPONS = bind("skeleton_preferred_weapons");
      DROWNED_PREFERRED_WEAPONS = bind("drowned_preferred_weapons");
      PIGLIN_PREFERRED_WEAPONS = bind("piglin_preferred_weapons");
      PILLAGER_PREFERRED_WEAPONS = bind("pillager_preferred_weapons");
      WITHER_SKELETON_DISLIKED_WEAPONS = bind("wither_skeleton_disliked_weapons");
      EGGS = bind("eggs");
      BOOK_CLONING_TARGET = bind("book_cloning_target");
      HARNESSES = bind("harnesses");
      HAPPY_GHAST_FOOD = bind("happy_ghast_food");
      HAPPY_GHAST_TEMPT_ITEMS = bind("happy_ghast_tempt_items");
      WOODEN_SHELVES = bind("wooden_shelves");
      COPPER_CHESTS = bind("copper_chests");
      LIGHTNING_RODS = bind("lightning_rods");
      COPPER_GOLEM_STATUES = bind("copper_golem_statues");
      COPPER = bind("copper");
      CHAINS = bind("chains");
      LANTERNS = bind("lanterns");
      BARS = bind("bars");
      COPPER_TOOL_MATERIALS = bind("copper_tool_materials");
      REPAIRS_COPPER_ARMOR = bind("repairs_copper_armor");
      SHEARABLE_FROM_COPPER_GOLEM = bind("shearable_from_copper_golem");
      ZOMBIE_HORSE_FOOD = bind("shearable_from_copper_golem");
      CAMEL_HUSK_FOOD = bind("camel_husk_food");
      NAUTILUS_BUCKET_FOOD = bind("nautilus_bucket_food");
      NAUTILUS_TAMING_ITEMS = bind("nautilus_taming_items");
      SPEARS = bind("spears");
      NAUTILUS_FOOD = bind("nautilus_food");
      ENCHANTABLE_MELEE_WEAPON = bind("enchantable/melee_weapon");
      ENCHANTABLE_SWEEPING = bind("enchantable/sweeping");
      ENCHANTABLE_LUNGE = bind("enchantable/lunge");
      copy(BlockTags.WOOL, WOOL);
      copy(BlockTags.PLANKS, PLANKS);
      copy(BlockTags.STONE_BRICKS, STONE_BRICKS);
      copy(BlockTags.WOODEN_BUTTONS, WOODEN_BUTTONS);
      copy(BlockTags.STONE_BUTTONS, STONE_BUTTONS);
      copy(BlockTags.WOOL_CARPETS, WOOL_CARPETS);
      copy(BlockTags.WOODEN_DOORS, WOODEN_DOORS);
      copy(BlockTags.WOODEN_STAIRS, WOODEN_STAIRS);
      copy(BlockTags.WOODEN_SLABS, WOODEN_SLABS);
      copy(BlockTags.WOODEN_FENCES, WOODEN_FENCES);
      copy(BlockTags.FENCE_GATES, FENCE_GATES);
      copy(BlockTags.WOODEN_PRESSURE_PLATES, WOODEN_PRESSURE_PLATES);
      copy(BlockTags.WOODEN_SHELVES, WOODEN_SHELVES);
      copy(BlockTags.SAPLINGS, SAPLINGS);
      copy(BlockTags.BAMBOO_BLOCKS, BAMBOO_BLOCKS);
      copy(BlockTags.OAK_LOGS, OAK_LOGS);
      copy(BlockTags.DARK_OAK_LOGS, DARK_OAK_LOGS);
      copy(BlockTags.PALE_OAK_LOGS, PALE_OAK_LOGS);
      copy(BlockTags.BIRCH_LOGS, BIRCH_LOGS);
      copy(BlockTags.ACACIA_LOGS, ACACIA_LOGS);
      copy(BlockTags.SPRUCE_LOGS, SPRUCE_LOGS);
      copy(BlockTags.MANGROVE_LOGS, MANGROVE_LOGS);
      copy(BlockTags.JUNGLE_LOGS, JUNGLE_LOGS);
      copy(BlockTags.CHERRY_LOGS, CHERRY_LOGS);
      copy(BlockTags.CRIMSON_STEMS, CRIMSON_STEMS);
      copy(BlockTags.WARPED_STEMS, WARPED_STEMS);
      copy(BlockTags.WART_BLOCKS, WART_BLOCKS);
      copy(BlockTags.SAND, SAND);
      copy(BlockTags.SMELTS_TO_GLASS, SMELTS_TO_GLASS);
      copy(BlockTags.WALLS, WALLS);
      copy(BlockTags.ANVIL, ANVIL);
      copy(BlockTags.RAILS, RAILS);
      copy(BlockTags.LEAVES, LEAVES);
      copy(BlockTags.WOODEN_TRAPDOORS, WOODEN_TRAPDOORS);
      copy(BlockTags.SMALL_FLOWERS, SMALL_FLOWERS);
      copy(BlockTags.BEDS, BEDS);
      copy(BlockTags.SOUL_FIRE_BASE_BLOCKS, SOUL_FIRE_BASE_BLOCKS);
      copy(BlockTags.CANDLES, CANDLES);
      copy(BlockTags.GOLD_ORES, GOLD_ORES);
      copy(BlockTags.IRON_ORES, IRON_ORES);
      copy(BlockTags.DIAMOND_ORES, DIAMOND_ORES);
      copy(BlockTags.REDSTONE_ORES, REDSTONE_ORES);
      copy(BlockTags.LAPIS_ORES, LAPIS_ORES);
      copy(BlockTags.COAL_ORES, COAL_ORES);
      copy(BlockTags.EMERALD_ORES, EMERALD_ORES);
      copy(BlockTags.COPPER_ORES, COPPER_ORES);
      copy(BlockTags.DIRT, DIRT);
      copy(BlockTags.TERRACOTTA, TERRACOTTA);
      copy(BlockTags.SHULKER_BOXES, SHULKER_BOXES);
      copy(BlockTags.COPPER_CHESTS, COPPER_CHESTS);
      copy(BlockTags.LIGHTNING_RODS, LIGHTNING_RODS);
      copy(BlockTags.COPPER_GOLEM_STATUES, COPPER_GOLEM_STATUES);
      copy(BlockTags.COPPER, COPPER);
      copy(BlockTags.CHAINS, CHAINS);
      copy(BlockTags.LANTERNS, LANTERNS);
      copy(BlockTags.BARS, BARS);
      copy(BlockTags.STANDING_SIGNS, SIGNS);
      copy(BlockTags.CEILING_HANGING_SIGNS, HANGING_SIGNS);
      copy(BlockTags.BEE_ATTRACTIVE, BEE_FOOD);
      BANNERS.add(ItemTypes.WHITE_BANNER, ItemTypes.ORANGE_BANNER, ItemTypes.MAGENTA_BANNER, ItemTypes.LIGHT_BLUE_BANNER, ItemTypes.YELLOW_BANNER, ItemTypes.LIME_BANNER, ItemTypes.PINK_BANNER, ItemTypes.GRAY_BANNER, ItemTypes.LIGHT_GRAY_BANNER, ItemTypes.CYAN_BANNER, ItemTypes.PURPLE_BANNER, ItemTypes.BLUE_BANNER, ItemTypes.BROWN_BANNER, ItemTypes.GREEN_BANNER, ItemTypes.RED_BANNER, ItemTypes.BLACK_BANNER);
      PIGLIN_REPELLENTS.add(ItemTypes.SOUL_TORCH, ItemTypes.SOUL_LANTERN, ItemTypes.SOUL_CAMPFIRE);
      IGNORED_BY_PIGLIN_BABIES.add(ItemTypes.LEATHER);
      PIGLIN_SAFE_ARMOR.add(ItemTypes.GOLDEN_HELMET, ItemTypes.GOLDEN_CHESTPLATE, ItemTypes.GOLDEN_LEGGINGS, ItemTypes.GOLDEN_BOOTS);
      DUPLICATES_ALLAYS.add(ItemTypes.AMETHYST_SHARD);
      BREWING_FUEL.add(ItemTypes.BLAZE_POWDER);
      EGGS.add(ItemTypes.EGG, ItemTypes.BLUE_EGG, ItemTypes.BROWN_EGG);
      MEAT.add(ItemTypes.BEEF, ItemTypes.CHICKEN, ItemTypes.COOKED_BEEF, ItemTypes.COOKED_CHICKEN, ItemTypes.COOKED_MUTTON, ItemTypes.COOKED_PORKCHOP, ItemTypes.COOKED_RABBIT, ItemTypes.MUTTON, ItemTypes.PORKCHOP, ItemTypes.RABBIT, ItemTypes.ROTTEN_FLESH);
      SNIFFER_FOOD.add(ItemTypes.TORCHFLOWER_SEEDS);
      PIGLIN_FOOD.add(ItemTypes.PORKCHOP, ItemTypes.COOKED_PORKCHOP);
      FOX_FOOD.add(ItemTypes.SWEET_BERRIES, ItemTypes.GLOW_BERRIES);
      COW_FOOD.add(ItemTypes.WHEAT);
      copy(COW_FOOD, GOAT_FOOD);
      copy(COW_FOOD, SHEEP_FOOD);
      CAT_FOOD.add(ItemTypes.COD, ItemTypes.SALMON);
      HORSE_FOOD.add(ItemTypes.WHEAT, ItemTypes.SUGAR, ItemTypes.HAY_BLOCK, ItemTypes.APPLE, ItemTypes.CARROT, ItemTypes.GOLDEN_CARROT, ItemTypes.GOLDEN_APPLE, ItemTypes.ENCHANTED_GOLDEN_APPLE);
      ZOMBIE_HORSE_FOOD.add(ItemTypes.RED_MUSHROOM);
      HORSE_TEMPT_ITEMS.add(ItemTypes.GOLDEN_CARROT, ItemTypes.GOLDEN_APPLE, ItemTypes.ENCHANTED_GOLDEN_APPLE);
      HARNESSES.add(ItemTypes.WHITE_HARNESS, ItemTypes.ORANGE_HARNESS, ItemTypes.MAGENTA_HARNESS, ItemTypes.LIGHT_BLUE_HARNESS, ItemTypes.YELLOW_HARNESS, ItemTypes.LIME_HARNESS, ItemTypes.PINK_HARNESS, ItemTypes.GRAY_HARNESS, ItemTypes.LIGHT_GRAY_HARNESS, ItemTypes.CYAN_HARNESS, ItemTypes.PURPLE_HARNESS, ItemTypes.BLUE_HARNESS, ItemTypes.BROWN_HARNESS, ItemTypes.GREEN_HARNESS, ItemTypes.RED_HARNESS, ItemTypes.BLACK_HARNESS);
      HAPPY_GHAST_FOOD.add(ItemTypes.SNOWBALL);
      CAMEL_FOOD.add(ItemTypes.CACTUS);
      CAMEL_HUSK_FOOD.add(ItemTypes.RABBIT_FOOT);
      ARMADILLO_FOOD.add(ItemTypes.SPIDER_EYE);
      CHICKEN_FOOD.add(ItemTypes.WHEAT_SEEDS, ItemTypes.MELON_SEEDS, ItemTypes.PUMPKIN_SEEDS, ItemTypes.BEETROOT_SEEDS, ItemTypes.TORCHFLOWER_SEEDS, ItemTypes.PITCHER_POD);
      FROG_FOOD.add(ItemTypes.SLIME_BALL);
      HOGLIN_FOOD.add(ItemTypes.CRIMSON_FUNGUS);
      LLAMA_FOOD.add(ItemTypes.WHEAT, ItemTypes.HAY_BLOCK);
      LLAMA_TEMPT_ITEMS.add(ItemTypes.HAY_BLOCK);
      copy(CAT_FOOD, OCELOT_FOOD);
      PANDA_FOOD.add(ItemTypes.BAMBOO);
      PIG_FOOD.add(ItemTypes.CARROT, ItemTypes.POTATO, ItemTypes.BEETROOT);
      RABBIT_FOOD.add(ItemTypes.CARROT, ItemTypes.GOLDEN_CARROT, ItemTypes.DANDELION);
      STRIDER_FOOD.add(ItemTypes.WARPED_FUNGUS);
      TURTLE_FOOD.add(ItemTypes.SEAGRASS);
      copy(CHICKEN_FOOD, PARROT_FOOD);
      PARROT_POISONOUS_FOOD.add(ItemTypes.COOKIE);
      AXOLOTL_FOOD.add(ItemTypes.TROPICAL_FISH_BUCKET);
      NAUTILUS_BUCKET_FOOD.add(ItemTypes.PUFFERFISH_BUCKET, ItemTypes.COD_BUCKET, ItemTypes.SALMON_BUCKET, ItemTypes.TROPICAL_FISH_BUCKET);
      NAUTILUS_TAMING_ITEMS.add(ItemTypes.PUFFERFISH_BUCKET, ItemTypes.PUFFERFISH);
      NON_FLAMMABLE_WOOD.add(ItemTypes.WARPED_STEM, ItemTypes.STRIPPED_WARPED_STEM, ItemTypes.WARPED_HYPHAE, ItemTypes.STRIPPED_WARPED_HYPHAE, ItemTypes.CRIMSON_STEM, ItemTypes.STRIPPED_CRIMSON_STEM, ItemTypes.CRIMSON_HYPHAE, ItemTypes.STRIPPED_CRIMSON_HYPHAE, ItemTypes.CRIMSON_PLANKS, ItemTypes.WARPED_PLANKS, ItemTypes.CRIMSON_SLAB, ItemTypes.WARPED_SLAB, ItemTypes.CRIMSON_PRESSURE_PLATE, ItemTypes.WARPED_PRESSURE_PLATE, ItemTypes.CRIMSON_FENCE, ItemTypes.WARPED_FENCE, ItemTypes.CRIMSON_TRAPDOOR, ItemTypes.WARPED_TRAPDOOR, ItemTypes.CRIMSON_FENCE_GATE, ItemTypes.WARPED_FENCE_GATE, ItemTypes.CRIMSON_STAIRS, ItemTypes.WARPED_STAIRS, ItemTypes.CRIMSON_BUTTON, ItemTypes.WARPED_BUTTON, ItemTypes.CRIMSON_DOOR, ItemTypes.WARPED_DOOR, ItemTypes.CRIMSON_SIGN, ItemTypes.WARPED_SIGN, ItemTypes.WARPED_HANGING_SIGN, ItemTypes.CRIMSON_HANGING_SIGN, ItemTypes.WARPED_SHELF, ItemTypes.CRIMSON_SHELF);
      CHEST_BOATS.add(ItemTypes.OAK_CHEST_BOAT, ItemTypes.SPRUCE_CHEST_BOAT, ItemTypes.BIRCH_CHEST_BOAT, ItemTypes.JUNGLE_CHEST_BOAT, ItemTypes.ACACIA_CHEST_BOAT, ItemTypes.DARK_OAK_CHEST_BOAT, ItemTypes.PALE_OAK_CHEST_BOAT, ItemTypes.MANGROVE_CHEST_BOAT, ItemTypes.BAMBOO_CHEST_RAFT, ItemTypes.CHERRY_CHEST_BOAT);
      FISHES.add(ItemTypes.COD, ItemTypes.COOKED_COD, ItemTypes.SALMON, ItemTypes.COOKED_SALMON, ItemTypes.PUFFERFISH, ItemTypes.TROPICAL_FISH);
      CREEPER_DROP_MUSIC_DISCS.add(ItemTypes.MUSIC_DISC_13, ItemTypes.MUSIC_DISC_CAT, ItemTypes.MUSIC_DISC_BLOCKS, ItemTypes.MUSIC_DISC_CHIRP, ItemTypes.MUSIC_DISC_FAR, ItemTypes.MUSIC_DISC_MALL, ItemTypes.MUSIC_DISC_MELLOHI, ItemTypes.MUSIC_DISC_STAL, ItemTypes.MUSIC_DISC_STRAD, ItemTypes.MUSIC_DISC_WARD, ItemTypes.MUSIC_DISC_11, ItemTypes.MUSIC_DISC_WAIT);
      COALS.add(ItemTypes.COAL, ItemTypes.CHARCOAL);
      ARROWS.add(ItemTypes.ARROW, ItemTypes.TIPPED_ARROW, ItemTypes.SPECTRAL_ARROW);
      LECTERN_BOOKS.add(ItemTypes.WRITTEN_BOOK, ItemTypes.WRITABLE_BOOK);
      BOOKSHELF_BOOKS.add(ItemTypes.BOOK, ItemTypes.WRITTEN_BOOK, ItemTypes.ENCHANTED_BOOK, ItemTypes.WRITABLE_BOOK, ItemTypes.KNOWLEDGE_BOOK);
      BEACON_PAYMENT_ITEMS.add(ItemTypes.NETHERITE_INGOT, ItemTypes.EMERALD, ItemTypes.DIAMOND, ItemTypes.GOLD_INGOT, ItemTypes.IRON_INGOT);
      STONE_TOOL_MATERIALS.add(ItemTypes.COBBLESTONE, ItemTypes.BLACKSTONE, ItemTypes.COBBLED_DEEPSLATE);
      COPPER_TOOL_MATERIALS.add(ItemTypes.COPPER_INGOT);
      IRON_TOOL_MATERIALS.add(ItemTypes.IRON_INGOT);
      GOLD_TOOL_MATERIALS.add(ItemTypes.GOLD_INGOT);
      DIAMOND_TOOL_MATERIALS.add(ItemTypes.DIAMOND);
      NETHERITE_TOOL_MATERIALS.add(ItemTypes.NETHERITE_INGOT);
      copy(IGNORED_BY_PIGLIN_BABIES, REPAIRS_LEATHER_ARMOR);
      copy(COPPER_TOOL_MATERIALS, REPAIRS_COPPER_ARMOR);
      copy(IRON_TOOL_MATERIALS, REPAIRS_CHAIN_ARMOR);
      copy(IRON_TOOL_MATERIALS, REPAIRS_IRON_ARMOR);
      copy(GOLD_TOOL_MATERIALS, REPAIRS_GOLD_ARMOR);
      copy(DIAMOND_TOOL_MATERIALS, REPAIRS_DIAMOND_ARMOR);
      copy(NETHERITE_TOOL_MATERIALS, REPAIRS_NETHERITE_ARMOR);
      REPAIRS_TURTLE_HELMET.add(ItemTypes.TURTLE_SCUTE);
      REPAIRS_WOLF_ARMOR.add(ItemTypes.ARMADILLO_SCUTE);
      copy(STONE_TOOL_MATERIALS, STONE_CRAFTING_MATERIALS);
      FREEZE_IMMUNE_WEARABLES.add(ItemTypes.LEATHER_BOOTS, ItemTypes.LEATHER_LEGGINGS, ItemTypes.LEATHER_CHESTPLATE, ItemTypes.LEATHER_HELMET, ItemTypes.LEATHER_HORSE_ARMOR);
      CLUSTER_MAX_HARVESTABLES.add(ItemTypes.DIAMOND_PICKAXE, ItemTypes.GOLDEN_PICKAXE, ItemTypes.IRON_PICKAXE, ItemTypes.NETHERITE_PICKAXE, ItemTypes.STONE_PICKAXE, ItemTypes.WOODEN_PICKAXE, ItemTypes.COPPER_PICKAXE);
      COMPASSES.add(ItemTypes.COMPASS, ItemTypes.RECOVERY_COMPASS);
      CREEPER_IGNITERS.add(ItemTypes.FLINT_AND_STEEL, ItemTypes.FIRE_CHARGE);
      NOTEBLOCK_TOP_INSTRUMENTS.add(ItemTypes.ZOMBIE_HEAD, ItemTypes.SKELETON_SKULL, ItemTypes.CREEPER_HEAD, ItemTypes.DRAGON_HEAD, ItemTypes.WITHER_SKELETON_SKULL, ItemTypes.PIGLIN_HEAD, ItemTypes.PLAYER_HEAD);
      FOOT_ARMOR.add(ItemTypes.LEATHER_BOOTS, ItemTypes.COPPER_BOOTS, ItemTypes.CHAINMAIL_BOOTS, ItemTypes.GOLDEN_BOOTS, ItemTypes.IRON_BOOTS, ItemTypes.DIAMOND_BOOTS, ItemTypes.NETHERITE_BOOTS);
      LEG_ARMOR.add(ItemTypes.LEATHER_LEGGINGS, ItemTypes.COPPER_LEGGINGS, ItemTypes.CHAINMAIL_LEGGINGS, ItemTypes.GOLDEN_LEGGINGS, ItemTypes.IRON_LEGGINGS, ItemTypes.DIAMOND_LEGGINGS, ItemTypes.NETHERITE_LEGGINGS);
      CHEST_ARMOR.add(ItemTypes.LEATHER_CHESTPLATE, ItemTypes.COPPER_CHESTPLATE, ItemTypes.CHAINMAIL_CHESTPLATE, ItemTypes.GOLDEN_CHESTPLATE, ItemTypes.IRON_CHESTPLATE, ItemTypes.DIAMOND_CHESTPLATE, ItemTypes.NETHERITE_CHESTPLATE);
      HEAD_ARMOR.add(ItemTypes.LEATHER_HELMET, ItemTypes.COPPER_HELMET, ItemTypes.CHAINMAIL_HELMET, ItemTypes.GOLDEN_HELMET, ItemTypes.IRON_HELMET, ItemTypes.DIAMOND_HELMET, ItemTypes.NETHERITE_HELMET, ItemTypes.TURTLE_HELMET);
      SKULLS.add(ItemTypes.PLAYER_HEAD, ItemTypes.CREEPER_HEAD, ItemTypes.ZOMBIE_HEAD, ItemTypes.SKELETON_SKULL, ItemTypes.WITHER_SKELETON_SKULL, ItemTypes.DRAGON_HEAD, ItemTypes.PIGLIN_HEAD);
      TRIM_MATERIALS.add(ItemTypes.AMETHYST_SHARD, ItemTypes.COPPER_INGOT, ItemTypes.DIAMOND, ItemTypes.EMERALD, ItemTypes.GOLD_INGOT, ItemTypes.IRON_INGOT, ItemTypes.LAPIS_LAZULI, ItemTypes.NETHERITE_INGOT, ItemTypes.QUARTZ, ItemTypes.REDSTONE, ItemTypes.RESIN_BRICK);
      DECORATED_POT_SHERDS.add(ItemTypes.ANGLER_POTTERY_SHERD, ItemTypes.ARCHER_POTTERY_SHERD, ItemTypes.ARMS_UP_POTTERY_SHERD, ItemTypes.BLADE_POTTERY_SHERD, ItemTypes.BREWER_POTTERY_SHERD, ItemTypes.BURN_POTTERY_SHERD, ItemTypes.DANGER_POTTERY_SHERD, ItemTypes.EXPLORER_POTTERY_SHERD, ItemTypes.FRIEND_POTTERY_SHERD, ItemTypes.HEART_POTTERY_SHERD, ItemTypes.HEARTBREAK_POTTERY_SHERD, ItemTypes.HOWL_POTTERY_SHERD, ItemTypes.MINER_POTTERY_SHERD, ItemTypes.MOURNER_POTTERY_SHERD, ItemTypes.PLENTY_POTTERY_SHERD, ItemTypes.PRIZE_POTTERY_SHERD, ItemTypes.SHEAF_POTTERY_SHERD, ItemTypes.SHELTER_POTTERY_SHERD, ItemTypes.SKULL_POTTERY_SHERD, ItemTypes.SNORT_POTTERY_SHERD, ItemTypes.FLOW_POTTERY_SHERD, ItemTypes.GUSTER_POTTERY_SHERD, ItemTypes.SCRAPE_POTTERY_SHERD);
      SWORDS.add(ItemTypes.DIAMOND_SWORD, ItemTypes.STONE_SWORD, ItemTypes.GOLDEN_SWORD, ItemTypes.NETHERITE_SWORD, ItemTypes.WOODEN_SWORD, ItemTypes.IRON_SWORD, ItemTypes.COPPER_SWORD);
      AXES.add(ItemTypes.DIAMOND_AXE, ItemTypes.STONE_AXE, ItemTypes.GOLDEN_AXE, ItemTypes.NETHERITE_AXE, ItemTypes.WOODEN_AXE, ItemTypes.IRON_AXE, ItemTypes.COPPER_AXE);
      HOES.add(ItemTypes.DIAMOND_HOE, ItemTypes.STONE_HOE, ItemTypes.GOLDEN_HOE, ItemTypes.NETHERITE_HOE, ItemTypes.WOODEN_HOE, ItemTypes.IRON_HOE, ItemTypes.COPPER_HOE);
      PICKAXES.add(ItemTypes.DIAMOND_PICKAXE, ItemTypes.STONE_PICKAXE, ItemTypes.GOLDEN_PICKAXE, ItemTypes.NETHERITE_PICKAXE, ItemTypes.WOODEN_PICKAXE, ItemTypes.IRON_PICKAXE, ItemTypes.COPPER_PICKAXE);
      SHOVELS.add(ItemTypes.DIAMOND_SHOVEL, ItemTypes.STONE_SHOVEL, ItemTypes.GOLDEN_SHOVEL, ItemTypes.NETHERITE_SHOVEL, ItemTypes.WOODEN_SHOVEL, ItemTypes.IRON_SHOVEL, ItemTypes.COPPER_SHOVEL);
      SPEARS.add(ItemTypes.DIAMOND_SPEAR, ItemTypes.STONE_SPEAR, ItemTypes.GOLDEN_SPEAR, ItemTypes.NETHERITE_SPEAR, ItemTypes.WOODEN_SPEAR, ItemTypes.IRON_SPEAR, ItemTypes.COPPER_SPEAR);
      VILLAGER_PLANTABLE_SEEDS.add(ItemTypes.WHEAT_SEEDS, ItemTypes.POTATO, ItemTypes.CARROT, ItemTypes.BEETROOT_SEEDS, ItemTypes.TORCHFLOWER_SEEDS, ItemTypes.PITCHER_POD);
      DYEABLE.add(ItemTypes.LEATHER_HELMET, ItemTypes.LEATHER_CHESTPLATE, ItemTypes.LEATHER_LEGGINGS, ItemTypes.LEATHER_BOOTS, ItemTypes.LEATHER_HORSE_ARMOR, ItemTypes.WOLF_ARMOR);
      copy(COALS, FURNACE_MINECART_FUEL);
      BUNDLES.add(ItemTypes.BUNDLE, ItemTypes.BLACK_BUNDLE, ItemTypes.BLUE_BUNDLE, ItemTypes.BROWN_BUNDLE, ItemTypes.CYAN_BUNDLE, ItemTypes.GRAY_BUNDLE, ItemTypes.GREEN_BUNDLE, ItemTypes.LIGHT_BLUE_BUNDLE, ItemTypes.LIGHT_GRAY_BUNDLE, ItemTypes.LIME_BUNDLE, ItemTypes.MAGENTA_BUNDLE, ItemTypes.ORANGE_BUNDLE, ItemTypes.PINK_BUNDLE, ItemTypes.PURPLE_BUNDLE, ItemTypes.RED_BUNDLE, ItemTypes.YELLOW_BUNDLE, ItemTypes.WHITE_BUNDLE);
      BOOK_CLONING_TARGET.add(ItemTypes.WRITABLE_BOOK);
      SKELETON_PREFERRED_WEAPONS.add(ItemTypes.BOW);
      DROWNED_PREFERRED_WEAPONS.add(ItemTypes.TRIDENT);
      PIGLIN_PREFERRED_WEAPONS.add(ItemTypes.CROSSBOW, ItemTypes.GOLDEN_SPEAR);
      PILLAGER_PREFERRED_WEAPONS.add(ItemTypes.CROSSBOW);
      WITHER_SKELETON_DISLIKED_WEAPONS.add(ItemTypes.BOW, ItemTypes.CROSSBOW);
      SHEARABLE_FROM_COPPER_GOLEM.add(ItemTypes.POPPY);
      ENCHANTABLE_FISHING.add(ItemTypes.FISHING_ROD);
      copy(DROWNED_PREFERRED_WEAPONS, ENCHANTABLE_TRIDENT);
      copy(SKELETON_PREFERRED_WEAPONS, ENCHANTABLE_BOW);
      copy(PILLAGER_PREFERRED_WEAPONS, ENCHANTABLE_CROSSBOW);
      ENCHANTABLE_MACE.add(ItemTypes.MACE);
      MAP_INVISIBILITY_EQUIPMENT.add(ItemTypes.CARVED_PUMPKIN);
      copy(MAP_INVISIBILITY_EQUIPMENT, GAZE_DISGUISE_EQUIPMENT);
      copy(BlockTags.BUTTONS, BUTTONS);
      copy(BlockTags.DOORS, DOORS);
      copy(BlockTags.LOGS_THAT_BURN, LOGS_THAT_BURN);
      copy(BlockTags.SLABS, SLABS);
      copy(BlockTags.STAIRS, STAIRS);
      copy(BlockTags.TRAPDOORS, TRAPDOORS);
      copy(BlockTags.FLOWERS, FLOWERS);
      copy(BlockTags.FENCES, FENCES);
      copy(BlockTags.DAMPENS_VIBRATIONS, DAMPENS_VIBRATIONS);
      PIGLIN_LOVED.addTag(GOLD_ORES).add(ItemTypes.GOLD_BLOCK, ItemTypes.GILDED_BLACKSTONE, ItemTypes.LIGHT_WEIGHTED_PRESSURE_PLATE, ItemTypes.GOLD_INGOT, ItemTypes.BELL, ItemTypes.CLOCK, ItemTypes.GOLDEN_CARROT, ItemTypes.GLISTERING_MELON_SLICE, ItemTypes.GOLDEN_APPLE, ItemTypes.ENCHANTED_GOLDEN_APPLE, ItemTypes.GOLDEN_HELMET, ItemTypes.GOLDEN_CHESTPLATE, ItemTypes.GOLDEN_LEGGINGS, ItemTypes.GOLDEN_BOOTS, ItemTypes.GOLDEN_HORSE_ARMOR, ItemTypes.GOLDEN_NAUTILUS_ARMOR, ItemTypes.GOLDEN_SWORD, ItemTypes.GOLDEN_SPEAR, ItemTypes.GOLDEN_PICKAXE, ItemTypes.GOLDEN_SHOVEL, ItemTypes.GOLDEN_AXE, ItemTypes.GOLDEN_HOE, ItemTypes.RAW_GOLD, ItemTypes.RAW_GOLD_BLOCK);
      WOLF_FOOD.addTag(MEAT).add(ItemTypes.COD, ItemTypes.COOKED_COD, ItemTypes.SALMON, ItemTypes.COOKED_SALMON, ItemTypes.TROPICAL_FISH, ItemTypes.PUFFERFISH, ItemTypes.RABBIT_STEW);
      HAPPY_GHAST_TEMPT_ITEMS.addTag(HAPPY_GHAST_FOOD).addTag(HARNESSES);
      PANDA_EATS_FROM_GROUND.addTag(PANDA_FOOD).add(ItemTypes.CAKE);
      STRIDER_TEMPT_ITEMS.addTag(STRIDER_FOOD).add(ItemTypes.WARPED_FUNGUS_ON_A_STICK);
      NAUTILUS_FOOD.addTag(FISHES).addTag(NAUTILUS_BUCKET_FOOD);
      BOATS.addTag(CHEST_BOATS).add(ItemTypes.OAK_BOAT, ItemTypes.SPRUCE_BOAT, ItemTypes.BIRCH_BOAT, ItemTypes.JUNGLE_BOAT, ItemTypes.ACACIA_BOAT, ItemTypes.DARK_OAK_BOAT, ItemTypes.PALE_OAK_BOAT, ItemTypes.MANGROVE_BOAT, ItemTypes.BAMBOO_RAFT, ItemTypes.CHERRY_BOAT);
      WOODEN_TOOL_MATERIALS.addTag(PLANKS);
      TRIMMABLE_ARMOR.addTag(FOOT_ARMOR).addTag(LEG_ARMOR).addTag(CHEST_ARMOR).addTag(HEAD_ARMOR);
      DECORATED_POT_INGREDIENTS.addTag(DECORATED_POT_SHERDS).add(ItemTypes.BRICK);
      BREAKS_DECORATED_POTS.addTag(SWORDS).addTag(AXES).addTag(PICKAXES).addTag(SHOVELS).addTag(HOES).add(ItemTypes.TRIDENT, ItemTypes.MACE);
      VILLAGER_PICKS_UP.addTag(VILLAGER_PLANTABLE_SEEDS).add(ItemTypes.BREAD, ItemTypes.WHEAT, ItemTypes.BEETROOT);
      ENCHANTABLE_FOOT_ARMOR.addTag(FOOT_ARMOR);
      ENCHANTABLE_LEG_ARMOR.addTag(LEG_ARMOR);
      ENCHANTABLE_CHEST_ARMOR.addTag(CHEST_ARMOR);
      ENCHANTABLE_HEAD_ARMOR.addTag(HEAD_ARMOR);
      ENCHANTABLE_MELEE_WEAPON.addTag(SWORDS).addTag(SPEARS);
      ENCHANTABLE_SWEEPING.addTag(SWORDS);
      ENCHANTABLE_MINING.addTag(AXES).addTag(PICKAXES).addTag(SHOVELS).addTag(HOES).add(ItemTypes.SHEARS);
      ENCHANTABLE_MINING_LOOT.addTag(AXES).addTag(PICKAXES).addTag(SHOVELS).addTag(HOES);
      ENCHANTABLE_LUNGE.addTag(SPEARS);
      ENCHANTABLE_DURABILITY.addTag(FOOT_ARMOR).addTag(LEG_ARMOR).addTag(CHEST_ARMOR).addTag(HEAD_ARMOR).addTag(SWORDS).addTag(AXES).addTag(PICKAXES).addTag(SHOVELS).addTag(HOES).addTag(SPEARS).add(ItemTypes.ELYTRA, ItemTypes.SHIELD, ItemTypes.BOW, ItemTypes.CROSSBOW, ItemTypes.TRIDENT, ItemTypes.FLINT_AND_STEEL, ItemTypes.SHEARS, ItemTypes.BRUSH, ItemTypes.FISHING_ROD, ItemTypes.CARROT_ON_A_STICK, ItemTypes.WARPED_FUNGUS_ON_A_STICK, ItemTypes.MACE);
      ENCHANTABLE_EQUIPPABLE.addTag(FOOT_ARMOR).addTag(LEG_ARMOR).addTag(CHEST_ARMOR).addTag(HEAD_ARMOR).addTag(SKULLS).add(ItemTypes.ELYTRA, ItemTypes.CARVED_PUMPKIN);
      copy(BlockTags.LOGS, LOGS);
      ENCHANTABLE_ARMOR.addTag(ENCHANTABLE_FOOT_ARMOR).addTag(ENCHANTABLE_LEG_ARMOR).addTag(ENCHANTABLE_CHEST_ARMOR).addTag(ENCHANTABLE_HEAD_ARMOR);
      ENCHANTABLE_FIRE_ASPECT.addTag(ENCHANTABLE_MELEE_WEAPON).add(ItemTypes.MACE);
      ENCHANTABLE_SHARP_WEAPON.addTag(ENCHANTABLE_MELEE_WEAPON).addTag(AXES);
      ENCHANTABLE_VANISHING.addTag(ENCHANTABLE_DURABILITY).addTag(SKULLS).add(ItemTypes.COMPASS, ItemTypes.CARVED_PUMPKIN);
      copy(BlockTags.COMPLETES_FIND_TREE_TUTORIAL, COMPLETES_FIND_TREE_TUTORIAL);
      ENCHANTABLE_WEAPON.addTag(ENCHANTABLE_SHARP_WEAPON).add(ItemTypes.MACE);
      Iterator var0 = ItemTypes.getRegistry().getEntries().iterator();

      while(var0.hasNext()) {
         ItemType type = (ItemType)var0.next();
         if (type.getComponents().has(ComponentTypes.JUKEBOX_PLAYABLE)) {
            MUSIC_DISCS.add(type);
         }
      }

      copy(BlockTags.TALL_FLOWERS, TALL_FLOWERS);
      copy(BlockTags.FLOWERS, FLOWERS);
      TRIM_TEMPLATES.add(ItemTypes.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTypes.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE);
      ENCHANTABLE_SWORD.addTag(SWORDS);
   }
}
