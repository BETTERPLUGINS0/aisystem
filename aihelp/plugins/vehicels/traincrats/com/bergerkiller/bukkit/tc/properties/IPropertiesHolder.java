package com.bergerkiller.bukkit.tc.properties;

import org.bukkit.World;

public interface IPropertiesHolder {
   World getWorld();

   IProperties getProperties();

   void onPropertiesChanged();
}
