import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import java.lang.reflect.Type;

public class CustomControlCharMapperFactory implements Jackson2ObjectMapperFactory {

    @Override
    public ObjectMapper create(Type cls, String charset) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        mapper.getFactory().setCharacterEscapes(new NoEscapeControlChars());

        // This is the key line:
        // Enables the mapper to accept unescaped control characters (codes < 32)
        // inside JSON string values during deserialization (reading).
//        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), false);

        return mapper;
    }
}

class NoEscapeControlChars extends CharacterEscapes {

    private final int[] escapes;

    public NoEscapeControlChars() {
        int[] std = CharacterEscapes.standardAsciiEscapesForJSON();
        // copy
        escapes = java.util.Arrays.copyOf(std, std.length);

        // disable escaping for 0x1F (Unit Separator)
        escapes[0x1F] = CharacterEscapes.ESCAPE_NONE;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return escapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        return null; // no custom sequences
    }
}

