package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MappedEntitySet<T extends MappedEntity> implements MappedEntityRefSet<T> {
   @Nullable
   private final ResourceLocation tagKey;
   @Nullable
   private final List<T> entities;

   public MappedEntitySet(ResourceLocation tagKey) {
      this(tagKey, (List)null);
   }

   public MappedEntitySet(List<T> entities) {
      this((ResourceLocation)null, entities);
   }

   public MappedEntitySet(@Nullable ResourceLocation tagKey, @Nullable List<T> entities) {
      if (tagKey == null && entities == null) {
         throw new IllegalArgumentException("Illegal generic holder set: either tag key or holder ids have to be set");
      } else {
         this.tagKey = tagKey;
         this.entities = entities;
      }
   }

   public static <Z extends MappedEntity> MappedEntitySet<Z> createEmpty() {
      return new MappedEntitySet(new ArrayList(0));
   }

   public static <Z extends MappedEntity> MappedEntityRefSet<Z> readRefSet(PacketWrapper<?> wrapper) {
      int count = wrapper.readVarInt() - 1;
      if (count == -1) {
         return new MappedEntitySet(wrapper.readIdentifier());
      } else {
         int[] entries = wrapper.readVarIntArrayOfSize(Math.min(count, 65536));
         return new MappedEntitySet.IdRefSetImpl(entries);
      }
   }

   public static void writeRefSet(PacketWrapper<?> wrapper, MappedEntityRefSet<?> refSet) {
      if (refSet instanceof MappedEntitySet.IdRefSetImpl) {
         MappedEntitySet.IdRefSetImpl<?> idRefSet = (MappedEntitySet.IdRefSetImpl)refSet;
         wrapper.writeVarInt(idRefSet.entries.length + 1);
         wrapper.writeVarIntArrayOfSize(idRefSet.entries);
      } else {
         if (!(refSet instanceof MappedEntitySet)) {
            throw new UnsupportedOperationException("Unsupported mapped entity reference set implementation: " + refSet);
         }

         write(wrapper, (MappedEntitySet)refSet);
      }

   }

   public static <Z extends MappedEntity> MappedEntitySet<Z> read(PacketWrapper<?> wrapper, BiFunction<ClientVersion, Integer, Z> getter) {
      int count = wrapper.readVarInt() - 1;
      if (count == -1) {
         return new MappedEntitySet(wrapper.readIdentifier(), (List)null);
      } else {
         List<Z> entities = new ArrayList(Math.min(count, 65536));

         for(int i = 0; i < count; ++i) {
            entities.add(wrapper.readMappedEntity(getter));
         }

         return new MappedEntitySet((ResourceLocation)null, entities);
      }
   }

   public static <Z extends MappedEntity> void write(PacketWrapper<?> wrapper, MappedEntitySet<Z> set) {
      if (set.tagKey != null) {
         wrapper.writeVarInt(0);
         wrapper.writeIdentifier(set.tagKey);
      } else {
         assert set.entities != null;

         wrapper.writeVarInt(set.entities.size() + 1);
         Iterator var2 = set.entities.iterator();

         while(var2.hasNext()) {
            Z entity = (MappedEntity)var2.next();
            wrapper.writeMappedEntity(entity);
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public static <Z extends MappedEntity> MappedEntitySet<Z> decode(NBT nbt, ClientVersion version, IRegistry<Z> registry) {
      return decode(nbt, PacketWrapper.createDummyWrapper(version), registry);
   }

   public static <Z extends MappedEntity> MappedEntitySet<Z> decode(NBT nbt, PacketWrapper<?> wrapper, IRegistry<Z> registry) {
      ClientVersion version = wrapper.getServerVersion().toClientVersion();
      ArrayList list;
      if (nbt instanceof NBTString) {
         String singleEntry = ((NBTString)nbt).getValue();
         if (!singleEntry.isEmpty() && singleEntry.charAt(0) == '#') {
            String tagName = singleEntry.substring(1);
            ResourceLocation tagKey = new ResourceLocation(tagName);
            return new MappedEntitySet(tagKey);
         }

         list = new ArrayList(1);
         ResourceLocation key = new ResourceLocation(singleEntry);
         list.add(registry.getByNameOrThrow(version, key));
      } else {
         NBTList<?> listTag = (NBTList)nbt;
         list = new ArrayList(listTag.size());
         Iterator var11 = listTag.getTags().iterator();

         while(var11.hasNext()) {
            NBT tag = (NBT)var11.next();
            ResourceLocation key = new ResourceLocation(((NBTString)tag).getValue());
            list.add(registry.getByNameOrThrow(version, key));
         }
      }

      return new MappedEntitySet(list);
   }

   /** @deprecated */
   @Deprecated
   public static <Z extends MappedEntity> NBT encode(MappedEntitySet<Z> set, ClientVersion version) {
      return encodeRefSet((PacketWrapper)PacketWrapper.createDummyWrapper(version), (MappedEntityRefSet)set);
   }

   public static <Z extends MappedEntity> NBT encode(PacketWrapper<?> wrapper, MappedEntitySet<Z> set) {
      if (set.tagKey != null) {
         return new NBTString("#" + set.tagKey);
      } else {
         assert set.entities != null;

         NBTList<NBTString> listTag = NBTList.createStringList();
         Iterator var3 = set.entities.iterator();

         while(var3.hasNext()) {
            Z entity = (MappedEntity)var3.next();
            listTag.addTag(new NBTString(entity.getName().toString()));
         }

         return listTag;
      }
   }

   public static <Z extends MappedEntity> MappedEntityRefSet<Z> decodeRefSet(NBT nbt, PacketWrapper<?> wrapper) {
      return decodeRefSet(nbt, wrapper.getServerVersion().toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   public static <Z extends MappedEntity> MappedEntityRefSet<Z> decodeRefSet(NBT nbt, ClientVersion version) {
      Object list;
      if (nbt instanceof NBTString) {
         String singleEntry = ((NBTString)nbt).getValue();
         if (!singleEntry.isEmpty() && singleEntry.charAt(0) == '#') {
            String tagName = singleEntry.substring(1);
            ResourceLocation tagKey = new ResourceLocation(tagName);
            return new MappedEntitySet(tagKey);
         }

         list = Collections.singletonList(singleEntry);
      } else {
         NBTList<?> listTag = (NBTList)nbt;
         list = new ArrayList(listTag.size());
         Iterator var7 = listTag.getTags().iterator();

         while(var7.hasNext()) {
            NBT tag = (NBT)var7.next();
            ((List)list).add(((NBTString)tag).getValue());
         }
      }

      return new MappedEntitySet.NameRefSetImpl((List)list);
   }

   public static <Z extends MappedEntity> NBT encodeRefSet(PacketWrapper<?> wrapper, MappedEntityRefSet<Z> refSet) {
      return encodeRefSet(refSet, wrapper.getServerVersion().toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   public static <Z extends MappedEntity> NBT encodeRefSet(MappedEntityRefSet<Z> refSet, ClientVersion version) {
      if (!(refSet instanceof MappedEntitySet.NameRefSetImpl)) {
         if (refSet instanceof MappedEntitySet) {
            return encode((MappedEntitySet)refSet, version);
         } else {
            throw new UnsupportedOperationException("Unsupported mapped entity reference set implementation: " + refSet);
         }
      } else {
         MappedEntitySet.NameRefSetImpl<?> nameRefSet = (MappedEntitySet.NameRefSetImpl)refSet;
         NBTList<NBTString> listTag = NBTList.createStringList();
         Iterator var4 = nameRefSet.entries.iterator();

         while(var4.hasNext()) {
            String entityName = (String)var4.next();
            listTag.addTag(new NBTString(entityName));
         }

         return listTag;
      }
   }

   public MappedEntitySet<T> resolve(PacketWrapper<?> wrapper, IRegistry<T> registry) {
      return this;
   }

   public MappedEntitySet<T> resolve(ClientVersion version, IRegistryHolder registryHolder, IRegistry<T> registry) {
      return this;
   }

   public MappedEntitySet<T> resolve(ClientVersion version, IRegistry<T> registry) {
      return this;
   }

   public boolean isEmpty() {
      return this.entities != null && this.entities.isEmpty();
   }

   @Nullable
   public ResourceLocation getTagKey() {
      return this.tagKey;
   }

   @Nullable
   public List<T> getEntities() {
      return this.entities;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof MappedEntitySet)) {
         return false;
      } else {
         MappedEntitySet<?> that = (MappedEntitySet)obj;
         return !Objects.equals(this.tagKey, that.tagKey) ? false : Objects.equals(this.entities, that.entities);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.tagKey, this.entities});
   }

   public String toString() {
      return "MappedEntitySet{tagKey=" + this.tagKey + ", entities=" + this.entities + '}';
   }

   private static final class IdRefSetImpl<T extends MappedEntity> implements MappedEntityRefSet<T> {
      private final int[] entries;

      public IdRefSetImpl(int[] entries) {
         this.entries = entries;
      }

      public MappedEntitySet<T> resolve(ClientVersion version, IRegistry<T> registry) {
         List<T> entities = new ArrayList(this.entries.length);
         int[] var4 = this.entries;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            int entityId = var4[var6];
            entities.add(registry.getByIdOrThrow(version, entityId));
         }

         return new MappedEntitySet(entities);
      }

      public boolean isEmpty() {
         return this.entries.length == 0;
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof MappedEntitySet.IdRefSetImpl)) {
            return false;
         } else {
            MappedEntitySet.IdRefSetImpl<?> idRefSet = (MappedEntitySet.IdRefSetImpl)obj;
            return Arrays.equals(this.entries, idRefSet.entries);
         }
      }

      public int hashCode() {
         return Arrays.hashCode(this.entries);
      }

      public String toString() {
         return "IdRefSetImpl{entries=" + Arrays.toString(this.entries) + '}';
      }
   }

   private static final class NameRefSetImpl<T extends MappedEntity> implements MappedEntityRefSet<T> {
      private final List<String> entries;

      public NameRefSetImpl(List<String> entries) {
         this.entries = entries;
      }

      public MappedEntitySet<T> resolve(ClientVersion version, IRegistry<T> registry) {
         List<T> entities = new ArrayList(this.entries.size());
         Iterator var4 = this.entries.iterator();

         while(var4.hasNext()) {
            String entityName = (String)var4.next();
            entities.add(registry.getByNameOrThrow(version, entityName));
         }

         return new MappedEntitySet(entities);
      }

      public boolean isEmpty() {
         return this.entries.isEmpty();
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof MappedEntitySet.NameRefSetImpl)) {
            return false;
         } else {
            MappedEntitySet.NameRefSetImpl<?> that = (MappedEntitySet.NameRefSetImpl)obj;
            return this.entries.equals(that.entries);
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.entries);
      }

      public String toString() {
         return "NameRefSetImpl{entries=" + this.entries + '}';
      }
   }
}
