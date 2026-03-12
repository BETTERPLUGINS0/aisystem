package ac.grim.grimac.platform.api.command;

import ac.grim.grimac.platform.api.sender.Sender;
import java.util.Collection;

public interface PlayerSelector {
   boolean isSingle();

   Sender getSinglePlayer();

   Collection<Sender> getPlayers();

   String inputString();
}
