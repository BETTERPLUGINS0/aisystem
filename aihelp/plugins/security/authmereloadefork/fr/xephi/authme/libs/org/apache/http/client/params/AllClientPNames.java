package fr.xephi.authme.libs.org.apache.http.client.params;

import fr.xephi.authme.libs.org.apache.http.auth.params.AuthPNames;
import fr.xephi.authme.libs.org.apache.http.conn.params.ConnConnectionPNames;
import fr.xephi.authme.libs.org.apache.http.conn.params.ConnManagerPNames;
import fr.xephi.authme.libs.org.apache.http.conn.params.ConnRoutePNames;
import fr.xephi.authme.libs.org.apache.http.cookie.params.CookieSpecPNames;
import fr.xephi.authme.libs.org.apache.http.params.CoreConnectionPNames;
import fr.xephi.authme.libs.org.apache.http.params.CoreProtocolPNames;

/** @deprecated */
@Deprecated
public interface AllClientPNames extends CoreConnectionPNames, CoreProtocolPNames, ClientPNames, AuthPNames, CookieSpecPNames, ConnConnectionPNames, ConnManagerPNames, ConnRoutePNames {
}
