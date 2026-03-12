package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticTimeline extends AbstractMappedEntity implements Timeline {
   @Nullable
   private final Integer periodTicks;
   private final Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> tracks;

   public StaticTimeline(@Nullable Integer periodTicks, Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> tracks) {
      this((TypesBuilderData)null, periodTicks, tracks);
   }

   @ApiStatus.Internal
   public StaticTimeline(@Nullable TypesBuilderData data, @Nullable Integer periodTicks, Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> tracks) {
      super(data);
      this.periodTicks = periodTicks;
      this.tracks = tracks;
   }

   public Timeline copy(@Nullable TypesBuilderData newData) {
      return new StaticTimeline(newData, this.periodTicks, this.tracks);
   }

   @Nullable
   public Integer getPeriodTicks() {
      return this.periodTicks;
   }

   public Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> getTracks() {
      return this.tracks;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         StaticTimeline that = (StaticTimeline)obj;
         return !Objects.equals(this.periodTicks, that.periodTicks) ? false : this.tracks.equals(that.tracks);
      } else {
         return false;
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.periodTicks, this.tracks});
   }
}
