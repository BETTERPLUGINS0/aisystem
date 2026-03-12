package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSet;
import fr.xephi.authme.libs.com.google.common.collect.Iterators;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Map;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
final class EdgesConnecting<E> extends AbstractSet<E> {
   private final Map<?, E> nodeToOutEdge;
   private final Object targetNode;

   EdgesConnecting(Map<?, E> nodeToEdgeMap, Object targetNode) {
      this.nodeToOutEdge = (Map)Preconditions.checkNotNull(nodeToEdgeMap);
      this.targetNode = Preconditions.checkNotNull(targetNode);
   }

   public UnmodifiableIterator<E> iterator() {
      E connectingEdge = this.getConnectingEdge();
      return connectingEdge == null ? ImmutableSet.of().iterator() : Iterators.singletonIterator(connectingEdge);
   }

   public int size() {
      return this.getConnectingEdge() == null ? 0 : 1;
   }

   public boolean contains(@CheckForNull Object edge) {
      E connectingEdge = this.getConnectingEdge();
      return connectingEdge != null && connectingEdge.equals(edge);
   }

   @CheckForNull
   private E getConnectingEdge() {
      return this.nodeToOutEdge.get(this.targetNode);
   }
}
