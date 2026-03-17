package me.PM2.infinitevehicles.xseries.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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
import me.PM2.infinitevehicles.xseries.XAttribute;
import me.PM2.infinitevehicles.xseries.XBiome;
import me.PM2.infinitevehicles.xseries.XEnchantment;
import me.PM2.infinitevehicles.xseries.XEntityType;
import me.PM2.infinitevehicles.xseries.XItemFlag;
import me.PM2.infinitevehicles.xseries.XPotion;
import me.PM2.infinitevehicles.xseries.XSound;
import me.PM2.infinitevehicles.xseries.base.annotations.XChange;
import me.PM2.infinitevehicles.xseries.base.annotations.XInfo;
import me.PM2.infinitevehicles.xseries.base.annotations.XMerge;
import me.PM2.infinitevehicles.xseries.particles.XParticle;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class XRegistry<XForm extends XBase<XForm, BukkitForm>, BukkitForm> implements Iterable<XForm> {
   @Internal
   private static boolean PERFORM_AUTO_ADD = true;
   @Internal
   private static boolean DISCARD_METADATA = true;
   private static final boolean KEYED_EXISTS;
   private static final Map<Class<? extends XBase<?, ?>>, XRegistry<?, ?>> REGISTRIES;
   private static boolean ensureLoaded;
   private final Map<String, XForm> nameMappings;
   private final Map<BukkitForm, XForm> bukkitToX;
   private Map<XForm, XModuleMetadata> metadata;
   private Map<XForm, Field> backingFields;
   private final Class<BukkitForm> bukkitFormClass;
   private final Class<XForm> xFormClass;
   private final Supplier<Object> registrySupplier;
   private final BiFunction<BukkitForm, String[], XForm> creator;
   private final Function<Integer, XForm[]> createArray;
   private final String registryName;
   private final boolean supportsRegistry;
   private final XRegistry.ClassType bukkitClassType;
   private boolean pulled;
   private boolean alreadyDiscardedMetadata;

   private static void ensureLoadedRegistries() {
      if (!ensureLoaded) {
         XAttribute.REGISTRY.getClass();
         XSound.REGISTRY.getClass();
         XBiome.REGISTRY.getClass();
         XItemFlag.REGISTRY.getClass();
         XPotion.REGISTRY.getClass();
         XEntityType.REGISTRY.getClass();
         XEnchantment.REGISTRY.getClass();
         XParticle.REGISTRY.getClass();
         ensureLoaded = true;
      }
   }

   @Nullable
   @Experimental
   public static XRegistry<?, ?> rawRegistryOf(Class<?> var0) {
      ensureLoadedRegistries();
      return (XRegistry)REGISTRIES.get(var0);
   }

   @Nullable
   @Experimental
   public static <XForm extends XBase<XForm, BukkitForm>, BukkitForm> XRegistry<XForm, BukkitForm> registryOf(Class<? extends XForm> var0) {
      ensureLoadedRegistries();
      return (XRegistry)REGISTRIES.get(var0);
   }

   protected static <XForm extends XBase<XForm, BukkitForm>, BukkitForm> void registerModule(XRegistry<XForm, BukkitForm> var0, Class<? extends XForm> var1) {
      REGISTRIES.put(var1, var0);
   }

   @Internal
   public XRegistry(Class<BukkitForm> var1, Class<XForm> var2, Supplier<Object> var3, BiFunction<BukkitForm, String[], XForm> var4, Function<Integer, XForm[]> var5) {
      this.nameMappings = new HashMap(20);
      this.bukkitToX = new IdentityHashMap(20);
      this.pulled = false;
      this.alreadyDiscardedMetadata = false;

      boolean var6;
      try {
         var3.get();
         var6 = true;
      } catch (Throwable var8) {
         var6 = false;
      }

      this.bukkitFormClass = (Class)Objects.requireNonNull(var1);
      this.xFormClass = (Class)Objects.requireNonNull(var2);
      this.registryName = this.bukkitFormClass.getSimpleName();
      this.registrySupplier = var3;
      this.createArray = (Function)Objects.requireNonNull(var5);
      this.creator = var4;
      this.supportsRegistry = var6;
      if (var1.isEnum()) {
         this.bukkitClassType = XRegistry.ClassType.ENUM;
      } else if (Modifier.isAbstract(var1.getModifiers())) {
         this.bukkitClassType = XRegistry.ClassType.ABSTRACTION;
      } else {
         this.bukkitClassType = null;
      }

      if (!this.supportsRegistry && this.bukkitClassType == null) {
         throw new IllegalStateException("Bukkit form is not an enum, abstraction or a registry " + var1);
      } else {
         registerModule(this, var2);
      }
   }

   @Internal
   public XRegistry(Class<BukkitForm> var1, Class<XForm> var2, Function<Integer, XForm[]> var3) {
      this(var1, var2, (Supplier)null, (BiFunction)null, var3);
   }

   @Internal
   @NotNull
   public Map<String, XForm> nameMapping() {
      return this.nameMappings;
   }

   @Internal
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

   private static <T> void processEnumLikeFields(Class<T> var0, BiConsumer<Field, T> var1) {
      Field[] var2 = var0.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         int var6 = var5.getModifiers();
         if (var5.getType() == var0 && Modifier.isPublic(var6) && Modifier.isStatic(var6) && Modifier.isFinal(var6)) {
            try {
               var1.accept(var5, var5.get((Object)null));
            } catch (IllegalAccessException var8) {
               throw new IllegalStateException("Cannot process enum-like fields of: " + var0, var8);
            }
         }
      }

   }

   @Internal
   public void registerName(String var1, XForm var2) {
      this.nameMappings.put(normalizeName(var1), var2);
   }

   private void pullFieldNames() {
      processEnumLikeFields(this.xFormClass, (var1, var2) -> {
         this.registerMerged(var2, var1);
      });
   }

   private void pullSystemValues() {
      if (this.bukkitClassType == XRegistry.ClassType.ENUM) {
         Object[] var1 = this.bukkitFormClass.getEnumConstants();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Object var4 = var1[var3];
            this.std(((Enum)var4).name(), var4);
         }
      } else {
         processEnumLikeFields(this.bukkitFormClass, (var1x, var2x) -> {
            if (var2x != null) {
               this.std(var1x.getName(), var2x);
            }
         });
      }

      if (this.supportsRegistry) {
         Iterator var5 = this.bukkitRegistry().iterator();

         while(var5.hasNext()) {
            Keyed var6 = (Keyed)var5.next();
            this.std((Object)var6);
         }
      }

   }

   private BukkitForm valueOf(String var1) {
      var1 = var1.toUpperCase(Locale.ENGLISH).replace('.', '_');
      Class var2 = this.bukkitFormClass;

      try {
         return Enum.valueOf(var2, var1);
      } catch (IllegalArgumentException var4) {
         return null;
      }
   }

   private BukkitForm fieldOf(String var1) {
      try {
         return this.bukkitFormClass.getDeclaredField(var1).get((Object)null);
      } catch (NoSuchFieldException | IllegalAccessException var3) {
         return null;
      }
   }

   @NotNull
   private Registry<?> bukkitRegistry() {
      return (Registry)this.registrySupplier.get();
   }

   @Nullable
   protected BukkitForm getBukkit(String[] var1) {
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         Object var6;
         if (this.supportsRegistry) {
            var5 = var5.toLowerCase(Locale.ENGLISH);
            NamespacedKey var7;
            if (var5.contains(":")) {
               var7 = XNamespacedKey.fromString(var5);
            } else {
               var7 = NamespacedKey.minecraft(var5);
            }

            Keyed var8 = this.bukkitRegistry().get(var7);
            if (var8 != null) {
               var6 = var8;
            } else {
               var6 = null;
            }
         } else if (this.bukkitClassType == XRegistry.ClassType.ENUM) {
            var6 = this.valueOf(var5);
         } else {
            if (this.bukkitClassType != XRegistry.ClassType.ABSTRACTION) {
               throw new AssertionError("None of the class strategies worked for " + this);
            }

            var6 = this.fieldOf(var5);
         }

         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }

   @Internal
   public void discardMetadata() {
      if (DISCARD_METADATA) {
         this.backingFields = null;
         this.metadata = null;
      }
   }

   @NotNull
   @Unmodifiable
   public Collection<XForm> getValues() {
      this.pullValues();
      return Collections.unmodifiableCollection(this.bukkitToX.values());
   }

   /** @deprecated */
   @Deprecated
   public XForm[] values() {
      this.pullValues();
      Collection var1 = this.bukkitToX.values();
      return (XBase[])var1.toArray((XBase[])this.createArray.apply(var1.size()));
   }

   @NotNull
   public Iterator<XForm> iterator() {
      return this.getValues().iterator();
   }

   @NotNull
   public XForm getByBukkitForm(BukkitForm var1) {
      Objects.requireNonNull(var1, () -> {
         return "Cannot match null " + this.registryName;
      });
      XBase var2 = (XBase)this.bukkitToX.get(var1);
      if (var2 == null) {
         if (!PERFORM_AUTO_ADD) {
            throw new UnsupportedOperationException("Unknown standard bukkit form (no auto-add) for " + this.registryName + ": " + var1);
         }

         if (this.creator == null) {
            throw new UnsupportedOperationException("Unsupported value for " + this.registryName + ": " + var1);
         }

         XBase var3 = this.std(var1);
         if (var3 == null) {
            throw new IllegalStateException("Unknown " + this.registryName + ": " + var1);
         }
      }

      return var2;
   }

   public Optional<XForm> getByName(@NotNull String var1) {
      Objects.requireNonNull(var1, () -> {
         return "Cannot match null " + this.registryName;
      });
      if (var1.isEmpty()) {
         return Optional.empty();
      } else {
         this.pullValues();
         return Optional.ofNullable((XBase)this.nameMappings.get(normalizeName(var1)));
      }
   }

   @Internal
   @NotNull
   public static String getBukkitName(@NotNull Object var0) {
      Objects.requireNonNull(var0, "Cannot get name of a null bukkit form");
      if (var0 instanceof Enum) {
         return ((Enum)var0).name();
      } else if (KEYED_EXISTS && var0 instanceof Keyed) {
         return ((Keyed)var0).getKey().toString();
      } else if (var0 instanceof PotionEffectType) {
         return ((PotionEffectType)var0).getName();
      } else if (var0 instanceof Enchantment) {
         return ((Enchantment)var0).getName();
      } else {
         throw new AssertionError("Unknown xform type: " + var0 + " (" + var0.getClass() + ')');
      }
   }

   @NotNull
   private static String format(@NotNull String var0) {
      int var1 = var0.length();
      char[] var2 = new char[var1];
      int var3 = 0;
      boolean var4 = false;

      for(int var5 = 0; var5 < var1; ++var5) {
         char var6 = var0.charAt(var5);
         if (!var4 && var3 != 0 && (var6 == '-' || var6 == ' ' || var6 == '_') && var2[var3] != '_') {
            var4 = true;
         } else {
            boolean var7 = false;
            if (var6 >= 'A' && var6 <= 'Z' || var6 >= 'a' && var6 <= 'z' || (var7 = var6 >= '0' && var6 <= '9')) {
               if (var4) {
                  var2[var3++] = '_';
                  var4 = false;
               }

               if (var7) {
                  var2[var3++] = var6;
               } else {
                  var2[var3++] = (char)(var6 & 95);
               }
            }
         }
      }

      return new String(var2, 0, var3);
   }

   private static String normalizeName(String var0) {
      var0 = var0.toLowerCase(Locale.ENGLISH);
      if (var0.startsWith("minecraft:")) {
         var0 = var0.substring("minecraft:".length());
      }

      var0 = var0.replace('.', '_');
      return var0;
   }

   private XForm std(BukkitForm var1) {
      return this.std((String)null, (Object)var1);
   }

   private XForm std(@Nullable String var1, BukkitForm var2) {
      XBase var3 = (XBase)this.bukkitToX.get(var2);
      if (var3 != null) {
         return var3;
      } else {
         String var4 = getBukkitName(var2);
         if (this.getBukkit(new String[]{var4}) == null && var1 == null) {
            throw new IllegalArgumentException("Unknown standard bukkit form for " + this.registryName + ": " + var2 + (var2.toString().equals(var4) ? "" : " (" + var4 + ')'));
         } else {
            var3 = (XBase)this.creator.apply(var2, var1 == null ? new String[]{var4} : new String[]{var1, var4});
            if (!PERFORM_AUTO_ADD) {
               return var3;
            } else {
               this.registerName(var4, var3);
               if (var1 != null) {
                  this.registerName(var1, var3);
               }

               this.bukkitToX.put(var2, var3);
               return var3;
            }
         }
      }
   }

   @Internal
   public XForm std(String[] var1) {
      Object var2 = this.getBukkit(var1);
      XBase var3 = (XBase)this.creator.apply(var2, var1);
      return this.std(var3);
   }

   @Internal
   public BukkitForm stdEnum(XForm var1, String[] var2) {
      String var3 = var1.name();
      boolean var4 = false;
      Object var5 = this.getBukkit(new String[]{var3});
      if (var5 == null) {
         var5 = this.getBukkit(var2);
      }

      if (var5 == null) {
         var5 = this.registerMerged(var1);
         var4 = true;
      }

      return this.stdEnum0(var1, var2, var5, var4);
   }

   public BukkitForm stdEnum(XForm var1, String[] var2, BukkitForm var3) {
      return this.stdEnum0(var1, var2, var3, false);
   }

   @Internal
   private BukkitForm stdEnum0(XForm var1, String[] var2, BukkitForm var3, boolean var4) {
      String var5 = var1.name();
      if (!var4) {
         this.registerMerged(var1);
      }

      this.registerName(var5, var1);
      String[] var6 = var2;
      int var7 = var2.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String var9 = var6[var8];
         this.registerName(var9, var1);
      }

      if (var3 != null) {
         this.bukkitToX.put(var3, var1);
      }

      return var3;
   }

   private BukkitForm registerMerged(XForm var1) {
      return this.registerMerged(var1, this.getBackingField(var1));
   }

   @NotNull
   @Internal
   public Field getBackingField(XForm var1) {
      try {
         return var1.getClass().getDeclaredField(var1.name());
      } catch (NoSuchFieldException var6) {
         try {
            if (this.backingFields == null) {
               this.cacheBackingFields();
            }

            Field var3 = (Field)this.backingFields.get(var1);
            if (var3 != null) {
               return var3;
            }
         } catch (Throwable var5) {
            IllegalStateException var4 = new IllegalStateException("Cannot find field for XForm: " + var1 + " - " + var1.getClass(), var5);
            var4.addSuppressed(var6);
            throw var4;
         }

         throw new IllegalStateException("Cannot find field for XForm: " + var1 + " - " + var1.getClass(), var6);
      }
   }

   private void cacheBackingFields() {
      if (this.backingFields != null) {
         throw new IllegalStateException("Backing fields are already cached");
      } else if (this.alreadyDiscardedMetadata) {
         throw new IllegalStateException("Metadata have already been used and discarded");
      } else {
         this.backingFields = new IdentityHashMap();
         this.alreadyDiscardedMetadata = true;
         Field[] var1 = this.xFormClass.getDeclaredFields();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Field var4 = var1[var3];
            int var5 = var4.getModifiers();
            if (Modifier.isPublic(var5) && Modifier.isStatic(var5) && Modifier.isFinal(var5) && var4.getType() == this.xFormClass && !var4.isAnnotationPresent(XRegistry.Ignore.class)) {
               try {
                  Object var6 = Objects.requireNonNull(var4.get((Object)null), () -> {
                     return "XForm backing field returned null: " + var4 + " for registry of " + this.xFormClass;
                  });
                  XBase var7 = (XBase)var6;
                  this.backingFields.put(var7, var4);
               } catch (IllegalAccessException var8) {
                  throw new RuntimeException(var8);
               }
            }
         }

      }
   }

   @Internal
   public XModuleMetadata getOrRegisterMetadata(XForm var1, Field var2, boolean var3) {
      XModuleMetadata var4 = this.metadata == null ? null : (XModuleMetadata)this.metadata.get(var1);
      if (var4 != null) {
         return var4;
      } else {
         var4 = new XModuleMetadata(var2.isAnnotationPresent(Deprecated.class), (XChange[])var2.getAnnotationsByType(XChange.class), (XMerge[])var2.getAnnotationsByType(XMerge.class), (XInfo)var2.getAnnotation(XInfo.class));
         if (!var3) {
            if (this.metadata == null) {
               this.metadata = new IdentityHashMap(10);
            }

            this.metadata.put(var1, var4);
         }

         return var4;
      }
   }

   private BukkitForm registerMerged(XForm var1, Field var2) {
      XMerge[] var3 = this.getOrRegisterMetadata(var1, var2, true).getMerges();
      Object var4 = null;
      XMerge[] var5 = var3;
      int var6 = var3.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         XMerge var8 = var5[var7];
         var4 = this.getBukkit(new String[]{var8.name()});
         this.registerName(var8.name(), var1);
         if (var4 != null) {
            this.bukkitToX.put(var4, var1);
         }
      }

      return var4;
   }

   @Internal
   public XForm std(Function<BukkitForm, XForm> var1, String[] var2) {
      Object var3 = this.getBukkit(var2);
      return this.std((XBase)var1.apply(var3));
   }

   @Internal
   public XForm std(Function<BukkitForm, XForm> var1, XForm var2, String[] var3) {
      Object var4 = this.getBukkit(var3);
      if (var4 == null) {
         var4 = var2.get();
      }

      return this.std((XBase)var1.apply(var4));
   }

   @Internal
   public XForm std(XForm var1) {
      String[] var2 = var1.getNames();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         this.registerName(var5, var1);
      }

      if (var1.isSupported()) {
         this.bukkitToX.put(var1.get(), var1);
      }

      return var1;
   }

   public String toString() {
      return "XRegistry<" + this.registryName + ">(nameMappings=" + this.nameMappings.size() + ", bukkitToX=" + this.bukkitToX.size() + ", bukkitFormClass=" + this.bukkitFormClass.getName() + ", xFormClass=" + this.xFormClass.getName() + ", supportsRegistry=" + this.supportsRegistry + ", bukkitFormClassType=" + this.bukkitClassType + ", pulled=" + this.pulled + ", values=[" + (String)this.bukkitToX.values().stream().limit(10L).map(XBase::name).collect(Collectors.joining(", ")) + ']' + ')';
   }

   static {
      boolean var0 = false;

      try {
         Class.forName("org.bukkit.Keyed");
         var0 = true;
      } catch (ClassNotFoundException var2) {
      }

      KEYED_EXISTS = var0;
      REGISTRIES = new IdentityHashMap();
      ensureLoaded = false;
   }

   private static enum ClassType {
      ENUM,
      ABSTRACTION;

      // $FF: synthetic method
      private static XRegistry.ClassType[] $values() {
         return new XRegistry.ClassType[]{ENUM, ABSTRACTION};
      }
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.FIELD})
   @Documented
   @Internal
   public @interface Ignore {
   }
}
