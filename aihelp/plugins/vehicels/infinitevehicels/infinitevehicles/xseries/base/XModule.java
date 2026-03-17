package me.PM2.infinitevehicles.xseries.base;

import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class XModule<XForm extends XModule<XForm, BukkitForm>, BukkitForm> implements XBase<XForm, BukkitForm> {
   private final BukkitForm bukkitForm;
   private final String[] names;

   @Internal
   protected XModule(BukkitForm var1, String[] var2) {
      this.bukkitForm = var1;
      this.names = var2;
   }

   @NotNull
   public final String name() {
      return this.names[0];
   }

   @Experimental
   protected void setEnumName(XRegistry<XForm, BukkitForm> var1, String var2) {
      if (this.names[0] != null) {
         throw new IllegalStateException("Enum name already set " + var2 + " -> " + Arrays.toString(this.names));
      } else {
         this.names[0] = var2;
         Object var3 = var1.getBukkit(this.names);
         if (this.bukkitForm != var3) {
            var1.std((XBase)this);
         }

      }
   }

   @Internal
   public String[] getNames() {
      return this.names;
   }

   @Nullable
   public final BukkitForm get() {
      return this.bukkitForm;
   }

   public final String toString() {
      return (this.isSupported() ? "" : "!") + this.getClass().getSimpleName() + '(' + this.name() + ')';
   }

   public final int hashCode() {
      return super.hashCode();
   }

   /** @deprecated */
   @Deprecated
   public final boolean equals(Object var1) {
      return super.equals(var1);
   }
}
