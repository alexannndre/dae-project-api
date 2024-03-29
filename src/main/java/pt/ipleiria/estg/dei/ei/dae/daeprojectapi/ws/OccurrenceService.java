package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.DocumentBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.OccurrenceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.enums.Status;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.helpers.CsvHelper;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
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
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("occurrences")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
@Authenticated
public class OccurrenceService {
    @EJB
    private OccurrenceBean occurrenceBean;

    @EJB
    private DocumentBean documentBean;

    @Context
    private SecurityContext securityContext;

    private List<OccurrenceDTO> occurrencesToDTOs(List<Occurrence> occurrences) {
        return occurrences.stream().map(OccurrenceDTO::from).collect(Collectors.toList());
    }

    private List<StatusDTO> statusesToDTOs(List<Status> statuses) {
        return statuses.stream().map(StatusDTO::from).collect(Collectors.toList());
    }

    @GET
    @RolesAllowed({"Administrator"})
    @Path("/")
    public List<OccurrenceDTO> all() {
        return occurrencesToDTOs(occurrenceBean.getAllOccurrences());
    }

    @GET
    @RolesAllowed({"Administrator"})
    @Path("/status/{status}")
    public List<OccurrenceDTO> allByStatus(@PathParam("status") Status status) {
        return occurrencesToDTOs(occurrenceBean.getAllOccurrencesByStatus(status));
    }

    @GET
    @RolesAllowed({"Administrator", "Expert"})
    @Path("pending")
    public List<OccurrenceDTO> pending() {
        return occurrencesToDTOs(occurrenceBean.getAllPendingOccurrences());
    }

    @GET
    @RolesAllowed({"Administrator"})
    @Path("approved")
    public List<OccurrenceDTO> approved() {
        return occurrencesToDTOs(occurrenceBean.getAllApprovedOccurrences());
    }

    @GET
    @RolesAllowed({"Administrator", "Repairer"})
    @Path("repairing")
    public List<OccurrenceDTO> repairing() {
        return occurrencesToDTOs(occurrenceBean.getAllRepairingOccurrences());
    }

    @GET
    @RolesAllowed({"Administrator", "Customer", "Repairer", "Expert"})
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        var occurrence = occurrenceBean.findOrFail(id);
        return Response.ok(OccurrenceDTO.from(occurrence)).build();
    }

    @GET
    @RolesAllowed({"Administrator", "Customer", "Expert", "Repairer"})
    @Path("status")
    public List<StatusDTO> getStatus() {
        return statusesToDTOs(occurrenceBean.getAllStatus());
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, OccurrenceDTO occurrenceDTO) {
        occurrenceBean.update(id, occurrenceDTO.getDescription(), occurrenceDTO.getPolicy());
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
    @RolesAllowed({"Expert"})
    @Path("{id}/approve")
    public Response approve(@PathParam("id") Long id, OccurrenceDTO occurrenceDTO) {
        try {
            occurrenceBean.approve(id, occurrenceDTO.getExpertVat());
            return Response.ok("This occurrence has been approved").build();
        } catch (IllegalStateException e) {
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(e.getMessage())).build();
        }
    }

    @PATCH
    @RolesAllowed({"Expert"})
    @Path("{id}/reject")
    public Response reject(@PathParam("id") Long id, OccurrenceDTO occurrenceDTO) {
        try {
            occurrenceBean.reject(id, occurrenceDTO.getExpertVat());
            return Response.ok("This occurrence has been rejected").build();
        } catch (IllegalStateException e) {
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(e.getMessage())).build();
        }
    }

    @PATCH
    @RolesAllowed({"Customer"})
    @Path("{id}/service")
    public Response chooseService(@PathParam("id") Long id, ServiceDTO serviceDTO) {
        try {
            occurrenceBean.chooseService(id, serviceDTO);
            return Response.ok("A service has been selected for this occurrence").build();
        } catch (IllegalStateException e) {
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(e.getMessage())).build();
        }
    }

    @POST
    @RolesAllowed({"Customer"})
    @Path("{id}/service")
    public Response chooseServiceAndCreate(@PathParam("id") Long id, ServiceDTO serviceDTO) {
        return chooseService(id, serviceDTO);
    }

    @PATCH
    @RolesAllowed({"Repairer"})
    @Path("{id}/solve")
    public Response solve(@PathParam("id") Long id, OccurrenceDTO occurrenceDTO) {
        try {
            occurrenceBean.solve(id, occurrenceDTO.getRepairerVat());
            return Response.ok("This occurrence has been set to solved").build();
        } catch (IllegalStateException e) {
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(e.getMessage())).build();
        }
    }

    // Documents
    @POST
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    @RolesAllowed({"Administrator", "Customer", "Repairer"})
    @Path("{id}/documents")
    public Response uploadDoc(@PathParam("id") Long id, MultipartFormDataInput input) throws IOException {

        var vat = securityContext.getUserPrincipal().getName();
        var occurrence = occurrenceBean.findOrFail(id);

        if (!occurrence.getCustomer().getVat().equals(vat) && securityContext.isUserInRole("Customer"))
            return Response.status(FORBIDDEN).build();

        if (occurrence.getStatus() != Status.PENDING && securityContext.isUserInRole("Customer")) {
            var msg = "You can't add a document to an occurrence that is not pending";
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(msg)).build();
        }

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

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

    @POST
    @Path("upload")
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    @Authenticated
    @RolesAllowed({"Administrator"})
    public Response loadData(MultipartFormDataInput input) {
        List<OccurrenceDTO> list;
        try {
            list = CsvHelper.loadCsv(input, CsvHelper::toOccurrence);
        } catch (Exception e) {
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(e.getMessage())).build();
        }

        int count = list.size(), success = 0, fail = 0;

        if (count == 0)
            return Response.status(BAD_REQUEST).entity(new ErrorDTO("No processable occurrences were found in that file")).build();

        for (OccurrenceDTO occ : list) {
            try {
                occurrenceBean.create(occ);
                success++;
            } catch (EJBTransactionRolledbackException etre) {
                fail++;
            }
        }

        var msg = "";
        if (fail == 0)
            msg = String.format("Success! Created %d new occurrences", count);
        else if (success == 0)
            msg = String.format("Failed! None of the %d processed occurrences were created due to invalid id/vat(s)", count);
        else
            msg = String.format("%d occurrences have been processed. %d were successfully created. Failed to import %d occurrences due to invalid id/vat(s)", count, success, fail);
        return Response.ok().entity(new UploadResultDTO(count, success, fail, msg)).build();
    }

}
