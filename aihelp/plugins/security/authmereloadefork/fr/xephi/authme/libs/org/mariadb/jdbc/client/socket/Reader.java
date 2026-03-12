package fr.xephi.authme.libs.org.mariadb.jdbc.client.socket;

import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableByte;
import java.io.IOException;

public interface Reader {
   ReadableByteBuf readReusablePacket(boolean var1) throws IOException;

   ReadableByteBuf readReusablePacket() throws IOException;

   byte[] readPacket(boolean var1) throws IOException;

   ReadableByteBuf readableBufFromArray(byte[] var1);

   void skipPacket() throws IOException;

   MutableByte getSequence();

   void close() throws IOException;

   void setServerThreadId(Long var1, HostAddress var2);
}
