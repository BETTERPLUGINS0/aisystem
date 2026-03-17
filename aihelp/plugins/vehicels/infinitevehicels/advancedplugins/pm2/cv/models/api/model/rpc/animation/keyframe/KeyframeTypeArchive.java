package advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe;

import advancedplugins.pm2.cv.models.api.utils.archive.AbstractArchive;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Generated;

public class KeyframeTypeArchive extends AbstractArchive<KeyframeType<?, ?>> {
   private final Set<String> keys = new LinkedHashSet();

   public void registerKeyframeType(KeyframeType<?, ?> var1) {
      this.keys.add(var1.getId());
      this.register(var1.getId(), var1);
   }

   @Generated
   public Set<String> getKeys() {
      return this.keys;
   }
}
