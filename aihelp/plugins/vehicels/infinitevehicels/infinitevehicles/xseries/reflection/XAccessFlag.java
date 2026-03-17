package me.PM2.infinitevehicles.xseries.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public enum XAccessFlag {
   PUBLIC(1, true, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.CLASS, XAccessFlag.JVMLocation.FIELD, XAccessFlag.JVMLocation.METHOD, XAccessFlag.JVMLocation.INNER_CLASS}),
   PRIVATE(2, true, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.FIELD, XAccessFlag.JVMLocation.METHOD, XAccessFlag.JVMLocation.INNER_CLASS}),
   PROTECTED(4, true, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.FIELD, XAccessFlag.JVMLocation.METHOD, XAccessFlag.JVMLocation.INNER_CLASS}),
   STATIC(8, true, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.FIELD, XAccessFlag.JVMLocation.METHOD, XAccessFlag.JVMLocation.INNER_CLASS}),
   FINAL(16, true, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.CLASS, XAccessFlag.JVMLocation.FIELD, XAccessFlag.JVMLocation.METHOD, XAccessFlag.JVMLocation.INNER_CLASS, XAccessFlag.JVMLocation.METHOD_PARAMETER}),
   SUPER(32, false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.CLASS}),
   OPEN(32, false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.MODULE}),
   TRANSITIVE(32, false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.MODULE_REQUIRES}),
   SYNCHRONIZED(32, true, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.METHOD}),
   STATIC_PHASE(64, false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.MODULE_REQUIRES}),
   VOLATILE(64, true, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.FIELD}),
   BRIDGE(getPrivateMod("BRIDGE"), false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.METHOD}),
   TRANSIENT(128, true, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.FIELD}),
   VARARGS(getPrivateMod("VARARGS"), false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.METHOD}),
   NATIVE(256, true, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.METHOD}),
   INTERFACE(512, false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.CLASS, XAccessFlag.JVMLocation.INNER_CLASS}),
   ABSTRACT(1024, true, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.CLASS, XAccessFlag.JVMLocation.METHOD, XAccessFlag.JVMLocation.INNER_CLASS}),
   STRICT(2048, true, new XAccessFlag.JVMLocation[0]),
   SYNTHETIC(getPrivateMod("SYNTHETIC"), false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.CLASS, XAccessFlag.JVMLocation.FIELD, XAccessFlag.JVMLocation.METHOD, XAccessFlag.JVMLocation.INNER_CLASS, XAccessFlag.JVMLocation.METHOD_PARAMETER, XAccessFlag.JVMLocation.MODULE, XAccessFlag.JVMLocation.MODULE_REQUIRES, XAccessFlag.JVMLocation.MODULE_EXPORTS, XAccessFlag.JVMLocation.MODULE_OPENS}),
   ANNOTATION(getPrivateMod("ANNOTATION"), false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.CLASS, XAccessFlag.JVMLocation.INNER_CLASS}),
   ENUM(getPrivateMod("ENUM"), false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.CLASS, XAccessFlag.JVMLocation.FIELD, XAccessFlag.JVMLocation.INNER_CLASS}),
   MANDATED(getPrivateMod("MANDATED"), false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.METHOD_PARAMETER, XAccessFlag.JVMLocation.MODULE, XAccessFlag.JVMLocation.MODULE_REQUIRES, XAccessFlag.JVMLocation.MODULE_EXPORTS, XAccessFlag.JVMLocation.MODULE_OPENS}),
   MODULE(32768, false, new XAccessFlag.JVMLocation[]{XAccessFlag.JVMLocation.CLASS});

   private static final XAccessFlag[] VALUES = values();
   private final int mask;
   private final boolean sourceModifier;
   private final Set<XAccessFlag.JVMLocation> locations;

   private XAccessFlag(int param3, boolean param4, XAccessFlag.JVMLocation... param5) {
      this.mask = var3;
      this.sourceModifier = var4;
      this.locations = Collections.unmodifiableSet(var5.length == 0 ? EnumSet.noneOf(XAccessFlag.JVMLocation.class) : EnumSet.copyOf(Arrays.asList(var5)));
   }

   private static int getPrivateMod(String var0) {
      try {
         Class var1 = Class.forName("java.lang.reflect.AccessFlag");
         Field var2 = var1.getDeclaredField(var0);
         Object var3 = var2.get((Object)null);
         Method var4 = var1.getDeclaredMethod("mask");
         return (Integer)var4.invoke(var3);
      } catch (Throwable var6) {
         try {
            return (Integer)Modifier.class.getDeclaredField(var0).get((Object)null);
         } catch (IllegalAccessException | NoSuchFieldException var5) {
            return 0;
         }
      }
   }

   @Contract(
      pure = true
   )
   public int mask() {
      return this.mask;
   }

   @Contract(
      pure = true
   )
   public boolean sourceModifier() {
      return this.sourceModifier;
   }

   @NotNull
   @Contract(
      pure = true
   )
   @Unmodifiable
   public Set<XAccessFlag.JVMLocation> locations() {
      return this.locations;
   }

   @Contract(
      pure = true
   )
   public static Set<XAccessFlag> of(int var0) {
      EnumSet var1 = EnumSet.noneOf(XAccessFlag.class);
      XAccessFlag[] var2 = VALUES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         XAccessFlag var5 = var2[var4];
         if (var5.isSet(var0)) {
            var1.add(var5);
         }
      }

      return var1;
   }

   @Contract(
      pure = true
   )
   public int add(int var1) {
      return var1 | this.mask;
   }

   @Contract(
      pure = true
   )
   public int remove(int var1) {
      return var1 & ~this.mask;
   }

   @Contract(
      pure = true
   )
   public boolean isSet(int var1) {
      return (var1 & this.mask) != 0;
   }

   @Contract(
      pure = true
   )
   public static boolean isSet(int var0, XAccessFlag... var1) {
      XAccessFlag[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         XAccessFlag var5 = var2[var4];
         if (!var5.isSet(var0)) {
            return false;
         }
      }

      return true;
   }

   @Contract(
      pure = true
   )
   public static Optional<Integer> getModifiers(Object var0) {
      if (var0 instanceof Class) {
         return Optional.of(((Class)var0).getModifiers());
      } else {
         return var0 instanceof Member ? Optional.of(((Member)var0).getModifiers()) : Optional.empty();
      }
   }

   @Contract(
      pure = true
   )
   public static String toString(int var0) {
      StringJoiner var1 = new StringJoiner(" ", "Flags::" + var0 + '(', ")");
      XAccessFlag[] var2 = VALUES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         XAccessFlag var5 = var2[var4];
         if (var5.isSet(var0)) {
            var1.add(var5.name().toLowerCase(Locale.ENGLISH));
         }
      }

      return var1.toString();
   }

   // $FF: synthetic method
   private static XAccessFlag[] $values() {
      return new XAccessFlag[]{PUBLIC, PRIVATE, PROTECTED, STATIC, FINAL, SUPER, OPEN, TRANSITIVE, SYNCHRONIZED, STATIC_PHASE, VOLATILE, BRIDGE, TRANSIENT, VARARGS, NATIVE, INTERFACE, ABSTRACT, STRICT, SYNTHETIC, ANNOTATION, ENUM, MANDATED, MODULE};
   }

   public static enum JVMLocation {
      CLASS,
      FIELD,
      METHOD,
      INNER_CLASS,
      METHOD_PARAMETER,
      MODULE,
      MODULE_REQUIRES,
      MODULE_EXPORTS,
      MODULE_OPENS;

      // $FF: synthetic method
      private static XAccessFlag.JVMLocation[] $values() {
         return new XAccessFlag.JVMLocation[]{CLASS, FIELD, METHOD, INNER_CLASS, METHOD_PARAMETER, MODULE, MODULE_REQUIRES, MODULE_EXPORTS, MODULE_OPENS};
      }
   }
}
