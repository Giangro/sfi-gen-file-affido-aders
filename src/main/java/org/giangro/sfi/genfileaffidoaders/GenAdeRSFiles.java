/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.giangro.sfi.genfileaffidoaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import org.springframework.beans.factory.annotation.Value;

@Service
class GenAdeRSFiles {

  final static Logger logger
    = LoggerFactory.getLogger(GenAdeRSFiles.class);

  final static String PROCESSING_EXT = ".processing";
  final static String OK_EXT = ".ok";
  final static String NOK_EXT = ".nok";

  public GenAdeRSFiles() {  
  } // public method

  public void generate() {
    try {
    }   // try
    catch (Exception ex) {
      logger.error(ex.getLocalizedMessage());
    }   // catch
  }
  
  @Value("${template_path}")
  private String templatePath;
  
  @Value("${destination_path}")
  private String destinationPath;
  
} // class GenAdeRSFiles
