package ac.grim.grimac.shaded.incendo.cloud.component;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.component.preprocessor.ComponentPreprocessor;
import ac.grim.grimac.shaded.incendo.cloud.component.preprocessor.PreprocessorHolder;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.description.Describable;
import ac.grim.grimac.shaded.incendo.cloud.description.Description;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserParameters;
import ac.grim.grimac.shaded.incendo.cloud.parser.flag.CommandFlagParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.LiteralParser;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;

@API(
   status = Status.STABLE
)
public class CommandComponent<C> implements Comparable<CommandComponent<C>>, PreprocessorHolder<C>, Describable {
   private final String name;
   private final ArgumentParser<C, ?> parser;
   private final Description description;
   private final CommandComponent.ComponentType componentType;
   private final DefaultValue<C, ?> defaultValue;
   private final TypeToken<?> valueType;
   private final SuggestionProvider<C> suggestionProvider;
   private final Collection<ComponentPreprocessor<C>> componentPreprocessors;

   @NonNull
   public static <C, T> CommandComponent.Builder<C, T> builder() {
      return new CommandComponent.Builder();
   }

   @NonNull
   public static <C, T> CommandComponent.Builder<C, T> builder(@NonNull final String name, @NonNull final ParserDescriptor<? super C, T> parserDescriptor) {
      return builder().name(name).parser(parserDescriptor);
   }

   @NonNull
   public static <C, T> CommandComponent.Builder<C, T> builder(@NonNull final CloudKey<T> name, @NonNull final ParserDescriptor<? super C, T> parserDescriptor) {
      return builder().key(name).parser(parserDescriptor);
   }

   @NonNull
   public static <C, T> CommandComponent.Builder<C, T> ofType(@NonNull final Class<T> clazz, @NonNull final String name) {
      return builder().valueType(clazz).name(name);
   }

   CommandComponent(@NonNull final String name, @NonNull final ArgumentParser<C, ?> parser, @NonNull final TypeToken<?> valueType, @NonNull final Description description, @NonNull final CommandComponent.ComponentType componentType, @Nullable final DefaultValue<C, ?> defaultValue, @NonNull final SuggestionProvider<C> suggestionProvider, @NonNull final Collection<ComponentPreprocessor<C>> componentPreprocessors) {
      this.name = name;
      this.parser = parser;
      this.valueType = valueType;
      this.componentType = componentType;
      this.description = description;
      this.defaultValue = defaultValue;
      this.suggestionProvider = suggestionProvider;
      this.componentPreprocessors = new ArrayList(componentPreprocessors);
   }

   @NonNull
   public TypeToken<?> valueType() {
      return this.valueType;
   }

   @NonNull
   public ArgumentParser<C, ?> parser() {
      return this.parser;
   }

   @NonNull
   public final String name() {
      return this.name;
   }

   @NonNull
   public final Collection<String> aliases() {
      return (Collection)(this.parser() instanceof LiteralParser ? ((LiteralParser)this.parser()).aliases() : Collections.emptyList());
   }

   @NonNull
   public final Collection<String> alternativeAliases() {
      return (Collection)(this.parser() instanceof LiteralParser ? ((LiteralParser)this.parser()).alternativeAliases() : Collections.emptyList());
   }

   @NonNull
   public final Description description() {
      return this.description;
   }

   public final boolean required() {
      return this.componentType.required();
   }

   public final boolean optional() {
      return this.componentType.optional();
   }

   @NonNull
   public final CommandComponent.ComponentType type() {
      return this.componentType;
   }

   @Nullable
   public DefaultValue<C, ?> defaultValue() {
      return this.defaultValue;
   }

   public final boolean hasDefaultValue() {
      return this.optional() && this.defaultValue() != null;
   }

   @NonNull
   public final SuggestionProvider<C> suggestionProvider() {
      return this.suggestionProvider;
   }

   @This
   @NonNull
   public final CommandComponent<C> addPreprocessor(@NonNull final ComponentPreprocessor<C> preprocessor) {
      this.componentPreprocessors.add((ComponentPreprocessor)Objects.requireNonNull(preprocessor, "preprocessor"));
      return this;
   }

   @NonNull
   public final ArgumentParseResult<Boolean> preprocess(@NonNull final CommandContext<C> context, @NonNull final CommandInput input) {
      Iterator var3 = this.componentPreprocessors.iterator();

      ArgumentParseResult result;
      do {
         if (!var3.hasNext()) {
            return ArgumentParseResult.success(true);
         }

         ComponentPreprocessor<C> preprocessor = (ComponentPreprocessor)var3.next();
         result = preprocessor.preprocess(context, input);
      } while(!result.failure().isPresent());

      return result;
   }

   @NonNull
   public final Collection<ComponentPreprocessor<C>> preprocessors() {
      return Collections.unmodifiableCollection(this.componentPreprocessors);
   }

   public final int hashCode() {
      return Objects.hash(new Object[]{this.name(), this.valueType()});
   }

   public final boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof CommandComponent)) {
         return false;
      } else {
         CommandComponent<?> that = (CommandComponent)o;
         return this.name().equals(that.name()) && this.valueType().equals(that.valueType());
      }
   }

   @NonNull
   public final String toString() {
      return String.format("%s{name=%s,type=%s,valueType=%s", this.getClass().getSimpleName(), this.name(), this.type(), this.valueType().getType().getTypeName());
   }

   public final int compareTo(@NonNull final CommandComponent<C> other) {
      if (this.type() == CommandComponent.ComponentType.LITERAL) {
         return other.type() == CommandComponent.ComponentType.LITERAL ? this.name().compareTo(other.name()) : -1;
      } else {
         return other.type() == CommandComponent.ComponentType.LITERAL ? 1 : 0;
      }
   }

   @API(
      status = Status.STABLE
   )
   public static class Builder<C, T> {
      private CommandManager<C> commandManager;
      private String name;
      private ArgumentParser<C, T> parser;
      private Description description = Description.empty();
      private boolean required = true;
      private DefaultValue<C, ?> defaultValue;
      private TypeToken<T> valueType;
      private SuggestionProvider<C> suggestionProvider;
      private final Collection<ComponentPreprocessor<C>> componentPreprocessors = new ArrayList();

      protected Builder() {
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> commandManager(@Nullable final CommandManager<C> commandManager) {
         this.commandManager = commandManager;
         return this;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> key(@NonNull final CloudKey<T> cloudKey) {
         return this.name(cloudKey.name()).valueType(cloudKey.type());
      }

      @MonotonicNonNull
      public String name() {
         return this.name;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> name(@NonNull final String name) {
         this.name = (String)Objects.requireNonNull(name, "name");
         return this;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> valueType(@NonNull final TypeToken<T> valueType) {
         this.valueType = (TypeToken)Objects.requireNonNull(valueType, "valueType");
         return this;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> valueType(@NonNull final Class<T> valueType) {
         return this.valueType(TypeToken.get(valueType));
      }

      @MonotonicNonNull
      public ParserDescriptor<C, T> parser() {
         return this.valueType != null && this.parser != null ? ParserDescriptor.of(this.parser, this.valueType) : null;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> parser(@NonNull final ParserDescriptor<? super C, T> parserDescriptor) {
         return this.parser(parserDescriptor.parser()).valueType(parserDescriptor.valueType());
      }

      @Nullable
      public DefaultValue<C, T> defaultValue() {
         return this.defaultValue == null ? null : this.defaultValue;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> defaultValue(@Nullable final DefaultValue<? super C, T> defaultValue) {
         this.defaultValue = defaultValue;
         return this;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> required(final boolean required) {
         this.required = required;
         return this;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> required() {
         return this.required(true);
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> optional() {
         return this.required(false);
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> optional(@Nullable final DefaultValue<? super C, T> defaultValue) {
         return this.optional().defaultValue(defaultValue);
      }

      @MonotonicNonNull
      public Description description() {
         return this.description;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> description(@NonNull final Description description) {
         this.description = (Description)Objects.requireNonNull(description, "description");
         return this;
      }

      @MonotonicNonNull
      public SuggestionProvider<C> suggestionProvider() {
         return this.suggestionProvider;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> suggestionProvider(@Nullable final SuggestionProvider<? super C> suggestionProvider) {
         this.suggestionProvider = suggestionProvider;
         return this;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> preprocessor(@NonNull final ComponentPreprocessor<? super C> preprocessor) {
         this.componentPreprocessors.add((ComponentPreprocessor)Objects.requireNonNull(preprocessor, "preprocessor"));
         return this;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> preprocessors(@NonNull final Collection<ComponentPreprocessor<C>> preprocessors) {
         this.componentPreprocessors.addAll(preprocessors);
         return this;
      }

      @This
      @NonNull
      public CommandComponent.Builder<C, T> parser(@NonNull final ArgumentParser<? super C, T> parser) {
         this.parser = (ArgumentParser)Objects.requireNonNull(parser, "parser");
         return this;
      }

      @NonNull
      public TypedCommandComponent<C, T> build() {
         ArgumentParser<C, T> parser = null;
         if (this.parser != null) {
            parser = this.parser;
         } else if (this.commandManager != null) {
            parser = (ArgumentParser)this.commandManager.parserRegistry().createParser(this.valueType, ParserParameters.empty()).orElse((Object)null);
         }

         if (parser == null) {
            parser = (ctx, input) -> {
               return ArgumentParseResult.failure(new UnsupportedOperationException("No parser was specified"));
            };
         }

         CommandComponent.ComponentType componentType;
         if (this.parser instanceof LiteralParser) {
            componentType = CommandComponent.ComponentType.LITERAL;
         } else if (this.parser instanceof CommandFlagParser) {
            componentType = CommandComponent.ComponentType.FLAG;
         } else if (this.required) {
            componentType = CommandComponent.ComponentType.REQUIRED_VARIABLE;
         } else {
            componentType = CommandComponent.ComponentType.OPTIONAL_VARIABLE;
         }

         SuggestionProvider suggestionProvider;
         if (this.suggestionProvider == null) {
            suggestionProvider = parser.suggestionProvider();
         } else {
            suggestionProvider = this.suggestionProvider;
         }

         return new TypedCommandComponent((String)Objects.requireNonNull(this.name, "name"), parser, (TypeToken)Objects.requireNonNull(this.valueType, "valueType"), (Description)Objects.requireNonNull(this.description, "description"), componentType, this.defaultValue, suggestionProvider, (Collection)Objects.requireNonNull(this.componentPreprocessors, "componentPreprocessors"));
      }
   }

   @API(
      status = Status.STABLE
   )
   public static enum ComponentType {
      LITERAL(true),
      REQUIRED_VARIABLE(true),
      OPTIONAL_VARIABLE(false),
      FLAG(false);

      private final boolean required;

      private ComponentType(final boolean required) {
         this.required = required;
      }

      public boolean required() {
         return this.required;
      }

      public boolean optional() {
         return !this.required;
      }

      // $FF: synthetic method
      private static CommandComponent.ComponentType[] $values() {
         return new CommandComponent.ComponentType[]{LITERAL, REQUIRED_VARIABLE, OPTIONAL_VARIABLE, FLAG};
      }
   }
}
