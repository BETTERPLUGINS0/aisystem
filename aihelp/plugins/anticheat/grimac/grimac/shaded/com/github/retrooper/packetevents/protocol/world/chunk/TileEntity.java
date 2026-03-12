package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;

public class TileEntity {
   byte packedByte;
   short y;
   int type;
   NBTCompound data;

   public TileEntity(final NBTCompound data) {
      this.data = data;
   }

   public TileEntity(final byte packedByte, final short y, final int type, final NBTCompound data) {
      this.packedByte = packedByte;
      this.y = y;
      this.type = type;
      this.data = data;
   }

   public int getX() {
      return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18) ? (this.packedByte & 240) >> 4 : ((NBTInt)this.data.getTagOfTypeOrNull("x", NBTInt.class)).getAsInt();
   }

   public int getZ() {
      return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18) ? this.packedByte & 15 : ((NBTInt)this.data.getTagOfTypeOrNull("z", NBTInt.class)).getAsInt();
   }

   public int getY() {
      return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18) ? this.y : ((NBTInt)this.data.getTagOfTypeOrNull("y", NBTInt.class)).getAsInt();
   }

   public void setX(final int x) {
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
         this.packedByte = (byte)(this.packedByte & 15 | (x & 15) << 4);
      } else {
         this.data.setTag("x", new NBTInt(x));
      }

   }

   public void setY(final int y) {
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
         this.y = (short)y;
      } else {
         this.data.setTag("y", new NBTInt(y));
      }

   }

   public void setZ(final int z) {
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
         this.packedByte = (byte)(this.packedByte & 240 | z & 15);
      } else {
         this.data.setTag("z", new NBTInt(z));
      }

   }

   public int getType() {
      return this.type;
   }

   public byte getPackedByte() {
      return this.packedByte;
   }

   public short getYShort() {
      return this.y;
   }

   public NBTCompound getNBT() {
      return this.data;
   }
}
