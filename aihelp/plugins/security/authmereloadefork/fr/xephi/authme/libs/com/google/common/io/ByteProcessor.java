package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.io.IOException;

@DoNotMock("Implement it normally")
@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public interface ByteProcessor<T> {
   @CanIgnoreReturnValue
   boolean processBytes(byte[] var1, int var2, int var3) throws IOException;

   @ParametricNullness
   T getResult();
}
