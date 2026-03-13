package me.ryandw11.ods.tags;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.io.CountingOutputStream;

public class StringTag implements Tag<String> {
   private String value;
   private String name;

   public StringTag(String name, String value) {
      this.name = name;
      this.value = value;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String s) {
      this.value = s;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void writeData(DataOutputStream dos) throws IOException {
      dos.write(this.getID());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      CountingOutputStream cos = new CountingOutputStream(os);
      DataOutputStream tempDos = new DataOutputStream(cos);
      tempDos.writeShort(this.name.getBytes(StandardCharsets.UTF_8).length);
      tempDos.write(this.name.getBytes(StandardCharsets.UTF_8));
      tempDos.write(this.value.getBytes(StandardCharsets.UTF_8));
      dos.writeInt(cos.getCount());
      dos.write(os.toByteArray());
      cos.close();
      os.close();
      tempDos.close();
   }

   public Tag<String> createFromData(ByteBuffer value, int length) {
      byte[] stringData = new byte[length];
      value.get(stringData);
      this.value = new String(stringData, StandardCharsets.UTF_8);
      return this;
   }

   public byte getID() {
      return 1;
   }
}
