package fr.xephi.authme.libs.org.postgresql.hostchooser;

import java.util.Iterator;

public interface HostChooser extends Iterable<CandidateHost> {
   Iterator<CandidateHost> iterator();
}
