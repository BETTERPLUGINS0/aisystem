package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.util.Optional;
import java.util.Set;
import javax.annotation.CheckForNull;

@DoNotMock("Use NetworkBuilder to create a real instance")
@ElementTypesAreNonnullByDefault
@Beta
public interface Network<N, E> extends SuccessorsFunction<N>, PredecessorsFunction<N> {
   Set<N> nodes();

   Set<E> edges();

   Graph<N> asGraph();

   boolean isDirected();

   boolean allowsParallelEdges();

   boolean allowsSelfLoops();

   ElementOrder<N> nodeOrder();

   ElementOrder<E> edgeOrder();

   Set<N> adjacentNodes(N var1);

   Set<N> predecessors(N var1);

   Set<N> successors(N var1);

   Set<E> incidentEdges(N var1);

   Set<E> inEdges(N var1);

   Set<E> outEdges(N var1);

   int degree(N var1);

   int inDegree(N var1);

   int outDegree(N var1);

   EndpointPair<N> incidentNodes(E var1);

   Set<E> adjacentEdges(E var1);

   Set<E> edgesConnecting(N var1, N var2);

   Set<E> edgesConnecting(EndpointPair<N> var1);

   Optional<E> edgeConnecting(N var1, N var2);

   Optional<E> edgeConnecting(EndpointPair<N> var1);

   @CheckForNull
   E edgeConnectingOrNull(N var1, N var2);

   @CheckForNull
   E edgeConnectingOrNull(EndpointPair<N> var1);

   boolean hasEdgeConnecting(N var1, N var2);

   boolean hasEdgeConnecting(EndpointPair<N> var1);

   boolean equals(@CheckForNull Object var1);

   int hashCode();
}
