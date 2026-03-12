package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.advancements;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class AdvancementHolder {
   private ResourceLocation identifier;
   private Advancement advancement;

   public AdvancementHolder(ResourceLocation identifier, Advancement advancement) {
      this.identifier = identifier;
      this.advancement = advancement;
   }

   public static AdvancementHolder read(PacketWrapper<?> wrapper) {
      ResourceLocation identifier = wrapper.readIdentifier();
      Advancement advancement = Advancement.read(wrapper);
      return new AdvancementHolder(identifier, advancement);
   }

   public static void write(PacketWrapper<?> wrapper, AdvancementHolder holder) {
      wrapper.writeIdentifier(holder.identifier);
      Advancement.write(wrapper, holder.advancement);
   }

   public ResourceLocation getIdentifier() {
      return this.identifier;
   }

   public void setIdentifier(ResourceLocation identifier) {
      this.identifier = identifier;
   }

   public Advancement getAdvancement() {
      return this.advancement;
   }

   public void setAdvancement(Advancement advancement) {
      this.advancement = advancement;
   }
}
