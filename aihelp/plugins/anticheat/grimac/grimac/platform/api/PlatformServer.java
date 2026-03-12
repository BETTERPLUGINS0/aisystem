package ac.grim.grimac.platform.api;

import ac.grim.grimac.platform.api.sender.Sender;

public interface PlatformServer {
   String getPlatformImplementationString();

   void dispatchCommand(Sender var1, String var2);

   Sender getConsoleSender();

   void registerOutgoingPluginChannel(String var1);

   double getTPS();
}
