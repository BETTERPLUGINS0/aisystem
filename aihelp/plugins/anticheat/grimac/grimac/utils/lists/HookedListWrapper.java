package ac.grim.grimac.utils.lists;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class HookedListWrapper<T> extends ListWrapper<T> {
   public HookedListWrapper(List<T> base) {
      super(base);
   }

   public abstract void onIterator();

   public int size() {
      return this.base.size();
   }

   public boolean isEmpty() {
      return this.base.isEmpty();
   }

   public boolean contains(Object o) {
      return this.base.contains(o);
   }

   @NotNull
   public Iterator<T> iterator() {
      this.onIterator();
      return this.listIterator();
   }

   @NotNull
   public Object[] toArray() {
      return this.base.toArray();
   }

   public boolean add(T o) {
      return this.base.add(o);
   }

   public boolean remove(Object o) {
      return this.base.remove(o);
   }

   public boolean addAll(@NotNull Collection c) {
      return this.base.addAll(c);
   }

   public boolean addAll(int index, @NotNull Collection c) {
      return this.base.addAll(index, c);
   }

   public void clear() {
      this.base.clear();
   }

   public T get(int index) {
      return this.base.get(index);
   }

   public T set(int index, T element) {
      return this.base.set(index, element);
   }

   public void add(int index, T element) {
      this.base.add(index, element);
   }

   public T remove(int index) {
      return this.base.remove(index);
   }

   public int indexOf(Object o) {
      return this.base.indexOf(o);
   }

   public int lastIndexOf(Object o) {
      return this.base.lastIndexOf(o);
   }

   @NotNull
   public ListIterator<T> listIterator() {
      return this.base.listIterator();
   }

   @NotNull
   public ListIterator<T> listIterator(int index) {
      return this.base.listIterator(index);
   }

   @NotNull
   public List<T> subList(int fromIndex, int toIndex) {
      return this.base.subList(fromIndex, toIndex);
   }

   public boolean retainAll(@NotNull Collection c) {
      return this.base.retainAll(c);
   }

   public boolean removeAll(@NotNull Collection c) {
      return this.base.removeAll(c);
   }

   public boolean containsAll(@NotNull Collection c) {
      return this.base.containsAll(c);
   }

   @NotNull
   public Object[] toArray(@NotNull Object[] a) {
      return this.base.toArray(a);
   }
}
