package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Collections;
import java.util.Map;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Timeline extends MappedEntity, CopyableEntity<Timeline>, DeepComparableEntity {
   NbtCodec<Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>>> TRACK_CODEC = NbtMapCodec.codecOfMap(NbtCodecs.forRegistry(EnvironmentAttributes.getRegistry()), TimelineTrack::codec).codec();
   NbtCodec<Timeline> CODEC = (new NbtMapCodec<Timeline>() {
      public Timeline decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
         Integer periodTicks = (Integer)compound.getOrNull("period_ticks", NbtCodecs.INT, wrapper);
         Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> tracks = (Map)compound.getOr("tracks", Timeline.TRACK_CODEC, Collections.emptyMap(), wrapper);
         return new StaticTimeline(periodTicks, tracks);
      }

      public void encode(NBTCompound compound, PacketWrapper<?> wrapper, Timeline value) throws NbtCodecException {
         Integer periodTicks = value.getPeriodTicks();
         if (periodTicks != null) {
            compound.setTag("period_ticks", new NBTInt(periodTicks));
         }

         Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> tracks = value.getTracks();
         if (!tracks.isEmpty()) {
            compound.set("tracks", tracks, Timeline.TRACK_CODEC, wrapper);
         }

      }
   }).codec();

   @Nullable
   Integer getPeriodTicks();

   Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> getTracks();
}
