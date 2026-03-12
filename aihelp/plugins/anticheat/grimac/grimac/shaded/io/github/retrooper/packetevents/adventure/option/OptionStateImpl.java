package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.value.ValueSource;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;

final class OptionStateImpl implements OptionState {
   private final OptionSchema schema;
   private final IdentityHashMap<Option<?>, Object> values;

   OptionStateImpl(final OptionSchema schema, final IdentityHashMap<Option<?>, Object> values) {
      this.schema = schema;
      this.values = new IdentityHashMap(values);
   }

   public OptionSchema schema() {
      return this.schema;
   }

   public boolean has(final Option<?> option) {
      return this.values.containsKey(Objects.requireNonNull(option, "flag"));
   }

   @Nullable
   public <V> V value(final Option<V> option) {
      V value = option.valueType().type().cast(this.values.get(Objects.requireNonNull(option, "flag")));
      return value == null ? option.defaultValue() : value;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         OptionStateImpl that = (OptionStateImpl)other;
         return Objects.equals(this.values, that.values);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.values});
   }

   public String toString() {
      return this.getClass().getSimpleName() + "{values=" + this.values + '}';
   }

   static final class VersionedBuilderImpl implements OptionState.VersionedBuilder {
      private final OptionSchema schema;
      private final Map<Integer, OptionStateImpl.BuilderImpl> builders = new TreeMap();

      VersionedBuilderImpl(final OptionSchema schema) {
         this.schema = schema;
      }

      public OptionState.Versioned build() {
         if (this.builders.isEmpty()) {
            return new OptionStateImpl.VersionedImpl(this.schema, Collections.emptySortedMap(), 0, this.schema.emptyState());
         } else {
            SortedMap<Integer, OptionState> built = new TreeMap();
            Iterator var2 = this.builders.entrySet().iterator();

            while(var2.hasNext()) {
               Entry<Integer, OptionStateImpl.BuilderImpl> entry = (Entry)var2.next();
               built.put((Integer)entry.getKey(), ((OptionStateImpl.BuilderImpl)entry.getValue()).build());
            }

            return new OptionStateImpl.VersionedImpl(this.schema, built, (Integer)built.lastKey(), OptionStateImpl.VersionedImpl.flattened(this.schema, built, (Integer)built.lastKey()));
         }
      }

      public OptionState.VersionedBuilder version(final int version, final Consumer<OptionState.Builder> versionBuilder) {
         ((Consumer)Objects.requireNonNull(versionBuilder, "versionBuilder")).accept((OptionState.Builder)this.builders.computeIfAbsent(version, ($) -> {
            return new OptionStateImpl.BuilderImpl(this.schema);
         }));
         return this;
      }
   }

   static final class BuilderImpl implements OptionState.Builder {
      private final OptionSchema schema;
      private final IdentityHashMap<Option<?>, Object> values = new IdentityHashMap();

      BuilderImpl(final OptionSchema schema) {
         this.schema = schema;
      }

      public OptionState build() {
         return (OptionState)(this.values.isEmpty() ? this.schema.emptyState() : new OptionStateImpl(this.schema, this.values));
      }

      public <V> OptionState.Builder value(final Option<V> option, @Nullable final V value) {
         if (!this.schema.has((Option)Objects.requireNonNull(option, "option"))) {
            throw new IllegalStateException("Option '" + option.id() + "' was not present in active schema");
         } else {
            if (value == null) {
               this.values.remove(option);
            } else {
               this.values.put(option, value);
            }

            return this;
         }
      }

      private void putAll(final Map<Option<?>, Object> values) {
         Iterator var2 = values.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<Option<?>, Object> entry = (Entry)var2.next();
            if (!this.schema.has((Option)entry.getKey())) {
               throw new IllegalStateException("Option '" + ((Option)entry.getKey()).id() + "' was not present in active schema");
            }

            this.values.put((Option)entry.getKey(), entry.getValue());
         }

      }

      public OptionState.Builder values(final OptionState existing) {
         if (existing instanceof OptionStateImpl) {
            this.putAll(((OptionStateImpl)existing).values);
         } else {
            if (!(existing instanceof OptionStateImpl.VersionedImpl)) {
               throw new IllegalArgumentException("existing set " + existing + " is of an unknown implementation type");
            }

            this.putAll(((OptionStateImpl)((OptionStateImpl.VersionedImpl)existing).filtered).values);
         }

         return this;
      }

      public OptionState.Builder values(final ValueSource source) {
         Iterator var2 = this.schema.knownOptions().iterator();

         while(var2.hasNext()) {
            Option<?> opt = (Option)var2.next();
            Object value = source.value(opt);
            if (value != null) {
               this.values.put(opt, value);
            }
         }

         return this;
      }
   }

   static final class VersionedImpl implements OptionState.Versioned {
      private final OptionSchema schema;
      private final SortedMap<Integer, OptionState> sets;
      private final int targetVersion;
      private final OptionState filtered;

      VersionedImpl(final OptionSchema schema, final SortedMap<Integer, OptionState> sets, final int targetVersion, final OptionState filtered) {
         this.schema = schema;
         this.sets = sets;
         this.targetVersion = targetVersion;
         this.filtered = filtered;
      }

      public OptionSchema schema() {
         return this.schema;
      }

      public boolean has(final Option<?> option) {
         return this.filtered.has(option);
      }

      @Nullable
      public <V> V value(final Option<V> option) {
         return this.filtered.value(option);
      }

      public Map<Integer, OptionState> childStates() {
         return Collections.unmodifiableSortedMap(this.sets.headMap(this.targetVersion + 1));
      }

      public OptionState.Versioned at(final int version) {
         return new OptionStateImpl.VersionedImpl(this.schema, this.sets, version, flattened(this.schema, this.sets, version));
      }

      public static OptionState flattened(final OptionSchema schema, final SortedMap<Integer, OptionState> versions, final int targetVersion) {
         Map<Integer, OptionState> applicable = versions.headMap(targetVersion + 1);
         OptionState.Builder builder = schema.stateBuilder();
         Iterator var5 = applicable.values().iterator();

         while(var5.hasNext()) {
            OptionState child = (OptionState)var5.next();
            builder.values(child);
         }

         return builder.build();
      }

      public boolean equals(@Nullable final Object other) {
         if (this == other) {
            return true;
         } else if (other != null && this.getClass() == other.getClass()) {
            OptionStateImpl.VersionedImpl that = (OptionStateImpl.VersionedImpl)other;
            return this.targetVersion == that.targetVersion && Objects.equals(this.schema, that.schema) && Objects.equals(this.sets, that.sets) && Objects.equals(this.filtered, that.filtered);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.schema, this.sets, this.targetVersion, this.filtered});
      }

      public String toString() {
         return this.getClass().getSimpleName() + "{schema=" + this.schema + ", sets=" + this.sets + ", targetVersion=" + this.targetVersion + ", filtered=" + this.filtered + '}';
      }
   }
}
