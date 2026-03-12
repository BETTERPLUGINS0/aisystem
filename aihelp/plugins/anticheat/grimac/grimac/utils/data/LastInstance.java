package ac.grim.grimac.utils.data;

import ac.grim.grimac.player.GrimPlayer;

public class LastInstance {
   private int lastInstance = 100;

   public LastInstance(GrimPlayer player) {
      player.lastInstanceManager.addInstance(this);
   }

   public boolean hasOccurredSince(int time) {
      return this.lastInstance <= time;
   }

   public void reset() {
      this.lastInstance = 0;
   }

   public void tick() {
      if (this.lastInstance == Integer.MAX_VALUE) {
         this.lastInstance = 100;
      }

      ++this.lastInstance;
   }
}
