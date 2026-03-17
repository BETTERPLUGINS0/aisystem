/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.Registry
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.ThrownPotion
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.potion.PotionType
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import me.zombie_striker.qav.util.xseries.base.XBase;
import me.zombie_striker.qav.util.xseries.base.XRegistry;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum XPotion implements XBase<XPotion, PotionEffectType>
{
    ABSORPTION("ABSORB"),
    BAD_OMEN("OMEN_BAD", "PILLAGER"),
    BLINDNESS("BLIND"),
    CONDUIT_POWER("CONDUIT", "POWER_CONDUIT"),
    DARKNESS(new String[0]),
    DOLPHINS_GRACE("DOLPHIN", "GRACE"),
    FIRE_RESISTANCE("FIRE_RESIST", "RESIST_FIRE", "FIRE_RESISTANCE"),
    GLOWING("GLOW", "SHINE", "SHINY"),
    HASTE("FAST_DIGGING", "SUPER_PICK", "DIGFAST", "DIG_SPEED", "QUICK_MINE", "SHARP"),
    HEALTH_BOOST("BOOST_HEALTH", "BOOST", "HP"),
    HERO_OF_THE_VILLAGE("HERO", "VILLAGE_HERO"),
    HUNGER("STARVE", "HUNGRY"),
    INFESTED(new String[0]),
    INSTANT_DAMAGE("INJURE", "DAMAGE", "HARMING", "INFLICT", "HARM"),
    INSTANT_HEALTH("HEALTH", "INSTA_HEAL", "INSTANT_HEAL", "INSTA_HEALTH", "HEAL", "HEALING"),
    INVISIBILITY("INVISIBLE", "VANISH", "INVIS", "DISAPPEAR", "HIDE"),
    JUMP_BOOST("LEAP", "LEAPING", "JUMP"),
    LEVITATION("LEVITATE"),
    LUCK("LUCKY"),
    MINING_FATIGUE("SLOW_DIGGING", "FATIGUE", "DULL", "DIGGING", "SLOW_DIG", "DIG_SLOW"),
    NAUSEA("CONFUSION", "SICKNESS", "SICK"),
    NIGHT_VISION("VISION", "VISION_NIGHT"),
    OOZING(new String[0]),
    POISON("VENOM"),
    RAID_OMEN(new String[0]),
    REGENERATION("REGEN"),
    RESISTANCE("DAMAGE_RESISTANCE", "ARMOR", "DMG_RESIST", "DMG_RESISTANCE"),
    SATURATION("FOOD"),
    SLOWNESS("SLOW", "SLUGGISH"),
    SLOW_FALLING("SLOW_FALL", "FALL_SLOW"),
    SPEED("SPRINT", "RUNFAST", "SWIFT", "SWIFTNESS", "FAST"),
    STRENGTH("INCREASE_DAMAGE", "BULL", "STRONG", "ATTACK"),
    TRIAL_OMEN(new String[0]),
    UNLUCK("UNLUCKY"),
    WATER_BREATHING("WATER_BREATH", "UNDERWATER_BREATHING", "UNDERWATER_BREATH", "AIR"),
    WEAKNESS("WEAK"),
    WEAVING(new String[0]),
    WIND_CHARGED(new String[0]),
    WITHER("DECAY");

    public static final XPotion[] VALUES;
    @Deprecated
    public static final Set<XPotion> DEBUFFS;
    private static final XPotion[] POTIONEFFECTTYPE_MAPPING;
    public static final XRegistry<XPotion, PotionEffectType> REGISTRY;
    private final PotionEffectType potionEffectType;
    private final PotionType potionType;

    private XPotion(String ... stringArray) {
        PotionEffectType potionEffectType = PotionEffectType.getByName((String)this.name());
        for (String illegalArgumentException : stringArray) {
            if (potionEffectType != null) continue;
            potionEffectType = PotionEffectType.getByName((String)illegalArgumentException);
        }
        if (this.name().equals("TURTLE_MASTER")) {
            potionEffectType = XPotion.findSlowness();
        }
        this.potionEffectType = potionEffectType;
        this.potionType = PotionType.getByEffect((PotionEffectType)this.potionEffectType);
        Data.REGISTRY.stdEnum(this, stringArray, this.potionEffectType);
        if (this.potionType != null) {
            String string2 = this.potionType.name();
            String string3 = "STRONG_" + string2;
            String string4 = "LONG_" + string2;
            Data.POTION_TYPE_MAPPING.put(this.potionType, this);
            try {
                Data.POTION_TYPE_MAPPING.put(PotionType.valueOf((String)string3), this);
                Data.REGISTRY.registerName(string3, this);
            } catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
            try {
                Data.POTION_TYPE_MAPPING.put(PotionType.valueOf((String)string4), this);
                Data.REGISTRY.registerName(string4, this);
            } catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
    }

    private static PotionEffectType findSlowness() {
        return Stream.of("SLOWNESS", "SLOW", "SLUGGISH").map(PotionEffectType::getByName).filter(Objects::nonNull).findFirst().orElseThrow(() -> new IllegalStateException("Cannot find slowness potion type"));
    }

    @Deprecated
    @NotNull
    public static Optional<XPotion> matchXPotion(@NotNull String string) {
        return XPotion.of(string);
    }

    @NotNull
    public static XPotion of(@NotNull PotionType potionType) {
        return (XPotion)Data.POTION_TYPE_MAPPING.get(potionType);
    }

    public static Optional<XPotion> of(@NotNull String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Cannot match XPotion of a null or empty potion effect type");
        }
        PotionEffectType potionEffectType = XPotion.fromId(string);
        if (potionEffectType != null) {
            Optional<XPotion> optional = REGISTRY.getByName(potionEffectType.getName());
            if (!optional.isPresent()) {
                throw new UnsupportedOperationException("Unsupported potion effect type ID: " + potionEffectType);
            }
            return optional;
        }
        return REGISTRY.getByName(string);
    }

    @Deprecated
    public static XPotion matchXPotion(@NotNull PotionType potionType) {
        return XPotion.of(potionType);
    }

    @Deprecated
    @NotNull
    public static XPotion matchXPotion(@NotNull PotionEffectType potionEffectType) {
        return XPotion.of(potionEffectType);
    }

    @NotNull
    public static XPotion of(@NotNull PotionEffectType potionEffectType) {
        Objects.requireNonNull(potionEffectType, "Cannot match XPotion of a null potion effect type");
        return REGISTRY.getByBukkitForm(potionEffectType);
    }

    @Nullable
    private static PotionEffectType fromId(@NotNull String string) {
        try {
            int n = Integer.parseInt(string);
            return PotionEffectType.getById((int)n);
        } catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    private static List<String> split(@NotNull String string, char c) {
        ArrayList<String> arrayList = new ArrayList<String>(5);
        boolean bl = false;
        boolean bl2 = false;
        int n = string.length();
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (string.charAt(i) == c) {
                if (bl) {
                    arrayList.add(string.substring(n2, i));
                    bl = false;
                    bl2 = true;
                }
                n2 = i + 1;
                continue;
            }
            bl2 = false;
            bl = true;
        }
        if (bl || bl2) {
            arrayList.add(string.substring(n2, n));
        }
        return arrayList;
    }

    @Nullable
    public static Effect parseEffect(@Nullable String string) {
        Optional<XPotion> optional;
        if (Strings.isNullOrEmpty(string) || string.equalsIgnoreCase("none")) {
            return null;
        }
        List<String> list = XPotion.split(string.replace(" ", ""), ',');
        if (list.isEmpty()) {
            list = XPotion.split(string, ' ');
        }
        double d = 100.0;
        int n = 0;
        if (list.size() > 2 && (n = list.get(2).indexOf(37)) != -1) {
            try {
                d = Double.parseDouble(list.get(2).substring(n + 1));
            } catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        if (!(optional = XPotion.of(list.get(0))).isPresent()) {
            return null;
        }
        PotionEffectType potionEffectType = optional.get().potionEffectType;
        if (potionEffectType == null) {
            return null;
        }
        int n2 = 2400;
        int n3 = 0;
        if (list.size() > 1) {
            n2 = XPotion.toInt(list.get(1), 1) * 20;
            if (list.size() > 2) {
                n3 = XPotion.toInt(n <= 0 ? list.get(2) : list.get(2).substring(0, n), 1) - 1;
            }
        }
        return new Effect(new PotionEffect(potionEffectType, n2, n3), d);
    }

    private static int toInt(String string, int n) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException numberFormatException) {
            return n;
        }
    }

    public static void addEffects(@NotNull LivingEntity livingEntity, @Nullable List<String> list) {
        Objects.requireNonNull(livingEntity, "Cannot add potion effects to null entity");
        for (Effect effect : XPotion.parseEffects(list)) {
            effect.apply(livingEntity);
        }
    }

    public static List<Effect> parseEffects(@Nullable List<String> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<Effect>();
        }
        ArrayList<Effect> arrayList = new ArrayList<Effect>(list.size());
        for (String string : list) {
            Effect effect = XPotion.parseEffect(string);
            if (effect == null) continue;
            arrayList.add(effect);
        }
        return arrayList;
    }

    @NotNull
    public static ThrownPotion throwPotion(@NotNull LivingEntity livingEntity, @Nullable Color color, @Nullable PotionEffect ... thrownPotion) {
        Objects.requireNonNull(livingEntity, "Cannot throw potion from null entity");
        ItemStack itemStack = Material.getMaterial((String)"SPLASH_POTION") == null ? new ItemStack(Material.POTION, 1, 16398) : new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta)itemStack.getItemMeta();
        potionMeta.setColor(color);
        if (thrownPotion != null) {
            for (ThrownPotion thrownPotion2 : thrownPotion) {
                potionMeta.addCustomEffect((PotionEffect)thrownPotion2, true);
            }
        }
        itemStack.setItemMeta((ItemMeta)potionMeta);
        ThrownPotion thrownPotion3 = (ThrownPotion)livingEntity.launchProjectile(ThrownPotion.class);
        thrownPotion3.setItem(itemStack);
        return thrownPotion3;
    }

    @NotNull
    public static ItemStack buildItemWithEffects(@NotNull Material material, @Nullable Color color, @Nullable PotionEffect ... potionEffectArray) {
        Objects.requireNonNull(material, "Cannot build an effected item with null type");
        if (!XPotion.canHaveEffects(material)) {
            throw new IllegalArgumentException("Cannot build item with " + material.name() + " potion type");
        }
        ItemStack itemStack = new ItemStack(material);
        PotionMeta potionMeta = (PotionMeta)itemStack.getItemMeta();
        potionMeta.setColor(color);
        potionMeta.setDisplayName(material == Material.POTION ? "Potion" : (material == Material.SPLASH_POTION ? "Splash Potion" : (material == Material.TIPPED_ARROW ? "Tipped Arrow" : "Lingering Potion")));
        if (potionEffectArray != null) {
            for (PotionEffect potionEffect : potionEffectArray) {
                potionMeta.addCustomEffect(potionEffect, true);
            }
        }
        itemStack.setItemMeta((ItemMeta)potionMeta);
        return itemStack;
    }

    public static boolean canHaveEffects(@Nullable Material material) {
        return material != null && (material.name().endsWith("POTION") || material.name().startsWith("TIPPED_ARROW"));
    }

    @Nullable
    public PotionEffectType getPotionEffectType() {
        return this.potionEffectType;
    }

    @Override
    public String[] getNames() {
        return new String[]{this.name()};
    }

    @Override
    @Nullable
    public PotionEffectType get() {
        return this.potionEffectType;
    }

    @Nullable
    public PotionType getPotionType() {
        return this.potionType;
    }

    @Nullable
    public PotionEffect buildPotionEffect(int n, int n2) {
        return this.potionEffectType == null ? null : new PotionEffect(this.potionEffectType, n, n2 - 1);
    }

    public String toString() {
        return this.friendlyName();
    }

    static {
        VALUES = XPotion.values();
        DEBUFFS = Collections.unmodifiableSet(EnumSet.of(BAD_OMEN, new XPotion[]{BLINDNESS, NAUSEA, INSTANT_DAMAGE, HUNGER, LEVITATION, POISON, SLOWNESS, MINING_FATIGUE, UNLUCK, WEAKNESS, WITHER}));
        POTIONEFFECTTYPE_MAPPING = new XPotion[VALUES.length + 1];
        for (XPotion xPotion : VALUES) {
            if (xPotion.potionEffectType == null) continue;
            XPotion.POTIONEFFECTTYPE_MAPPING[xPotion.potionEffectType.getId()] = xPotion;
        }
        REGISTRY = Data.REGISTRY;
    }

    private static final class Data {
        private static final Map<PotionType, XPotion> POTION_TYPE_MAPPING = new EnumMap<PotionType, XPotion>(PotionType.class);
        private static final XRegistry<XPotion, PotionEffectType> REGISTRY = new XRegistry<XPotion, PotionEffectType>(PotionEffectType.class, XPotion.class, () -> Registry.EFFECT, null, XPotion[]::new);

        private Data() {
        }
    }

    public static class Effect {
        private PotionEffect effect;
        private double chance;

        public Effect(PotionEffect potionEffect, double d) {
            this.effect = potionEffect;
            this.chance = d;
        }

        public XPotion getXPotion() {
            return XPotion.of(this.effect.getType());
        }

        public double getChance() {
            return this.chance;
        }

        public boolean hasChance() {
            return this.chance >= 100.0 || ThreadLocalRandom.current().nextDouble(0.0, 100.0) <= this.chance;
        }

        public void setChance(double d) {
            this.chance = d;
        }

        public void apply(LivingEntity livingEntity) {
            if (this.hasChance()) {
                livingEntity.addPotionEffect(this.effect);
            }
        }

        public PotionEffect getEffect() {
            return this.effect;
        }

        public void setEffect(PotionEffect potionEffect) {
            this.effect = potionEffect;
        }
    }
}

