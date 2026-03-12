package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.collect.Iterators;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
final class UndirectedGraphConnections<N, V> implements GraphConnections<N, V> {
   private final Map<N, V> adjacentNodeValues;

   private UndirectedGraphConnections(Map<N, V> adjacentNodeValues) {
      this.adjacentNodeValues = (Map)Preconditions.checkNotNull(adjacentNodeValues);
   }

   static <N, V> UndirectedGraphConnections<N, V> of(ElementOrder<N> incidentEdgeOrder) {
      switch(incidentEdgeOrder.type()) {
      case UNORDERED:
         return new UndirectedGraphConnections(new HashMap(2, 1.0F));
      case STABLE:
         return new UndirectedGraphConnections(new LinkedHashMap(2, 1.0F));
      default:
         throw new AssertionError(incidentEdgeOrder.type());
      }
   }

   static <N, V> UndirectedGraphConnections<N, V> ofImmutable(Map<N, V> adjacentNodeValues) {
      return new UndirectedGraphConnections(ImmutableMap.copyOf(adjacentNodeValues));
   }

   public Set<N> adjacentNodes() {
      return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
   }

   public Set<N> predecessors() {
      return this.adjacentNodes();
   }

   public Set<N> successors() {
      return this.adjacentNodes();
   }

   public Iterator<EndpointPair<N>> incidentEdgeIterator(N thisNode) {
      return Iterators.transform(this.adjacentNodeValues.keySet().iterator(), (incidentNode) -> {
         return EndpointPair.unordered(thisNode, incidentNode);
      });
   }

   @CheckForNull
   public V value(N node) {
      return this.adjacentNodeValues.get(node);
   }

   public void removePredecessor(N node) {
      this.removeSuccessor(node);
   }

   @CheckForNull
   public V removeSuccessor(N node) {
      return this.adjacentNodeValues.remove(node);
   }

   public void addPredecessor(N node, V value) {
      this.addSuccessor(node, value);
   }

   @CheckForNull
   public V addSuccessor(N node, V value) {
      return this.adjacentNodeValues.put(node, value);
   }
}
