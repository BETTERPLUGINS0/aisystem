package github.nighter.smartspawner.libs.mariadb.client.socket;

import github.nighter.smartspawner.libs.mariadb.HostAddress;
import java.io.IOException;

public interface Writer {
   int pos();

   byte[] buf();

   void pos(int var1) throws IOException;

   void writeByte(int var1) throws IOException;

   void writeShort(short var1) throws IOException;

   void writeInt(int var1) throws IOException;

   void writeLong(long var1) throws IOException;

   void writeDouble(double var1) throws IOException;

   void writeFloat(float var1) throws IOException;

   void writeBytes(byte[] var1) throws IOException;

   void writeBytesAtPos(byte[] var1, int var2);

   void writeBytes(byte[] var1, int var2, int var3) throws IOException;

   void writeLength(long var1) throws IOException;

   void writeAscii(String var1) throws IOException;

   void writeString(String var1) throws IOException;

   void writeStringEscaped(String var1, boolean var2) throws IOException;

   void writeBytesEscaped(byte[] var1, int var2, boolean var3) throws IOException;

   void writeEmptyPacket() throws IOException;

   void flush() throws IOException;

   void flushPipeline() throws IOException;

   boolean throwMaxAllowedLength(int var1);

   boolean throwMaxAllowedLengthOr16M(int var1);

   long getCmdLength();

   void permitTrace(boolean var1);

   void setServerThreadId(Long var1, HostAddress var2);

   void mark();

   boolean isMarked();

   boolean hasFlushed();

   void flushBufferStopAtMark() throws IOException;

   boolean bufIsDataAfterMark();

   byte[] resetMark();

   void initPacket();

   void close() throws IOException;

   byte getSequence();
}
