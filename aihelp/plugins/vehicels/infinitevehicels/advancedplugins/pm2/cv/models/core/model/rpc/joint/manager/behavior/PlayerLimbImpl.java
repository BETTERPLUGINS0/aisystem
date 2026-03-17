package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.AbstractJointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.PlayerLimb;
import com.destroystokyo.paper.profile.PlayerProfile;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

public class PlayerLimbImpl extends AbstractJointAction<PlayerLimbImpl> implements PlayerLimb {
   private final PlayerLimb.Limb limbType;
   private boolean isSlim;

   public PlayerLimbImpl(IJoint var1, JointActionType<PlayerLimbImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
      this.limbType = (PlayerLimb.Limb)var3.get("limb");
   }

   public void onApply() {
      this.joint.setRenderer(true);
      this.joint.setModel(new ItemStack(Material.AIR));
   }

   public void onFinalize() {
      super.onFinalize();
      this.joint.getGlobalPosition().add(0.0F, this.isSlim ? this.limbType.slimYOffset : this.limbType.defaultYOffset, 0.0F);
   }

   public void setTexture(@Nullable Player var1) {
      if (var1 == null) {
         this.joint.setModel(new ItemStack(Material.AIR));
      } else {
         this.setTexture(var1.getPlayerProfile());
      }

   }

   public void setTexture(@Nullable PlayerProfile var1) {
      if (var1 == null) {
         this.joint.setModel(new ItemStack(Material.AIR));
      } else {
         ItemStack var2 = new ItemStack(Material.PLAYER_HEAD);
         ItemMeta var3 = var2.getItemMeta();
         ((SkullMeta)var3).setPlayerProfile(var1);
         Integer var10000;
         switch(var1.getTextures().getSkinModel()) {
         case CLASSIC:
            this.isSlim = false;
            var10000 = this.limbType.defaultId;
            break;
         case SLIM:
            this.isSlim = true;
            var10000 = this.limbType.slimId;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         Integer var4 = var10000;
         var3.setCustomModelData(var4);
         var2.setItemMeta(var3);
         this.joint.setModel(var2);
      }

   }

   @Generated
   public PlayerLimb.Limb getLimbType() {
      return this.limbType;
   }
}
