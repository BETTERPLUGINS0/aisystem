package ac.grim.grimac.shaded.incendo.cloud.parser;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class StandardParameters {
   public static final ParserParameter<Number> RANGE_MIN = create("min", TypeToken.get(Number.class));
   public static final ParserParameter<Number> RANGE_MAX = create("max", TypeToken.get(Number.class));
   public static final ParserParameter<Boolean> GREEDY = create("greedy", TypeToken.get(Boolean.class));
   @API(
      status = Status.STABLE
   )
   public static final ParserParameter<Boolean> FLAG_YIELDING = create("flag_yielding", TypeToken.get(Boolean.class));
   @API(
      status = Status.STABLE
   )
   public static final ParserParameter<Boolean> QUOTED = create("quoted", TypeToken.get(Boolean.class));
   @API(
      status = Status.STABLE
   )
   public static final ParserParameter<Boolean> LIBERAL = create("liberal", TypeToken.get(Boolean.class));

   private StandardParameters() {
   }

   @NonNull
   private static <T> ParserParameter<T> create(@NonNull final String key, @NonNull final TypeToken<T> expectedType) {
      return new ParserParameter(key, expectedType);
   }
}
