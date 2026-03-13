package org.apache.commons.io.input.buffer;

import java.util.Objects;
import org.apache.commons.io.IOUtils;

public class CircularByteBuffer {
   private final byte[] buffer;
   private int startOffset;
   private int endOffset;
   private int currentNumberOfBytes;

   public CircularByteBuffer(int var1) {
      this.buffer = IOUtils.byteArray(var1);
      this.startOffset = 0;
      this.endOffset = 0;
      this.currentNumberOfBytes = 0;
   }

   public CircularByteBuffer() {
      this(8192);
   }

   public byte read() {
      if (this.currentNumberOfBytes <= 0) {
         throw new IllegalStateException("No bytes available.");
      } else {
         byte var1 = this.buffer[this.startOffset];
         --this.currentNumberOfBytes;
         if (++this.startOffset == this.buffer.length) {
            this.startOffset = 0;
         }

         return var1;
      }
   }

   public void read(byte[] var1, int var2, int var3) {
      Objects.requireNonNull(var1, "targetBuffer");
      if (var2 >= 0 && var2 < var1.length) {
         if (var3 >= 0 && var3 <= this.buffer.length) {
            if (var2 + var3 > var1.length) {
               throw new IllegalArgumentException("The supplied byte array contains only " + var1.length + " bytes, but offset, and length would require " + (var2 + var3 - 1));
            } else if (this.currentNumberOfBytes < var3) {
               throw new IllegalStateException("Currently, there are only " + this.currentNumberOfBytes + "in the buffer, not " + var3);
            } else {
               int var4 = var2;

               for(int var5 = 0; var5 < var3; ++var5) {
                  var1[var4++] = this.buffer[this.startOffset];
                  --this.currentNumberOfBytes;
                  if (++this.startOffset == this.buffer.length) {
                     this.startOffset = 0;
                  }
               }

            }
         } else {
            throw new IllegalArgumentException("Invalid length: " + var3);
         }
      } else {
         throw new IllegalArgumentException("Invalid offset: " + var2);
      }
   }

   public void add(byte var1) {
      if (this.currentNumberOfBytes >= this.buffer.length) {
         throw new IllegalStateException("No space available");
      } else {
         this.buffer[this.endOffset] = var1;
         ++this.currentNumberOfBytes;
         if (++this.endOffset == this.buffer.length) {
            this.endOffset = 0;
         }

      }
   }

   public boolean peek(byte[] var1, int var2, int var3) {
      Objects.requireNonNull(var1, "Buffer");
      if (var2 >= 0 && var2 < var1.length) {
         if (var3 >= 0 && var3 <= this.buffer.length) {
            if (var3 < this.currentNumberOfBytes) {
               return false;
            } else {
               int var4 = this.startOffset;

               for(int var5 = 0; var5 < var3; ++var5) {
                  if (this.buffer[var4] != var1[var5 + var2]) {
                     return false;
                  }

                  ++var4;
                  if (var4 == this.buffer.length) {
                     var4 = 0;
                  }
               }

               return true;
            }
         } else {
            throw new IllegalArgumentException("Invalid length: " + var3);
         }
      } else {
         throw new IllegalArgumentException("Invalid offset: " + var2);
      }
   }

   public void add(byte[] var1, int var2, int var3) {
      Objects.requireNonNull(var1, "Buffer");
      if (var2 >= 0 && var2 < var1.length) {
         if (var3 < 0) {
            throw new IllegalArgumentException("Invalid length: " + var3);
         } else if (this.currentNumberOfBytes + var3 > this.buffer.length) {
            throw new IllegalStateException("No space available");
         } else {
            for(int var4 = 0; var4 < var3; ++var4) {
               this.buffer[this.endOffset] = var1[var2 + var4];
               if (++this.endOffset == this.buffer.length) {
                  this.endOffset = 0;
               }
            }

            this.currentNumberOfBytes += var3;
         }
      } else {
         throw new IllegalArgumentException("Invalid offset: " + var2);
      }
   }

   public boolean hasSpace() {
      return this.currentNumberOfBytes < this.buffer.length;
   }

   public boolean hasSpace(int var1) {
      return this.currentNumberOfBytes + var1 <= this.buffer.length;
   }

   public boolean hasBytes() {
      return this.currentNumberOfBytes > 0;
   }

   public int getSpace() {
      return this.buffer.length - this.currentNumberOfBytes;
   }

   public int getCurrentNumberOfBytes() {
      return this.currentNumberOfBytes;
   }

   public void clear() {
      this.startOffset = 0;
      this.endOffset = 0;
      this.currentNumberOfBytes = 0;
   }
}
