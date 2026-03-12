package fr.xephi.authme.libs.org.mariadb.jdbc.client.util;

public interface Parameters {
   Parameter get(int var1);

   boolean containsKey(int var1);

   void set(int var1, Parameter var2);

   int size();

   Parameters clone();
}
