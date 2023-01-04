package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Logger;

@Singleton
@Startup
public class ConfigBean {
    @EJB
    OccurrenceBean occurrenceBean = new OccurrenceBean();

    private static final Logger LOGGER = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void PopulateDB() {
        System.out.println("Hello Java EE!");

        occurrenceBean.create(1, "Occurrence 1", "Open");
        occurrenceBean.create(2, "Occurrence 2", "Open");
        occurrenceBean.create(3, "Occurrence 3", "Open");
        occurrenceBean.create(4, "Occurrence 4", "Open");
    }
}
