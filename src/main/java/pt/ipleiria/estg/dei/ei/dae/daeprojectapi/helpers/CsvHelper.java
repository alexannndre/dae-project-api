package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.helpers;

import com.opencsv.CSVReader;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.DocumentDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.OccurrenceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ServiceDTO;
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

    public static <T> List<T> loadCsv(MultipartFormDataInput input, Function<String[], T> from) throws Exception {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        List<InputPart> inputParts = uploadForm.get("file");

        if (inputParts == null) {
            var msg = "The \"file\" field is required";
            throw new Exception(msg);
        }
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

    private static boolean isStringNull(String[] o, int idx){
        if(idx >= o.length)
            return true;
        String s = String.valueOf(o[idx]);
        return s == null || s.isEmpty() || s.equalsIgnoreCase("null");
    }

    public static OccurrenceDTO toOccurrence(String[] arr){
        if(arr.length < 4)
            throw new IllegalArgumentException("Every occurrence in the csv file must have at least 4 columns");
        return new OccurrenceDTO(-1L, arr[0], arr[1], Status.valueOf(arr[2]), arr[3],
                isStringNull(arr, 4)?null:arr[4],
                isStringNull(arr, 5)?null:Long.parseLong(arr[5]),
                isStringNull(arr, 6)?null:arr[6]
        );
    }

    public static ServiceDTO toService(String[] arr){
        if(arr.length < 3)
            throw new IllegalArgumentException("Every service in the csv file must have at least 3 columns");
        String customerVat = isStringNull(arr, 3)?null:arr[3];
        boolean isOfficial = customerVat==null||(!isStringNull(arr, 4)&&Boolean.parseBoolean(arr[4]));
        return new ServiceDTO(-1L, arr[0], arr[1], arr[2], customerVat,isOfficial);
    }
}
