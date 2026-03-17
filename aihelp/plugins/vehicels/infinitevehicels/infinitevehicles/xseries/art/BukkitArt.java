package me.PM2.infinitevehicles.xseries.art;

import me.PM2.infinitevehicles.xseries.AbstractReferencedClass;
import org.bukkit.Art;

public abstract class BukkitArt extends AbstractReferencedClass<Art> {
   public abstract int getBlockWidth();

   public abstract int getBlockHeight();

   public abstract String getKey();

   public abstract int getId();
}
