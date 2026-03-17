package advancedplugins.pm2.cv.enums;

import advancedplugins.pm2.cv.api.util.reflection.ClassReflection;
import org.jetbrains.annotations.NotNull;

public enum EnumStandProperty implements PropertiesEnum {
   VISIBILITY(Boolean.TYPE, true),
   GLOWING(Boolean.TYPE, false),
   GRAVITY(Boolean.TYPE, true),
   MARKER(Boolean.TYPE, false),
   BASE_PLATE(Boolean.TYPE, true),
   ARMS(Boolean.TYPE, false),
   SMALL(Boolean.TYPE, false),
   SILENT(Boolean.TYPE, false),
   CUSTOM_NAME(String.class, ""),
   CUSTOM_NAME_VISIBILITY(Boolean.TYPE, false);

   @NotNull
   private final Class<?> valueType;
   @NotNull
   private final Object defaultValue;

   private EnumStandProperty(@NotNull Class<?> valueType, @NotNull Object defaultValue) {
      this.valueType = ClassReflection.isPrimitiveType(var3) ? ClassReflection.getPrimitiveType(var3) : var3;
      this.defaultValue = var4;
   }

   @NotNull
   public Class<?> getValueType() {
      return this.valueType;
   }

   @NotNull
   public Object getDefaultValueRaw() {
      return this.defaultValue;
   }

   // $FF: synthetic method
   private static EnumStandProperty[] $values() {
      return new EnumStandProperty[]{VISIBILITY, GLOWING, GRAVITY, MARKER, BASE_PLATE, ARMS, SMALL, SILENT, CUSTOM_NAME, CUSTOM_NAME_VISIBILITY};
   }
}
