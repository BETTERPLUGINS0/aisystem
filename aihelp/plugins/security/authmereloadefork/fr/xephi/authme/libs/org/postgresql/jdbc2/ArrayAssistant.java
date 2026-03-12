package fr.xephi.authme.libs.org.postgresql.jdbc2;

public interface ArrayAssistant {
   Class<?> baseType();

   Object buildElement(byte[] var1, int var2, int var3);

   Object buildElement(String var1);
}
