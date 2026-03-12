package fr.xephi.authme.libs.com.google.common.graph;

import java.util.Optional;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
abstract class ForwardingValueGraph<N, V> extends AbstractValueGraph<N, V> {
   abstract ValueGraph<N, V> delegate();

   public Set<N> nodes() {
      return this.delegate().nodes();
   }

   protected long edgeCount() {
      return (long)this.delegate().edges().size();
   }

   public boolean isDirected() {
      return this.delegate().isDirected();
   }

   public boolean allowsSelfLoops() {
      return this.delegate().allowsSelfLoops();
   }

   public ElementOrder<N> nodeOrder() {
      return this.delegate().nodeOrder();
   }

   public ElementOrder<N> incidentEdgeOrder() {
      return this.delegate().incidentEdgeOrder();
   }

   public Set<N> adjacentNodes(N node) {
      return this.delegate().adjacentNodes(node);
   }

   public Set<N> predecessors(N node) {
      return this.delegate().predecessors(node);
   }

   public Set<N> successors(N node) {
      return this.delegate().successors(node);
   }

   public int degree(N node) {
      return this.delegate().degree(node);
   }

   public int inDegree(N node) {
      return this.delegate().inDegree(node);
   }

   public int outDegree(N node) {
      return this.delegate().outDegree(node);
   }

   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
      return this.delegate().hasEdgeConnecting(nodeU, nodeV);
   }

   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
      return this.delegate().hasEdgeConnecting(endpoints);
   }

   public Optional<V> edgeValue(N nodeU, N nodeV) {
      return this.delegate().edgeValue(nodeU, nodeV);
   }

   public Optional<V> edgeValue(EndpointPair<N> endpoints) {
      return this.delegate().edgeValue(endpoints);
   }

   @CheckForNull
   public V edgeValueOrDefault(N nodeU, N nodeV, @CheckForNull V defaultValue) {
      return this.delegate().edgeValueOrDefault(nodeU, nodeV, defaultValue);
   }

   @CheckForNull
   public V edgeValueOrDefault(EndpointPair<N> endpoints, @CheckForNull V defaultValue) {
      return this.delegate().edgeValueOrDefault(endpoints, defaultValue);
   }
}
