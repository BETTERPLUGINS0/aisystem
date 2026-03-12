package fr.xephi.authme.libs.waffle.windows.auth.impl;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Netapi32Util;
import com.sun.jna.platform.win32.Secur32;
import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.SspiUtil;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import fr.xephi.authme.libs.waffle.util.cache.Cache;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAccount;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAuthProvider;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsComputer;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsCredentialsHandle;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsDomain;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsSecurityContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class WindowsAuthProviderImpl implements IWindowsAuthProvider {
   public static final int CONTINUE_CONTEXT_TIMEOUT = 30;
   private final Cache<String, WindowsAuthProviderImpl.ContinueContext> continueContexts;

   public WindowsAuthProviderImpl() {
      this(30);
   }

   public WindowsAuthProviderImpl(int continueContextsTimeout) {
      this.continueContexts = Cache.newCache(continueContextsTimeout);
   }

   public IWindowsSecurityContext acceptSecurityToken(String connectionId, byte[] token, String securityPackage) {
      if (token != null && token.length != 0) {
         Sspi.CtxtHandle continueHandle = null;
         WindowsAuthProviderImpl.ContinueContext continueContext = (WindowsAuthProviderImpl.ContinueContext)this.continueContexts.get(connectionId);
         Object serverCredential;
         if (continueContext != null) {
            continueHandle = continueContext.continueHandle;
            serverCredential = continueContext.serverCredential;
         } else {
            serverCredential = new WindowsCredentialsHandleImpl((String)null, 1, securityPackage);
            ((IWindowsCredentialsHandle)serverCredential).initialize();
         }

         int tokenSize = Sspi.MAX_TOKEN_SIZE;

         WindowsSecurityContextImpl sc;
         int rc;
         do {
            SspiUtil.ManagedSecBufferDesc pbServerToken = new SspiUtil.ManagedSecBufferDesc(2, tokenSize);
            SspiUtil.ManagedSecBufferDesc pbClientToken = new SspiUtil.ManagedSecBufferDesc(2, token);
            IntByReference pfClientContextAttr = new IntByReference();
            Sspi.CtxtHandle phNewServerContext = new Sspi.CtxtHandle();
            rc = Secur32.INSTANCE.AcceptSecurityContext(((IWindowsCredentialsHandle)serverCredential).getHandle(), continueHandle, pbClientToken, 2048, 16, phNewServerContext, pbServerToken, pfClientContextAttr, (Sspi.TimeStamp)null);
            sc = new WindowsSecurityContextImpl();
            sc.setCredentialsHandle((IWindowsCredentialsHandle)serverCredential);
            sc.setSecurityPackage(securityPackage);
            sc.setSecurityContext(phNewServerContext);
            switch(rc) {
            case -2146893023:
               tokenSize += Sspi.MAX_TOKEN_SIZE;
               sc.dispose();
               WindowsSecurityContextImpl.dispose(continueHandle);
               break;
            case 0:
               this.resetSecurityToken(connectionId);
               if (pbServerToken.pBuffers != null && pbServerToken.cBuffers == 1 && pbServerToken.getBuffer(0).cbBuffer > 0) {
                  sc.setToken(pbServerToken.getBuffer(0).getBytes() == null ? new byte[0] : (byte[])pbServerToken.getBuffer(0).getBytes().clone());
               }

               sc.setContinue(false);
               break;
            case 590610:
               continueContext = new WindowsAuthProviderImpl.ContinueContext(phNewServerContext, (IWindowsCredentialsHandle)serverCredential);
               this.continueContexts.put(connectionId, continueContext);
               sc.setToken(pbServerToken.getBuffer(0).getBytes() == null ? new byte[0] : (byte[])pbServerToken.getBuffer(0).getBytes().clone());
               sc.setContinue(true);
               break;
            default:
               sc.dispose();
               WindowsSecurityContextImpl.dispose(continueHandle);
               this.resetSecurityToken(connectionId);
               throw new Win32Exception(rc);
            }
         } while(rc == -2146893023);

         return sc;
      } else {
         this.resetSecurityToken(connectionId);
         throw new Win32Exception(-2146893048);
      }
   }

   public IWindowsComputer getCurrentComputer() {
      try {
         return new WindowsComputerImpl(InetAddress.getLocalHost().getHostName());
      } catch (UnknownHostException var2) {
         throw new RuntimeException(var2);
      }
   }

   public IWindowsDomain[] getDomains() {
      List<IWindowsDomain> domains = new ArrayList();
      Netapi32Util.DomainTrust[] trusts = Netapi32Util.getDomainTrusts();
      Netapi32Util.DomainTrust[] var3 = trusts;
      int var4 = trusts.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Netapi32Util.DomainTrust trust = var3[var5];
         domains.add(new WindowsDomainImpl(trust));
      }

      return (IWindowsDomain[])domains.toArray(new IWindowsDomain[0]);
   }

   public IWindowsIdentity logonDomainUser(String username, String domain, String password) {
      return this.logonDomainUserEx(username, domain, password, 3, 0);
   }

   public IWindowsIdentity logonDomainUserEx(String username, String domain, String password, int logonType, int logonProvider) {
      WinNT.HANDLEByReference phUser = new WinNT.HANDLEByReference();
      if (!Advapi32.INSTANCE.LogonUser(username, domain, password, logonType, logonProvider, phUser)) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      } else {
         return new WindowsIdentityImpl(phUser.getValue());
      }
   }

   public IWindowsIdentity logonUser(String username, String password) {
      String[] userNameDomain = username.split("\\\\", 2);
      return userNameDomain.length == 2 ? this.logonDomainUser(userNameDomain[1], userNameDomain[0], password) : this.logonDomainUser(username, (String)null, password);
   }

   public IWindowsAccount lookupAccount(String username) {
      return new WindowsAccountImpl(username);
   }

   public void resetSecurityToken(String connectionId) {
      this.continueContexts.remove(connectionId);
   }

   public int getContinueContextsSize() {
      return this.continueContexts.size();
   }

   private static class ContinueContext {
      Sspi.CtxtHandle continueHandle;
      IWindowsCredentialsHandle serverCredential;

      public ContinueContext(Sspi.CtxtHandle handle, IWindowsCredentialsHandle windowsCredential) {
         this.continueHandle = handle;
         this.serverCredential = windowsCredential;
      }
   }
}
