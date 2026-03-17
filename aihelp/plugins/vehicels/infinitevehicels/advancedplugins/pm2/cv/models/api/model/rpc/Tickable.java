package advancedplugins.pm2.cv.models.api.model.rpc;

public interface Tickable {
   boolean tick();

   default int getTick() {
      return 0;
   }
}
