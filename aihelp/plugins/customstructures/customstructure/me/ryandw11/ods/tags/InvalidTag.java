package me.ryandw11.ods.tags;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.exception.ODSException;

public class InvalidTag implements Tag<byte[]> {
   private String name;
   private byte[] value;

   /** @deprecated */
   public InvalidTag(String name) {
      this.name = name;
   }

   public byte[] getValue() {
      return this.value;
   }

   public void setValue(byte[] bytes) {
      this.value = bytes;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void writeData(DataOutputStream dos) throws IOException {
      throw new ODSException("Cannot write an InvalidTag.");
   }

   public Tag<byte[]> createFromData(ByteBuffer value, int length) {
      byte[] data = new byte[length];
      value.get(data);
      this.value = data;
      return this;
   }

   public byte getID() {
      return 0;
   }
}
