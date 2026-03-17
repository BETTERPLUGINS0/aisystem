/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Location
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Registry
 *  org.bukkit.TreeSpecies
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.boss.BarColor
 *  org.bukkit.boss.BarFlag
 *  org.bukkit.boss.BarStyle
 *  org.bukkit.boss.BossBar
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.AbstractHorse
 *  org.bukkit.entity.Ageable
 *  org.bukkit.entity.Animals
 *  org.bukkit.entity.Axolotl
 *  org.bukkit.entity.Axolotl$Variant
 *  org.bukkit.entity.Bat
 *  org.bukkit.entity.Bee
 *  org.bukkit.entity.Boat
 *  org.bukkit.entity.Boss
 *  org.bukkit.entity.Cat
 *  org.bukkit.entity.Cat$Type
 *  org.bukkit.entity.ChestedHorse
 *  org.bukkit.entity.Creeper
 *  org.bukkit.entity.EnderCrystal
 *  org.bukkit.entity.EnderDragon
 *  org.bukkit.entity.EnderDragon$Phase
 *  org.bukkit.entity.EnderSignal
 *  org.bukkit.entity.Enderman
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.ExperienceOrb
 *  org.bukkit.entity.Explosive
 *  org.bukkit.entity.Fox
 *  org.bukkit.entity.Fox$Type
 *  org.bukkit.entity.Frog
 *  org.bukkit.entity.Frog$Variant
 *  org.bukkit.entity.GlowSquid
 *  org.bukkit.entity.Goat
 *  org.bukkit.entity.Hoglin
 *  org.bukkit.entity.Husk
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Llama
 *  org.bukkit.entity.Llama$Color
 *  org.bukkit.entity.Mob
 *  org.bukkit.entity.MushroomCow
 *  org.bukkit.entity.MushroomCow$Variant
 *  org.bukkit.entity.Panda
 *  org.bukkit.entity.Panda$Gene
 *  org.bukkit.entity.Parrot
 *  org.bukkit.entity.Parrot$Variant
 *  org.bukkit.entity.Phantom
 *  org.bukkit.entity.Piglin
 *  org.bukkit.entity.PufferFish
 *  org.bukkit.entity.Rabbit
 *  org.bukkit.entity.Rabbit$Type
 *  org.bukkit.entity.Raider
 *  org.bukkit.entity.Sheep
 *  org.bukkit.entity.Sittable
 *  org.bukkit.entity.Spellcaster
 *  org.bukkit.entity.Spellcaster$Spell
 *  org.bukkit.entity.Strider
 *  org.bukkit.entity.Tameable
 *  org.bukkit.entity.TropicalFish
 *  org.bukkit.entity.TropicalFish$Pattern
 *  org.bukkit.entity.Vehicle
 *  org.bukkit.entity.Vex
 *  org.bukkit.entity.Villager
 *  org.bukkit.entity.Wolf
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.loot.Lootable
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries;

import com.google.common.base.Enums;
import com.google.common.base.Strings;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XAttribute;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XItemStack;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XPotion;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.FieldMemberHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.MethodMemberHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.ClassHandle;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.TreeSpecies;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Cat;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Mob;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Strider;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class XEntity {
    public static final Set<EntityType> UNDEAD;
    private static final Object REGISTRY_CAT_VARIANT;
    private static Object REGISTRY_DEFAULT_CAT_VARIANT;
    private static final Map<Class<?>, BiConsumer<Entity, ConfigurationSection>> MAPPING;
    private static final boolean SUPPORTS_Villager_setVillagerLevel;
    private static final boolean SUPPORTS_Villager_setVillagerExperience;
    private static final boolean SUPPORTS_Villager_setVillagerType;

    private static <T extends Entity> void register(Class<T> clazz, BiConsumer<T, ConfigurationSection> biConsumer) {
        MAPPING.put(clazz, (BiConsumer)XEntity.cast(biConsumer));
    }

    private static void mapObjectToConfig(Class<? extends Entity> clazz) {
        ArrayList<MappedConfigObject> arrayList = new ArrayList<MappedConfigObject>();
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        for (Method method : clazz.getDeclaredMethods()) {
            MethodHandle methodHandle;
            String string = method.getName();
            if (!string.startsWith("set")) continue;
            String string2 = string.substring(3).replaceAll("[A-Z]", "-");
            if (string2.startsWith("-")) {
                string2 = string.charAt(3) + string2.substring(1);
            }
            try {
                methodHandle = lookup.unreflect(method);
            } catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
            arrayList.add(new MappedConfigObject(string2, methodHandle, null));
        }
    }

    private XEntity() {
    }

    private static Object supportsRegistry(String string) {
        try {
            Object object = XReflection.ofMinecraft().inPackage("org.bukkit").named("Registry").reflect();
            return ((FieldMemberHandle)XReflection.of(object).field().asStatic().getter().named(string).returns((Class)object)).reflect().invoke();
        } catch (Throwable throwable) {
            return null;
        }
    }

    private static <T> T getRegistryOrEnum(Class<T> clazz, Object object, String string, Object object2) {
        if (Strings.isNullOrEmpty(string)) {
            return (T)object2;
        }
        T t = object != null ? XEntity.cast(((Registry)object).get(XEntity.fromConfig(string))) : XEntity.cast(Enums.getIfPresent((Class)XEntity.cast(clazz), string.toUpperCase(Locale.ENGLISH)).orNull());
        if (t == null) {
            return (T)object2;
        }
        return t;
    }

    private static <T> T cast(Object object) {
        return (T)object;
    }

    private static NamespacedKey fromConfig(String string) {
        NamespacedKey namespacedKey = !string.contains(":") ? NamespacedKey.minecraft((String)string.toLowerCase(Locale.ENGLISH)) : NamespacedKey.fromString((String)string.toLowerCase(Locale.ENGLISH));
        return Objects.requireNonNull(namespacedKey, () -> "Invalid namespace key: " + string);
    }

    @NotNull
    private static Cat.Type getCatVariant(@Nullable String string) {
        if (REGISTRY_DEFAULT_CAT_VARIANT == null) {
            REGISTRY_DEFAULT_CAT_VARIANT = XEntity.getRegistryOrEnum(Cat.Type.class, REGISTRY_CAT_VARIANT, "TABBY", null);
        }
        return XEntity.getRegistryOrEnum(Cat.Type.class, REGISTRY_CAT_VARIANT, string, REGISTRY_DEFAULT_CAT_VARIANT);
    }

    public static boolean isUndead(@Nullable EntityType entityType) {
        return entityType != null && UNDEAD.contains(entityType);
    }

    @Nullable
    public static Entity spawn(@NotNull Location location, @NotNull ConfigurationSection configurationSection) {
        Objects.requireNonNull(location, "Cannot spawn entity at a null location.");
        Objects.requireNonNull(configurationSection, "Cannot spawn entity from a null configuration section");
        String string = configurationSection.getString("type");
        if (string == null) {
            return null;
        }
        EntityType entityType = Enums.getIfPresent(EntityType.class, string.toUpperCase(Locale.ENGLISH)).or(EntityType.ZOMBIE);
        return XEntity.edit(location.getWorld().spawnEntity(location, entityType), configurationSection);
    }

    private static void map(Class<?> clazz, Entity entity, ConfigurationSection configurationSection) {
        Class<?> clazz2;
        if (clazz == Entity.class) {
            return;
        }
        BiConsumer<Entity, ConfigurationSection> biConsumer = MAPPING.get(clazz);
        if (biConsumer != null) {
            biConsumer.accept(entity, configurationSection);
        }
        if ((clazz2 = clazz.getSuperclass()) != null) {
            XEntity.map(clazz2, entity, configurationSection);
        }
        for (Class<?> clazz3 : clazz.getInterfaces()) {
            XEntity.map(clazz3, entity, configurationSection);
        }
    }

    @NotNull
    public static Entity edit(@NotNull Entity entity, @NotNull ConfigurationSection configurationSection) {
        Object object;
        Lootable lootable;
        int n;
        Objects.requireNonNull(entity, "Cannot edit properties of a null entity");
        Objects.requireNonNull(configurationSection, "Cannot edit an entity from a null configuration section");
        String string = configurationSection.getString("name");
        if (string != null) {
            entity.setCustomName(ChatColor.translateAlternateColorCodes((char)'&', (String)string));
            entity.setCustomNameVisible(true);
        }
        if (configurationSection.isSet("glowing")) {
            entity.setGlowing(configurationSection.getBoolean("glowing"));
        }
        if (configurationSection.isSet("gravity")) {
            entity.setGravity(configurationSection.getBoolean("gravity"));
        }
        if (configurationSection.isSet("silent")) {
            entity.setSilent(configurationSection.getBoolean("silent"));
        }
        entity.setFireTicks(configurationSection.getInt("fire-ticks"));
        entity.setFallDistance((float)configurationSection.getInt("fall-distance"));
        if (configurationSection.isSet("invulnerable")) {
            entity.setInvulnerable(configurationSection.getBoolean("invulnerable"));
        }
        if ((n = configurationSection.getInt("ticks-lived")) > 0) {
            entity.setTicksLived(n);
        }
        if (configurationSection.isSet("portal-cooldown")) {
            entity.setPortalCooldown(configurationSection.getInt("portal-cooldown", -1));
        }
        if (XMaterial.supports(13)) {
            if (entity instanceof Lootable) {
                lootable = (Lootable)entity;
                long l = configurationSection.getLong("seed");
                if (l != 0L) {
                    lootable.setSeed(l);
                }
            }
            if (entity instanceof Boss) {
                lootable = (Boss)entity;
                ConfigurationSection configurationSection2 = configurationSection.getConfigurationSection("bossbar");
                if (configurationSection2 != null) {
                    object = lootable.getBossBar();
                    XEntity.editBossBar((BossBar)object, configurationSection2);
                }
            }
        }
        if (entity instanceof Vehicle && entity instanceof Boat) {
            lootable = (Boat)entity;
            String string2 = configurationSection.getString("tree-species");
            if (string2 != null && ((com.google.common.base.Optional)(object = Enums.getIfPresent(TreeSpecies.class, string2))).isPresent()) {
                lootable.setWoodType((TreeSpecies)((com.google.common.base.Optional)object).get());
            }
        }
        if (entity instanceof LivingEntity) {
            ConfigurationSection configurationSection3;
            ConfigurationSection configurationSection4;
            Object object22;
            Object object3;
            int n2;
            lootable = (LivingEntity)entity;
            if (configurationSection.isSet("health")) {
                double d = configurationSection.getDouble("health");
                lootable.getAttribute((Attribute)XAttribute.MAX_HEALTH.get()).setBaseValue(d);
                lootable.setHealth(d);
            }
            if (XMaterial.supports(14)) {
                lootable.setAbsorptionAmount((double)configurationSection.getInt("absorption"));
            }
            if (configurationSection.isSet("AI")) {
                lootable.setAI(configurationSection.getBoolean("AI"));
            }
            if (configurationSection.isSet("can-pickup-items")) {
                lootable.setCanPickupItems(configurationSection.getBoolean("can-pickup-items"));
            }
            if (configurationSection.isSet("collidable")) {
                lootable.setCollidable(configurationSection.getBoolean("collidable"));
            }
            if (configurationSection.isSet("gliding")) {
                lootable.setGliding(configurationSection.getBoolean("gliding"));
            }
            if (configurationSection.isSet("remove-when-far-away")) {
                lootable.setRemoveWhenFarAway(configurationSection.getBoolean("remove-when-far-away"));
            }
            if (XMaterial.supports(13) && configurationSection.isSet("swimming")) {
                lootable.setSwimming(configurationSection.getBoolean("swimming"));
            }
            if (configurationSection.isSet("max-air")) {
                lootable.setMaximumAir(configurationSection.getInt("max-air"));
            }
            if (configurationSection.isSet("no-damage-ticks")) {
                lootable.setNoDamageTicks(configurationSection.getInt("no-damage-ticks"));
            }
            if (configurationSection.isSet("remaining-air")) {
                lootable.setRemainingAir(configurationSection.getInt("remaining-air"));
            }
            XPotion.addEffects((LivingEntity)lootable, configurationSection.getStringList("effects"));
            ConfigurationSection configurationSection5 = configurationSection.getConfigurationSection("equipment");
            if (configurationSection5 != null) {
                ConfigurationSection configurationSection6;
                Object object4;
                object = lootable.getEquipment();
                n2 = entity instanceof Mob;
                object3 = configurationSection5.getConfigurationSection("helmet");
                if (object3 != null) {
                    object.setHelmet(XItemStack.deserialize(object3.getConfigurationSection("item")));
                    if (n2 != 0) {
                        object.setHelmetDropChance((float)object3.getInt("drop-chance"));
                    }
                }
                if ((object4 = configurationSection5.getConfigurationSection("chestplate")) != null) {
                    object.setChestplate(XItemStack.deserialize(object4.getConfigurationSection("item")));
                    if (n2 != 0) {
                        object.setChestplateDropChance((float)object4.getInt("drop-chance"));
                    }
                }
                if ((object22 = configurationSection5.getConfigurationSection("leggings")) != null) {
                    object.setLeggings(XItemStack.deserialize(object22.getConfigurationSection("item")));
                    if (n2 != 0) {
                        object.setLeggingsDropChance((float)object22.getInt("drop-chance"));
                    }
                }
                if ((configurationSection4 = configurationSection5.getConfigurationSection("boots")) != null) {
                    object.setBoots(XItemStack.deserialize(configurationSection4.getConfigurationSection("item")));
                    if (n2 != 0) {
                        object.setBootsDropChance((float)configurationSection4.getInt("drop-chance"));
                    }
                }
                if ((configurationSection6 = configurationSection5.getConfigurationSection("main-hand")) != null) {
                    object.setItemInMainHand(XItemStack.deserialize(configurationSection6.getConfigurationSection("item")));
                    if (n2 != 0) {
                        object.setItemInMainHandDropChance((float)configurationSection6.getInt("drop-chance"));
                    }
                }
                if ((configurationSection3 = configurationSection5.getConfigurationSection("off-hand")) != null) {
                    object.setItemInOffHand(XItemStack.deserialize(configurationSection3.getConfigurationSection("item")));
                    if (n2 != 0) {
                        object.setItemInOffHandDropChance((float)configurationSection3.getInt("drop-chance"));
                    }
                }
            }
            if (lootable instanceof Ageable) {
                object = (Ageable)lootable;
                if (configurationSection.isSet("breed")) {
                    object.setBreed(configurationSection.getBoolean("breed"));
                }
                if (configurationSection.isSet("baby")) {
                    if (configurationSection.getBoolean("baby")) {
                        object.setBaby();
                    } else {
                        object.setAdult();
                    }
                }
                if ((n2 = configurationSection.getInt("age", 0)) > 0) {
                    object.setAge(n2);
                }
                if (configurationSection.isSet("age-lock")) {
                    object.setAgeLock(configurationSection.getBoolean("age-lock"));
                }
                if (lootable instanceof Animals) {
                    object3 = (Animals)lootable;
                    int n3 = configurationSection.getInt("love-mode");
                    if (n3 != 0) {
                        object3.setLoveModeTicks(n3);
                    }
                    if (lootable instanceof Tameable) {
                        object22 = (Tameable)lootable;
                        object22.setTamed(configurationSection.getBoolean("tamed"));
                    }
                }
            }
            if (lootable instanceof Sittable) {
                object = (Sittable)lootable;
                object.setSitting(configurationSection.getBoolean("sitting"));
            }
            if (lootable instanceof Spellcaster) {
                object = (Spellcaster)lootable;
                String string3 = configurationSection.getString("spell");
                if (string3 != null) {
                    object.setSpell(Enums.getIfPresent(Spellcaster.Spell.class, string3).or(Spellcaster.Spell.NONE));
                }
            }
            if (lootable instanceof AbstractHorse) {
                ConfigurationSection configurationSection7;
                object = (AbstractHorse)lootable;
                if (configurationSection.isSet("domestication")) {
                    object.setDomestication(configurationSection.getInt("domestication"));
                }
                if (configurationSection.isSet("jump-strength")) {
                    object.setJumpStrength(configurationSection.getDouble("jump-strength"));
                }
                if (configurationSection.isSet("max-domestication")) {
                    object.setMaxDomestication(configurationSection.getInt("max-domestication"));
                }
                if ((configurationSection7 = configurationSection.getConfigurationSection("items")) != null) {
                    object3 = object.getInventory();
                    for (Object object22 : configurationSection7.getKeys(false)) {
                        configurationSection4 = configurationSection7.getConfigurationSection((String)object22);
                        int n4 = configurationSection4.getInt("slot", -1);
                        if (n4 == -1 || (configurationSection3 = XItemStack.deserialize(configurationSection4)) == null) continue;
                        object3.setItem(n4, (ItemStack)configurationSection3);
                    }
                }
                if (lootable instanceof ChestedHorse) {
                    object3 = (ChestedHorse)lootable;
                    boolean bl = configurationSection.getBoolean("has-chest");
                    if (bl) {
                        object3.setCarryingChest(true);
                    }
                }
            }
            XEntity.map(entity.getClass(), entity, configurationSection);
            if (lootable instanceof Villager) {
                object = (Villager)lootable;
                if (SUPPORTS_Villager_setVillagerLevel) {
                    object.setVillagerLevel(configurationSection.getInt("level"));
                }
                if (SUPPORTS_Villager_setVillagerExperience) {
                    object.setVillagerExperience(configurationSection.getInt("xp"));
                }
            } else if (lootable instanceof Enderman) {
                ItemStack itemStack;
                object = (Enderman)lootable;
                String string4 = configurationSection.getString("carrying");
                if (string4 != null && ((Optional)(object3 = XMaterial.matchXMaterial(string4))).isPresent() && (itemStack = ((XMaterial)((Optional)object3).get()).parseItem()) != null) {
                    object.setCarriedMaterial(itemStack.getData());
                }
            } else if (lootable instanceof Sheep) {
                object = (Sheep)lootable;
                boolean bl = configurationSection.getBoolean("sheared");
                if (bl) {
                    object.setSheared(true);
                }
            } else if (lootable instanceof Rabbit) {
                object = (Rabbit)lootable;
                object.setRabbitType(Enums.getIfPresent(Rabbit.Type.class, configurationSection.getString("color")).or(Rabbit.Type.WHITE));
            } else if (lootable instanceof Bat) {
                object = (Bat)lootable;
                if (!configurationSection.getBoolean("awake")) {
                    object.setAwake(false);
                }
            } else if (lootable instanceof Wolf) {
                object = (Wolf)lootable;
                object.setAngry(configurationSection.getBoolean("angry"));
                object.setCollarColor(Enums.getIfPresent(DyeColor.class, configurationSection.getString("color")).or(DyeColor.GREEN));
            } else if (lootable instanceof Creeper) {
                object = (Creeper)lootable;
                object.setExplosionRadius(configurationSection.getInt("explosion-radius"));
                object.setMaxFuseTicks(configurationSection.getInt("max-fuse-ticks"));
                object.setPowered(configurationSection.getBoolean("powered"));
            } else if (XMaterial.supports(10) && XMaterial.supports(11)) {
                if (lootable instanceof Llama) {
                    com.google.common.base.Optional<Llama.Color> optional;
                    object = (Llama)lootable;
                    if (configurationSection.isSet("strength")) {
                        object.setStrength(configurationSection.getInt("strength"));
                    }
                    if ((optional = Enums.getIfPresent(Llama.Color.class, configurationSection.getString("color"))).isPresent()) {
                        object.setColor(optional.get());
                    }
                } else if (XMaterial.supports(12)) {
                    if (lootable instanceof Parrot) {
                        object = (Parrot)lootable;
                        object.setVariant(Enums.getIfPresent(Parrot.Variant.class, configurationSection.getString("color")).or(Parrot.Variant.RED));
                    }
                    if (XMaterial.supports(13)) {
                        XEntity.thirteen(entity, configurationSection);
                    }
                    if (XMaterial.supports(14)) {
                        XEntity.fourteen(entity, configurationSection);
                    }
                    if (XMaterial.supports(15)) {
                        XEntity.fifteen(entity, configurationSection);
                    }
                    if (XMaterial.supports(16)) {
                        XEntity.sixteen(entity, configurationSection);
                    }
                    if (XMaterial.supports(17)) {
                        XEntity.seventeen(entity, configurationSection);
                    }
                }
            }
        } else if (entity instanceof EnderSignal) {
            lootable = (EnderSignal)entity;
            lootable.setDespawnTimer(configurationSection.getInt("despawn-timer"));
            lootable.setDropItem(configurationSection.getBoolean("drop-item"));
        } else if (entity instanceof ExperienceOrb) {
            lootable = (ExperienceOrb)entity;
            lootable.setExperience(configurationSection.getInt("exp"));
        } else if (entity instanceof Explosive) {
            lootable = (Explosive)entity;
            lootable.setYield((float)configurationSection.getDouble("yield"));
            lootable.setIsIncendiary(configurationSection.getBoolean("incendiary"));
        } else if (entity instanceof EnderCrystal) {
            lootable = (EnderCrystal)entity;
            lootable.setShowingBottom(configurationSection.getBoolean("show-bottom"));
        }
        return entity;
    }

    private static void fourteen(Entity entity, ConfigurationSection configurationSection) {
        if (entity instanceof Raider) {
            Raider raider = (Raider)entity;
            if (configurationSection.isSet("can-join-raid")) {
                raider.setCanJoinRaid(configurationSection.getBoolean("can-join-raid"));
            }
            if (configurationSection.isSet("is-patrol-leader")) {
                raider.setCanJoinRaid(configurationSection.getBoolean("is-patrol-leader"));
            }
        } else if (entity instanceof Cat) {
            Cat cat = (Cat)entity;
            cat.setCatType(XEntity.getCatVariant(configurationSection.getString("variant")));
            cat.setCollarColor(Enums.getIfPresent(DyeColor.class, configurationSection.getString("color")).or(DyeColor.GREEN));
        } else if (entity instanceof Fox) {
            Fox fox = (Fox)entity;
            fox.setCrouching(configurationSection.getBoolean("crouching"));
            fox.setSleeping(configurationSection.getBoolean("sleeping"));
            fox.setFoxType(Enums.getIfPresent(Fox.Type.class, configurationSection.getString("color")).or(Fox.Type.RED));
        } else if (entity instanceof Panda) {
            Panda panda = (Panda)entity;
            panda.setHiddenGene(Enums.getIfPresent(Panda.Gene.class, configurationSection.getString("hidden-gene")).or(Panda.Gene.PLAYFUL));
            panda.setMainGene(Enums.getIfPresent(Panda.Gene.class, configurationSection.getString("main-gene")).or(Panda.Gene.NORMAL));
        } else if (entity instanceof MushroomCow) {
            MushroomCow mushroomCow = (MushroomCow)entity;
            mushroomCow.setVariant(Enums.getIfPresent(MushroomCow.Variant.class, configurationSection.getString("color")).or(MushroomCow.Variant.RED));
        }
    }

    private static void thirteen(Entity entity, ConfigurationSection configurationSection) {
        if (entity instanceof Husk) {
            Husk husk = (Husk)entity;
            husk.setConversionTime(configurationSection.getInt("conversion-time"));
        } else if (entity instanceof Vex) {
            Vex vex = (Vex)entity;
            vex.setCharging(configurationSection.getBoolean("charging"));
        } else if (entity instanceof PufferFish) {
            PufferFish pufferFish = (PufferFish)entity;
            pufferFish.setPuffState(configurationSection.getInt("puff-state"));
        } else if (entity instanceof TropicalFish) {
            TropicalFish tropicalFish = (TropicalFish)entity;
            tropicalFish.setBodyColor(Enums.getIfPresent(DyeColor.class, configurationSection.getString("color")).or(DyeColor.WHITE));
            tropicalFish.setPattern(Enums.getIfPresent(TropicalFish.Pattern.class, configurationSection.getString("pattern")).or(TropicalFish.Pattern.BETTY));
            tropicalFish.setPatternColor(Enums.getIfPresent(DyeColor.class, configurationSection.getString("pattern-color")).or(DyeColor.WHITE));
        } else if (entity instanceof EnderDragon) {
            EnderDragon enderDragon = (EnderDragon)entity;
            enderDragon.setPhase(Enums.getIfPresent(EnderDragon.Phase.class, configurationSection.getString("phase")).or(EnderDragon.Phase.ROAR_BEFORE_ATTACK));
        } else if (entity instanceof Phantom) {
            Phantom phantom = (Phantom)entity;
            phantom.setSize(configurationSection.getInt("size"));
        }
    }

    private static void fifteen(Entity entity, ConfigurationSection configurationSection) {
        if (entity instanceof Bee) {
            Bee bee = (Bee)entity;
            bee.setAnger(configurationSection.getInt("anger") * 20);
            bee.setHasNectar(configurationSection.getBoolean("nectar"));
            bee.setHasStung(configurationSection.getBoolean("stung"));
            bee.setCannotEnterHiveTicks(configurationSection.getInt("disallow-hive") * 20);
        }
    }

    private static void sixteen(Entity entity, ConfigurationSection configurationSection) {
        if (entity instanceof Hoglin) {
            Hoglin hoglin = (Hoglin)entity;
            hoglin.setConversionTime(configurationSection.getInt("conversation") * 20);
            hoglin.setImmuneToZombification(configurationSection.getBoolean("zombification-immunity"));
            hoglin.setIsAbleToBeHunted(configurationSection.getBoolean("can-be-hunted"));
        } else if (entity instanceof Piglin) {
            Piglin piglin = (Piglin)entity;
            piglin.setConversionTime(configurationSection.getInt("conversation") * 20);
            piglin.setImmuneToZombification(configurationSection.getBoolean("zombification-immunity"));
        } else if (entity instanceof Strider) {
            Strider strider = (Strider)entity;
            strider.setShivering(configurationSection.getBoolean("shivering"));
        }
    }

    private static void frog(Entity entity, ConfigurationSection configurationSection) {
        Frog frog = (Frog)entity;
        frog.setVariant((Frog.Variant)Registry.FROG_VARIANT.get(XEntity.fromConfig(configurationSection.getString("variant"))));
    }

    private static boolean seventeen(Entity entity, ConfigurationSection configurationSection) {
        if (entity instanceof Axolotl) {
            com.google.common.base.Optional<Axolotl.Variant> optional;
            Axolotl axolotl = (Axolotl)entity;
            String string = configurationSection.getString("variant");
            if (Strings.isNullOrEmpty(string) && (optional = Enums.getIfPresent(Axolotl.Variant.class, string)).isPresent()) {
                axolotl.setVariant(optional.get());
            }
            if (configurationSection.isSet("playing-dead")) {
                axolotl.setPlayingDead(configurationSection.getBoolean("playing-dead"));
            }
            return true;
        }
        if (entity instanceof Goat) {
            Goat goat = (Goat)entity;
            if (configurationSection.isSet("screaming")) {
                goat.setScreaming(configurationSection.getBoolean("screaming"));
            }
            return true;
        }
        if (entity instanceof GlowSquid) {
            GlowSquid glowSquid = (GlowSquid)entity;
            if (configurationSection.isSet("dark-ticks-remaining")) {
                glowSquid.setDarkTicksRemaining(configurationSection.getInt("dark-ticks-remaining"));
            }
            return true;
        }
        return false;
    }

    public static void editBossBar(BossBar bossBar, ConfigurationSection configurationSection) {
        Object object;
        Object object2;
        String string;
        String string2 = configurationSection.getString("title");
        if (string2 != null) {
            bossBar.setTitle(ChatColor.translateAlternateColorCodes((char)'&', (String)string2));
        }
        if ((string = configurationSection.getString("color")) != null && ((com.google.common.base.Optional)(object2 = Enums.getIfPresent(BarColor.class, string.toUpperCase(Locale.ENGLISH)))).isPresent()) {
            bossBar.setColor((BarColor)((com.google.common.base.Optional)object2).get());
        }
        if ((object2 = configurationSection.getString("style")) != null && ((com.google.common.base.Optional)(object = Enums.getIfPresent(BarStyle.class, ((String)object2).toUpperCase(Locale.ENGLISH)))).isPresent()) {
            bossBar.setStyle((BarStyle)((com.google.common.base.Optional)object).get());
        }
        if (!(object = configurationSection.getStringList("flags")).isEmpty()) {
            EnumSet<BarFlag> enumSet = EnumSet.noneOf(BarFlag.class);
            BarFlag[] barFlagArray = object.iterator();
            while (barFlagArray.hasNext()) {
                String string3 = (String)barFlagArray.next();
                BarFlag barFlag = Enums.getIfPresent(BarFlag.class, string3.toUpperCase(Locale.ENGLISH)).orNull();
                if (barFlag == null) continue;
                enumSet.add(barFlag);
            }
            for (BarFlag barFlag : BarFlag.values()) {
                if (enumSet.contains(barFlag)) {
                    bossBar.addFlag(barFlag);
                    continue;
                }
                bossBar.removeFlag(barFlag);
            }
        }
    }

    static {
        REGISTRY_CAT_VARIANT = XEntity.supportsRegistry("CAT_VARIANT");
        MAPPING = new HashMap(20);
        if (XMaterial.supports(19)) {
            XEntity.register(Frog.class, XEntity::frog);
        }
        Cloneable cloneable = XReflection.of(Villager.class);
        SUPPORTS_Villager_setVillagerLevel = ((ClassHandle)cloneable).method("void setVillagerLevel(int var1);").exists();
        SUPPORTS_Villager_setVillagerExperience = ((ClassHandle)cloneable).method("void setVillagerExperience(int xp);").exists();
        SUPPORTS_Villager_setVillagerType = ((MethodMemberHandle)((ClassHandle)cloneable).method().named("setVillagerType").returns((Class)Void.TYPE)).parameters(((ClassHandle)cloneable).inner(XReflection.ofMinecraft().named("Type"))).exists();
        cloneable = EnumSet.of(EntityType.SKELETON, new EntityType[]{EntityType.ZOMBIE, EntityType.GIANT, EntityType.ZOMBIE_VILLAGER, EntityType.WITHER, EntityType.WITHER_SKELETON, EntityType.ZOMBIE_HORSE});
        if (XMaterial.supports(10)) {
            cloneable.add(EntityType.HUSK);
            cloneable.add(EntityType.STRAY);
            if (XMaterial.supports(11)) {
                cloneable.add(EntityType.SKELETON_HORSE);
                if (XMaterial.supports(13)) {
                    cloneable.add(EntityType.DROWNED);
                    cloneable.add(EntityType.PHANTOM);
                    if (XMaterial.supports(16)) {
                        cloneable.add(EntityType.ZOGLIN);
                        cloneable.add(EntityType.PIGLIN);
                        cloneable.add(EntityType.ZOMBIFIED_PIGLIN);
                    }
                }
            }
        }
        if (!XMaterial.supports(16)) {
            cloneable.add(EntityType.valueOf((String)"PIG_ZOMBIE"));
        }
        UNDEAD = Collections.unmodifiableSet(cloneable);
    }

    private static final class MappedConfigObject {
        private final String configEntry;
        private final MethodHandle setter;
        private final Function<ConfigurationSection, Object> configurationValue;

        private MappedConfigObject(String string, MethodHandle methodHandle, Function<ConfigurationSection, Object> function) {
            this.configEntry = string;
            this.setter = methodHandle;
            this.configurationValue = function;
        }

        private void handle(Entity entity, ConfigurationSection configurationSection) {
            if (configurationSection.isSet(this.configEntry)) {
                try {
                    this.setter.invoke(this.setter, this.configurationValue.apply(configurationSection));
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        }
    }
}

