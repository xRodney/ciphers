package name.dusanjakub.cryptoservice.entities;

import com.fasterxml.jackson.annotation.JsonMerge;

public class JsonRequest<T> {
    @JsonMerge
    private JsonAttributes<T> data;

    public JsonRequest(JsonAttributes<T> data) {
        this.data = data;
    }

    public JsonRequest(String id, T data) {
        this.data = new JsonAttributes<>(id, data);
    }

    public JsonRequest() {
    }

    public JsonAttributes<T> getData() {
        return data;
    }

    public void setData(JsonAttributes<T> data) {
        this.data = data;
    }
}
