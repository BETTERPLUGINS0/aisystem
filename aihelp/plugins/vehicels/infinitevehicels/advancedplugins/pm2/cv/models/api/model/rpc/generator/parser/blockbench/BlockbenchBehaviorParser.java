package advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench;

import advancedplugins.pm2.cv.models.api.model.rpc.error.ErrorCollector;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;

public interface BlockbenchBehaviorParser {
   void processModel(ErrorCollector var1, BlockbenchModel var2, ModelBlueprint var3);

   void processJoint(ErrorCollector var1, BlockbenchModel var2, BlockbenchModel.Group var3, BlueprintJoint var4);
}
