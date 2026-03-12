package fr.xephi.authme.libs.com.mysql.cj.protocol;

public interface Message {
   byte[] getByteBuffer();

   int getPosition();
}
