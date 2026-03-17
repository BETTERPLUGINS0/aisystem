package me.PM2.infinitevehicles.xseries.base;

import me.PM2.infinitevehicles.xseries.base.annotations.XChange;
import me.PM2.infinitevehicles.xseries.base.annotations.XInfo;
import me.PM2.infinitevehicles.xseries.base.annotations.XMerge;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class XModuleMetadata {
   private final boolean wasRemoved;
   private final XChange[] changes;
   private final XMerge[] merges;
   private final XInfo info;

   public XModuleMetadata(boolean var1, XChange[] var2, XMerge[] var3, XInfo var4) {
      this.wasRemoved = var1;
      this.changes = var2;
      this.merges = var3;
      this.info = var4;
   }

   public boolean wasRemoved() {
      return this.wasRemoved;
   }

   public XChange[] getChanges() {
      return this.changes;
   }

   public XMerge[] getMerges() {
      return this.merges;
   }

   public XInfo getInfo() {
      return this.info;
   }
}
