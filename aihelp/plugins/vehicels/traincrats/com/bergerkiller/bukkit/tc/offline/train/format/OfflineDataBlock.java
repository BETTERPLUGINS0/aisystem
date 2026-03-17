package com.bergerkiller.bukkit.tc.offline.train.format;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.Util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class OfflineDataBlock {
   private static final byte[] NO_DATA = new byte[0];
   private final OfflineDataBlock.DataBlockBuilder dataBlockBuilder;
   public final String name;
   public final byte[] data;
   public final List<OfflineDataBlock> children;

   public static OfflineDataBlock read(DataInputStream stream) throws IOException {
      OfflineDataBlockSerializer serializer = new OfflineDataBlockSerializer();
      return serializer.readDataBlock(stream);
   }

   public static OfflineDataBlock create(String name) {
      return new OfflineDataBlock(new OfflineDataBlock.DataBlockBuilder(), name, NO_DATA);
   }

   public static OfflineDataBlock createWithData(String name, byte[] data) {
      return new OfflineDataBlock(new OfflineDataBlock.DataBlockBuilder(), name, data);
   }

   public static OfflineDataBlock createWithData(String name, OfflineDataBlock.DataWriter writer) throws IOException {
      return (new OfflineDataBlock.DataBlockBuilder()).create(name, writer);
   }

   OfflineDataBlock(OfflineDataBlock.DataBlockBuilder dataBlockBuilder, String name, byte[] data) {
      this.dataBlockBuilder = dataBlockBuilder;
      this.name = name;
      this.data = data;
      this.children = new ArrayList();
   }

   public void writeTo(DataOutputStream stream) throws IOException {
      OfflineDataBlockSerializer serializer = new OfflineDataBlockSerializer();
      serializer.writeDataBlock(stream, this);
   }

   public DataInputStream readData() {
      return new DataInputStream(new ByteArrayInputStream(this.data));
   }

   public List<OfflineDataBlock> findChildren(String name) {
      return Util.filterList(Collections.unmodifiableList(this.children), (c) -> {
         return c.name.equals(name);
      });
   }

   public OfflineDataBlock findChildOrThrow(String name) {
      return (OfflineDataBlock)this.findChild(name).orElseThrow(() -> {
         return new RuntimeException("Data '" + name + "' is missing in '" + this.name + "' data");
      });
   }

   public Optional<OfflineDataBlock> findChild(String name) {
      Iterator var2 = this.children.iterator();

      OfflineDataBlock child;
      do {
         if (!var2.hasNext()) {
            return Optional.empty();
         }

         child = (OfflineDataBlock)var2.next();
      } while(!child.name.equals(name));

      return Optional.of(child);
   }

   public boolean tryReadChild(String name, OfflineDataBlock.DataReader reader) throws IOException {
      OfflineDataBlock block = (OfflineDataBlock)this.findChild(name).orElse((Object)null);
      if (block == null) {
         return false;
      } else {
         DataInputStream stream = block.readData();

         try {
            reader.read(stream);
         } catch (Throwable var8) {
            if (stream != null) {
               try {
                  stream.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (stream != null) {
            stream.close();
         }

         return true;
      }
   }

   public OfflineDataBlock addChild(String name) {
      return this.addChild(new OfflineDataBlock(this.dataBlockBuilder, name, NO_DATA));
   }

   public OfflineDataBlock addChild(String name, OfflineDataBlock.DataWriter writer) throws IOException {
      OfflineDataBlock child = this.addChildOrAbort(name, writer);
      if (child == null) {
         throw new IllegalStateException("AbortChildException thrown in addChild. Use addChildOrAbort instead!");
      } else {
         return child;
      }
   }

   public OfflineDataBlock addChildOrAbort(String name, OfflineDataBlock.AbortableDataWriter writer) throws IOException {
      OfflineDataBlock child = this.dataBlockBuilder.create(name, writer);
      return child == null ? null : this.addChild(child);
   }

   public OfflineDataBlock addChild(String name, byte[] data) {
      return this.addChild(new OfflineDataBlock(this.dataBlockBuilder, name, data));
   }

   OfflineDataBlock addChild(OfflineDataBlock child) {
      this.children.add(child);
      return child;
   }

   public String toString() {
      StringBuilder str = new StringBuilder();
      this.appendToString(str, 0);
      return str.toString();
   }

   private void appendToString(StringBuilder str, int indent) {
      for(int i = 0; i < indent; ++i) {
         str.append("  ");
      }

      str.append(this.name);
      if (this.data.length > 0) {
         str.append(" b[").append(this.data.length).append("]");
      }

      if (!this.children.isEmpty()) {
         str.append(':');
         Iterator var5 = this.children.iterator();

         while(var5.hasNext()) {
            OfflineDataBlock child = (OfflineDataBlock)var5.next();
            str.append('\n');
            child.appendToString(str, indent + 1);
         }
      }

   }

   static final class DataBlockBuilder {
      private WeakReference<ByteArrayOutputStream> stream = LogicUtil.nullWeakReference();

      public OfflineDataBlock create(String name, OfflineDataBlock.AbortableDataWriter writer) throws IOException {
         ByteArrayOutputStream tempByteArrayStream = (ByteArrayOutputStream)this.stream.get();
         if (tempByteArrayStream == null) {
            tempByteArrayStream = new ByteArrayOutputStream(64);
            this.stream = new WeakReference(tempByteArrayStream);
         }

         Object var5;
         try {
            DataOutputStream stream = new DataOutputStream(tempByteArrayStream);

            try {
               writer.write(stream);
            } catch (Throwable var13) {
               try {
                  stream.close();
               } catch (Throwable var12) {
                  var13.addSuppressed(var12);
               }

               throw var13;
            }

            stream.close();
            OfflineDataBlock var16 = new OfflineDataBlock(this, name, tempByteArrayStream.toByteArray());
            return var16;
         } catch (OfflineDataBlock.AbortChildException var14) {
            var5 = null;
         } finally {
            tempByteArrayStream.reset();
         }

         return (OfflineDataBlock)var5;
      }
   }

   @FunctionalInterface
   public interface AbortableDataWriter {
      void write(DataOutputStream var1) throws IOException, OfflineDataBlock.AbortChildException;
   }

   @FunctionalInterface
   public interface DataReader {
      void read(DataInputStream var1) throws IOException;
   }

   public static final class AbortChildException extends Exception {
   }

   @FunctionalInterface
   public interface DataWriter extends OfflineDataBlock.AbortableDataWriter {
      void write(DataOutputStream var1) throws IOException;
   }
}
