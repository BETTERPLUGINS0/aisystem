package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.score;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;

@ApiStatus.NonExtendable
public interface ScoreFormat {
   static BlankScoreFormat blankScore() {
      return BlankScoreFormat.INSTANCE;
   }

   static StyledScoreFormat styledScore(Style style) {
      return new StyledScoreFormat(style);
   }

   static FixedScoreFormat fixedScore(Component value) {
      return new FixedScoreFormat(value);
   }

   static ScoreFormat readTyped(PacketWrapper<?> wrapper) {
      return ((ScoreFormatType)wrapper.readMappedEntity((IRegistry)ScoreFormatTypes.getRegistry())).read(wrapper);
   }

   static <T extends ScoreFormat> void writeTyped(PacketWrapper<?> wrapper, T format) {
      wrapper.writeMappedEntity(format.getType());
      format.getType().write(wrapper, format);
   }

   Component format(int score);

   ScoreFormatType<?> getType();
}
