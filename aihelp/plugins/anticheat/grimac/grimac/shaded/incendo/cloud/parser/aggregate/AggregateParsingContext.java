package ac.grim.grimac.shaded.incendo.cloud.parser.aggregate;

import ac.grim.grimac.shaded.incendo.cloud.key.MutableCloudKeyContainer;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public interface AggregateParsingContext<C> extends MutableCloudKeyContainer {
   @NonNull
   static <C> AggregateParsingContext<C> argumentContext(@NonNull final AggregateParser<C, ?> parser) {
      return new AggregateParsingContextImpl(parser);
   }
}
