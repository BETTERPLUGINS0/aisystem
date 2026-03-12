package fr.xephi.authme.command.executable.email;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.Utils;
import java.util.List;
import org.bukkit.entity.Player;

public class ShowEmailCommand extends PlayerCommand {
   @Inject
   private CommonService commonService;
   @Inject
   private PlayerCache playerCache;

   public void runCommand(Player player, List<String> arguments) {
      PlayerAuth auth = this.playerCache.getAuth(player.getName());
      if (auth != null && !Utils.isEmailEmpty(auth.getEmail())) {
         if ((Boolean)this.commonService.getProperty(SecuritySettings.USE_EMAIL_MASKING)) {
            this.commonService.send(player, MessageKey.EMAIL_SHOW, this.emailMask(auth.getEmail()));
         } else {
            this.commonService.send(player, MessageKey.EMAIL_SHOW, auth.getEmail());
         }
      } else {
         this.commonService.send(player, MessageKey.SHOW_NO_EMAIL);
      }

   }

   private String emailMask(String email) {
      String[] frag = email.split("@");
      int sid = frag[0].length() / 3 + 1;
      int sdomain = frag[1].length() / 3;
      String id = frag[0].substring(0, sid) + "***";
      String domain = "***" + frag[1].substring(sdomain);
      return id + "@" + domain;
   }
}
