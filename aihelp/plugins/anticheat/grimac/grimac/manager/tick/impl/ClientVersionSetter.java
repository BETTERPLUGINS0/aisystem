package ac.grim.grimac.manager.tick.impl;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.tick.Tickable;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import java.util.Iterator;

public class ClientVersionSetter implements Tickable {
   public void tick() {
      Iterator var1 = GrimAPI.INSTANCE.getPlayerDataManager().getEntries().iterator();

      while(var1.hasNext()) {
         GrimPlayer player = (GrimPlayer)var1.next();
         if (!ChannelHelper.isOpen(player.user.getChannel())) {
            GrimAPI.INSTANCE.getPlayerDataManager().onDisconnect(player.user);
         } else {
            player.pollData();
         }
      }

   }
}
