package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class Versionable {
    @Version
    private int version;
}
