package fr.xephi.authme.libs.org.mariadb.jdbc.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.MariaDbBlob;

public interface ReadableByteBuf {
   int readableBytes();

   int pos();

   byte[] buf();

   void buf(byte[] var1, int var2, int var3);

   void pos(int var1);

   void skip();

   void skip(int var1);

   void skipLengthEncoded();

   MariaDbBlob readBlob(int var1);

   byte getByte();

   byte getByte(int var1);

   short getUnsignedByte();

   long readLongLengthEncodedNotNull();

   int readIntLengthEncodedNotNull();

   int skipIdentifier();

   long atoll(int var1);

   long atoull(int var1);

   Integer readLength();

   byte readByte();

   short readUnsignedByte();

   short readShort();

   int readUnsignedShort();

   int readMedium();

   int readUnsignedMedium();

   int readInt();

   int readIntBE();

   long readUnsignedInt();

   long readLong();

   long readLongBE();

   void readBytes(byte[] var1);

   byte[] readBytesNullEnd();

   ReadableByteBuf readLengthBuffer();

   String readString(int var1);

   String readAscii(int var1);

   String readStringNullEnd();

   String readStringEof();

   float readFloat();

   double readDouble();

   double readDoubleBE();
}
