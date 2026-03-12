package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Optional;

@ElementTypesAreNonnullByDefault
abstract class AbstractGraphBuilder<N> {
   final boolean directed;
   boolean allowsSelfLoops = false;
   ElementOrder<N> nodeOrder = ElementOrder.insertion();
   ElementOrder<N> incidentEdgeOrder = ElementOrder.unordered();
   Optional<Integer> expectedNodeCount = Optional.absent();

   AbstractGraphBuilder(boolean directed) {
      this.directed = directed;
   }
}
