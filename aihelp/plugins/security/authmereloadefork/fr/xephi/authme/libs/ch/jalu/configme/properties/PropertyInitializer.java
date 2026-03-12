package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.inlinearray.InlineArrayConverter;
import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PropertyType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.regex.Pattern;

public class PropertyInitializer {
   protected PropertyInitializer() {
   }

   public static Property<Boolean> newProperty(String path, boolean defaultValue) {
      return new BooleanProperty(path, defaultValue);
   }

   public static Property<Short> newProperty(String path, short defaultValue) {
      return new ShortProperty(path, defaultValue);
   }

   public static Property<Integer> newProperty(String path, int defaultValue) {
      return new IntegerProperty(path, defaultValue);
   }

   public static Property<Long> newProperty(String path, long defaultValue) {
      return new LongProperty(path, defaultValue);
   }

   public static Property<Double> newProperty(String path, double defaultValue) {
      return new DoubleProperty(path, defaultValue);
   }

   public static Property<Float> newProperty(String path, float defaultValue) {
      return new FloatProperty(path, defaultValue);
   }

   public static Property<String> newProperty(String path, String defaultValue) {
      return new StringProperty(path, defaultValue);
   }

   public static <E extends Enum<E>> Property<E> newProperty(Class<E> clazz, String path, E defaultValue) {
      return new EnumProperty(clazz, path, defaultValue);
   }

   public static RegexProperty newRegexProperty(String path, String defaultRegexValue) {
      return new RegexProperty(path, defaultRegexValue);
   }

   public static RegexProperty newRegexProperty(String path, Pattern defaultRegexValue) {
      return new RegexProperty(path, defaultRegexValue);
   }

   public static Property<List<String>> newListProperty(String path, String... defaultValues) {
      return new StringListProperty(path, defaultValues);
   }

   public static Property<List<String>> newListProperty(String path, List<String> defaultValues) {
      return new StringListProperty(path, defaultValues);
   }

   public static Property<Set<String>> newSetProperty(String path, String... defaultValues) {
      return new StringSetProperty(path, defaultValues);
   }

   public static Property<Set<String>> newSetProperty(String path, Set<String> defaultValues) {
      return new StringSetProperty(path, defaultValues);
   }

   public static Property<Set<String>> newLowercaseStringSetProperty(String path, String... defaultValues) {
      return new LowercaseStringSetProperty(path, defaultValues);
   }

   public static Property<Set<String>> newLowercaseStringSetProperty(String path, Collection<String> defaultValues) {
      return new LowercaseStringSetProperty(path, defaultValues);
   }

   public static <B> Property<B> newBeanProperty(Class<B> beanClass, String path, B defaultValue) {
      return new BeanProperty(beanClass, path, defaultValue);
   }

   public static <T> PropertyBuilder.TypeBasedPropertyBuilder<T> typeBasedProperty(PropertyType<T> type) {
      return new PropertyBuilder.TypeBasedPropertyBuilder(type);
   }

   public static <T> PropertyBuilder.ListPropertyBuilder<T> listProperty(PropertyType<T> type) {
      return new PropertyBuilder.ListPropertyBuilder(type);
   }

   public static <T> PropertyBuilder.SetPropertyBuilder<T> setProperty(PropertyType<T> type) {
      return new PropertyBuilder.SetPropertyBuilder(type);
   }

   public static <T> PropertyBuilder.MapPropertyBuilder<T> mapProperty(PropertyType<T> type) {
      return new PropertyBuilder.MapPropertyBuilder(type);
   }

   public static <T> PropertyBuilder.ArrayPropertyBuilder<T> arrayProperty(PropertyType<T> type, IntFunction<T[]> arrayProducer) {
      return new PropertyBuilder.ArrayPropertyBuilder(type, arrayProducer);
   }

   public static <T> PropertyBuilder.InlineArrayPropertyBuilder<T> inlineArrayProperty(InlineArrayConverter<T> inlineConverter) {
      return new PropertyBuilder.InlineArrayPropertyBuilder(inlineConverter);
   }

   public static Property<Optional<Boolean>> optionalBooleanProperty(String path) {
      return new OptionalProperty(new BooleanProperty(path, false));
   }

   public static Property<Optional<Integer>> optionalIntegerProperty(String path) {
      return new OptionalProperty(new IntegerProperty(path, 0));
   }

   public static Property<Optional<String>> optionalStringProperty(String path) {
      return new OptionalProperty(new StringProperty(path, ""));
   }

   public static <E extends Enum<E>> Property<Optional<E>> optionalEnumProperty(Class<E> clazz, String path) {
      return new OptionalProperty(new EnumProperty(clazz, path, ((Enum[])clazz.getEnumConstants())[0]));
   }
}
