package database;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import util.GameInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonSavefile implements Database {
    private String fileName;

    public JsonSavefile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String saveGame(GameInfo gameInfo) {
        try {
            PrintWriter printWriter = new PrintWriter(fileName);
            printWriter.write(gameInfo.toDocument().toJson(JsonWriterSettings.builder().indent(true).build()));
            printWriter.close();
            return "success";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public void updateGame(String gameId, GameInfo gameInfo) {
        saveGame(gameInfo);
    }

    @Override
    public void removeGame(String gameId) {
        try {
            PrintWriter printWriter = new PrintWriter(fileName);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Document read() throws IOException {
        return Document.parse(Files.readString(Paths.get(fileName)));
    }
}
