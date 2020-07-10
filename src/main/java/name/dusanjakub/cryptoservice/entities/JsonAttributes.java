package name.dusanjakub.cryptoservice.entities;

import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonMerge;

public class JsonAttributes<T> {
    private String id;
    @JsonMerge
    private T attributes;

    public JsonAttributes(String id, T attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    public JsonAttributes() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getAttributes() {
        return attributes;
    }

    public void setAttributes(T attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JsonAttributes.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("attributes=" + attributes)
                .toString();
    }
}
