package name.dusanjakub.cryptoservice;

import java.io.IOException;
import java.util.StringJoiner;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PartialBodyDeserializer.class)
public class PartialBody<T> {
    private final ObjectReader reader;
    private final JavaType type;
    private final JsonNode node;
    private final T partialObject;

    public PartialBody(ObjectReader reader, JavaType type, JsonNode node, T partialObject) {
        this.reader = reader;
        this.type = type;
        this.node = node;
        this.partialObject = partialObject;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PartialBody.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("partialObject=" + partialObject)
                .toString();
    }

    public T mergeInto(T source) throws IOException {
        return reader.withValueToUpdate(source).readValue(node);
    }

    public JavaType getType() {
        return type;
    }

    public JsonNode getNode() {
        return node;
    }

    public T getPartialObject() {
        return partialObject;
    }
}
