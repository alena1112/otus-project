package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        List<Measurement> measurements;
        try (var reader = new JsonReader(new FileReader(new File(getFileResource().toURI())))) {
            var gson = new Gson();
            var listType = new TypeToken<List<Measurement>>() {}.getType();
            measurements = gson.fromJson(reader, listType);
        } catch (IOException | URISyntaxException e) {
            throw new FileProcessException(e.getMessage());
        }
        return measurements;
    }

    private URL getFileResource() {
        var resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new FileProcessException(String.format("File %s not found", fileName));
        }
        return resource;
    }
}
