/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Keyed
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Registry
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.potion.PotionEffectType
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Unmodifiable
 */
package me.zombie_striker.qav.util.xseries.base;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import me.zombie_striker.qav.util.xseries.base.XBase;
import me.zombie_striker.qav.util.xseries.base.annotations.XMerge;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.Internal
public final class XRegistry<XForm extends XBase<XForm, BukkitForm>, BukkitForm>
implements Iterable<XForm> {
    private static boolean PERFORM_AUTO_ADD = true;
    private static final boolean KEYED_EXISTS;
    private final Map<String, XForm> nameMappings = new HashMap<String, XForm>(20);
    private final Map<BukkitForm, XForm> bukkitToX = new IdentityHashMap<BukkitForm, XForm>(20);
    private final Class<BukkitForm> bukkitFormClass;
    private final Class<XForm> xFormClass;
    private final Supplier<Object> registrySupplier;
    private final BiFunction<BukkitForm, String[], XForm> creator;
    private final Function<Integer, XForm[]> createArray;
    private final String registryName;
    private final boolean supportsRegistry;
    private final ClassType bukkitClassType;
    private boolean pulled = false;

    @ApiStatus.Internal
    public XRegistry(Class<BukkitForm> clazz, Class<XForm> clazz2, Supplier<Object> supplier, BiFunction<BukkitForm, String[], XForm> biFunction, Function<Integer, XForm[]> function) {
        boolean bl;
        try {
            supplier.get();
            bl = true;
        } catch (Throwable throwable) {
            bl = false;
        }
        this.bukkitFormClass = Objects.requireNonNull(clazz);
        this.xFormClass = Objects.requireNonNull(clazz2);
        this.registryName = this.bukkitFormClass.getSimpleName();
        this.registrySupplier = supplier;
        this.createArray = Objects.requireNonNull(function);
        this.creator = biFunction;
        this.supportsRegistry = bl;
        this.bukkitClassType = clazz.isEnum() ? ClassType.ENUM : (Modifier.isAbstract(clazz.getModifiers()) ? ClassType.ABSTRACTION : null);
        if (!this.supportsRegistry && this.bukkitClassType == null) {
            throw new IllegalStateException("Bukkit form is not an enum, abstraction or a registry " + clazz);
        }
    }

    @ApiStatus.Internal
    public XRegistry(Class<BukkitForm> clazz, Class<XForm> clazz2, Function<Integer, XForm[]> function) {
        this(clazz, clazz2, null, null, function);
    }

    @ApiStatus.Internal
    @NotNull
    public Map<String, XForm> nameMapping() {
        return this.nameMappings;
    }

    @ApiStatus.Internal
    @NotNull
    public Map<BukkitForm, XForm> bukkitMapping() {
        return this.bukkitToX;
    }

    public Class<BukkitForm> getBukkitFormClass() {
        return this.bukkitFormClass;
    }

    public Class<XForm> getXFormClass() {
        return this.xFormClass;
    }

    public String getName() {
        return this.registryName;
    }

    private void pullValues() {
        if (!this.pulled) {
            this.pulled = true;
            if (this.creator == null) {
                return;
            }
            this.pullFieldNames();
            if (PERFORM_AUTO_ADD) {
                this.pullSystemValues();
            }
        }
    }

    private static <T> void processEnumLikeFields(Class<T> clazz, BiConsumer<Field, T> biConsumer) {
        for (Field field : clazz.getDeclaredFields()) {
            int n = field.getModifiers();
            if (field.getType() != clazz || !Modifier.isPublic(n) || !Modifier.isStatic(n) || !Modifier.isFinal(n)) continue;
            try {
                biConsumer.accept(field, (Field)field.get(null));
            } catch (IllegalAccessException illegalAccessException) {
                throw new IllegalStateException("Cannot process enum-like fields of: " + clazz, illegalAccessException);
            }
        }
    }

    @ApiStatus.Internal
    public void registerName(String string, XForm XForm) {
        this.nameMappings.put(XRegistry.normalizeName(string), XForm);
    }

    private void pullFieldNames() {
        XRegistry.processEnumLikeFields(this.xFormClass, (field, xBase) -> this.registerMerged((XForm)xBase, (Field)field));
    }

    private void pullSystemValues() {
        if (this.bukkitClassType == ClassType.ENUM) {
            for (BukkitForm BukkitForm : this.bukkitFormClass.getEnumConstants()) {
                this.std(((Enum)BukkitForm).name(), BukkitForm);
            }
        } else {
            XRegistry.processEnumLikeFields(this.bukkitFormClass, (field, object) -> {
                if (object == null) {
                    return;
                }
                this.std(field.getName(), object);
            });
        }
        if (this.supportsRegistry) {
            for (Keyed keyed : this.bukkitRegistry()) {
                this.std((XForm)keyed);
            }
        }
    }

    private BukkitForm valueOf(String string) {
        string = string.toUpperCase(Locale.ENGLISH).replace('.', '_');
        Class<BukkitForm> clazz = this.bukkitFormClass;
        try {
            return Enum.valueOf(clazz, string);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    private BukkitForm fieldOf(String string) {
        try {
            return (BukkitForm)this.bukkitFormClass.getDeclaredField(string).get(null);
        } catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
            return null;
        }
    }

    @NotNull
    private Registry<?> bukkitRegistry() {
        return (Registry)this.registrySupplier.get();
    }

    @Nullable
    protected BukkitForm getBukkit(String[] stringArray) {
        for (String string : stringArray) {
            Object object;
            if (this.supportsRegistry) {
                NamespacedKey namespacedKey = (string = string.toLowerCase(Locale.ENGLISH)).contains(":") ? NamespacedKey.fromString((String)string) : NamespacedKey.minecraft((String)string);
                Keyed keyed = this.bukkitRegistry().get(namespacedKey);
                object = keyed != null ? keyed : null;
            } else if (this.bukkitClassType == ClassType.ENUM) {
                object = this.valueOf(string);
            } else if (this.bukkitClassType == ClassType.ABSTRACTION) {
                object = this.fieldOf(string);
            } else {
                throw new AssertionError((Object)("None of the class strategies worked for " + this));
            }
            if (object == null) continue;
            return object;
        }
        return null;
    }

    public @Unmodifiable @NotNull Collection<XForm> getValues() {
        this.pullValues();
        return Collections.unmodifiableCollection(this.bukkitToX.values());
    }

    @Deprecated
    public XForm[] values() {
        this.pullValues();
        Collection<XForm> collection = this.bukkitToX.values();
        return collection.toArray((XBase[])this.createArray.apply(collection.size()));
    }

    @Override
    @NotNull
    public Iterator<XForm> iterator() {
        return this.getValues().iterator();
    }

    @NotNull
    public XForm getByBukkitForm(BukkitForm BukkitForm) {
        Objects.requireNonNull(BukkitForm, () -> "Cannot match null " + this.registryName);
        XBase xBase = (XBase)this.bukkitToX.get(BukkitForm);
        if (xBase == null) {
            if (!PERFORM_AUTO_ADD) {
                throw new UnsupportedOperationException("Unknown standard bukkit form (no auto-add) for " + this.registryName + ": " + BukkitForm);
            }
            if (this.creator == null) {
                throw new UnsupportedOperationException("Unsupported value for " + this.registryName + ": " + BukkitForm);
            }
            BukkitForm BukkitForm2 = this.std((XForm)BukkitForm);
            if (BukkitForm2 == null) {
                throw new IllegalStateException("Unknown " + this.registryName + ": " + BukkitForm);
            }
        }
        return (XForm)xBase;
    }

    public Optional<XForm> getByName(@NotNull String string) {
        Objects.requireNonNull(string, () -> "Cannot match null " + this.registryName);
        if (string.isEmpty()) {
            return Optional.empty();
        }
        this.pullValues();
        return Optional.ofNullable((XBase)this.nameMappings.get(XRegistry.normalizeName(string)));
    }

    @ApiStatus.Internal
    @NotNull
    public static String getBukkitName(@NotNull Object object) {
        Objects.requireNonNull(object, "Cannot get name of a null bukkit form");
        if (object instanceof Enum) {
            return ((Enum)object).name();
        }
        if (KEYED_EXISTS && object instanceof Keyed) {
            return ((Keyed)object).getKey().toString();
        }
        if (object instanceof PotionEffectType) {
            return ((PotionEffectType)object).getName();
        }
        if (object instanceof Enchantment) {
            return ((Enchantment)object).getName();
        }
        throw new AssertionError((Object)("Unknown xform type: " + object + " (" + object.getClass() + ')'));
    }

    @NotNull
    private static String format(@NotNull String string) {
        int n = string.length();
        char[] cArray = new char[n];
        int n2 = 0;
        boolean bl = false;
        for (int i = 0; i < n; ++i) {
            char c = string.charAt(i);
            if (!(bl || n2 == 0 || c != '-' && c != ' ' && c != '_' || cArray[n2] == '_')) {
                bl = true;
                continue;
            }
            boolean bl2 = false;
            if (!(c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') && !(bl2 = c >= '0' && c <= '9')) continue;
            if (bl) {
                cArray[n2++] = 95;
                bl = false;
            }
            cArray[n2++] = bl2 ? c : (char)(c & 0x5F);
        }
        return new String(cArray, 0, n2);
    }

    private static String normalizeName(String string) {
        if ((string = string.toLowerCase(Locale.ENGLISH)).startsWith("minecraft:")) {
            string = string.substring("minecraft:".length());
        }
        string = string.replace('.', '_');
        return string;
    }

    private XForm std(BukkitForm BukkitForm) {
        return this.std(null, BukkitForm);
    }

    private XForm std(@Nullable String string, BukkitForm BukkitForm) {
        String[] stringArray;
        XBase xBase = (XBase)this.bukkitToX.get(BukkitForm);
        if (xBase != null) {
            return (XForm)xBase;
        }
        String string2 = XRegistry.getBukkitName(BukkitForm);
        if (this.getBukkit(new String[]{string2}) == null && string == null) {
            throw new IllegalArgumentException("Unknown standard bukkit form for " + this.registryName + ": " + BukkitForm + (BukkitForm.toString().equals(string2) ? "" : " (" + string2 + ')'));
        }
        if (string == null) {
            String[] stringArray2 = new String[1];
            stringArray = stringArray2;
            stringArray2[0] = string2;
        } else {
            String[] stringArray3 = new String[2];
            stringArray3[0] = string;
            stringArray = stringArray3;
            stringArray3[1] = string2;
        }
        xBase = (XBase)this.creator.apply(BukkitForm, stringArray);
        if (!PERFORM_AUTO_ADD) {
            return (XForm)xBase;
        }
        this.registerName(string2, xBase);
        if (string != null) {
            this.registerName(string, xBase);
        }
        this.bukkitToX.put(BukkitForm, xBase);
        return (XForm)xBase;
    }

    @ApiStatus.Internal
    public XForm std(String[] stringArray) {
        BukkitForm BukkitForm = this.getBukkit(stringArray);
        XBase xBase = (XBase)this.creator.apply(BukkitForm, stringArray);
        return (XForm)this.std((XForm)xBase);
    }

    @ApiStatus.Internal
    public BukkitForm stdEnum(XForm XForm, String[] stringArray) {
        String string = XForm.name();
        boolean bl = false;
        BukkitForm BukkitForm = this.getBukkit(new String[]{string});
        if (BukkitForm == null) {
            BukkitForm = this.getBukkit(stringArray);
        }
        if (BukkitForm == null) {
            BukkitForm = this.registerMerged(XForm);
            bl = true;
        }
        return this.stdEnum0(XForm, stringArray, BukkitForm, bl);
    }

    public BukkitForm stdEnum(XForm XForm, String[] stringArray, BukkitForm BukkitForm) {
        return this.stdEnum0(XForm, stringArray, BukkitForm, false);
    }

    @ApiStatus.Internal
    private BukkitForm stdEnum0(XForm XForm, String[] stringArray, BukkitForm BukkitForm, boolean bl) {
        String string = XForm.name();
        if (!bl) {
            this.registerMerged(XForm);
        }
        this.registerName(string, XForm);
        for (String string2 : stringArray) {
            this.registerName(string2, XForm);
        }
        if (BukkitForm != null) {
            this.bukkitToX.put(BukkitForm, XForm);
        }
        return BukkitForm;
    }

    private BukkitForm registerMerged(XForm XForm) {
        Field field;
        try {
            field = XForm.getClass().getDeclaredField(XForm.name());
        } catch (NoSuchFieldException noSuchFieldException) {
            throw new IllegalStateException("Cannot find field for XForm: " + XForm, noSuchFieldException);
        }
        return this.registerMerged(XForm, field);
    }

    private BukkitForm registerMerged(XForm XForm, Field field) {
        XMerge[] xMergeArray = (XMerge[])field.getAnnotationsByType(XMerge.class);
        Object k = null;
        for (XMerge xMerge : xMergeArray) {
            k = this.getBukkit(new String[]{xMerge.name()});
            this.registerName(xMerge.name(), XForm);
            if (k == null) continue;
            this.bukkitToX.put(k, XForm);
        }
        return (BukkitForm)k;
    }

    @ApiStatus.Internal
    public XForm std(Function<BukkitForm, XForm> function, String[] stringArray) {
        BukkitForm BukkitForm = this.getBukkit(stringArray);
        return (XForm)this.std((XForm)((XBase)function.apply(BukkitForm)));
    }

    @ApiStatus.Internal
    public XForm std(Function<BukkitForm, XForm> function, XForm XForm, String[] stringArray) {
        BukkitForm BukkitForm = this.getBukkit(stringArray);
        if (BukkitForm == null) {
            BukkitForm = XForm.get();
        }
        return (XForm)this.std((XForm)((XBase)function.apply(BukkitForm)));
    }

    @ApiStatus.Internal
    public XForm std(XForm XForm) {
        for (String string : XForm.getNames()) {
            this.registerName(string, XForm);
        }
        if (XForm.isSupported()) {
            this.bukkitToX.put(XForm.get(), XForm);
        }
        return XForm;
    }

    public String toString() {
        return "XRegistry<" + this.registryName + ">(nameMappings=" + this.nameMappings.size() + ", bukkitToX=" + this.bukkitToX.size() + ", bukkitFormClass=" + this.bukkitFormClass.getName() + ", xFormClass=" + this.xFormClass.getName() + ", supportsRegistry=" + this.supportsRegistry + ", bukkitFormClassType=" + (Object)((Object)this.bukkitClassType) + ", pulled=" + this.pulled + ", values=[" + this.bukkitToX.values().stream().limit(10L).map(XBase::name).collect(Collectors.joining(", ")) + ']' + ')';
    }

    static {
        boolean bl = false;
        try {
            Class.forName("org.bukkit.Keyed");
            bl = true;
        } catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        KEYED_EXISTS = bl;
    }

    private static enum ClassType {
        ENUM,
        ABSTRACTION;

    }
}

