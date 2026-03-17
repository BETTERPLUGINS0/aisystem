package advancedplugins.pm2.cv.models.api.model.nrpc.nms;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public interface FakeDisplayEntity {
   Location getInitialSpawnLocation();

   List<UUID> getViewers();

   default void spawn(Player forPlayer) {
      this.getViewers().add(forPlayer.getUniqueId());
      this.spawnInternal(forPlayer);
   }

   void setTransformation(Transformation var1);

   void positionSync(double var1, double var3, double var5, float var7, float var8);

   Vector3f getPosition();

   float getYaw();

   float getPitch();

   default void positionSync(Vector3f position) {
      this.positionSync((double)position.x, (double)position.y, (double)position.z, this.getYaw(), this.getPitch());
   }

   default void positionSync(Vector3f position, float yaw, float pitch) {
      this.positionSync((double)position.x, (double)position.y, (double)position.z, yaw, pitch);
   }

   default void positionSync(double x, double y, double z) {
      this.positionSync(x, y, z, this.getYaw(), this.getPitch());
   }

   default void rotationSync(float yaw, float pitch) {
      this.positionSync(this.getPosition(), yaw, pitch);
   }

   default void rotationSync(float yaw) {
      this.positionSync(this.getPosition(), yaw, this.getPitch());
   }

   default void rotationSync() {
      this.positionSync(this.getPosition(), this.getYaw(), this.getPitch());
   }

   void spawnInternal(Player var1);

   default void despawn(Player forPlayer) {
      this.getViewers().remove(forPlayer.getUniqueId());
      this.destroyInternal(Bukkit.getPlayer(forPlayer.getUniqueId()));
   }

   default void destroy() {
      Iterator var1 = this.getViewers().iterator();

      while(var1.hasNext()) {
         UUID viewer = (UUID)var1.next();
         this.destroyInternal(Bukkit.getPlayer(viewer));
      }

   }

   void destroyInternal(Player var1);

   default void refresh() {
      Iterator var1 = this.getViewers().iterator();

      while(var1.hasNext()) {
         UUID viewer = (UUID)var1.next();
         this.destroyInternal(Bukkit.getPlayer(viewer));
         this.spawnInternal(Bukkit.getPlayer(viewer));
      }

   }

   default void packAndSend(Player player) {
      throw new UnsupportedOperationException("Not implemented! Please implement this in " + this.getClass().getName());
   }
}
