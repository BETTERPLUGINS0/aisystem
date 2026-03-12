package fr.xephi.authme.libs.waffle.servlet;

import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import fr.xephi.authme.libs.waffle.util.WaffleInfo;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WaffleInfoServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static final Logger logger = LoggerFactory.getLogger(WaffleInfoServlet.class);

   public void doGet(HttpServletRequest request, HttpServletResponse response) {
      this.getWaffleInfoResponse(request, response);
   }

   public void doPost(HttpServletRequest request, HttpServletResponse response) {
      this.getWaffleInfoResponse(request, response);
   }

   public void getWaffleInfoResponse(HttpServletRequest request, HttpServletResponse response) {
      WaffleInfo info = new WaffleInfo();

      try {
         Document doc = info.getWaffleInfo();
         Element root = doc.getDocumentElement();
         Element http = this.getRequestInfo(doc, request);
         root.insertBefore(http, root.getFirstChild());
         String[] lookup = request.getParameterValues("lookup");
         if (lookup != null) {
            String[] var8 = lookup;
            int var9 = lookup.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String name = var8[var10];
               root.appendChild(info.getLookupInfo(doc, name));
            }
         }

         TransformerFactory transfac = TransformerFactory.newInstance();
         transfac.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
         transfac.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet", "");
         Transformer trans = transfac.newTransformer();
         trans.setOutputProperty("indent", "yes");
         StreamResult result = new StreamResult(response.getWriter());
         DOMSource source = new DOMSource(doc);
         trans.transform(source, result);
         response.setContentType("application/xml");
      } catch (TransformerException | IOException | ParserConfigurationException var12) {
         logger.error((String)"", (Throwable)var12);
         throw new RuntimeException("See logs for underlying error condition");
      }
   }

   private Element getRequestInfo(Document doc, HttpServletRequest request) {
      Element node = doc.createElement("request");
      Element value = doc.createElement("AuthType");
      value.setTextContent(request.getAuthType());
      node.appendChild(value);
      Principal p = request.getUserPrincipal();
      if (p != null) {
         Element child = doc.createElement("principal");
         child.setAttribute("class", p.getClass().getName());
         value = doc.createElement("name");
         value.setTextContent(p.getName());
         child.appendChild(value);
         value = doc.createElement("string");
         value.setTextContent(p.toString());
         child.appendChild(value);
         node.appendChild(child);
      }

      List<String> headers = Collections.list(request.getHeaderNames());
      if (!headers.isEmpty()) {
         Element child = doc.createElement("headers");
         Iterator var8 = headers.iterator();

         while(var8.hasNext()) {
            String header = (String)var8.next();
            value = doc.createElement(header);
            value.setTextContent(request.getHeader(header));
            child.appendChild(value);
         }

         node.appendChild(child);
      }

      return node;
   }
}
