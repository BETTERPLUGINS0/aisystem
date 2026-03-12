package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
interface GraphConnections<N, V> {
   Set<N> adjacentNodes();

   Set<N> predecessors();

   Set<N> successors();

   Iterator<EndpointPair<N>> incidentEdgeIterator(N var1);

   @CheckForNull
   V value(N var1);

   void removePredecessor(N var1);

   @CheckForNull
   @CanIgnoreReturnValue
   V removeSuccessor(N var1);

   void addPredecessor(N var1, V var2);

   @CheckForNull
   @CanIgnoreReturnValue
   V addSuccessor(N var1, V var2);
}
