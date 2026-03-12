package ac.grim.grimac.utils.team;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.UserProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import ac.grim.grimac.shaded.fastutil.objects.ObjectIterator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import lombok.Generated;

public final class EntityTeam {
   public final String name;
   public final Set<String> entries = new HashSet();
   private final GrimPlayer player;
   private WrapperPlayServerTeams.CollisionRule collisionRule;

   public EntityTeam(GrimPlayer player, String name) {
      this.player = player;
      this.name = name;
   }

   public void update(WrapperPlayServerTeams teams) {
      teams.getTeamInfo().ifPresent((info) -> {
         this.collisionRule = info.getCollisionRule();
      });
      TeamHandler teamHandler = (TeamHandler)this.player.checkManager.getPacketCheck(TeamHandler.class);
      WrapperPlayServerTeams.TeamMode mode = teams.getTeamMode();
      Iterator var4;
      String teamPlayer;
      ObjectIterator var6;
      UserProfile profile;
      if (mode != WrapperPlayServerTeams.TeamMode.ADD_ENTITIES && mode != WrapperPlayServerTeams.TeamMode.CREATE) {
         if (mode == WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES) {
            var4 = teams.getPlayers().iterator();

            while(true) {
               label62:
               while(var4.hasNext()) {
                  teamPlayer = (String)var4.next();
                  if (teamPlayer.equals(this.player.user.getName())) {
                     teamHandler.setPlayerTeam((EntityTeam)null);
                  } else {
                     var6 = this.player.compensatedEntities.profiles.values().iterator();

                     do {
                        if (!var6.hasNext()) {
                           teamHandler.removeEntityFromTeam(teamPlayer);
                           this.entries.remove(teamPlayer);
                           continue label62;
                        }

                        profile = (UserProfile)var6.next();
                     } while(profile.getName() == null || !profile.getName().equals(teamPlayer));

                     String uuid = profile.getUUID().toString();
                     this.entries.remove(uuid);
                     teamHandler.removeEntityFromTeam(uuid);
                  }
               }

               return;
            }
         } else if (mode == WrapperPlayServerTeams.TeamMode.REMOVE) {
            EntityTeam playersTeam = teamHandler.getPlayerTeam();
            if (playersTeam != null && playersTeam.name.equals(this.name)) {
               teamHandler.setPlayerTeam((EntityTeam)null);
            }

            Iterator var10 = this.entries.iterator();

            while(var10.hasNext()) {
               String entry = (String)var10.next();
               teamHandler.removeEntityFromTeam(entry);
            }

            this.entries.clear();
         }
      } else {
         var4 = teams.getPlayers().iterator();

         while(true) {
            label79:
            while(var4.hasNext()) {
               teamPlayer = (String)var4.next();
               if (teamPlayer.equals(this.player.user.getName())) {
                  teamHandler.setPlayerTeam(this);
               } else {
                  var6 = this.player.compensatedEntities.profiles.values().iterator();

                  do {
                     if (!var6.hasNext()) {
                        teamHandler.addEntityToTeam(teamPlayer, this);
                        continue label79;
                     }

                     profile = (UserProfile)var6.next();
                  } while(profile.getName() == null || !profile.getName().equals(teamPlayer));

                  teamHandler.addEntityToTeam(profile.getUUID().toString(), this);
               }
            }

            return;
         }
      }

   }

   public boolean equals(Object o) {
      boolean var10000;
      if (this != o) {
         label26: {
            if (o instanceof EntityTeam) {
               EntityTeam t = (EntityTeam)o;
               if (Objects.equals(this.name, t.name)) {
                  break label26;
               }
            }

            var10000 = false;
            return var10000;
         }
      }

      var10000 = true;
      return var10000;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.name});
   }

   @Generated
   public WrapperPlayServerTeams.CollisionRule getCollisionRule() {
      return this.collisionRule;
   }
}
