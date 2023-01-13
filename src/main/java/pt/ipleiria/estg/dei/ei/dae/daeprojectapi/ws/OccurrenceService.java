package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.DocumentDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.OccurrenceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.DocumentBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.OccurrenceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Authenticated;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("occurrences")
@Produces({APPLICATION_JSON})  // injects header “Content-Type: application/json”
@Consumes({APPLICATION_JSON})  // injects header “Accept: application/json”
//@Authenticated
//@RolesAllowed({"Teacher", "Administrator"})
public class OccurrenceService {
    //    @EJB
//    private CustomerBean studentBean;

    @EJB
    private OccurrenceBean occurrenceBean;

    @EJB
    private DocumentBean documentBean;

    @Context
    private SecurityContext securityContext;

    private OccurrenceDTO toDTO(Occurrence occurrence) {
        return new OccurrenceDTO(
                occurrence.getId(),
                occurrence.getDescription(),
                occurrence.getPolicy(),
                occurrence.getStatus()
        );
    }

    private List<OccurrenceDTO> occurrencesToDTOs(List<Occurrence> occurrences) {
        return occurrences.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<OccurrenceDTO> all() {
        return occurrencesToDTOs(occurrenceBean.getAllOccurrences());
    }

    @GET
    @Path("pending")
    public List<OccurrenceDTO> pending() {
        return occurrencesToDTOs(occurrenceBean.getAllPendingOccurrences());
    }

    @GET
    @Path("approved")
    public List<OccurrenceDTO> approved() {
        return occurrencesToDTOs(occurrenceBean.getAllApprovedOccurrences());
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        var occurrence = occurrenceBean.findOrFail(id);
        return Response.ok(toDTO(occurrence)).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, OccurrenceDTO occurrenceDTO) {
        occurrenceBean.update(id, occurrenceDTO.getDescription(), occurrenceDTO.getStatus());
        occurrenceDTO = OccurrenceDTO.from(occurrenceBean.find(id));
        return Response.ok(occurrenceDTO).build();
    }
    
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        occurrenceBean.remove(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("{id}/approve")
    public Response approve(@PathParam("id") Long id) {
        try {
            occurrenceBean.approve(id);
            return Response.ok("This occurrence has been approved").build();
        } catch (IllegalStateException e) {
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(e.getMessage())).build();
        }
    }

    @PATCH
    @Path("{id}/reject")
    public Response reject(@PathParam("id") Long id) {
        try {
            occurrenceBean.reject(id);
            return Response.ok("This occurrence has been rejected").build();
        } catch (IllegalStateException e) {
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(e.getMessage())).build();
        }
    }

    // Documents
    @POST
    @Path("{id}/documents")
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    @Authenticated
    public Response upload(@PathParam("id") Long id, MultipartFormDataInput input) throws IOException {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        var vat = securityContext.getUserPrincipal().getName();

        List<InputPart> inputParts = uploadForm.get("file");

        if (inputParts == null) {
            var msg = "The \"file\" field is required";
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(msg)).build();
        }

        List<Document> documents = new LinkedList<>();

        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            String filename = getFileName(headers);

            // Convert the uploaded file to InputStream
            var inputStream = inputPart.getBody(InputStream.class, null);

            byte[] bytes = IOUtils.toByteArray(inputStream);

            String homedir = System.getProperty("user.home");
            String dirpath = homedir + File.separator + "uploads" + File.separator + "occurrences" + File.separator + id;

            mkdirIfNotExists(dirpath);

            String filepath = dirpath + File.separator + filename;
            writeFile(bytes, filepath);

            var document = documentBean.create(filepath, filename, id, vat);
            documents.add(document);
        }

        return Response.ok(DocumentDTO.from(documents)).build();
    }

    @GET
    @Path("{id}/documents/{documentId}")
    @Produces(APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("documentId") Long documentId) {
        var document = documentBean.findOrFail(documentId);
        var response = Response.ok(new File(document.getFilepath()));

        response.header("Content-Disposition", "attachment;filename=" + document.getFilename());

        return response.build();
    }

    @GET
    @Path("{id}/documents")
    @Produces(APPLICATION_JSON)
    public Response getDocuments(@PathParam("id") Long id) {
        var documents = documentBean.getOccurrenceDocuments(id);
        return Response.ok(DocumentDTO.from(documents)).build();
    }

    private void mkdirIfNotExists(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private String getFileName(MultivaluedMap<String, String> headers) {
        var contentDisposition = headers.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                return name[1].trim().replaceAll("\"", "");
            }
        }
        return "unknown";
    }

    private void writeFile(byte[] content, String filename) throws IOException {
        var file = new File(filename);

        if (!file.exists())
            file.createNewFile();

        var fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();
    }

}
