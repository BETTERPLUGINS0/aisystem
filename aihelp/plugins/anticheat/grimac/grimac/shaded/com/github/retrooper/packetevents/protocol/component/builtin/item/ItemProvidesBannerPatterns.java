package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemProvidesBannerPatterns {
   private ResourceLocation tagKey;

   public ItemProvidesBannerPatterns(ResourceLocation tagKey) {
      this.tagKey = tagKey;
   }

   public static ItemProvidesBannerPatterns read(PacketWrapper<?> wrapper) {
      ResourceLocation tagKey = wrapper.readIdentifier();
      return new ItemProvidesBannerPatterns(tagKey);
   }

   public static void write(PacketWrapper<?> wrapper, ItemProvidesBannerPatterns patterns) {
      wrapper.writeIdentifier(patterns.tagKey);
   }

   public ResourceLocation getTagKey() {
      return this.tagKey;
   }

   public void setTagKey(ResourceLocation tagKey) {
      this.tagKey = tagKey;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ItemProvidesBannerPatterns)) {
         return false;
      } else {
         ItemProvidesBannerPatterns that = (ItemProvidesBannerPatterns)obj;
         return this.tagKey.equals(that.tagKey);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.tagKey);
   }
}
