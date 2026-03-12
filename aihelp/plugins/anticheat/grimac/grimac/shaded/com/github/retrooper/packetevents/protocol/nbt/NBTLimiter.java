package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.NonExtendable
public interface NBTLimiter {
   int DEFAULT_MAX_SIZE = Integer.getInteger("packetevents.nbt.default-max-size", 2097152);
   int DEFAULT_MAX_DEPTH = Integer.getInteger("packetevents.nbt.default-max-depth", 512);

   static NBTLimiter noop() {
      return new NBTLimiter() {
         public void increment(int amount) {
         }

         public void checkReadability(int length) {
         }

         public void enterDepth() {
         }

         public void exitDepth() {
         }
      };
   }

   static NBTLimiter forBuffer(Object byteBuf) {
      return forBuffer(byteBuf, DEFAULT_MAX_SIZE);
   }

   static NBTLimiter forBuffer(Object byteBuf, int maxBytes) {
      return forBuffer(byteBuf, maxBytes, DEFAULT_MAX_DEPTH);
   }

   static NBTLimiter forBuffer(Object byteBuf, int maxBytes, int maxDepth) {
      return new NBTLimiter() {
         private int bytes;
         private int depth;

         public void increment(int amount) {
            if (amount < 0) {
               throw new IllegalArgumentException("Can't increment NBT limiter by negative amount: " + amount);
            } else if (this.bytes + amount > maxBytes) {
               throw new IllegalArgumentException("NBT size limit reached (" + this.bytes + "/" + maxBytes + ")");
            } else {
               this.bytes += amount;
            }
         }

         public void checkReadability(int length) {
            int readableBytes = ByteBufHelper.readableBytes(byteBuf);
            if (length > readableBytes) {
               throw new IllegalArgumentException("Can't read more than possible: " + length + " > " + readableBytes);
            }
         }

         public void enterDepth() {
            if (this.depth >= maxDepth) {
               throw new IllegalArgumentException("NBT depth limit reached (" + this.depth + "/" + maxDepth + ")");
            } else {
               ++this.depth;
            }
         }

         public void exitDepth() {
            if (this.depth <= 0) {
               throw new IllegalArgumentException("Can't exit top-level depth");
            } else {
               --this.depth;
            }
         }
      };
   }

   void increment(int amount);

   void checkReadability(int length);

   void enterDepth();

   void exitDepth();
}
