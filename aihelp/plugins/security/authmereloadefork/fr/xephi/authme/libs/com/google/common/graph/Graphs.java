package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSet;
import fr.xephi.authme.libs.com.google.common.collect.Iterables;
import fr.xephi.authme.libs.com.google.common.collect.Iterators;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
public final class Graphs {
   private Graphs() {
   }

   public static <N> boolean hasCycle(Graph<N> graph) {
      int numEdges = graph.edges().size();
      if (numEdges == 0) {
         return false;
      } else if (!graph.isDirected() && numEdges >= graph.nodes().size()) {
         return true;
      } else {
         Map<Object, Graphs.NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
         Iterator var3 = graph.nodes().iterator();

         Object node;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            node = var3.next();
         } while(!subgraphHasCycle(graph, visitedNodes, node, (Object)null));

         return true;
      }
   }

   public static boolean hasCycle(Network<?, ?> network) {
      return !network.isDirected() && network.allowsParallelEdges() && network.edges().size() > network.asGraph().edges().size() ? true : hasCycle(network.asGraph());
   }

   private static <N> boolean subgraphHasCycle(Graph<N> graph, Map<Object, Graphs.NodeVisitState> visitedNodes, N node, @CheckForNull N previousNode) {
      Graphs.NodeVisitState state = (Graphs.NodeVisitState)visitedNodes.get(node);
      if (state == Graphs.NodeVisitState.COMPLETE) {
         return false;
      } else if (state == Graphs.NodeVisitState.PENDING) {
         return true;
      } else {
         visitedNodes.put(node, Graphs.NodeVisitState.PENDING);
         Iterator var5 = graph.successors(node).iterator();

         Object nextNode;
         do {
            if (!var5.hasNext()) {
               visitedNodes.put(node, Graphs.NodeVisitState.COMPLETE);
               return false;
            }

            nextNode = var5.next();
         } while(!canTraverseWithoutReusingEdge(graph, nextNode, previousNode) || !subgraphHasCycle(graph, visitedNodes, nextNode, node));

         return true;
      }
   }

   private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object nextNode, @CheckForNull Object previousNode) {
      return graph.isDirected() || !Objects.equal(previousNode, nextNode);
   }

   public static <N> Graph<N> transitiveClosure(Graph<N> graph) {
      MutableGraph<N> transitiveClosure = GraphBuilder.from(graph).allowsSelfLoops(true).build();
      if (graph.isDirected()) {
         Iterator var2 = graph.nodes().iterator();

         while(var2.hasNext()) {
            N node = var2.next();
            Iterator var4 = reachableNodes(graph, node).iterator();

            while(var4.hasNext()) {
               N reachableNode = var4.next();
               transitiveClosure.putEdge(node, reachableNode);
            }
         }

         return transitiveClosure;
      } else {
         Set<N> visitedNodes = new HashSet();
         Iterator var12 = graph.nodes().iterator();

         while(true) {
            Object node;
            do {
               if (!var12.hasNext()) {
                  return transitiveClosure;
               }

               node = var12.next();
            } while(visitedNodes.contains(node));

            Set<N> reachableNodes = reachableNodes(graph, node);
            visitedNodes.addAll(reachableNodes);
            int pairwiseMatch = 1;
            Iterator var7 = reachableNodes.iterator();

            while(var7.hasNext()) {
               N nodeU = var7.next();
               Iterator var9 = Iterables.limit(reachableNodes, pairwiseMatch++).iterator();

               while(var9.hasNext()) {
                  N nodeV = var9.next();
                  transitiveClosure.putEdge(nodeU, nodeV);
               }
            }
         }
      }
   }

   public static <N> Set<N> reachableNodes(Graph<N> graph, N node) {
      Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
      return ImmutableSet.copyOf(Traverser.forGraph(graph).breadthFirst(node));
   }

   public static <N> Graph<N> transpose(Graph<N> graph) {
      if (!graph.isDirected()) {
         return graph;
      } else {
         return (Graph)(graph instanceof Graphs.TransposedGraph ? ((Graphs.TransposedGraph)graph).graph : new Graphs.TransposedGraph(graph));
      }
   }

   public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> graph) {
      if (!graph.isDirected()) {
         return graph;
      } else {
         return (ValueGraph)(graph instanceof Graphs.TransposedValueGraph ? ((Graphs.TransposedValueGraph)graph).graph : new Graphs.TransposedValueGraph(graph));
      }
   }

   public static <N, E> Network<N, E> transpose(Network<N, E> network) {
      if (!network.isDirected()) {
         return network;
      } else {
         return (Network)(network instanceof Graphs.TransposedNetwork ? ((Graphs.TransposedNetwork)network).network : new Graphs.TransposedNetwork(network));
      }
   }

   static <N> EndpointPair<N> transpose(EndpointPair<N> endpoints) {
      return endpoints.isOrdered() ? EndpointPair.ordered(endpoints.target(), endpoints.source()) : endpoints;
   }

   public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> nodes) {
      MutableGraph<N> subgraph = nodes instanceof Collection ? GraphBuilder.from(graph).expectedNodeCount(((Collection)nodes).size()).build() : GraphBuilder.from(graph).build();
      Iterator var3 = nodes.iterator();

      Object node;
      while(var3.hasNext()) {
         node = var3.next();
         subgraph.addNode(node);
      }

      var3 = subgraph.nodes().iterator();

      while(var3.hasNext()) {
         node = var3.next();
         Iterator var5 = graph.successors(node).iterator();

         while(var5.hasNext()) {
            N successorNode = var5.next();
            if (subgraph.nodes().contains(successorNode)) {
               subgraph.putEdge(node, successorNode);
            }
         }
      }

      return subgraph;
   }

   public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> graph, Iterable<? extends N> nodes) {
      MutableValueGraph<N, V> subgraph = nodes instanceof Collection ? ValueGraphBuilder.from(graph).expectedNodeCount(((Collection)nodes).size()).build() : ValueGraphBuilder.from(graph).build();
      Iterator var3 = nodes.iterator();

      Object node;
      while(var3.hasNext()) {
         node = var3.next();
         subgraph.addNode(node);
      }

      var3 = subgraph.nodes().iterator();

      while(var3.hasNext()) {
         node = var3.next();
         Iterator var5 = graph.successors(node).iterator();

         while(var5.hasNext()) {
            N successorNode = var5.next();
            if (subgraph.nodes().contains(successorNode)) {
               subgraph.putEdgeValue(node, successorNode, java.util.Objects.requireNonNull(graph.edgeValueOrDefault(node, successorNode, (Object)null)));
            }
         }
      }

      return subgraph;
   }

   public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> nodes) {
      MutableNetwork<N, E> subgraph = nodes instanceof Collection ? NetworkBuilder.from(network).expectedNodeCount(((Collection)nodes).size()).build() : NetworkBuilder.from(network).build();
      Iterator var3 = nodes.iterator();

      Object node;
      while(var3.hasNext()) {
         node = var3.next();
         subgraph.addNode(node);
      }

      var3 = subgraph.nodes().iterator();

      while(var3.hasNext()) {
         node = var3.next();
         Iterator var5 = network.outEdges(node).iterator();

         while(var5.hasNext()) {
            E edge = var5.next();
            N successorNode = network.incidentNodes(edge).adjacentNode(node);
            if (subgraph.nodes().contains(successorNode)) {
               subgraph.addEdge(node, successorNode, edge);
            }
         }
      }

      return subgraph;
   }

   public static <N> MutableGraph<N> copyOf(Graph<N> graph) {
      MutableGraph<N> copy = GraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
      Iterator var2 = graph.nodes().iterator();

      while(var2.hasNext()) {
         N node = var2.next();
         copy.addNode(node);
      }

      var2 = graph.edges().iterator();

      while(var2.hasNext()) {
         EndpointPair<N> edge = (EndpointPair)var2.next();
         copy.putEdge(edge.nodeU(), edge.nodeV());
      }

      return copy;
   }

   public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
      MutableValueGraph<N, V> copy = ValueGraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
      Iterator var2 = graph.nodes().iterator();

      while(var2.hasNext()) {
         N node = var2.next();
         copy.addNode(node);
      }

      var2 = graph.edges().iterator();

      while(var2.hasNext()) {
         EndpointPair<N> edge = (EndpointPair)var2.next();
         copy.putEdgeValue(edge.nodeU(), edge.nodeV(), java.util.Objects.requireNonNull(graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), (Object)null)));
      }

      return copy;
   }

   public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network) {
      MutableNetwork<N, E> copy = NetworkBuilder.from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
      Iterator var2 = network.nodes().iterator();

      Object edge;
      while(var2.hasNext()) {
         edge = var2.next();
         copy.addNode(edge);
      }

      var2 = network.edges().iterator();

      while(var2.hasNext()) {
         edge = var2.next();
         EndpointPair<N> endpointPair = network.incidentNodes(edge);
         copy.addEdge(endpointPair.nodeU(), endpointPair.nodeV(), edge);
      }

      return copy;
   }

   @CanIgnoreReturnValue
   static int checkNonNegative(int value) {
      Preconditions.checkArgument(value >= 0, "Not true that %s is non-negative.", value);
      return value;
   }

   @CanIgnoreReturnValue
   static long checkNonNegative(long value) {
      Preconditions.checkArgument(value >= 0L, "Not true that %s is non-negative.", value);
      return value;
   }

   @CanIgnoreReturnValue
   static int checkPositive(int value) {
      Preconditions.checkArgument(value > 0, "Not true that %s is positive.", value);
      return value;
   }

   @CanIgnoreReturnValue
   static long checkPositive(long value) {
      Preconditions.checkArgument(value > 0L, "Not true that %s is positive.", value);
      return value;
   }

   private static enum NodeVisitState {
      PENDING,
      COMPLETE;

      // $FF: synthetic method
      private static Graphs.NodeVisitState[] $values() {
         return new Graphs.NodeVisitState[]{PENDING, COMPLETE};
      }
   }

   private static class TransposedNetwork<N, E> extends ForwardingNetwork<N, E> {
      private final Network<N, E> network;

      TransposedNetwork(Network<N, E> network) {
         this.network = network;
      }

      Network<N, E> delegate() {
         return this.network;
      }

      public Set<N> predecessors(N node) {
         return this.delegate().successors(node);
      }

      public Set<N> successors(N node) {
         return this.delegate().predecessors(node);
      }

      public int inDegree(N node) {
         return this.delegate().outDegree(node);
      }

      public int outDegree(N node) {
         return this.delegate().inDegree(node);
      }

      public Set<E> inEdges(N node) {
         return this.delegate().outEdges(node);
      }

      public Set<E> outEdges(N node) {
         return this.delegate().inEdges(node);
      }

      public EndpointPair<N> incidentNodes(E edge) {
         EndpointPair<N> endpointPair = this.delegate().incidentNodes(edge);
         return EndpointPair.of(this.network, endpointPair.nodeV(), endpointPair.nodeU());
      }

      public Set<E> edgesConnecting(N nodeU, N nodeV) {
         return this.delegate().edgesConnecting(nodeV, nodeU);
      }

      public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
         return this.delegate().edgesConnecting(Graphs.transpose(endpoints));
      }

      public Optional<E> edgeConnecting(N nodeU, N nodeV) {
         return this.delegate().edgeConnecting(nodeV, nodeU);
      }

      public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
         return this.delegate().edgeConnecting(Graphs.transpose(endpoints));
      }

      @CheckForNull
      public E edgeConnectingOrNull(N nodeU, N nodeV) {
         return this.delegate().edgeConnectingOrNull(nodeV, nodeU);
      }

      @CheckForNull
      public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
         return this.delegate().edgeConnectingOrNull(Graphs.transpose(endpoints));
      }

      public boolean hasEdgeConnecting(N nodeU, N nodeV) {
         return this.delegate().hasEdgeConnecting(nodeV, nodeU);
      }

      public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
         return this.delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
      }
   }

   private static class TransposedValueGraph<N, V> extends ForwardingValueGraph<N, V> {
      private final ValueGraph<N, V> graph;

      TransposedValueGraph(ValueGraph<N, V> graph) {
         this.graph = graph;
      }

      ValueGraph<N, V> delegate() {
         return this.graph;
      }

      public Set<N> predecessors(N node) {
         return this.delegate().successors(node);
      }

      public Set<N> successors(N node) {
         return this.delegate().predecessors(node);
      }

      public int inDegree(N node) {
         return this.delegate().outDegree(node);
      }

      public int outDegree(N node) {
         return this.delegate().inDegree(node);
      }

      public boolean hasEdgeConnecting(N nodeU, N nodeV) {
         return this.delegate().hasEdgeConnecting(nodeV, nodeU);
      }

      public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
         return this.delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
      }

      public Optional<V> edgeValue(N nodeU, N nodeV) {
         return this.delegate().edgeValue(nodeV, nodeU);
      }

      public Optional<V> edgeValue(EndpointPair<N> endpoints) {
         return this.delegate().edgeValue(Graphs.transpose(endpoints));
      }

      @CheckForNull
      public V edgeValueOrDefault(N nodeU, N nodeV, @CheckForNull V defaultValue) {
         return this.delegate().edgeValueOrDefault(nodeV, nodeU, defaultValue);
      }

      @CheckForNull
      public V edgeValueOrDefault(EndpointPair<N> endpoints, @CheckForNull V defaultValue) {
         return this.delegate().edgeValueOrDefault(Graphs.transpose(endpoints), defaultValue);
      }
   }

   private static class TransposedGraph<N> extends ForwardingGraph<N> {
      private final Graph<N> graph;

      TransposedGraph(Graph<N> graph) {
         this.graph = graph;
      }

      Graph<N> delegate() {
         return this.graph;
      }

      public Set<N> predecessors(N node) {
         return this.delegate().successors(node);
      }

      public Set<N> successors(N node) {
         return this.delegate().predecessors(node);
      }

      public Set<EndpointPair<N>> incidentEdges(N node) {
         return new IncidentEdgeSet<N>(this, node) {
            public Iterator<EndpointPair<N>> iterator() {
               return Iterators.transform(TransposedGraph.this.delegate().incidentEdges(this.node).iterator(), new Function<EndpointPair<N>, EndpointPair<N>>() {
                  public EndpointPair<N> apply(EndpointPair<N> edge) {
                     return EndpointPair.of(TransposedGraph.this.delegate(), edge.nodeV(), edge.nodeU());
                  }
               });
            }
         };
      }

      public int inDegree(N node) {
         return this.delegate().outDegree(node);
      }

      public int outDegree(N node) {
         return this.delegate().inDegree(node);
      }

      public boolean hasEdgeConnecting(N nodeU, N nodeV) {
         return this.delegate().hasEdgeConnecting(nodeV, nodeU);
      }

      public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
         return this.delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
      }
   }
}
