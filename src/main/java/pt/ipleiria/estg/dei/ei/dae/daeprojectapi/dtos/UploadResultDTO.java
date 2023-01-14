package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

public class UploadResultDTO {
    private int process, success, fail;

    private String message;

    public UploadResultDTO() {
    }

    public UploadResultDTO(int process, int success, int fail, String message) {
        this.process = process;
        this.success = success;
        this.fail = fail;
        this.message = message;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
