package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try(var writer = new FileWriter(fileName)) {
            var gson = new Gson();
            String json = gson.toJson(data);
            writer.write(json);
        } catch (IOException e) {
            throw new FileProcessException(e.getMessage());
        }
    }
}
