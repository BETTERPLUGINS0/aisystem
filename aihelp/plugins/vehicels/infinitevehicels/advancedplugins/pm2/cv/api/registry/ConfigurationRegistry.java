package advancedplugins.pm2.cv.api.registry;

import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import java.io.IOException;

public interface ConfigurationRegistry<T extends IDeyed> extends Registry<T> {
   void reload();

   void saveDefaults() throws IOException;
}
