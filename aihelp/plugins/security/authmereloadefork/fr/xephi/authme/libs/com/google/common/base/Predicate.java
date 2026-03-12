package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Predicate<T> extends java.util.function.Predicate<T> {
   @CanIgnoreReturnValue
   boolean apply(@ParametricNullness T var1);

   boolean equals(@CheckForNull Object var1);

   default boolean test(@ParametricNullness T input) {
      return this.apply(input);
   }
}
