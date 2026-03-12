package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.Iterators;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import javax.annotation.CheckForNull;

@Immutable(
   containerOf = {"N"}
)
@ElementTypesAreNonnullByDefault
@Beta
public abstract class EndpointPair<N> implements Iterable<N> {
   private final N nodeU;
   private final N nodeV;

   private EndpointPair(N nodeU, N nodeV) {
      this.nodeU = Preconditions.checkNotNull(nodeU);
      this.nodeV = Preconditions.checkNotNull(nodeV);
   }

   public static <N> EndpointPair<N> ordered(N source, N target) {
      return new EndpointPair.Ordered(source, target);
   }

   public static <N> EndpointPair<N> unordered(N nodeU, N nodeV) {
      return new EndpointPair.Unordered(nodeV, nodeU);
   }

   static <N> EndpointPair<N> of(Graph<?> graph, N nodeU, N nodeV) {
      return graph.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
   }

   static <N> EndpointPair<N> of(Network<?, ?> network, N nodeU, N nodeV) {
      return network.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
   }

   public abstract N source();

   public abstract N target();

   public final N nodeU() {
      return this.nodeU;
   }

   public final N nodeV() {
      return this.nodeV;
   }

   public final N adjacentNode(N node) {
      if (node.equals(this.nodeU)) {
         return this.nodeV;
      } else if (node.equals(this.nodeV)) {
         return this.nodeU;
      } else {
         String var2 = String.valueOf(this);
         String var3 = String.valueOf(node);
         throw new IllegalArgumentException((new StringBuilder(36 + String.valueOf(var2).length() + String.valueOf(var3).length())).append("EndpointPair ").append(var2).append(" does not contain node ").append(var3).toString());
      }
   }

   public abstract boolean isOrdered();

   public final UnmodifiableIterator<N> iterator() {
      return Iterators.forArray(this.nodeU, this.nodeV);
   }

   public abstract boolean equals(@CheckForNull Object var1);

   public abstract int hashCode();

   // $FF: synthetic method
   EndpointPair(Object x0, Object x1, Object x2) {
      this(x0, x1);
   }

   private static final class Unordered<N> extends EndpointPair<N> {
      private Unordered(N nodeU, N nodeV) {
         super(nodeU, nodeV, null);
      }

      public N source() {
         throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
      }

      public N target() {
         throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
      }

      public boolean isOrdered() {
         return false;
      }

      public boolean equals(@CheckForNull Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof EndpointPair)) {
            return false;
         } else {
            EndpointPair<?> other = (EndpointPair)obj;
            if (this.isOrdered() != other.isOrdered()) {
               return false;
            } else if (this.nodeU().equals(other.nodeU())) {
               return this.nodeV().equals(other.nodeV());
            } else {
               return this.nodeU().equals(other.nodeV()) && this.nodeV().equals(other.nodeU());
            }
         }
      }

      public int hashCode() {
         return this.nodeU().hashCode() + this.nodeV().hashCode();
      }

      public String toString() {
         String var1 = String.valueOf(this.nodeU());
         String var2 = String.valueOf(this.nodeV());
         return (new StringBuilder(4 + String.valueOf(var1).length() + String.valueOf(var2).length())).append("[").append(var1).append(", ").append(var2).append("]").toString();
      }

      // $FF: synthetic method
      Unordered(Object x0, Object x1, Object x2) {
         this(x0, x1);
      }
   }

   private static final class Ordered<N> extends EndpointPair<N> {
      private Ordered(N source, N target) {
         super(source, target, null);
      }

      public N source() {
         return this.nodeU();
      }

      public N target() {
         return this.nodeV();
      }

      public boolean isOrdered() {
         return true;
      }

      public boolean equals(@CheckForNull Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof EndpointPair)) {
            return false;
         } else {
            EndpointPair<?> other = (EndpointPair)obj;
            if (this.isOrdered() != other.isOrdered()) {
               return false;
            } else {
               return this.source().equals(other.source()) && this.target().equals(other.target());
            }
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.source(), this.target());
      }

      public String toString() {
         String var1 = String.valueOf(this.source());
         String var2 = String.valueOf(this.target());
         return (new StringBuilder(6 + String.valueOf(var1).length() + String.valueOf(var2).length())).append("<").append(var1).append(" -> ").append(var2).append(">").toString();
      }

      // $FF: synthetic method
      Ordered(Object x0, Object x1, Object x2) {
         this(x0, x1);
      }
   }
}
