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
    CustomerBean customerBean = new CustomerBean();

    @EJB
    OccurrenceBean occurrenceBean = new OccurrenceBean();

    private static final Logger LOGGER = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void PopulateDB() {
        System.out.println("Hello Java EE!");

        try {
            customerBean.create("289910323", "João", "joao@email.com", "123456");
            customerBean.create("289910324", "Maria", "maria@email.com", "123456");
            customerBean.create("289910325", "Pedro", "pedro@email.com", "123456");

            occurrenceBean.create("Occurrence 1", "Open", "289910323");
            occurrenceBean.create("Occurrence 2", "Pending", "289910324");
            occurrenceBean.create("Occurrence 3", "Open", "289910325");
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

    }
}
