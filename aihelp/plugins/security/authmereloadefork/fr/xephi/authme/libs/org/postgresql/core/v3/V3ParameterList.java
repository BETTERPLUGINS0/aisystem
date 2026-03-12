package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

interface V3ParameterList extends ParameterList {
   void checkAllParametersSet() throws SQLException;

   void convertFunctionOutParameters();

   @Nullable
   SimpleParameterList[] getSubparams();

   @Nullable
   int[] getParamTypes();

   @Nullable
   byte[] getFlags();

   @Nullable
   byte[][] getEncoding();
}
