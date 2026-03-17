package advancedplugins.pm2.cv.models.api.model.rpc.entity.data;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.CullType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.utils.callback.ExecutionCallback;
import advancedplugins.pm2.cv.models.api.utils.data.io.DataIO;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.api.utils.math.Box;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface IEntityData extends DataIO {
   SavedData.DataSaver<CullType> CULL_TYPE_DATA_SAVER = (data, s, cullType) -> {
      data.putString(s, cullType.name());
   };
   SavedData.DataLoader<CullType> CULL_TYPE_DATA_LOADER = (data, key) -> {
      String s = data.getString(key);
      return s == null ? null : CullType.get(s);
   };
   ExecutionCallback<IEntityData.CullCameraOverride> PLAYER_EXECUTION_CALLBACK = new ExecutionCallback((cullCameraOverrides) -> {
      return (data, player, lastLocation) -> {
         Location location = lastLocation;

         IEntityData.CullCameraOverride override;
         for(Iterator var5 = cullCameraOverrides.iterator(); var5.hasNext(); location = override.calculate(data, player, location)) {
            override = (IEntityData.CullCameraOverride)var5.next();
         }

         return location;
      };
   });

   void asyncUpdate();

   void syncUpdate();

   void cullUpdate();

   void cleanup();

   void destroy();

   boolean isDataValid();

   Location getLocation();

   List<Entity> getPassengers();

   Set<UUID> getStartTracking();

   Map<UUID, CullType> getTracking();

   Set<UUID> getStopTracking();

   boolean hasTracking();

   Box getCullHitbox();

   void setCullHitbox(Box var1);

   Integer getCullInterval();

   void setCullInterval(Integer var1);

   int cullInterval();

   Boolean getVerticalCull();

   void setVerticalCull(Boolean var1);

   boolean verticalCull();

   Double getVerticalCullDistance();

   void setVerticalCullDistance(Double var1);

   double verticalCullDistance();

   CullType getVerticalCullType();

   void setVerticalCullType(CullType var1);

   CullType verticalCullType();

   Boolean getBackCull();

   void setBackCull(Boolean var1);

   boolean backCull();

   Double getBackCullAngle();

   void setBackCullAngle(Double var1);

   double backCullAngle();

   Double getBackCullIgnoreRadius();

   void setBackCullIgnoreRadius(Double var1);

   double backCullIgnoreRadius();

   CullType getBackCullType();

   void setBackCullType(CullType var1);

   CullType backCullType();

   Boolean getBlockedCull();

   void setBlockedCull(Boolean var1);

   boolean blockedCull();

   Double getBlockedCullIgnoreRadius();

   void setBlockedCullIgnoreRadius(Double var1);

   double blockedCullIgnoreRadius();

   CullType getBlockedCullType();

   void setBlockedCullType(CullType var1);

   CullType blockedCullType();

   void markModelGlowing(IVisualModel var1, boolean var2);

   void markJointGlowing(IJoint var1, boolean var2);

   boolean isModelGlowing();

   default void save(SavedData data) {
      data.saveIfExist("cull_interval", this::getCullInterval, SavedData::putInt);
      data.saveIfExist("vertical_cull", this::getVerticalCull, SavedData::putBoolean);
      data.saveIfExist("vertical_cull_distance", this::getVerticalCullDistance, SavedData::putDouble);
      data.saveIfExist("vertical_cull_type", this::getVerticalCullType, CULL_TYPE_DATA_SAVER);
      data.saveIfExist("back_cull", this::getBackCull, SavedData::putBoolean);
      data.saveIfExist("back_cull_angle", this::getBackCullAngle, SavedData::putDouble);
      data.saveIfExist("back_cull_ignore_radius", this::getBackCullIgnoreRadius, SavedData::putDouble);
      data.saveIfExist("back_cull_type", this::getBackCullType, CULL_TYPE_DATA_SAVER);
      data.saveIfExist("blocked_cull", this::getBlockedCull, SavedData::putBoolean);
      data.saveIfExist("blocked_cull_ignore_radius", this::getBlockedCullIgnoreRadius, SavedData::putDouble);
      data.saveIfExist("blocked_cull_type", this::getBlockedCullType, CULL_TYPE_DATA_SAVER);
   }

   default void load(SavedData data) {
      data.loadIfExist("cull_interval", SavedData::getInt, this::setCullInterval);
      data.loadIfExist("vertical_cull", SavedData::getBoolean, this::setVerticalCull);
      data.loadIfExist("vertical_cull_distance", SavedData::getDouble, this::setVerticalCullDistance);
      data.loadIfExist("vertical_cull_type", CULL_TYPE_DATA_LOADER, this::setVerticalCullType);
      data.loadIfExist("back_cull", SavedData::getBoolean, this::setBackCull);
      data.loadIfExist("back_cull_angle", SavedData::getDouble, this::setBackCullAngle);
      data.loadIfExist("back_cull_ignore_radius", SavedData::getDouble, this::setBackCullIgnoreRadius);
      data.loadIfExist("back_cull_type", CULL_TYPE_DATA_LOADER, this::setBackCullType);
      data.loadIfExist("blocked_cull", SavedData::getBoolean, this::setBlockedCull);
      data.loadIfExist("blocked_cull_ignore_radius", SavedData::getDouble, this::setBlockedCullIgnoreRadius);
      data.loadIfExist("blocked_cull_type", CULL_TYPE_DATA_LOADER, this::setBlockedCullType);
   }

   @FunctionalInterface
   public interface CullCameraOverride {
      Location calculate(IEntityData var1, Player var2, Location var3);
   }
}
