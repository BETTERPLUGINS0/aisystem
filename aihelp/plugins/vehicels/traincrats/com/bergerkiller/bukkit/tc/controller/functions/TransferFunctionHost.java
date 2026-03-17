package com.bergerkiller.bukkit.tc.controller.functions;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.functions.inputs.TransferFunctionInput;
import com.bergerkiller.bukkit.tc.properties.CartProperties;

public interface TransferFunctionHost extends TrainCarts.Provider {
   TransferFunctionRegistry getRegistry();

   TransferFunctionInput.ReferencedSource registerInputSource(TransferFunctionInput.ReferencedSource var1);

   default CartProperties getCartProperties() {
      MinecartMember<?> member = this.getMember();
      return member == null ? null : member.getProperties();
   }

   MinecartMember<?> getMember();

   Attachment getAttachment();

   boolean isSequencer();

   boolean isAttachment();

   default TransferFunction loadFunction(ConfigurationNode config) {
      return this.getRegistry().load(this, config);
   }

   default ConfigurationNode saveFunction(TransferFunction function) {
      return this.getRegistry().save(this, function);
   }
}
