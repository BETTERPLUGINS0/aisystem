package fr.xephi.authme.libs.com.mysql.cj.protocol;

public interface PacketReceivedTimeHolder {
   default long getLastPacketReceivedTime() {
      return 0L;
   }
}
