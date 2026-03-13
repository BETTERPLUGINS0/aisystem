package ch.jalu.configme.properties;

import ch.jalu.configme.properties.inlinearray.InlineArrayConverter;
import ch.jalu.configme.properties.types.PropertyType;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class PropertyInitializer {
   protected PropertyInitializer() {
   }

   @NotNull
   public static BooleanProperty newProperty(@NotNull String path, boolean defaultValue) {
      return new BooleanProperty(path, defaultValue);
   }

   @NotNull
   public static ShortProperty newProperty(@NotNull String path, short defaultValue) {
      return new ShortProperty(path, defaultValue);
   }

   @NotNull
   public static IntegerProperty newProperty(@NotNull String path, int defaultValue) {
      return new IntegerProperty(path, defaultValue);
   }

   @NotNull
   public static LongProperty newProperty(@NotNull String path, long defaultValue) {
      return new LongProperty(path, defaultValue);
   }

   @NotNull
   public static FloatProperty newProperty(@NotNull String path, float defaultValue) {
      return new FloatProperty(path, defaultValue);
   }

   @NotNull
   public static DoubleProperty newProperty(@NotNull String path, double defaultValue) {
      return new DoubleProperty(path, defaultValue);
   }

   @NotNull
   public static StringProperty newProperty(@NotNull String path, @NotNull String defaultValue) {
      return new StringProperty(path, defaultValue);
   }

   @NotNull
   public static <E extends Enum<E>> EnumProperty<E> newProperty(@NotNull Class<E> clazz, @NotNull String path, @NotNull E defaultValue) {
      return new EnumProperty(clazz, path, defaultValue);
   }

   @NotNull
   public static RegexProperty newRegexProperty(@NotNull String path, @NotNull String defaultRegexValue) {
      return new RegexProperty(path, defaultRegexValue);
   }

   @NotNull
   public static RegexProperty newRegexProperty(@NotNull String path, @NotNull Pattern defaultRegexValue) {
      return new RegexProperty(path, defaultRegexValue);
   }

   @NotNull
   public static StringListProperty newListProperty(@NotNull String path, @NotNull String... defaultValues) {
      return new StringListProperty(path, defaultValues);
   }

   @NotNull
   public static StringListProperty newListProperty(@NotNull String path, @NotNull List<String> defaultValues) {
      return new StringListProperty(path, defaultValues);
   }

   @NotNull
   public static StringSetProperty newSetProperty(@NotNull String path, @NotNull String... defaultValues) {
      return new StringSetProperty(path, defaultValues);
   }

   @NotNull
   public static StringSetProperty newSetProperty(@NotNull String path, @NotNull Set<String> defaultValues) {
      return new StringSetProperty(path, defaultValues);
   }

   @NotNull
   public static LowercaseStringSetProperty newLowercaseStringSetProperty(@NotNull String path, @NotNull String... defaultValues) {
      return new LowercaseStringSetProperty(path, defaultValues);
   }

   @NotNull
   public static LowercaseStringSetProperty newLowercaseStringSetProperty(@NotNull String path, @NotNull Collection<String> defaultValues) {
      return new LowercaseStringSetProperty(path, defaultValues);
   }

   @NotNull
   public static <B> BeanProperty<B> newBeanProperty(@NotNull Class<B> beanClass, @NotNull String path, @NotNull B defaultValue) {
      return new BeanProperty(beanClass, path, defaultValue);
   }

   @NotNull
   public static <T> PropertyBuilder.TypeBasedPropertyBuilder<T> typeBasedProperty(@NotNull PropertyType<T> type) {
      return new PropertyBuilder.TypeBasedPropertyBuilder(type);
   }

   @NotNull
   public static <T> PropertyBuilder.ListPropertyBuilder<T> listProperty(@NotNull PropertyType<T> type) {
      return new PropertyBuilder.ListPropertyBuilder(type);
   }

   @NotNull
   public static <T> PropertyBuilder.SetPropertyBuilder<T> setProperty(@NotNull PropertyType<T> type) {
      return new PropertyBuilder.SetPropertyBuilder(type);
   }

   @NotNull
   public static <T> PropertyBuilder.MapPropertyBuilder<T> mapProperty(@NotNull PropertyType<T> type) {
      return new PropertyBuilder.MapPropertyBuilder(type);
   }

   @NotNull
   public static <T> PropertyBuilder.ArrayPropertyBuilder<T> arrayProperty(@NotNull PropertyType<T> type, @NotNull IntFunction<T[]> arrayProducer) {
      return new PropertyBuilder.ArrayPropertyBuilder(type, arrayProducer);
   }

   @NotNull
   public static <T> PropertyBuilder.InlineArrayPropertyBuilder<T> inlineArrayProperty(@NotNull InlineArrayConverter<T> inlineConverter) {
      return new PropertyBuilder.InlineArrayPropertyBuilder(inlineConverter);
   }

   @NotNull
   public static OptionalProperty<Boolean> optionalBooleanProperty(@NotNull String path) {
      return new OptionalProperty(new BooleanProperty(path, false));
   }

   @NotNull
   public static OptionalProperty<Short> optionalShortProperty(@NotNull String path) {
      return new OptionalProperty(new ShortProperty(path, Short.valueOf((short)0)));
   }

   @NotNull
   public static OptionalProperty<Integer> optionalIntegerProperty(@NotNull String path) {
      return new OptionalProperty(new IntegerProperty(path, 0));
   }

   @NotNull
   public static OptionalProperty<Long> optionalLongProperty(@NotNull String path) {
      return new OptionalProperty(new LongProperty(path, 0L));
   }

   @NotNull
   public static OptionalProperty<Float> optionalFloatProperty(@NotNull String path) {
      return new OptionalProperty(new FloatProperty(path, 0.0F));
   }

   @NotNull
   public static OptionalProperty<Double> optionalDoubleProperty(@NotNull String path) {
      return new OptionalProperty(new DoubleProperty(path, 0.0D));
   }

   @NotNull
   public static OptionalProperty<String> optionalStringProperty(@NotNull String path) {
      return new OptionalProperty(new StringProperty(path, ""));
   }

   @NotNull
   public static <E extends Enum<E>> OptionalProperty<E> optionalEnumProperty(@NotNull Class<E> clazz, @NotNull String path) {
      return new OptionalProperty(new EnumProperty(clazz, path, ((Enum[])clazz.getEnumConstants())[0]));
   }

   @NotNull
   public static OptionalProperty<Pattern> optionalRegexProperty(@NotNull String path) {
      return new OptionalProperty(new RegexProperty(path, ""));
   }

   @NotNull
   public static OptionalProperty<List<String>> optionalListProperty(@NotNull String path) {
      return new OptionalProperty(new StringListProperty(path, new String[0]));
   }

   @NotNull
   public static OptionalProperty<Set<String>> optionalSetProperty(@NotNull String path) {
      return new OptionalProperty(new StringSetProperty(path, new String[0]));
   }
}
