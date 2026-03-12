package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Functions;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import java.util.Iterator;

@Immutable(
   containerOf = {"N"}
)
@ElementTypesAreNonnullByDefault
@Beta
public class ImmutableGraph<N> extends ForwardingGraph<N> {
   private final BaseGraph<N> backingGraph;

   ImmutableGraph(BaseGraph<N> backingGraph) {
      this.backingGraph = backingGraph;
   }

   public static <N> ImmutableGraph<N> copyOf(Graph<N> graph) {
      return graph instanceof ImmutableGraph ? (ImmutableGraph)graph : new ImmutableGraph(new StandardValueGraph(GraphBuilder.from(graph), getNodeConnections(graph), (long)graph.edges().size()));
   }

   /** @deprecated */
   @Deprecated
   public static <N> ImmutableGraph<N> copyOf(ImmutableGraph<N> graph) {
      return (ImmutableGraph)Preconditions.checkNotNull(graph);
   }

   public ElementOrder<N> incidentEdgeOrder() {
      return ElementOrder.stable();
   }

   private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(Graph<N> graph) {
      ImmutableMap.Builder<N, GraphConnections<N, GraphConstants.Presence>> nodeConnections = ImmutableMap.builder();
      Iterator var2 = graph.nodes().iterator();

      while(var2.hasNext()) {
         N node = var2.next();
         nodeConnections.put(node, connectionsOf(graph, node));
      }

      return nodeConnections.buildOrThrow();
   }

   private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(Graph<N> graph, N node) {
      Function<N, GraphConstants.Presence> edgeValueFn = Functions.constant(GraphConstants.Presence.EDGE_EXISTS);
      return (GraphConnections)(graph.isDirected() ? DirectedGraphConnections.ofImmutable(node, graph.incidentEdges(node), edgeValueFn) : UndirectedGraphConnections.ofImmutable(Maps.asMap(graph.adjacentNodes(node), edgeValueFn)));
   }

   BaseGraph<N> delegate() {
      return this.backingGraph;
   }

   public static class Builder<N> {
      private final MutableGraph<N> mutableGraph;

      Builder(GraphBuilder<N> graphBuilder) {
         this.mutableGraph = graphBuilder.copy().incidentEdgeOrder(ElementOrder.stable()).build();
      }

      @CanIgnoreReturnValue
      public ImmutableGraph.Builder<N> addNode(N node) {
         this.mutableGraph.addNode(node);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableGraph.Builder<N> putEdge(N nodeU, N nodeV) {
         this.mutableGraph.putEdge(nodeU, nodeV);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableGraph.Builder<N> putEdge(EndpointPair<N> endpoints) {
         this.mutableGraph.putEdge(endpoints);
         return this;
      }

      public ImmutableGraph<N> build() {
         return ImmutableGraph.copyOf((Graph)this.mutableGraph);
      }
   }
}
