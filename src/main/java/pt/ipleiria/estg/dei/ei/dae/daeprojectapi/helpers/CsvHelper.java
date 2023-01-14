package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.helpers;

import com.opencsv.CSVReader;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.DocumentDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.OccurrenceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.OccurrenceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.enums.Status;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

public class CsvHelper {

    public static <T> List<T> loadCsv(List<InputPart> inputParts, Function<String[], T> from) throws Exception {
        List<T> tStuff = new LinkedList<>();
        for (InputPart inputPart : inputParts) {
            var inputStream = inputPart.getBody(InputStream.class, null);
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));) {
                String[] values = null;
                while ((values = csvReader.readNext()) != null)
                    tStuff.add(from.apply(values));
            }catch(Exception e){
                e.printStackTrace();
                throw new Exception("An error has occurred while processing your csv file: " + e.getMessage());
            }
        }
        return tStuff;
    }

    private static boolean isStringNull(Object o){
        String s = String.valueOf(o);
        return s == null || s.isEmpty() || s.equalsIgnoreCase("null");
    }

    public static OccurrenceDTO toOccurrence(String[] arr){
        return new OccurrenceDTO(Long.parseLong(arr[0]), arr[1], arr[2], Status.valueOf(arr[3]), arr[4],
                isStringNull(arr[5])?null:arr[5],
                isStringNull(arr[6])?null:Long.parseLong(arr[6]),
                isStringNull(arr[7])?null:arr[7]
        );
    }
}
