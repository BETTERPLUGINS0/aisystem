package me.ryandw11.ods.internal;

import java.io.BufferedOutputStream;
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
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterOutputStream;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.compression.Compressor;
import me.ryandw11.ods.exception.ODSException;
import me.ryandw11.ods.io.ODSIOUtils;
import me.ryandw11.ods.tags.ObjectTag;
import me.ryandw11.ods.util.KeyScout;
import me.ryandw11.ods.util.KeyScoutChild;

public class ODSFile implements ODSInternal {
   private final File file;
   private final Compressor compression;

   public ODSFile(File file, Compressor compression) {
      this.file = file;
      this.compression = compression;
   }

   private OutputStream getOutputStream() throws IOException {
      FileOutputStream fos = new FileOutputStream(this.file);
      return this.compression.getOutputStream(fos);
   }

   private InputStream getInputStream() throws IOException {
      FileInputStream fis = new FileInputStream(this.file);
      return this.compression.getInputStream(fis);
   }

   private ByteBuffer getInputBuffer(InputStream stream) throws IOException {
      if (stream instanceof FileInputStream) {
         FileInputStream fis = (FileInputStream)stream;
         Throwable var3 = null;

         MappedByteBuffer var5;
         try {
            FileChannel channel = fis.getChannel();
            var5 = channel.map(MapMode.READ_ONLY, 0L, channel.size());
         } catch (Throwable var14) {
            var3 = var14;
            throw var14;
         } finally {
            if (fis != null) {
               if (var3 != null) {
                  try {
                     fis.close();
                  } catch (Throwable var13) {
                     var3.addSuppressed(var13);
                  }
               } else {
                  fis.close();
               }
            }

         }

         return var5;
      } else {
         return ByteBuffer.wrap(ODSIOUtils.toByteArray(stream));
      }
   }

   public <T extends Tag<?>> T get(String key) {
      try {
         if (!this.file.exists()) {
            return null;
         } else {
            InputStream is = this.getInputStream();
            ByteBuffer buffer = this.getInputBuffer(is);
            T out = InternalUtils.getSubObjectData(buffer, key);
            buffer.clear();
            return out;
         }
      } catch (IOException var5) {
         throw new ODSException("Error when receiving information from a file.", var5);
      } catch (BufferUnderflowException | BufferOverflowException var6) {
         throw new ODSException("Invalid file format or the file has been tampered with / corrupted.");
      }
   }

   public List<Tag<?>> getAll() {
      try {
         if (!this.file.exists()) {
            return null;
         } else {
            InputStream is = this.getInputStream();
            ByteBuffer buffer = this.getInputBuffer(is);
            List<Tag<?>> output = InternalUtils.getListData(buffer, buffer.limit());
            is.close();
            return output;
         }
      } catch (IOException var4) {
         throw new ODSException("Error when receiving information from a file.", var4);
      } catch (BufferUnderflowException | BufferOverflowException var5) {
         throw new ODSException("Invalid file format or the file has been tampered with / corrupted.");
      }
   }

   public void save(List<? extends Tag<?>> tags) {
      try {
         OutputStream os = this.getOutputStream();
         DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
         Iterator var4 = tags.iterator();

         while(var4.hasNext()) {
            Tag<?> tag = (Tag)var4.next();
            tag.writeData(dos);
         }

         dos.close();
         os.close();
      } catch (IOException var6) {
         throw new ODSException("Error when saving information to the file", var6);
      }
   }

   public void append(Tag<?> tag) {
      try {
         byte[] data = new byte[0];
         if (!this.file.exists()) {
            if (!this.file.createNewFile()) {
               throw new ODSException("Unable to create file when appending tag.");
            }
         } else {
            InputStream is = this.getInputStream();
            data = ODSIOUtils.toByteArray(is);
            is.close();
         }

         OutputStream os = this.getOutputStream();
         DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
         dos.write(data);
         tag.writeData(dos);
         dos.close();
         os.close();
         this.finish(os);
      } catch (IOException var5) {
         throw new ODSException("Error when saving information to the file", var5);
      }
   }

   public void appendAll(List<Tag<?>> tags) {
      try {
         byte[] data = new byte[0];
         if (!this.file.exists()) {
            if (!this.file.createNewFile()) {
               throw new ODSException("Unable to create file when appending all tags.");
            }
         } else {
            InputStream is = this.getInputStream();
            data = ODSIOUtils.toByteArray(is);
            is.close();
         }

         OutputStream os = this.getOutputStream();
         DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
         dos.write(data);
         Iterator var5 = tags.iterator();

         while(var5.hasNext()) {
            Tag<?> tag = (Tag)var5.next();
            tag.writeData(dos);
         }

         dos.close();
         this.finish(os);
      } catch (IOException var7) {
         throw new ODSException("Error when saving information to the file", var7);
      }
   }

   public boolean find(String key) {
      try {
         InputStream is = this.getInputStream();
         ByteBuffer buffer = this.getInputBuffer(is);
         return InternalUtils.findSubObjectData(buffer, key);
      } catch (IOException var4) {
         return false;
      } catch (BufferUnderflowException | BufferOverflowException var5) {
         throw new ODSException("Invalid file format or the file has been tampered with / corrupted.");
      }
   }

   public boolean delete(String key) {
      try {
         InputStream is = this.getInputStream();
         ByteBuffer buffer = this.getInputBuffer(is);
         is.close();
         KeyScout counter = InternalUtils.scoutObjectData(buffer, key, new KeyScout());
         if (counter == null) {
            return false;
         } else {
            byte[] deleteReturn = InternalUtils.deleteSubObjectData(buffer.array(), counter);
            OutputStream out = this.getOutputStream();
            out.write(deleteReturn);
            out.close();
            return true;
         }
      } catch (IOException var7) {
         return false;
      } catch (BufferUnderflowException | BufferOverflowException var8) {
         throw new ODSException("Invalid file format or the file has been tampered with / corrupted.");
      }
   }

   public boolean replaceData(String key, Tag<?> replacement) {
      try {
         InputStream is = this.getInputStream();
         ByteBuffer buffer = this.getInputBuffer(is);
         is.close();
         KeyScout counter = InternalUtils.scoutObjectData(buffer, key, new KeyScout());
         if (counter.getEnd() == null) {
            return false;
         } else {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(byteArrayOut);
            replacement.writeData(dos);
            byte[] replaceReturn = InternalUtils.replaceSubObjectData(buffer.array(), counter, byteArrayOut.toByteArray());
            OutputStream out = this.getOutputStream();
            out.write(replaceReturn);
            out.close();
            dos.close();
            byteArrayOut.close();
            return true;
         }
      } catch (IOException var10) {
         return false;
      } catch (BufferUnderflowException | BufferOverflowException var11) {
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
            InputStream is = this.getInputStream();
            ByteBuffer buffer = this.getInputBuffer(is);
            is.close();
            KeyScout counter = InternalUtils.scoutObjectData(buffer, key, new KeyScout());
            if (counter.getEnd() == null) {
               if (counter.getChildren().size() < 1) {
                  this.append(value);
                  return;
               }

               StringBuilder existingKey = new StringBuilder();

               KeyScoutChild child;
               for(Iterator var7 = counter.getChildren().iterator(); var7.hasNext(); existingKey.append(child.getName())) {
                  child = (KeyScoutChild)var7.next();
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

                  for(Iterator var13 = keys.iterator(); var13.hasNext(); ++i) {
                     String s = (String)var13.next();
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
               InputStream stream = this.getInputStream();
               byte[] output = InternalUtils.setSubObjectData(ODSIOUtils.toByteArray(stream), counter, data);
               stream.close();
               OutputStream out = this.getOutputStream();
               out.write(output);
               out.close();
            } else {
               ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
               DataOutputStream dos = new DataOutputStream(byteArrayOut);
               value.writeData(dos);
               byte[] replaceReturn = InternalUtils.replaceSubObjectData(buffer.array(), counter, byteArrayOut.toByteArray());
               OutputStream out = this.getOutputStream();
               out.write(replaceReturn);
               out.close();
               dos.close();
               byteArrayOut.close();
            }

         } catch (IOException var16) {
            throw new ODSException("An error had occurred when trying to set data. Does that key exist?", var16);
         } catch (BufferUnderflowException | BufferOverflowException var17) {
            throw new ODSException("Invalid file format or the file has been tampered with / corrupted.");
         }
      }
   }

   public byte[] export(Compressor compressor) {
      try {
         InputStream io = this.getInputStream();
         byte[] data = ODSIOUtils.toByteArray(io);
         io.close();
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         OutputStream os = compressor.getOutputStream(bos);
         os.write(data);
         byte[] output = bos.toByteArray();
         os.close();
         return output;
      } catch (IOException var7) {
         throw new ODSException("Unable to export data from file.", var7);
      }
   }

   public void importFile(File file, Compressor compressor) {
      try {
         InputStream is = compressor.getInputStream(new FileInputStream(file));
         Throwable var4 = null;

         try {
            byte[] data = ODSIOUtils.toByteArray(is);

            try {
               OutputStream fos = this.compression.getOutputStream(new FileOutputStream(this.file));
               Throwable var7 = null;

               try {
                  fos.write(data);
               } catch (Throwable var34) {
                  var7 = var34;
                  throw var34;
               } finally {
                  if (fos != null) {
                     if (var7 != null) {
                        try {
                           fos.close();
                        } catch (Throwable var33) {
                           var7.addSuppressed(var33);
                        }
                     } else {
                        fos.close();
                     }
                  }

               }
            } catch (IOException var36) {
               throw new ODSException("Unable to export bytes to file.", var36);
            }
         } catch (Throwable var37) {
            var4 = var37;
            throw var37;
         } finally {
            if (is != null) {
               if (var4 != null) {
                  try {
                     is.close();
                  } catch (Throwable var32) {
                     var4.addSuppressed(var32);
                  }
               } else {
                  is.close();
               }
            }

         }

      } catch (IOException var39) {
         throw new ODSException("Unable to import bytes from files.", var39);
      }
   }

   public void saveToFile(File file, Compressor compressor) {
      try {
         InputStream is = this.compression.getInputStream(new FileInputStream(this.file));
         Throwable var4 = null;

         try {
            byte[] data = ODSIOUtils.toByteArray(is);

            try {
               OutputStream fos = compressor.getOutputStream(new FileOutputStream(file));
               Throwable var7 = null;

               try {
                  fos.write(data);
               } catch (Throwable var34) {
                  var7 = var34;
                  throw var34;
               } finally {
                  if (fos != null) {
                     if (var7 != null) {
                        try {
                           fos.close();
                        } catch (Throwable var33) {
                           var7.addSuppressed(var33);
                        }
                     } else {
                        fos.close();
                     }
                  }

               }
            } catch (IOException var36) {
               throw new ODSException("Unable to export bytes to file.", var36);
            }
         } catch (Throwable var37) {
            var4 = var37;
            throw var37;
         } finally {
            if (is != null) {
               if (var4 != null) {
                  try {
                     is.close();
                  } catch (Throwable var32) {
                     var4.addSuppressed(var32);
                  }
               } else {
                  is.close();
               }
            }

         }

      } catch (IOException var39) {
         throw new ODSException("Unable to import bytes from files.", var39);
      }
   }

   public void clear() {
      try {
         if (!this.file.createNewFile()) {
            throw new ODSException("Unable to clear file. Does the file have the correct permission?");
         }
      } catch (IOException var2) {
         throw new ODSException("IO error occurred when clearing the file.", var2);
      }
   }

   private void finish(OutputStream stream) {
      try {
         if (stream instanceof GZIPOutputStream) {
            ((GZIPOutputStream)stream).finish();
         }

         if (stream instanceof InflaterOutputStream) {
            ((InflaterOutputStream)stream).finish();
         }

      } catch (IOException var3) {
         throw new ODSException("An error has occurred while attempting to close the output stream of the file.", var3);
      }
   }
}
