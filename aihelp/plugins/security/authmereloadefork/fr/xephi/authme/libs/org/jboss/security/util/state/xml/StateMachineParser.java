package fr.xephi.authme.libs.org.jboss.security.util.state.xml;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.util.state.State;
import fr.xephi.authme.libs.org.jboss.security.util.state.StateMachine;
import fr.xephi.authme.libs.org.jboss.security.util.state.Transition;
import fr.xephi.authme.libs.org.jboss.security.util.xml.DOMUtils;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StateMachineParser {
   public StateMachine parse(URL source) throws Exception {
      InputStream in = null;
      Element root = null;

      try {
         in = source.openConnection().getInputStream();
         root = DOMUtils.parse(in);
      } finally {
         this.safeClose(in);
      }

      String description = root.getAttribute("description");
      HashMap nameToStateMap = new HashMap();
      HashMap nameToTransitionsMap = new HashMap();
      HashSet states = new HashSet();
      State startState = null;
      NodeList stateList = root.getChildNodes();

      for(int i = 0; i < stateList.getLength(); ++i) {
         Node stateNode = stateList.item(i);
         if (stateNode.getNodeName().equals("state")) {
            Element stateElement = (Element)stateNode;
            String stateName = stateElement.getAttribute("name");
            State s = new State(stateName);
            states.add(s);
            nameToStateMap.put(stateName, s);
            HashMap transitions = new HashMap();
            NodeList transitionList = stateElement.getChildNodes();

            for(int j = 0; j < transitionList.getLength(); ++j) {
               Node transitionNode = transitionList.item(j);
               if (transitionNode.getNodeName().equals("transition")) {
                  Element transitionElement = (Element)transitionNode;
                  String name = transitionElement.getAttribute("name");
                  String targetName = transitionElement.getAttribute("target");
                  transitions.put(name, targetName);
               }
            }

            nameToTransitionsMap.put(stateName, transitions);
            if (Boolean.valueOf(stateElement.getAttribute("isStartState")) == Boolean.TRUE) {
               startState = s;
            }
         }
      }

      Iterator transitions = nameToTransitionsMap.keySet().iterator();
      StringBuffer resolveFailed = new StringBuffer();

      while(transitions.hasNext()) {
         String stateName = (String)transitions.next();
         State s = (State)nameToStateMap.get(stateName);
         HashMap stateTransitions = (HashMap)nameToTransitionsMap.get(stateName);
         Iterator it = stateTransitions.keySet().iterator();

         while(it.hasNext()) {
            String name = (String)it.next();
            String targetName = (String)stateTransitions.get(name);
            State target = (State)nameToStateMap.get(targetName);
            if (target == null) {
               resolveFailed.append(PicketBoxMessages.MESSAGES.failedToResolveTargetStateMessage(targetName, name));
            }

            Transition t = new Transition(name, target);
            s.addTransition(t);
         }
      }

      if (resolveFailed.length() > 0) {
         throw new Exception(resolveFailed.toString());
      } else {
         StateMachine sm = new StateMachine(states, startState, description);
         return sm;
      }
   }

   private void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var3) {
      }

   }
}
