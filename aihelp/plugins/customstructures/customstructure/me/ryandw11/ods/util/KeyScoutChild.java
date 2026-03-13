package me.ryandw11.ods.util;

public class KeyScoutChild {
   private String name;
   private int size = 0;
   private int startingIndex = -1;

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setSize(int size) {
      this.size = size;
   }

   public int getSize() {
      return this.size;
   }

   public void setStartingIndex(int startingIndex) {
      this.startingIndex = startingIndex;
   }

   public int getStartingIndex() {
      return this.startingIndex;
   }

   protected void removeSize(int amount) {
      this.size -= amount;
   }

   protected void addSize(int amount) {
      this.size += amount;
   }
}
