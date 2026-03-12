package ac.grim.grimac.platform.bukkit.command;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.command.PlayerSelector;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.platform.bukkit.sender.BukkitSenderFactory;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.SinglePlayerSelector;
import java.util.Collection;
import java.util.Collections;
import lombok.Generated;
import org.bukkit.command.CommandSender;

public class BukkitPlayerSelectorAdapter implements PlayerSelector {
   private final SinglePlayerSelector bukkitSelector;

   public boolean isSingle() {
      return true;
   }

   public Sender getSinglePlayer() {
      return ((BukkitSenderFactory)GrimAPI.INSTANCE.getSenderFactory()).map((CommandSender)this.bukkitSelector.single());
   }

   public Collection<Sender> getPlayers() {
      return Collections.singletonList(((BukkitSenderFactory)GrimAPI.INSTANCE.getSenderFactory()).map((CommandSender)this.bukkitSelector.single()));
   }

   public String inputString() {
      return this.bukkitSelector.inputString();
   }

   @Generated
   public BukkitPlayerSelectorAdapter(SinglePlayerSelector bukkitSelector) {
      this.bukkitSelector = bukkitSelector;
   }
}
