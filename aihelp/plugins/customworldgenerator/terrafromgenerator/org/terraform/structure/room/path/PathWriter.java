package org.terraform.structure.room.path;

import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;

public abstract class PathWriter {
   public abstract void apply(PopulatorDataAbstract var1, TerraformWorld var2, PathState.PathNode var3);
}
