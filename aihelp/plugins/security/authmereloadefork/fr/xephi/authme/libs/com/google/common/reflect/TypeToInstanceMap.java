package fr.xephi.authme.libs.com.google.common.reflect;

import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.util.Map;
import javax.annotation.CheckForNull;

@DoNotMock("Use ImmutableTypeToInstanceMap or MutableTypeToInstanceMap")
@ElementTypesAreNonnullByDefault
public interface TypeToInstanceMap<B> extends Map<TypeToken<? extends B>, B> {
   @CheckForNull
   <T extends B> T getInstance(Class<T> var1);

   @CheckForNull
   <T extends B> T getInstance(TypeToken<T> var1);

   @CheckForNull
   @CanIgnoreReturnValue
   <T extends B> T putInstance(Class<T> var1, T var2);

   @CheckForNull
   @CanIgnoreReturnValue
   <T extends B> T putInstance(TypeToken<T> var1, T var2);
}
