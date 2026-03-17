package advancedplugins.pm2.cv.models.v1_20_R1.entity.fake;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntity;
import advancedplugins.pm2.cv.models.v1_20_R1.entity.EntityHandlerImpl;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class FakeDisplayEntityImpl implements FakeDisplayEntity {
   private final int entityId;
   private final UUID uuid = UUID.randomUUID();
   protected final Location initialSpawnLocation;

   public FakeDisplayEntityImpl(@NotNull Location initialSpawnLocation) {
      this.initialSpawnLocation = var1;
      this.entityId = EntityHandlerImpl.ENTITY_COUNTER.incrementAndGet();
   }

   public void spawnInternal(Player forPlayer) {
      PacketPlayOutSpawnEntity var2 = new PacketPlayOutSpawnEntity(this.entityId, this.uuid, var1.getLocation().getX(), var1.getLocation().getY(), var1.getLocation().getZ(), var1.getLocation().getYaw(), var1.getLocation().getPitch(), this.getEntityType(), 0, Vec3D.b, 0.0D);
      this.sendPacket(var1, var2);
   }

   protected abstract EntityTypes<?> getEntityType();

   protected final void sendPacket(Player player, Packet<?> packet) {
      CraftPlayer var3 = (CraftPlayer)var1;
      var3.getHandle().c.a(var2);
   }

   public void destroyInternal(Player viewer) {
      PacketPlayOutEntityDestroy var2 = new PacketPlayOutEntityDestroy(new int[]{this.entityId});
      this.sendPacket(var1, var2);
   }

   protected List<Player> getOnlineViewers() {
      return this.getViewers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).toList();
   }

   @Generated
   public int getEntityId() {
      return this.entityId;
   }

   @Generated
   public UUID getUuid() {
      return this.uuid;
   }

   @Generated
   public Location getInitialSpawnLocation() {
      return this.initialSpawnLocation;
   }
}
