package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.NoSuchElementException;
import java.util.Queue;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingQueue<E> extends ForwardingCollection<E> implements Queue<E> {
   protected ForwardingQueue() {
   }

   protected abstract Queue<E> delegate();

   @CanIgnoreReturnValue
   public boolean offer(@ParametricNullness E o) {
      return this.delegate().offer(o);
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public E poll() {
      return this.delegate().poll();
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   public E remove() {
      return this.delegate().remove();
   }

   @CheckForNull
   public E peek() {
      return this.delegate().peek();
   }

   @ParametricNullness
   public E element() {
      return this.delegate().element();
   }

   protected boolean standardOffer(@ParametricNullness E e) {
      try {
         return this.add(e);
      } catch (IllegalStateException var3) {
         return false;
      }
   }

   @CheckForNull
   protected E standardPeek() {
      try {
         return this.element();
      } catch (NoSuchElementException var2) {
         return null;
      }
   }

   @CheckForNull
   protected E standardPoll() {
      try {
         return this.remove();
      } catch (NoSuchElementException var2) {
         return null;
      }
   }
}
