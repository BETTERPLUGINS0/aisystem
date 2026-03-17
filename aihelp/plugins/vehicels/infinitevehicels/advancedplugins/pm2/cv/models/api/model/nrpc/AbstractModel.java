package advancedplugins.pm2.cv.models.api.model.nrpc;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import lombok.Generated;
import org.bukkit.Location;

public abstract class AbstractModel {
   private final Map<UUID, AbstractModelPart> parts = new ConcurrentHashMap();
   private final BlockbenchModel blockbenchModel;
   private final String name;

   public AbstractModel(String var1, BlockbenchModel var2) {
      this.name = var1;
      this.blockbenchModel = var2;
   }

   public abstract Location getLocation();

   public abstract void spawn();

   public abstract void destroy();

   public void addPart(AbstractModelPart var1) {
      this.parts.put(var1.getUniqueID(), var1);
   }

   @Nullable
   public AbstractModelPart getPart(UUID var1) {
      return (AbstractModelPart)this.parts.get(var1);
   }

   @Nullable
   public AbstractModelPart getPart(String var1) {
      Iterator var2 = this.parts.values().iterator();

      AbstractModelPart var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (AbstractModelPart)var2.next();
      } while(!var3.getName().equals(var1) && (var3.getParentBoneName() == null || !var3.getParentBoneName().equals(var1)));

      return var3;
   }

   public List<AbstractModelPart> getPartsFromParent(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.parts.values().iterator();

      while(var3.hasNext()) {
         AbstractModelPart var4 = (AbstractModelPart)var3.next();
         if (var4.getParentBoneName() != null && var4.getParentBoneName().equals(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   @Generated
   public Map<UUID, AbstractModelPart> getParts() {
      return this.parts;
   }

   @Generated
   public BlockbenchModel getBlockbenchModel() {
      return this.blockbenchModel;
   }

   @Generated
   public String getName() {
      return this.name;
   }
}
