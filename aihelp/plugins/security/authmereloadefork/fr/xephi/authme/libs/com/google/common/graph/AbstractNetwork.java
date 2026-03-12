package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Predicate;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSet;
import fr.xephi.authme.libs.com.google.common.collect.Iterators;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import fr.xephi.authme.libs.com.google.common.collect.Sets;
import fr.xephi.authme.libs.com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
public abstract class AbstractNetwork<N, E> implements Network<N, E> {
   public Graph<N> asGraph() {
      return new AbstractGraph<N>() {
         public Set<N> nodes() {
            return AbstractNetwork.this.nodes();
         }

         public Set<EndpointPair<N>> edges() {
            return (Set)(AbstractNetwork.this.allowsParallelEdges() ? super.edges() : new AbstractSet<EndpointPair<N>>() {
               public Iterator<EndpointPair<N>> iterator() {
                  return Iterators.transform(AbstractNetwork.this.edges().iterator(), new Function<E, EndpointPair<N>>() {
                     public EndpointPair<N> apply(E edge) {
                        return AbstractNetwork.this.incidentNodes(edge);
                     }
                  });
               }

               public int size() {
                  return AbstractNetwork.this.edges().size();
               }

               public boolean contains(@CheckForNull Object obj) {
                  if (!(obj instanceof EndpointPair)) {
                     return false;
                  } else {
                     EndpointPair<?> endpointPair = (EndpointPair)obj;
                     return isOrderingCompatible(endpointPair) && nodes().contains(endpointPair.nodeU()) && successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
                  }
               }
            });
         }

         public ElementOrder<N> nodeOrder() {
            return AbstractNetwork.this.nodeOrder();
         }

         public ElementOrder<N> incidentEdgeOrder() {
            return ElementOrder.unordered();
         }

         public boolean isDirected() {
            return AbstractNetwork.this.isDirected();
         }

         public boolean allowsSelfLoops() {
            return AbstractNetwork.this.allowsSelfLoops();
         }

         public Set<N> adjacentNodes(N node) {
            return AbstractNetwork.this.adjacentNodes(node);
         }

         public Set<N> predecessors(N node) {
            return AbstractNetwork.this.predecessors(node);
         }

         public Set<N> successors(N node) {
            return AbstractNetwork.this.successors(node);
         }
      };
   }

   public int degree(N node) {
      return this.isDirected() ? IntMath.saturatedAdd(this.inEdges(node).size(), this.outEdges(node).size()) : IntMath.saturatedAdd(this.incidentEdges(node).size(), this.edgesConnecting(node, node).size());
   }

   public int inDegree(N node) {
      return this.isDirected() ? this.inEdges(node).size() : this.degree(node);
   }

   public int outDegree(N node) {
      return this.isDirected() ? this.outEdges(node).size() : this.degree(node);
   }

   public Set<E> adjacentEdges(E edge) {
      EndpointPair<N> endpointPair = this.incidentNodes(edge);
      Set<E> endpointPairIncidentEdges = Sets.union(this.incidentEdges(endpointPair.nodeU()), this.incidentEdges(endpointPair.nodeV()));
      return Sets.difference(endpointPairIncidentEdges, ImmutableSet.of(edge));
   }

   public Set<E> edgesConnecting(N nodeU, N nodeV) {
      Set<E> outEdgesU = this.outEdges(nodeU);
      Set<E> inEdgesV = this.inEdges(nodeV);
      return outEdgesU.size() <= inEdgesV.size() ? Collections.unmodifiableSet(Sets.filter(outEdgesU, this.connectedPredicate(nodeU, nodeV))) : Collections.unmodifiableSet(Sets.filter(inEdgesV, this.connectedPredicate(nodeV, nodeU)));
   }

   public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
      this.validateEndpoints(endpoints);
      return this.edgesConnecting(endpoints.nodeU(), endpoints.nodeV());
   }

   private Predicate<E> connectedPredicate(final N nodePresent, final N nodeToCheck) {
      return new Predicate<E>() {
         public boolean apply(E edge) {
            return AbstractNetwork.this.incidentNodes(edge).adjacentNode(nodePresent).equals(nodeToCheck);
         }
      };
   }

   public Optional<E> edgeConnecting(N nodeU, N nodeV) {
      return Optional.ofNullable(this.edgeConnectingOrNull(nodeU, nodeV));
   }

   public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
      this.validateEndpoints(endpoints);
      return this.edgeConnecting(endpoints.nodeU(), endpoints.nodeV());
   }

   @CheckForNull
   public E edgeConnectingOrNull(N nodeU, N nodeV) {
      Set<E> edgesConnecting = this.edgesConnecting(nodeU, nodeV);
      switch(edgesConnecting.size()) {
      case 0:
         return null;
      case 1:
         return edgesConnecting.iterator().next();
      default:
         throw new IllegalArgumentException(String.format("Cannot call edgeConnecting() when parallel edges exist between %s and %s. Consider calling edgesConnecting() instead.", nodeU, nodeV));
      }
   }

   @CheckForNull
   public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
      this.validateEndpoints(endpoints);
      return this.edgeConnectingOrNull(endpoints.nodeU(), endpoints.nodeV());
   }

   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
      Preconditions.checkNotNull(nodeU);
      Preconditions.checkNotNull(nodeV);
      return this.nodes().contains(nodeU) && this.successors(nodeU).contains(nodeV);
   }

   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
      Preconditions.checkNotNull(endpoints);
      return !this.isOrderingCompatible(endpoints) ? false : this.hasEdgeConnecting(endpoints.nodeU(), endpoints.nodeV());
   }

   protected final void validateEndpoints(EndpointPair<?> endpoints) {
      Preconditions.checkNotNull(endpoints);
      Preconditions.checkArgument(this.isOrderingCompatible(endpoints), "Mismatch: unordered endpoints cannot be used with directed graphs");
   }

   protected final boolean isOrderingCompatible(EndpointPair<?> endpoints) {
      return endpoints.isOrdered() || !this.isDirected();
   }

   public final boolean equals(@CheckForNull Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Network)) {
         return false;
      } else {
         Network<?, ?> other = (Network)obj;
         return this.isDirected() == other.isDirected() && this.nodes().equals(other.nodes()) && edgeIncidentNodesMap(this).equals(edgeIncidentNodesMap(other));
      }
   }

   public final int hashCode() {
      return edgeIncidentNodesMap(this).hashCode();
   }

   public String toString() {
      boolean var1 = this.isDirected();
      boolean var2 = this.allowsParallelEdges();
      boolean var3 = this.allowsSelfLoops();
      String var4 = String.valueOf(this.nodes());
      String var5 = String.valueOf(edgeIncidentNodesMap(this));
      return (new StringBuilder(87 + String.valueOf(var4).length() + String.valueOf(var5).length())).append("isDirected: ").append(var1).append(", allowsParallelEdges: ").append(var2).append(", allowsSelfLoops: ").append(var3).append(", nodes: ").append(var4).append(", edges: ").append(var5).toString();
   }

   private static <N, E> Map<E, EndpointPair<N>> edgeIncidentNodesMap(final Network<N, E> network) {
      Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function<E, EndpointPair<N>>() {
         public EndpointPair<N> apply(E edge) {
            return network.incidentNodes(edge);
         }
      };
      return Maps.asMap(network.edges(), edgeToIncidentNodesFn);
   }
}
