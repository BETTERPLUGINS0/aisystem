package me.ryandw11.ods;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import me.ryandw11.ods.compression.Compressor;
import me.ryandw11.ods.compression.GZIPCompression;
import me.ryandw11.ods.internal.ODSFile;
import me.ryandw11.ods.internal.ODSInternal;
import me.ryandw11.ods.internal.ODSMem;

public class ObjectDataStructure {
   private final ODSInternal internal;

   public ObjectDataStructure(File file) {
      this((File)file, new GZIPCompression());
   }

   public ObjectDataStructure(File file, Compressor compression) {
      this.internal = new ODSFile(file, compression);
   }

   public ObjectDataStructure(byte[] data, Compressor compressor) {
      this.internal = new ODSMem(data, compressor);
   }

   public ObjectDataStructure(ByteBuffer data, Compressor compressor) {
      this.internal = new ODSMem(data, compressor);
   }

   public ObjectDataStructure() {
      this.internal = new ODSMem();
   }

   public <T extends Tag<?>> T get(String key) {
      return this.internal.get(key);
   }

   public List<Tag<?>> getAll() {
      return this.internal.getAll();
   }

   public void save(List<? extends Tag<?>> tags) {
      this.internal.save(tags);
   }

   public void append(Tag<?> tag) {
      this.internal.append(tag);
   }

   public void appendAll(List<Tag<?>> tags) {
      this.internal.appendAll(tags);
   }

   public boolean find(String key) {
      return this.internal.find(key);
   }

   public boolean delete(String key) {
      return this.internal.delete(key);
   }

   public boolean replaceData(String key, Tag<?> replacement) {
      return this.internal.replaceData(key, replacement);
   }

   public void set(String key, Tag<?> value) {
      this.internal.set(key, value);
   }

   public byte[] export(Compressor compressor) {
      return this.internal.export(compressor);
   }

   public void importFile(File file, Compressor compressor) {
      this.internal.importFile(file, compressor);
   }

   public void saveToFile(File file, Compressor compressor) {
      this.internal.saveToFile(file, compressor);
   }

   public void clear() {
      this.internal.clear();
   }
}
