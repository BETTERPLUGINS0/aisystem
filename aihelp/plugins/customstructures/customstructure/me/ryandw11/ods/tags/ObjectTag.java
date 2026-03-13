package me.ryandw11.ods.tags;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.internal.InternalUtils;
import me.ryandw11.ods.io.CountingOutputStream;

public class ObjectTag implements Tag<List<Tag<?>>> {
   private String name;
   private List<Tag<?>> value;

   public ObjectTag(String name, List<Tag<?>> value) {
      this.name = name;
      this.value = value;
   }

   public ObjectTag(String name) {
      this.name = name;
      this.value = new ArrayList();
   }

   public void addTag(Tag<?> t) {
      this.value.add(t);
   }

   public Tag<?> getTag(String name) {
      List<Tag<?>> results = (List)this.value.stream().filter((tag) -> {
         return tag.getName().equals(name);
      }).collect(Collectors.toList());
      return results.size() < 1 ? null : (Tag)results.get(0);
   }

   public void removeTag(Tag<?> tag) {
      this.value.remove(tag);
   }

   public void removeTag(String name) {
      this.value.removeIf((tag) -> {
         return tag.getName().equals(name);
      });
   }

   public void removeAllTags() {
      this.value.clear();
   }

   public boolean hasTag(String name) {
      return ((List)this.value.stream().filter((tag) -> {
         return tag.getName().equals(name);
      }).collect(Collectors.toList())).size() > 0;
   }

   public void setValue(List<Tag<?>> s) {
      this.value = s;
   }

   public List<Tag<?>> getValue() {
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
      Iterator var5 = this.value.iterator();

      while(var5.hasNext()) {
         Tag<?> tag = (Tag)var5.next();
         tag.writeData(tempDos);
      }

      dos.writeInt(cos.getCount());
      dos.write(os.toByteArray());
      tempDos.close();
   }

   public Tag<List<Tag<?>>> createFromData(ByteBuffer value, int length) {
      List<Tag<?>> data = InternalUtils.getListData(value, length);
      this.value = data;
      return this;
   }

   public byte getID() {
      return 11;
   }
}
