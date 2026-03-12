package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound;

public enum SoundCategory {
   MASTER,
   MUSIC,
   RECORD,
   WEATHER,
   BLOCK,
   HOSTILE,
   NEUTRAL,
   PLAYER,
   AMBIENT,
   VOICE,
   UI;

   private static final SoundCategory[] VALUES = values();

   public static SoundCategory fromId(int id) {
      return VALUES[id];
   }

   // $FF: synthetic method
   private static SoundCategory[] $values() {
      return new SoundCategory[]{MASTER, MUSIC, RECORD, WEATHER, BLOCK, HOSTILE, NEUTRAL, PLAYER, AMBIENT, VOICE, UI};
   }
}
