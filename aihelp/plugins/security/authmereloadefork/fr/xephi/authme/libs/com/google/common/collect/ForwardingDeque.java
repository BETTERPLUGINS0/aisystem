package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Deque;
import java.util.Iterator;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class ForwardingDeque<E> extends ForwardingQueue<E> implements Deque<E> {
   protected ForwardingDeque() {
   }

   protected abstract Deque<E> delegate();

   public void addFirst(@ParametricNullness E e) {
      this.delegate().addFirst(e);
   }

   public void addLast(@ParametricNullness E e) {
      this.delegate().addLast(e);
   }

   public Iterator<E> descendingIterator() {
      return this.delegate().descendingIterator();
   }

   @ParametricNullness
   public E getFirst() {
      return this.delegate().getFirst();
   }

   @ParametricNullness
   public E getLast() {
      return this.delegate().getLast();
   }

   @CanIgnoreReturnValue
   public boolean offerFirst(@ParametricNullness E e) {
      return this.delegate().offerFirst(e);
   }

   @CanIgnoreReturnValue
   public boolean offerLast(@ParametricNullness E e) {
      return this.delegate().offerLast(e);
   }

   @CheckForNull
   public E peekFirst() {
      return this.delegate().peekFirst();
   }

   @CheckForNull
   public E peekLast() {
      return this.delegate().peekLast();
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public E pollFirst() {
      return this.delegate().pollFirst();
   }

   @CheckForNull
   @CanIgnoreReturnValue
   public E pollLast() {
      return this.delegate().pollLast();
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   public E pop() {
      return this.delegate().pop();
   }

   public void push(@ParametricNullness E e) {
      this.delegate().push(e);
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   public E removeFirst() {
      return this.delegate().removeFirst();
   }

   @ParametricNullness
   @CanIgnoreReturnValue
   public E removeLast() {
      return this.delegate().removeLast();
   }

   @CanIgnoreReturnValue
   public boolean removeFirstOccurrence(@CheckForNull Object o) {
      return this.delegate().removeFirstOccurrence(o);
   }

   @CanIgnoreReturnValue
   public boolean removeLastOccurrence(@CheckForNull Object o) {
      return this.delegate().removeLastOccurrence(o);
   }
}
