package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class ItemFireworks {
   private int flightDuration;
   private List<FireworkExplosion> explosions;

   public ItemFireworks(int flightDuration, List<FireworkExplosion> explosions) {
      this.flightDuration = flightDuration;
      this.explosions = explosions;
   }

   public static ItemFireworks read(PacketWrapper<?> wrapper) {
      int flightDuration = wrapper.readVarInt();
      List<FireworkExplosion> explosions = wrapper.readList(FireworkExplosion::read);
      return new ItemFireworks(flightDuration, explosions);
   }

   public static void write(PacketWrapper<?> wrapper, ItemFireworks fireworks) {
      wrapper.writeVarInt(fireworks.flightDuration);
      wrapper.writeList(fireworks.explosions, FireworkExplosion::write);
   }

   public int getFlightDuration() {
      return this.flightDuration;
   }

   public void setFlightDuration(int flightDuration) {
      this.flightDuration = flightDuration;
   }

   public void addExplosion(FireworkExplosion explosion) {
      this.explosions.add(explosion);
   }

   public List<FireworkExplosion> getExplosions() {
      return this.explosions;
   }

   public void setExplosions(List<FireworkExplosion> explosions) {
      this.explosions = explosions;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemFireworks)) {
         return false;
      } else {
         ItemFireworks that = (ItemFireworks)obj;
         return this.flightDuration != that.flightDuration ? false : this.explosions.equals(that.explosions);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.flightDuration, this.explosions});
   }
}
