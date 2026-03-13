package github.nighter.smartspawner.libs.mariadb.client;

public interface ServerVersion {
   String getVersion();

   int getMajorVersion();

   int getMinorVersion();

   int getPatchVersion();

   String getQualifier();

   boolean versionGreaterOrEqual(int var1, int var2, int var3);

   boolean versionFixedMajorMinorGreaterOrEqual(int var1, int var2, int var3);

   boolean isMariaDBServer();
}
