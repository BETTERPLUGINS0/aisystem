package me.ryandw11.ods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.ryandw11.ods.exception.ODSException;
import me.ryandw11.ods.tags.ByteTag;
import me.ryandw11.ods.tags.CharTag;
import me.ryandw11.ods.tags.CompressedObjectTag;
import me.ryandw11.ods.tags.DoubleTag;
import me.ryandw11.ods.tags.FloatTag;
import me.ryandw11.ods.tags.IntTag;
import me.ryandw11.ods.tags.InvalidTag;
import me.ryandw11.ods.tags.ListTag;
import me.ryandw11.ods.tags.LongTag;
import me.ryandw11.ods.tags.MapTag;
import me.ryandw11.ods.tags.ObjectTag;
import me.ryandw11.ods.tags.ShortTag;
import me.ryandw11.ods.tags.StringTag;

public class TagBuilder {
   private int dataType = -1;
   private int dataSize = -1;
   private long startingIndex = -1L;
   private String name = "";
   private int nameSize;
   private ByteBuffer valueBytes = null;
   private int valueLength = -1;

   public void setDataType(int dataType) {
      this.dataType = dataType;
   }

   public int getDataType() {
      return this.dataType;
   }

   public void setDataSize(int size) {
      this.dataSize = size;
   }

   public int getDataSize() {
      return this.dataSize;
   }

   public void setStartingIndex(long startingIndex) {
      this.startingIndex = startingIndex;
   }

   public long getStartingIndex() {
      return this.startingIndex;
   }

   public void setName(String s) {
      this.name = s;
   }

   public String getName() {
      return this.name;
   }

   public void setNameSize(int size) {
      this.nameSize = size;
   }

   public int getNameSize() {
      return this.nameSize;
   }

   public void setValueBytes(ByteBuffer valueBytes) {
      this.valueBytes = valueBytes;
   }

   public ByteBuffer getValueBytes() {
      return this.valueBytes;
   }

   public void setValueLength(int length) {
      this.valueLength = length;
   }

   public int getValueLength() {
      return this.valueLength;
   }

   public Tag<?> process() {
      switch(this.getDataType()) {
      case 1:
         return (new StringTag(this.name, (String)null)).createFromData(this.valueBytes, this.valueLength);
      case 2:
         return (new IntTag(this.name, -1)).createFromData(this.valueBytes, this.valueLength);
      case 3:
         return (new FloatTag(this.name, -1.0F)).createFromData(this.valueBytes, this.valueLength);
      case 4:
         return (new DoubleTag(this.name, -1.0D)).createFromData(this.valueBytes, this.valueLength);
      case 5:
         return (new ShortTag(this.name, (short)-1)).createFromData(this.valueBytes, this.valueLength);
      case 6:
         return (new LongTag(this.name, -1L)).createFromData(this.valueBytes, this.valueLength);
      case 7:
         return (new CharTag(this.name, ' ')).createFromData(this.valueBytes, this.valueLength);
      case 8:
         return (new ByteTag(this.name, (byte)0)).createFromData(this.valueBytes, this.valueLength);
      case 9:
         return (new ListTag(this.name, (List)null)).createFromData(this.valueBytes, this.valueLength);
      case 10:
         return (new MapTag(this.name, (Map)null)).createFromData(this.valueBytes, this.valueLength);
      case 11:
         return (new ObjectTag(this.name)).createFromData(this.valueBytes, this.valueLength);
      case 12:
         return (new CompressedObjectTag(this.name)).createFromData(this.valueBytes, this.valueLength);
      default:
         Iterator var1 = ODS.getCustomTags().iterator();

         Tag tag;
         do {
            if (!var1.hasNext()) {
               if (!ODS.ignoreInvalidCustomTags) {
                  throw new RuntimeException("Error: That data type does not exist!");
               }

               return (new InvalidTag(this.name)).createFromData(this.valueBytes, this.valueLength);
            }

            tag = (Tag)var1.next();
         } while(this.getDataType() != tag.getID());

         Class tagClazz = tag.getClass();

         try {
            return ((Tag)tagClazz.getConstructor(String.class, (Class)((ParameterizedType)tagClazz.getGenericInterfaces()[0]).getActualTypeArguments()[0]).newInstance(this.name, null)).createFromData(this.valueBytes, this.valueLength);
         } catch (NoSuchMethodException var5) {
            throw new ODSException("Invalid Custom Tag Constructor.");
         } catch (InstantiationException | InvocationTargetException | IllegalAccessException var6) {
            throw new ODSException("Unable to create an instance of a custom tag. IllegalAccessException, InstantiationException, or InvocationTargetException.");
         }
      }
   }
}
