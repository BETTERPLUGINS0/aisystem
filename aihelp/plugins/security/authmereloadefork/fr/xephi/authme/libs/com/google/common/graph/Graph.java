package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.util.Set;
import javax.annotation.CheckForNull;

@DoNotMock("Use GraphBuilder to create a real instance")
@ElementTypesAreNonnullByDefault
@Beta
public interface Graph<N> extends BaseGraph<N> {
   Set<N> nodes();

   Set<EndpointPair<N>> edges();

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

   boolean equals(@CheckForNull Object var1);

   int hashCode();
}
