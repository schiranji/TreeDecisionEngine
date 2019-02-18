package com.techsavy.de.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

  public static long getLineCount(String resourceFileName) {
    try {
      URL resource = FileUtil.class.getResource(File.separator + resourceFileName);
      Path path = Paths.get(resource.toURI());
      //Path path = Paths.get(resourceFileName);
      long lineCount = Files.lines(path).count();
      return lineCount;
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
      return -1;
    }
  }
}
