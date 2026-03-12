package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
public interface MutableValueGraph<N, V> extends ValueGraph<N, V> {
   @CanIgnoreReturnValue
   boolean addNode(N var1);

   @CheckForNull
   @CanIgnoreReturnValue
   V putEdgeValue(N var1, N var2, V var3);

   @CheckForNull
   @CanIgnoreReturnValue
   V putEdgeValue(EndpointPair<N> var1, V var2);

   @CanIgnoreReturnValue
   boolean removeNode(N var1);

   @CheckForNull
   @CanIgnoreReturnValue
   V removeEdge(N var1, N var2);

   @CheckForNull
   @CanIgnoreReturnValue
   V removeEdge(EndpointPair<N> var1);
}
