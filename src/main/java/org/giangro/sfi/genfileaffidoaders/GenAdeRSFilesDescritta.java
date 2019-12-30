/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.giangro.sfi.genfileaffidoaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author GIANGR40
 */
@Service
public class GenAdeRSFilesDescritta extends GenAdeRSFiles {

    final static Logger logger
        = LoggerFactory.getLogger(App.class);
    
    @Override
    public void generate() throws Exception {
        try {
            logger.info("generating ADERS Descritta");
        } // try
        catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
        }   // catch
    }
    
} // class GenAdeRSFilesDescritta
