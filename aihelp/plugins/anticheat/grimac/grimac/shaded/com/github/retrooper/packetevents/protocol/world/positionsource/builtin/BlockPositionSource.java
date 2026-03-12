package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.PositionSource;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.PositionSourceTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BlockPositionSource extends PositionSource {
   private Vector3i pos;

   public BlockPositionSource(Vector3i pos) {
      super(PositionSourceTypes.BLOCK);
      this.pos = pos;
   }

   public static BlockPositionSource read(PacketWrapper<?> wrapper) {
      return new BlockPositionSource(wrapper.readBlockPosition());
   }

   public static void write(PacketWrapper<?> wrapper, BlockPositionSource source) {
      wrapper.writeBlockPosition(source.pos);
   }

   public static BlockPositionSource decodeSource(NBTCompound compound, ClientVersion version) {
      NBTIntArray arr = (NBTIntArray)compound.getTagOfTypeOrThrow("pos", NBTIntArray.class);
      return new BlockPositionSource(new Vector3i(arr.getValue()));
   }

   public static void encodeSource(BlockPositionSource source, ClientVersion version, NBTCompound compound) {
      compound.setTag("pos", new NBTIntArray(new int[]{source.pos.x, source.pos.y, source.pos.z}));
   }

   public Vector3i getPos() {
      return this.pos;
   }

   public void setPos(Vector3i pos) {
      this.pos = pos;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         BlockPositionSource that = (BlockPositionSource)obj;
         return this.pos.equals(that.pos);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.pos);
   }
}
