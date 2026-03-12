package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.advancements;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public final class AdvancementProgress {
   private Map<String, AdvancementProgress.CriterionProgress> criteria;

   public AdvancementProgress(Map<String, AdvancementProgress.CriterionProgress> criteria) {
      this.criteria = criteria;
   }

   public static AdvancementProgress read(PacketWrapper<?> wrapper) {
      Map<String, AdvancementProgress.CriterionProgress> criteria = wrapper.readMap(PacketWrapper::readString, AdvancementProgress.CriterionProgress::read);
      return new AdvancementProgress(criteria);
   }

   public static void write(PacketWrapper<?> wrapper, AdvancementProgress progress) {
      wrapper.writeMap(progress.getCriteria(), PacketWrapper::writeString, AdvancementProgress.CriterionProgress::write);
   }

   public Map<String, AdvancementProgress.CriterionProgress> getCriteria() {
      return this.criteria;
   }

   public void setCriteria(Map<String, AdvancementProgress.CriterionProgress> criteria) {
      this.criteria = criteria;
   }

   public static final class CriterionProgress {
      @Nullable
      private Long obtainedTimestamp;

      public CriterionProgress(@Nullable Long obtainedTimestamp) {
         this.obtainedTimestamp = obtainedTimestamp;
      }

      public static AdvancementProgress.CriterionProgress read(PacketWrapper<?> wrapper) {
         return new AdvancementProgress.CriterionProgress((Long)wrapper.readOptional(PacketWrapper::readLong));
      }

      public static void write(PacketWrapper<?> wrapper, AdvancementProgress.CriterionProgress progress) {
         wrapper.writeOptional(progress.obtainedTimestamp, PacketWrapper::writeLong);
      }

      @Nullable
      public Long getObtainedTimestamp() {
         return this.obtainedTimestamp;
      }

      public void setObtainedTimestamp(@Nullable Long obtainedTimestamp) {
         this.obtainedTimestamp = obtainedTimestamp;
      }
   }
}
