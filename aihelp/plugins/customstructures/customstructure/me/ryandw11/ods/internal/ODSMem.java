package me.ryandw11.ods.internal;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.compression.Compressor;
import me.ryandw11.ods.exception.ODSException;
import me.ryandw11.ods.io.ODSIOUtils;
import me.ryandw11.ods.tags.ObjectTag;
import me.ryandw11.ods.util.KeyScout;
import me.ryandw11.ods.util.KeyScoutChild;

public class ODSMem implements ODSInternal {
   private ByteBuffer memBuffer;

   public ODSMem(byte[] data, Compressor compressor) {
      ByteArrayInputStream bis = new ByteArrayInputStream(data);

      try {
         InputStream is = compressor.getInputStream(bis);
         this.memBuffer = ByteBuffer.wrap(ODSIOUtils.toByteArray(is));
      } catch (IOException var5) {
         throw new ODSException("Cannot decompress data.", var5);
      }
   }

   public ODSMem(ByteBuffer buffer, Compressor compressor) {
      ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());

      try {
         InputStream is = compressor.getInputStream(bis);
         this.memBuffer = ByteBuffer.wrap(ODSIOUtils.toByteArray(is));
      } catch (IOException var5) {
         throw new ODSException("Cannot decompress data.", var5);
      }
   }

   public ODSMem() {
      this.memBuffer = ByteBuffer.allocate(1);
   }

   private ByteBuffer getInputBuffer() {
      this.memBuffer.position(0);
      return this.memBuffer;
   }

   public <T extends Tag<?>> T get(String key) {
      try {
         this.memBuffer.position(0);
         if (this.memBuffer.limit() == 1) {
            return null;
         } else {
            ByteBuffer buffer = this.getInputBuffer();
            T out = InternalUtils.getSubObjectData(buffer, key);
            return out;
         }
      } catch (BufferUnderflowException | BufferOverflowException var4) {
         throw new ODSException("Invalid format or the buffer has been tampered with / corrupted.");
      }
   }

   public List<Tag<?>> getAll() {
      try {
         if (this.memBuffer.limit() == 1) {
            return null;
         } else {
            ByteBuffer buffer = this.getInputBuffer();
            List<Tag<?>> output = InternalUtils.getListData(buffer, buffer.limit());
            return output;
         }
      } catch (BufferUnderflowException | BufferOverflowException var3) {
         throw new ODSException("Invalid format or the buffer has been tampered with / corrupted.");
      }
   }

   public void save(List<? extends Tag<?>> tags) {
      try {
         ByteArrayOutputStream os = new ByteArrayOutputStream();
         DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
         Iterator var4 = tags.iterator();

         while(var4.hasNext()) {
            Tag<?> tag = (Tag)var4.next();
            tag.writeData(dos);
         }

         dos.close();
         os.close();
         this.memBuffer = ByteBuffer.wrap(os.toByteArray());
      } catch (IOException var6) {
         throw new ODSException("Error when saving information to the buffer", var6);
      }
   }

   public void append(Tag<?> tag) {
      try {
         byte[] data = this.memBuffer.array();
         ByteArrayOutputStream os = new ByteArrayOutputStream();
         DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
         dos.write(data);
         tag.writeData(dos);
         dos.close();
         os.close();
         this.memBuffer = ByteBuffer.wrap(data);
      } catch (IOException var5) {
         throw new ODSException("Error when saving information to the buffer", var5);
      }
   }

   public void appendAll(List<Tag<?>> tags) {
      try {
         byte[] data = this.memBuffer.array();
         ByteArrayOutputStream os = new ByteArrayOutputStream();
         DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
         dos.write(data);
         Iterator var5 = tags.iterator();

         while(var5.hasNext()) {
            Tag<?> tag = (Tag)var5.next();
            tag.writeData(dos);
         }

         dos.close();
         this.memBuffer = ByteBuffer.wrap(os.toByteArray());
      } catch (IOException var7) {
         throw new ODSException("Error when saving information to the buffer", var7);
      }
   }

   public boolean find(String key) {
      try {
         ByteBuffer buffer = this.getInputBuffer();
         return InternalUtils.findSubObjectData(buffer, key);
      } catch (BufferUnderflowException | BufferOverflowException var3) {
         throw new ODSException("Invalid format or the buffer has been tampered with / corrupted.");
      }
   }

   public boolean delete(String key) {
      try {
         ByteBuffer buffer = this.getInputBuffer();
         KeyScout counter = InternalUtils.scoutObjectData(buffer, key, new KeyScout());
         if (counter == null) {
            return false;
         } else {
            byte[] deleteReturn = InternalUtils.deleteSubObjectData(buffer.array(), counter);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(deleteReturn);
            this.memBuffer = ByteBuffer.wrap(out.toByteArray());
            out.close();
            return true;
         }
      } catch (IOException var6) {
         return false;
      } catch (BufferUnderflowException | BufferOverflowException var7) {
         throw new ODSException("Invalid format or the buffer has been tampered with / corrupted.");
      }
   }

   public boolean replaceData(String key, Tag<?> replacement) {
      try {
         ByteBuffer buffer = this.getInputBuffer();
         KeyScout counter = InternalUtils.scoutObjectData(buffer, key, new KeyScout());
         if (counter.getEnd() == null) {
            return false;
         } else {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(byteArrayOut);
            replacement.writeData(dos);
            byte[] replaceReturn = InternalUtils.replaceSubObjectData(buffer.array(), counter, byteArrayOut.toByteArray());
            this.memBuffer = ByteBuffer.wrap(replaceReturn);
            dos.close();
            byteArrayOut.close();
            return true;
         }
      } catch (IOException var8) {
         return false;
      } catch (BufferUnderflowException | BufferOverflowException var9) {
         throw new ODSException("Invalid file format or the file has been tampered with / corrupted.");
      }
   }

   public void set(String key, Tag<?> value) {
      if (value == null) {
         boolean output = this.delete(key);
         if (!output) {
            throw new ODSException("The key " + key + " does not exist!");
         }
      } else if (key.equals("")) {
         this.save(Collections.singletonList(value));
      } else {
         try {
            ByteBuffer buffer = this.getInputBuffer();
            KeyScout counter = InternalUtils.scoutObjectData(buffer, key, new KeyScout());
            if (counter.getEnd() == null) {
               if (counter.getChildren().size() < 1) {
                  this.append(value);
                  return;
               }

               StringBuilder existingKey = new StringBuilder();

               KeyScoutChild child;
               for(Iterator var6 = counter.getChildren().iterator(); var6.hasNext(); existingKey.append(child.getName())) {
                  child = (KeyScoutChild)var6.next();
                  if (existingKey.length() != 0) {
                     existingKey.append(".");
                  }
               }

               String newKey = key.replace(existingKey + ".", "");
               Object currentData;
               if (newKey.split("\\.").length <= 1) {
                  currentData = value;
               } else {
                  ObjectTag output = null;
                  ObjectTag curTag = null;
                  List<String> keys = new ArrayList(Arrays.asList(newKey.split("\\.")));
                  int i = 0;

                  for(Iterator var12 = keys.iterator(); var12.hasNext(); ++i) {
                     String s = (String)var12.next();
                     if (i == 0) {
                        output = new ObjectTag(s);
                        curTag = output;
                     } else if (i == keys.size() - 1) {
                        curTag.addTag(value);
                     } else {
                        ObjectTag tag = new ObjectTag(s);
                        curTag.addTag(tag);
                        curTag = tag;
                     }
                  }

                  currentData = output;
               }

               ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
               DataOutputStream dos = new DataOutputStream(byteArrayOut);

               assert currentData != null;

               ((Tag)currentData).writeData(dos);
               byte[] data = byteArrayOut.toByteArray();
               dos.close();
               byteArrayOut.close();
               byte[] output = InternalUtils.setSubObjectData(this.memBuffer.array(), counter, data);
               this.memBuffer = ByteBuffer.wrap(output);
            } else {
               ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
               DataOutputStream dos = new DataOutputStream(byteArrayOut);
               value.writeData(dos);
               byte[] replaceReturn = InternalUtils.replaceSubObjectData(buffer.array(), counter, byteArrayOut.toByteArray());
               this.memBuffer = ByteBuffer.wrap(replaceReturn);
               dos.close();
               byteArrayOut.close();
            }

         } catch (IOException var15) {
            throw new ODSException("An error had occurred when trying to set data. Does that key exist?", var15);
         } catch (BufferUnderflowException | BufferOverflowException var16) {
            throw new ODSException("Invalid format or the buffer has been tampered with / corrupted.");
         }
      }
   }

   public byte[] export(Compressor compressor) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      try {
         OutputStream os = compressor.getOutputStream(bos);
         os.write(this.memBuffer.array());
         byte[] output = bos.toByteArray();
         os.close();
         return output;
      } catch (IOException var5) {
         throw new ODSException("Unable to export bytes from memory.", var5);
      }
   }

   public void importFile(File file, Compressor compressor) {
      try {
         InputStream is = compressor.getInputStream(new FileInputStream(file));
         Throwable var4 = null;

         try {
            byte[] data = ODSIOUtils.toByteArray(is);
            this.memBuffer = ByteBuffer.wrap(data);
         } catch (Throwable var14) {
            var4 = var14;
            throw var14;
         } finally {
            if (is != null) {
               if (var4 != null) {
                  try {
                     is.close();
                  } catch (Throwable var13) {
                     var4.addSuppressed(var13);
                  }
               } else {
                  is.close();
               }
            }

         }

      } catch (IOException var16) {
         throw new ODSException("Unable to import bytes from files.", var16);
      }
   }

   public void saveToFile(File file, Compressor compressor) {
      try {
         OutputStream fos = compressor.getOutputStream(new FileOutputStream(file));
         Throwable var4 = null;

         try {
            fos.write(this.memBuffer.array());
         } catch (Throwable var14) {
            var4 = var14;
            throw var14;
         } finally {
            if (fos != null) {
               if (var4 != null) {
                  try {
                     fos.close();
                  } catch (Throwable var13) {
                     var4.addSuppressed(var13);
                  }
               } else {
                  fos.close();
               }
            }

         }

      } catch (IOException var16) {
         throw new ODSException("Unable to export bytes to file.", var16);
      }
   }

   public void clear() {
      this.memBuffer.clear();
   }
}
