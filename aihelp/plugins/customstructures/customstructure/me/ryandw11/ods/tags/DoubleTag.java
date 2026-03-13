package me.ryandw11.ods.tags;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.io.CountingOutputStream;

public class DoubleTag implements Tag<Double> {
   private String name;
   private Double value;

   public DoubleTag(String name, double value) {
      this.name = name;
      this.value = value;
   }

   public void setValue(Double s) {
      this.value = s;
   }

   public Double getValue() {
      return this.value;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void writeData(DataOutputStream dos) throws IOException {
      dos.write(this.getID());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      CountingOutputStream cos = new CountingOutputStream(os);
      DataOutputStream tempDos = new DataOutputStream(cos);
      tempDos.writeShort(this.name.getBytes(StandardCharsets.UTF_8).length);
      tempDos.write(this.name.getBytes(StandardCharsets.UTF_8));
      tempDos.writeDouble(this.value);
      dos.writeInt(cos.getCount());
      dos.write(os.toByteArray());
      tempDos.close();
   }

   public Tag<Double> createFromData(ByteBuffer value, int length) {
      this.value = value.getDouble();
      return this;
   }

   public byte getID() {
      return 4;
   }
}
