package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import java.util.List;

public abstract class ListDiff<T> implements Diff<List<T>> {
   private final int index;

   public ListDiff(final int index) {
      this.index = index;
   }

   public abstract void applyTo(List<T> list);

   public int getIndex() {
      return this.index;
   }

   public static class Changed<T> extends ListDiff<T> {
      private final int oldSize;
      private final List<T> newValue;

      public Changed(final int index, final int oldSize, final List<T> newValue) {
         super(index);
         this.oldSize = oldSize;
         this.newValue = newValue;
      }

      public int getOldSize() {
         return this.oldSize;
      }

      public List<T> getNewValue() {
         return this.newValue;
      }

      public String toString() {
         return "* " + this.getIndex() + " : " + this.getOldSize() + " -> " + this.getNewValue();
      }

      public void applyTo(List<T> list) {
         list.subList(this.getIndex(), this.getIndex() + this.oldSize).clear();
         list.addAll(this.getIndex(), this.getNewValue());
      }
   }

   public static class Removal<T> extends ListDiff<T> {
      private final int size;

      public Removal(final int index, final int size) {
         super(index);
         this.size = size;
      }

      public int getSize() {
         return this.size;
      }

      public void applyTo(List<T> list) {
         list.subList(this.getIndex(), this.getIndex() + this.size).clear();
      }

      public String toString() {
         return "- " + this.getIndex() + " : " + this.getSize();
      }
   }

   public static class Addition<T> extends ListDiff<T> {
      private final List<T> values;

      public Addition(final int index, final List<T> values) {
         super(index);
         this.values = values;
      }

      public List<T> getValues() {
         return this.values;
      }

      public void applyTo(List<T> list) {
         list.addAll(this.getIndex(), this.getValues());
      }

      public String toString() {
         return "+ " + this.getIndex() + " : " + this.getValues();
      }
   }
}
