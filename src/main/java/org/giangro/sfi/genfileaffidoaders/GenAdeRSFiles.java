/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.giangro.sfi.genfileaffidoaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public abstract class GenAdeRSFiles {

  final static Logger logger
    = LoggerFactory.getLogger(GenAdeRSFiles.class);

  final static String PROCESSING_EXT = ".processing";
  final static String OK_EXT = ".ok";
  final static String NOK_EXT = ".nok";

  public GenAdeRSFiles() {  
  } // public method

  public abstract void generate() throws Exception;    
  
} // class GenAdeRSFiles
