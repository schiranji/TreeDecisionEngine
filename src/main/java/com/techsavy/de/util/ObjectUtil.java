package com.techsavy.de.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.techsavy.de.domain.RuleEngineData;
import com.techsavy.de.domain.RuleEngineResult;
import com.techsavy.de.processor.BaseAbstractProcessor;

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
  
  public static void setProcessorData(BaseAbstractProcessor processor, RuleEngineData argRuleEngineData, RuleEngineResult argResult,
      Map<String, Object> argProcessorMap, int depth) {
    processor.ruleEngineData = argRuleEngineData;
    processor.result = argResult;
    processor.childProcessorMap = argProcessorMap;
    processor.depth = depth;
  }
}
