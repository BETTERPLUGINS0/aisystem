package advancedplugins.pm2.cv.models.core.model.rpc.generator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModelCacheGroup {
   protected final Map<String, ModelIdCache> cache = new ConcurrentHashMap();
}
