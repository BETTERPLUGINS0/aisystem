package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class RecipeDisplayId {
   private final int id;

   public RecipeDisplayId(int id) {
      this.id = id;
   }

   public static RecipeDisplayId read(PacketWrapper<?> wrapper) {
      int id = wrapper.readVarInt();
      return new RecipeDisplayId(id);
   }

   public static void write(PacketWrapper<?> wrapper, RecipeDisplayId id) {
      wrapper.writeVarInt(id.id);
   }

   public int getId() {
      return this.id;
   }
}
