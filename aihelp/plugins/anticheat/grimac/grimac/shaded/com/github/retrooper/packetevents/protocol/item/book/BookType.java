package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.book;

public enum BookType {
   CRAFTING,
   FURNACE,
   BLAST_FURNACE,
   SMOKER;

   private static final BookType[] VALUES = values();

   public int getId() {
      return this.ordinal();
   }

   public static BookType getById(int id) {
      return VALUES[id];
   }

   // $FF: synthetic method
   private static BookType[] $values() {
      return new BookType[]{CRAFTING, FURNACE, BLAST_FURNACE, SMOKER};
   }
}
