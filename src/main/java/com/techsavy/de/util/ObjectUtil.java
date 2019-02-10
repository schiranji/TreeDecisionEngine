package com.techsavy.de.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectUtil {

  public static Object getInstanceByName(String className) {
    try {
      Class<?> clazz = Class.forName(className);
      Constructor<?> constructor = clazz.getConstructor();
      Object instance = constructor.newInstance();
      return instance;
    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
       throw new RuntimeException("Error while instantialting class.", e);
    }
  }
}
