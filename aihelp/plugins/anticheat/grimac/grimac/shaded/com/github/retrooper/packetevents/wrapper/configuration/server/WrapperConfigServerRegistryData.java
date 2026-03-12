package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class WrapperConfigServerRegistryData extends PacketWrapper<WrapperConfigServerRegistryData> {
   private NBTCompound registryData;
   private ResourceLocation registryKey;
   private List<WrapperConfigServerRegistryData.RegistryElement> elements;

   public WrapperConfigServerRegistryData(PacketSendEvent event) {
      super(event);
   }

   @ApiStatus.Obsolete
   public WrapperConfigServerRegistryData(NBTCompound registryData) {
      this(registryData, (ResourceLocation)null, (List)null);
   }

   public WrapperConfigServerRegistryData(ResourceLocation registryKey, List<WrapperConfigServerRegistryData.RegistryElement> elements) {
      this((NBTCompound)null, registryKey, elements);
   }

   @ApiStatus.Obsolete
   public WrapperConfigServerRegistryData(@Nullable NBTCompound registryData, @Nullable ResourceLocation registryKey, @Nullable List<WrapperConfigServerRegistryData.RegistryElement> elements) {
      super((PacketTypeCommon)PacketType.Configuration.Server.REGISTRY_DATA);
      this.registryData = registryData;
      this.registryKey = registryKey;
      this.elements = elements;
   }

   public void read() {
      if (this.serverVersion.isOlderThan(ServerVersion.V_1_20_5)) {
         this.registryData = this.readUnlimitedNBT();
      } else {
         this.registryKey = this.readIdentifier();
         this.elements = this.readList((wrapper) -> {
            ResourceLocation id = wrapper.readIdentifier();
            NBT data = (NBT)wrapper.readOptional(PacketWrapper::readNBTRaw);
            return new WrapperConfigServerRegistryData.RegistryElement(id, data);
         });
      }
   }

   public void write() {
      if (this.serverVersion.isOlderThan(ServerVersion.V_1_20_5)) {
         this.writeNBT(this.registryData);
      } else {
         this.writeIdentifier(this.registryKey);
         this.writeList(this.elements, (wrapper, element) -> {
            wrapper.writeIdentifier(element.getId());
            wrapper.writeOptional(element.getData(), PacketWrapper::writeNBTRaw);
         });
      }
   }

   public void copy(WrapperConfigServerRegistryData wrapper) {
      this.registryData = wrapper.registryData;
      this.registryKey = wrapper.registryKey;
      this.elements = wrapper.elements;
   }

   @ApiStatus.Obsolete
   @Nullable
   public NBTCompound getRegistryData() {
      return this.registryData;
   }

   @ApiStatus.Obsolete
   public void setRegistryData(NBTCompound registryData) {
      this.registryData = registryData;
   }

   @Nullable
   public ResourceLocation getRegistryKey() {
      return this.registryKey;
   }

   public void setRegistryKey(ResourceLocation registryKey) {
      this.registryKey = registryKey;
   }

   @Nullable
   public List<WrapperConfigServerRegistryData.RegistryElement> getElements() {
      return this.elements;
   }

   public void setElements(List<WrapperConfigServerRegistryData.RegistryElement> elements) {
      this.elements = elements;
   }

   public static class RegistryElement {
      private final ResourceLocation id;
      @Nullable
      private final NBT data;

      public RegistryElement(NBTCompound nbt) {
         this(new ResourceLocation(nbt.getStringTagValueOrThrow("name")), nbt.getTagOrNull("element"));
      }

      public RegistryElement(ResourceLocation id, @Nullable NBT data) {
         this.id = id;
         this.data = data;
      }

      public static List<WrapperConfigServerRegistryData.RegistryElement> convertNbt(NBTList<NBTCompound> list) {
         List<WrapperConfigServerRegistryData.RegistryElement> elements = new ArrayList(list.size());
         Iterator var2 = list.getTags().iterator();

         while(var2.hasNext()) {
            NBTCompound tag = (NBTCompound)var2.next();
            elements.add(new WrapperConfigServerRegistryData.RegistryElement(tag));
         }

         return Collections.unmodifiableList(elements);
      }

      public ResourceLocation getId() {
         return this.id;
      }

      @Nullable
      public NBT getData() {
         return this.data;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof WrapperConfigServerRegistryData.RegistryElement)) {
            return false;
         } else {
            WrapperConfigServerRegistryData.RegistryElement that = (WrapperConfigServerRegistryData.RegistryElement)obj;
            return !this.id.equals(that.id) ? false : Objects.equals(this.data, that.data);
         }
      }

      public int hashCode() {
         int result = this.id.hashCode();
         result = 31 * result + (this.data != null ? this.data.hashCode() : 0);
         return result;
      }

      public String toString() {
         return "RegistryElement{id=" + this.id + ", data=" + this.data + '}';
      }
   }
}
