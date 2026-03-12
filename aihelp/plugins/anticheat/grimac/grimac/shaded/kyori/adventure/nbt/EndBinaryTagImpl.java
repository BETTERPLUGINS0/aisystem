package ac.grim.grimac.shaded.kyori.adventure.nbt;

final class EndBinaryTagImpl extends AbstractBinaryTag implements EndBinaryTag {
   static final EndBinaryTagImpl INSTANCE = new EndBinaryTagImpl();

   public boolean equals(final Object that) {
      return this == that;
   }

   public int hashCode() {
      return 0;
   }
}
