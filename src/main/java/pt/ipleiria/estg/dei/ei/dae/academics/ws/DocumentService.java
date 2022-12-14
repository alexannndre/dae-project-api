package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.DocumentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.DocumentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Authenticated;

import javax.annotation.security.RolesAllowed;
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
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.OK;

@Path("documents")
@Authenticated
@RolesAllowed({"Student", "Administrator"})
public class DocumentService {
    @EJB
    private StudentBean studentBean;

    @EJB
    private DocumentBean documentBean;

    @Context
    private SecurityContext securityContext;

    private DocumentDTO toDTO(Document document) {
        return new DocumentDTO(
                document.getId(),
                document.getFilename()
        );
    }


    private List<DocumentDTO> documentsToDTOs(List<Document> documents) {
        return documents.stream().map(this::toDTO).collect(Collectors.toList());
    }


    @POST
    @Path("")
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    public Response upload(MultipartFormDataInput input) throws IOException {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        var username = securityContext.getUserPrincipal().getName();

        List<InputPart> inputParts = uploadForm.get("file");
        List<Document> documents = new LinkedList<>();

        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            String filename = getFileName(headers);

            // Convert the uploaded file to InputStream
            var inputStream = inputPart.getBody(InputStream.class, null);

            byte[] bytes = IOUtils.toByteArray(inputStream);

            String homedir = System.getProperty("user.home");
            String dirpath = homedir + File.separator + "uploads" + File.separator + username;

            mkdirIfNotExists(dirpath);

            String filepath = dirpath + File.separator + filename;
            writeFile(bytes, filepath);

            System.out.println("File saved to " + filepath);

            var document = documentBean.create(filepath, filename, username);
            documents.add(document);
        }

        return Response.ok(DocumentDTO.from(documents)).build();
    }

    private void mkdirIfNotExists(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @GET
    @Path("download/{id}")
    @Produces(APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("id") Long id) throws MyEntityNotFoundException {
        var document = documentBean.find(id);
        if (document == null)
            throw new MyEntityNotFoundException("Document not found");
        // TODO: can I download this? - Security breach here! How do you fix it?
        var response = Response.ok(new File(document.getFilepath()));
        response.header("Content-Disposition", "attachment; filename=" + document.getFilename());
        return response.build();
    }

    @GET
    @Path("")
    @Produces(APPLICATION_JSON)
    public Response getDocuments() {
        var username = securityContext.getUserPrincipal().getName();
        var documents = documentBean.getStudentDocuments(username);
        return Response.ok(DocumentDTO.from(documents)).build();
    }

    @GET
    @Authenticated
    @Path("{username}")
    public Response getDocuments(@PathParam("username") String username) {
        var principal = securityContext.getUserPrincipal().getName();

        if (!principal.equals(username) && !securityContext.isUserInRole("Administrator"))
            return Response.status(FORBIDDEN).build();

        List<Document> documents = documentBean.getStudentDocuments(username);
        return Response.ok(documentsToDTOs(documents)).build();
    }

    @GET
    @Path("exists")
    public Response hasDocuments() throws MyEntityNotFoundException {
        var username = securityContext.getUserPrincipal().getName();
        Student student = studentBean.find(username);

        if (student == null)
            throw new MyEntityNotFoundException("Student not found");

        return Response.status(OK).entity(!student.getDocuments().isEmpty()).build();

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
