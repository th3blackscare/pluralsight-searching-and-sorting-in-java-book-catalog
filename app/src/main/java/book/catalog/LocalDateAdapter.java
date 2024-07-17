package book.catalog;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return LocalDate.parse(json.getAsString(), FORMATTER);
        } catch (Exception e) {
            throw new JsonParseException("Unable to parse date: " + json.getAsString(), e);
        }
    }

    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.format(FORMATTER));
    }
}


