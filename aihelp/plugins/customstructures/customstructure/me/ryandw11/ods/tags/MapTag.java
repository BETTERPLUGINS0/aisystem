package me.ryandw11.ods.tags;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.internal.InternalUtils;
import me.ryandw11.ods.io.CountingOutputStream;

public class MapTag<T extends Tag<?>> implements Tag<Map<String, T>> {
   private String name;
   private Map<String, T> value;

   public MapTag(String name, Map<String, T> value) {
      this.name = name;
      this.value = value;
   }

   public void setValue(Map<String, T> s) {
      this.value = s;
   }

   public Map<String, T> getValue() {
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
      Iterator var5 = this.value.entrySet().iterator();

      while(var5.hasNext()) {
         Entry<String, T> entry = (Entry)var5.next();
         ((Tag)entry.getValue()).setName((String)entry.getKey());
         ((Tag)entry.getValue()).writeData(tempDos);
      }

      dos.writeInt(cos.getCount());
      dos.write(os.toByteArray());
      tempDos.close();
   }

   public Tag<Map<String, T>> createFromData(ByteBuffer value, int length) {
      List<Tag<?>> data = InternalUtils.getListData(value, length);
      Map<String, T> output = new HashMap();
      Iterator var5 = data.iterator();

      while(var5.hasNext()) {
         Tag<?> tag = (Tag)var5.next();
         output.put(tag.getName(), tag);
         tag.setName("");
      }

      this.value = output;
      return this;
   }

   public byte getID() {
      return 10;
   }
}
