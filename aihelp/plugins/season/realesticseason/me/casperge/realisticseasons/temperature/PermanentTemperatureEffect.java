package me.casperge.realisticseasons.temperature;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.casperge.realisticseasons.api.TemperatureEffect;

public class PermanentTemperatureEffect implements TemperatureEffect {
   private TempData tempdata;
   private int modifier;
   private UUID uuid;
   private int ID;

   public PermanentTemperatureEffect(TempData var1, UUID var2, int var3) {
      this.tempdata = var1;
      this.modifier = var3;
      this.uuid = var2;
      this.ID = (new Random()).nextInt(Integer.MAX_VALUE);
   }

   public void cancel() {
      if (this.tempdata.activePermanentEffects.containsKey(this.uuid)) {
         for(int var1 = 0; var1 < ((List)this.tempdata.activePermanentEffects.get(this.uuid)).size(); ++var1) {
            if (((PermanentTemperatureEffect)((List)this.tempdata.activePermanentEffects.get(this.uuid)).get(var1)).getID() == this.ID) {
               ((List)this.tempdata.activePermanentEffects.get(this.uuid)).remove(var1);
               break;
            }
         }
      }

   }

   public int getID() {
      return this.ID;
   }

   public int getModifier() {
      return this.modifier;
   }
}
