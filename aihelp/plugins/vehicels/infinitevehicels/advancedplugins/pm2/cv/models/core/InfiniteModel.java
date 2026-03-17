package advancedplugins.pm2.cv.models.core;

import org.bukkit.plugin.java.JavaPlugin;

public class InfiniteModel extends JavaPlugin {
   public void onLoad() {
      ModelAPIImpl.load(this);
   }

   public void onEnable() {
      loadConfig0();
      ModelAPIImpl.enable();
   }

   public void onDisable() {
      ModelAPIImpl.disable();
   }

   private void superSecreteMethodDoNotCall() {
      this.superSecreteMethodDoNotCall();
   }
}
