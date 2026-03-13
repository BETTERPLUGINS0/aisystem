package org.terraform.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.main.TerraformGeneratorPlugin;

public class Pre14PrivateFieldHandler extends PrivateFieldHandler {
   @Nullable
   private static final MethodHandle FIELD_MODIFIERS;

   public void injectField(@NotNull Object obj, @NotNull String field, Object value) throws Throwable {
      Field targetField = obj.getClass().getField(field);
      targetField.setAccessible(true);
      FIELD_MODIFIERS.invoke(targetField, targetField.getModifiers() & -17);
      targetField.set(obj, value);
      TerraformGeneratorPlugin.logger.info("Pre Java 14 detected.");
   }

   static {
      Lookup lookup = MethodHandles.lookup();
      MethodHandle fieldModifiers = null;

      try {
         Field modifiersField = Field.class.getDeclaredField("modifiers");
         modifiersField.setAccessible(true);
         fieldModifiers = lookup.unreflectSetter(modifiersField);
      } catch (IllegalAccessException | NoSuchFieldException var3) {
         TerraformGeneratorPlugin.logger.stackTrace(var3);
      }

      FIELD_MODIFIERS = fieldModifiers;
   }
}
