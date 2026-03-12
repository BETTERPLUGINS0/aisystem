package ac.grim.grimac.shaded.kyori.option;

import ac.grim.grimac.shaded.kyori.option.value.ValueType;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jspecify.annotations.Nullable;

final class OptionSchemaImpl implements OptionSchema {
   final OptionState emptyState;
   final ConcurrentMap<String, Option<?>> options = new ConcurrentHashMap();

   OptionSchemaImpl(@Nullable final OptionSchemaImpl parent) {
      if (parent != null) {
         this.options.putAll(parent.options);
      }

      this.emptyState = new OptionStateImpl(this, new IdentityHashMap());
   }

   public Set<Option<?>> knownOptions() {
      return Collections.unmodifiableSet(new HashSet(this.options.values()));
   }

   public boolean has(final Option<?> option) {
      Option<?> own = (Option)this.options.get(option.id());
      return own != null && own.equals(option);
   }

   public OptionState.Builder stateBuilder() {
      return new OptionStateImpl.BuilderImpl(this);
   }

   public OptionState.VersionedBuilder versionedStateBuilder() {
      return new OptionStateImpl.VersionedBuilderImpl(this);
   }

   public OptionState emptyState() {
      return this.emptyState;
   }

   public String toString() {
      return "OptionSchemaImpl{options=" + this.options + '}';
   }

   final class MutableImpl implements OptionSchema.Mutable {
      <T> Option<T> register(final String id, final ValueType<T> type, @Nullable final T defaultValue) {
         Option<T> ret = new OptionImpl((String)Objects.requireNonNull(id, "id"), (ValueType)Objects.requireNonNull(type, "type"), defaultValue);
         if (OptionSchemaImpl.this.options.putIfAbsent(id, ret) != null) {
            throw new IllegalStateException("Key " + id + " has already been used. Option keys must be unique within a schema.");
         } else {
            return ret;
         }
      }

      public Option<String> stringOption(final String id, @Nullable final String defaultValue) {
         return this.register(id, ValueType.stringType(), defaultValue);
      }

      public Option<Boolean> booleanOption(final String id, final boolean defaultValue) {
         return this.register(id, ValueType.booleanType(), defaultValue);
      }

      public Option<Integer> intOption(final String id, final int defaultValue) {
         return this.register(id, ValueType.integerType(), defaultValue);
      }

      public Option<Double> doubleOption(final String id, final double defaultValue) {
         return this.register(id, ValueType.doubleType(), defaultValue);
      }

      public <E extends Enum<E>> Option<E> enumOption(final String id, final Class<E> enumClazz, @Nullable final E defaultValue) {
         return this.register(id, ValueType.enumType(enumClazz), defaultValue);
      }

      public OptionSchema frozenView() {
         return OptionSchemaImpl.this;
      }

      public Set<Option<?>> knownOptions() {
         return OptionSchemaImpl.this.knownOptions();
      }

      public boolean has(final Option<?> option) {
         return OptionSchemaImpl.this.has(option);
      }

      public OptionState.Builder stateBuilder() {
         return OptionSchemaImpl.this.stateBuilder();
      }

      public OptionState.VersionedBuilder versionedStateBuilder() {
         return OptionSchemaImpl.this.versionedStateBuilder();
      }

      public OptionState emptyState() {
         return OptionSchemaImpl.this.emptyState();
      }

      public String toString() {
         return "MutableImpl{schema=" + OptionSchemaImpl.this + "}";
      }
   }

   static final class Instances {
      static OptionSchemaImpl.MutableImpl GLOBAL = new OptionSchemaImpl((OptionSchemaImpl)null).new MutableImpl();
   }
}
