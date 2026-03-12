package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

public enum ChatCompletionAction {
   ADD,
   REMOVE,
   SET;

   private static final ChatCompletionAction[] VALUES = values();

   public static ChatCompletionAction fromId(int id) {
      return VALUES[id];
   }

   // $FF: synthetic method
   private static ChatCompletionAction[] $values() {
      return new ChatCompletionAction[]{ADD, REMOVE, SET};
   }
}
