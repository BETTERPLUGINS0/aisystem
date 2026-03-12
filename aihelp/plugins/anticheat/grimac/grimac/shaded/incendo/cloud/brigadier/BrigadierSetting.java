package ac.grim.grimac.shaded.incendo.cloud.brigadier;

import ac.grim.grimac.shaded.incendo.cloud.setting.Setting;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(
   status = Status.STABLE,
   since = "2.0.0"
)
public enum BrigadierSetting implements Setting {
   FORCE_EXECUTABLE;

   // $FF: synthetic method
   private static BrigadierSetting[] $values() {
      return new BrigadierSetting[]{FORCE_EXECUTABLE};
   }
}
