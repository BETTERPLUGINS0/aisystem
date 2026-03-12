package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Queue;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class ConsumingQueueIterator<T> extends AbstractIterator<T> {
   private final Queue<T> queue;

   ConsumingQueueIterator(Queue<T> queue) {
      this.queue = (Queue)Preconditions.checkNotNull(queue);
   }

   @CheckForNull
   public T computeNext() {
      return this.queue.isEmpty() ? this.endOfData() : this.queue.remove();
   }
}
