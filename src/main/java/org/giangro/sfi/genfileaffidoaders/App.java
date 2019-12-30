/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.giangro.sfi.genfileaffidoaders;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;

/**
 *
 * @author Alex
 */
@ComponentScan(basePackages = "org.giangro.sfi")
@Configuration
@PropertySource("classpath:gen_adersfiles.properties")
public class App {

  final static Logger logger
    = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    logger.info("=================================================");
    logger.debug("Initializing spring framework");
    AnnotationConfigApplicationContext ctx = null;
    try {
      ctx = new AnnotationConfigApplicationContext(App.class);
      App app = ctx.getBean(App.class);
      app.doStart();
    } // try
    finally {
      if (ctx!=null) {
        ctx.close();
        logger.info("exit");
      } // if
    } // finally

  }

  private void doStart() {
    logger.info("Starting ADERS File Generation Tool version "+this.version);   
    try {
        logger.debug("template path: \""+this.templatePath+"\"");
        logger.debug("destination path: \""+this.destinationPath+"\"");
    } // try
    catch (Exception ex) {
      logger.error(ex.getLocalizedMessage());
    } // catch
  }

  @Value("${version}")
  private String version;
  
  @Value("${template_path}")
  private String templatePath;
  
  @Value("${destination_path}")
  private String destinationPath;

  @Autowired
  private GenAdeRSFilesDescritta genAderRSFilesDescritta;

} // class App
