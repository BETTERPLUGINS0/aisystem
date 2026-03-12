package ac.grim.grimac.shaded.kyori.option;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import java.util.Set;
import org.jspecify.annotations.Nullable;

@ApiStatus.NonExtendable
public interface OptionSchema {
   static OptionSchema.Mutable globalSchema() {
      return OptionSchemaImpl.Instances.GLOBAL;
   }

   static OptionSchema.Mutable childSchema(final OptionSchema schema) {
      OptionSchemaImpl impl;
      if (schema instanceof OptionSchemaImpl.MutableImpl) {
         impl = (OptionSchemaImpl)((OptionSchema.Mutable)schema).frozenView();
      } else {
         impl = (OptionSchemaImpl)schema;
      }

      return new OptionSchemaImpl((OptionSchemaImpl)Objects.requireNonNull(impl, "impl")).new MutableImpl();
   }

   static OptionSchema.Mutable emptySchema() {
      return new OptionSchemaImpl((OptionSchemaImpl)null).new MutableImpl();
   }

   Set<Option<?>> knownOptions();

   boolean has(final Option<?> option);

   OptionState.Builder stateBuilder();

   OptionState.VersionedBuilder versionedStateBuilder();

   OptionState emptyState();

   @ApiStatus.NonExtendable
   public interface Mutable extends OptionSchema {
      Option<String> stringOption(final String id, @Nullable final String defaultValue);

      Option<Boolean> booleanOption(final String id, final boolean defaultValue);

      Option<Integer> intOption(final String id, final int defaultValue);

      Option<Double> doubleOption(final String id, final double defaultValue);

      <E extends Enum<E>> Option<E> enumOption(final String id, final Class<E> enumClazz, @Nullable final E defaultValue);

      OptionSchema frozenView();
   }
}
