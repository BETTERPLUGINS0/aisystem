package fr.xephi.authme.libs.org.mariadb.jdbc.client.util;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import java.io.IOException;
import java.sql.SQLException;

public interface Parameter {
   void encodeText(Writer var1, Context var2) throws IOException, SQLException;

   void encodeBinary(Writer var1) throws IOException, SQLException;

   void encodeLongData(Writer var1) throws IOException, SQLException;

   byte[] encodeData() throws IOException, SQLException;

   boolean canEncodeLongData();

   int getBinaryEncodeType();

   boolean isNull();

   String bestEffortStringValue(Context var1);
}
