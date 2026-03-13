package me.ryandw11.ods.internal;

import java.io.File;
import java.util.List;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.compression.Compressor;

public interface ODSInternal {
   <T extends Tag<?>> T get(String var1);

   List<Tag<?>> getAll();

   void save(List<? extends Tag<?>> var1);

   void append(Tag<?> var1);

   void appendAll(List<Tag<?>> var1);

   boolean find(String var1);

   boolean delete(String var1);

   boolean replaceData(String var1, Tag<?> var2);

   void set(String var1, Tag<?> var2);

   byte[] export(Compressor var1);

   void importFile(File var1, Compressor var2);

   void saveToFile(File var1, Compressor var2);

   void clear();
}
