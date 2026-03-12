package me.frep.vulcan.spigot;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class Vulcan_W extends Vulcan_Q {
   public Vulcan_W(List var1) {
      super(var1);
   }

   public abstract void Vulcan_K(Object[] var1);

   public int size() {
      return this.Vulcan_t.size();
   }

   public boolean isEmpty() {
      return this.Vulcan_t.isEmpty();
   }

   public boolean contains(Object var1) {
      return this.Vulcan_t.contains(var1);
   }

   public Iterator iterator() {
      this.Vulcan_K(new Object[0]);
      return this.listIterator();
   }

   public Object[] toArray() {
      return this.Vulcan_t.toArray();
   }

   public boolean add(Object var1) {
      return this.Vulcan_t.add(var1);
   }

   public boolean remove(Object var1) {
      return this.Vulcan_t.remove(var1);
   }

   public boolean addAll(Collection var1) {
      return this.Vulcan_t.addAll(var1);
   }

   public boolean addAll(int var1, Collection var2) {
      return this.Vulcan_t.addAll(var1, var2);
   }

   public void clear() {
      this.Vulcan_t.clear();
   }

   public Object get(int var1) {
      return this.Vulcan_t.get(var1);
   }

   public Object set(int var1, Object var2) {
      return this.Vulcan_t.set(var1, var2);
   }

   public void add(int var1, Object var2) {
      this.Vulcan_t.add(var1, var2);
   }

   public Object remove(int var1) {
      return this.Vulcan_t.remove(var1);
   }

   public int indexOf(Object var1) {
      return this.Vulcan_t.indexOf(var1);
   }

   public int lastIndexOf(Object var1) {
      return this.Vulcan_t.lastIndexOf(var1);
   }

   public ListIterator listIterator() {
      return this.Vulcan_t.listIterator();
   }

   public ListIterator listIterator(int var1) {
      return this.Vulcan_t.listIterator(var1);
   }

   public List subList(int var1, int var2) {
      return this.Vulcan_t.subList(var1, var2);
   }

   public boolean retainAll(Collection var1) {
      return this.Vulcan_t.retainAll(var1);
   }

   public boolean removeAll(Collection var1) {
      return this.Vulcan_t.removeAll(var1);
   }

   public boolean containsAll(Collection var1) {
      return this.Vulcan_t.containsAll(var1);
   }

   public Object[] toArray(Object[] var1) {
      return this.Vulcan_t.toArray(var1);
   }
}
