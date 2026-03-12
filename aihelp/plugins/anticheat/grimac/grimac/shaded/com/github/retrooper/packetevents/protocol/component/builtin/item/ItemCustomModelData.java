package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.ArrayList;
import java.util.List;

public class ItemCustomModelData {
   private List<Float> floats;
   private List<Boolean> flags;
   private List<String> strings;
   private List<Color> colors;

   public ItemCustomModelData(List<Float> floats, List<Boolean> flags, List<String> strings, List<Color> colors) {
      this.floats = floats;
      this.flags = flags;
      this.strings = strings;
      this.colors = colors;
   }

   public ItemCustomModelData(int legacyId) {
      this.floats = new ArrayList(1);
      this.flags = new ArrayList(0);
      this.strings = new ArrayList(0);
      this.colors = new ArrayList(0);
      this.setLegacyId(legacyId);
   }

   public static ItemCustomModelData read(PacketWrapper<?> wrapper) {
      return wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4) ? new ItemCustomModelData(wrapper.readList(PacketWrapper::readFloat), wrapper.readList(PacketWrapper::readBoolean), wrapper.readList(PacketWrapper::readString), wrapper.readList(Color::read)) : new ItemCustomModelData(wrapper.readVarInt());
   }

   public static void write(PacketWrapper<?> wrapper, ItemCustomModelData data) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
         wrapper.writeList(data.floats, PacketWrapper::writeFloat);
         wrapper.writeList(data.flags, PacketWrapper::writeBoolean);
         wrapper.writeList(data.strings, PacketWrapper::writeString);
         wrapper.writeList(data.colors, Color::write);
      } else {
         wrapper.writeVarInt(data.getLegacyId());
      }

   }

   public List<Float> getFloats() {
      return this.floats;
   }

   public void setFloats(List<Float> floats) {
      this.floats = floats;
   }

   public List<Boolean> getFlags() {
      return this.flags;
   }

   public void setFlags(List<Boolean> flags) {
      this.flags = flags;
   }

   public List<String> getStrings() {
      return this.strings;
   }

   public void setStrings(List<String> strings) {
      this.strings = strings;
   }

   public List<Color> getColors() {
      return this.colors;
   }

   public void setColors(List<Color> colors) {
      this.colors = colors;
   }

   @ApiStatus.Obsolete
   public int getLegacyId() {
      return !this.floats.isEmpty() ? ((Float)this.floats.get(0)).intValue() : 0;
   }

   @ApiStatus.Obsolete
   public void setLegacyId(int legacyId) {
      if (this.flags.isEmpty()) {
         this.floats.add((float)legacyId);
      } else {
         this.floats.set(0, (float)legacyId);
      }

   }
}
