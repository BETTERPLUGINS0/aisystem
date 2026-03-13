package org.terraform.utils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Range<T> implements Serializable {
   private static final long serialVersionUID = 1L;
   @NotNull
   private final Comparator<T> comparator;
   @NotNull
   private final T maximum;
   @NotNull
   private final T minimum;
   private transient int hashCode;
   private transient String toString;

   private Range(@NotNull T element1, @NotNull T element2, @Nullable Comparator<T> comp) {
      this.comparator = (Comparator)Objects.requireNonNullElse(comp, Range.ComparableComparator.INSTANCE);
      if (this.comparator.compare(element1, element2) < 1) {
         this.minimum = element1;
         this.maximum = element2;
      } else {
         this.minimum = element2;
         this.maximum = element1;
      }

   }

   @NotNull
   public static <T extends Comparable<? super T>> Range<T> between(@NotNull T fromInclusive, @NotNull T toInclusive) {
      return between(fromInclusive, toInclusive, (Comparator)null);
   }

   @NotNull
   public static <T> Range<T> between(@NotNull T fromInclusive, @NotNull T toInclusive, @Nullable Comparator<T> comparator) {
      return new Range(fromInclusive, toInclusive, comparator);
   }

   @NotNull
   public static <T extends Comparable<? super T>> Range<T> is(@NotNull T element) {
      return between(element, element, (Comparator)null);
   }

   @NotNull
   public static <T> Range<T> is(@NotNull T element, Comparator<T> comparator) {
      return between(element, element, comparator);
   }

   public boolean contains(@Nullable T element) {
      if (element == null) {
         return false;
      } else {
         return this.comparator.compare(element, this.minimum) > -1 && this.comparator.compare(element, this.maximum) < 1;
      }
   }

   public boolean containsRange(@Nullable Range<T> otherRange) {
      if (otherRange == null) {
         return false;
      } else {
         return this.contains(otherRange.minimum) && this.contains(otherRange.maximum);
      }
   }

   public int elementCompareTo(T element) {
      if (this.isAfter(element)) {
         return -1;
      } else {
         return this.isBefore(element) ? 1 : 0;
      }
   }

   public boolean equals(@Nullable Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         Range<T> range = (Range)obj;
         return this.minimum.equals(range.minimum) && this.maximum.equals(range.maximum);
      } else {
         return false;
      }
   }

   @Nullable
   public T fit(T element) {
      if (this.isAfter(element)) {
         return this.minimum;
      } else {
         return this.isBefore(element) ? this.maximum : element;
      }
   }

   @NotNull
   public Comparator<T> getComparator() {
      return this.comparator;
   }

   @NotNull
   public T getMaximum() {
      return this.maximum;
   }

   @NotNull
   public T getMinimum() {
      return this.minimum;
   }

   public int hashCode() {
      int result = this.hashCode;
      if (this.hashCode == 0) {
         int result = 17;
         result = 37 * result + this.getClass().hashCode();
         result = 37 * result + this.minimum.hashCode();
         result = 37 * result + this.maximum.hashCode();
         this.hashCode = result;
      }

      return result;
   }

   @NotNull
   public Range<T> intersectionWith(@NotNull Range<T> other) {
      if (!this.isOverlappedBy(other)) {
         throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", other));
      } else if (this.equals(other)) {
         return this;
      } else {
         T min = this.getComparator().compare(this.minimum, other.minimum) < 0 ? other.minimum : this.minimum;
         T max = this.getComparator().compare(this.maximum, other.maximum) < 0 ? this.maximum : other.maximum;
         return between(min, max, this.getComparator());
      }
   }

   public boolean isAfter(@Nullable T element) {
      if (element == null) {
         return false;
      } else {
         return this.comparator.compare(element, this.minimum) < 0;
      }
   }

   public boolean isAfterRange(@Nullable Range<T> otherRange) {
      return otherRange == null ? false : this.isAfter(otherRange.maximum);
   }

   public boolean isBefore(@Nullable T element) {
      if (element == null) {
         return false;
      } else {
         return this.comparator.compare(element, this.maximum) > 0;
      }
   }

   public boolean isBeforeRange(@Nullable Range<T> otherRange) {
      return otherRange == null ? false : this.isBefore(otherRange.minimum);
   }

   public boolean isEndedBy(@Nullable T element) {
      if (element == null) {
         return false;
      } else {
         return this.comparator.compare(element, this.maximum) == 0;
      }
   }

   public boolean isNaturalOrdering() {
      return this.comparator == Range.ComparableComparator.INSTANCE;
   }

   public boolean isOverlappedBy(@Nullable Range<T> otherRange) {
      if (otherRange == null) {
         return false;
      } else {
         return otherRange.contains(this.minimum) || otherRange.contains(this.maximum) || this.contains(otherRange.minimum);
      }
   }

   public boolean isStartedBy(@Nullable T element) {
      if (element == null) {
         return false;
      } else {
         return this.comparator.compare(element, this.minimum) == 0;
      }
   }

   @NotNull
   public String toString() {
      if (this.toString == null) {
         String var10001 = String.valueOf(this.minimum);
         this.toString = "[" + var10001 + ".." + String.valueOf(this.maximum) + "]";
      }

      return this.toString;
   }

   @NotNull
   public String toString(@NotNull String format) {
      return String.format(format, this.minimum, this.maximum, this.comparator);
   }

   private static enum ComparableComparator implements Comparator {
      INSTANCE;

      public int compare(@NotNull Object obj1, @NotNull Object obj2) {
         return ((Comparable)obj1).compareTo(obj2);
      }

      // $FF: synthetic method
      private static Range.ComparableComparator[] $values() {
         return new Range.ComparableComparator[]{INSTANCE};
      }
   }
}
