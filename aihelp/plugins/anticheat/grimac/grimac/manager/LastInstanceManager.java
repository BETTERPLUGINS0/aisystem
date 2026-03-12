package ac.grim.grimac.manager;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.data.LastInstance;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LastInstanceManager extends Check implements PostPredictionCheck {
   private final List<LastInstance> instances = new ArrayList();

   public LastInstanceManager(GrimPlayer player) {
      super(player);
   }

   public void addInstance(LastInstance instance) {
      this.instances.add(instance);
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      Iterator var2 = this.instances.iterator();

      while(var2.hasNext()) {
         LastInstance instance = (LastInstance)var2.next();
         instance.tick();
      }

   }
}
