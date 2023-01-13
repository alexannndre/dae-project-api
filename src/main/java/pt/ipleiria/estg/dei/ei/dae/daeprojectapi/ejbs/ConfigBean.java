package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.managers.PolicyManager;

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
    ExpertBean expertBean = new ExpertBean();

    @EJB
    AdministratorBean adminBean = new AdministratorBean();

    @EJB
    OccurrenceBean occurrenceBean = new OccurrenceBean();

    @EJB
    DocumentBean documentBean = new DocumentBean();

    @EJB
    ServiceBean serviceBean = new ServiceBean();

    private static final Logger LOGGER = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void PopulateDB() {
        System.out.println("Hello, Java EE!");

        try {
            // Customers
            customerBean.create("289910323", "João", "joao@email.com", "123456");
            customerBean.create("289910324", "Maria", "maria@email.com", "123456");
            customerBean.create("289910325", "Pedro", "pedro@email.com", "123456");
            customerBean.create("284542983", "Sofia", "Sofia@email.com", "123456");
            customerBean.create("530701258", "Driven Notice LLC", "legal@drivenotice.com", "123456");

            // Experts
            expertBean.create("123123123", "Elso Bas", "oque@bas.com", "123456");

            // Administrators
            adminBean.create("421189655", "Marco", "marco@email.com", "123456");

            // Services
            serviceBean.create("Mech & Mech", "vehicle");
            serviceBean.create("Oil Trooper", "vehicle");
            serviceBean.create("Tire Smash", "vehicle");
            serviceBean.create("Peter & Karl Home Repairs", "home");
            serviceBean.create("Innov Renovations", "home");
            serviceBean.create("Taj Repairs", "electronics");
            serviceBean.create("Phony Johnny", "electronics");

            // Occurrences
            occurrenceBean.create("Occurrence 1", "65510", PENDING, "289910323");
            occurrenceBean.create("Occurrence 2", "65389", PENDING, "289910323");
            occurrenceBean.create("Occurrence 3", "64820", REJECTED, "289910323", "123123123");

            occurrenceBean.create("Occurrence 4", "83649", PENDING, "289910324");
            occurrenceBean.create("Occurrence 5", "86084", PENDING, "289910324");
            occurrenceBean.create("Occurrence 6", "49838", APPROVED, "289910324", "123123123");

            occurrenceBean.create("Occurrence 7", "50864", PENDING, "289910325");
            occurrenceBean.create("Occurrence 8", "77380", PENDING, "289910325");
            occurrenceBean.create("Occurrence 9", "86084", SOLVED, "289910325", "123123123");


            // Documents
            //documentBean.create("/opt/jboss/uploads/occurrences/1/test.txt", "test.txt", 1L, "289910323");

            PolicyManager.customerBean = customerBean;
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

    }
}
