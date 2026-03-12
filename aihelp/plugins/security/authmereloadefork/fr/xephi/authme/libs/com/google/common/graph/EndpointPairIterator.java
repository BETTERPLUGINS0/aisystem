package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.AbstractIterator;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSet;
import fr.xephi.authme.libs.com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
abstract class EndpointPairIterator<N> extends AbstractIterator<EndpointPair<N>> {
   private final BaseGraph<N> graph;
   private final Iterator<N> nodeIterator;
   @CheckForNull
   N node;
   Iterator<N> successorIterator;

   static <N> EndpointPairIterator<N> of(BaseGraph<N> graph) {
      return (EndpointPairIterator)(graph.isDirected() ? new EndpointPairIterator.Directed(graph) : new EndpointPairIterator.Undirected(graph));
   }

   private EndpointPairIterator(BaseGraph<N> graph) {
      this.node = null;
      this.successorIterator = ImmutableSet.of().iterator();
      this.graph = graph;
      this.nodeIterator = graph.nodes().iterator();
   }

   final boolean advance() {
      Preconditions.checkState(!this.successorIterator.hasNext());
      if (!this.nodeIterator.hasNext()) {
         return false;
      } else {
         this.node = this.nodeIterator.next();
         this.successorIterator = this.graph.successors(this.node).iterator();
         return true;
      }
   }

   // $FF: synthetic method
   EndpointPairIterator(BaseGraph x0, Object x1) {
      this(x0);
   }

   private static final class Undirected<N> extends EndpointPairIterator<N> {
      @CheckForNull
      private Set<N> visitedNodes;

      private Undirected(BaseGraph<N> graph) {
         super(graph, null);
         this.visitedNodes = Sets.newHashSetWithExpectedSize(graph.nodes().size() + 1);
      }

      @CheckForNull
      protected EndpointPair<N> computeNext() {
         label18:
         while(true) {
            Objects.requireNonNull(this.visitedNodes);

            Object otherNode;
            do {
               if (!this.successorIterator.hasNext()) {
                  this.visitedNodes.add(this.node);
                  if (this.advance()) {
                     continue label18;
                  }

                  this.visitedNodes = null;
                  return (EndpointPair)this.endOfData();
               }

               otherNode = this.successorIterator.next();
            } while(this.visitedNodes.contains(otherNode));

            return EndpointPair.unordered(Objects.requireNonNull(this.node), otherNode);
         }
      }

      // $FF: synthetic method
      Undirected(BaseGraph x0, Object x1) {
         this(x0);
      }
   }

   private static final class Directed<N> extends EndpointPairIterator<N> {
      private Directed(BaseGraph<N> graph) {
         super(graph, null);
      }

      @CheckForNull
      protected EndpointPair<N> computeNext() {
         do {
            if (this.successorIterator.hasNext()) {
               return EndpointPair.ordered(Objects.requireNonNull(this.node), this.successorIterator.next());
            }
         } while(this.advance());

         return (EndpointPair)this.endOfData();
      }

      // $FF: synthetic method
      Directed(BaseGraph x0, Object x1) {
         this(x0);
      }
   }
}
