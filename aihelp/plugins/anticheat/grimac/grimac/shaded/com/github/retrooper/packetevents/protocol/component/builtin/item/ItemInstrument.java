package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument.Instrument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument.Instruments;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemInstrument {
   private MaybeMappedEntity<Instrument> instrument;

   public ItemInstrument(MaybeMappedEntity<Instrument> instrument) {
      this.instrument = instrument;
   }

   public static ItemInstrument read(PacketWrapper<?> wrapper) {
      MaybeMappedEntity instrument;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         instrument = MaybeMappedEntity.read(wrapper, Instruments.getRegistry(), Instrument::read);
      } else {
         instrument = new MaybeMappedEntity(Instrument.read(wrapper));
      }

      return new ItemInstrument(instrument);
   }

   public static void write(PacketWrapper<?> wrapper, ItemInstrument instrument) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         MaybeMappedEntity.write(wrapper, instrument.instrument, Instrument::write);
      } else {
         Instrument.write(wrapper, (Instrument)instrument.instrument.getValueOrThrow());
      }

   }

   public MaybeMappedEntity<Instrument> getInstrument() {
      return this.instrument;
   }

   public void setInstrument(MaybeMappedEntity<Instrument> instrument) {
      this.instrument = instrument;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ItemInstrument)) {
         return false;
      } else {
         ItemInstrument that = (ItemInstrument)obj;
         return this.instrument.equals(that.instrument);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.instrument);
   }
}
