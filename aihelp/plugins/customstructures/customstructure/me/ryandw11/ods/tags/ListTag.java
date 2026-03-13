package me.ryandw11.ods.tags;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.internal.InternalUtils;
import me.ryandw11.ods.io.CountingOutputStream;

public class ListTag<T extends Tag<?>> implements Tag<List<T>> {
   private String name;
   private List<T> value;

   public ListTag(String name, List<T> value) {
      this.name = name;
      this.value = value;
   }

   public void setValue(List<T> s) {
      this.value = s;
   }

   public List<T> getValue() {
      return this.value;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void addTag(T tag) {
      this.value.add(tag);
   }

   public T getTag(int i) {
      return (Tag)this.value.get(i);
   }

   public void removeTag(T tag) {
      this.value.remove(tag);
   }

   public void removeTag(int i) {
      this.value.remove(i);
   }

   public void removeAllTags() {
      this.value.clear();
   }

   public int indexOf(T tag) {
      return this.value.indexOf(tag);
   }

   public void writeData(DataOutputStream dos) throws IOException {
      dos.write(this.getID());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      CountingOutputStream cos = new CountingOutputStream(os);
      DataOutputStream tempDos = new DataOutputStream(cos);
      tempDos.writeShort(this.name.getBytes(StandardCharsets.UTF_8).length);
      tempDos.write(this.name.getBytes(StandardCharsets.UTF_8));
      Iterator var5 = this.value.iterator();

      while(var5.hasNext()) {
         T tag = (Tag)var5.next();
         tag.setName("");
         tag.writeData(tempDos);
      }

      dos.writeInt(cos.getCount());
      dos.write(os.toByteArray());
      tempDos.close();
   }

   public Tag<List<T>> createFromData(ByteBuffer value, int length) {
      List<Tag<?>> data = InternalUtils.getListData(value, length);
      this.value = (List)data.stream().map((d) -> {
         return d;
      }).collect(Collectors.toList());
      return this;
   }

   public byte getID() {
      return 9;
   }
}
