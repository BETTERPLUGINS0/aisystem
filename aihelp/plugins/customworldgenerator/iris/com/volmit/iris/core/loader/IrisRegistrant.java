package com.volmit.iris.core.loader;

import com.google.gson.GsonBuilder;
import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.IrisScript;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import java.awt.Desktop;
import java.io.File;
import lombok.Generated;

public abstract class IrisRegistrant {
   @Desc("Preprocess this object in-memory when it's loaded, run scripts using the variable 'object' and modify properties about this object before it's used.\nFile extension: .proc.kts")
   @RegistryListResource(IrisScript.class)
   @ArrayType(
      min = 1,
      type = String.class
   )
   private KList<String> preprocessors = new KList();
   private transient IrisData loader;
   private transient String loadKey;
   private transient File loadFile;

   public abstract String getFolderName();

   public abstract String getTypeName();

   public void registerTypeAdapters(GsonBuilder builder) {
   }

   public File openInVSCode() {
      try {
         Desktop.getDesktop().open(this.getLoadFile());
      } catch (Throwable var2) {
         Iris.reportError(var2);
      }

      return this.getLoadFile();
   }

   public abstract void scanForErrors(JSONObject p, VolmitSender sender);

   @Generated
   public KList<String> getPreprocessors() {
      return this.preprocessors;
   }

   @Generated
   public IrisData getLoader() {
      return this.loader;
   }

   @Generated
   public String getLoadKey() {
      return this.loadKey;
   }

   @Generated
   public File getLoadFile() {
      return this.loadFile;
   }

   @Generated
   public void setPreprocessors(final KList<String> preprocessors) {
      this.preprocessors = var1;
   }

   @Generated
   public void setLoader(final IrisData loader) {
      this.loader = var1;
   }

   @Generated
   public void setLoadKey(final String loadKey) {
      this.loadKey = var1;
   }

   @Generated
   public void setLoadFile(final File loadFile) {
      this.loadFile = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisRegistrant)) {
         return false;
      } else {
         IrisRegistrant var2 = (IrisRegistrant)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            KList var3 = this.getPreprocessors();
            KList var4 = var2.getPreprocessors();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisRegistrant;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KList var3 = this.getPreprocessors();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getPreprocessors());
      return "IrisRegistrant(preprocessors=" + var10000 + ", loader=" + String.valueOf(this.getLoader()) + ", loadKey=" + this.getLoadKey() + ", loadFile=" + String.valueOf(this.getLoadFile()) + ")";
   }
}
