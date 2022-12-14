package pt.ipleiria.estg.dei.ei.dae.daeproject.ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Logger;

@Singleton
@Startup
public class ConfigBean {

    private static final Logger LOGGER = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void PopulateDB() {
        System.out.println("Hello Java EE!");
    }
}