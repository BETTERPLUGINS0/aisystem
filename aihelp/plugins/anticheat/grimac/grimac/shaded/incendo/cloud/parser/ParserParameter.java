package ac.grim.grimac.shaded.incendo.cloud.parser;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public class ParserParameter<T> {
   private final String key;
   private final TypeToken<T> expectedType;

   public ParserParameter(@NonNull final String key, @NonNull final TypeToken<T> expectedType) {
      this.key = key;
      this.expectedType = expectedType;
   }

   @NonNull
   public String key() {
      return this.key;
   }

   @NonNull
   public TypeToken<T> expectedType() {
      return this.expectedType;
   }

   public final boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ParserParameter<?> that = (ParserParameter)o;
         return Objects.equals(this.key, that.key) && Objects.equals(this.expectedType, that.expectedType);
      } else {
         return false;
      }
   }

   public final int hashCode() {
      return Objects.hash(new Object[]{this.key, this.expectedType});
   }
}
