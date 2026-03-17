package advancedplugins.pm2.cv.models.api.model.rpc.error;

import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class ErrorCollector {
   private final String fileName;
   private final List<IError> errorList = new ArrayList();

   public void collect(IError var1) {
      this.errorList.add(var1);
   }

   public void logAll() {
      String var10000 = String.valueOf(LogUtil.LogColor.GOLD);
      LogUtil.log(var10000 + "Trying to import " + this.fileName + ".");
      this.errorList.forEach(IError::log);
   }

   @Generated
   public ErrorCollector(String var1) {
      this.fileName = var1;
   }

   @Generated
   public String getFileName() {
      return this.fileName;
   }

   @Generated
   public List<IError> getErrorList() {
      return this.errorList;
   }
}
