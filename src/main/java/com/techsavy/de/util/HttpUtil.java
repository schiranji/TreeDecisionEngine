package com.techsavy.de.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.DecisionEngineRequest;

public class HttpUtil {

  private static final Logger log = LogManager.getLogger();

  public static Object postRequest(String urlStr, Map<String, String> params, DecisionEngineRequest request) {
    try {
      URL url = new URL(urlStr);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      for(String key: params.keySet()) {
        con.addRequestProperty(key, (String)params.get(key));
      }
      con.setDoOutput(true);
      OutputStream outStream = con.getOutputStream();
      OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
      outStreamWriter.write(JsonUtil.getJson(request));
      outStreamWriter.flush();
      outStreamWriter.close();
      outStream.close();
      
      int status = con.getResponseCode();
      InputStream inputStream = (status > 299) ? con.getErrorStream() : con.getInputStream();
      if(inputStream == null) {
        log.error("Error while connecting to " + urlStr + ", Status:"+status);
        return null;
      }
      BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
      String inputLine;
      StringBuffer content = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
      in.close();
      con.disconnect();
      return content.toString();
    } catch (Exception e) {
      log.error("Error while connecting to " + urlStr, e);
    }
    return null;
  }

}
