package com.bergerkiller.bukkit.tc.offline.train.format;

import com.bergerkiller.bukkit.tc.Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class OfflineDataBlockSerializer {
   private final List<String> values = new ArrayList();
   private final Map<String, Integer> valueToIndex = new HashMap();
   private final OfflineDataBlock.DataBlockBuilder dataBlockBuilder = new OfflineDataBlock.DataBlockBuilder();

   public OfflineDataBlockSerializer() {
      this.reset();
   }

   public void reset() {
      this.values.clear();
      this.valueToIndex.clear();
      this.values.add("");
      this.valueToIndex.put("", 0);
   }

   public OfflineDataBlock readDataBlock(DataInputStream stream) throws IOException {
      String name = this.readString(stream);
      if (name.isEmpty()) {
         return null;
      } else {
         byte[] data = Util.readByteArray(stream);
         OfflineDataBlock dataBlock = new OfflineDataBlock(this.dataBlockBuilder, name, data);

         OfflineDataBlock child;
         while((child = this.readDataBlock(stream)) != null) {
            dataBlock.addChild(child);
         }

         return dataBlock;
      }
   }

   public void writeDataBlock(DataOutputStream stream, OfflineDataBlock dataBlock) throws IOException {
      this.writeString(stream, dataBlock.name);
      Util.writeByteArray(stream, dataBlock.data);
      Iterator var3 = dataBlock.children.iterator();

      while(var3.hasNext()) {
         OfflineDataBlock child = (OfflineDataBlock)var3.next();
         this.writeDataBlock(stream, child);
      }

      this.writeEmptyString(stream);
   }

   public void writeString(DataOutputStream stream, String value) throws IOException {
      Integer index = (Integer)this.valueToIndex.get(value);
      if (index == null) {
         index = this.values.size();
         this.values.add(value);
         this.valueToIndex.put(value, index);
         Util.writeVariableLengthInt(stream, index);
         stream.writeUTF(value);
      } else {
         Util.writeVariableLengthInt(stream, index);
      }

   }

   public void writeEmptyString(DataOutputStream stream) throws IOException {
      Util.writeVariableLengthInt(stream, 0);
   }

   public String readString(DataInputStream stream) throws IOException {
      int index = Util.readVariableLengthInt(stream);
      if (index == this.values.size()) {
         String value = stream.readUTF();
         this.values.add(value);
         this.valueToIndex.put(value, index);
         return value;
      } else if (index >= 0 && index <= this.values.size()) {
         return (String)this.values.get(index);
      } else {
         throw new IOException("String index out of range: " + index);
      }
   }
}
