package ac.grim.grimac.shaded.incendo.cloud;

import java.util.function.Function;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public interface SenderMapper<B, M> {
   @NonNull
   M map(@NonNull B base);

   @NonNull
   B reverse(@NonNull M mapped);

   @NonNull
   static <B, M> SenderMapper<B, M> create(@NonNull final Function<B, M> map, @NonNull final Function<M, B> reverse) {
      return new SenderMapperImpl(map, reverse);
   }

   @NonNull
   static <S> SenderMapper<S, S> identity() {
      return SenderMapperImpl.IDENTITY;
   }
}
