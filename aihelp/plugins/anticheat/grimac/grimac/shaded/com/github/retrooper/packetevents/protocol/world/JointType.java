package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world;

import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Arrays;
import java.util.Optional;

public enum JointType {
   ROLLABLE("rollable"),
   ALIGNED("aligned");

   private static final JointType[] VALUES = values();
   private final String name;

   private JointType(String name) {
      this.name = name;
   }

   public String getSerializedName() {
      return this.name;
   }

   public Component getTranslatableName() {
      return Component.translatable("jigsaw_block.joint." + this.name);
   }

   public static Optional<JointType> byName(String name) {
      return Arrays.stream(VALUES).filter((jointType) -> {
         return jointType.getSerializedName().equals(name);
      }).findFirst();
   }

   // $FF: synthetic method
   private static JointType[] $values() {
      return new JointType[]{ROLLABLE, ALIGNED};
   }
}
