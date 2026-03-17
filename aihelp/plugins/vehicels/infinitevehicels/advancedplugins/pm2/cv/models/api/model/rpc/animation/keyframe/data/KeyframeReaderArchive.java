package advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.data;

import advancedplugins.pm2.cv.models.api.utils.archive.AbstractArchive;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import java.util.Iterator;
import java.util.function.Function;

public class KeyframeReaderArchive extends AbstractArchive<Function<String, IKeyframeData>> {
   public IKeyframeData tryParse(String var1) {
      if (var1 == null) {
         return IKeyframeData.EMPTY;
      } else {
         var1 = var1.trim();
         if (var1.isEmpty()) {
            return IKeyframeData.EMPTY;
         } else {
            try {
               return new DoubleData(Double.parseDouble(var1));
            } catch (NumberFormatException var12) {
               String[] var3 = var1.split(":", 2);
               if (var3.length == 1) {
                  Iterator var13 = this.registry.values().iterator();

                  while(var13.hasNext()) {
                     Function var14 = (Function)var13.next();

                     try {
                        return (IKeyframeData)var14.apply(var3[0]);
                     } catch (Throwable var10) {
                     }
                  }
               } else {
                  Function var4 = (Function)this.get(var3[0]);
                  if (var4 != null) {
                     try {
                        return (IKeyframeData)var4.apply(var3[1]);
                     } catch (Throwable var9) {
                        LogUtil.error(2, "------An error occurred while parsing the keyframe: " + var1);
                        var9.printStackTrace();
                     }
                  } else {
                     Iterator var5 = this.registry.values().iterator();

                     while(var5.hasNext()) {
                        Function var6 = (Function)var5.next();
                        Function var7 = var6;

                        try {
                           return (IKeyframeData)var7.apply(var1);
                        } catch (Throwable var11) {
                        }
                     }
                  }
               }

               LogUtil.warn(2, "------Unknown keyframe data: " + var1);
               return IKeyframeData.EMPTY;
            }
         }
      }
   }
}
