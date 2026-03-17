package advancedplugins.pm2.cv.enums;

import advancedplugins.pm2.cv.api.util.reflection.ClassReflection;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public enum EnumDisplayProperty implements PropertiesEnum {
   VISIBILITY(Boolean.TYPE, true),
   GLOWING(Boolean.TYPE, false),
   GLOWING_COLOR(Color.class, Color.WHITE),
   TRANSFORM_INTERPOLATION_DELAY(Integer.TYPE, 0),
   TRANSFORM_INTERPOLATION_DURATION(Integer.TYPE, 0),
   VIEW_RANGE(Float.TYPE, 1.0F),
   BLOCK_DATA(BlockData.class, Material.AIR.createBlockData()),
   ITEM_STACK(ItemStack.class, new ItemStack(Material.AIR)),
   ITEM_DISPLAY_SLOT(EnumItemDisplaySlot.class, EnumItemDisplaySlot.NONE),
   TEXT(String.class, ""),
   TEXT_LINE_WIDTH(Integer.TYPE, 200),
   TEXT_OPACITY(Byte.TYPE, -1),
   TEXT_SHADOWED(Boolean.TYPE, false),
   TEXT_CAN_SEE_THROUGH(Boolean.TYPE, false),
   TEXT_DEFAULT_BACKGROUND(Boolean.TYPE, false),
   TEXT_ALIGNMENT(EnumDisplayTextAlignment.class, EnumDisplayTextAlignment.CENTER),
   LEFT_ROTATION(Quaternionf.class, new Quaternionf()),
   POSITION(Vector3f.class, new Vector3f(0.0F, 0.0F, 0.0F)),
   TRANSFORMATION(Transformation.class, new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(), new Quaternionf()));

   @NotNull
   private final Class<?> valueType;
   @NotNull
   private final Object defaultValue;

   private EnumDisplayProperty(@NotNull Class<?> valueType, @NotNull Object defaultValue) {
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
   private static EnumDisplayProperty[] $values() {
      return new EnumDisplayProperty[]{VISIBILITY, GLOWING, GLOWING_COLOR, TRANSFORM_INTERPOLATION_DELAY, TRANSFORM_INTERPOLATION_DURATION, VIEW_RANGE, BLOCK_DATA, ITEM_STACK, ITEM_DISPLAY_SLOT, TEXT, TEXT_LINE_WIDTH, TEXT_OPACITY, TEXT_SHADOWED, TEXT_CAN_SEE_THROUGH, TEXT_DEFAULT_BACKGROUND, TEXT_ALIGNMENT, LEFT_ROTATION, POSITION, TRANSFORMATION};
   }
}
