package fr.xephi.authme.libs.com.mysql.cj.protocol;

import fr.xephi.authme.libs.com.mysql.cj.result.Row;

public interface ResultsetRow extends Row, ProtocolEntity {
   default boolean isBinaryEncoded() {
      return false;
   }
}
