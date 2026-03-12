package ac.grim.grimac.utils.team;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import lombok.Generated;

public final class EntityPredicates {
   public static boolean canBePushedBy(EntityTeam entityTeam, EntityTeam playersTeam) {
      WrapperPlayServerTeams.CollisionRule entityCollisionRule = entityTeam == null ? WrapperPlayServerTeams.CollisionRule.ALWAYS : entityTeam.getCollisionRule();
      if (entityCollisionRule == WrapperPlayServerTeams.CollisionRule.NEVER) {
         return false;
      } else {
         WrapperPlayServerTeams.CollisionRule playerCollisionRule = playersTeam == null ? WrapperPlayServerTeams.CollisionRule.ALWAYS : playersTeam.getCollisionRule();
         if (playerCollisionRule == WrapperPlayServerTeams.CollisionRule.NEVER) {
            return false;
         } else {
            boolean isSameTeam = entityTeam != null && entityTeam.equals(playersTeam);
            return (!isSameTeam || entityCollisionRule != WrapperPlayServerTeams.CollisionRule.PUSH_OWN_TEAM && playerCollisionRule != WrapperPlayServerTeams.CollisionRule.PUSH_OWN_TEAM) && (entityCollisionRule != WrapperPlayServerTeams.CollisionRule.PUSH_OTHER_TEAMS && playerCollisionRule != WrapperPlayServerTeams.CollisionRule.PUSH_OTHER_TEAMS || isSameTeam);
         }
      }
   }

   @Generated
   private EntityPredicates() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
