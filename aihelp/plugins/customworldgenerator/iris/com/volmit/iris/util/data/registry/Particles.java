package com.volmit.iris.util.data.registry;

import org.bukkit.Particle;

public class Particles {
   public static final Particle CRIT_MAGIC = (Particle)RegistryUtil.find(Particle.class, "crit_magic", "crit");
   public static final Particle REDSTONE = (Particle)RegistryUtil.find(Particle.class, "redstone", "dust");
   public static final Particle ITEM = (Particle)RegistryUtil.find(Particle.class, "item_crack", "item");
}
