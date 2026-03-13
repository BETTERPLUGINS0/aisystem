package github.nighter.smartspawner.libs.mariadb.client;

import github.nighter.smartspawner.libs.mariadb.BasePreparedStatement;
import github.nighter.smartspawner.libs.mariadb.export.Prepare;

public interface PrepareCache {
   Prepare get(String var1, BasePreparedStatement var2);

   Prepare put(String var1, Prepare var2, BasePreparedStatement var3);

   void reset();
}
