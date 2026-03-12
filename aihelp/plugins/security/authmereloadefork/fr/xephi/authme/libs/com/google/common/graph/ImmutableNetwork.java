package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import java.util.Iterator;
import java.util.Map;

@Immutable(
   containerOf = {"N", "E"}
)
@ElementTypesAreNonnullByDefault
@Beta
public final class ImmutableNetwork<N, E> extends StandardNetwork<N, E> {
   private ImmutableNetwork(Network<N, E> network) {
      super(NetworkBuilder.from(network), getNodeConnections(network), getEdgeToReferenceNode(network));
   }

   public static <N, E> ImmutableNetwork<N, E> copyOf(Network<N, E> network) {
      return network instanceof ImmutableNetwork ? (ImmutableNetwork)network : new ImmutableNetwork(network);
   }

   /** @deprecated */
   @Deprecated
   public static <N, E> ImmutableNetwork<N, E> copyOf(ImmutableNetwork<N, E> network) {
      return (ImmutableNetwork)Preconditions.checkNotNull(network);
   }

   public ImmutableGraph<N> asGraph() {
      return new ImmutableGraph(super.asGraph());
   }

   private static <N, E> Map<N, NetworkConnections<N, E>> getNodeConnections(Network<N, E> network) {
      ImmutableMap.Builder<N, NetworkConnections<N, E>> nodeConnections = ImmutableMap.builder();
      Iterator var2 = network.nodes().iterator();

      while(var2.hasNext()) {
         N node = var2.next();
         nodeConnections.put(node, connectionsOf(network, node));
      }

      return nodeConnections.buildOrThrow();
   }

   private static <N, E> Map<E, N> getEdgeToReferenceNode(Network<N, E> network) {
      ImmutableMap.Builder<E, N> edgeToReferenceNode = ImmutableMap.builder();
      Iterator var2 = network.edges().iterator();

      while(var2.hasNext()) {
         E edge = var2.next();
         edgeToReferenceNode.put(edge, network.incidentNodes(edge).nodeU());
      }

      return edgeToReferenceNode.buildOrThrow();
   }

   private static <N, E> NetworkConnections<N, E> connectionsOf(Network<N, E> network, N node) {
      Map incidentEdgeMap;
      if (network.isDirected()) {
         incidentEdgeMap = Maps.asMap(network.inEdges(node), sourceNodeFn(network));
         Map<E, N> outEdgeMap = Maps.asMap(network.outEdges(node), targetNodeFn(network));
         int selfLoopCount = network.edgesConnecting(node, node).size();
         return (NetworkConnections)(network.allowsParallelEdges() ? DirectedMultiNetworkConnections.ofImmutable(incidentEdgeMap, outEdgeMap, selfLoopCount) : DirectedNetworkConnections.ofImmutable(incidentEdgeMap, outEdgeMap, selfLoopCount));
      } else {
         incidentEdgeMap = Maps.asMap(network.incidentEdges(node), adjacentNodeFn(network, node));
         return (NetworkConnections)(network.allowsParallelEdges() ? UndirectedMultiNetworkConnections.ofImmutable(incidentEdgeMap) : UndirectedNetworkConnections.ofImmutable(incidentEdgeMap));
      }
   }

   private static <N, E> Function<E, N> sourceNodeFn(Network<N, E> network) {
      return (edge) -> {
         return network.incidentNodes(edge).source();
      };
   }

   private static <N, E> Function<E, N> targetNodeFn(Network<N, E> network) {
      return (edge) -> {
         return network.incidentNodes(edge).target();
      };
   }

   private static <N, E> Function<E, N> adjacentNodeFn(Network<N, E> network, N node) {
      return (edge) -> {
         return network.incidentNodes(edge).adjacentNode(node);
      };
   }

   public static class Builder<N, E> {
      private final MutableNetwork<N, E> mutableNetwork;

      Builder(NetworkBuilder<N, E> networkBuilder) {
         this.mutableNetwork = networkBuilder.build();
      }

      @CanIgnoreReturnValue
      public ImmutableNetwork.Builder<N, E> addNode(N node) {
         this.mutableNetwork.addNode(node);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableNetwork.Builder<N, E> addEdge(N nodeU, N nodeV, E edge) {
         this.mutableNetwork.addEdge(nodeU, nodeV, edge);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableNetwork.Builder<N, E> addEdge(EndpointPair<N> endpoints, E edge) {
         this.mutableNetwork.addEdge(endpoints, edge);
         return this;
      }

      public ImmutableNetwork<N, E> build() {
         return ImmutableNetwork.copyOf((Network)this.mutableNetwork);
      }
   }
}
