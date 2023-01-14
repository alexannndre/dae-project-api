package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.helpers;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.IdentifiableDTO;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class ImportDataHelper {

    public static <T, D extends IdentifiableDTO<T>, E> Response importData(MultipartFormDataInput input, Function<String[], D> from, E bean){
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        List<InputPart> inputParts = uploadForm.get("file");

        if (inputParts == null)
            return Response.status(BAD_REQUEST).entity(new ErrorDTO("The \"file\" field is required")).build();

        List<D> list;

        try{
            list = CsvHelper.loadCsv(inputParts, from);
        }catch(Exception e){
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(e.getMessage())).build();
        }

        int count=list.size(),created=0,updated=0;

        if(count==0)
            return Response.status(BAD_REQUEST).entity(new ErrorDTO("No processable entities were found in that file")).build();

        for (D dto : list) {
            if(occurrenceBean.find(dto.getId()) == null){
                occurrenceBean.create(dto);
                created++;
            }else{
                occurrenceBean.update(dto);
                updated++;
            }
        }
        return Response.ok(String.format("Success! Processed %d entities; Created %d new entities; Updated %d entities;", count, created, updated)).build();
    }
}
