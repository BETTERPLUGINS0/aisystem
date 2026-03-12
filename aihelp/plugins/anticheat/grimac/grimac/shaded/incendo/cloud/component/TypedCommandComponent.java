package ac.grim.grimac.shaded.incendo.cloud.component;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.component.preprocessor.ComponentPreprocessor;
import ac.grim.grimac.shaded.incendo.cloud.description.Description;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKeyHolder;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import java.util.Collection;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.STABLE
)
public final class TypedCommandComponent<C, T> extends CommandComponent<C> implements CloudKeyHolder<T> {
   TypedCommandComponent(@NonNull final String name, @NonNull final ArgumentParser<C, ?> parser, @NonNull final TypeToken<?> valueType, @NonNull final Description description, @NonNull final CommandComponent.ComponentType componentType, @Nullable final DefaultValue<C, ?> defaultValue, @NonNull final SuggestionProvider<C> suggestionProvider, @NonNull final Collection<ComponentPreprocessor<C>> componentPreprocessors) {
      super(name, parser, valueType, description, componentType, defaultValue, suggestionProvider, componentPreprocessors);
   }

   @NonNull
   public TypeToken<T> valueType() {
      return super.valueType();
   }

   @NonNull
   public ArgumentParser<C, T> parser() {
      return super.parser();
   }

   @Nullable
   public DefaultValue<C, T> defaultValue() {
      return super.defaultValue();
   }

   @NonNull
   public CloudKey<T> key() {
      return CloudKey.of(this.name(), this.valueType());
   }
}
