package ac.grim.grimac.shaded.incendo.cloud.parser.aggregate;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public class AggregateParserBuilder<C> {
   private final List<CommandComponent<C>> components;

   AggregateParserBuilder(@NonNull final List<? extends CommandComponent<C>> components) {
      this.components = Collections.unmodifiableList(components);
   }

   AggregateParserBuilder() {
      this.components = Collections.emptyList();
   }

   @NonNull
   public final <O> AggregateParserBuilder.MappedAggregateParserBuilder<C, O> withMapper(@NonNull final TypeToken<O> valueType, @NonNull final AggregateResultMapper<C, O> mapper) {
      return new AggregateParserBuilder.MappedAggregateParserBuilder(this.components(), valueType, mapper);
   }

   @NonNull
   public final <O> AggregateParserBuilder.MappedAggregateParserBuilder<C, O> withMapper(@NonNull final Class<O> valueType, @NonNull final AggregateResultMapper<C, O> mapper) {
      return new AggregateParserBuilder.MappedAggregateParserBuilder(this.components(), TypeToken.get(valueType), mapper);
   }

   @NonNull
   public final <O> AggregateParserBuilder.MappedAggregateParserBuilder<C, O> withDirectMapper(@NonNull final Class<O> valueType, @NonNull final AggregateResultMapper.DirectSuccessMapper<C, O> mapper) {
      return new AggregateParserBuilder.MappedAggregateParserBuilder(this.components(), TypeToken.get(valueType), mapper);
   }

   @NonNull
   public final <O> AggregateParserBuilder.MappedAggregateParserBuilder<C, O> withDirectMapper(@NonNull final TypeToken<O> valueType, @NonNull final AggregateResultMapper.DirectSuccessMapper<C, O> mapper) {
      return new AggregateParserBuilder.MappedAggregateParserBuilder(this.components(), valueType, mapper);
   }

   @NonNull
   public AggregateParserBuilder<C> withComponent(@NonNull final CommandComponent<C> component) {
      List<CommandComponent<C>> components = new ArrayList(this.components());
      components.add(component);
      return new AggregateParserBuilder(components);
   }

   @NonNull
   public <T> AggregateParserBuilder<C> withComponent(@NonNull final String name, @NonNull final ParserDescriptor<C, T> parserDescriptor) {
      return this.withComponent(CommandComponent.builder().name(name).parser(parserDescriptor).build());
   }

   @NonNull
   public <T> AggregateParserBuilder<C> withComponent(@NonNull final String name, @NonNull final ParserDescriptor<C, T> parserDescriptor, @NonNull final SuggestionProvider<C> suggestionProvider) {
      return this.withComponent(CommandComponent.builder().name(name).parser(parserDescriptor).suggestionProvider(suggestionProvider).build());
   }

   @NonNull
   public <T> AggregateParserBuilder<C> withComponent(@NonNull final CloudKey<T> name, @NonNull final ParserDescriptor<C, T> parserDescriptor) {
      return this.withComponent(CommandComponent.builder().key(name).parser(parserDescriptor).build());
   }

   @NonNull
   public <T> AggregateParserBuilder<C> withComponent(@NonNull final CloudKey<T> name, @NonNull final ParserDescriptor<C, T> parserDescriptor, @NonNull final SuggestionProvider<C> suggestionProvider) {
      return this.withComponent(CommandComponent.builder().key(name).parser(parserDescriptor).suggestionProvider(suggestionProvider).build());
   }

   @NonNull
   final List<CommandComponent<C>> components() {
      return this.components;
   }

   public static final class MappedAggregateParserBuilder<C, O> extends AggregateParserBuilder<C> {
      private final AggregateResultMapper<C, O> mapper;
      private final TypeToken<O> valueType;

      MappedAggregateParserBuilder(@NonNull final List<CommandComponent<C>> components, @NonNull final TypeToken<O> valueType, @NonNull final AggregateResultMapper<C, O> mapper) {
         super(components);
         this.valueType = valueType;
         this.mapper = mapper;
      }

      @NonNull
      public AggregateParserBuilder.MappedAggregateParserBuilder<C, O> withComponent(@NonNull final CommandComponent<C> component) {
         List<CommandComponent<C>> components = new ArrayList(this.components());
         components.add(component);
         return new AggregateParserBuilder.MappedAggregateParserBuilder(components, this.valueType, this.mapper);
      }

      @NonNull
      public <T> AggregateParserBuilder.MappedAggregateParserBuilder<C, O> withComponent(@NonNull final String name, @NonNull final ParserDescriptor<C, T> parserDescriptor) {
         return this.withComponent(CommandComponent.builder().name(name).parser(parserDescriptor).build());
      }

      @NonNull
      public <T> AggregateParserBuilder.MappedAggregateParserBuilder<C, O> withComponent(@NonNull final String name, @NonNull final ParserDescriptor<C, T> parserDescriptor, @NonNull final SuggestionProvider<C> suggestionProvider) {
         return this.withComponent(CommandComponent.builder().name(name).parser(parserDescriptor).suggestionProvider(suggestionProvider).build());
      }

      @NonNull
      public <T> AggregateParserBuilder.MappedAggregateParserBuilder<C, O> withComponent(@NonNull final CloudKey<T> name, @NonNull final ParserDescriptor<C, T> parserDescriptor) {
         return this.withComponent(CommandComponent.builder().key(name).parser(parserDescriptor).build());
      }

      @NonNull
      public <T> AggregateParserBuilder.MappedAggregateParserBuilder<C, O> withComponent(@NonNull final CloudKey<T> name, @NonNull final ParserDescriptor<C, T> parserDescriptor, @NonNull final SuggestionProvider<C> suggestionProvider) {
         return this.withComponent(CommandComponent.builder().key(name).parser(parserDescriptor).suggestionProvider(suggestionProvider).build());
      }

      @NonNull
      public AggregateParser<C, O> build() {
         return new AggregateParserImpl(this.components(), this.valueType, this.mapper);
      }
   }
}
