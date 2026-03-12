package xyz.xenondevs.particle.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleConstants;

public final class ParticleUtils {
   public static void sendBulk(Collection<Object> packets, Player player) {
      Object connection = ReflectionUtils.PLAYER_CONNECTION_CACHE.getConnection(player);
      Iterator var3 = packets.iterator();

      while(var3.hasNext()) {
         Object packet = var3.next();

         try {
            ParticleConstants.PLAYER_CONNECTION_SEND_PACKET_METHOD.invoke(connection, packet);
         } catch (Exception var6) {
         }
      }

   }

   public static void sendBulk(Collection<Object> packets, Collection<Player> players) {
      Iterator var2 = players.iterator();

      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         sendBulk(packets, player);
      }

   }

   public static void sendBulk(Collection<Object> packets) {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         sendBulk(packets, player);
      }

   }

   public static void sendBulkBuilders(Collection<ParticleBuilder> builders, Player player) {
      sendBulk((Collection)builders.stream().map(ParticleBuilder::toPacket).collect(Collectors.toList()), player);
   }

   public static void sendBulkBuilders(Collection<ParticleBuilder> builders, Collection<Player> players) {
      List<Object> packets = (List)builders.stream().map(ParticleBuilder::toPacket).collect(Collectors.toList());
      Iterator var3 = players.iterator();

      while(var3.hasNext()) {
         Player player = (Player)var3.next();
         sendBulk(packets, (Player)player);
      }

   }

   public static void sendBulkBuilders(Collection<ParticleBuilder> builders) {
      List<Object> packets = (List)builders.stream().map(ParticleBuilder::toPacket).collect(Collectors.toList());
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         sendBulk(packets, (Player)player);
      }

   }
}
