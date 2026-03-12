package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import java.util.Iterator;
import java.util.Objects;

@Immutable(
   containerOf = {"N", "V"}
)
@ElementTypesAreNonnullByDefault
@Beta
public final class ImmutableValueGraph<N, V> extends StandardValueGraph<N, V> {
   private ImmutableValueGraph(ValueGraph<N, V> graph) {
      super(ValueGraphBuilder.from(graph), getNodeConnections(graph), (long)graph.edges().size());
   }

   public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
      return graph instanceof ImmutableValueGraph ? (ImmutableValueGraph)graph : new ImmutableValueGraph(graph);
   }

   /** @deprecated */
   @Deprecated
   public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> graph) {
      return (ImmutableValueGraph)Preconditions.checkNotNull(graph);
   }

   public ElementOrder<N> incidentEdgeOrder() {
      return ElementOrder.stable();
   }

   public ImmutableGraph<N> asGraph() {
      return new ImmutableGraph(this);
   }

   private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> graph) {
      ImmutableMap.Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.builder();
      Iterator var2 = graph.nodes().iterator();

      while(var2.hasNext()) {
         N node = var2.next();
         nodeConnections.put(node, connectionsOf(graph, node));
      }

      return nodeConnections.buildOrThrow();
   }

   private static <N, V> GraphConnections<N, V> connectionsOf(ValueGraph<N, V> graph, N node) {
      Function<N, V> successorNodeToValueFn = (successorNode) -> {
         return Objects.requireNonNull(graph.edgeValueOrDefault(node, successorNode, (Object)null));
      };
      return (GraphConnections)(graph.isDirected() ? DirectedGraphConnections.ofImmutable(node, graph.incidentEdges(node), successorNodeToValueFn) : UndirectedGraphConnections.ofImmutable(Maps.asMap(graph.adjacentNodes(node), successorNodeToValueFn)));
   }

   public static class Builder<N, V> {
      private final MutableValueGraph<N, V> mutableValueGraph;

      Builder(ValueGraphBuilder<N, V> graphBuilder) {
         this.mutableValueGraph = graphBuilder.copy().incidentEdgeOrder(ElementOrder.stable()).build();
      }

      @CanIgnoreReturnValue
      public ImmutableValueGraph.Builder<N, V> addNode(N node) {
         this.mutableValueGraph.addNode(node);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableValueGraph.Builder<N, V> putEdgeValue(N nodeU, N nodeV, V value) {
         this.mutableValueGraph.putEdgeValue(nodeU, nodeV, value);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableValueGraph.Builder<N, V> putEdgeValue(EndpointPair<N> endpoints, V value) {
         this.mutableValueGraph.putEdgeValue(endpoints, value);
         return this;
      }

      public ImmutableValueGraph<N, V> build() {
         return ImmutableValueGraph.copyOf((ValueGraph)this.mutableValueGraph);
      }
   }
}
