package github.nighter.smartspawner.libs.mariadb.client.socket.impl;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

@FunctionalInterface
public interface SocketHandlerFunction {
   Socket apply(Configuration var1, HostAddress var2) throws IOException, SQLException;
}
