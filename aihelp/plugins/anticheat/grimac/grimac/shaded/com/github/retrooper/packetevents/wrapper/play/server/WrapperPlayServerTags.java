package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WrapperPlayServerTags extends PacketWrapper<WrapperPlayServerTags> {
   private Map<ResourceLocation, List<WrapperPlayServerTags.Tag>> tags;

   public WrapperPlayServerTags(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerTags(Map<ResourceLocation, List<WrapperPlayServerTags.Tag>> tags) {
      super((PacketTypeCommon)PacketType.Play.Server.TAGS);
      this.tags = tags;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
         this.tags = this.readMap(PacketWrapper::readIdentifier, (ew) -> {
            return ew.readList(WrapperPlayServerTags.Tag::read);
         });
      } else {
         this.tags = new HashMap(4);
         this.tags.put(ResourceLocation.minecraft("block"), this.readList(WrapperPlayServerTags.Tag::read));
         this.tags.put(ResourceLocation.minecraft("item"), this.readList(WrapperPlayServerTags.Tag::read));
         this.tags.put(ResourceLocation.minecraft("fluid"), this.readList(WrapperPlayServerTags.Tag::read));
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            this.tags.put(ResourceLocation.minecraft("entity_type"), this.readList(WrapperPlayServerTags.Tag::read));
         }
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
         this.writeMap(this.tags, PacketWrapper::writeIdentifier, (ew, tags) -> {
            ew.writeList(tags, WrapperPlayServerTags.Tag::write);
         });
      } else {
         this.writeList((List)this.tags.get(ResourceLocation.minecraft("block")), WrapperPlayServerTags.Tag::write);
         this.writeList((List)this.tags.get(ResourceLocation.minecraft("item")), WrapperPlayServerTags.Tag::write);
         this.writeList((List)this.tags.get(ResourceLocation.minecraft("fluid")), WrapperPlayServerTags.Tag::write);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            this.writeList((List)this.tags.get(ResourceLocation.minecraft("entity_type")), WrapperPlayServerTags.Tag::write);
         }
      }

   }

   public void copy(WrapperPlayServerTags wrapper) {
      this.tags = wrapper.tags;
   }

   public Map<ResourceLocation, List<WrapperPlayServerTags.Tag>> getTagMap() {
      return this.tags;
   }

   public void setTagMap(Map<ResourceLocation, List<WrapperPlayServerTags.Tag>> tags) {
      this.tags = tags;
   }

   /** @deprecated */
   @Deprecated
   public Map<String, List<WrapperPlayServerTags.Tag>> getTags() {
      if (this.tags == null) {
         return null;
      } else {
         Map<String, List<WrapperPlayServerTags.Tag>> tags = new HashMap(this.tags.size());
         Iterator var2 = this.tags.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<ResourceLocation, List<WrapperPlayServerTags.Tag>> entry = (Entry)var2.next();
            tags.put(((ResourceLocation)entry.getKey()).toString(), (List)entry.getValue());
         }

         return Collections.unmodifiableMap(tags);
      }
   }

   /** @deprecated */
   @Deprecated
   public void setTags(HashMap<String, List<WrapperPlayServerTags.Tag>> tags) {
      if (tags == null) {
         this.tags = null;
      } else {
         this.tags = new HashMap(tags.size());
         Iterator var2 = tags.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<String, List<WrapperPlayServerTags.Tag>> entry = (Entry)var2.next();
            this.tags.put(new ResourceLocation((String)entry.getKey()), (List)entry.getValue());
         }
      }

   }

   public static class Tag {
      private ResourceLocation key;
      private List<Integer> values;

      public Tag(String name, List<Integer> values) {
         this(new ResourceLocation(name), values);
      }

      public Tag(ResourceLocation key, List<Integer> values) {
         this.key = key;
         this.values = values;
      }

      public static WrapperPlayServerTags.Tag read(PacketWrapper<?> wrapper) {
         ResourceLocation tagName = wrapper.readIdentifier();
         List<Integer> values = wrapper.readList(PacketWrapper::readVarInt);
         return new WrapperPlayServerTags.Tag(tagName, values);
      }

      public static void write(PacketWrapper<?> wrapper, WrapperPlayServerTags.Tag tag) {
         wrapper.writeIdentifier(tag.key);
         wrapper.writeList(tag.values, PacketWrapper::writeVarInt);
      }

      public String getName() {
         return this.key.toString();
      }

      public void setName(String name) {
         this.key = new ResourceLocation(name);
      }

      public ResourceLocation getKey() {
         return this.key;
      }

      public void setKey(ResourceLocation key) {
         this.key = key;
      }

      public List<Integer> getValues() {
         return this.values;
      }

      public void setValues(List<Integer> values) {
         this.values = values;
      }
   }
}
