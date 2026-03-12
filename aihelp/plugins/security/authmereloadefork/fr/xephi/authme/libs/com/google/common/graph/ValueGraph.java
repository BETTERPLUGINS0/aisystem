package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import java.util.Optional;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
public interface ValueGraph<N, V> extends BaseGraph<N> {
   Set<N> nodes();

   Set<EndpointPair<N>> edges();

   Graph<N> asGraph();

   boolean isDirected();

   boolean allowsSelfLoops();

   ElementOrder<N> nodeOrder();

   ElementOrder<N> incidentEdgeOrder();

   Set<N> adjacentNodes(N var1);

   Set<N> predecessors(N var1);

   Set<N> successors(N var1);

   Set<EndpointPair<N>> incidentEdges(N var1);

   int degree(N var1);

   int inDegree(N var1);

   int outDegree(N var1);

   boolean hasEdgeConnecting(N var1, N var2);

   boolean hasEdgeConnecting(EndpointPair<N> var1);

   Optional<V> edgeValue(N var1, N var2);

   Optional<V> edgeValue(EndpointPair<N> var1);

   @CheckForNull
   V edgeValueOrDefault(N var1, N var2, @CheckForNull V var3);

   @CheckForNull
   V edgeValueOrDefault(EndpointPair<N> var1, @CheckForNull V var2);

   boolean equals(@CheckForNull Object var1);

   int hashCode();
}
