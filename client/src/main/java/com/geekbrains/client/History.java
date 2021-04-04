package com.geekbrains.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class History {
    private static final int COUNT_MAX_LINES = 100;

    public static void saveMessage(String login, String msg) {
        try {
            Files.write(getFilePath(login), msg.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path getFilePath(String login) {
        Path path = Paths.get("history", "history_" + login + ".txt");

        if (Files.notExists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public static String getLastHistory(String login) {
        Path path = getFilePath(login);
        if (Files.notExists(path)) return "";

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            StringBuilder result = new StringBuilder();
            int startPos = 0;
            if (lines.size() > COUNT_MAX_LINES) startPos = lines.size() - COUNT_MAX_LINES;

            for (int i = startPos; i < lines.size(); i++) {
                result.append(lines.get(i)).append(System.lineSeparator());
            }
            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
