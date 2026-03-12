package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.advancements;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

public class Advancement {
   @Nullable
   private ResourceLocation parent;
   @Nullable
   private AdvancementDisplay display;
   private List<String> criteria;
   private List<List<String>> requirements;
   private boolean sendsTelemetryData;

   public Advancement(@Nullable ResourceLocation parent, @Nullable AdvancementDisplay display, List<List<String>> requirements, boolean sendsTelemetryData) {
      this(parent, display, Collections.emptyList(), requirements, sendsTelemetryData);
   }

   @ApiStatus.Obsolete
   public Advancement(@Nullable ResourceLocation parent, @Nullable AdvancementDisplay display, List<String> criteria, List<List<String>> requirements, boolean sendsTelemetryData) {
      this.parent = parent;
      this.display = display;
      this.criteria = criteria;
      this.requirements = requirements;
      this.sendsTelemetryData = sendsTelemetryData;
   }

   public static Advancement read(PacketWrapper<?> wrapper) {
      ResourceLocation parentId = (ResourceLocation)wrapper.readOptional(ResourceLocation::read);
      AdvancementDisplay display = (AdvancementDisplay)wrapper.readOptional(AdvancementDisplay::read);
      List<String> criteria = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_2) ? wrapper.readList(PacketWrapper::readString) : null;
      List<List<String>> requirements = wrapper.readList((ew) -> {
         return wrapper.readList(PacketWrapper::readString);
      });
      boolean sendsTelemetryData = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20) && wrapper.readBoolean();
      return new Advancement(parentId, display, criteria, requirements, sendsTelemetryData);
   }

   public static void write(PacketWrapper<?> wrapper, Advancement advancement) {
      wrapper.writeOptional(advancement.parent, ResourceLocation::write);
      wrapper.writeOptional(advancement.display, AdvancementDisplay::write);
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_2)) {
         wrapper.writeList(advancement.criteria, PacketWrapper::writeString);
      }

      wrapper.writeList(advancement.getRequirements(), (ew, anyList) -> {
         ew.writeList(anyList, PacketWrapper::writeString);
      });
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20)) {
         wrapper.writeBoolean(advancement.sendsTelemetryData);
      }

   }

   @Nullable
   public ResourceLocation getParent() {
      return this.parent;
   }

   public void setParent(@Nullable ResourceLocation parent) {
      this.parent = parent;
   }

   @Nullable
   public AdvancementDisplay getDisplay() {
      return this.display;
   }

   public void setDisplay(@Nullable AdvancementDisplay display) {
      this.display = display;
   }

   public List<String> getCriteria() {
      return this.criteria;
   }

   public void setCriteria(List<String> criteria) {
      this.criteria = criteria;
   }

   public List<List<String>> getRequirements() {
      return this.requirements;
   }

   public void setRequirements(List<List<String>> requirements) {
      this.requirements = requirements;
   }

   public boolean isSendsTelemetryData() {
      return this.sendsTelemetryData;
   }

   public void setSendsTelemetryData(boolean sendsTelemetryData) {
      this.sendsTelemetryData = sendsTelemetryData;
   }
}
