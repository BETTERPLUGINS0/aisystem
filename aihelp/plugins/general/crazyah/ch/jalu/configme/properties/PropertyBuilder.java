package ch.jalu.configme.properties;

import ch.jalu.configme.properties.inlinearray.InlineArrayConverter;
import ch.jalu.configme.properties.types.PropertyType;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PropertyBuilder<K, T, B extends PropertyBuilder<K, T, B>> {
   private String path;
   private T defaultValue;
   private final PropertyType<K> type;

   public PropertyBuilder(@Nullable PropertyType<K> type) {
      this.type = type;
   }

   @NotNull
   public B path(@NotNull String path) {
      this.path = path;
      return this;
   }

   @NotNull
   public B defaultValue(@NotNull T defaultValue) {
      this.defaultValue = defaultValue;
      return this;
   }

   @NotNull
   public abstract Property<T> build();

   @Nullable
   protected final String getPath() {
      return this.path;
   }

   @Nullable
   protected final T getDefaultValue() {
      return this.defaultValue;
   }

   @Nullable
   protected final PropertyType<K> getType() {
      return this.type;
   }

   @FunctionalInterface
   public interface CreateFunction<K, T> {
      @NotNull
      Property<T> apply(@NotNull String var1, @NotNull T var2, @NotNull PropertyType<K> var3);
   }

   public static class SetPropertyBuilder<T> extends PropertyBuilder<T, Set<T>, PropertyBuilder.SetPropertyBuilder<T>> {
      public SetPropertyBuilder(@NotNull PropertyType<T> type) {
         super(type);
      }

      @NotNull
      public PropertyBuilder.SetPropertyBuilder<T> defaultValue(@NotNull T... defaultValue) {
         Set<T> defaultSet = (Set)Arrays.stream(defaultValue).collect(Collectors.toCollection(LinkedHashSet::new));
         return (PropertyBuilder.SetPropertyBuilder)super.defaultValue(defaultSet);
      }

      @NotNull
      public Property<Set<T>> build() {
         return new SetProperty(this.getPath(), this.getType(), (Set)this.getDefaultValue());
      }
   }

   public static class ListPropertyBuilder<T> extends PropertyBuilder<T, List<T>, PropertyBuilder.ListPropertyBuilder<T>> {
      public ListPropertyBuilder(@NotNull PropertyType<T> type) {
         super(type);
      }

      @NotNull
      public PropertyBuilder.ListPropertyBuilder<T> defaultValue(@NotNull T... defaultValue) {
         return (PropertyBuilder.ListPropertyBuilder)super.defaultValue(Arrays.asList(defaultValue));
      }

      @NotNull
      public Property<List<T>> build() {
         return new ListProperty(this.getPath(), this.getType(), (List)this.getDefaultValue());
      }
   }

   public static class InlineArrayPropertyBuilder<T> extends PropertyBuilder<T, T[], PropertyBuilder.InlineArrayPropertyBuilder<T>> {
      private InlineArrayConverter<T> inlineConverter;

      public InlineArrayPropertyBuilder(@NotNull InlineArrayConverter<T> inlineConverter) {
         super((PropertyType)null);
         this.inlineConverter = inlineConverter;
      }

      @NotNull
      public PropertyBuilder.InlineArrayPropertyBuilder<T> defaultValue(@NotNull T... defaultValue) {
         return (PropertyBuilder.InlineArrayPropertyBuilder)super.defaultValue(defaultValue);
      }

      @NotNull
      public Property<T[]> build() {
         return new InlineArrayProperty(this.getPath(), (Object[])this.getDefaultValue(), this.inlineConverter);
      }
   }

   public static class ArrayPropertyBuilder<T> extends PropertyBuilder<T, T[], PropertyBuilder.ArrayPropertyBuilder<T>> {
      private final IntFunction<T[]> arrayProducer;

      public ArrayPropertyBuilder(@NotNull PropertyType<T> type, @NotNull IntFunction<T[]> arrayProducer) {
         super(type);
         this.arrayProducer = arrayProducer;
      }

      @NotNull
      public PropertyBuilder.ArrayPropertyBuilder<T> defaultValue(@NotNull T... defaultValue) {
         return (PropertyBuilder.ArrayPropertyBuilder)super.defaultValue(defaultValue);
      }

      @NotNull
      public Property<T[]> build() {
         return new ArrayProperty(this.getPath(), (Object[])this.getDefaultValue(), this.getType(), this.arrayProducer);
      }
   }

   public static class TypeBasedPropertyBuilder<T> extends PropertyBuilder<T, T, PropertyBuilder.TypeBasedPropertyBuilder<T>> {
      private PropertyBuilder.CreateFunction<T, T> createFunction = TypeBasedProperty::new;

      public TypeBasedPropertyBuilder(@NotNull PropertyType<T> type) {
         super(type);
      }

      @NotNull
      public PropertyBuilder.TypeBasedPropertyBuilder<T> createFunction(@NotNull PropertyBuilder.CreateFunction<T, T> createFunction) {
         this.createFunction = createFunction;
         return this;
      }

      @NotNull
      public Property<T> build() {
         return this.createFunction.apply(this.getPath(), this.getDefaultValue(), this.getType());
      }
   }

   public static class MapPropertyBuilder<T> extends PropertyBuilder<T, Map<String, T>, PropertyBuilder.MapPropertyBuilder<T>> {
      public MapPropertyBuilder(@NotNull PropertyType<T> type) {
         super(type);
         this.defaultValue(new LinkedHashMap());
      }

      @NotNull
      public PropertyBuilder.MapPropertyBuilder<T> defaultEntry(@NotNull String key, @NotNull T value) {
         ((Map)this.getDefaultValue()).put(key, value);
         return this;
      }

      @NotNull
      public MapProperty<T> build() {
         return new MapProperty(this.getPath(), (Map)this.getDefaultValue(), this.getType());
      }
   }
}
