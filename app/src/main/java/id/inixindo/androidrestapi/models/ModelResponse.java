package id.inixindo.androidrestapi.models;

import java.util.List;

public class ModelResponse {
    int status;
    String message;
    List<ModelRiders> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ModelRiders> getData() {
        return data;
    }

}
