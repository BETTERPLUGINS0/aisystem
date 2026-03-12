package fr.xephi.authme.libs.ch.jalu.configme.properties;

import fr.xephi.authme.libs.ch.jalu.configme.properties.inlinearray.InlineArrayConverter;
import fr.xephi.authme.libs.ch.jalu.configme.properties.types.PropertyType;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public abstract class PropertyBuilder<K, T, B extends PropertyBuilder<K, T, B>> {
   private String path;
   private T defaultValue;
   private PropertyType<K> type;

   public PropertyBuilder(PropertyType<K> type) {
      this.type = type;
   }

   public B path(String path) {
      this.path = path;
      return this;
   }

   public B defaultValue(T defaultValue) {
      this.defaultValue = defaultValue;
      return this;
   }

   public abstract Property<T> build();

   protected final String getPath() {
      return this.path;
   }

   protected final T getDefaultValue() {
      return this.defaultValue;
   }

   protected final PropertyType<K> getType() {
      return this.type;
   }

   @FunctionalInterface
   public interface CreateFunction<K, T> {
      Property<T> apply(String var1, T var2, PropertyType<K> var3);
   }

   public static class SetPropertyBuilder<T> extends PropertyBuilder<T, Set<T>, PropertyBuilder.SetPropertyBuilder<T>> {
      public SetPropertyBuilder(PropertyType<T> type) {
         super(type);
      }

      public PropertyBuilder.SetPropertyBuilder<T> defaultValue(T... defaultValue) {
         Set<T> defaultSet = (Set)Arrays.stream(defaultValue).collect(Collectors.toCollection(LinkedHashSet::new));
         return (PropertyBuilder.SetPropertyBuilder)super.defaultValue(defaultSet);
      }

      public Property<Set<T>> build() {
         return new SetProperty(this.getPath(), this.getType(), (Set)this.getDefaultValue());
      }
   }

   public static class ListPropertyBuilder<T> extends PropertyBuilder<T, List<T>, PropertyBuilder.ListPropertyBuilder<T>> {
      public ListPropertyBuilder(PropertyType<T> type) {
         super(type);
      }

      public PropertyBuilder.ListPropertyBuilder<T> defaultValue(T... defaultValue) {
         return (PropertyBuilder.ListPropertyBuilder)super.defaultValue(Arrays.asList(defaultValue));
      }

      public Property<List<T>> build() {
         return new ListProperty(this.getPath(), this.getType(), (List)this.getDefaultValue());
      }
   }

   public static class InlineArrayPropertyBuilder<T> extends PropertyBuilder<T, T[], PropertyBuilder.InlineArrayPropertyBuilder<T>> {
      private InlineArrayConverter<T> inlineConverter;

      public InlineArrayPropertyBuilder(InlineArrayConverter<T> inlineConverter) {
         super((PropertyType)null);
         this.inlineConverter = inlineConverter;
      }

      public PropertyBuilder.InlineArrayPropertyBuilder<T> defaultValue(T... defaultValue) {
         return (PropertyBuilder.InlineArrayPropertyBuilder)super.defaultValue(defaultValue);
      }

      public Property<T[]> build() {
         return new InlineArrayProperty(this.getPath(), (Object[])this.getDefaultValue(), this.inlineConverter);
      }
   }

   public static class ArrayPropertyBuilder<T> extends PropertyBuilder<T, T[], PropertyBuilder.ArrayPropertyBuilder<T>> {
      private final IntFunction<T[]> arrayProducer;

      public ArrayPropertyBuilder(PropertyType<T> type, IntFunction<T[]> arrayProducer) {
         super(type);
         this.arrayProducer = arrayProducer;
      }

      public PropertyBuilder.ArrayPropertyBuilder<T> defaultValue(T... defaultValue) {
         return (PropertyBuilder.ArrayPropertyBuilder)super.defaultValue(defaultValue);
      }

      public Property<T[]> build() {
         return new ArrayProperty(this.getPath(), (Object[])this.getDefaultValue(), this.getType(), this.arrayProducer);
      }
   }

   public static class TypeBasedPropertyBuilder<T> extends PropertyBuilder<T, T, PropertyBuilder.TypeBasedPropertyBuilder<T>> {
      private PropertyBuilder.CreateFunction<T, T> createFunction = TypeBasedProperty::new;

      public TypeBasedPropertyBuilder(PropertyType<T> type) {
         super(type);
      }

      public PropertyBuilder.TypeBasedPropertyBuilder<T> createFunction(PropertyBuilder.CreateFunction<T, T> createFunction) {
         this.createFunction = createFunction;
         return this;
      }

      public Property<T> build() {
         return this.createFunction.apply(this.getPath(), this.getDefaultValue(), this.getType());
      }
   }

   public static class MapPropertyBuilder<T> extends PropertyBuilder<T, Map<String, T>, PropertyBuilder.MapPropertyBuilder<T>> {
      public MapPropertyBuilder(PropertyType<T> type) {
         super(type);
         this.defaultValue(new LinkedHashMap());
      }

      public PropertyBuilder.MapPropertyBuilder<T> defaultEntry(String key, T value) {
         ((Map)this.getDefaultValue()).put(key, value);
         return this;
      }

      public MapProperty<T> build() {
         return new MapProperty(this.getPath(), (Map)this.getDefaultValue(), this.getType());
      }
   }
}
