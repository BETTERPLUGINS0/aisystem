package org.terraform.coregen.populatordata;

import org.terraform.coregen.NaturalSpawnType;

public abstract class PopulatorDataICAAbstract extends PopulatorDataAbstract implements IPopulatorDataMinecartSpawner {
   public abstract void registerNaturalSpawns(NaturalSpawnType var1, int var2, int var3, int var4, int var5, int var6, int var7);
}
