package com.techsavy.de.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.techsavy.de.common.AppConfig;
import com.techsavy.de.domain.DecisionEngineScope;
import com.techsavy.de.exception.SystemException;

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
  
  public static ExecutorService getExecutor(int count) {
    if("fixed".equals(AppConfig.getSystemProperty("threadpool.type"))) {
      return Executors.newFixedThreadPool(count);
    } else if("blocked".equals(AppConfig.getSystemProperty("threadpool.type"))) {
      return new BlockingExecutor(count, count);
    } else {
      return Executors.newCachedThreadPool();
    }
  }
  
  public static boolean assertNotNull(Object obj) {
    if(obj == null) {
      throw new SystemException("Object is NULL.");
    }
    return true;
  }
}
