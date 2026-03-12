package fr.xephi.authme.libs.org.postgresql.core.v3.adaptivefetch;

public class AdaptiveFetchCacheEntry {
   private int size = -1;
   private int counter;
   private int maximumRowSizeBytes = -1;

   public int getSize() {
      return this.size;
   }

   public void setSize(int size) {
      this.size = size;
   }

   public int getCounter() {
      return this.counter;
   }

   public void setCounter(int counter) {
      this.counter = counter;
   }

   public int getMaximumRowSizeBytes() {
      return this.maximumRowSizeBytes;
   }

   public void setMaximumRowSizeBytes(int maximumRowSizeBytes) {
      this.maximumRowSizeBytes = maximumRowSizeBytes;
   }

   public void incrementCounter() {
      ++this.counter;
   }

   public void decrementCounter() {
      --this.counter;
   }
}
