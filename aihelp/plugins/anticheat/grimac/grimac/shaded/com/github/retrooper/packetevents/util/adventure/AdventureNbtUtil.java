package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagType;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagTypes;
import ac.grim.grimac.shaded.kyori.adventure.nbt.EndBinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.TagStringIO;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AdventureNbtUtil {
   private static final BinaryTagType<?>[] NBT_TAG_TYPES = buildNbtTagTypes();

   private AdventureNbtUtil() {
   }

   private static BinaryTagType<?>[] buildNbtTagTypes() {
      BinaryTagTypes.BYTE.id();
      List types = null;

      int i;
      try {
         Field[] var1 = BinaryTagType.class.getDeclaredFields();
         i = var1.length;

         for(int var3 = 0; var3 < i; ++var3) {
            Field field = var1[var3];
            if (Modifier.isStatic(field.getModifiers()) && List.class.isAssignableFrom(field.getType())) {
               field.setAccessible(true);
               Object value = field.get((Object)null);
               if (value instanceof List && ((List)value).contains(BinaryTagTypes.BYTE)) {
                  types = (List)value;
                  break;
               }
            }
         }
      } catch (Exception var6) {
         throw new RuntimeException("Error scanning for Adventure NBT registry", var6);
      }

      if (types == null) {
         throw new RuntimeException("Could not locate Adventure NBT registry field (mapping mismatch?)");
      } else {
         BinaryTagType<?>[] nbtTagTypes = new BinaryTagType[types.size()];

         for(i = 0; i < nbtTagTypes.length; ++i) {
            BinaryTagType<? extends BinaryTag> type = (BinaryTagType)types.get(i);
            if (type.id() != i) {
               throw new IllegalStateException("Registered nbt tag types are wrong: " + type.id() + " != " + i);
            }

            nbtTagTypes[i] = type;
         }

         return nbtTagTypes;
      }
   }

   public static BinaryTag readAdventureTag(Object buf) {
      byte tagTypeId = ByteBufHelper.readByte(buf);
      if (tagTypeId == BinaryTagTypes.END.id()) {
         return EndBinaryTag.endBinaryTag();
      } else {
         BinaryTagType tagType = NBT_TAG_TYPES[tagTypeId];

         try {
            return tagType.read(new ByteBufInputStream(buf));
         } catch (IOException var4) {
            throw new RuntimeException("Error while reading adventure nbt tag from buf: " + buf, var4);
         }
      }
   }

   public static void writeAdventureTag(Object buf, BinaryTag tag) {
      BinaryTagType<? super BinaryTag> tagType = tag.type();
      ByteBufHelper.writeByte(buf, tagType.id());
      if (tagType.id() != BinaryTagTypes.END.id()) {
         try {
            tagType.write(tag, new ByteBufOutputStream(buf));
         } catch (IOException var4) {
            throw new RuntimeException("Error while writing adventure nbt tag to buf: " + tag, var4);
         }
      }

   }

   public static NBT fromAdventure(BinaryTag tag) {
      Object buf = UnpooledByteBufAllocationHelper.buffer();

      NBT var2;
      try {
         writeAdventureTag(buf, tag);
         var2 = NBTCodec.readNBTFromBuffer(buf, ServerVersion.getLatest());
      } finally {
         ByteBufHelper.release(buf);
      }

      return var2;
   }

   public static BinaryTag toAdventure(NBT tag) {
      Object buf = UnpooledByteBufAllocationHelper.buffer();

      BinaryTag var2;
      try {
         NBTCodec.writeNBTToBuffer(buf, ServerVersion.getLatest(), tag);
         var2 = readAdventureTag(buf);
      } finally {
         ByteBufHelper.release(buf);
      }

      return var2;
   }

   public static NBT fromString(String string) {
      BinaryTag advTag;
      try {
         advTag = TagStringIO.tagStringIO().asTag(string);
      } catch (IOException var3) {
         throw new RuntimeException("Error while decoding nbt from string: " + string, var3);
      }

      return fromAdventure(advTag);
   }

   public static String toString(NBT tag) {
      BinaryTag advTag = toAdventure(tag);

      try {
         return TagStringIO.tagStringIO().asString(advTag);
      } catch (IOException var3) {
         throw new RuntimeException("Error while encoding nbt to string: " + advTag, var3);
      }
   }
}
