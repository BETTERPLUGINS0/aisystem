package com.ryandw11.structure.io.sql;

import java.sql.SQLException;
import org.sqlite.Function;

public class DistanceFunction extends Function {
   protected void xFunc() throws SQLException {
      if (this.args() != 6) {
         throw new SQLException("DIST(x1, y1, z1, x2, y2, z2): Invalid argument count.");
      } else {
         double x1 = this.value_double(0);
         double y1 = this.value_double(1);
         double z1 = this.value_double(2);
         double x2 = this.value_double(3);
         double y2 = this.value_double(4);
         double z2 = this.value_double(5);
         this.result(Math.sqrt(Math.pow(x1 - x2, 2.0D) + Math.pow(y1 - y2, 2.0D) + Math.pow(z1 - z2, 2.0D)));
      }
   }
}
