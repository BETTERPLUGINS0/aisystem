package com.nisovin.shopkeepers.util.java;

import org.checkerframework.checker.nullness.qual.Nullable;

public class Rectangle {
   private final int x;
   private final int y;
   private final int width;
   private final int height;

   public Rectangle(int x, int y, int width, int height) {
      Validate.isTrue(x >= 0 && y >= 0 && width >= 0 && height >= 0, () -> {
         return "Invalid rectangle: x=" + x + ", y=" + y + ", width" + width + ", height=" + height;
      });
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public boolean isEmpty() {
      return this.getWidth() == 0 || this.getHeight() == 0;
   }

   public boolean contains(int pointX, int pointY) {
      return pointX >= this.getX() && pointX < this.getX() + this.getWidth() && pointY >= this.getY() && pointY < this.getY() + this.getHeight();
   }

   public boolean contains(int x, int y, int width, int height) {
      return x >= this.getX() && y >= this.getY() && x + width <= this.getX() + this.getWidth() && y + height <= this.getY() + this.getHeight();
   }

   public boolean contains(Rectangle other) {
      Validate.notNull(other, (String)"other is null");
      return this.contains(other.getX(), other.getY(), other.getWidth(), other.getHeight());
   }

   public int getMaxSlot() {
      Validate.isTrue(!this.isEmpty(), "The rectangle is empty!");
      return this.getWidth() * this.getHeight() - 1;
   }

   public int slotToX(int slot) {
      Validate.isTrue(this.containsSlot(slot), "Slot is outside the rectangle!");
      return this.getX() + slot % this.getWidth();
   }

   public int slotToY(int slot) {
      Validate.isTrue(this.containsSlot(slot), "Slot is outside the rectangle!");
      return this.getY() + slot / this.getWidth();
   }

   public int toSlot(int pointX, int pointY) {
      Validate.isTrue(this.contains(pointX, pointY), "Point is outside the rectangle!");
      return (pointY - this.getY()) * this.getWidth() + (pointX - this.getX());
   }

   public int convertSlotFrom(Rectangle other, int otherSlot) {
      Validate.notNull(other, (String)"other is null");
      int x = other.slotToX(otherSlot);
      int y = other.slotToY(otherSlot);
      return this.toSlot(x, y);
   }

   public boolean containsSlot(int slot) {
      return slot >= 0 && slot <= this.getMaxSlot();
   }

   public boolean containsSlots(int startSlot, int width, int height) {
      int startX = this.slotToX(startSlot);
      int startY = this.slotToY(startSlot);
      return this.contains(startX, startY, width, height);
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.getX();
      result = 31 * result + this.getY();
      result = 31 * result + this.getWidth();
      result = 31 * result + this.getHeight();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof Rectangle)) {
         return false;
      } else {
         Rectangle other = (Rectangle)obj;
         if (this.getX() != other.getX()) {
            return false;
         } else if (this.getY() != other.getY()) {
            return false;
         } else if (this.getWidth() != other.getWidth()) {
            return false;
         } else {
            return this.getHeight() == other.getHeight();
         }
      }
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getSimpleName());
      builder.append(" [x=");
      builder.append(this.getX());
      builder.append(", y=");
      builder.append(this.getY());
      builder.append(", width=");
      builder.append(this.getWidth());
      builder.append(", height=");
      builder.append(this.getHeight());
      builder.append("]");
      return builder.toString();
   }
}
