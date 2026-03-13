package github.nighter.smartspawner.libs.mariadb.util.timeout;

public interface QueryTimeoutHandler extends AutoCloseable {
   QueryTimeoutHandler create(int var1);

   void close();
}
