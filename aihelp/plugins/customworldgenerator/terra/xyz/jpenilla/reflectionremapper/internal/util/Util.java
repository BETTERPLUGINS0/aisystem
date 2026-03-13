package xyz.jpenilla.reflectionremapper.internal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.reflectionremapper.proxy.annotation.Proxies;

@DefaultQualifier(NonNull.class)
public final class Util {
   @Nullable
   private static final Method PRIVATE_LOOKUP_IN = findMethod(MethodHandles.class, "privateLookupIn", Class.class, Lookup.class);
   @Nullable
   private static final Method DESCRIPTOR_STRING = findMethod(Class.class, "descriptorString");

   private Util() {
   }

   public static boolean mojangMapped() {
      return classExists("net.minecraft.server.level.ServerPlayer");
   }

   public static boolean classExists(final String className) {
      try {
         Class.forName(className);
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }

   public static <E extends Throwable> E sneakyThrow(final Throwable ex) throws E {
      throw ex;
   }

   public static <T> T sneakyThrows(final Util.ThrowingSupplier<T> supplier) {
      try {
         return supplier.get();
      } catch (Throwable var2) {
         throw (RuntimeException)sneakyThrow(var2);
      }
   }

   public static boolean isSynthetic(final int modifiers) {
      return (modifiers & 4096) != 0;
   }

   public static Class<?> findProxiedClass(final Class<?> proxyInterface, final UnaryOperator<String> classMapper) {
      if (!proxyInterface.isInterface()) {
         throw new IllegalArgumentException(proxyInterface.getTypeName() + " is not an interface annotated with @Proxies.");
      } else {
         Proxies proxies = (Proxies)proxyInterface.getDeclaredAnnotation(Proxies.class);
         if (proxies == null) {
            throw new IllegalArgumentException("interface " + proxyInterface.getTypeName() + " is not annotated with @Proxies.");
         } else if (proxies.value() == Object.class && proxies.className().isEmpty()) {
            throw new IllegalArgumentException("@Proxies annotation must either have value() or className() set. Interface: " + proxyInterface.getTypeName());
         } else if (proxies.value() != Object.class) {
            return proxies.value();
         } else {
            try {
               return Class.forName((String)classMapper.apply(proxies.className()));
            } catch (ClassNotFoundException var4) {
               throw new IllegalArgumentException("Could not find class for @Proxied className() " + proxies.className() + ".");
            }
         }
      }
   }

   @Nullable
   private static Method findMethod(final Class<?> holder, final String name, final Class<?>... paramTypes) {
      try {
         return holder.getDeclaredMethod(name, paramTypes);
      } catch (ReflectiveOperationException var4) {
         return null;
      }
   }

   public static MethodHandle handleForDefaultMethod(final Class<?> interfaceClass, final Method method) throws Throwable {
      if (PRIVATE_LOOKUP_IN == null) {
         Constructor<Lookup> constructor = Lookup.class.getDeclaredConstructor(Class.class);
         constructor.setAccessible(true);
         return ((Lookup)constructor.newInstance(interfaceClass)).in(interfaceClass).unreflectSpecial(method, interfaceClass);
      } else {
         return ((Lookup)PRIVATE_LOOKUP_IN.invoke((Object)null, interfaceClass, MethodHandles.lookup())).findSpecial(interfaceClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()), interfaceClass);
      }
   }

   public static List<Class<?>> topDownInterfaceHierarchy(final Class<?> cls) {
      if (!cls.isInterface()) {
         throw new IllegalStateException("Expected an interface, got " + cls);
      } else {
         Set<Class<?>> set = new LinkedHashSet();
         set.add(cls);
         interfaces(cls, set);
         List<Class<?>> list = new ArrayList(set);
         Collections.reverse(list);
         return Collections.unmodifiableList(list);
      }
   }

   private static void interfaces(final Class<?> cls, final Collection<Class<?>> list) {
      Class[] var2 = cls.getInterfaces();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class<?> iface = var2[var4];
         list.add(iface);
         interfaces(iface, list);
      }

   }

   public static String descriptorString(final Class<?> clazz) {
      if (DESCRIPTOR_STRING != null) {
         try {
            return (String)DESCRIPTOR_STRING.invoke(clazz);
         } catch (ReflectiveOperationException var2) {
            throw new RuntimeException("Failed to call Class#descriptorString", var2);
         }
      } else if (clazz == Long.TYPE) {
         return "J";
      } else if (clazz == Integer.TYPE) {
         return "I";
      } else if (clazz == Character.TYPE) {
         return "C";
      } else if (clazz == Short.TYPE) {
         return "S";
      } else if (clazz == Byte.TYPE) {
         return "B";
      } else if (clazz == Double.TYPE) {
         return "D";
      } else if (clazz == Float.TYPE) {
         return "F";
      } else if (clazz == Boolean.TYPE) {
         return "Z";
      } else if (clazz == Void.TYPE) {
         return "V";
      } else {
         return clazz.isArray() ? "[" + descriptorString(clazz.getComponentType()) : 'L' + clazz.getName().replace('.', '/') + ';';
      }
   }

   public static String firstLine(final InputStream mappings) {
      try {
         mappings.mark(1024);
         BufferedReader reader = new BufferedReader(new InputStreamReader(mappings, StandardCharsets.UTF_8));
         String line = reader.readLine();
         mappings.reset();
         return line;
      } catch (IOException var3) {
         throw new UncheckedIOException("Failed to read first line of input stream", var3);
      }
   }

   @FunctionalInterface
   public interface ThrowingSupplier<T> {
      T get() throws Throwable;
   }
}
