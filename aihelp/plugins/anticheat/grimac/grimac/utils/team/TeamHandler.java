package ac.grim.grimac.utils.team;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import ac.grim.grimac.shaded.fastutil.objects.Object2ObjectOpenHashMap;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import java.util.Map;
import java.util.UUID;
import lombok.Generated;

public class TeamHandler extends Check implements PacketCheck {
   private final Map<String, EntityTeam> entityTeams = new Object2ObjectOpenHashMap();
   private final Map<String, EntityTeam> entityToTeam = new Object2ObjectOpenHashMap();
   @Nullable
   private EntityTeam playerTeam = null;

   public TeamHandler(GrimPlayer player) {
      super(player);
   }

   public void addEntityToTeam(String entityTeamRepresentation, EntityTeam team) {
      this.entityToTeam.put(entityTeamRepresentation, team);
   }

   public void removeEntityFromTeam(String entityTeamRepresentation) {
      this.entityToTeam.remove(entityTeamRepresentation);
   }

   public EntityTeam getEntityTeam(PacketEntity entity) {
      UUID uuid = entity.getUuid();
      return uuid == null ? null : (EntityTeam)this.entityToTeam.get(uuid.toString());
   }

   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.TEAMS) {
         WrapperPlayServerTeams teams = new WrapperPlayServerTeams(event);
         String teamName = teams.getTeamName();
         this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
            EntityTeam var10000;
            switch(teams.getTeamMode()) {
            case CREATE:
               EntityTeam newTeam = new EntityTeam(this.player, teamName);
               this.entityTeams.put(teamName, newTeam);
               var10000 = newTeam;
               break;
            case REMOVE:
               var10000 = (EntityTeam)this.entityTeams.remove(teamName);
               break;
            default:
               var10000 = (EntityTeam)this.entityTeams.get(teamName);
            }

            EntityTeam entityTeam = var10000;
            if (entityTeam != null) {
               entityTeam.update(teams);
            }

         });
      }

   }

   @Nullable
   @Generated
   public EntityTeam getPlayerTeam() {
      return this.playerTeam;
   }

   @Generated
   public void setPlayerTeam(@Nullable EntityTeam playerTeam) {
      this.playerTeam = playerTeam;
   }
}
