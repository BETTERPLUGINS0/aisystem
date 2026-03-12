package fr.xephi.authme.libs.waffle.util;

import com.sun.jna.Platform;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.Netapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAccount;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAuthProvider;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsComputer;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsDomain;
import fr.xephi.authme.libs.waffle.windows.auth.impl.WindowsAccountImpl;
import fr.xephi.authme.libs.waffle.windows.auth.impl.WindowsAuthProviderImpl;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WaffleInfo {
   private static final Logger LOGGER = LoggerFactory.getLogger(WaffleInfo.class);

   public Document getWaffleInfo() throws ParserConfigurationException {
      DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
      df.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
      df.setAttribute("http://javax.xml.XMLConstants/property/accessExternalSchema", "");
      df.setExpandEntityReferences(false);
      Document doc = df.newDocumentBuilder().newDocument();
      Element root = doc.createElement("fr.xephi.authme.libs.waffle");
      String version = WaffleInfo.class.getPackage().getImplementationVersion();
      if (version != null) {
         root.setAttribute("version", version);
      }

      version = Platform.class.getPackage().getImplementationVersion();
      if (version != null) {
         root.setAttribute("jna", version);
      }

      version = WindowUtils.class.getPackage().getImplementationVersion();
      if (version != null) {
         root.setAttribute("jna-platform", version);
      }

      doc.appendChild(root);
      root.appendChild(this.getAuthProviderInfo(doc));
      return doc;
   }

   protected Element getAuthProviderInfo(Document doc) {
      IWindowsAuthProvider auth = new WindowsAuthProviderImpl();
      Element node = doc.createElement("auth");
      node.setAttribute("class", auth.getClass().getName());
      Element child = doc.createElement("currentUser");
      node.appendChild(child);
      String currentUsername = WindowsAccountImpl.getCurrentUsername();
      this.addAccountInfo(doc, child, new WindowsAccountImpl(currentUsername));
      child = doc.createElement("computer");
      node.appendChild(child);
      IWindowsComputer c = auth.getCurrentComputer();
      Element value = doc.createElement("computerName");
      value.setTextContent(c.getComputerName());
      child.appendChild(value);
      value = doc.createElement("memberOf");
      value.setTextContent(c.getMemberOf());
      child.appendChild(value);
      value = doc.createElement("joinStatus");
      value.setTextContent(c.getJoinStatus());
      child.appendChild(value);
      value = doc.createElement("groups");
      String[] var9 = c.getGroups();
      int var10 = var9.length;

      int var11;
      for(var11 = 0; var11 < var10; ++var11) {
         String s = var9[var11];
         Element g = doc.createElement("group");
         g.setTextContent(s);
         value.appendChild(g);
      }

      child.appendChild(value);
      if (Netapi32Util.getJoinStatus() == 3) {
         child = doc.createElement("domains");
         node.appendChild(child);
         IWindowsDomain[] var15 = auth.getDomains();
         var11 = var15.length;

         for(int var16 = 0; var16 < var11; ++var16) {
            IWindowsDomain domain = var15[var16];
            Element d = doc.createElement("domain");
            node.appendChild(d);
            value = doc.createElement("FQN");
            value.setTextContent(domain.getFqn());
            child.appendChild(value);
            value = doc.createElement("TrustTypeString");
            value.setTextContent(domain.getTrustTypeString());
            child.appendChild(value);
            value = doc.createElement("TrustDirectionString");
            value.setTextContent(domain.getTrustDirectionString());
            child.appendChild(value);
         }
      }

      return node;
   }

   protected void addAccountInfo(Document doc, Element node, IWindowsAccount account) {
      Element value = doc.createElement("Name");
      value.setTextContent(account.getName());
      node.appendChild(value);
      value = doc.createElement("FQN");
      value.setTextContent(account.getFqn());
      node.appendChild(value);
      value = doc.createElement("Domain");
      value.setTextContent(account.getDomain());
      node.appendChild(value);
      value = doc.createElement("SID");
      value.setTextContent(account.getSidString());
      node.appendChild(value);
   }

   public Element getLookupInfo(Document doc, String lookup) {
      IWindowsAuthProvider auth = new WindowsAuthProviderImpl();
      Element node = doc.createElement("lookup");
      node.setAttribute("name", lookup);

      try {
         this.addAccountInfo(doc, node, auth.lookupAccount(lookup));
      } catch (Win32Exception var6) {
         node.appendChild(getException(doc, var6));
      }

      return node;
   }

   public static Element getException(Document doc, Exception t) {
      Element node = doc.createElement("exception");
      node.setAttribute("class", t.getClass().getName());
      Element value = doc.createElement("message");
      if (t.getMessage() != null) {
         value.setTextContent(t.getMessage());
         node.appendChild(value);
      }

      value = doc.createElement("trace");
      value.setTextContent(Arrays.toString(t.getStackTrace()));
      node.appendChild(value);
      return node;
   }

   public static String toPrettyXML(Document doc) throws TransformerException {
      TransformerFactory transfac = TransformerFactory.newInstance();
      transfac.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
      transfac.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet", "");
      Transformer trans = transfac.newTransformer();
      trans.setOutputProperty("omit-xml-declaration", "yes");
      trans.setOutputProperty("indent", "yes");
      StringWriter sw = new StringWriter();
      StreamResult result = new StreamResult(sw);
      DOMSource source = new DOMSource(doc);
      trans.transform(source, result);
      return sw.toString();
   }

   public static void main(String[] args) {
      boolean show = false;
      List<String> lookup = new ArrayList();
      if (args != null) {
         for(int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (null != arg) {
               byte var6 = -1;
               switch(arg.hashCode()) {
               case 45087914:
                  if (arg.equals("-show")) {
                     var6 = 0;
                  }
                  break;
               case 185865191:
                  if (arg.equals("-lookup")) {
                     var6 = 1;
                  }
               }

               switch(var6) {
               case 0:
                  show = true;
                  break;
               case 1:
                  ++i;
                  lookup.add(args[i]);
                  break;
               default:
                  LOGGER.error((String)"Unknown Argument: {}", (Object)arg);
                  throw new RuntimeException("Unknown Argument: " + arg);
               }
            }
         }
      }

      WaffleInfo helper = new WaffleInfo();

      try {
         Document info = helper.getWaffleInfo();
         Iterator var5 = lookup.iterator();

         while(var5.hasNext()) {
            String name = (String)var5.next();
            info.getDocumentElement().appendChild(helper.getLookupInfo(info, name));
         }

         String xml = toPrettyXML(info);
         if (show) {
            File f = Files.createTempFile("fr.xephi.authme.libs.waffle-info-", ".xml").toFile();
            Files.write(f.toPath(), xml.getBytes(StandardCharsets.UTF_8), new OpenOption[]{StandardOpenOption.APPEND});
            Desktop.getDesktop().open(f);
         } else {
            LOGGER.info(xml);
         }
      } catch (TransformerException | ParserConfigurationException | IOException var7) {
         LOGGER.error(var7.getMessage());
         LOGGER.trace((String)"", (Throwable)var7);
      }

   }
}
