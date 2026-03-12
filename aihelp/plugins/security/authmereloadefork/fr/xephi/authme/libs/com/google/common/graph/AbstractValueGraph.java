package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
public abstract class AbstractValueGraph<N, V> extends AbstractBaseGraph<N> implements ValueGraph<N, V> {
   public Graph<N> asGraph() {
      return new AbstractGraph<N>() {
         public Set<N> nodes() {
            return AbstractValueGraph.this.nodes();
         }

         public Set<EndpointPair<N>> edges() {
            return AbstractValueGraph.this.edges();
         }

         public boolean isDirected() {
            return AbstractValueGraph.this.isDirected();
         }

         public boolean allowsSelfLoops() {
            return AbstractValueGraph.this.allowsSelfLoops();
         }

         public ElementOrder<N> nodeOrder() {
            return AbstractValueGraph.this.nodeOrder();
         }

         public ElementOrder<N> incidentEdgeOrder() {
            return AbstractValueGraph.this.incidentEdgeOrder();
         }

         public Set<N> adjacentNodes(N node) {
            return AbstractValueGraph.this.adjacentNodes(node);
         }

         public Set<N> predecessors(N node) {
            return AbstractValueGraph.this.predecessors(node);
         }

         public Set<N> successors(N node) {
            return AbstractValueGraph.this.successors(node);
         }

         public int degree(N node) {
            return AbstractValueGraph.this.degree(node);
         }

         public int inDegree(N node) {
            return AbstractValueGraph.this.inDegree(node);
         }

         public int outDegree(N node) {
            return AbstractValueGraph.this.outDegree(node);
         }
      };
   }

   public Optional<V> edgeValue(N nodeU, N nodeV) {
      return Optional.ofNullable(this.edgeValueOrDefault(nodeU, nodeV, (Object)null));
   }

   public Optional<V> edgeValue(EndpointPair<N> endpoints) {
      return Optional.ofNullable(this.edgeValueOrDefault(endpoints, (Object)null));
   }

   public final boolean equals(@CheckForNull Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof ValueGraph)) {
         return false;
      } else {
         ValueGraph<?, ?> other = (ValueGraph)obj;
         return this.isDirected() == other.isDirected() && this.nodes().equals(other.nodes()) && edgeValueMap(this).equals(edgeValueMap(other));
      }
   }

   public final int hashCode() {
      return edgeValueMap(this).hashCode();
   }

   public String toString() {
      boolean var1 = this.isDirected();
      boolean var2 = this.allowsSelfLoops();
      String var3 = String.valueOf(this.nodes());
      String var4 = String.valueOf(edgeValueMap(this));
      return (new StringBuilder(59 + String.valueOf(var3).length() + String.valueOf(var4).length())).append("isDirected: ").append(var1).append(", allowsSelfLoops: ").append(var2).append(", nodes: ").append(var3).append(", edges: ").append(var4).toString();
   }

   private static <N, V> Map<EndpointPair<N>, V> edgeValueMap(final ValueGraph<N, V> graph) {
      Function<EndpointPair<N>, V> edgeToValueFn = new Function<EndpointPair<N>, V>() {
         public V apply(EndpointPair<N> edge) {
            return Objects.requireNonNull(graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), (Object)null));
         }
      };
      return Maps.asMap(graph.edges(), edgeToValueFn);
   }
}
