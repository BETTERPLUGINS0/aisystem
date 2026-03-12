package ac.grim.grimac.shaded.incendo.cloud.parser.aggregate;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;

final class AggregateParserImpl<C, O> implements AggregateParser<C, O> {
   private final List<CommandComponent<C>> components;
   private final TypeToken<O> valueType;
   private final AggregateResultMapper<C, O> mapper;

   AggregateParserImpl(@NonNull final List<CommandComponent<C>> components, @NonNull final TypeToken<O> valueType, @NonNull final AggregateResultMapper<C, O> mapper) {
      this.components = components;
      this.valueType = valueType;
      this.mapper = mapper;
   }

   @NonNull
   public List<CommandComponent<C>> components() {
      return Collections.unmodifiableList(this.components);
   }

   @NonNull
   public AggregateResultMapper<C, O> mapper() {
      return this.mapper;
   }

   @NonNull
   public TypeToken<O> valueType() {
      return this.valueType;
   }
}
