package id.inixindo.androidrestapi.models;

import java.util.List;

public class ModelResponseUsers {
    int status;
    String message;
    List<ModelUsers> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ModelUsers> getData() {
        return data;
    }
}
