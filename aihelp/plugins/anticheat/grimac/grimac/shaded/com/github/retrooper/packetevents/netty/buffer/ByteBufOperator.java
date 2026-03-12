package ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer;

import java.nio.charset.Charset;

public interface ByteBufOperator {
   int capacity(Object buffer);

   Object capacity(Object buffer, int capacity);

   int readerIndex(Object buffer);

   Object readerIndex(Object buffer, int readerIndex);

   int writerIndex(Object buffer);

   Object writerIndex(Object buffer, int writerIndex);

   int readableBytes(Object buffer);

   int writableBytes(Object buffer);

   Object clear(Object buffer);

   byte readByte(Object buffer);

   short readShort(Object buffer);

   int readMedium(Object buffer);

   int readInt(Object buffer);

   long readUnsignedInt(Object buffer);

   long readLong(Object buffer);

   void writeByte(Object buffer, int value);

   void writeShort(Object buffer, int value);

   void writeShortLE(Object buffer, int value);

   void writeMedium(Object buffer, int value);

   void writeInt(Object buffer, int value);

   void writeLong(Object buffer, long value);

   Object getBytes(Object buffer, int index, byte[] destination);

   short getUnsignedByte(Object buffer, int index);

   boolean isReadable(Object buffer);

   Object copy(Object buffer);

   Object duplicate(Object buffer);

   boolean hasArray(Object buffer);

   byte[] array(Object buffer);

   Object retain(Object buffer);

   Object retainedDuplicate(Object buffer);

   Object readSlice(Object buffer, int length);

   Object readBytes(Object buffer, byte[] destination, int destinationIndex, int length);

   Object readBytes(Object buffer, int length);

   void readBytes(Object buffer, byte[] bytes);

   Object writeBytes(Object buffer, Object src);

   Object writeBytes(Object buffer, byte[] bytes);

   Object writeBytes(Object buffer, byte[] bytes, int offset, int length);

   boolean release(Object buffer);

   int refCnt(Object buffer);

   Object skipBytes(Object buffer, int length);

   String toString(Object buffer, int index, int length, Charset charset);

   Object markReaderIndex(Object buffer);

   Object resetReaderIndex(Object buffer);

   Object markWriterIndex(Object buffer);

   Object resetWriterIndex(Object buffer);

   Object allocateNewBuffer(Object buffer);

   default float readFloat(Object buffer) {
      return Float.intBitsToFloat(this.readInt(buffer));
   }

   default void writeFloat(Object buffer, float value) {
      this.writeInt(buffer, Float.floatToIntBits(value));
   }

   default double readDouble(Object buffer) {
      return Double.longBitsToDouble(this.readLong(buffer));
   }

   default void writeDouble(Object buffer, double value) {
      this.writeLong(buffer, Double.doubleToLongBits(value));
   }

   default char readChar(Object buffer) {
      return (char)this.readShort(buffer);
   }

   default void writeChar(Object buffer, int value) {
      this.writeShort(buffer, value);
   }

   default int readUnsignedShort(Object buffer) {
      return this.readShort(buffer) & '\uffff';
   }

   default short readUnsignedByte(Object buffer) {
      return (short)(this.readByte(buffer) & 255);
   }

   default boolean readBoolean(Object buffer) {
      return this.readByte(buffer) != 0;
   }

   default void writeBoolean(Object buffer, boolean value) {
      this.writeByte(buffer, value ? 1 : 0);
   }
}
