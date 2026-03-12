package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface BiMap<K, V> extends Map<K, V> {
   @CheckForNull
   @CanIgnoreReturnValue
   V put(@ParametricNullness K var1, @ParametricNullness V var2);

   @CheckForNull
   @CanIgnoreReturnValue
   V forcePut(@ParametricNullness K var1, @ParametricNullness V var2);

   void putAll(Map<? extends K, ? extends V> var1);

   Set<V> values();

   BiMap<V, K> inverse();
}
