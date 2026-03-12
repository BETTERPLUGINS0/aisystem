package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Function<F, T> extends java.util.function.Function<F, T> {
   @ParametricNullness
   @CanIgnoreReturnValue
   T apply(@ParametricNullness F var1);

   boolean equals(@CheckForNull Object var1);
}
