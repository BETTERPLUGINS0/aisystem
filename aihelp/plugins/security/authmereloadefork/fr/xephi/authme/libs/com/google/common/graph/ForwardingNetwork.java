package fr.xephi.authme.libs.com.google.common.graph;

import java.util.Optional;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
abstract class ForwardingNetwork<N, E> extends AbstractNetwork<N, E> {
   abstract Network<N, E> delegate();

   public Set<N> nodes() {
      return this.delegate().nodes();
   }

   public Set<E> edges() {
      return this.delegate().edges();
   }

   public boolean isDirected() {
      return this.delegate().isDirected();
   }

   public boolean allowsParallelEdges() {
      return this.delegate().allowsParallelEdges();
   }

   public boolean allowsSelfLoops() {
      return this.delegate().allowsSelfLoops();
   }

   public ElementOrder<N> nodeOrder() {
      return this.delegate().nodeOrder();
   }

   public ElementOrder<E> edgeOrder() {
      return this.delegate().edgeOrder();
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

   public Set<E> incidentEdges(N node) {
      return this.delegate().incidentEdges(node);
   }

   public Set<E> inEdges(N node) {
      return this.delegate().inEdges(node);
   }

   public Set<E> outEdges(N node) {
      return this.delegate().outEdges(node);
   }

   public EndpointPair<N> incidentNodes(E edge) {
      return this.delegate().incidentNodes(edge);
   }

   public Set<E> adjacentEdges(E edge) {
      return this.delegate().adjacentEdges(edge);
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

   public Set<E> edgesConnecting(N nodeU, N nodeV) {
      return this.delegate().edgesConnecting(nodeU, nodeV);
   }

   public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
      return this.delegate().edgesConnecting(endpoints);
   }

   public Optional<E> edgeConnecting(N nodeU, N nodeV) {
      return this.delegate().edgeConnecting(nodeU, nodeV);
   }

   public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
      return this.delegate().edgeConnecting(endpoints);
   }

   @CheckForNull
   public E edgeConnectingOrNull(N nodeU, N nodeV) {
      return this.delegate().edgeConnectingOrNull(nodeU, nodeV);
   }

   @CheckForNull
   public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
      return this.delegate().edgeConnectingOrNull(endpoints);
   }

   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
      return this.delegate().hasEdgeConnecting(nodeU, nodeV);
   }

   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
      return this.delegate().hasEdgeConnecting(endpoints);
   }
}
