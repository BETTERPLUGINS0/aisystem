package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.codec;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTCodec {
   /** @deprecated */
   @Deprecated
   public static NBT jsonToNBT(JsonElement element) {
      if (element instanceof JsonPrimitive) {
         if (((JsonPrimitive)element).isBoolean()) {
            return new NBTByte(element.getAsBoolean());
         }

         if (((JsonPrimitive)element).isString()) {
            return new NBTString(element.getAsString());
         }

         if (((JsonPrimitive)element).isNumber()) {
            Number num = element.getAsNumber();
            if (num instanceof Float) {
               return new NBTFloat(num.floatValue());
            }

            if (num instanceof Double) {
               return new NBTDouble(num.doubleValue());
            }

            if (num instanceof Byte) {
               return new NBTByte(num.byteValue());
            }

            if (num instanceof Short) {
               return new NBTShort(num.shortValue());
            }

            if (num instanceof Integer || num instanceof LazilyParsedNumber) {
               return new NBTInt(num.intValue());
            }

            if (num instanceof Long) {
               return new NBTLong(num.longValue());
            }
         }
      } else {
         Iterator var3;
         if (element instanceof JsonArray) {
            List<NBT> list = new ArrayList();
            Iterator var7 = ((JsonArray)element).iterator();

            while(var7.hasNext()) {
               JsonElement var = (JsonElement)var7.next();
               list.add(jsonToNBT(var));
            }

            if (list.isEmpty()) {
               return new NBTList(NBTType.COMPOUND);
            }

            NBTList<? extends NBT> l = new NBTList(((NBT)list.get(0)).getType());
            var3 = list.iterator();

            while(var3.hasNext()) {
               NBT nbt = (NBT)var3.next();
               l.addTagUnsafe(nbt);
            }

            return l;
         }

         if (element instanceof JsonObject) {
            JsonObject obj = (JsonObject)element;
            NBTCompound compound = new NBTCompound();
            var3 = obj.entrySet().iterator();

            while(var3.hasNext()) {
               Entry<String, JsonElement> jsonEntry = (Entry)var3.next();
               compound.setTag((String)jsonEntry.getKey(), jsonToNBT((JsonElement)jsonEntry.getValue()));
            }

            return compound;
         }

         if (element instanceof JsonNull || element == null) {
            return new NBTCompound();
         }
      }

      throw new IllegalStateException("Failed to convert JSON to NBT " + element.toString());
   }

   /** @deprecated */
   @Deprecated
   public static JsonElement nbtToJson(NBT nbt, boolean parseByteAsBool) {
      if (nbt instanceof NBTNumber) {
         if (nbt instanceof NBTByte && parseByteAsBool) {
            byte val = ((NBTByte)nbt).getAsByte();
            if (val == 0) {
               return new JsonPrimitive(false);
            }

            if (val == 1) {
               return new JsonPrimitive(true);
            }
         }

         return new JsonPrimitive(((NBTNumber)nbt).getAsNumber());
      } else if (nbt instanceof NBTString) {
         return new JsonPrimitive(((NBTString)nbt).getValue());
      } else if (nbt instanceof NBTList) {
         NBTList<? extends NBT> list = (NBTList)nbt;
         JsonArray jsonArray = new JsonArray();
         list.getTags().forEach((tag) -> {
            jsonArray.add(nbtToJson(tag, parseByteAsBool));
         });
         return jsonArray;
      } else if (nbt instanceof NBTEnd) {
         throw new IllegalStateException("Encountered the NBTEnd tag during the NBT to JSON conversion: " + nbt.toString());
      } else if (!(nbt instanceof NBTCompound)) {
         throw new IllegalStateException("Failed to convert NBT to JSON.");
      } else {
         JsonObject jsonObject = new JsonObject();
         Map<String, NBT> compoundTags = ((NBTCompound)nbt).getTags();
         Iterator var4 = compoundTags.entrySet().iterator();

         while(var4.hasNext()) {
            Entry<String, NBT> entry = (Entry)var4.next();
            JsonElement jsonValue = nbtToJson((NBT)entry.getValue(), parseByteAsBool);
            jsonObject.add((String)entry.getKey(), jsonValue);
         }

         return jsonObject;
      }
   }

   public static NBT readNBTFromBuffer(Object byteBuf, ServerVersion serverVersion) {
      NBTLimiter limiter = NBTLimiter.forBuffer(byteBuf);
      return readNBTFromBuffer(byteBuf, serverVersion, limiter);
   }

   public static NBT readNBTFromBuffer(Object byteBuf, ServerVersion serverVersion, NBTLimiter limiter) {
      if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         try {
            boolean named = serverVersion.isOlderThan(ServerVersion.V_1_20_2);
            return DefaultNBTSerializer.INSTANCE.deserializeTag(limiter, new ByteBufInputStream(byteBuf), named);
         } catch (IOException var9) {
            throw new IllegalStateException(var9);
         }
      } else {
         try {
            short length = ByteBufHelper.readShort(byteBuf);
            if (length < 0) {
               return null;
            } else {
               Object slicedBuffer = ByteBufHelper.readSlice(byteBuf, length);
               DataInputStream stream = new DataInputStream(new GZIPInputStream(new ByteBufInputStream(slicedBuffer)));

               NBT var6;
               try {
                  var6 = DefaultNBTSerializer.INSTANCE.deserializeTag(limiter, stream);
               } catch (Throwable var10) {
                  try {
                     stream.close();
                  } catch (Throwable var8) {
                     var10.addSuppressed(var8);
                  }

                  throw var10;
               }

               stream.close();
               return var6;
            }
         } catch (IOException var11) {
            throw new IllegalStateException(var11);
         }
      }
   }

   public static void writeNBTToBuffer(Object byteBuf, ServerVersion serverVersion, NBTCompound tag) {
      writeNBTToBuffer(byteBuf, serverVersion, (NBT)tag);
   }

   public static void writeNBTToBuffer(Object byteBuf, ServerVersion serverVersion, NBT tag) {
      if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         try {
            ByteBufOutputStream outputStream = new ByteBufOutputStream(byteBuf);

            try {
               if (tag != null) {
                  boolean named = serverVersion.isOlderThan(ServerVersion.V_1_20_2);
                  DefaultNBTSerializer.INSTANCE.serializeTag(outputStream, tag, named);
               } else {
                  DefaultNBTSerializer.INSTANCE.serializeTag(outputStream, NBTEnd.INSTANCE);
               }
            } catch (Throwable var12) {
               try {
                  outputStream.close();
               } catch (Throwable var8) {
                  var12.addSuppressed(var8);
               }

               throw var12;
            }

            outputStream.close();
         } catch (IOException var13) {
            throw new IllegalStateException(var13);
         }
      } else if (tag == null) {
         ByteBufHelper.writeShort(byteBuf, -1);
      } else {
         int lengthWriterIndex = ByteBufHelper.writerIndex(byteBuf);
         ByteBufHelper.writeShort(byteBuf, 0);
         int writerIndexDataStart = ByteBufHelper.writerIndex(byteBuf);

         try {
            DataOutputStream outputstream = new DataOutputStream(new GZIPOutputStream(new ByteBufOutputStream(byteBuf)));

            try {
               DefaultNBTSerializer.INSTANCE.serializeTag(outputstream, tag);
            } catch (Throwable var10) {
               try {
                  outputstream.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }

               throw var10;
            }

            outputstream.close();
         } catch (Exception var11) {
            throw new IllegalStateException(var11);
         }

         int writerIndexDataEnd = ByteBufHelper.writerIndex(byteBuf);
         ByteBufHelper.writerIndex(byteBuf, lengthWriterIndex);
         ByteBufHelper.writeShort(byteBuf, writerIndexDataEnd - writerIndexDataStart);
         ByteBufHelper.writerIndex(byteBuf, writerIndexDataEnd);
      }

   }
}
