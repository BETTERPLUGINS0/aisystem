package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.jdbc2.ArrayAssistant;
import fr.xephi.authme.libs.org.postgresql.util.ByteConverter;
import java.util.UUID;

public class UUIDArrayAssistant implements ArrayAssistant {
   public Class<?> baseType() {
      return UUID.class;
   }

   public Object buildElement(byte[] bytes, int pos, int len) {
      return new UUID(ByteConverter.int8(bytes, pos + 0), ByteConverter.int8(bytes, pos + 8));
   }

   public Object buildElement(String literal) {
      return UUID.fromString(literal);
   }
}
