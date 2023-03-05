package plugin.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import plugin.entities.ChestLockerBean;

import java.io.IOException;

public class ChestLockerAdapter extends TypeAdapter<ChestLockerBean> {

    @Override
    public void write(JsonWriter writer, ChestLockerBean chestLocker) throws IOException {
        writer.beginObject();

        writer.name("playerName");
        writer.value(chestLocker.getPlayerName());

        writer.name("worldName");
        writer.value(chestLocker.getWorldName());

        writer.name("x");
        writer.value(chestLocker.getX());

        writer.name("y");
        writer.value(chestLocker.getY());

        writer.name("z");
        writer.value(chestLocker.getZ());

        writer.endObject();
    }

    @Override
    public ChestLockerBean read(JsonReader reader) throws IOException {
       ChestLockerBean chestLocker = new ChestLockerBean();
        reader.beginObject();
        String fieldName = null;

        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (token.equals(JsonToken.NAME)) {
                fieldName = reader.nextName();
            }

            if ("playerName".equals(fieldName)) {
                token = reader.peek();
                chestLocker.setPlayerName(reader.nextString());
            }

            if ("worldName".equals(fieldName)) {
                token = reader.peek();
                chestLocker.setWorldName(reader.nextString());
            }

            if ("x".equals(fieldName)) {
                token = reader.peek();
                chestLocker.setX(reader.nextDouble());
            }

            if ("y".equals(fieldName)) {
                token = reader.peek();
                chestLocker.setY(reader.nextDouble());
            }

            if ("z".equals(fieldName)) {
                token = reader.peek();
                chestLocker.setZ(reader.nextDouble());
            }
        }
        reader.endObject();
        return chestLocker;
    }
}
