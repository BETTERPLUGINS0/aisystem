package fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

@FunctionalInterface
public interface SocketHandlerFunction {
   Socket apply(Configuration var1, HostAddress var2) throws IOException, SQLException;
}
