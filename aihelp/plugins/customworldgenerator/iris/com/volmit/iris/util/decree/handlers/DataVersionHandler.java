package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.core.nms.datapack.DataVersion;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.DecreeParameterHandler;
import com.volmit.iris.util.decree.exceptions.DecreeParsingException;

public class DataVersionHandler implements DecreeParameterHandler<DataVersion> {
   public KList<DataVersion> getPossibilities() {
      return (new KList(DataVersion.values())).qdel(DataVersion.UNSUPPORTED);
   }

   public String toString(DataVersion version) {
      return var1.getVersion();
   }

   public DataVersion parse(String in, boolean force) {
      if (var1.equalsIgnoreCase("latest")) {
         return DataVersion.getLatest();
      } else {
         DataVersion[] var3 = DataVersion.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            DataVersion var6 = var3[var5];
            if (var6.getVersion().equalsIgnoreCase(var1)) {
               return var6;
            }
         }

         throw new DecreeParsingException("Unable to parse data version \"" + var1 + "\"");
      }
   }

   public boolean supports(Class<?> type) {
      return DataVersion.class.equals(var1);
   }
}
