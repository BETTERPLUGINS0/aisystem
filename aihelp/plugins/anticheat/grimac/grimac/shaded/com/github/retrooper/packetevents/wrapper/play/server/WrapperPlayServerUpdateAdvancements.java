package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.advancements.AdvancementHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.advancements.AdvancementProgress;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WrapperPlayServerUpdateAdvancements extends PacketWrapper<WrapperPlayServerUpdateAdvancements> {
   private boolean reset;
   private List<AdvancementHolder> addedAdvancements;
   private Set<ResourceLocation> removedAdvancements;
   private Map<ResourceLocation, AdvancementProgress> progress;
   private boolean showAdvancements;

   public WrapperPlayServerUpdateAdvancements(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerUpdateAdvancements(boolean reset, List<AdvancementHolder> addedAdvancements, Set<ResourceLocation> removedAdvancements, Map<ResourceLocation, AdvancementProgress> progress, boolean showAdvancements) {
      super((PacketTypeCommon)PacketType.Play.Server.UPDATE_ADVANCEMENTS);
      this.reset = reset;
      this.addedAdvancements = addedAdvancements;
      this.removedAdvancements = removedAdvancements;
      this.progress = progress;
      this.showAdvancements = showAdvancements;
   }

   public void read() {
      this.reset = this.readBoolean();
      this.addedAdvancements = this.readList(AdvancementHolder::read);
      this.removedAdvancements = (Set)this.readCollection(LinkedHashSet::new, ResourceLocation::read);
      this.progress = this.readMap(ResourceLocation::read, AdvancementProgress::read);
      this.showAdvancements = this.serverVersion.isOlderThan(ServerVersion.V_1_21_5) || this.readBoolean();
   }

   public void write() {
      this.writeBoolean(this.reset);
      this.writeList(this.addedAdvancements, AdvancementHolder::write);
      this.writeCollection(this.removedAdvancements, ResourceLocation::write);
      this.writeMap(this.progress, ResourceLocation::write, AdvancementProgress::write);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         this.writeBoolean(this.showAdvancements);
      }

   }

   public void copy(WrapperPlayServerUpdateAdvancements wrapper) {
      this.reset = wrapper.reset;
      this.addedAdvancements = wrapper.addedAdvancements;
      this.removedAdvancements = wrapper.removedAdvancements;
      this.progress = wrapper.progress;
      this.showAdvancements = wrapper.showAdvancements;
   }

   public boolean isReset() {
      return this.reset;
   }

   public void setReset(boolean reset) {
      this.reset = reset;
   }

   public List<AdvancementHolder> getAddedAdvancements() {
      return this.addedAdvancements;
   }

   public void setAddedAdvancements(List<AdvancementHolder> addedAdvancements) {
      this.addedAdvancements = addedAdvancements;
   }

   public Set<ResourceLocation> getRemovedAdvancements() {
      return this.removedAdvancements;
   }

   public void setRemovedAdvancements(Set<ResourceLocation> removedAdvancements) {
      this.removedAdvancements = removedAdvancements;
   }

   public Map<ResourceLocation, AdvancementProgress> getProgress() {
      return this.progress;
   }

   public void setProgress(Map<ResourceLocation, AdvancementProgress> progress) {
      this.progress = progress;
   }

   public boolean isShowAdvancements() {
      return this.showAdvancements;
   }

   public void setShowAdvancements(boolean showAdvancements) {
      this.showAdvancements = showAdvancements;
   }
}
