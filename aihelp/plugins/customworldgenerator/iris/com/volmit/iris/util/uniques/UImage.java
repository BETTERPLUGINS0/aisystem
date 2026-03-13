package com.volmit.iris.util.uniques;

import java.awt.Color;

public interface UImage {
   int getWidth();

   int getHeight();

   default boolean isInBounds(int x, int y) {
      return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight();
   }

   UImage copy();

   Color get(int x, int y);

   void set(int x, int y, Color color);
}
