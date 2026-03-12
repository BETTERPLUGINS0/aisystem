package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.util.PlayerUtils;
import java.util.Locale;
import org.bukkit.entity.Player;

final class PlayerAuthBuilderHelper {
   private PlayerAuthBuilderHelper() {
   }

   static PlayerAuth createPlayerAuth(Player player, HashedPassword hashedPassword, String email) {
      return PlayerAuth.builder().name(player.getName().toLowerCase(Locale.ROOT)).realName(player.getName()).password(hashedPassword).email(email).registrationIp(PlayerUtils.getPlayerIp(player)).registrationDate(System.currentTimeMillis()).uuid(player.getUniqueId()).build();
   }
}
