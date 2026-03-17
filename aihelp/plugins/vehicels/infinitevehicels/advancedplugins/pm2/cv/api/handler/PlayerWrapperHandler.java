package advancedplugins.pm2.cv.api.handler;

import advancedplugins.pm2.cv.api.player.PlayerWrapper;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerWrapperHandler extends PluginHandler {
   @NotNull
   PlayerWrapper getWrapper(@NotNull Player var1);

   @NotNull
   PlayerWrapper getWrapper(@NotNull UUID var1);
}
