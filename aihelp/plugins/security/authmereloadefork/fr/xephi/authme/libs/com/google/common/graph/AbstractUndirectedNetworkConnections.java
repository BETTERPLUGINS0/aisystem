package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
abstract class AbstractUndirectedNetworkConnections<N, E> implements NetworkConnections<N, E> {
   final Map<E, N> incidentEdgeMap;

   AbstractUndirectedNetworkConnections(Map<E, N> incidentEdgeMap) {
      this.incidentEdgeMap = (Map)Preconditions.checkNotNull(incidentEdgeMap);
   }

   public Set<N> predecessors() {
      return this.adjacentNodes();
   }

   public Set<N> successors() {
      return this.adjacentNodes();
   }

   public Set<E> incidentEdges() {
      return Collections.unmodifiableSet(this.incidentEdgeMap.keySet());
   }

   public Set<E> inEdges() {
      return this.incidentEdges();
   }

   public Set<E> outEdges() {
      return this.incidentEdges();
   }

   public N adjacentNode(E edge) {
      return Objects.requireNonNull(this.incidentEdgeMap.get(edge));
   }

   @CheckForNull
   public N removeInEdge(E edge, boolean isSelfLoop) {
      return !isSelfLoop ? this.removeOutEdge(edge) : null;
   }

   public N removeOutEdge(E edge) {
      N previousNode = this.incidentEdgeMap.remove(edge);
      return Objects.requireNonNull(previousNode);
   }

   public void addInEdge(E edge, N node, boolean isSelfLoop) {
      if (!isSelfLoop) {
         this.addOutEdge(edge, node);
      }

   }

   public void addOutEdge(E edge, N node) {
      N previousNode = this.incidentEdgeMap.put(edge, node);
      Preconditions.checkState(previousNode == null);
   }
}
