package com.techsavy.de.common;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public interface Constants {

  final String DEFAULT_CONFIG_FILE_NAME = "processors.yml";
  final String PROCESSORS_FILE_PARAM_NAME = "processors.file";
  final int PROCESSOR_MAX_WAIT_TIME = 5;
  final Marker AUDIT_MARKER = MarkerManager.getMarker("AUDIT");

}
