package github.nighter.smartspawner.libs.mariadb.client.socket;

import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableByte;
import java.io.IOException;

public interface Reader {
   ReadableByteBuf readReusablePacket(boolean var1) throws IOException;

   ReadableByteBuf readReusablePacket() throws IOException;

   byte[] readPacket(boolean var1) throws IOException;

   ReadableByteBuf readableBufFromArray(byte[] var1);

   MutableByte getSequence();

   void close() throws IOException;

   void setServerThreadId(Long var1, HostAddress var2);
}
