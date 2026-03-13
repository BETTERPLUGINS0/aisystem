package org.terraform.biome.cavepopulators;

import java.util.Random;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;

public abstract class AbstractCavePopulator {
   public abstract void populate(TerraformWorld var1, Random var2, SimpleBlock var3, SimpleBlock var4);
}
