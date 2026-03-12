package com.nisovin.shopkeepers.config.lib.value;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.lib.value.types.BooleanValue;
import com.nisovin.shopkeepers.config.lib.value.types.DoubleValue;
import com.nisovin.shopkeepers.config.lib.value.types.EntityTypeValue;
import com.nisovin.shopkeepers.config.lib.value.types.EnumValue;
import com.nisovin.shopkeepers.config.lib.value.types.FloatValue;
import com.nisovin.shopkeepers.config.lib.value.types.IntegerValue;
import com.nisovin.shopkeepers.config.lib.value.types.ItemDataValue;
import com.nisovin.shopkeepers.config.lib.value.types.ListValue;
import com.nisovin.shopkeepers.config.lib.value.types.LongValue;
import com.nisovin.shopkeepers.config.lib.value.types.MaterialValue;
import com.nisovin.shopkeepers.config.lib.value.types.SoundEffectValue;
import com.nisovin.shopkeepers.config.lib.value.types.StringValue;
import com.nisovin.shopkeepers.config.lib.value.types.TextValue;
import com.nisovin.shopkeepers.config.lib.value.types.TrileanValue;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.SoundEffect;
import com.nisovin.shopkeepers.util.inventory.ItemData;
import com.nisovin.shopkeepers.util.java.Trilean;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DefaultValueTypes {
   private static final ValueTypeRegistry registry = new ValueTypeRegistry();

   @Nullable
   public static <T> ValueType<T> get(Type type) {
      return registry.getValueType(type);
   }

   private DefaultValueTypes() {
   }

   static {
      registry.register((Type)String.class, StringValue.INSTANCE);
      registry.register((Type)Boolean.class, BooleanValue.INSTANCE);
      registry.register((Type)Boolean.TYPE, BooleanValue.INSTANCE);
      registry.register((Type)Integer.class, IntegerValue.INSTANCE);
      registry.register((Type)Integer.TYPE, IntegerValue.INSTANCE);
      registry.register((Type)Double.class, DoubleValue.INSTANCE);
      registry.register((Type)Double.TYPE, DoubleValue.INSTANCE);
      registry.register((Type)Float.class, FloatValue.INSTANCE);
      registry.register((Type)Float.TYPE, FloatValue.INSTANCE);
      registry.register((Type)Long.class, LongValue.INSTANCE);
      registry.register((Type)Long.TYPE, LongValue.INSTANCE);
      registry.register((Type)Trilean.class, TrileanValue.INSTANCE);
      registry.register((Type)Text.class, TextValue.INSTANCE);
      registry.register((Type)Material.class, MaterialValue.INSTANCE);
      registry.register((Type)ItemData.class, ItemDataValue.INSTANCE);
      registry.register((Type)SoundEffect.class, SoundEffectValue.INSTANCE);
      registry.register((Type)EntityType.class, EntityTypeValue.INSTANCE);
      registry.register(ValueTypeProviders.forTypePattern(TypePatterns.forBaseType(Enum.class), new Function<Type, ValueType<?>>() {
         public ValueType<?> apply(@Nullable Type type) {
            assert type instanceof Class && Enum.class.isAssignableFrom((Class)type);

            Class<? extends Enum<?>> enumClass = (Class)Unsafe.castNonNull(type);
            return this.newEnumValueType(enumClass);
         }

         private <E extends Enum<E>> EnumValue<E> newEnumValueType(Class<? extends Enum<?>> enumClass) {
            return new EnumValue((Class)Unsafe.cast(enumClass));
         }
      }));
      registry.register(ValueTypeProviders.forTypePattern(TypePatterns.forClass(List.class), (type) -> {
         assert type instanceof ParameterizedType;

         Type elementType = ((ParameterizedType)type).getActualTypeArguments()[0];
         ValueType<?> elementValueType = get(elementType);
         if (elementValueType == null) {
            throw new IllegalArgumentException("Unsupported element type: " + elementType.getTypeName());
         } else {
            return new ListValue(elementValueType);
         }
      }));
   }
}
