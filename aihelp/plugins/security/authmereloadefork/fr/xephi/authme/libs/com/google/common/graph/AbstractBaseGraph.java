package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSet;
import fr.xephi.authme.libs.com.google.common.collect.Iterators;
import fr.xephi.authme.libs.com.google.common.collect.Sets;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import fr.xephi.authme.libs.com.google.common.math.IntMath;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
abstract class AbstractBaseGraph<N> implements BaseGraph<N> {
   protected long edgeCount() {
      long degreeSum = 0L;

      Object node;
      for(Iterator var3 = this.nodes().iterator(); var3.hasNext(); degreeSum += (long)this.degree(node)) {
         node = var3.next();
      }

      Preconditions.checkState((degreeSum & 1L) == 0L);
      return degreeSum >>> 1;
   }

   public Set<EndpointPair<N>> edges() {
      return new AbstractSet<EndpointPair<N>>() {
         public UnmodifiableIterator<EndpointPair<N>> iterator() {
            return EndpointPairIterator.of(AbstractBaseGraph.this);
         }

         public int size() {
            return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
         }

         public boolean remove(@CheckForNull Object o) {
            throw new UnsupportedOperationException();
         }

         public boolean contains(@CheckForNull Object obj) {
            if (!(obj instanceof EndpointPair)) {
               return false;
            } else {
               EndpointPair<?> endpointPair = (EndpointPair)obj;
               return AbstractBaseGraph.this.isOrderingCompatible(endpointPair) && AbstractBaseGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractBaseGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
            }
         }
      };
   }

   public ElementOrder<N> incidentEdgeOrder() {
      return ElementOrder.unordered();
   }

   public Set<EndpointPair<N>> incidentEdges(N node) {
      Preconditions.checkNotNull(node);
      Preconditions.checkArgument(this.nodes().contains(node), "Node %s is not an element of this graph.", node);
      return new IncidentEdgeSet<N>(this, this, node) {
         public UnmodifiableIterator<EndpointPair<N>> iterator() {
            return this.graph.isDirected() ? Iterators.unmodifiableIterator(Iterators.concat(Iterators.transform(this.graph.predecessors(this.node).iterator(), (predecessor) -> {
               return EndpointPair.ordered(predecessor, this.node);
            }), Iterators.transform(Sets.difference(this.graph.successors(this.node), ImmutableSet.of(this.node)).iterator(), (successor) -> {
               return EndpointPair.ordered(this.node, successor);
            }))) : Iterators.unmodifiableIterator(Iterators.transform(this.graph.adjacentNodes(this.node).iterator(), (adjacentNode) -> {
               return EndpointPair.unordered(this.node, adjacentNode);
            }));
         }
      };
   }

   public int degree(N node) {
      if (this.isDirected()) {
         return IntMath.saturatedAdd(this.predecessors(node).size(), this.successors(node).size());
      } else {
         Set<N> neighbors = this.adjacentNodes(node);
         int selfLoopCount = this.allowsSelfLoops() && neighbors.contains(node) ? 1 : 0;
         return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
      }
   }

   public int inDegree(N node) {
      return this.isDirected() ? this.predecessors(node).size() : this.degree(node);
   }

   public int outDegree(N node) {
      return this.isDirected() ? this.successors(node).size() : this.degree(node);
   }

   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
      Preconditions.checkNotNull(nodeU);
      Preconditions.checkNotNull(nodeV);
      return this.nodes().contains(nodeU) && this.successors(nodeU).contains(nodeV);
   }

   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
      Preconditions.checkNotNull(endpoints);
      if (!this.isOrderingCompatible(endpoints)) {
         return false;
      } else {
         N nodeU = endpoints.nodeU();
         N nodeV = endpoints.nodeV();
         return this.nodes().contains(nodeU) && this.successors(nodeU).contains(nodeV);
      }
   }

   protected final void validateEndpoints(EndpointPair<?> endpoints) {
      Preconditions.checkNotNull(endpoints);
      Preconditions.checkArgument(this.isOrderingCompatible(endpoints), "Mismatch: unordered endpoints cannot be used with directed graphs");
   }

   protected final boolean isOrderingCompatible(EndpointPair<?> endpoints) {
      return endpoints.isOrdered() || !this.isDirected();
   }
}
