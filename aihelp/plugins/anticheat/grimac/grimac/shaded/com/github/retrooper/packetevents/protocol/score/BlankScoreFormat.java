package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.score;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public final class BlankScoreFormat implements ScoreFormat {
   public static final BlankScoreFormat INSTANCE = new BlankScoreFormat();

   private BlankScoreFormat() {
   }

   public static BlankScoreFormat read(PacketWrapper<?> wrapper) {
      return INSTANCE;
   }

   public static void write(PacketWrapper<?> wrapper, BlankScoreFormat format) {
   }

   public Component format(int score) {
      return Component.empty();
   }

   public ScoreFormatType<BlankScoreFormat> getType() {
      return ScoreFormatTypes.BLANK;
   }
}
