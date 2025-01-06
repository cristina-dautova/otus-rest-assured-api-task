package ru.otus.utils;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

  @SneakyThrows
  public static String readJsonFile(String path) {
    return Files.readString(Paths.get(path));
  }
}
