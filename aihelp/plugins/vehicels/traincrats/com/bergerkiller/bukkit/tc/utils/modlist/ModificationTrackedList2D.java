package com.bergerkiller.bukkit.tc.utils.modlist;

import com.bergerkiller.bukkit.common.collections.CollectionBasics;
import com.bergerkiller.bukkit.common.collections.List2DIterator;
import com.bergerkiller.bukkit.common.collections.List2DListIterator;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ModificationTrackedList2D<E> extends AbstractList<E> implements ModificationTrackedList<E> {
   private final ArrayList<List<E>> lists = new ArrayList();

   public void resetLists() {
      this.lists.clear();
      ++this.modCount;
   }

   public void addListIfNotEmpty(List<E> list) {
      if (!list.isEmpty()) {
         this.lists.add(list);
         ++this.modCount;
      }

   }

   public void removeList(List<E> list) {
      Iterator iter = this.lists.iterator();

      while(iter.hasNext()) {
         if (iter.next() == list) {
            iter.remove();
            ++this.modCount;
            break;
         }
      }

   }

   public int getModCount() {
      return this.modCount;
   }

   public boolean add(E e) {
      Iterator<List<E>> iter = this.lists.iterator();

      List rval;
      for(rval = null; iter.hasNext(); rval = (List)iter.next()) {
      }

      ++this.modCount;
      return rval.add(e);
   }

   public int size() {
      int size = 0;

      List list;
      for(Iterator var2 = this.lists.iterator(); var2.hasNext(); size += list.size()) {
         list = (List)var2.next();
      }

      return size;
   }

   public boolean isEmpty() {
      Iterator var1 = this.lists.iterator();

      List list;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         list = (List)var1.next();
      } while(list.isEmpty());

      return false;
   }

   public boolean contains(Object o) {
      Iterator var2 = this.lists.iterator();

      List list;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         list = (List)var2.next();
      } while(!list.contains(o));

      return true;
   }

   public Iterator<E> iterator() {
      return new List2DIterator(this.lists);
   }

   public Object[] toArray() {
      return CollectionBasics.toArray(this);
   }

   public <T> T[] toArray(T[] array) {
      return CollectionBasics.toArray(this, array);
   }

   public boolean remove(Object o) {
      Iterator var2 = this.lists.iterator();

      List list;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         list = (List)var2.next();
      } while(!list.remove(o));

      ++this.modCount;
      return true;
   }

   public boolean containsAll(Collection<?> c) {
      Iterator var2 = c.iterator();

      Object o;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         o = var2.next();
      } while(this.contains(o));

      return false;
   }

   public boolean addAll(int index, Collection<? extends E> c) {
      if (c.isEmpty()) {
         return true;
      } else {
         ++this.modCount;
         return CollectionBasics.getEntry(this.lists, index).addAll(c);
      }
   }

   public boolean removeAll(Collection<?> c) {
      boolean changed = false;

      List list;
      for(Iterator var3 = this.lists.iterator(); var3.hasNext(); changed |= list.removeAll(c)) {
         list = (List)var3.next();
      }

      if (changed) {
         ++this.modCount;
      }

      return changed;
   }

   public boolean retainAll(Collection<?> c) {
      return CollectionBasics.retainAll(this, c);
   }

   public void clear() {
      Iterator var1 = this.lists.iterator();

      while(var1.hasNext()) {
         List<E> list = (List)var1.next();
         list.clear();
      }

   }

   public E get(int index) {
      return CollectionBasics.getEntry(this.lists, index).get();
   }

   public E set(int index, E element) {
      ++this.modCount;
      return CollectionBasics.getEntry(this.lists, index).set(element);
   }

   public void add(int index, E element) {
      ++this.modCount;
      CollectionBasics.getEntry(this.lists, index).add(element);
   }

   public E remove(int index) {
      ++this.modCount;
      return CollectionBasics.getEntry(this.lists, index).remove();
   }

   public boolean addAll(Collection<? extends E> c) {
      boolean changed = false;

      Object element;
      for(Iterator var3 = c.iterator(); var3.hasNext(); changed |= this.add(element)) {
         element = var3.next();
      }

      if (changed) {
         ++this.modCount;
      }

      return changed;
   }

   public int indexOf(Object o) {
      int index = 0;

      List list;
      for(Iterator var4 = this.lists.iterator(); var4.hasNext(); index += list.size()) {
         list = (List)var4.next();
         int subIndex = list.indexOf(o);
         if (subIndex != -1) {
            return index + subIndex;
         }
      }

      return -1;
   }

   public int lastIndexOf(Object o) {
      int rval = -1;
      int index = 0;
      Iterator var5 = this.lists.iterator();

      while(var5.hasNext()) {
         List<E> list = (List)var5.next();
         int subIndex = list.lastIndexOf(o);
         if (subIndex == -1) {
            index += list.size();
         } else {
            rval = index + subIndex;
         }
      }

      return rval;
   }

   public ListIterator<E> listIterator() {
      return new List2DListIterator(this.lists);
   }

   public ListIterator<E> listIterator(int index) {
      return new List2DListIterator(this.lists, index);
   }
}
