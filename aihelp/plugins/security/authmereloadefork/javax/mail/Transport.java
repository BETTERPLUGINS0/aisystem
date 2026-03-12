package javax.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

public abstract class Transport extends Service {
   private volatile Vector<TransportListener> transportListeners = null;

   public Transport(Session session, URLName urlname) {
      super(session, urlname);
   }

   public static void send(Message msg) throws MessagingException {
      msg.saveChanges();
      send0(msg, msg.getAllRecipients(), (String)null, (String)null);
   }

   public static void send(Message msg, Address[] addresses) throws MessagingException {
      msg.saveChanges();
      send0(msg, addresses, (String)null, (String)null);
   }

   public static void send(Message msg, String user, String password) throws MessagingException {
      msg.saveChanges();
      send0(msg, msg.getAllRecipients(), user, password);
   }

   public static void send(Message msg, Address[] addresses, String user, String password) throws MessagingException {
      msg.saveChanges();
      send0(msg, addresses, user, password);
   }

   private static void send0(Message msg, Address[] addresses, String user, String password) throws MessagingException {
      if (addresses != null && addresses.length != 0) {
         Map<String, List<Address>> protocols = new HashMap();
         List<Address> invalid = new ArrayList();
         List<Address> validSent = new ArrayList();
         List<Address> validUnsent = new ArrayList();

         int dsize;
         for(dsize = 0; dsize < addresses.length; ++dsize) {
            if (protocols.containsKey(addresses[dsize].getType())) {
               List<Address> v = (List)protocols.get(addresses[dsize].getType());
               v.add(addresses[dsize]);
            } else {
               List<Address> w = new ArrayList();
               w.add(addresses[dsize]);
               protocols.put(addresses[dsize].getType(), w);
            }
         }

         dsize = protocols.size();
         if (dsize == 0) {
            throw new SendFailedException("No recipient addresses");
         } else {
            Session s = msg.session != null ? msg.session : Session.getDefaultInstance(System.getProperties(), (Authenticator)null);
            Transport transport;
            if (dsize == 1) {
               transport = s.getTransport(addresses[0]);

               try {
                  if (user != null) {
                     transport.connect(user, password);
                  } else {
                     transport.connect();
                  }

                  transport.sendMessage(msg, addresses);
               } finally {
                  transport.close();
               }

            } else {
               MessagingException chainedEx = null;
               boolean sendFailed = false;
               Iterator var13 = protocols.values().iterator();

               while(true) {
                  Address[] c;
                  while(var13.hasNext()) {
                     List<Address> v = (List)var13.next();
                     c = new Address[v.size()];
                     v.toArray(c);
                     if ((transport = s.getTransport(c[0])) == null) {
                        for(int j = 0; j < c.length; ++j) {
                           invalid.add(c[j]);
                        }
                     } else {
                        try {
                           transport.connect();
                           transport.sendMessage(msg, c);
                        } catch (SendFailedException var30) {
                           sendFailed = true;
                           if (chainedEx == null) {
                              chainedEx = var30;
                           } else {
                              ((MessagingException)chainedEx).setNextException(var30);
                           }

                           Address[] a = var30.getInvalidAddresses();
                           int k;
                           if (a != null) {
                              for(k = 0; k < a.length; ++k) {
                                 invalid.add(a[k]);
                              }
                           }

                           a = var30.getValidSentAddresses();
                           if (a != null) {
                              for(k = 0; k < a.length; ++k) {
                                 validSent.add(a[k]);
                              }
                           }

                           Address[] c = var30.getValidUnsentAddresses();
                           if (c != null) {
                              for(int l = 0; l < c.length; ++l) {
                                 validUnsent.add(c[l]);
                              }
                           }
                        } catch (MessagingException var31) {
                           sendFailed = true;
                           if (chainedEx == null) {
                              chainedEx = var31;
                           } else {
                              ((MessagingException)chainedEx).setNextException(var31);
                           }
                        } finally {
                           transport.close();
                        }
                     }
                  }

                  if (!sendFailed && invalid.size() == 0 && validUnsent.size() == 0) {
                     return;
                  }

                  Address[] a = null;
                  Address[] b = null;
                  c = null;
                  if (validSent.size() > 0) {
                     a = new Address[validSent.size()];
                     validSent.toArray(a);
                  }

                  if (validUnsent.size() > 0) {
                     b = new Address[validUnsent.size()];
                     validUnsent.toArray(b);
                  }

                  if (invalid.size() > 0) {
                     c = new Address[invalid.size()];
                     invalid.toArray(c);
                  }

                  throw new SendFailedException("Sending failed", (Exception)chainedEx, a, b, c);
               }
            }
         }
      } else {
         throw new SendFailedException("No recipient addresses");
      }
   }

   public abstract void sendMessage(Message var1, Address[] var2) throws MessagingException;

   public synchronized void addTransportListener(TransportListener l) {
      if (this.transportListeners == null) {
         this.transportListeners = new Vector();
      }

      this.transportListeners.addElement(l);
   }

   public synchronized void removeTransportListener(TransportListener l) {
      if (this.transportListeners != null) {
         this.transportListeners.removeElement(l);
      }

   }

   protected void notifyTransportListeners(int type, Address[] validSent, Address[] validUnsent, Address[] invalid, Message msg) {
      if (this.transportListeners != null) {
         TransportEvent e = new TransportEvent(this, type, validSent, validUnsent, invalid, msg);
         this.queueEvent(e, this.transportListeners);
      }
   }
}
