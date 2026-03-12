package ac.grim.grimac.platform.api;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.command.CommandService;
import ac.grim.grimac.platform.api.manager.ItemResetHandler;
import ac.grim.grimac.platform.api.manager.MessagePlaceHolderManager;
import ac.grim.grimac.platform.api.manager.PermissionRegistrationManager;
import ac.grim.grimac.platform.api.manager.PlatformPluginManager;
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory;
import ac.grim.grimac.platform.api.scheduler.PlatformScheduler;
import ac.grim.grimac.platform.api.sender.SenderFactory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface PlatformLoader {
   PlatformScheduler getScheduler();

   PlatformPlayerFactory getPlatformPlayerFactory();

   PacketEventsAPI<?> getPacketEvents();

   ItemResetHandler getItemResetHandler();

   CommandService getCommandService();

   SenderFactory<?> getSenderFactory();

   GrimPlugin getPlugin();

   PlatformPluginManager getPluginManager();

   PlatformServer getPlatformServer();

   void registerAPIService();

   @NotNull
   MessagePlaceHolderManager getMessagePlaceHolderManager();

   PermissionRegistrationManager getPermissionManager();
}
