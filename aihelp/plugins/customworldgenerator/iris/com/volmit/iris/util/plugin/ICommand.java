package com.volmit.iris.util.plugin;

import com.volmit.iris.util.collection.KList;

public interface ICommand {
   KList<String> getRequiredPermissions();

   String getNode();

   KList<String> getNodes();

   KList<String> getAllNodes();

   void addNode(String node);

   boolean handle(VolmitSender sender, String[] args);

   KList<String> handleTab(VolmitSender sender, String[] args);
}
