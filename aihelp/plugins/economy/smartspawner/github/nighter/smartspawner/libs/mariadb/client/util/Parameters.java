package github.nighter.smartspawner.libs.mariadb.client.util;

public interface Parameters {
   Parameter get(int var1);

   boolean containsKey(int var1);

   void set(int var1, Parameter var2);

   int size();

   Parameters clone();
}
