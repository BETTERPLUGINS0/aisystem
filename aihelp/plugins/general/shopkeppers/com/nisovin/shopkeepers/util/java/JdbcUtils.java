package com.nisovin.shopkeepers.util.java;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcUtils {
   private JdbcUtils() {
   }

   public static void setParameters(PreparedStatement preparedStatement, int parameterIndexOffset, Object... parameters) throws SQLException {
      Validate.notNull(preparedStatement, (String)"preparedStatement is null");
      Validate.notNull(parameters, (String)"parameters is null!");
      Validate.isTrue(parameterIndexOffset >= 0, "parameterIndexOffset must be positive!");
      int index = 1 + parameterIndexOffset;
      Object[] var4 = parameters;
      int var5 = parameters.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Object parameter = var4[var6];
         preparedStatement.setObject(index, parameter);
         ++index;
      }

   }
}
