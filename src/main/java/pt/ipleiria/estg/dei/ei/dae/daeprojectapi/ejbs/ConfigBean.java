package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Logger;

import static pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status.*;

@Singleton
@Startup
public class ConfigBean {
    @EJB
    CustomerBean customerBean = new CustomerBean();

    @EJB
    OccurrenceBean occurrenceBean = new OccurrenceBean();

    @EJB
    DocumentBean documentBean = new DocumentBean();

    private static final Logger LOGGER = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void PopulateDB() {
        System.out.println("Hello, Java EE!");

        try {
            // Customers
            customerBean.create("289910323", "João", "joao@email.com", "123456");
            customerBean.create("289910324", "Maria", "maria@email.com", "123456");
            customerBean.create("289910325", "Pedro", "pedro@email.com", "123456");
            customerBean.create("530701258", "Driven Notice LLC", "legal@drivenotice.com", "123456");

            // Occurrences
            occurrenceBean.create("Occurrence 1", PENDING, "289910323");
            occurrenceBean.create("Occurrence 2", APPROVED, "289910323");
            occurrenceBean.create("Occurrence 3", REJECTED, "289910323");

            occurrenceBean.create("Occurrence 4", SOLVED, "289910324");
            occurrenceBean.create("Occurrence 5", PENDING, "289910324");
            occurrenceBean.create("Occurrence 6", PENDING, "289910324");

            occurrenceBean.create("Occurrence 7", APPROVED, "289910325");
            occurrenceBean.create("Occurrence 8", REJECTED, "289910325");
            occurrenceBean.create("Occurrence 9", APPROVED, "289910325");

            // Documents
            documentBean.create("/opt/jboss/uploads/occurrences/1/doc1.txt", "doc1.txt", 1L);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

    }
}
