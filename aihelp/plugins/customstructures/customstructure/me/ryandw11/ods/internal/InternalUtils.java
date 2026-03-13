package me.ryandw11.ods.internal;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.TagBuilder;
import me.ryandw11.ods.exception.CompressedObjectException;
import me.ryandw11.ods.util.KeyScout;
import me.ryandw11.ods.util.KeyScoutChild;

public class InternalUtils {
   private InternalUtils() {
   }

   protected static Tag<?> getSubObjectData(ByteBuffer data, String key) {
      String name = key.split("\\.")[0];
      String otherKey = getKey(key.split("\\."));
      TagBuilder currentBuilder = new TagBuilder();

      while(data.hasRemaining()) {
         currentBuilder.setDataType(data.get());
         currentBuilder.setDataSize(data.getInt());
         currentBuilder.setStartingIndex((long)data.position());
         currentBuilder.setNameSize(Short.valueOf(data.getShort()).intValue());
         if (currentBuilder.getNameSize() != name.getBytes(StandardCharsets.UTF_8).length) {
            data.position(Long.valueOf(currentBuilder.getStartingIndex()).intValue() + currentBuilder.getDataSize());
            currentBuilder = new TagBuilder();
         } else {
            byte[] nameBytes = new byte[currentBuilder.getNameSize()];
            data.get(nameBytes);
            String tagName = new String(nameBytes, StandardCharsets.UTF_8);
            currentBuilder.setName(tagName);
            if (name.equals(tagName)) {
               currentBuilder.setValueLength((int)currentBuilder.getStartingIndex() - data.position() + currentBuilder.getDataSize());
               currentBuilder.setValueBytes(data);
               if (otherKey != null) {
                  validateNotCompressed(currentBuilder);
                  return getSubObjectData(data, otherKey);
               }

               return currentBuilder.process();
            }

            data.position(Long.valueOf(currentBuilder.getStartingIndex()).intValue() + currentBuilder.getDataSize());
            currentBuilder = new TagBuilder();
         }
      }

      return null;
   }

   protected static boolean findSubObjectData(ByteBuffer data, String key) {
      String name = key.split("\\.")[0];
      String otherKey = getKey(key.split("\\."));
      TagBuilder currentBuilder = new TagBuilder();

      while(data.hasRemaining()) {
         currentBuilder.setDataType(data.get());
         currentBuilder.setDataSize(data.getInt());
         currentBuilder.setStartingIndex((long)data.position());
         currentBuilder.setNameSize(Short.valueOf(data.getShort()).intValue());
         if (currentBuilder.getNameSize() != name.getBytes(StandardCharsets.UTF_8).length) {
            data.position(Long.valueOf(currentBuilder.getStartingIndex()).intValue() + currentBuilder.getDataSize());
            currentBuilder = new TagBuilder();
         } else {
            byte[] nameBytes = new byte[currentBuilder.getNameSize()];
            data.get(nameBytes);
            String tagName = new String(nameBytes, StandardCharsets.UTF_8);
            currentBuilder.setName(tagName);
            if (name.equals(tagName)) {
               currentBuilder.setValueLength((int)currentBuilder.getStartingIndex() - data.position() + currentBuilder.getDataSize());
               currentBuilder.setValueBytes(data);
               if (otherKey != null) {
                  validateNotCompressed(currentBuilder);
                  return findSubObjectData(data, otherKey);
               }

               return true;
            }

            data.position(Long.valueOf(currentBuilder.getStartingIndex()).intValue() + currentBuilder.getDataSize());
            currentBuilder = new TagBuilder();
         }
      }

      return false;
   }

   private static String getKey(String[] s) {
      List<String> list = new ArrayList(Arrays.asList(s));
      list.remove(0);
      if (list.size() == 1) {
         return (String)list.get(0);
      } else {
         return list.size() < 1 ? null : String.join(".", list);
      }
   }

   protected static byte[] deleteSubObjectData(byte[] data, KeyScout counter) {
      counter.removeAmount(counter.getEnd().getSize() + 5);
      KeyScoutChild end = counter.getEnd();
      byte[] array1 = new byte[data.length - (5 + end.getSize())];
      System.arraycopy(data, 0, array1, 0, end.getStartingIndex() - 1);
      System.arraycopy(data, end.getStartingIndex() + 4 + end.getSize(), array1, end.getStartingIndex() - 1, data.length - (end.getStartingIndex() + 4 + end.getSize()));

      int index;
      int size;
      for(Iterator var4 = counter.getChildren().iterator(); var4.hasNext(); array1[index + 3] = (byte)size) {
         KeyScoutChild child = (KeyScoutChild)var4.next();
         index = child.getStartingIndex();
         size = child.getSize();
         array1[index] = (byte)(size >> 24);
         array1[index + 1] = (byte)(size >> 16);
         array1[index + 2] = (byte)(size >> 8);
      }

      return array1;
   }

   protected static byte[] replaceSubObjectData(byte[] data, KeyScout counter, byte[] dataToReplace) {
      counter.removeAmount(counter.getEnd().getSize() + 5);
      counter.addAmount(dataToReplace.length);
      KeyScoutChild end = counter.getEnd();
      byte[] array1 = new byte[data.length - (5 + end.getSize()) + dataToReplace.length];
      System.arraycopy(data, 0, array1, 0, end.getStartingIndex() - 1);
      System.arraycopy(dataToReplace, 0, array1, end.getStartingIndex() - 1, dataToReplace.length);
      System.arraycopy(data, end.getStartingIndex() + 4 + end.getSize(), array1, end.getStartingIndex() - 1 + dataToReplace.length, data.length - (end.getStartingIndex() + 4 + end.getSize()));

      int index;
      int size;
      for(Iterator var5 = counter.getChildren().iterator(); var5.hasNext(); array1[index + 3] = (byte)size) {
         KeyScoutChild child = (KeyScoutChild)var5.next();
         index = child.getStartingIndex();
         size = child.getSize();
         array1[index] = (byte)(size >> 24);
         array1[index + 1] = (byte)(size >> 16);
         array1[index + 2] = (byte)(size >> 8);
      }

      return array1;
   }

   protected static byte[] setSubObjectData(byte[] data, KeyScout counter, byte[] dataToReplace) {
      KeyScoutChild child = (KeyScoutChild)counter.getChildren().get(counter.getChildren().size() - 1);
      byte[] array1 = new byte[data.length + dataToReplace.length];
      System.arraycopy(data, 0, array1, 0, child.getStartingIndex() + 4 + child.getSize());
      System.arraycopy(dataToReplace, 0, array1, child.getStartingIndex() + 4 + child.getSize(), dataToReplace.length);
      System.arraycopy(data, child.getStartingIndex() + 4 + child.getSize(), array1, child.getStartingIndex() + 4 + child.getSize() + dataToReplace.length, data.length - (child.getStartingIndex() + 4 + child.getSize()));
      counter.addAmount(dataToReplace.length);

      int index;
      int size;
      for(Iterator var5 = counter.getChildren().iterator(); var5.hasNext(); array1[index + 3] = (byte)size) {
         KeyScoutChild childs = (KeyScoutChild)var5.next();
         index = childs.getStartingIndex();
         size = childs.getSize();
         array1[index] = (byte)(size >> 24);
         array1[index + 1] = (byte)(size >> 16);
         array1[index + 2] = (byte)(size >> 8);
      }

      return array1;
   }

   protected static KeyScout scoutObjectData(ByteBuffer data, String key, KeyScout counter) {
      String name = key.split("\\.")[0];
      String otherKey = getKey(key.split("\\."));
      TagBuilder currentBuilder = new TagBuilder();

      while(data.hasRemaining()) {
         KeyScoutChild child = new KeyScoutChild();
         currentBuilder.setDataType(data.get());
         child.setStartingIndex(data.position());
         currentBuilder.setDataSize(data.getInt());
         currentBuilder.setStartingIndex((long)data.position());
         currentBuilder.setNameSize(Short.valueOf(data.getShort()).intValue());
         if (currentBuilder.getNameSize() != name.getBytes(StandardCharsets.UTF_8).length) {
            data.position(Long.valueOf(currentBuilder.getStartingIndex()).intValue() + currentBuilder.getDataSize());
            currentBuilder = new TagBuilder();
         } else {
            byte[] nameBytes = new byte[currentBuilder.getNameSize()];
            data.get(nameBytes);
            String tagName = new String(nameBytes, StandardCharsets.UTF_8);
            currentBuilder.setName(tagName);
            if (name.equals(tagName)) {
               currentBuilder.setValueBytes(data);
               if (otherKey != null) {
                  validateNotCompressed(currentBuilder);
                  child.setSize(currentBuilder.getDataSize());
                  child.setName(currentBuilder.getName());
                  counter.addChild(child);
                  return scoutObjectData(currentBuilder.getValueBytes(), otherKey, counter);
               }

               child.setName(currentBuilder.getName());
               child.setSize(currentBuilder.getDataSize());
               counter.setEnd(child);
               return counter;
            }

            data.position(Long.valueOf(currentBuilder.getStartingIndex()).intValue() + currentBuilder.getDataSize());
            currentBuilder = new TagBuilder();
         }
      }

      return counter;
   }

   public static List<Tag<?>> getListData(ByteBuffer data, int limit) {
      List<Tag<?>> output = new ArrayList();
      TagBuilder currentBuilder = new TagBuilder();
      int initialPos = data.position();

      while(data.position() < initialPos + limit) {
         currentBuilder.setDataType(data.get());
         currentBuilder.setDataSize(data.getInt());
         currentBuilder.setStartingIndex((long)data.position());
         currentBuilder.setNameSize(data.getShort());
         byte[] nameBytes = new byte[currentBuilder.getNameSize()];
         data.get(nameBytes);
         String tagName = new String(nameBytes, StandardCharsets.UTF_8);
         currentBuilder.setName(tagName);
         currentBuilder.setValueLength((int)currentBuilder.getStartingIndex() - data.position() + currentBuilder.getDataSize());
         currentBuilder.setValueBytes(data);
         output.add(currentBuilder.process());
      }

      return output;
   }

   private static void validateNotCompressed(TagBuilder builder) {
      if (builder.getDataType() == 12) {
         throw new CompressedObjectException("Unable to traverse a Compressed Object. Consider decompressing the object '" + builder.getName() + "' first.");
      }
   }
}
