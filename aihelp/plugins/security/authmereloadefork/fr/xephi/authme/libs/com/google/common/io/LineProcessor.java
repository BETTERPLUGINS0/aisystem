package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public interface LineProcessor<T> {
   @CanIgnoreReturnValue
   boolean processLine(String var1) throws IOException;

   @ParametricNullness
   T getResult();
}
