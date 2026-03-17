/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import me.zombie_striker.qav.util.xseries.XEnchantment;
import me.zombie_striker.qav.util.xseries.XEntityType;
import me.zombie_striker.qav.util.xseries.XMaterial;
import me.zombie_striker.qav.util.xseries.XPotion;
import me.zombie_striker.qav.util.xseries.base.XBase;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class XTag<T extends XBase<?, ?>> {
    @NotNull
    public static final XTag<XMaterial> AIR = TagBuilder.access$000(new XMaterial[]{XMaterial.AIR, XMaterial.CAVE_AIR, XMaterial.VOID_AIR});
    @NotNull
    public static final XTag<XMaterial> INVENTORY_NOT_DISPLAYABLE;
    @NotNull
    public static final XTag<XMaterial> ACACIA_LOGS;
    @NotNull
    public static final XTag<XMaterial> CORAL_FANS;
    @NotNull
    public static final XTag<XMaterial> ALIVE_CORAL_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> ALIVE_CORAL_FANS;
    @NotNull
    public static final XTag<XMaterial> ALIVE_CORAL_PLANTS;
    @NotNull
    public static final XTag<XMaterial> ALIVE_CORAL_WALL_FANS;
    @NotNull
    public static final XTag<XMaterial> ANIMALS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> ANVIL;
    @NotNull
    public static final XTag<XMaterial> AXOLOTL_TEMPT_ITEMS;
    @NotNull
    public static final XTag<XMaterial> AXOLOTLS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> AZALEA_GROWS_ON;
    @NotNull
    public static final XTag<XMaterial> AZALEA_ROOT_REPLACEABLE;
    @NotNull
    public static final XTag<XMaterial> BAMBOO_LOGS;
    @NotNull
    public static final XTag<XMaterial> BAMBOO_PLANTABLE_ON;
    @NotNull
    public static final XTag<XMaterial> BANNERS;
    @NotNull
    public static final XTag<XMaterial> BASE_STONE_NETHER;
    @NotNull
    public static final XTag<XMaterial> BASE_STONE_OVERWORLD;
    @NotNull
    public static final XTag<XMaterial> BEACON_BASE_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> BEDS;
    @NotNull
    public static final XTag<XMaterial> BEE_GROWABLES;
    @NotNull
    public static final XTag<XMaterial> BIG_DRIPLEAF_PLACEABLE;
    @NotNull
    public static final XTag<XMaterial> BIRCH_LOGS;
    @NotNull
    public static final XTag<XMaterial> BUTTONS;
    @NotNull
    public static final XTag<XMaterial> CAMPFIRES;
    @NotNull
    public static final XTag<XMaterial> CANDLE_CAKES;
    @NotNull
    public static final XTag<XMaterial> CANDLES;
    @NotNull
    public static final XTag<XMaterial> CARPETS;
    @NotNull
    public static final XTag<XMaterial> CAULDRONS;
    @NotNull
    public static final XTag<XMaterial> CAVE_VINES;
    @NotNull
    public static final XTag<XMaterial> CHERRY_LOGS;
    @NotNull
    public static final XTag<XMaterial> CLIMBABLE;
    @NotNull
    public static final XTag<XMaterial> CLUSTER_MAX_HARVESTABLES;
    @NotNull
    public static final XTag<XMaterial> COAL_ORES;
    @NotNull
    public static final XTag<XMaterial> CONCRETE;
    @NotNull
    public static final XTag<XMaterial> CONCRETE_POWDER;
    @NotNull
    public static final XTag<XMaterial> COPPER_ORES;
    @NotNull
    public static final XTag<XMaterial> CORALS;
    @NotNull
    public static final XTag<XMaterial> CRIMSON_STEMS;
    @NotNull
    public static final XTag<XMaterial> CROPS;
    @NotNull
    public static final XTag<XMaterial> CRYSTAL_SOUND_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> DARK_OAK_LOGS;
    @NotNull
    public static final XTag<XMaterial> DEAD_CORAL_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> DEAD_CORAL_FANS;
    @NotNull
    public static final XTag<XMaterial> DEAD_CORAL_PLANTS;
    @NotNull
    public static final XTag<XMaterial> DEAD_CORAL_WALL_FANS;
    @NotNull
    public static final XTag<XMaterial> DEEPSLATE_ORE_REPLACEABLES;
    @NotNull
    public static final XTag<XMaterial> DIAMOND_ORES;
    @NotNull
    public static final XTag<XMaterial> DIRT;
    @NotNull
    public static final XTag<XMaterial> DOORS;
    @NotNull
    public static final XTag<XMaterial> DRAGON_IMMUNE;
    @NotNull
    public static final XTag<XMaterial> DRIPSTONE_REPLACEABLE;
    @NotNull
    public static final XTag<XMaterial> WALL_HEADS;
    @NotNull
    public static final XTag<XMaterial> EMERALD_ORES;
    @NotNull
    public static final XTag<XMaterial> ENDERMAN_HOLDABLE;
    @NotNull
    public static final XTag<XMaterial> FEATURES_CANNOT_REPLACE;
    @NotNull
    public static final XTag<XMaterial> FENCE_GATES;
    @NotNull
    public static final XTag<XMaterial> FENCES;
    @NotNull
    public static final XTag<XMaterial> FILLED_CAULDRONS;
    @NotNull
    public static final XTag<XMaterial> FIRE;
    @NotNull
    public static final XTag<XMaterial> FLOWER_POTS;
    @NotNull
    public static final XTag<XMaterial> FLOWERS;
    @NotNull
    public static final XTag<XMaterial> FOX_FOOD;
    @NotNull
    public static final XTag<XMaterial> FOXES_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> FREEZE_IMMUNE_WEARABLES;
    @NotNull
    public static final XTag<XMaterial> GEODE_INVALID_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> GLASS;
    @NotNull
    public static final XTag<XMaterial> GLAZED_TERRACOTTA;
    @NotNull
    public static final XTag<XMaterial> GOATS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> GOLD_ORES;
    @NotNull
    public static final XTag<XMaterial> GUARDED_BY_PIGLINS;
    @NotNull
    public static final XTag<XMaterial> HANGING_SIGNS;
    @NotNull
    public static final XTag<XMaterial> HOGLIN_REPELLENTS;
    @NotNull
    public static final XTag<XMaterial> ICE;
    @NotNull
    public static final XTag<XMaterial> IGNORED_BY_PIGLIN_BABIES;
    @NotNull
    public static final XTag<XMaterial> IMPERMEABLE;
    @NotNull
    public static final XTag<XMaterial> INFINIBURN_END;
    @NotNull
    public static final XTag<XMaterial> INFINIBURN_NETHER;
    @NotNull
    public static final XTag<XMaterial> INFINIBURN_OVERWORLD;
    @NotNull
    public static final XTag<XMaterial> INSIDE_STEP_SOUND_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> IRON_ORES;
    @NotNull
    public static final XTag<XMaterial> ITEMS_ARROWS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_BANNERS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_BEACON_PAYMENT_ITEMS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_BOATS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_COALS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_CREEPER_DROP_MUSIC_DISCS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_FISHES;
    @NotNull
    public static final XTag<XMaterial> ITEMS_FURNACE_MATERIALS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_LECTERN_BOOKS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_MUSIC_DISCS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_PIGLIN_LOVED;
    @NotNull
    public static final XTag<XMaterial> ITEMS_STONE_TOOL_MATERIALS;
    @NotNull
    public static final XTag<XMaterial> WALL_BANNERS;
    @NotNull
    public static final XTag<XMaterial> JUNGLE_LOGS;
    @NotNull
    public static final XTag<XMaterial> LAPIS_ORES;
    @NotNull
    public static final XTag<XMaterial> LAVA_POOL_STONE_CANNOT_REPLACE;
    @NotNull
    public static final XTag<XMaterial> LEAVES;
    @NotNull
    public static final XTag<XMaterial> LOGS;
    @NotNull
    public static final XTag<XMaterial> LOGS_THAT_BURN;
    @NotNull
    public static final XTag<XMaterial> LUSH_GROUND_REPLACEABLE;
    @NotNull
    public static final XTag<XMaterial> MANGROVE_LOGS;
    @NotNull
    public static final XTag<XMaterial> MINEABLE_AXE;
    @NotNull
    public static final XTag<XMaterial> MINEABLE_HOE;
    @NotNull
    public static final XTag<XMaterial> MINEABLE_PICKAXE;
    @NotNull
    public static final XTag<XMaterial> MINEABLE_SHOVEL;
    @NotNull
    public static final XTag<XMaterial> MOOSHROOMS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> MOSS_REPLACEABLE;
    @NotNull
    public static final XTag<XMaterial> MUSHROOM_GROW_BLOCK;
    @NotNull
    public static final XTag<XMaterial> NEEDS_DIAMOND_TOOL;
    @NotNull
    public static final XTag<XMaterial> NEEDS_IRON_TOOL;
    @NotNull
    public static final XTag<XMaterial> NEEDS_STONE_TOOL;
    @NotNull
    public static final XTag<XMaterial> NON_FLAMMABLE_WOOD;
    @NotNull
    public static final XTag<XMaterial> NON_WOODEN_STAIRS;
    @NotNull
    public static final XTag<XMaterial> NON_WOODEN_SLABS;
    @NotNull
    public static final XTag<XMaterial> NYLIUM;
    @NotNull
    public static final XTag<XMaterial> OAK_LOGS;
    @NotNull
    public static final XTag<XMaterial> OCCLUDES_VIBRATION_SIGNALS;
    @NotNull
    public static final XTag<XMaterial> ORES;
    @NotNull
    public static final XTag<XMaterial> PALE_OAK_LOGS;
    @NotNull
    public static final XTag<XMaterial> PARROTS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> PIGLIN_FOOD;
    @NotNull
    public static final XTag<XMaterial> PIGLIN_REPELLENTS;
    @NotNull
    public static final XTag<XMaterial> PLANKS;
    @NotNull
    public static final XTag<XMaterial> POLAR_BEARS_SPAWNABLE_ON_IN_FROZEN_OCEAN;
    @NotNull
    public static final XTag<XMaterial> PORTALS;
    @NotNull
    public static final XTag<XMaterial> POTTERY_SHERDS;
    @NotNull
    public static final XTag<XMaterial> PRESSURE_PLATES;
    @NotNull
    public static final XTag<XMaterial> PREVENT_MOB_SPAWNING_INSIDE;
    @NotNull
    public static final XTag<XMaterial> RABBITS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> RAILS;
    @NotNull
    public static final XTag<XMaterial> REDSTONE_ORES;
    @NotNull
    public static final XTag<XMaterial> REPLACEABLE_PLANTS;
    @NotNull
    public static final XTag<XMaterial> SAND;
    @NotNull
    public static final XTag<XMaterial> SAPLINGS;
    @NotNull
    public static final XTag<XMaterial> SHULKER_BOXES;
    @NotNull
    public static final XTag<XMaterial> SIGNS;
    @NotNull
    public static final XTag<XMaterial> SMALL_DRIPLEAF_PLACEABLE;
    @NotNull
    public static final XTag<XMaterial> SMALL_FLOWERS;
    @NotNull
    public static final XTag<XMaterial> SMITHING_TEMPLATES;
    @NotNull
    public static final XTag<XMaterial> SNOW;
    @NotNull
    public static final XTag<XMaterial> SOUL_FIRE_BASE_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> SOUL_SPEED_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> SPRUCE_LOGS;
    @NotNull
    public static final XTag<XMaterial> STAIRS;
    @NotNull
    public static final XTag<XMaterial> STANDING_SIGNS;
    @NotNull
    public static final XTag<XMaterial> STONE_BRICKS;
    @NotNull
    public static final XTag<XMaterial> STONE_ORE_REPLACEABLES;
    @NotNull
    public static final XTag<XMaterial> STONE_PRESSURE_PLATES;
    @NotNull
    public static final XTag<XMaterial> STRIDER_WARM_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> TALL_FLOWERS;
    @NotNull
    public static final XTag<XMaterial> TERRACOTTA;
    @NotNull
    public static final XTag<XMaterial> TRAPDOORS;
    @NotNull
    public static final XTag<XMaterial> UNDERWATER_BONEMEALS;
    @NotNull
    public static final XTag<XMaterial> UNSTABLE_BOTTOM_CENTER;
    @NotNull
    public static final XTag<XMaterial> VALID_SPAWN;
    @NotNull
    public static final XTag<XMaterial> WALL_HANGING_SIGNS;
    @NotNull
    public static final XTag<XMaterial> WALL_POST_OVERRIDE;
    @NotNull
    public static final XTag<XMaterial> WALL_SIGNS;
    @NotNull
    public static final XTag<XMaterial> WALL_TORCHES;
    @NotNull
    public static final XTag<XMaterial> WALLS;
    @NotNull
    public static final XTag<XMaterial> WARPED_STEMS;
    @NotNull
    public static final XTag<XMaterial> WITHER_IMMUNE;
    @NotNull
    public static final XTag<XMaterial> WITHER_SUMMON_BASE_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> WOLVES_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> WOODEN_BUTTONS;
    @NotNull
    public static final XTag<XMaterial> WOODEN_DOORS;
    @NotNull
    public static final XTag<XMaterial> WOODEN_FENCE_GATES;
    @NotNull
    public static final XTag<XMaterial> WOODEN_FENCES;
    @NotNull
    public static final XTag<XMaterial> WOODEN_PRESSURE_PLATES;
    @NotNull
    public static final XTag<XMaterial> WOODEN_SLABS;
    @NotNull
    public static final XTag<XMaterial> WOODEN_STAIRS;
    @NotNull
    public static final XTag<XMaterial> WOODEN_TRAPDOORS;
    @NotNull
    public static final XTag<XMaterial> WOOL;
    @NotNull
    public static final XTag<XMaterial> LEATHER_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> IRON_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> CHAINMAIL_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> GOLDEN_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> DIAMOND_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> NETHERITE_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> WOODEN_TOOLS;
    @NotNull
    public static final XTag<XMaterial> FLUID;
    @NotNull
    public static final XTag<XMaterial> STONE_TOOLS;
    @NotNull
    public static final XTag<XMaterial> IRON_TOOLS;
    @NotNull
    public static final XTag<XMaterial> DIAMOND_TOOLS;
    @NotNull
    public static final XTag<XMaterial> NETHERITE_TOOLS;
    @NotNull
    public static final XTag<XMaterial> DANGEROUS_BLOCKS;
    @NotNull
    public static final XTag<XEnchantment> ARMOR_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> HELEMT_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> CHESTPLATE_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> LEGGINGS_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> BOOTS_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> ELYTRA_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> SWORD_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> AXE_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> HOE_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> PICKAXE_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> SHOVEL_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> SHEARS_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> BOW_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> CROSSBOW_ENCHANTS;
    @NotNull
    public static final XTag<XEntityType> EFFECTIVE_SMITE_ENTITIES;
    @NotNull
    public static final XTag<XEntityType> EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES;
    @NotNull
    public static final XTag<XPotion> DEBUFFS;
    @NotNull
    private final Set<T> values;
    private static final Map<String, XTag<?>> TAGS;

    private XTag(@NotNull Set<T> set) {
        this.values = set;
    }

    public static <E> List<Matcher<E>> stringMatcher(@Nullable Collection<String> collection) {
        return XTag.stringMatcher(collection, null);
    }

    public static <E> List<Matcher<E>> stringMatcher(@Nullable Collection<String> collection, @Nullable Collection<Matcher.Error> collection2) {
        if (collection == null || collection.isEmpty()) {
            return new ArrayList<Matcher<E>>();
        }
        ArrayList<Matcher<Matcher>> arrayList = new ArrayList<Matcher<Matcher>>(collection.size());
        for (String string : collection) {
            String string2 = string.toUpperCase(Locale.ENGLISH);
            if (string2.startsWith("CONTAINS:")) {
                string = XMaterial.format(string2.substring(9));
                arrayList.add(new Matcher.TextMatcher(string, true));
                continue;
            }
            if (string2.startsWith("REGEX:")) {
                string = string.substring(6);
                try {
                    arrayList.add(new Matcher.RegexMatcher(Pattern.compile(string)));
                } catch (Throwable throwable) {
                    if (collection2 == null) continue;
                    collection2.add(new Matcher.Error(string, "REGEX", throwable));
                }
                continue;
            }
            if (string2.startsWith("TAG:")) {
                Optional<XTag<?>> optional = XTag.getTag(string = XMaterial.format(string.substring(4)));
                if (optional.isPresent()) {
                    arrayList.add(new Matcher.XTagMatcher(optional.get()));
                } else {
                    collection2.add(new Matcher.Error("Cannot find tag: " + string, "TAG"));
                }
            }
            arrayList.add(new Matcher.TextMatcher(string, false));
        }
        return arrayList;
    }

    public static <T> boolean anyMatchString(T t, Collection<String> collection) {
        return XTag.anyMatch(t, XTag.stringMatcher(collection));
    }

    public static <T> boolean anyMatch(T t, Collection<Matcher<T>> collection) {
        return collection.stream().anyMatch(matcher -> matcher.matches(t));
    }

    private static XMaterial[] findAllColors(String string) {
        String[] stringArray = new String[]{"ORANGE", "LIGHT_BLUE", "GRAY", "BLACK", "MAGENTA", "PINK", "BLUE", "GREEN", "CYAN", "PURPLE", "YELLOW", "LIME", "LIGHT_GRAY", "WHITE", "BROWN", "RED"};
        ArrayList arrayList = new ArrayList();
        XMaterial.matchXMaterial(string).ifPresent(arrayList::add);
        for (String string2 : stringArray) {
            XMaterial.matchXMaterial(string2 + '_' + string).ifPresent(arrayList::add);
        }
        return arrayList.toArray(new XMaterial[0]);
    }

    private static XMaterial[] findAllWoodTypes(String string) {
        String[] stringArray = new String[]{"ACACIA", "DARK_OAK", "PALE_OAK", "JUNGLE", "BIRCH", "WARPED", "OAK", "SPRUCE", "CRIMSON", "MANGROVE", "CHERRY", "BAMBOO"};
        ArrayList arrayList = new ArrayList();
        for (String string2 : stringArray) {
            XMaterial.matchXMaterial(string2 + '_' + string).ifPresent(arrayList::add);
        }
        return arrayList.toArray(new XMaterial[0]);
    }

    private static XMaterial[] findMaterialsEndingWith(String string) {
        return (XMaterial[])Arrays.stream(XMaterial.VALUES).filter(xMaterial -> xMaterial.name().endsWith(string)).toArray(XMaterial[]::new);
    }

    private static XMaterial[] findMaterialsStartingWith(String string) {
        return (XMaterial[])Arrays.stream(XMaterial.VALUES).filter(xMaterial -> xMaterial.name().startsWith(string)).toArray(XMaterial[]::new);
    }

    private static XMaterial[] findAllCorals(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        String[] stringArray = new String[]{"FIRE", "TUBE", "BRAIN", "HORN", "BUBBLE"};
        ArrayList arrayList = new ArrayList();
        for (String string : stringArray) {
            StringBuilder stringBuilder = new StringBuilder();
            if (!bl) {
                stringBuilder.append("DEAD_");
            }
            stringBuilder.append(string).append("_CORAL");
            if (bl2) {
                stringBuilder.append("_BLOCK");
            }
            if (bl3) {
                if (bl4) {
                    stringBuilder.append("_WALL");
                }
                stringBuilder.append("_FAN");
            }
            XMaterial.matchXMaterial(stringBuilder.toString()).ifPresent(arrayList::add);
        }
        return arrayList.toArray(new XMaterial[0]);
    }

    public static boolean isItem(XMaterial xMaterial) {
        if (XMaterial.supports(13)) {
            Material material = xMaterial.get();
            return material != null && material.isItem();
        }
        switch (xMaterial) {
            case ATTACHED_MELON_STEM: 
            case ATTACHED_PUMPKIN_STEM: 
            case BEETROOTS: 
            case BLACK_WALL_BANNER: 
            case BLUE_WALL_BANNER: 
            case BROWN_WALL_BANNER: 
            case CARROTS: 
            case COCOA: 
            case CREEPER_WALL_HEAD: 
            case CYAN_WALL_BANNER: 
            case DRAGON_WALL_HEAD: 
            case END_GATEWAY: 
            case END_PORTAL: 
            case FIRE: 
            case FIRE_CORAL_WALL_FAN: 
            case FROSTED_ICE: 
            case GRAY_WALL_BANNER: 
            case GREEN_WALL_BANNER: 
            case HORN_CORAL_WALL_FAN: 
            case LAVA: 
            case LIGHT_BLUE_WALL_BANNER: 
            case LIGHT_GRAY_WALL_BANNER: 
            case LIME_WALL_BANNER: 
            case MAGENTA_WALL_BANNER: 
            case MELON_STEM: 
            case MOVING_PISTON: 
            case NETHER_PORTAL: 
            case ORANGE_WALL_BANNER: 
            case PINK_WALL_BANNER: 
            case PISTON_HEAD: 
            case PLAYER_WALL_HEAD: 
            case POTATOES: 
            case POTTED_ACACIA_SAPLING: 
            case POTTED_ALLIUM: 
            case POTTED_AZURE_BLUET: 
            case POTTED_BIRCH_SAPLING: 
            case POTTED_BLUE_ORCHID: 
            case POTTED_BROWN_MUSHROOM: 
            case POTTED_CACTUS: 
            case POTTED_DANDELION: 
            case POTTED_DARK_OAK_SAPLING: 
            case POTTED_PALE_OAK_SAPLING: 
            case POTTED_DEAD_BUSH: 
            case POTTED_FERN: 
            case POTTED_JUNGLE_SAPLING: 
            case POTTED_OAK_SAPLING: 
            case POTTED_ORANGE_TULIP: 
            case POTTED_OXEYE_DAISY: 
            case POTTED_PINK_TULIP: 
            case POTTED_POPPY: 
            case POTTED_RED_MUSHROOM: 
            case POTTED_RED_TULIP: 
            case POTTED_SPRUCE_SAPLING: 
            case POTTED_WHITE_TULIP: 
            case PUMPKIN_STEM: 
            case PURPLE_WALL_BANNER: 
            case REDSTONE_WALL_TORCH: 
            case REDSTONE_WIRE: 
            case RED_WALL_BANNER: 
            case SKELETON_WALL_SKULL: 
            case TRIPWIRE: 
            case ACACIA_WALL_SIGN: 
            case OAK_WALL_SIGN: 
            case BIRCH_WALL_SIGN: 
            case JUNGLE_WALL_SIGN: 
            case SPRUCE_WALL_SIGN: 
            case DARK_OAK_WALL_SIGN: 
            case PALE_OAK_WALL_SIGN: 
            case WALL_TORCH: 
            case WATER: 
            case WHITE_WALL_BANNER: 
            case WITHER_SKELETON_WALL_SKULL: 
            case YELLOW_WALL_BANNER: 
            case ZOMBIE_WALL_HEAD: {
                return false;
            }
        }
        return true;
    }

    public static boolean isInteractable(XMaterial xMaterial) {
        if (XMaterial.supports(13)) {
            return xMaterial.get().isInteractable();
        }
        switch (xMaterial) {
            case MOVING_PISTON: 
            case POTTED_ACACIA_SAPLING: 
            case POTTED_ALLIUM: 
            case POTTED_AZURE_BLUET: 
            case POTTED_BIRCH_SAPLING: 
            case POTTED_BLUE_ORCHID: 
            case POTTED_BROWN_MUSHROOM: 
            case POTTED_CACTUS: 
            case POTTED_DANDELION: 
            case POTTED_DARK_OAK_SAPLING: 
            case POTTED_PALE_OAK_SAPLING: 
            case POTTED_DEAD_BUSH: 
            case POTTED_FERN: 
            case POTTED_JUNGLE_SAPLING: 
            case POTTED_OAK_SAPLING: 
            case POTTED_ORANGE_TULIP: 
            case POTTED_OXEYE_DAISY: 
            case POTTED_PINK_TULIP: 
            case POTTED_POPPY: 
            case POTTED_RED_MUSHROOM: 
            case POTTED_RED_TULIP: 
            case POTTED_SPRUCE_SAPLING: 
            case POTTED_WHITE_TULIP: 
            case ACACIA_WALL_SIGN: 
            case OAK_WALL_SIGN: 
            case BIRCH_WALL_SIGN: 
            case JUNGLE_WALL_SIGN: 
            case SPRUCE_WALL_SIGN: 
            case DARK_OAK_WALL_SIGN: 
            case PALE_OAK_WALL_SIGN: 
            case ACACIA_BUTTON: 
            case ACACIA_DOOR: 
            case ACACIA_FENCE: 
            case ACACIA_FENCE_GATE: 
            case ACACIA_STAIRS: 
            case ACACIA_TRAPDOOR: 
            case ANVIL: 
            case BEACON: 
            case BIRCH_BUTTON: 
            case BIRCH_DOOR: 
            case BIRCH_FENCE: 
            case PALE_OAK_BUTTON: 
            case PALE_OAK_DOOR: 
            case PALE_OAK_FENCE: 
            case PALE_OAK_FENCE_GATE: 
            case PALE_OAK_STAIRS: 
            case PALE_OAK_TRAPDOOR: 
            case BIRCH_FENCE_GATE: 
            case BIRCH_STAIRS: 
            case BIRCH_TRAPDOOR: 
            case BLACK_BED: 
            case BLACK_SHULKER_BOX: 
            case BLUE_BED: 
            case BLUE_SHULKER_BOX: 
            case BREWING_STAND: 
            case BRICK_STAIRS: 
            case BROWN_BED: 
            case BROWN_SHULKER_BOX: 
            case CAKE: 
            case CAULDRON: 
            case CHAIN_COMMAND_BLOCK: 
            case CHEST: 
            case CHIPPED_ANVIL: 
            case COBBLESTONE_STAIRS: 
            case COMMAND_BLOCK: 
            case COMPARATOR: 
            case CRAFTING_TABLE: 
            case CYAN_BED: 
            case CYAN_SHULKER_BOX: 
            case DAMAGED_ANVIL: 
            case DARK_OAK_BUTTON: 
            case DARK_OAK_DOOR: 
            case DARK_OAK_FENCE: 
            case DARK_OAK_FENCE_GATE: 
            case DARK_OAK_STAIRS: 
            case DARK_OAK_TRAPDOOR: 
            case DARK_PRISMARINE_STAIRS: 
            case DAYLIGHT_DETECTOR: 
            case DISPENSER: 
            case DRAGON_EGG: 
            case DROPPER: 
            case ENCHANTING_TABLE: 
            case ENDER_CHEST: 
            case FLOWER_POT: 
            case FURNACE: 
            case GRAY_BED: 
            case GRAY_SHULKER_BOX: 
            case GREEN_BED: 
            case GREEN_SHULKER_BOX: 
            case HOPPER: 
            case IRON_DOOR: 
            case IRON_TRAPDOOR: 
            case JUKEBOX: 
            case JUNGLE_BUTTON: 
            case JUNGLE_DOOR: 
            case JUNGLE_FENCE: 
            case JUNGLE_FENCE_GATE: 
            case JUNGLE_STAIRS: 
            case JUNGLE_TRAPDOOR: 
            case LEVER: 
            case LIGHT_BLUE_BED: 
            case LIGHT_BLUE_SHULKER_BOX: 
            case LIGHT_GRAY_BED: 
            case LIGHT_GRAY_SHULKER_BOX: 
            case LIME_BED: 
            case LIME_SHULKER_BOX: 
            case MAGENTA_BED: 
            case MAGENTA_SHULKER_BOX: 
            case NETHER_BRICK_FENCE: 
            case NETHER_BRICK_STAIRS: 
            case NOTE_BLOCK: 
            case OAK_BUTTON: 
            case OAK_DOOR: 
            case OAK_FENCE: 
            case OAK_FENCE_GATE: 
            case OAK_STAIRS: 
            case OAK_TRAPDOOR: 
            case ORANGE_BED: 
            case ORANGE_SHULKER_BOX: 
            case PINK_BED: 
            case PINK_SHULKER_BOX: 
            case PRISMARINE_BRICK_STAIRS: 
            case PRISMARINE_STAIRS: 
            case PUMPKIN: 
            case PURPLE_BED: 
            case PURPLE_SHULKER_BOX: 
            case PURPUR_STAIRS: 
            case QUARTZ_STAIRS: 
            case REDSTONE_ORE: 
            case RED_BED: 
            case RED_SANDSTONE_STAIRS: 
            case RED_SHULKER_BOX: 
            case REPEATER: 
            case REPEATING_COMMAND_BLOCK: 
            case SANDSTONE_STAIRS: 
            case SHULKER_BOX: 
            case ACACIA_SIGN: 
            case BIRCH_SIGN: 
            case DARK_OAK_SIGN: 
            case JUNGLE_SIGN: 
            case OAK_SIGN: 
            case SPRUCE_SIGN: 
            case SPRUCE_BUTTON: 
            case SPRUCE_DOOR: 
            case SPRUCE_FENCE: 
            case SPRUCE_FENCE_GATE: 
            case SPRUCE_STAIRS: 
            case SPRUCE_TRAPDOOR: 
            case STONE_BRICK_STAIRS: 
            case STONE_BUTTON: 
            case STRUCTURE_BLOCK: 
            case TNT: 
            case TRAPPED_CHEST: 
            case WHITE_BED: 
            case WHITE_SHULKER_BOX: 
            case YELLOW_BED: 
            case YELLOW_SHULKER_BOX: {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public Set<T> getValues() {
        return this.values;
    }

    public boolean isTagged(@Nullable T t) {
        return t != null && this.values.contains(t);
    }

    @SafeVarargs
    private final XTag<T> without(T ... TArray) {
        HashSet<T> hashSet = new HashSet<T>(this.values);
        for (T t : TArray) {
            hashSet.remove(t);
        }
        return new XTag<T>(hashSet);
    }

    public static Optional<XTag<?>> getTag(String string) {
        return Optional.ofNullable(TAGS.get(string));
    }

    static {
        FIRE = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.FIRE, XMaterial.SOUL_FIRE});
        PORTALS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.END_GATEWAY, XMaterial.END_PORTAL, XMaterial.NETHER_PORTAL});
        FLUID = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.LAVA, XMaterial.WATER});
        DANGEROUS_BLOCKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.MAGMA_BLOCK, XMaterial.LAVA, XMaterial.CAMPFIRE, XMaterial.FIRE, XMaterial.SOUL_FIRE});
        EFFECTIVE_SMITE_ENTITIES = TagBuilder.simple((XBase[])new XEntityType[]{XEntityType.ZOMBIE, XEntityType.SKELETON, XEntityType.WITHER, XEntityType.BEE, XEntityType.PHANTOM, XEntityType.DROWNED, XEntityType.WITHER_SKELETON, XEntityType.SKELETON_HORSE, XEntityType.STRAY, XEntityType.HUSK});
        EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES = TagBuilder.simple((XBase[])new XEntityType[]{XEntityType.SPIDER, XEntityType.CAVE_SPIDER, XEntityType.SILVERFISH, XEntityType.ENDERMITE});
        DEBUFFS = TagBuilder.simple((XBase[])new XPotion[]{XPotion.BAD_OMEN, XPotion.BLINDNESS, XPotion.NAUSEA, XPotion.INSTANT_DAMAGE, XPotion.HUNGER, XPotion.LEVITATION, XPotion.POISON, XPotion.SLOWNESS, XPotion.MINING_FATIGUE, XPotion.UNLUCK, XPotion.WEAKNESS, XPotion.WITHER});
        ACACIA_LOGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRIPPED_ACACIA_LOG, XMaterial.ACACIA_LOG, XMaterial.ACACIA_WOOD, XMaterial.STRIPPED_ACACIA_WOOD});
        BIRCH_LOGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRIPPED_BIRCH_LOG, XMaterial.BIRCH_LOG, XMaterial.BIRCH_WOOD, XMaterial.STRIPPED_BIRCH_WOOD});
        DARK_OAK_LOGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRIPPED_DARK_OAK_LOG, XMaterial.DARK_OAK_LOG, XMaterial.DARK_OAK_WOOD, XMaterial.STRIPPED_DARK_OAK_WOOD});
        JUNGLE_LOGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRIPPED_JUNGLE_LOG, XMaterial.JUNGLE_LOG, XMaterial.JUNGLE_WOOD, XMaterial.STRIPPED_JUNGLE_WOOD});
        MANGROVE_LOGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRIPPED_MANGROVE_LOG, XMaterial.MANGROVE_LOG, XMaterial.MANGROVE_WOOD, XMaterial.STRIPPED_MANGROVE_WOOD});
        OAK_LOGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRIPPED_OAK_LOG, XMaterial.OAK_LOG, XMaterial.OAK_WOOD, XMaterial.STRIPPED_OAK_WOOD});
        PALE_OAK_LOGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRIPPED_PALE_OAK_LOG, XMaterial.PALE_OAK_LOG, XMaterial.PALE_OAK_WOOD, XMaterial.STRIPPED_PALE_OAK_WOOD});
        SPRUCE_LOGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRIPPED_SPRUCE_LOG, XMaterial.SPRUCE_LOG, XMaterial.SPRUCE_WOOD, XMaterial.STRIPPED_SPRUCE_WOOD});
        CHERRY_LOGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRIPPED_CHERRY_LOG, XMaterial.CHERRY_LOG, XMaterial.CHERRY_WOOD, XMaterial.STRIPPED_CHERRY_WOOD});
        BAMBOO_LOGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRIPPED_BAMBOO_BLOCK, XMaterial.BAMBOO_BLOCK, XMaterial.BAMBOO_MOSAIC, XMaterial.BAMBOO_PLANKS});
        CANDLE_CAKES = TagBuilder.simple((XBase[])XTag.findAllColors("CANDLE_CAKE"));
        CANDLES = TagBuilder.simple((XBase[])XTag.findAllColors("CANDLE"));
        TERRACOTTA = TagBuilder.simple((XBase[])XTag.findAllColors("TERRACOTTA"));
        GLAZED_TERRACOTTA = TagBuilder.simple((XBase[])XTag.findAllColors("GLAZED_TERRACOTTA"));
        SHULKER_BOXES = TagBuilder.simple((XBase[])XTag.findAllColors("SHULKER_BOX"));
        CARPETS = TagBuilder.simple((XBase[])XTag.findAllColors("CARPET"));
        WOOL = TagBuilder.simple((XBase[])XTag.findAllColors("WOOL"));
        GLASS = ((TagBuilder)TagBuilder.of((XBase[])XTag.findAllColors("GLASS")).inheritFrom(new XTag[]{TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.TINTED_GLASS})})).build();
        ITEMS_BANNERS = TagBuilder.simple((XBase[])XTag.findAllColors("BANNER"));
        WALL_BANNERS = TagBuilder.simple((XBase[])XTag.findAllColors("WALL_BANNER"));
        BANNERS = TagBuilder.simple(new XTag[]{XTag.ITEMS_BANNERS, XTag.WALL_BANNERS});
        BEDS = TagBuilder.simple((XBase[])XTag.findAllColors("BED"));
        CONCRETE = TagBuilder.simple((XBase[])XTag.findAllColors("CONCRETE"));
        CONCRETE_POWDER = TagBuilder.simple((XBase[])XTag.findAllColors("CONCRETE_POWDER"));
        STANDING_SIGNS = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("SIGN"));
        WALL_SIGNS = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("WALL_SIGN"));
        WALL_HANGING_SIGNS = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("WALL_HANGING_SIGN"));
        HANGING_SIGNS = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("HANGING_SIGN"));
        WOODEN_PRESSURE_PLATES = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("PRESSURE_PLATE"));
        WOODEN_DOORS = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("DOOR"));
        WOODEN_FENCE_GATES = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("FENCE_GATE"));
        WOODEN_FENCES = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("FENCE"));
        WOODEN_SLABS = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("SLAB"));
        WOODEN_STAIRS = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("STAIRS"));
        WOODEN_TRAPDOORS = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("TRAPDOOR"));
        PLANKS = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("PLANKS"));
        WOODEN_BUTTONS = TagBuilder.simple((XBase[])XTag.findAllWoodTypes("BUTTON"));
        COAL_ORES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.COAL_ORE, XMaterial.DEEPSLATE_COAL_ORE});
        IRON_ORES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.IRON_ORE, XMaterial.DEEPSLATE_IRON_ORE});
        COPPER_ORES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.COPPER_ORE, XMaterial.DEEPSLATE_COPPER_ORE});
        REDSTONE_ORES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.REDSTONE_ORE, XMaterial.DEEPSLATE_REDSTONE_ORE});
        LAPIS_ORES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.LAPIS_ORE, XMaterial.DEEPSLATE_LAPIS_ORE});
        GOLD_ORES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.GOLD_ORE, XMaterial.DEEPSLATE_GOLD_ORE, XMaterial.NETHER_GOLD_ORE});
        DIAMOND_ORES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.DIAMOND_ORE, XMaterial.DEEPSLATE_DIAMOND_ORE});
        EMERALD_ORES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.EMERALD_ORE, XMaterial.DEEPSLATE_EMERALD_ORE});
        ORES = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.ANCIENT_DEBRIS, XMaterial.NETHER_QUARTZ_ORE}).inheritFrom(new XTag[]{XTag.COAL_ORES, XTag.IRON_ORES, XTag.COPPER_ORES, XTag.REDSTONE_ORES, XTag.LAPIS_ORES, XTag.GOLD_ORES, XTag.DIAMOND_ORES, XTag.EMERALD_ORES})).build();
        ALIVE_CORAL_WALL_FANS = TagBuilder.simple((XBase[])XTag.findAllCorals(true, false, true, true));
        ALIVE_CORAL_FANS = TagBuilder.simple((XBase[])XTag.findAllCorals(true, false, true, false));
        ALIVE_CORAL_BLOCKS = TagBuilder.simple((XBase[])XTag.findAllCorals(true, true, false, false));
        ALIVE_CORAL_PLANTS = TagBuilder.simple((XBase[])XTag.findAllCorals(true, false, false, false));
        DEAD_CORAL_WALL_FANS = TagBuilder.simple((XBase[])XTag.findAllCorals(false, false, true, true));
        DEAD_CORAL_FANS = TagBuilder.simple((XBase[])XTag.findAllCorals(false, false, true, false));
        DEAD_CORAL_BLOCKS = TagBuilder.simple((XBase[])XTag.findAllCorals(false, true, false, false));
        DEAD_CORAL_PLANTS = TagBuilder.simple((XBase[])XTag.findAllCorals(false, false, false, false));
        CORAL_FANS = TagBuilder.simple(new XTag[]{XTag.ALIVE_CORAL_FANS, XTag.ALIVE_CORAL_WALL_FANS, XTag.DEAD_CORAL_WALL_FANS, XTag.DEAD_CORAL_FANS});
        CORALS = TagBuilder.simple(new XTag[]{XTag.ALIVE_CORAL_WALL_FANS, XTag.ALIVE_CORAL_FANS, XTag.ALIVE_CORAL_BLOCKS, XTag.ALIVE_CORAL_PLANTS, XTag.DEAD_CORAL_WALL_FANS, XTag.DEAD_CORAL_FANS, XTag.DEAD_CORAL_BLOCKS, XTag.DEAD_CORAL_PLANTS});
        WALL_HEADS = TagBuilder.simple(new XTag[]{TagBuilder.simple((XBase[])XTag.findMaterialsEndingWith("WALL_HEAD")), TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.WITHER_SKELETON_WALL_SKULL, XMaterial.SKELETON_WALL_SKULL})});
        WALL_TORCHES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.WALL_TORCH, XMaterial.SOUL_WALL_TORCH, XMaterial.REDSTONE_WALL_TORCH});
        WALLS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.POLISHED_DEEPSLATE_WALL, XMaterial.NETHER_BRICK_WALL, XMaterial.POLISHED_BLACKSTONE_WALL, XMaterial.DEEPSLATE_BRICK_WALL, XMaterial.RED_SANDSTONE_WALL, XMaterial.BRICK_WALL, XMaterial.COBBLESTONE_WALL, XMaterial.POLISHED_BLACKSTONE_BRICK_WALL, XMaterial.PRISMARINE_WALL, XMaterial.SANDSTONE_WALL, XMaterial.GRANITE_WALL, XMaterial.DEEPSLATE_TILE_WALL, XMaterial.BLACKSTONE_WALL, XMaterial.STONE_BRICK_WALL, XMaterial.RED_NETHER_BRICK_WALL, XMaterial.DIORITE_WALL, XMaterial.MOSSY_COBBLESTONE_WALL, XMaterial.ANDESITE_WALL, XMaterial.MOSSY_STONE_BRICK_WALL, XMaterial.END_STONE_BRICK_WALL, XMaterial.COBBLED_DEEPSLATE_WALL});
        STONE_PRESSURE_PLATES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STONE_PRESSURE_PLATE, XMaterial.POLISHED_BLACKSTONE_PRESSURE_PLATE});
        RAILS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.RAIL, XMaterial.ACTIVATOR_RAIL, XMaterial.DETECTOR_RAIL, XMaterial.POWERED_RAIL});
        ANIMALS_SPAWNABLE_ON = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.GRASS_BLOCK});
        ANVIL = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.ANVIL, XMaterial.CHIPPED_ANVIL, XMaterial.DAMAGED_ANVIL});
        AXOLOTL_TEMPT_ITEMS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.TROPICAL_FISH_BUCKET});
        AXOLOTLS_SPAWNABLE_ON = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CLAY});
        SNOW = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.SNOW_BLOCK, XMaterial.SNOW, XMaterial.POWDER_SNOW});
        SAND = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.SAND, XMaterial.RED_SAND});
        DIRT = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.MOSS_BLOCK, XMaterial.COARSE_DIRT, XMaterial.PODZOL, XMaterial.DIRT, XMaterial.ROOTED_DIRT, XMaterial.MYCELIUM, XMaterial.GRASS_BLOCK});
        CAVE_VINES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CAVE_VINES, XMaterial.CAVE_VINES_PLANT});
        BASE_STONE_NETHER = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.NETHERRACK, XMaterial.BASALT, XMaterial.BLACKSTONE});
        BASE_STONE_OVERWORLD = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.TUFF, XMaterial.DIORITE, XMaterial.DEEPSLATE, XMaterial.ANDESITE, XMaterial.GRANITE, XMaterial.STONE});
        BEACON_BASE_BLOCKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.NETHERITE_BLOCK, XMaterial.GOLD_BLOCK, XMaterial.IRON_BLOCK, XMaterial.EMERALD_BLOCK, XMaterial.DIAMOND_BLOCK});
        CROPS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CARROT, XMaterial.CARROTS, XMaterial.POTATO, XMaterial.POTATOES, XMaterial.NETHER_WART, XMaterial.PUMPKIN_SEEDS, XMaterial.WHEAT_SEEDS, XMaterial.WHEAT, XMaterial.MELON_SEEDS, XMaterial.BEETROOT_SEEDS, XMaterial.BEETROOTS, XMaterial.SUGAR_CANE, XMaterial.BAMBOO_SAPLING, XMaterial.BAMBOO, XMaterial.CHORUS_PLANT, XMaterial.KELP, XMaterial.KELP_PLANT, XMaterial.SEA_PICKLE, XMaterial.BROWN_MUSHROOM, XMaterial.RED_MUSHROOM, XMaterial.MELON_STEM, XMaterial.PUMPKIN_STEM, XMaterial.COCOA, XMaterial.COCOA_BEANS});
        CAMPFIRES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CAMPFIRE, XMaterial.SOUL_CAMPFIRE});
        FILLED_CAULDRONS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.LAVA_CAULDRON, XMaterial.POWDER_SNOW_CAULDRON, XMaterial.WATER_CAULDRON});
        CAULDRONS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CAULDRON, XMaterial.LAVA_CAULDRON, XMaterial.POWDER_SNOW_CAULDRON, XMaterial.WATER_CAULDRON});
        CLIMBABLE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.SCAFFOLDING, XMaterial.WEEPING_VINES_PLANT, XMaterial.WEEPING_VINES, XMaterial.TWISTING_VINES, XMaterial.TWISTING_VINES_PLANT, XMaterial.VINE, XMaterial.LADDER}).inheritFrom(new XTag[]{XTag.CAVE_VINES})).build();
        CLUSTER_MAX_HARVESTABLES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.DIAMOND_PICKAXE, XMaterial.GOLDEN_PICKAXE, XMaterial.STONE_PICKAXE, XMaterial.NETHERITE_PICKAXE, XMaterial.WOODEN_PICKAXE, XMaterial.IRON_PICKAXE});
        CRIMSON_STEMS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CRIMSON_HYPHAE, XMaterial.STRIPPED_CRIMSON_STEM, XMaterial.CRIMSON_STEM, XMaterial.STRIPPED_CRIMSON_HYPHAE});
        WARPED_STEMS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.WARPED_HYPHAE, XMaterial.STRIPPED_WARPED_STEM, XMaterial.WARPED_STEM, XMaterial.STRIPPED_WARPED_HYPHAE});
        CRYSTAL_SOUND_BLOCKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.AMETHYST_BLOCK, XMaterial.BUDDING_AMETHYST});
        DEEPSLATE_ORE_REPLACEABLES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.TUFF, XMaterial.DEEPSLATE});
        DOORS = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.IRON_DOOR}).inheritFrom(new XTag[]{XTag.WOODEN_DOORS})).build();
        WITHER_IMMUNE = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STRUCTURE_BLOCK, XMaterial.END_GATEWAY, XMaterial.BEDROCK, XMaterial.END_PORTAL, XMaterial.COMMAND_BLOCK, XMaterial.REPEATING_COMMAND_BLOCK, XMaterial.MOVING_PISTON, XMaterial.CHAIN_COMMAND_BLOCK, XMaterial.BARRIER, XMaterial.END_PORTAL_FRAME, XMaterial.JIGSAW});
        WITHER_SUMMON_BASE_BLOCKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.SOUL_SOIL, XMaterial.SOUL_SAND});
        NYLIUM = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CRIMSON_NYLIUM, XMaterial.WARPED_NYLIUM});
        SMALL_FLOWERS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.RED_TULIP, XMaterial.AZURE_BLUET, XMaterial.OXEYE_DAISY, XMaterial.BLUE_ORCHID, XMaterial.PINK_TULIP, XMaterial.POPPY, XMaterial.WHITE_TULIP, XMaterial.DANDELION, XMaterial.ALLIUM, XMaterial.CORNFLOWER, XMaterial.ORANGE_TULIP, XMaterial.LILY_OF_THE_VALLEY, XMaterial.WITHER_ROSE});
        TALL_FLOWERS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.PEONY, XMaterial.SUNFLOWER, XMaterial.LILAC, XMaterial.ROSE_BUSH});
        FEATURES_CANNOT_REPLACE = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.SPAWNER, XMaterial.END_PORTAL_FRAME, XMaterial.BEDROCK, XMaterial.CHEST});
        FENCE_GATES = TagBuilder.simple(new XTag[]{XTag.WOODEN_FENCE_GATES});
        FENCES = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.NETHER_BRICK_FENCE}).inheritFrom(new XTag[]{XTag.WOODEN_FENCES})).build();
        FLOWER_POTS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.POTTED_OAK_SAPLING, XMaterial.POTTED_WITHER_ROSE, XMaterial.POTTED_ACACIA_SAPLING, XMaterial.POTTED_LILY_OF_THE_VALLEY, XMaterial.POTTED_WARPED_FUNGUS, XMaterial.POTTED_WARPED_ROOTS, XMaterial.POTTED_ALLIUM, XMaterial.POTTED_BROWN_MUSHROOM, XMaterial.POTTED_WHITE_TULIP, XMaterial.POTTED_ORANGE_TULIP, XMaterial.POTTED_DANDELION, XMaterial.POTTED_AZURE_BLUET, XMaterial.POTTED_FLOWERING_AZALEA_BUSH, XMaterial.POTTED_PINK_TULIP, XMaterial.POTTED_CORNFLOWER, XMaterial.POTTED_CRIMSON_FUNGUS, XMaterial.POTTED_RED_MUSHROOM, XMaterial.POTTED_BLUE_ORCHID, XMaterial.POTTED_FERN, XMaterial.POTTED_POPPY, XMaterial.POTTED_CRIMSON_ROOTS, XMaterial.POTTED_RED_TULIP, XMaterial.POTTED_OXEYE_DAISY, XMaterial.POTTED_AZALEA_BUSH, XMaterial.POTTED_BAMBOO, XMaterial.POTTED_CACTUS, XMaterial.FLOWER_POT, XMaterial.POTTED_DEAD_BUSH, XMaterial.POTTED_DARK_OAK_SAPLING, XMaterial.POTTED_PALE_OAK_SAPLING, XMaterial.POTTED_SPRUCE_SAPLING, XMaterial.POTTED_JUNGLE_SAPLING, XMaterial.POTTED_BIRCH_SAPLING, XMaterial.POTTED_MANGROVE_PROPAGULE, XMaterial.POTTED_CHERRY_SAPLING, XMaterial.POTTED_TORCHFLOWER});
        FOX_FOOD = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.GLOW_BERRIES, XMaterial.SWEET_BERRIES});
        FOXES_SPAWNABLE_ON = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.SNOW, XMaterial.SNOW_BLOCK, XMaterial.PODZOL, XMaterial.GRASS_BLOCK, XMaterial.COARSE_DIRT});
        FREEZE_IMMUNE_WEARABLES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.LEATHER_BOOTS, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_HELMET, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_HORSE_ARMOR});
        ICE = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.ICE, XMaterial.PACKED_ICE, XMaterial.BLUE_ICE, XMaterial.FROSTED_ICE});
        GEODE_INVALID_BLOCKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.BEDROCK, XMaterial.WATER, XMaterial.LAVA, XMaterial.ICE, XMaterial.PACKED_ICE, XMaterial.BLUE_ICE});
        HOGLIN_REPELLENTS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.WARPED_FUNGUS, XMaterial.NETHER_PORTAL, XMaterial.POTTED_WARPED_FUNGUS, XMaterial.RESPAWN_ANCHOR});
        IGNORED_BY_PIGLIN_BABIES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.LEATHER});
        IMPERMEABLE = TagBuilder.simple(new XTag[]{XTag.GLASS});
        INFINIBURN_END = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.BEDROCK, XMaterial.NETHERRACK, XMaterial.MAGMA_BLOCK});
        INFINIBURN_NETHER = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.NETHERRACK, XMaterial.MAGMA_BLOCK});
        INFINIBURN_OVERWORLD = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.NETHERRACK, XMaterial.MAGMA_BLOCK});
        INSIDE_STEP_SOUND_BLOCKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.SNOW, XMaterial.POWDER_SNOW});
        ITEMS_ARROWS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.ARROW, XMaterial.SPECTRAL_ARROW, XMaterial.TIPPED_ARROW});
        ITEMS_BEACON_PAYMENT_ITEMS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.EMERALD, XMaterial.DIAMOND, XMaterial.NETHERITE_INGOT, XMaterial.IRON_INGOT, XMaterial.GOLD_INGOT});
        ITEMS_BOATS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.OAK_BOAT, XMaterial.ACACIA_BOAT, XMaterial.DARK_OAK_BOAT, XMaterial.PALE_OAK_BOAT, XMaterial.BIRCH_BOAT, XMaterial.SPRUCE_BOAT, XMaterial.JUNGLE_BOAT, XMaterial.MANGROVE_BOAT, XMaterial.CHERRY_BOAT, XMaterial.BAMBOO_RAFT});
        ITEMS_COALS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.COAL, XMaterial.CHARCOAL});
        ITEMS_CREEPER_DROP_MUSIC_DISCS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.MUSIC_DISC_BLOCKS, XMaterial.MUSIC_DISC_11, XMaterial.MUSIC_DISC_WAIT, XMaterial.MUSIC_DISC_MELLOHI, XMaterial.MUSIC_DISC_STAL, XMaterial.MUSIC_DISC_WARD, XMaterial.MUSIC_DISC_13, XMaterial.MUSIC_DISC_CAT, XMaterial.MUSIC_DISC_CHIRP, XMaterial.MUSIC_DISC_MALL, XMaterial.MUSIC_DISC_FAR, XMaterial.MUSIC_DISC_STRAD});
        ITEMS_FISHES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.TROPICAL_FISH, XMaterial.SALMON, XMaterial.PUFFERFISH, XMaterial.COOKED_COD, XMaterial.COD, XMaterial.COOKED_SALMON});
        ITEMS_LECTERN_BOOKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.WRITABLE_BOOK, XMaterial.WRITTEN_BOOK});
        ITEMS_STONE_TOOL_MATERIALS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.COBBLED_DEEPSLATE, XMaterial.BLACKSTONE, XMaterial.COBBLESTONE});
        LEAVES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.SPRUCE_LEAVES, XMaterial.ACACIA_LEAVES, XMaterial.DARK_OAK_LEAVES, XMaterial.AZALEA_LEAVES, XMaterial.JUNGLE_LEAVES, XMaterial.FLOWERING_AZALEA_LEAVES, XMaterial.BIRCH_LEAVES, XMaterial.OAK_LEAVES, XMaterial.MANGROVE_LEAVES, XMaterial.CHERRY_LEAVES});
        NON_WOODEN_STAIRS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STONE_BRICK_STAIRS, XMaterial.STONE_STAIRS, XMaterial.POLISHED_BLACKSTONE_BRICK_STAIRS, XMaterial.RED_SANDSTONE_STAIRS, XMaterial.PRISMARINE_STAIRS, XMaterial.GRANITE_STAIRS, XMaterial.WAXED_WEATHERED_CUT_COPPER_STAIRS, XMaterial.POLISHED_DIORITE_STAIRS, XMaterial.WEATHERED_CUT_COPPER_STAIRS, XMaterial.NETHER_BRICK_STAIRS, XMaterial.RED_NETHER_BRICK_STAIRS, XMaterial.PRISMARINE_BRICK_STAIRS, XMaterial.WAXED_CUT_COPPER_STAIRS, XMaterial.DEEPSLATE_TILE_STAIRS, XMaterial.POLISHED_ANDESITE_STAIRS, XMaterial.SMOOTH_RED_SANDSTONE_STAIRS, XMaterial.PURPUR_STAIRS, XMaterial.POLISHED_DEEPSLATE_STAIRS, XMaterial.QUARTZ_STAIRS, XMaterial.MOSSY_COBBLESTONE_STAIRS, XMaterial.BRICK_STAIRS, XMaterial.CUT_COPPER_STAIRS, XMaterial.SANDSTONE_STAIRS, XMaterial.ANDESITE_STAIRS, XMaterial.WAXED_EXPOSED_CUT_COPPER_STAIRS, XMaterial.COBBLED_DEEPSLATE_STAIRS, XMaterial.COBBLESTONE_STAIRS, XMaterial.DEEPSLATE_BRICK_STAIRS, XMaterial.DIORITE_STAIRS, XMaterial.SMOOTH_QUARTZ_STAIRS, XMaterial.EXPOSED_CUT_COPPER_STAIRS, XMaterial.DARK_PRISMARINE_STAIRS, XMaterial.OXIDIZED_CUT_COPPER_STAIRS, XMaterial.POLISHED_BLACKSTONE_STAIRS, XMaterial.POLISHED_GRANITE_STAIRS, XMaterial.MOSSY_STONE_BRICK_STAIRS, XMaterial.END_STONE_BRICK_STAIRS, XMaterial.WAXED_OXIDIZED_CUT_COPPER_STAIRS, XMaterial.SMOOTH_SANDSTONE_STAIRS, XMaterial.BLACKSTONE_STAIRS});
        STAIRS = TagBuilder.simple(new XTag[]{XTag.NON_WOODEN_STAIRS, XTag.WOODEN_STAIRS});
        NON_WOODEN_SLABS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.MOSSY_COBBLESTONE_SLAB, XMaterial.EXPOSED_CUT_COPPER_SLAB, XMaterial.SMOOTH_QUARTZ_SLAB, XMaterial.COBBLESTONE_SLAB, XMaterial.POLISHED_BLACKSTONE_SLAB, XMaterial.OXIDIZED_CUT_COPPER_SLAB, XMaterial.POLISHED_ANDESITE_SLAB, XMaterial.RED_SANDSTONE_SLAB, XMaterial.BLACKSTONE_SLAB, XMaterial.STONE_SLAB, XMaterial.SMOOTH_SANDSTONE_SLAB, XMaterial.COBBLED_DEEPSLATE_SLAB, XMaterial.SMOOTH_RED_SANDSTONE_SLAB, XMaterial.POLISHED_DIORITE_SLAB, XMaterial.PRISMARINE_BRICK_SLAB, XMaterial.QUARTZ_SLAB, XMaterial.DIORITE_SLAB, XMaterial.NETHER_BRICK_SLAB, XMaterial.PRISMARINE_SLAB, XMaterial.WAXED_EXPOSED_CUT_COPPER_SLAB, XMaterial.RED_NETHER_BRICK_SLAB, XMaterial.POLISHED_BLACKSTONE_BRICK_SLAB, XMaterial.MOSSY_STONE_BRICK_SLAB, XMaterial.SMOOTH_STONE_SLAB, XMaterial.SANDSTONE_SLAB, XMaterial.WEATHERED_CUT_COPPER_SLAB, XMaterial.DEEPSLATE_BRICK_SLAB, XMaterial.POLISHED_DEEPSLATE_SLAB, XMaterial.GRANITE_SLAB, XMaterial.ANDESITE_SLAB, XMaterial.CUT_COPPER_SLAB, XMaterial.CUT_SANDSTONE_SLAB, XMaterial.END_STONE_BRICK_SLAB, XMaterial.WAXED_OXIDIZED_CUT_COPPER_SLAB, XMaterial.CUT_RED_SANDSTONE_SLAB, XMaterial.PURPUR_SLAB, XMaterial.STONE_BRICK_SLAB, XMaterial.WAXED_CUT_COPPER_SLAB, XMaterial.DEEPSLATE_TILE_SLAB, XMaterial.DARK_PRISMARINE_SLAB, XMaterial.PETRIFIED_OAK_SLAB, XMaterial.WAXED_WEATHERED_CUT_COPPER_SLAB, XMaterial.BRICK_SLAB, XMaterial.POLISHED_GRANITE_SLAB});
        POTTERY_SHERDS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.ANGLER_POTTERY_SHERD, XMaterial.ARCHER_POTTERY_SHERD, XMaterial.ARMS_UP_POTTERY_SHERD, XMaterial.BLADE_POTTERY_SHERD, XMaterial.BREWER_POTTERY_SHERD, XMaterial.BURN_POTTERY_SHERD, XMaterial.DANGER_POTTERY_SHERD, XMaterial.EXPLORER_POTTERY_SHERD, XMaterial.FRIEND_POTTERY_SHERD, XMaterial.HEART_POTTERY_SHERD, XMaterial.HEARTBREAK_POTTERY_SHERD, XMaterial.HOWL_POTTERY_SHERD, XMaterial.MINER_POTTERY_SHERD, XMaterial.MOURNER_POTTERY_SHERD, XMaterial.PLENTY_POTTERY_SHERD, XMaterial.PRIZE_POTTERY_SHERD, XMaterial.SHEAF_POTTERY_SHERD, XMaterial.SHELTER_POTTERY_SHERD, XMaterial.SKULL_POTTERY_SHERD, XMaterial.SNORT_POTTERY_SHERD});
        SOUL_FIRE_BASE_BLOCKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.SOUL_SOIL, XMaterial.SOUL_SAND});
        SOUL_SPEED_BLOCKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.SOUL_SOIL, XMaterial.SOUL_SAND});
        STONE_ORE_REPLACEABLES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STONE, XMaterial.DIORITE, XMaterial.ANDESITE, XMaterial.GRANITE});
        STRIDER_WARM_BLOCKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.LAVA});
        VALID_SPAWN = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.PODZOL, XMaterial.GRASS_BLOCK});
        STONE_BRICKS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CHISELED_STONE_BRICKS, XMaterial.CRACKED_STONE_BRICKS, XMaterial.MOSSY_STONE_BRICKS, XMaterial.STONE_BRICKS});
        SAPLINGS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.ACACIA_SAPLING, XMaterial.JUNGLE_SAPLING, XMaterial.SPRUCE_SAPLING, XMaterial.DARK_OAK_SAPLING, XMaterial.PALE_OAK_SAPLING, XMaterial.AZALEA, XMaterial.OAK_SAPLING, XMaterial.FLOWERING_AZALEA, XMaterial.BIRCH_SAPLING, XMaterial.MANGROVE_PROPAGULE, XMaterial.CHERRY_SAPLING});
        WOLVES_SPAWNABLE_ON = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.GRASS_BLOCK, XMaterial.SNOW, XMaterial.SNOW_BLOCK});
        POLAR_BEARS_SPAWNABLE_ON_IN_FROZEN_OCEAN = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.ICE});
        RABBITS_SPAWNABLE_ON = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.GRASS_BLOCK, XMaterial.SNOW, XMaterial.SNOW_BLOCK, XMaterial.SAND});
        PIGLIN_FOOD = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.COOKED_PORKCHOP, XMaterial.PORKCHOP});
        PIGLIN_REPELLENTS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.SOUL_WALL_TORCH, XMaterial.SOUL_TORCH, XMaterial.SOUL_CAMPFIRE, XMaterial.SOUL_LANTERN, XMaterial.SOUL_FIRE});
        REPLACEABLE_PLANTS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.FERN, XMaterial.GLOW_LICHEN, XMaterial.DEAD_BUSH, XMaterial.PEONY, XMaterial.TALL_GRASS, XMaterial.HANGING_ROOTS, XMaterial.VINE, XMaterial.SUNFLOWER, XMaterial.LARGE_FERN, XMaterial.LILAC, XMaterial.ROSE_BUSH, XMaterial.SHORT_GRASS});
        SMALL_DRIPLEAF_PLACEABLE = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CLAY, XMaterial.MOSS_BLOCK});
        NON_FLAMMABLE_WOOD = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CRIMSON_PLANKS, XMaterial.WARPED_WALL_SIGN, XMaterial.CRIMSON_FENCE_GATE, XMaterial.WARPED_HYPHAE, XMaterial.CRIMSON_HYPHAE, XMaterial.WARPED_STEM, XMaterial.WARPED_TRAPDOOR, XMaterial.STRIPPED_CRIMSON_HYPHAE, XMaterial.CRIMSON_PRESSURE_PLATE, XMaterial.WARPED_STAIRS, XMaterial.CRIMSON_SIGN, XMaterial.CRIMSON_STAIRS, XMaterial.STRIPPED_WARPED_STEM, XMaterial.CRIMSON_FENCE, XMaterial.WARPED_FENCE, XMaterial.CRIMSON_TRAPDOOR, XMaterial.STRIPPED_WARPED_HYPHAE, XMaterial.WARPED_DOOR, XMaterial.WARPED_PRESSURE_PLATE, XMaterial.WARPED_PLANKS, XMaterial.STRIPPED_CRIMSON_STEM, XMaterial.CRIMSON_STEM, XMaterial.CRIMSON_SLAB, XMaterial.CRIMSON_WALL_SIGN, XMaterial.WARPED_FENCE_GATE, XMaterial.WARPED_BUTTON, XMaterial.WARPED_SLAB, XMaterial.CRIMSON_DOOR, XMaterial.CRIMSON_BUTTON, XMaterial.WARPED_SIGN});
        MOOSHROOMS_SPAWNABLE_ON = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.MYCELIUM});
        NEEDS_STONE_TOOL = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.OXIDIZED_CUT_COPPER, XMaterial.DEEPSLATE_COPPER_ORE, XMaterial.EXPOSED_CUT_COPPER_SLAB, XMaterial.WAXED_OXIDIZED_CUT_COPPER_SLAB, XMaterial.WAXED_OXIDIZED_CUT_COPPER, XMaterial.OXIDIZED_CUT_COPPER_SLAB, XMaterial.WAXED_WEATHERED_CUT_COPPER, XMaterial.WAXED_WEATHERED_CUT_COPPER_STAIRS, XMaterial.WEATHERED_COPPER, XMaterial.WEATHERED_CUT_COPPER_STAIRS, XMaterial.EXPOSED_CUT_COPPER, XMaterial.DEEPSLATE_LAPIS_ORE, XMaterial.COPPER_ORE, XMaterial.WEATHERED_CUT_COPPER, XMaterial.WAXED_CUT_COPPER_STAIRS, XMaterial.WAXED_EXPOSED_CUT_COPPER, XMaterial.OXIDIZED_COPPER, XMaterial.WAXED_COPPER_BLOCK, XMaterial.RAW_IRON_BLOCK, XMaterial.LAPIS_BLOCK, XMaterial.DEEPSLATE_IRON_ORE, XMaterial.CUT_COPPER_STAIRS, XMaterial.COPPER_BLOCK, XMaterial.WAXED_WEATHERED_CUT_COPPER_SLAB, XMaterial.IRON_BLOCK, XMaterial.WAXED_EXPOSED_CUT_COPPER_STAIRS, XMaterial.RAW_COPPER_BLOCK, XMaterial.LAPIS_ORE, XMaterial.WEATHERED_CUT_COPPER_SLAB, XMaterial.CUT_COPPER_SLAB, XMaterial.IRON_ORE, XMaterial.EXPOSED_COPPER, XMaterial.WAXED_EXPOSED_COPPER, XMaterial.EXPOSED_CUT_COPPER_STAIRS, XMaterial.WAXED_CUT_COPPER_SLAB, XMaterial.WAXED_EXPOSED_CUT_COPPER_SLAB, XMaterial.OXIDIZED_CUT_COPPER_STAIRS, XMaterial.WAXED_OXIDIZED_COPPER, XMaterial.WAXED_CUT_COPPER, XMaterial.WAXED_WEATHERED_COPPER, XMaterial.LIGHTNING_ROD, XMaterial.WAXED_OXIDIZED_CUT_COPPER_STAIRS, XMaterial.CUT_COPPER});
        NEEDS_IRON_TOOL = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.GOLD_ORE, XMaterial.GOLD_BLOCK, XMaterial.REDSTONE_ORE, XMaterial.RAW_GOLD_BLOCK, XMaterial.EMERALD_BLOCK, XMaterial.DIAMOND_BLOCK, XMaterial.DIAMOND_ORE, XMaterial.DEEPSLATE_EMERALD_ORE, XMaterial.DEEPSLATE_GOLD_ORE, XMaterial.EMERALD_ORE, XMaterial.DEEPSLATE_REDSTONE_ORE, XMaterial.DEEPSLATE_DIAMOND_ORE});
        NEEDS_DIAMOND_TOOL = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.OBSIDIAN, XMaterial.NETHERITE_BLOCK, XMaterial.ANCIENT_DEBRIS, XMaterial.RESPAWN_ANCHOR, XMaterial.CRYING_OBSIDIAN});
        MINEABLE_PICKAXE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.OXIDIZED_CUT_COPPER, XMaterial.GOLD_BLOCK, XMaterial.SMOOTH_SANDSTONE, XMaterial.IRON_DOOR, XMaterial.COBBLESTONE, XMaterial.DRIPSTONE_BLOCK, XMaterial.CHISELED_SANDSTONE, XMaterial.INFESTED_STONE_BRICKS, XMaterial.QUARTZ_BLOCK, XMaterial.COPPER_BLOCK, XMaterial.STONE_BRICKS, XMaterial.CHISELED_POLISHED_BLACKSTONE, XMaterial.DISPENSER, XMaterial.DEEPSLATE_BRICKS, XMaterial.HEAVY_WEIGHTED_PRESSURE_PLATE, XMaterial.OBSIDIAN, XMaterial.EXPOSED_CUT_COPPER, XMaterial.SMOOTH_QUARTZ, XMaterial.SMOOTH_RED_SANDSTONE, XMaterial.STONE, XMaterial.INFESTED_COBBLESTONE, XMaterial.WAXED_CUT_COPPER, XMaterial.PRISMARINE, XMaterial.PISTON, XMaterial.CUT_COPPER, XMaterial.CHISELED_QUARTZ_BLOCK, XMaterial.MOSSY_STONE_BRICKS, XMaterial.EMERALD_BLOCK, XMaterial.BELL, XMaterial.AMETHYST_BLOCK, XMaterial.GILDED_BLACKSTONE, XMaterial.CHISELED_NETHER_BRICKS, XMaterial.WAXED_COPPER_BLOCK, XMaterial.IRON_BLOCK, XMaterial.BUDDING_AMETHYST, XMaterial.POLISHED_DEEPSLATE, XMaterial.HOPPER, XMaterial.CUT_RED_SANDSTONE, XMaterial.QUARTZ_BRICKS, XMaterial.CHISELED_STONE_BRICKS, XMaterial.ENDER_CHEST, XMaterial.END_STONE_BRICKS, XMaterial.NETHERRACK, XMaterial.REDSTONE_BLOCK, XMaterial.WAXED_OXIDIZED_CUT_COPPER, XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE, XMaterial.WAXED_WEATHERED_CUT_COPPER, XMaterial.CHAIN, XMaterial.MAGMA_BLOCK, XMaterial.STONE_PRESSURE_PLATE, XMaterial.DARK_PRISMARINE, XMaterial.MEDIUM_AMETHYST_BUD, XMaterial.LANTERN, XMaterial.ICE, XMaterial.DIORITE, XMaterial.DROPPER, XMaterial.CRACKED_NETHER_BRICKS, XMaterial.BREWING_STAND, XMaterial.CHISELED_RED_SANDSTONE, XMaterial.CALCITE, XMaterial.CUT_SANDSTONE, XMaterial.POLISHED_BASALT, XMaterial.DEEPSLATE_TILES, XMaterial.QUARTZ_PILLAR, XMaterial.LODESTONE, XMaterial.POLISHED_GRANITE, XMaterial.POLISHED_ANDESITE, XMaterial.OBSERVER, XMaterial.CHISELED_DEEPSLATE, XMaterial.RAW_GOLD_BLOCK, XMaterial.CRACKED_POLISHED_BLACKSTONE_BRICKS, XMaterial.WAXED_EXPOSED_CUT_COPPER, XMaterial.SMALL_AMETHYST_BUD, XMaterial.OXIDIZED_COPPER, XMaterial.POLISHED_BLACKSTONE, XMaterial.RAW_IRON_BLOCK, XMaterial.POLISHED_BLACKSTONE_BRICKS, XMaterial.INFESTED_DEEPSLATE, XMaterial.RAW_COPPER_BLOCK, XMaterial.BLACKSTONE, XMaterial.AMETHYST_CLUSTER, XMaterial.GRINDSTONE, XMaterial.WAXED_EXPOSED_COPPER, XMaterial.RED_SANDSTONE, XMaterial.LIGHTNING_ROD, XMaterial.SOUL_LANTERN, XMaterial.POLISHED_BLACKSTONE_PRESSURE_PLATE, XMaterial.IRON_BARS, XMaterial.PURPUR_BLOCK, XMaterial.FURNACE, XMaterial.CONDUIT, XMaterial.SPAWNER, XMaterial.COAL_BLOCK, XMaterial.BONE_BLOCK, XMaterial.WARPED_NYLIUM, XMaterial.WEATHERED_COPPER, XMaterial.WEATHERED_CUT_COPPER, XMaterial.MOSSY_COBBLESTONE, XMaterial.SMOKER, XMaterial.COBBLED_DEEPSLATE, XMaterial.SMOOTH_BASALT, XMaterial.STONE_BUTTON, XMaterial.NETHER_BRICKS, XMaterial.BRICKS, XMaterial.RED_NETHER_BRICKS, XMaterial.SMOOTH_STONE, XMaterial.ANDESITE, XMaterial.BASALT, XMaterial.TUFF, XMaterial.END_STONE, XMaterial.WAXED_OXIDIZED_COPPER, XMaterial.INFESTED_CHISELED_STONE_BRICKS, XMaterial.PRISMARINE_BRICKS, XMaterial.CRYING_OBSIDIAN, XMaterial.CRACKED_DEEPSLATE_TILES, XMaterial.INFESTED_STONE, XMaterial.IRON_TRAPDOOR, XMaterial.INFESTED_MOSSY_STONE_BRICKS, XMaterial.RESPAWN_ANCHOR, XMaterial.BLUE_ICE, XMaterial.POLISHED_DIORITE, XMaterial.NETHER_BRICK_FENCE, XMaterial.INFESTED_CRACKED_STONE_BRICKS, XMaterial.SANDSTONE, XMaterial.EXPOSED_COPPER, XMaterial.WAXED_WEATHERED_COPPER, XMaterial.CRACKED_DEEPSLATE_BRICKS, XMaterial.LARGE_AMETHYST_BUD, XMaterial.PISTON_HEAD, XMaterial.NETHERITE_BLOCK, XMaterial.PURPUR_PILLAR, XMaterial.GRANITE, XMaterial.STONECUTTER, XMaterial.BLAST_FURNACE, XMaterial.ENCHANTING_TABLE, XMaterial.LAPIS_BLOCK, XMaterial.PACKED_ICE, XMaterial.CRACKED_STONE_BRICKS, XMaterial.DEEPSLATE, XMaterial.CRIMSON_NYLIUM, XMaterial.STICKY_PISTON, XMaterial.DIAMOND_BLOCK, XMaterial.POINTED_DRIPSTONE}).inheritFrom(new XTag[]{XTag.TERRACOTTA, XTag.GLAZED_TERRACOTTA, XTag.WALLS, XTag.CORALS, XTag.SHULKER_BOXES, XTag.RAILS, XTag.DIAMOND_ORES, XTag.GOLD_ORES, XTag.IRON_ORES, XTag.EMERALD_ORES, XTag.COPPER_ORES, XTag.ANVIL, XTag.CONCRETE, XTag.NON_WOODEN_STAIRS, XTag.NON_WOODEN_SLABS, XTag.CAULDRONS})).build();
        MINEABLE_SHOVEL = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.FARMLAND, XMaterial.DIRT_PATH, XMaterial.SNOW, XMaterial.SNOW_BLOCK, XMaterial.RED_SAND, XMaterial.COARSE_DIRT, XMaterial.SOUL_SAND, XMaterial.GRAVEL, XMaterial.SAND, XMaterial.PODZOL, XMaterial.DIRT, XMaterial.CLAY, XMaterial.ROOTED_DIRT, XMaterial.MYCELIUM, XMaterial.SOUL_SOIL, XMaterial.GRASS_BLOCK}).inheritFrom(new XTag[]{XTag.CONCRETE_POWDER})).build();
        MINEABLE_HOE = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.FLOWERING_AZALEA_LEAVES, XMaterial.DARK_OAK_LEAVES, XMaterial.PALE_OAK_LEAVES, XMaterial.SHROOMLIGHT, XMaterial.BIRCH_LEAVES, XMaterial.DRIED_KELP_BLOCK, XMaterial.JUNGLE_LEAVES, XMaterial.OAK_LEAVES, XMaterial.MOSS_CARPET, XMaterial.WET_SPONGE, XMaterial.AZALEA_LEAVES, XMaterial.NETHER_WART_BLOCK, XMaterial.WARPED_WART_BLOCK, XMaterial.SPONGE, XMaterial.SPRUCE_LEAVES, XMaterial.SCULK_SENSOR, XMaterial.HAY_BLOCK, XMaterial.TARGET, XMaterial.ACACIA_LEAVES, XMaterial.MANGROVE_LEAVES, XMaterial.CHERRY_LEAVES, XMaterial.MOSS_BLOCK});
        LAVA_POOL_STONE_CANNOT_REPLACE = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.DARK_OAK_LEAVES, XMaterial.STRIPPED_DARK_OAK_WOOD, XMaterial.STRIPPED_PALE_OAK_WOOD, XMaterial.OAK_WOOD, XMaterial.CRIMSON_HYPHAE, XMaterial.JUNGLE_LEAVES, XMaterial.MANGROVE_LEAVES, XMaterial.CHERRY_LEAVES, XMaterial.DARK_OAK_WOOD, XMaterial.STRIPPED_ACACIA_LOG, XMaterial.DARK_OAK_LOG, XMaterial.STRIPPED_DARK_OAK_LOG, XMaterial.AZALEA_LEAVES, XMaterial.SPAWNER, XMaterial.JUNGLE_LOG, XMaterial.SPRUCE_LOG, XMaterial.MANGROVE_LOG, XMaterial.CHERRY_LOG, XMaterial.STRIPPED_CRIMSON_HYPHAE, XMaterial.SPRUCE_LEAVES, XMaterial.STRIPPED_BIRCH_LOG, XMaterial.PALE_OAK_WOOD, XMaterial.PALE_OAK_LOG, XMaterial.STRIPPED_PALE_OAK_LOG, XMaterial.ACACIA_LOG, XMaterial.STRIPPED_ACACIA_WOOD, XMaterial.CRIMSON_STEM, XMaterial.BIRCH_WOOD, XMaterial.STRIPPED_JUNGLE_WOOD, XMaterial.STRIPPED_MANGROVE_LOG, XMaterial.STRIPPED_CHERRY_LOG, XMaterial.WARPED_HYPHAE, XMaterial.CHEST, XMaterial.FLOWERING_AZALEA_LEAVES, XMaterial.STRIPPED_OAK_LOG, XMaterial.ACACIA_WOOD, XMaterial.BEDROCK, XMaterial.BIRCH_LEAVES, XMaterial.STRIPPED_CRIMSON_STEM, XMaterial.OAK_LEAVES, XMaterial.STRIPPED_BIRCH_WOOD, XMaterial.STRIPPED_MANGROVE_WOOD, XMaterial.STRIPPED_CHERRY_WOOD, XMaterial.STRIPPED_JUNGLE_LOG, XMaterial.WARPED_STEM, XMaterial.END_PORTAL_FRAME, XMaterial.SPRUCE_WOOD, XMaterial.STRIPPED_SPRUCE_LOG, XMaterial.STRIPPED_SPRUCE_WOOD, XMaterial.JUNGLE_WOOD, XMaterial.MANGROVE_WOOD, XMaterial.CHERRY_WOOD, XMaterial.STRIPPED_OAK_WOOD, XMaterial.STRIPPED_WARPED_STEM, XMaterial.OAK_LOG, XMaterial.ACACIA_LEAVES, XMaterial.STRIPPED_WARPED_HYPHAE, XMaterial.BIRCH_LOG});
        LEATHER_ARMOR_PIECES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.LEATHER_HELMET, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS});
        IRON_ARMOR_PIECES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.IRON_HELMET, XMaterial.IRON_CHESTPLATE, XMaterial.IRON_LEGGINGS, XMaterial.IRON_BOOTS});
        CHAINMAIL_ARMOR_PIECES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.CHAINMAIL_HELMET, XMaterial.CHAINMAIL_CHESTPLATE, XMaterial.CHAINMAIL_LEGGINGS, XMaterial.CHAINMAIL_BOOTS});
        GOLDEN_ARMOR_PIECES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.GOLDEN_HELMET, XMaterial.GOLDEN_CHESTPLATE, XMaterial.GOLDEN_LEGGINGS, XMaterial.GOLDEN_BOOTS});
        DIAMOND_ARMOR_PIECES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.DIAMOND_HELMET, XMaterial.DIAMOND_CHESTPLATE, XMaterial.DIAMOND_LEGGINGS, XMaterial.DIAMOND_BOOTS});
        NETHERITE_ARMOR_PIECES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.NETHERITE_HELMET, XMaterial.NETHERITE_CHESTPLATE, XMaterial.NETHERITE_LEGGINGS, XMaterial.NETHERITE_BOOTS});
        WOODEN_TOOLS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.WOODEN_PICKAXE, XMaterial.WOODEN_AXE, XMaterial.WOODEN_HOE, XMaterial.WOODEN_SHOVEL, XMaterial.WOODEN_SWORD});
        STONE_TOOLS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.STONE_PICKAXE, XMaterial.STONE_AXE, XMaterial.STONE_HOE, XMaterial.STONE_SHOVEL, XMaterial.STONE_SWORD});
        IRON_TOOLS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.IRON_PICKAXE, XMaterial.IRON_AXE, XMaterial.IRON_HOE, XMaterial.IRON_SHOVEL, XMaterial.IRON_SWORD});
        DIAMOND_TOOLS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.DIAMOND_PICKAXE, XMaterial.DIAMOND_AXE, XMaterial.DIAMOND_HOE, XMaterial.DIAMOND_SHOVEL, XMaterial.DIAMOND_SHOVEL});
        NETHERITE_TOOLS = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.NETHERITE_PICKAXE, XMaterial.NETHERITE_AXE, XMaterial.NETHERITE_HOE, XMaterial.NETHERITE_SHOVEL, XMaterial.NETHERITE_SHOVEL});
        ARMOR_PIECES = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.TURTLE_HELMET}).inheritFrom(new XTag[]{XTag.LEATHER_ARMOR_PIECES, XTag.CHAINMAIL_ARMOR_PIECES, XTag.IRON_ARMOR_PIECES, XTag.GOLDEN_ARMOR_PIECES, XTag.DIAMOND_ARMOR_PIECES, XTag.NETHERITE_ARMOR_PIECES})).build();
        SMITHING_TEMPLATES = TagBuilder.simple((XBase[])new XMaterial[]{XMaterial.NETHERITE_UPGRADE_SMITHING_TEMPLATE, XMaterial.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.WILD_ARMOR_TRIM_SMITHING_TEMPLATE});
        AZALEA_GROWS_ON = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.SNOW_BLOCK, XMaterial.POWDER_SNOW}).inheritFrom(new XTag[]{XTag.TERRACOTTA, XTag.SAND, XTag.DIRT})).build();
        AZALEA_ROOT_REPLACEABLE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.CLAY, XMaterial.GRAVEL}).inheritFrom(new XTag[]{XTag.AZALEA_GROWS_ON, XTag.CAVE_VINES, XTag.BASE_STONE_OVERWORLD})).build();
        BAMBOO_PLANTABLE_ON = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.GRAVEL, XMaterial.BAMBOO_SAPLING, XMaterial.BAMBOO}).inheritFrom(new XTag[]{XTag.DIRT, XTag.SAND})).build();
        BEE_GROWABLES = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.SWEET_BERRY_BUSH}).inheritFrom(new XTag[]{XTag.CROPS, XTag.CAVE_VINES})).build();
        BIG_DRIPLEAF_PLACEABLE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.CLAY, XMaterial.FARMLAND}).inheritFrom(new XTag[]{XTag.DIRT})).build();
        BUTTONS = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.STONE_BUTTON, XMaterial.POLISHED_BLACKSTONE_BUTTON}).inheritFrom(new XTag[]{XTag.WOODEN_BUTTONS})).build();
        DRIPSTONE_REPLACEABLE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.DIRT}).inheritFrom(new XTag[]{XTag.BASE_STONE_OVERWORLD})).build();
        ENDERMAN_HOLDABLE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.TNT, XMaterial.PUMPKIN, XMaterial.CARVED_PUMPKIN, XMaterial.MELON, XMaterial.CRIMSON_FUNGUS, XMaterial.WARPED_FUNGUS, XMaterial.WARPED_ROOTS, XMaterial.CRIMSON_ROOTS, XMaterial.RED_MUSHROOM, XMaterial.BROWN_MUSHROOM, XMaterial.CACTUS, XMaterial.GRAVEL, XMaterial.CLAY}).inheritFrom(new XTag[]{XTag.DIRT, XTag.NYLIUM, XTag.SAND, XTag.SMALL_FLOWERS})).build();
        FLOWERS = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.FLOWERING_AZALEA, XMaterial.FLOWERING_AZALEA_LEAVES}).inheritFrom(new XTag[]{XTag.SMALL_FLOWERS, XTag.TALL_FLOWERS})).build();
        GOATS_SPAWNABLE_ON = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.GRAVEL, XMaterial.STONE, XMaterial.PACKED_ICE}).inheritFrom(new XTag[]{XTag.SNOW})).build();
        GUARDED_BY_PIGLINS = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.GOLD_BLOCK, XMaterial.ENDER_CHEST, XMaterial.RAW_GOLD_BLOCK, XMaterial.GILDED_BLACKSTONE, XMaterial.CHEST, XMaterial.BARREL, XMaterial.TRAPPED_CHEST}).inheritFrom(new XTag[]{XTag.SHULKER_BOXES, XTag.GOLD_ORES})).build();
        ITEMS_MUSIC_DISCS = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.MUSIC_DISC_OTHERSIDE, XMaterial.MUSIC_DISC_PIGSTEP}).inheritFrom(new XTag[]{XTag.ITEMS_CREEPER_DROP_MUSIC_DISCS})).build();
        ITEMS_PIGLIN_LOVED = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.GOLD_BLOCK, XMaterial.RAW_GOLD, XMaterial.GLISTERING_MELON_SLICE, XMaterial.GOLDEN_HORSE_ARMOR, XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE, XMaterial.GOLDEN_SWORD, XMaterial.GOLDEN_AXE, XMaterial.BELL, XMaterial.ENCHANTED_GOLDEN_APPLE, XMaterial.RAW_GOLD_BLOCK, XMaterial.GILDED_BLACKSTONE, XMaterial.CLOCK, XMaterial.GOLDEN_CARROT, XMaterial.GOLDEN_APPLE, XMaterial.GOLDEN_SHOVEL, XMaterial.GOLDEN_PICKAXE, XMaterial.GOLDEN_HOE, XMaterial.GOLD_INGOT}).inheritFrom(new XTag[]{XTag.GOLD_ORES, XTag.GOLDEN_ARMOR_PIECES})).build();
        SIGNS = TagBuilder.simple(new XTag[]{XTag.WALL_SIGNS, XTag.STANDING_SIGNS});
        PRESSURE_PLATES = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE, XMaterial.HEAVY_WEIGHTED_PRESSURE_PLATE}).inheritFrom(new XTag[]{XTag.STONE_PRESSURE_PLATES, XTag.WOODEN_PRESSURE_PLATES})).build();
        DRAGON_IMMUNE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.IRON_BARS, XMaterial.OBSIDIAN, XMaterial.RESPAWN_ANCHOR, XMaterial.END_STONE, XMaterial.CRYING_OBSIDIAN}).inheritFrom(new XTag[]{XTag.WITHER_IMMUNE})).build();
        WALL_POST_OVERRIDE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.TORCH, XMaterial.TRIPWIRE, XMaterial.REDSTONE_TORCH, XMaterial.SOUL_TORCH}).inheritFrom(new XTag[]{XTag.SIGNS, XTag.BANNERS, XTag.PRESSURE_PLATES})).build();
        UNDERWATER_BONEMEALS = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.SEAGRASS}).inheritFrom(new XTag[]{XTag.CORALS, XTag.ALIVE_CORAL_WALL_FANS})).build();
        UNSTABLE_BOTTOM_CENTER = TagBuilder.simple(new XTag[]{XTag.FENCE_GATES});
        PREVENT_MOB_SPAWNING_INSIDE = TagBuilder.simple(new XTag[]{XTag.RAILS});
        OCCLUDES_VIBRATION_SIGNALS = TagBuilder.simple(new XTag[]{XTag.WOOL});
        LOGS_THAT_BURN = TagBuilder.simple(new XTag[]{XTag.ACACIA_LOGS, XTag.OAK_LOGS, XTag.DARK_OAK_LOGS, XTag.PALE_OAK_LOGS, XTag.SPRUCE_LOGS, XTag.JUNGLE_LOGS, XTag.BIRCH_LOGS, XTag.MANGROVE_LOGS, XTag.CHERRY_LOGS});
        LOGS = TagBuilder.simple(new XTag[]{XTag.LOGS_THAT_BURN, XTag.CRIMSON_STEMS, XTag.WARPED_STEMS});
        ITEMS_FURNACE_MATERIALS = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.COAL, XMaterial.CHARCOAL, XMaterial.COAL_BLOCK}).inheritFrom(new XTag[]{XTag.LOGS, XTag.PLANKS})).build();
        PARROTS_SPAWNABLE_ON = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.GRASS_BLOCK}).inheritFrom(new XTag[]{XTag.AIR, XTag.LEAVES, XTag.LOGS})).build();
        LUSH_GROUND_REPLACEABLE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.GRAVEL, XMaterial.SAND, XMaterial.CLAY}).inheritFrom(new XTag[]{XTag.CAVE_VINES, XTag.DIRT, XTag.BASE_STONE_OVERWORLD})).build();
        TRAPDOORS = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.IRON_TRAPDOOR}).inheritFrom(new XTag[]{XTag.WOODEN_TRAPDOORS})).build();
        MUSHROOM_GROW_BLOCK = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.PODZOL, XMaterial.MYCELIUM}).inheritFrom(new XTag[]{XTag.NYLIUM})).build();
        MOSS_REPLACEABLE = TagBuilder.simple(new XTag[]{XTag.CAVE_VINES, XTag.DIRT, XTag.BASE_STONE_OVERWORLD});
        ARMOR_ENCHANTS = TagBuilder.simple((XBase[])new XEnchantment[]{XEnchantment.BLAST_PROTECTION, XEnchantment.BINDING_CURSE, XEnchantment.VANISHING_CURSE, XEnchantment.FIRE_PROTECTION, XEnchantment.MENDING, XEnchantment.PROJECTILE_PROTECTION, XEnchantment.PROTECTION, XEnchantment.THORNS, XEnchantment.UNBREAKING});
        HELEMT_ENCHANTS = ((TagBuilder)TagBuilder.of((XBase[])new XEnchantment[]{XEnchantment.AQUA_AFFINITY, XEnchantment.RESPIRATION}).inheritFrom(new XTag[]{XTag.ARMOR_ENCHANTS})).build();
        CHESTPLATE_ENCHANTS = TagBuilder.simple(new XTag[]{XTag.ARMOR_ENCHANTS});
        LEGGINGS_ENCHANTS = TagBuilder.simple(new XTag[]{XTag.ARMOR_ENCHANTS});
        BOOTS_ENCHANTS = ((TagBuilder)TagBuilder.of((XBase[])new XEnchantment[]{XEnchantment.DEPTH_STRIDER, XEnchantment.FEATHER_FALLING, XEnchantment.FROST_WALKER}).inheritFrom(new XTag[]{XTag.ARMOR_ENCHANTS})).build();
        ELYTRA_ENCHANTS = TagBuilder.simple((XBase[])new XEnchantment[]{XEnchantment.BINDING_CURSE, XEnchantment.VANISHING_CURSE, XEnchantment.MENDING, XEnchantment.UNBREAKING});
        SWORD_ENCHANTS = TagBuilder.simple((XBase[])new XEnchantment[]{XEnchantment.BANE_OF_ARTHROPODS, XEnchantment.VANISHING_CURSE, XEnchantment.FIRE_ASPECT, XEnchantment.KNOCKBACK, XEnchantment.LOOTING, XEnchantment.MENDING, XEnchantment.SHARPNESS, XEnchantment.SMITE, XEnchantment.SWEEPING_EDGE, XEnchantment.UNBREAKING});
        AXE_ENCHANTS = TagBuilder.simple((XBase[])new XEnchantment[]{XEnchantment.BANE_OF_ARTHROPODS, XEnchantment.VANISHING_CURSE, XEnchantment.EFFICIENCY, XEnchantment.FORTUNE, XEnchantment.MENDING, XEnchantment.SHARPNESS, XEnchantment.SILK_TOUCH, XEnchantment.SMITE, XEnchantment.UNBREAKING});
        HOE_ENCHANTS = TagBuilder.simple((XBase[])new XEnchantment[]{XEnchantment.VANISHING_CURSE, XEnchantment.EFFICIENCY, XEnchantment.FORTUNE, XEnchantment.MENDING, XEnchantment.SILK_TOUCH, XEnchantment.UNBREAKING});
        PICKAXE_ENCHANTS = TagBuilder.simple((XBase[])new XEnchantment[]{XEnchantment.VANISHING_CURSE, XEnchantment.EFFICIENCY, XEnchantment.FORTUNE, XEnchantment.MENDING, XEnchantment.SILK_TOUCH, XEnchantment.UNBREAKING});
        SHOVEL_ENCHANTS = TagBuilder.simple((XBase[])new XEnchantment[]{XEnchantment.VANISHING_CURSE, XEnchantment.EFFICIENCY, XEnchantment.FORTUNE, XEnchantment.MENDING, XEnchantment.SILK_TOUCH, XEnchantment.UNBREAKING});
        SHEARS_ENCHANTS = TagBuilder.of((XBase[])new XEnchantment[]{XEnchantment.VANISHING_CURSE, XEnchantment.EFFICIENCY, XEnchantment.MENDING, XEnchantment.UNBREAKING}).build();
        BOW_ENCHANTS = TagBuilder.simple((XBase[])new XEnchantment[]{XEnchantment.VANISHING_CURSE, XEnchantment.FLAME, XEnchantment.INFINITY, XEnchantment.MENDING, XEnchantment.PUNCH, XEnchantment.UNBREAKING});
        CROSSBOW_ENCHANTS = TagBuilder.simple((XBase[])new XEnchantment[]{XEnchantment.VANISHING_CURSE, XEnchantment.MENDING, XEnchantment.MULTISHOT, XEnchantment.PIERCING, XEnchantment.QUICK_CHARGE, XEnchantment.UNBREAKING});
        MINEABLE_AXE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.COMPOSTER, XMaterial.COCOA, XMaterial.RED_MUSHROOM_BLOCK, XMaterial.CRAFTING_TABLE, XMaterial.TALL_GRASS, XMaterial.BIG_DRIPLEAF_STEM, XMaterial.RED_MUSHROOM, XMaterial.JUKEBOX, XMaterial.WARPED_FUNGUS, XMaterial.DEAD_BUSH, XMaterial.NOTE_BLOCK, XMaterial.CRIMSON_FUNGUS, XMaterial.MUSHROOM_STEM, XMaterial.CHORUS_PLANT, XMaterial.BEE_NEST, XMaterial.BROWN_MUSHROOM_BLOCK, XMaterial.JACK_O_LANTERN, XMaterial.FERN, XMaterial.NETHER_WART, XMaterial.CARTOGRAPHY_TABLE, XMaterial.CHEST, XMaterial.SWEET_BERRY_BUSH, XMaterial.BROWN_MUSHROOM, XMaterial.CARVED_PUMPKIN, XMaterial.SMITHING_TABLE, XMaterial.GLOW_LICHEN, XMaterial.SMALL_DRIPLEAF, XMaterial.LOOM, XMaterial.BEEHIVE, XMaterial.SHORT_GRASS, XMaterial.HANGING_ROOTS, XMaterial.CHORUS_FLOWER, XMaterial.ATTACHED_PUMPKIN_STEM, XMaterial.BIG_DRIPLEAF, XMaterial.DAYLIGHT_DETECTOR, XMaterial.SPORE_BLOSSOM, XMaterial.LILY_PAD, XMaterial.TRAPPED_CHEST, XMaterial.BARREL, XMaterial.LARGE_FERN, XMaterial.LECTERN, XMaterial.SUGAR_CANE, XMaterial.MELON, XMaterial.ATTACHED_MELON_STEM, XMaterial.PUMPKIN, XMaterial.BAMBOO, XMaterial.FLETCHING_TABLE, XMaterial.BOOKSHELF}).inheritFrom(new XTag[]{XTag.BANNERS, XTag.SIGNS, XTag.CAVE_VINES, XTag.CROPS, XTag.LOGS, XTag.WOODEN_STAIRS, XTag.WOODEN_SLABS, XTag.WOODEN_PRESSURE_PLATES, XTag.WOODEN_FENCES, XTag.WOODEN_FENCE_GATES, XTag.WOODEN_TRAPDOORS, XTag.WOODEN_DOORS, XTag.WOODEN_BUTTONS, XTag.PLANKS, XTag.SAPLINGS, XTag.CLIMBABLE, XTag.CAMPFIRES})).build();
        INVENTORY_NOT_DISPLAYABLE = ((TagBuilder)TagBuilder.of((XBase[])new XMaterial[]{XMaterial.BIG_DRIPLEAF_STEM, XMaterial.SWEET_BERRY_BUSH, XMaterial.KELP_PLANT, XMaterial.FROSTED_ICE, XMaterial.ATTACHED_MELON_STEM, XMaterial.ATTACHED_PUMPKIN_STEM, XMaterial.COCOA, XMaterial.MOVING_PISTON, XMaterial.PISTON_HEAD, XMaterial.PITCHER_CROP, XMaterial.POWDER_SNOW, XMaterial.REDSTONE_WIRE, XMaterial.TALL_SEAGRASS, XMaterial.TRIPWIRE, XMaterial.TORCHFLOWER_CROP, XMaterial.BUBBLE_COLUMN, XMaterial.TWISTING_VINES_PLANT, XMaterial.WEEPING_VINES_PLANT, XMaterial.BAMBOO_SAPLING}).inheritFrom(new XTag[]{XTag.AIR, XTag.CAVE_VINES, XTag.FILLED_CAULDRONS, XTag.FIRE, XTag.FLUID, XTag.PORTALS, XTag.WALL_SIGNS, XTag.WALL_HANGING_SIGNS, XTag.WALL_TORCHES, XTag.ALIVE_CORAL_WALL_FANS, XTag.DEAD_CORAL_WALL_FANS, XTag.WALL_HEADS, XTag.CANDLE_CAKES, XTag.WALL_BANNERS, super.without((XBase[])new XMaterial[]{XMaterial.FLOWER_POT}), super.without((XBase[])new XMaterial[]{XMaterial.WHEAT_SEEDS, XMaterial.WHEAT})})).build();
        TAGS = new HashMap(30);
        for (Field field : XTag.class.getDeclaredFields()) {
            try {
                if (field.getType() != XTag.class) continue;
                TAGS.put(field.getName(), (XTag)field.get(null));
            } catch (IllegalAccessException illegalAccessException) {
                new IllegalStateException("Failed to get XTag " + field, illegalAccessException).printStackTrace();
            }
        }
    }

    public static abstract class Matcher<T> {
        public abstract boolean matches(T var1);

        public static final class XTagMatcher<T extends XBase<?, ?>>
        extends Matcher<T> {
            public final XTag<T> matcher;

            public XTagMatcher(XTag<T> xTag) {
                this.matcher = xTag;
            }

            @Override
            public boolean matches(T t) {
                return this.matcher.isTagged(t);
            }
        }

        public static final class RegexMatcher<T>
        extends Matcher<T> {
            public final Pattern regex;

            public RegexMatcher(Pattern pattern) {
                this.regex = pattern;
            }

            @Override
            public boolean matches(T t) {
                String string = t instanceof Enum ? ((Enum)t).name() : t.toString();
                return this.regex.matcher(string).matches();
            }
        }

        public static final class TextMatcher<T>
        extends Matcher<T> {
            public final String text;
            public final boolean contains;

            public TextMatcher(String string, boolean bl) {
                this.text = string;
                this.contains = bl;
            }

            @Override
            public boolean matches(T t) {
                String string = t instanceof Enum ? ((Enum)t).name() : t.toString();
                return this.contains ? string.contains(this.text) : string.equals(this.text);
            }
        }

        public static final class Error
        extends RuntimeException {
            public final String matcher;

            public Error(String string, String string2) {
                super(string2);
                this.matcher = string;
            }

            public Error(String string, String string2, Throwable throwable) {
                super(string2, throwable);
                this.matcher = string;
            }
        }
    }

    private static final class TagBuilder<T extends XBase<?, ?>> {
        private final Set<T> values;

        private TagBuilder(Collection<T> collection) {
            if (collection.isEmpty()) {
                this.values = Collections.newSetFromMap(new IdentityHashMap());
            } else if (collection.iterator().next() instanceof Enum) {
                Iterator<T> iterator = collection.iterator();
                XBase xBase = (XBase)iterator.next();
                this.values = EnumSet.of((Enum)((Object)xBase));
                while (iterator.hasNext()) {
                    this.values.add((XBase)iterator.next());
                }
            } else {
                this.values = Collections.newSetFromMap(new IdentityHashMap(collection.size()));
                this.values.addAll(collection);
            }
        }

        @SafeVarargs
        private static <T extends XBase<?, ?>> XTag<T> simple(T ... TArray) {
            return TagBuilder.of(TArray).build();
        }

        @SafeVarargs
        private static <T extends XBase<?, ?>> XTag<T> simple(XTag<T> ... xTagArray) {
            return super.build();
        }

        @SafeVarargs
        private static <T extends XBase<?, ?>> TagBuilder<T> of(T ... TArray) {
            return new TagBuilder<T>(Arrays.asList(TArray));
        }

        @SafeVarargs
        private final TagBuilder<T> inheritFrom(@NotNull XTag<T> ... xTagArray) {
            for (XTag<T> xTag : xTagArray) {
                this.values.addAll(((XTag)xTag).values);
            }
            return this;
        }

        private XTag<T> build() {
            return new XTag(Collections.unmodifiableSet(this.values));
        }
    }
}

