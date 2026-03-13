package me.ryandw11.ods;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface Tag<T> {
   T getValue();

   void setValue(T var1);

   String getName();

   void setName(String var1);

   void writeData(DataOutputStream var1) throws IOException;

   Tag<T> createFromData(ByteBuffer var1, int var2);

   byte getID();
}
