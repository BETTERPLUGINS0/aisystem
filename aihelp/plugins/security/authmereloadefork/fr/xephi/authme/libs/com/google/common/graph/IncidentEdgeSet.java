package fr.xephi.authme.libs.com.google.common.graph;

import java.util.AbstractSet;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
abstract class IncidentEdgeSet<N> extends AbstractSet<EndpointPair<N>> {
   final N node;
   final BaseGraph<N> graph;

   IncidentEdgeSet(BaseGraph<N> graph, N node) {
      this.graph = graph;
      this.node = node;
   }

   public boolean remove(@CheckForNull Object o) {
      throw new UnsupportedOperationException();
   }

   public int size() {
      return this.graph.isDirected() ? this.graph.inDegree(this.node) + this.graph.outDegree(this.node) - (this.graph.successors(this.node).contains(this.node) ? 1 : 0) : this.graph.adjacentNodes(this.node).size();
   }

   public boolean contains(@CheckForNull Object obj) {
      if (!(obj instanceof EndpointPair)) {
         return false;
      } else {
         EndpointPair<?> endpointPair = (EndpointPair)obj;
         Object nodeU;
         if (this.graph.isDirected()) {
            if (!endpointPair.isOrdered()) {
               return false;
            } else {
               Object source = endpointPair.source();
               nodeU = endpointPair.target();
               return this.node.equals(source) && this.graph.successors(this.node).contains(nodeU) || this.node.equals(nodeU) && this.graph.predecessors(this.node).contains(source);
            }
         } else if (endpointPair.isOrdered()) {
            return false;
         } else {
            Set<N> adjacent = this.graph.adjacentNodes(this.node);
            nodeU = endpointPair.nodeU();
            Object nodeV = endpointPair.nodeV();
            return this.node.equals(nodeV) && adjacent.contains(nodeU) || this.node.equals(nodeU) && adjacent.contains(nodeV);
         }
      }
   }
}
