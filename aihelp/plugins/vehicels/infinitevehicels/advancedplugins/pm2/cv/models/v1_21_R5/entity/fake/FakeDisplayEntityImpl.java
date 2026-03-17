package advancedplugins.pm2.cv.models.v1_21_R5.entity.fake;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntity;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class FakeDisplayEntityImpl implements FakeDisplayEntity {
   private final UUID uuid = UUID.randomUUID();
   protected final Location initialSpawnLocation;

   public FakeDisplayEntityImpl(@NotNull Location var1) {
      this.initialSpawnLocation = var1;
   }

   public void spawnInternal(Player var1) {
      ClientboundAddEntityPacket var2 = new ClientboundAddEntityPacket(this.getEntityId(), this.uuid, this.initialSpawnLocation.getX(), this.initialSpawnLocation.getY(), this.initialSpawnLocation.getZ(), this.initialSpawnLocation.getYaw(), this.initialSpawnLocation.getPitch(), this.getEntityType(), 0, Vec3.ZERO, 0.0D);
      this.sendPacket(var1, var2);
   }

   protected abstract int getEntityId();

   protected abstract EntityType<?> getEntityType();

   protected final void sendPacket(Player var1, Packet<?> var2) {
      CraftPlayer var3 = (CraftPlayer)var1;
      var3.getHandle().connection.send(var2);
   }

   public void destroyInternal(Player var1) {
      ClientboundRemoveEntitiesPacket var2 = new ClientboundRemoveEntitiesPacket(new int[]{this.getEntityId()});
      this.sendPacket(var1, var2);
   }

   protected List<Player> getOnlineViewers() {
      return this.getViewers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).toList();
   }

   protected ServerPlayer getNmsPlayer(Player var1) {
      return ((CraftPlayer)var1).getHandle();
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
