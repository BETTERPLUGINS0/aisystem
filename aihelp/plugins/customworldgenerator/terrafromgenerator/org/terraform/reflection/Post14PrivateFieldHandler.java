package org.terraform.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;
import org.terraform.main.TerraformGeneratorPlugin;

public class Post14PrivateFieldHandler extends PrivateFieldHandler {
   private static final MethodHandle LOOKUP;
   private static final MethodHandle VAR_HANDLE_SET;
   private static final MethodHandle FIND_VAR_HANDLE;

   public void injectField(@NotNull Object obj, @NotNull String field, Object value) throws Exception {
      Field targetField = obj.getClass().getField(field);
      targetField.setAccessible(true);
      int mds = targetField.getModifiers();

      try {
         Object lookup = LOOKUP.invoke((Void)null, Field.class, MethodHandles.lookup());
         Object varHandleModifiers = FIND_VAR_HANDLE.invoke(lookup, Field.class, "modifiers", Integer.TYPE);
         VAR_HANDLE_SET.invoke(varHandleModifiers, new Object[]{targetField, mds & -17});
      } catch (Throwable var8) {
         TerraformGeneratorPlugin.logger.info("Java 14+ detected.");
      }

      targetField.set(obj, value);
   }

   public void injectField(Object obj, @NotNull Field targetField, Object value) throws IllegalArgumentException, IllegalAccessException {
      targetField.setAccessible(true);
      int mds = targetField.getModifiers();

      try {
         Object lookup = LOOKUP.invoke((Void)null, Field.class, MethodHandles.lookup());
         Object varHandleModifiers = FIND_VAR_HANDLE.invoke(lookup, Field.class, "modifiers", Integer.TYPE);
         VAR_HANDLE_SET.invoke(varHandleModifiers, new Object[]{targetField, mds & -17});
      } catch (Throwable var7) {
         TerraformGeneratorPlugin.logger.info("Java 14+ detected.");
      }

      targetField.set(obj, value);
   }

   static {
      MethodHandle lookup = null;
      MethodHandle varHandleSet = null;
      MethodHandle findVarHandle = null;
      Lookup publicLookup = MethodHandles.lookup();

      try {
         Class<?> varHandle = Class.forName("java.lang.invoke.VarHandle");
         lookup = publicLookup.findStatic(MethodHandles.class, "privateLookupIn", MethodType.methodType(Lookup.class, Class.class, Lookup.class));
         findVarHandle = publicLookup.findVirtual(Lookup.class, "findVarHandle", MethodType.methodType(varHandle, Class.class, String.class, Class.class));
         varHandleSet = publicLookup.findVirtual(varHandle, "set", MethodType.methodType(Void.TYPE, Object[].class));
      } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException var5) {
         TerraformGeneratorPlugin.logger.stackTrace(var5);
      }

      LOOKUP = lookup;
      VAR_HANDLE_SET = varHandleSet;
      FIND_VAR_HANDLE = findVarHandle;
   }
}
