package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.Reflection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.channel.Channel;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ViaVersionAccessorImplLegacy implements ViaVersionAccessor {
   private Class<?> viaClass;
   private Class<?> bukkitDecodeHandlerClass;
   private Class<?> bukkitEncodeHandlerClass;
   private Field viaManagerField;
   private Method apiAccessor;
   private Method getPlayerVersionMethod;
   private Class<?> userConnectionClass;

   private void load() {
      ClassLoader classLoader;
      if (this.viaClass == null) {
         try {
            classLoader = PacketEvents.getAPI().getPlugin().getClass().getClassLoader();
            this.viaClass = classLoader.loadClass("us.myles.ViaVersion.api.Via");
            this.viaManagerField = this.viaClass.getDeclaredField("manager");
            this.bukkitDecodeHandlerClass = classLoader.loadClass("us.myles.ViaVersion.bukkit.handlers.BukkitDecodeHandler");
            this.bukkitEncodeHandlerClass = classLoader.loadClass("us.myles.ViaVersion.bukkit.handlers.BukkitEncodeHandler");
            Class<?> viaAPIClass = classLoader.loadClass("us.myles.ViaVersion.api.ViaAPI");
            this.apiAccessor = this.viaClass.getMethod("getAPI");
            this.getPlayerVersionMethod = viaAPIClass.getMethod("getPlayerVersion", Object.class);
         } catch (NoSuchMethodException | NoSuchFieldException | ClassNotFoundException var4) {
            var4.printStackTrace();
         }
      }

      if (this.userConnectionClass == null) {
         try {
            classLoader = PacketEvents.getAPI().getPlugin().getClass().getClassLoader();
            this.userConnectionClass = classLoader.loadClass("us.myles.ViaVersion.api.data.UserConnection");
         } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
         }
      }

   }

   public int getProtocolVersion(Player player) {
      this.load();

      try {
         Object viaAPI = this.apiAccessor.invoke((Object)null);
         return (Integer)this.getPlayerVersionMethod.invoke(viaAPI, player);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         var3.printStackTrace();
         return -1;
      }
   }

   public int getProtocolVersion(User user) {
      try {
         if (user.getUUID() != null) {
            Player player = Bukkit.getPlayer(user.getUUID());
            if (player != null) {
               int version = this.getProtocolVersion(player);
               if (version != -1) {
                  return version;
               }
            }
         }

         Object viaEncoder = ((Channel)user.getChannel()).pipeline().get("via-encoder");
         Object connection = Reflection.getField(viaEncoder.getClass(), "connection").get(viaEncoder);
         Object protocolInfo = Reflection.getField(connection.getClass(), "protocolInfo").get(connection);
         Object protocolVersion = Reflection.getField(protocolInfo.getClass(), "protocolVersion").get(protocolInfo);
         return protocolVersion instanceof Integer ? (Integer)protocolVersion : ((ProtocolVersion)protocolVersion).getVersion();
      } catch (Exception var6) {
         PacketEvents.getAPI().getLogManager().warn("Unable to grab ViaVersion client version for player!");
         return -1;
      }
   }

   public Class<?> getUserConnectionClass() {
      this.load();
      return this.userConnectionClass;
   }

   public Class<?> getBukkitDecodeHandlerClass() {
      this.load();
      return this.bukkitDecodeHandlerClass;
   }

   public Class<?> getBukkitEncodeHandlerClass() {
      this.load();
      return this.bukkitEncodeHandlerClass;
   }
}
