/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Registry
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.enchantments.EnchantmentWrapper
 *  org.bukkit.entity.EntityType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.EnchantmentStorageMeta
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries;

import com.google.common.base.Enums;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import me.zombie_striker.qav.util.xseries.base.XModule;
import me.zombie_striker.qav.util.xseries.base.XRegistry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class XEnchantment
extends XModule<XEnchantment, Enchantment> {
    private static final boolean ISFLAT;
    private static final boolean IS_SUPER_FLAT;
    private static final boolean USES_WRAPPER;
    public static final XRegistry<XEnchantment, Enchantment> REGISTRY;
    public static final XEnchantment AQUA_AFFINITY;
    public static final XEnchantment BANE_OF_ARTHROPODS;
    public static final XEnchantment BINDING_CURSE;
    public static final XEnchantment BLAST_PROTECTION;
    public static final XEnchantment BREACH;
    public static final XEnchantment CHANNELING;
    public static final XEnchantment DENSITY;
    public static final XEnchantment DEPTH_STRIDER;
    public static final XEnchantment EFFICIENCY;
    public static final XEnchantment FEATHER_FALLING;
    public static final XEnchantment FIRE_ASPECT;
    public static final XEnchantment FIRE_PROTECTION;
    public static final XEnchantment FLAME;
    public static final XEnchantment FORTUNE;
    public static final XEnchantment FROST_WALKER;
    public static final XEnchantment IMPALING;
    public static final XEnchantment INFINITY;
    public static final XEnchantment KNOCKBACK;
    public static final XEnchantment LOOTING;
    public static final XEnchantment LOYALTY;
    public static final XEnchantment LUCK_OF_THE_SEA;
    public static final XEnchantment LURE;
    public static final XEnchantment MENDING;
    public static final XEnchantment MULTISHOT;
    public static final XEnchantment PIERCING;
    public static final XEnchantment POWER;
    public static final XEnchantment PROJECTILE_PROTECTION;
    public static final XEnchantment PROTECTION;
    public static final XEnchantment PUNCH;
    public static final XEnchantment QUICK_CHARGE;
    public static final XEnchantment RESPIRATION;
    public static final XEnchantment RIPTIDE;
    public static final XEnchantment SHARPNESS;
    public static final XEnchantment SILK_TOUCH;
    public static final XEnchantment SMITE;
    public static final XEnchantment SOUL_SPEED;
    public static final XEnchantment SWIFT_SNEAK;
    public static final XEnchantment THORNS;
    public static final XEnchantment UNBREAKING;
    public static final XEnchantment VANISHING_CURSE;
    public static final XEnchantment WIND_BURST;
    public static final XEnchantment SWEEPING_EDGE;
    @Deprecated
    public static final XEnchantment[] VALUES;
    @Deprecated
    public static final Set<EntityType> EFFECTIVE_SMITE_ENTITIES;
    @Deprecated
    public static final Set<EntityType> EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES;

    private XEnchantment(Enchantment enchantment, String[] stringArray) {
        super(enchantment, stringArray);
    }

    @NotNull
    public static XEnchantment of(@NotNull Enchantment enchantment) {
        return REGISTRY.getByBukkitForm(enchantment);
    }

    public static Optional<XEnchantment> of(@NotNull String string) {
        return REGISTRY.getByName(string);
    }

    @Deprecated
    @NotNull
    public static XEnchantment[] values() {
        return (XEnchantment[])REGISTRY.values();
    }

    @NotNull
    private static XEnchantment std(@NotNull String ... stringArray) {
        Enchantment enchantment;
        XEnchantment xEnchantment = REGISTRY.std((XEnchantment)stringArray);
        if (USES_WRAPPER && xEnchantment.isSupported() && (enchantment = (Enchantment)xEnchantment.get()) instanceof EnchantmentWrapper) {
            Enchantment enchantment2 = ((EnchantmentWrapper)enchantment).getEnchantment();
            REGISTRY.bukkitMapping().put(enchantment2, xEnchantment);
        }
        return xEnchantment;
    }

    @Deprecated
    private static Enchantment getBukkitEnchant(String string) {
        if (IS_SUPER_FLAT) {
            return (Enchantment)Registry.ENCHANTMENT.get(NamespacedKey.minecraft((String)string.toLowerCase(Locale.ENGLISH)));
        }
        if (ISFLAT) {
            return Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)string.toLowerCase(Locale.ENGLISH)));
        }
        return Enchantment.getByName((String)string);
    }

    @Deprecated
    public static boolean isSmiteEffectiveAgainst(@Nullable EntityType entityType) {
        return entityType != null && EFFECTIVE_SMITE_ENTITIES.contains(entityType);
    }

    @Deprecated
    public static boolean isArthropodsEffectiveAgainst(@Nullable EntityType entityType) {
        return entityType != null && EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES.contains(entityType);
    }

    @Deprecated
    @NotNull
    public static Optional<XEnchantment> matchXEnchantment(@NotNull String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Enchantment name cannot be null or empty");
        }
        return XEnchantment.of(string);
    }

    @Deprecated
    @NotNull
    public static XEnchantment matchXEnchantment(@NotNull Enchantment enchantment) {
        Objects.requireNonNull(enchantment, "Cannot parse XEnchantment of a null enchantment");
        return XEnchantment.of(enchantment);
    }

    @NotNull
    public ItemStack getBook(int n) {
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta)itemStack.getItemMeta();
        enchantmentStorageMeta.addStoredEnchant((Enchantment)this.get(), n, true);
        itemStack.setItemMeta((ItemMeta)enchantmentStorageMeta);
        return itemStack;
    }

    @Deprecated
    @Nullable
    public Enchantment getEnchant() {
        return (Enchantment)this.get();
    }

    private static /* synthetic */ String lambda$static$2(Field field, EnchantmentWrapper enchantmentWrapper) {
        return "No main mapping found for Enchantment." + field.getName() + " (" + enchantmentWrapper + ')';
    }

    static {
        EnumSet<EntityType> enumSet;
        Field field2;
        boolean bl;
        boolean bl2;
        boolean bl3 = false;
        try {
            Class<?> object4 = Class.forName("org.bukkit.NamespacedKey");
            Class<?> clazz = Class.forName("org.bukkit.enchantments.Enchantment");
            clazz.getDeclaredMethod("getByKey", object4);
            bl2 = true;
        } catch (ClassNotFoundException | NoSuchMethodException reflectiveOperationException) {
            bl2 = false;
        }
        try {
            Class.forName("org.bukkit.Registry");
            bl = true;
        } catch (ClassNotFoundException classNotFoundException) {
            bl = false;
        }
        for (Field field2 : Enchantment.class.getDeclaredFields()) {
            int n = field2.getModifiers();
            if (!Modifier.isPublic(n) || !Modifier.isStatic(n) || !Modifier.isFinal(n) || field2.getType() != Enchantment.class) continue;
            try {
                enumSet = field2.get(null);
                if (!(enumSet instanceof EnchantmentWrapper)) continue;
                bl3 = true;
            } catch (IllegalAccessException illegalAccessException) {
                throw new IllegalStateException("Cannot get enchantment field for " + field2, illegalAccessException);
            }
        }
        ISFLAT = bl2;
        IS_SUPER_FLAT = bl;
        USES_WRAPPER = bl3;
        REGISTRY = new XRegistry<XEnchantment, Enchantment>(Enchantment.class, XEnchantment.class, () -> Registry.ENCHANTMENT, XEnchantment::new, XEnchantment[]::new);
        AQUA_AFFINITY = XEnchantment.std("WATER_WORKER", "WATER_WORKER", "AQUA_AFFINITY", "WATER_MINE");
        BANE_OF_ARTHROPODS = XEnchantment.std("BANE_OF_ARTHROPODS", "DAMAGE_ARTHROPODS", "BANE_OF_ARTHROPOD", "ARTHROPOD");
        BINDING_CURSE = XEnchantment.std("BINDING_CURSE", "BIND_CURSE", "BINDING", "BIND");
        BLAST_PROTECTION = XEnchantment.std("PROTECTION_EXPLOSIONS", "BLAST_PROTECT", "EXPLOSIONS_PROTECTION", "EXPLOSION_PROTECTION", "BLAST_PROTECTION");
        BREACH = XEnchantment.std("BREACH");
        CHANNELING = XEnchantment.std("CHANNELING", "CHANNELLING", "CHANELLING", "CHANELING", "CHANNEL");
        DENSITY = XEnchantment.std("DENSITY");
        DEPTH_STRIDER = XEnchantment.std("DEPTH_STRIDER", "DEPTH", "STRIDER");
        EFFICIENCY = XEnchantment.std("EFFICIENCY", "DIG_SPEED", "MINE_SPEED", "CUT_SPEED");
        FEATHER_FALLING = XEnchantment.std("PROTECTION_FALL", "FEATHER_FALL", "FALL_PROTECTION", "FEATHER_FALLING");
        FIRE_ASPECT = XEnchantment.std("FIRE_ASPECT", "FIRE", "MELEE_FIRE", "MELEE_FLAME");
        FIRE_PROTECTION = XEnchantment.std("PROTECTION_FIRE", "FIRE_PROT", "FIRE_PROTECT", "FIRE_PROTECTION", "FLAME_PROTECTION", "FLAME_PROTECT");
        FLAME = XEnchantment.std("FLAME", "ARROW_FIRE", "FLAME_ARROW", "FIRE_ARROW");
        FORTUNE = XEnchantment.std("FORTUNE", "LOOT_BONUS_BLOCKS", "BLOCKS_LOOT_BONUS");
        FROST_WALKER = XEnchantment.std("FROST_WALKER", "FROST", "WALKER");
        IMPALING = XEnchantment.std("IMPALING", "IMPALE", "OCEAN_DAMAGE");
        INFINITY = XEnchantment.std("INFINITY", "ARROW_INFINITE", "INFINITE_ARROWS", "INFINITE", "UNLIMITED_ARROWS");
        KNOCKBACK = XEnchantment.std("KNOCKBACK");
        LOOTING = XEnchantment.std("LOOTING", "LOOT_BONUS_MOBS", "MOB_LOOT", "MOBS_LOOT_BONUS");
        LOYALTY = XEnchantment.std("LOYALTY", "LOYAL", "RETURN");
        LUCK_OF_THE_SEA = XEnchantment.std("LUCK_OF_THE_SEA", "LUCK", "LUCK_OF_SEA", "LUCK_OF_SEAS", "ROD_LUCK");
        LURE = XEnchantment.std("LURE", "ROD_LURE");
        MENDING = XEnchantment.std("MENDING");
        MULTISHOT = XEnchantment.std("MULTISHOT", "TRIPLE_SHOT");
        PIERCING = XEnchantment.std("PIERCING");
        POWER = XEnchantment.std("POWER", "ARROW_DAMAGE", "ARROW_POWER");
        PROJECTILE_PROTECTION = XEnchantment.std("PROTECTION_PROJECTILE", "PROJECTILE_PROTECTION");
        PROTECTION = XEnchantment.std("PROTECTION", "PROTECTION_ENVIRONMENTAL", "PROTECT");
        PUNCH = XEnchantment.std("PUNCH", "ARROW_KNOCKBACK", "ARROW_PUNCH");
        QUICK_CHARGE = XEnchantment.std("QUICK_CHARGE", "QUICKCHARGE", "QUICK_DRAW", "FAST_CHARGE", "FAST_DRAW");
        RESPIRATION = XEnchantment.std("RESPIRATION", "OXYGEN", "BREATH", "BREATHING");
        RIPTIDE = XEnchantment.std("RIPTIDE", "RIP", "TIDE", "LAUNCH");
        SHARPNESS = XEnchantment.std("SHARPNESS", "DAMAGE_ALL", "ALL_DAMAGE", "ALL_DMG", "SHARP");
        SILK_TOUCH = XEnchantment.std("SILK_TOUCH", "SOFT_TOUCH");
        SMITE = XEnchantment.std("SMITE", "DAMAGE_UNDEAD", "UNDEAD_DAMAGE");
        SOUL_SPEED = XEnchantment.std("SOUL_SPEED", "SPEED_SOUL", "SOUL_RUNNER");
        SWIFT_SNEAK = XEnchantment.std("SWIFT_SNEAK", "SNEAK_SWIFT");
        THORNS = XEnchantment.std("THORNS", "HIGHCRIT", "THORN", "HIGHERCRIT");
        UNBREAKING = XEnchantment.std("UNBREAKING", "DURABILITY", "DURA");
        VANISHING_CURSE = XEnchantment.std("VANISHING_CURSE", "VANISH_CURSE", "VANISHING", "VANISH");
        WIND_BURST = XEnchantment.std("WIND_BURST");
        SWEEPING_EDGE = XEnchantment.std("SWEEPING", "SWEEPING_EDGE", "SWEEP_EDGE");
        VALUES = XEnchantment.values();
        Field[] fieldArray = Enums.getIfPresent(EntityType.class, "BEE").orNull();
        EntityType entityType = Enums.getIfPresent(EntityType.class, "PHANTOM").orNull();
        EntityType entityType2 = Enums.getIfPresent(EntityType.class, "DROWNED").orNull();
        EntityType entityType3 = Enums.getIfPresent(EntityType.class, "WITHER_SKELETON").orNull();
        EntityType entityType4 = Enums.getIfPresent(EntityType.class, "SKELETON_HORSE").orNull();
        Object object = Enums.getIfPresent(EntityType.class, "STRAY").orNull();
        field2 = Enums.getIfPresent(EntityType.class, "HUSK").orNull();
        Object object2 = EnumSet.of(EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SILVERFISH, EntityType.ENDERMITE);
        if (fieldArray != null) {
            object2.add((EntityType)fieldArray);
        }
        EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES = Collections.unmodifiableSet(object2);
        enumSet = EnumSet.of(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.WITHER);
        if (entityType != null) {
            enumSet.add(entityType);
        }
        if (entityType2 != null) {
            enumSet.add(entityType2);
        }
        if (entityType3 != null) {
            enumSet.add(entityType3);
        }
        if (entityType4 != null) {
            enumSet.add(entityType4);
        }
        if (object != null) {
            enumSet.add((EntityType)object);
        }
        if (field2 != null) {
            enumSet.add((EntityType)field2);
        }
        EFFECTIVE_SMITE_ENTITIES = Collections.unmodifiableSet(enumSet);
        if (USES_WRAPPER) {
            for (Field field3 : Enchantment.class.getDeclaredFields()) {
                int n = field3.getModifiers();
                if (!Modifier.isPublic(n) || !Modifier.isStatic(n) || !Modifier.isFinal(n) || field3.getType() != Enchantment.class) continue;
                try {
                    object = field3.get(null);
                    if (!(object instanceof EnchantmentWrapper)) continue;
                    field2 = (EnchantmentWrapper)object;
                    object2 = REGISTRY.bukkitMapping().get(field2.getEnchantment());
                    Objects.requireNonNull(object2, () -> XEnchantment.lambda$static$2(field3, (EnchantmentWrapper)field2));
                    REGISTRY.bukkitMapping().put((Enchantment)field2, (XEnchantment)object2);
                } catch (IllegalAccessException illegalAccessException) {
                    throw new IllegalStateException("Cannot get direct enchantment field for " + field3, illegalAccessException);
                }
            }
        }
    }
}

