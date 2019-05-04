package com.techsavy.de.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtil {
  
  private static final Logger log = LogManager.getLogger();
  
  public static long getLineCount(String resourceFileName) {
    try {
      URL resource = FileUtil.class.getResource(File.separator + resourceFileName);
      Path path = Paths.get(resource.toURI());
      long lineCount = Files.lines(path).count();
      return lineCount;
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
      return -1;
    }
  }
  
  public static String readFromFile(String templateFile) throws IOException {
    InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(templateFile);
    String templateContents = null;
    if(inputStream == null) {
      inputStream = FileUtil.class.getClassLoader().getResourceAsStream("classpath:"+templateFile);
    }
    if(inputStream == null) {
      inputStream = FileUtil.class.getClassLoader().getResourceAsStream("/"+templateFile);
    }
    if (inputStream != null) {
      templateContents = new BufferedReader(new InputStreamReader(inputStream)).lines().parallel()
          .collect(Collectors.joining("\n"));
    } else {
      log.info("Input stream is NULL, reading using FileUtil as external file." + templateFile);
      templateContents = FileUtil.getContents(templateFile);
    }
    return templateContents;
  }
  
  public static String getContents(String fileName) throws IOException {
    File file = new File(fileName);
    
    return getContents(file);
  }
  
  public static String getContents(File aFile) throws IOException {
    StringBuffer contents = new StringBuffer();    
    if(!aFile.exists()){
      return contents.toString();
    }

    try (BufferedReader input = new BufferedReader( new FileReader(aFile) );) {
      String line = null; //not declared within while loop
      while (( line = input.readLine()) != null){
        contents.append(line);
        contents.append(System.getProperty("line.separator"));
      }
    }
    return contents.toString();
  }
}
