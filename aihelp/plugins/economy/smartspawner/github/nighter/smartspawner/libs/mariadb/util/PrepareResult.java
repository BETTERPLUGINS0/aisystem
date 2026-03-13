package github.nighter.smartspawner.libs.mariadb.util;

public interface PrepareResult {
   String getSql();

   int getParamCount();
}
