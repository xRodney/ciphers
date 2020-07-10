package name.dusanjakub.cryptoservice;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

public class PartialBodyDeserializer<T> extends JsonDeserializer<PartialBody<T>> implements ContextualDeserializer {
    private final JavaType type;

    public PartialBodyDeserializer() {
        this(null);
    }

    public PartialBodyDeserializer(JavaType type) {
        this.type = type;
    }

    @Override
    public PartialBody<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JavaType javaType = Objects.requireNonNull(type, "'type' cannot be null");
        JavaType containedType = javaType.containedType(0);

        JsonNode node = p.readValueAsTree();
        JsonParser tree = p.getCodec().treeAsTokens(node);
        T object = p.getCodec().readValue(tree, containedType);

        ObjectReader reader = ((ObjectMapper) p.getCodec()).readerFor(containedType);
        return new PartialBody<>(reader, containedType, node, object);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        return new PartialBodyDeserializer<>(ctxt.getContextualType());
    }
}
