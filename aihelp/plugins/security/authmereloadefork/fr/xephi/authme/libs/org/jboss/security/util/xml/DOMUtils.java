package fr.xephi.authme.libs.org.jboss.security.util.xml;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.util.StringPropertyReplacer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class DOMUtils {
   private static ThreadLocal documentThreadLocal = new ThreadLocal();
   private static ThreadLocal builderThreadLocal = new ThreadLocal() {
      protected Object initialValue() {
         try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new JBossEntityResolver());
            return builder;
         } catch (ParserConfigurationException var3) {
            throw PicketBoxMessages.MESSAGES.failedToCreateDocumentBuilder(var3);
         }
      }
   };

   private DOMUtils() {
   }

   public static DocumentBuilder getDocumentBuilder() {
      DocumentBuilder builder = (DocumentBuilder)builderThreadLocal.get();
      return builder;
   }

   public static Element parse(String xmlString) throws IOException {
      return parse((InputStream)(new ByteArrayInputStream(xmlString.getBytes("UTF-8"))));
   }

   public static Element parse(InputStream xmlStream) throws IOException {
      try {
         Document doc = getDocumentBuilder().parse(xmlStream);
         Element root = doc.getDocumentElement();
         return root;
      } catch (SAXException var3) {
         throw new IOException(var3);
      }
   }

   public static Element parse(InputSource source) throws IOException {
      try {
         Document doc = getDocumentBuilder().parse(source);
         Element root = doc.getDocumentElement();
         return root;
      } catch (SAXException var3) {
         throw new IOException(var3);
      }
   }

   public static Element createElement(String localPart) {
      Document doc = getOwnerDocument();
      return doc.createElement(localPart);
   }

   public static Element createElement(String localPart, String prefix) {
      Document doc = getOwnerDocument();
      return doc.createElement(prefix + ":" + localPart);
   }

   public static Element createElement(String localPart, String prefix, String uri) {
      Document doc = getOwnerDocument();
      return prefix != null && prefix.length() != 0 ? doc.createElementNS(uri, prefix + ":" + localPart) : doc.createElementNS(uri, localPart);
   }

   public static Element createElement(QName qname) {
      return createElement(qname.getLocalPart(), qname.getPrefix(), qname.getNamespaceURI());
   }

   public static Text createTextNode(String value) {
      Document doc = getOwnerDocument();
      return doc.createTextNode(value);
   }

   public static QName getElementQName(Element el) {
      String qualifiedName = el.getNodeName();
      return resolveQName(el, qualifiedName);
   }

   public static QName resolveQName(Element el, String qualifiedName) {
      String prefix = "";
      String namespaceURI = "";
      String localPart = qualifiedName;
      int colIndex = qualifiedName.indexOf(":");
      if (colIndex > 0) {
         prefix = qualifiedName.substring(0, colIndex);
         localPart = qualifiedName.substring(colIndex + 1);
         if ("xmlns".equals(prefix)) {
            namespaceURI = "URI:XML_PREDEFINED_NAMESPACE";
         } else {
            Element nsElement = el;

            while(namespaceURI.equals("") && nsElement != null) {
               namespaceURI = nsElement.getAttribute("xmlns:" + prefix);
               if (namespaceURI.equals("")) {
                  nsElement = getParentElement(nsElement);
               }
            }
         }

         if (namespaceURI.equals("")) {
            throw PicketBoxMessages.MESSAGES.failedToFindNamespaceURI(qualifiedName);
         }
      }

      QName qname = new QName(namespaceURI, localPart, prefix);
      return qname;
   }

   public static String getAttributeValue(Element el, String attrName) {
      return getAttributeValue(el, new QName(attrName));
   }

   public static String getAttributeValue(Element el, QName attrName) {
      String attr = null;
      if ("".equals(attrName.getNamespaceURI())) {
         attr = el.getAttribute(attrName.getLocalPart());
      } else {
         attr = el.getAttributeNS(attrName.getNamespaceURI(), attrName.getLocalPart());
      }

      if ("".equals(attr)) {
         attr = null;
      }

      return attr;
   }

   public static QName getAttributeValueAsQName(Element el, String attrName) {
      return getAttributeValueAsQName(el, new QName(attrName));
   }

   public static QName getAttributeValueAsQName(Element el, QName attrName) {
      QName qname = null;
      String qualifiedName = getAttributeValue(el, attrName);
      if (qualifiedName != null) {
         qname = resolveQName(el, qualifiedName);
      }

      return qname;
   }

   public static boolean getAttributeValueAsBoolean(Element el, String attrName) {
      return getAttributeValueAsBoolean(el, new QName(attrName));
   }

   public static boolean getAttributeValueAsBoolean(Element el, QName attrName) {
      String attrVal = getAttributeValue(el, attrName);
      boolean ret = "true".equalsIgnoreCase(attrVal) || "1".equalsIgnoreCase(attrVal);
      return ret;
   }

   public static Integer getAttributeValueAsInteger(Element el, String attrName) {
      return getAttributeValueAsInteger(el, new QName(attrName));
   }

   public static Integer getAttributeValueAsInteger(Element el, QName attrName) {
      String attrVal = getAttributeValue(el, attrName);
      return attrVal != null ? new Integer(attrVal) : null;
   }

   public static Map getAttributes(Element el) {
      Map attmap = new HashMap();
      NamedNodeMap attribs = el.getAttributes();

      for(int i = 0; i < attribs.getLength(); ++i) {
         Attr attr = (Attr)attribs.item(i);
         String name = attr.getName();
         QName qname = resolveQName(el, name);
         String value = attr.getNodeValue();
         attmap.put(qname, value);
      }

      return attmap;
   }

   public static void copyAttributes(Element destElement, Element srcElement) {
      NamedNodeMap attribs = srcElement.getAttributes();

      for(int i = 0; i < attribs.getLength(); ++i) {
         Attr attr = (Attr)attribs.item(i);
         String uri = attr.getNamespaceURI();
         String qname = attr.getName();
         String value = attr.getNodeValue();
         if (uri == null && qname.startsWith("xmlns")) {
            PicketBoxLogger.LOGGER.traceIgnoreXMLAttribute(uri, qname, value);
         } else {
            destElement.setAttributeNS(uri, qname, value);
         }
      }

   }

   public static boolean hasChildElements(Node node) {
      NodeList nlist = node.getChildNodes();

      for(int i = 0; i < nlist.getLength(); ++i) {
         Node child = nlist.item(i);
         if (child.getNodeType() == 1) {
            return true;
         }
      }

      return false;
   }

   public static Iterator getChildElements(Node node) {
      ArrayList list = new ArrayList();
      NodeList nlist = node.getChildNodes();

      for(int i = 0; i < nlist.getLength(); ++i) {
         Node child = nlist.item(i);
         if (child.getNodeType() == 1) {
            list.add(child);
         }
      }

      return list.iterator();
   }

   public static String getTextContent(Node node) {
      return getTextContent(node, false);
   }

   public static String getTextContent(Node node, boolean replaceProps) {
      boolean hasTextContent = false;
      StringBuffer buffer = new StringBuffer();
      NodeList nlist = node.getChildNodes();

      for(int i = 0; i < nlist.getLength(); ++i) {
         Node child = nlist.item(i);
         if (child.getNodeType() == 3) {
            buffer.append(child.getNodeValue());
            hasTextContent = true;
         }
      }

      String text = hasTextContent ? buffer.toString() : null;
      if (text != null && replaceProps) {
         text = StringPropertyReplacer.replaceProperties(text);
      }

      return text;
   }

   public static Element getFirstChildElement(Node node) {
      return getFirstChildElementIntern(node, (QName)null);
   }

   public static Element getFirstChildElement(Node node, String nodeName) {
      return getFirstChildElementIntern(node, new QName(nodeName));
   }

   public static Element getFirstChildElement(Node node, QName nodeName) {
      return getFirstChildElementIntern(node, nodeName);
   }

   private static Element getFirstChildElementIntern(Node node, QName nodeName) {
      Element childElement = null;
      Iterator it = getChildElementsIntern(node, nodeName);
      if (it.hasNext()) {
         childElement = (Element)it.next();
      }

      return childElement;
   }

   public static Iterator getChildElements(Node node, String nodeName) {
      return getChildElementsIntern(node, new QName(nodeName));
   }

   public static Iterator getChildElements(Node node, QName nodeName) {
      return getChildElementsIntern(node, nodeName);
   }

   private static Iterator getChildElementsIntern(Node node, QName nodeName) {
      ArrayList list = new ArrayList();
      NodeList nlist = node.getChildNodes();

      for(int i = 0; i < nlist.getLength(); ++i) {
         Node child = nlist.item(i);
         if (child.getNodeType() == 1) {
            if (nodeName == null) {
               list.add(child);
            } else {
               QName qname;
               if (nodeName.getNamespaceURI().length() > 0) {
                  qname = new QName(child.getNamespaceURI(), child.getLocalName());
               } else {
                  qname = new QName(child.getLocalName());
               }

               if (qname.equals(nodeName)) {
                  list.add(child);
               }
            }
         }
      }

      return list.iterator();
   }

   public static Element getParentElement(Node node) {
      Node parent = node.getParentNode();
      return parent instanceof Element ? (Element)parent : null;
   }

   public static Document getOwnerDocument() {
      Document doc = (Document)documentThreadLocal.get();
      if (doc == null) {
         doc = getDocumentBuilder().newDocument();
         documentThreadLocal.set(doc);
      }

      return doc;
   }
}
