package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.score;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public final class FixedScoreFormat implements ScoreFormat {
   private final Component value;

   public FixedScoreFormat(Component value) {
      this.value = value;
   }

   public static FixedScoreFormat read(PacketWrapper<?> wrapper) {
      return new FixedScoreFormat(wrapper.readComponent());
   }

   public static void write(PacketWrapper<?> wrapper, FixedScoreFormat format) {
      wrapper.writeComponent(format.value);
   }

   public Component format(int score) {
      return this.value;
   }

   public ScoreFormatType<FixedScoreFormat> getType() {
      return ScoreFormatTypes.FIXED;
   }

   public Component getValue() {
      return this.value;
   }
}
