/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sun.jdi.Bootstrap
 *  com.sun.jdi.ReferenceType
 *  com.sun.jdi.VirtualMachine
 *  com.sun.jdi.connect.AttachingConnector
 *  com.sun.jdi.connect.Connector
 *  com.sun.jdi.connect.Connector$Argument
 *  com.sun.jdi.event.Event
 *  com.sun.jdi.event.EventIterator
 *  com.sun.jdi.event.EventQueue
 *  com.sun.jdi.event.EventSet
 *  com.sun.jdi.event.MethodEntryEvent
 *  com.sun.jdi.request.EventRequest
 *  com.sun.jdi.request.EventRequestManager
 *  com.sun.jdi.request.MethodEntryRequest
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.Trigger;

public class HotSwapper {
    private VirtualMachine jvm = null;
    private MethodEntryRequest request = null;
    private Map<ReferenceType, byte[]> newClassFiles = null;
    private Trigger trigger = new Trigger();
    private static final String HOST_NAME = "localhost";
    private static final String TRIGGER_NAME = Trigger.class.getName();

    public HotSwapper(int n) {
        this(Integer.toString(n));
    }

    public HotSwapper(String string) {
        AttachingConnector attachingConnector = (AttachingConnector)this.findConnector("com.sun.jdi.SocketAttach");
        Map map = attachingConnector.defaultArguments();
        ((Connector.Argument)map.get("hostname")).setValue(HOST_NAME);
        ((Connector.Argument)map.get("port")).setValue(string);
        this.jvm = attachingConnector.attach(map);
        EventRequestManager eventRequestManager = this.jvm.eventRequestManager();
        this.request = HotSwapper.methodEntryRequests(eventRequestManager, TRIGGER_NAME);
    }

    private Connector findConnector(String string) {
        List list = Bootstrap.virtualMachineManager().allConnectors();
        for (Connector connector : list) {
            if (!connector.name().equals(string)) continue;
            return connector;
        }
        throw new IOException("Not found: " + string);
    }

    private static MethodEntryRequest methodEntryRequests(EventRequestManager eventRequestManager, String string) {
        MethodEntryRequest methodEntryRequest = eventRequestManager.createMethodEntryRequest();
        methodEntryRequest.addClassFilter(string);
        methodEntryRequest.setSuspendPolicy(1);
        return methodEntryRequest;
    }

    private void deleteEventRequest(EventRequestManager eventRequestManager, MethodEntryRequest methodEntryRequest) {
        eventRequestManager.deleteEventRequest((EventRequest)methodEntryRequest);
    }

    public void reload(String string, byte[] byArray) {
        ReferenceType referenceType = this.toRefType(string);
        HashMap<ReferenceType, byte[]> hashMap = new HashMap<ReferenceType, byte[]>();
        hashMap.put(referenceType, byArray);
        this.reload2(hashMap, string);
    }

    public void reload(Map<String, byte[]> map) {
        HashMap<ReferenceType, byte[]> hashMap = new HashMap<ReferenceType, byte[]>();
        String string = null;
        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            string = entry.getKey();
            hashMap.put(this.toRefType(string), entry.getValue());
        }
        if (string != null) {
            this.reload2(hashMap, string + " etc.");
        }
    }

    private ReferenceType toRefType(String string) {
        List list = this.jvm.classesByName(string);
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("no such class: " + string);
        }
        return (ReferenceType)list.get(0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void reload2(Map<ReferenceType, byte[]> map, String string) {
        Trigger trigger = this.trigger;
        synchronized (trigger) {
            this.startDaemon();
            this.newClassFiles = map;
            this.request.enable();
            this.trigger.doSwap();
            this.request.disable();
            Map<ReferenceType, byte[]> map2 = this.newClassFiles;
            if (map2 != null) {
                this.newClassFiles = null;
                throw new RuntimeException("failed to reload: " + string);
            }
        }
    }

    private void startDaemon() {
        new Thread(){

            private void errorMsg(Throwable throwable) {
                System.err.print("Exception in thread \"HotSwap\" ");
                throwable.printStackTrace(System.err);
            }

            @Override
            public void run() {
                EventSet eventSet = null;
                try {
                    eventSet = HotSwapper.this.waitEvent();
                    EventIterator eventIterator = eventSet.eventIterator();
                    while (eventIterator.hasNext()) {
                        Event event = eventIterator.nextEvent();
                        if (!(event instanceof MethodEntryEvent)) continue;
                        HotSwapper.this.hotswap();
                        break;
                    }
                } catch (Throwable throwable) {
                    this.errorMsg(throwable);
                }
                try {
                    if (eventSet != null) {
                        eventSet.resume();
                    }
                } catch (Throwable throwable) {
                    this.errorMsg(throwable);
                }
            }
        }.start();
    }

    EventSet waitEvent() {
        EventQueue eventQueue = this.jvm.eventQueue();
        return eventQueue.remove();
    }

    void hotswap() {
        Map<ReferenceType, byte[]> map = this.newClassFiles;
        this.jvm.redefineClasses(map);
        this.newClassFiles = null;
    }
}

