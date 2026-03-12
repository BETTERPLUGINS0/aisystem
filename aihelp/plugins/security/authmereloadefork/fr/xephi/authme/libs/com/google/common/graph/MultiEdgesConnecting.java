package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.AbstractIterator;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
abstract class MultiEdgesConnecting<E> extends AbstractSet<E> {
   private final Map<E, ?> outEdgeToNode;
   private final Object targetNode;

   MultiEdgesConnecting(Map<E, ?> outEdgeToNode, Object targetNode) {
      this.outEdgeToNode = (Map)Preconditions.checkNotNull(outEdgeToNode);
      this.targetNode = Preconditions.checkNotNull(targetNode);
   }

   public UnmodifiableIterator<E> iterator() {
      final Iterator<? extends Entry<E, ?>> entries = this.outEdgeToNode.entrySet().iterator();
      return new AbstractIterator<E>() {
         @CheckForNull
         protected E computeNext() {
            while(true) {
               if (entries.hasNext()) {
                  Entry<E, ?> entry = (Entry)entries.next();
                  if (!MultiEdgesConnecting.this.targetNode.equals(entry.getValue())) {
                     continue;
                  }

                  return entry.getKey();
               }

               return this.endOfData();
            }
         }
      };
   }

   public boolean contains(@CheckForNull Object edge) {
      return this.targetNode.equals(this.outEdgeToNode.get(edge));
   }
}
