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
        System.out.println("Hello, Java EE!");

        try {
            // Customers
            customerBean.create("289910323", "João", "joao@email.com", "123456");
            customerBean.create("289910324", "Maria", "maria@email.com", "123456");
            customerBean.create("289910325", "Pedro", "pedro@email.com", "123456");

            // Occurrences
            occurrenceBean.create("Occurrence 1", "pending", "289910323");
            occurrenceBean.create("Occurrence 2", "approved", "289910323");
            occurrenceBean.create("Occurrence 3", "rejected", "289910323");

            occurrenceBean.create("Occurrence 4", "solved", "289910324");
            occurrenceBean.create("Occurrence 5", "pending", "289910324");
            occurrenceBean.create("Occurrence 6", "pending", "289910324");

            occurrenceBean.create("Occurrence 7", "approved", "289910325");
            occurrenceBean.create("Occurrence 8", "rejected", "289910325");
            occurrenceBean.create("Occurrence 9", "approved", "289910325");
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

    }
}
