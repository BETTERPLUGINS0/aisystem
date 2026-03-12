package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
class StandardValueGraph<N, V> extends AbstractValueGraph<N, V> {
   private final boolean isDirected;
   private final boolean allowsSelfLoops;
   private final ElementOrder<N> nodeOrder;
   final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
   long edgeCount;

   StandardValueGraph(AbstractGraphBuilder<? super N> builder) {
      this(builder, builder.nodeOrder.createMap((Integer)builder.expectedNodeCount.or((int)10)), 0L);
   }

   StandardValueGraph(AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount) {
      this.isDirected = builder.directed;
      this.allowsSelfLoops = builder.allowsSelfLoops;
      this.nodeOrder = builder.nodeOrder.cast();
      this.nodeConnections = (MapIteratorCache)(nodeConnections instanceof TreeMap ? new MapRetrievalCache(nodeConnections) : new MapIteratorCache(nodeConnections));
      this.edgeCount = Graphs.checkNonNegative(edgeCount);
   }

   public Set<N> nodes() {
      return this.nodeConnections.unmodifiableKeySet();
   }

   public boolean isDirected() {
      return this.isDirected;
   }

   public boolean allowsSelfLoops() {
      return this.allowsSelfLoops;
   }

   public ElementOrder<N> nodeOrder() {
      return this.nodeOrder;
   }

   public Set<N> adjacentNodes(N node) {
      return this.checkedConnections(node).adjacentNodes();
   }

   public Set<N> predecessors(N node) {
      return this.checkedConnections(node).predecessors();
   }

   public Set<N> successors(N node) {
      return this.checkedConnections(node).successors();
   }

   public Set<EndpointPair<N>> incidentEdges(N node) {
      final GraphConnections<N, V> connections = this.checkedConnections(node);
      return new IncidentEdgeSet<N>(this, this, node) {
         public Iterator<EndpointPair<N>> iterator() {
            return connections.incidentEdgeIterator(this.node);
         }
      };
   }

   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
      return this.hasEdgeConnectingInternal(Preconditions.checkNotNull(nodeU), Preconditions.checkNotNull(nodeV));
   }

   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
      Preconditions.checkNotNull(endpoints);
      return this.isOrderingCompatible(endpoints) && this.hasEdgeConnectingInternal(endpoints.nodeU(), endpoints.nodeV());
   }

   @CheckForNull
   public V edgeValueOrDefault(N nodeU, N nodeV, @CheckForNull V defaultValue) {
      return this.edgeValueOrDefaultInternal(Preconditions.checkNotNull(nodeU), Preconditions.checkNotNull(nodeV), defaultValue);
   }

   @CheckForNull
   public V edgeValueOrDefault(EndpointPair<N> endpoints, @CheckForNull V defaultValue) {
      this.validateEndpoints(endpoints);
      return this.edgeValueOrDefaultInternal(endpoints.nodeU(), endpoints.nodeV(), defaultValue);
   }

   protected long edgeCount() {
      return this.edgeCount;
   }

   private final GraphConnections<N, V> checkedConnections(N node) {
      GraphConnections<N, V> connections = (GraphConnections)this.nodeConnections.get(node);
      if (connections == null) {
         Preconditions.checkNotNull(node);
         String var3 = String.valueOf(node);
         throw new IllegalArgumentException((new StringBuilder(38 + String.valueOf(var3).length())).append("Node ").append(var3).append(" is not an element of this graph.").toString());
      } else {
         return connections;
      }
   }

   final boolean containsNode(@CheckForNull N node) {
      return this.nodeConnections.containsKey(node);
   }

   private final boolean hasEdgeConnectingInternal(N nodeU, N nodeV) {
      GraphConnections<N, V> connectionsU = (GraphConnections)this.nodeConnections.get(nodeU);
      return connectionsU != null && connectionsU.successors().contains(nodeV);
   }

   @CheckForNull
   private final V edgeValueOrDefaultInternal(N nodeU, N nodeV, @CheckForNull V defaultValue) {
      GraphConnections<N, V> connectionsU = (GraphConnections)this.nodeConnections.get(nodeU);
      V value = connectionsU == null ? null : connectionsU.value(nodeV);
      return value == null ? defaultValue : value;
   }
}
