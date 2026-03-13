package me.ryandw11.ods.tags;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.ryandw11.ods.ODS;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.compression.Compressor;
import me.ryandw11.ods.compression.GZIPCompression;
import me.ryandw11.ods.exception.ODSException;
import me.ryandw11.ods.internal.InternalUtils;
import me.ryandw11.ods.io.CountingOutputStream;
import me.ryandw11.ods.io.ODSIOUtils;

public class CompressedObjectTag implements Tag<List<Tag<?>>> {
   private String name;
   private Compressor compressor;
   private List<Tag<?>> value;

   public CompressedObjectTag(String name, List<Tag<?>> value, Compressor compressor) {
      this.name = name;
      this.value = value;
      this.compressor = compressor;
   }

   public CompressedObjectTag(String name, Compressor compressor) {
      this.name = name;
      this.compressor = compressor;
      this.value = new ArrayList();
   }

   public CompressedObjectTag(String name) {
      this(name, new ArrayList(), new GZIPCompression());
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
      return this.value.stream().anyMatch((tag) -> {
         return tag.getName().equals(name);
      });
   }

   public Compressor getCompressor() {
      return this.compressor;
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
      String compressorName = ODS.getCompressorName(this.compressor);
      if (compressorName == null) {
         throw new ODSException("Unable to find compressor: " + this.compressor);
      } else {
         tempDos.writeShort(compressorName.getBytes(StandardCharsets.UTF_8).length);
         tempDos.write(compressorName.getBytes(StandardCharsets.UTF_8));
         ByteArrayOutputStream osTemp = new ByteArrayOutputStream();
         OutputStream compressedStream = this.compressor.getOutputStream(osTemp);
         DataOutputStream tempDos2 = new DataOutputStream(compressedStream);
         Iterator var9 = this.value.iterator();

         while(var9.hasNext()) {
            Tag<?> tag = (Tag)var9.next();
            tag.writeData(tempDos2);
         }

         tempDos2.close();
         tempDos.write(osTemp.toByteArray());
         dos.writeInt(cos.getCount());
         dos.write(os.toByteArray());
         tempDos.close();
      }
   }

   public Tag<List<Tag<?>>> createFromData(ByteBuffer value, int length) {
      short compressorLength = value.getShort();
      length -= 2 + compressorLength;
      byte[] compressorBytes = new byte[compressorLength];
      value.get(compressorBytes);
      String compressionName = new String(compressorBytes);
      Compressor compressor = ODS.getCompressor(compressionName);
      if (compressor == null) {
         throw new ODSException("Cannot find compressor: " + compressionName);
      } else {
         this.compressor = compressor;
         byte[] bitData = new byte[length];
         value.get(bitData);

         try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bitData);
            InputStream compressedInputStream = compressor.getInputStream(bis);
            ByteBuffer buffer = ByteBuffer.wrap(ODSIOUtils.toByteArray(compressedInputStream));
            bis.close();
            List<Tag<?>> data = InternalUtils.getListData(buffer, buffer.capacity());
            this.value = data;
            return this;
         } catch (IOException var12) {
            var12.printStackTrace();
            throw new ODSException("An IO Error has occurred!", var12);
         }
      }
   }

   public byte getID() {
      return 12;
   }
}
