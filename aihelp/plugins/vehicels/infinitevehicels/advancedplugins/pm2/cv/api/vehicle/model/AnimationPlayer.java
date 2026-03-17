package advancedplugins.pm2.cv.api.vehicle.model;

public interface AnimationPlayer {
   void start();

   void stop();

   void playFrame(int var1);

   void updateProgress(int var1, float var2);
}
