package advancedplugins.pm2.cv.models.api.model.rpc.generator.parser;

import advancedplugins.pm2.cv.models.api.model.rpc.error.ErrorCollector;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ModelAssets;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import it.unimi.dsi.fastutil.Pair;
import java.io.File;

public interface ModelParser {
   boolean validateFile(File var1);

   Pair<ModelBlueprint, ModelAssets> generate(File var1, ErrorCollector var2) throws Exception;
}
