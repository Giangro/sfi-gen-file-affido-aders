/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.giangro.sfi.genfileaffidoaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import static org.giangro.sfi.genfileaffidoaders.App.logger;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author GIANGR40
 */
@Service
public class GenAdeRSFilesDescritta extends GenAdeRSFiles {

    final static Logger logger
            = LoggerFactory.getLogger(GenAdeRSFilesDescritta.class);
    final static String SEP_CONF
            = ",";
    final static String FILE_NAME_PRE
            = "FTP.";
    final static String DEFAULT_DATE_PATTERN
            = "yyyyMMdd";
    final static String DEFAULT_DATE
            = "19700101";
    final static int _00C = 0;
    final static int _DPT = 1;
    final static int _DPD = 2;
    final static int _DPC = 3;
    final static int _99C = 4;

    final static int PROGRESSIVO_RECORD_DPT = 1;

    @PostConstruct
    void init() {
        codAmbitoArr
                = splitElem(codAmbito);
        tipoModelloArr
                = splitElem(tipoModello);
        codiceClienteArr
                = splitElem(codiceCliente);
        codiceCapArr
                = splitElem(codiceCap);
        numLavorazioneArr
                = splitElem(numLavorazione);
        tipoSpedizioneArr
                = splitElem(tipoSpedizione);
        destinatarioArr
                = splitElem(destinatario);
        comuneDestinatarioArr
                = splitElem(comuneDestinatario);
        provinciaDestinatarioArr
                = splitElem(provinciaDestinatario);
        indirizzoDestinatarioArr
                = splitElem(indirizzoDestinatario);
        dataCreazioneFileArr
                = splitElem(dataCreazioneFile);
        numeroRiferimentoDPTArr
                = splitElem(numeroRiferimentoDPT);

        line00C = "";
        lineDPT = "";
        lineDPD = "";
        lineDPC = "";
        line99C = "";

        counterFlussiLogici = 0;
        counterRaccomandate = 0;
        nextRaccomandataId = startRaccomandata;

        randomNumber = 0;

        counterProgressivoDPT = 0;

    }

    @Override
    public void generate() throws Exception {
        logger.info("generating ADERS Descritta");
        logConfParams();

        BufferedReader br = null;
        PrintWriter pw = null;

        String newfilename
                = getNewFileName();

        logger.info("creating new file \"" + newfilename + "\"");

        try {
            br = new BufferedReader(new FileReader(templateFile));
            pw = new PrintWriter(new FileWriter(destinationPath + newfilename));

            String line;
            int noline = 0;
            Boolean exit = false;
            while ((line = br.readLine()) != null && !exit) {
                switch (noline) {
                    case GenAdeRSFilesDescritta._00C:
                        logger.debug(String.valueOf(noline) + ": reading 00C section from template");
                        line00C = line;
                        break;
                    case GenAdeRSFilesDescritta._DPT:
                        logger.debug(String.valueOf(noline) + ": reading DPT section from template");
                        lineDPT = line;
                        break;
                    case GenAdeRSFilesDescritta._DPD:
                        logger.debug(String.valueOf(noline) + ": reading DPD section from template");
                        lineDPD = line;
                        break;
                    case GenAdeRSFilesDescritta._DPC:
                        logger.debug(String.valueOf(noline) + ": reading DPC section from template");
                        lineDPC = line;
                        break;
                    case GenAdeRSFilesDescritta._99C:
                        logger.debug(String.valueOf(noline) + ": reading 99C section from template");
                        line99C = line;
                        break;
                    default:
                        exit = true;
                        break;
                } // switch   
                noline++;
                //logger.debug(line);
            } // while

            Random r = new Random();
            nextRaccomandataId = 0;
                        
            write_00C(pw, line00C);            

            for (int i = 0; i < nFlussiLogici; i++) {
                // incremente counter progressivo DPT 
                counterProgressivoDPT++;      
                // genera randomicamente codice ambito, tipo modello, ecc..
                randomNumber = r.nextInt(maxNumValues);
                write_DPT(pw, lineDPT);
                counterRaccomandate=0;
                for (int j = 0; j < nRaccomandate; j++ ) {
                    counterRaccomandate++;
                    nextRaccomandataId++;    
                    write_DPD(pw, lineDPD);
                } // for
                write_DPC(pw, lineDPC);
            } // for

            write_99C(pw, line99C);
            
            logger.info("file has been generated.");
        } // try
        catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
        } // catch
        finally {
            if (br != null) {
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
        } // finally

    }

    private void write_00C(PrintWriter pw, String line) {
        String line_00C = line;
        line_00C = StringUtils.overlay(line_00C, codAder, 3, 8);
        //line_00C = StringUtils.overlay(line_00C, "****", 8,12);        
        line_00C = StringUtils.overlay(line_00C, getCurrentDate(DEFAULT_DATE_PATTERN), 12, 20);
        line_00C = StringUtils.overlay(line_00C, idFlusso, 20, 28);
        line_00C = StringUtils.overlay(line_00C, codForn, 28, 33);
        line_00C = StringUtils.overlay(line_00C, "0", 28, 29); // patch to be eliminated in the future
        pw.println(line_00C);
    }

    private void write_DPT(PrintWriter pw, String line) {
        String line_DPT = line;
        String progressivorecorddpt = String.format("%07d", PROGRESSIVO_RECORD_DPT);
        line_DPT = StringUtils.overlay(line_DPT, progressivorecorddpt, 3, 10);
        line_DPT = StringUtils.overlay(line_DPT, codAmbitoArr[randomNumber], 10, 13);
        line_DPT = StringUtils.overlay(line_DPT, tipoModelloArr[randomNumber], 13, 14);
        line_DPT = StringUtils.overlay(line_DPT, codAmbitoArr[randomNumber], 17, 20);
        line_DPT = StringUtils.overlay(line_DPT, dataCreazioneFileArr[randomNumber], 20, 28);
        line_DPT = StringUtils.overlay(line_DPT, tipoModelloArr[randomNumber], 28, 29);
        String counterprogressivodpt = String.format("%04d", counterProgressivoDPT);
        line_DPT = StringUtils.overlay(line_DPT, counterprogressivodpt, 30, 34);
        line_DPT = StringUtils.overlay(line_DPT, codiceClienteArr[randomNumber], 34, 42);
        line_DPT = StringUtils.overlay(line_DPT, codiceCapArr[randomNumber], 42, 47);
        line_DPT = StringUtils.overlay(line_DPT, numLavorazioneArr[randomNumber], 47, 61);
        line_DPT = StringUtils.overlay(line_DPT, dataCreazioneFileArr[randomNumber], 61, 69);
        line_DPT = StringUtils.overlay(line_DPT, numeroRiferimentoDPTArr[randomNumber], 69, 74);
        line_DPT = StringUtils.overlay(line_DPT, tipoSpedizioneArr[randomNumber], 79, 81);

        pw.println(line_DPT);
    }

    private void write_DPD(PrintWriter pw, String line) {
        String line_DPD = line;
        
        String progressivorecorddpd = String.format("%07d", counterRaccomandate+1);
        line_DPD = StringUtils.overlay(line_DPD, progressivorecorddpd, 3, 10);
        String nextraccomandataiddpd = String.format("%012d", nextRaccomandataId);
        line_DPD = StringUtils.overlay(line_DPD, nextraccomandataiddpd , 10, 22);
        line_DPD = StringUtils.overlay(line_DPD, destinatarioArr[randomNumber], 42, 42 + destinatarioArr[randomNumber].length());
        line_DPD = StringUtils.overlay(line_DPD, codiceCapArr[randomNumber] , 130, 135);
        line_DPD = StringUtils.overlay(line_DPD, comuneDestinatarioArr[randomNumber] , 135, 135 + comuneDestinatarioArr[randomNumber].length());
        line_DPD = StringUtils.overlay(line_DPD, provinciaDestinatarioArr[randomNumber] , 177, 179);
        line_DPD = StringUtils.overlay(line_DPD, indirizzoDestinatarioArr[randomNumber] , 179, 179 + indirizzoDestinatarioArr[randomNumber].length());
        
        pw.println(line_DPD);        
    }

    private void write_DPC(PrintWriter pw, String line) {
        String line_DPC = line;
        
        String progressivorecorddpc = String.format("%07d", counterRaccomandate+2);
        line_DPC = StringUtils.overlay(line_DPC, progressivorecorddpc, 3, 10);
        line_DPC = StringUtils.overlay(line_DPC, codAmbitoArr[randomNumber], 10, 13);
        progressivorecorddpc = String.format("%08d", counterRaccomandate+2);
        line_DPC = StringUtils.overlay(line_DPC, progressivorecorddpc, 13, 21);
        
        pw.println(line_DPC);
    }

    private void write_99C(PrintWriter pw, String line) {
        String line_99C = line;
        
        line_99C = StringUtils.overlay(line_99C, codAder, 3, 8);
        String progressivorecord99C = String.format("%08d", nFlussiLogici*(counterRaccomandate+2));
        line_99C = StringUtils.overlay(line_99C, progressivorecord99C, 12, 20);
        
        pw.print(line_99C);
    }

    private String[] splitElem(String value) {
        String[] array;
        array = value.split(GenAdeRSFilesDescritta.SEP_CONF, -1);
        return array;
    }

    private String getCurrentDate(String pattern) {
        String date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            date = simpleDateFormat.format(new Date());
        } // try
        catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            logger.warn("using default pattern: " + DEFAULT_DATE_PATTERN);
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
                date = simpleDateFormat.format(new Date());
            } // try
            catch (Exception ex1) {
                logger.error(ex1.getLocalizedMessage());
                logger.warn("using default date: " + DEFAULT_DATE);
                date = DEFAULT_DATE;
            } // catch

        } // catch        
        return date;
    }

    private String getNewFileName() {
        StringBuilder newfilename = new StringBuilder();
        Random r = new Random();       
        String codproglav = String.format("%06d", r.nextInt(1000000));        
        newfilename
                .append(FILE_NAME_PRE)
                .append(idFlusso)
                .append(".M")
                .append(codAder)
                .append(".D")
                .append(codForn)
//                .append(".D")
                .append(".")
                .append(getCurrentDate(dateFormat))
                .append(".T")
                .append(codproglav);
        return newfilename.toString();
    }

    private void logConfParams() {

        if (logConfParams.equals(Boolean.FALSE)) {
            return;
        } // if
        else {
            logger.debug("max_num_values: " + maxNumValues.toString());
            logger.debug("template file: \"" + templateFile + "\"");
            logger.debug("destination path: \"" + destinationPath + "\"");
            logger.debug("filler_char: \"" + fillerChar + "\"");
            logger.debug("id_flusso: \"" + idFlusso + "\"");
            logger.debug("cod_ader: \"" + codAder + "\"");
            logger.debug("cod_forn: \"" + codForn + "\"");
            //logger.debug("cod_prog_lav: \"" + codProgLav + "\"");
            logger.debug("date_format: \"" + dateFormat + "\"");
            logger.debug("cod_ambito: \"" + String.join(SEP_CONF, codAmbitoArr) + "\"");
            logger.debug("tipo_modello: \"" + String.join(SEP_CONF, tipoModelloArr) + "\"");
            logger.debug("codice_cliente: \"" + String.join(SEP_CONF, codiceClienteArr) + "\"");
            logger.debug("codice_cap: \"" + String.join(SEP_CONF, codiceCapArr) + "\"");
            logger.debug("num_lavorazione: \"" + String.join(SEP_CONF, numLavorazioneArr) + "\"");
            logger.debug("tipo_spedizione: \"" + String.join(SEP_CONF, tipoSpedizioneArr) + "\"");
            logger.debug("destinatario: \"" + String.join(SEP_CONF, destinatarioArr) + "\"");
            logger.debug("comune_destinatario: \"" + String.join(SEP_CONF, comuneDestinatarioArr) + "\"");
            logger.debug("provincia_destinatario: \"" + String.join(SEP_CONF, provinciaDestinatarioArr) + "\"");
            logger.debug("indirizzo_destinatario: \"" + String.join(SEP_CONF, indirizzoDestinatarioArr) + "\"");
            logger.debug("data_creazione_file: \"" + String.join(SEP_CONF, dataCreazioneFileArr) + "\"");
            logger.debug("numero_riferimento_dpt: \"" + String.join(SEP_CONF, numeroRiferimentoDPTArr) + "\"");
            logger.debug("n_flussi_logici: \"" + nFlussiLogici.toString() + "\"");
            logger.debug("n_raccomandate: \"" + nRaccomandate.toString() + "\"");
            logger.debug("start_raccomandata: \"" + nRaccomandate.toString() + "\"");
        } // else   
    }

    @Value("${log_conf_params}")
    Boolean logConfParams;

    @Value("${filler_char}")
    private String fillerChar;

    @Value("${max_num_values}")
    private Integer maxNumValues;

    @Value("${template_file}")
    private String templateFile;

    @Value("${destination_path}")
    private String destinationPath;

    @Value("${id_flusso}")
    private String idFlusso;

    @Value("${cod_ader}")
    private String codAder;

    @Value("${cod_forn}")
    private String codForn;

    //@Value("${cod_prog_lav}")
    //private String codProgLav;

    @Value("${date_format}")
    private String dateFormat;

    @Value("${date_format_filename}")
    private String dateFormatFileName;

    @Value("${cod_ambito}")
    private String codAmbito;

    @Value("${tipo_modello}")
    private String tipoModello;

    @Value("${codice_cliente}")
    private String codiceCliente;

    @Value("${codice_cap}")
    private String codiceCap;

    @Value("${num_lavorazione}")
    private String numLavorazione;

    @Value("${tipo_spedizione}")
    private String tipoSpedizione;

    @Value("${destinatario}")
    private String destinatario;

    @Value("${comune_destinatario}")
    private String comuneDestinatario;

    @Value("${provincia_destinatario}")
    private String provinciaDestinatario;

    @Value("${indirizzo_destinatario}")
    private String indirizzoDestinatario;

    @Value("${data_creazione_file}")
    private String dataCreazioneFile;

    @Value("${numero_riferimento_dpt}")
    private String numeroRiferimentoDPT;

    @Value("${n_flussi_logici}")
    private Integer nFlussiLogici;

    @Value("${n_raccomandate}")
    private Integer nRaccomandate;

    @Value("${start_raccomandata}")
    private Integer startRaccomandata;

    private String[] codAmbitoArr;
    private String[] tipoModelloArr;
    private String[] codiceClienteArr;
    private String[] codiceCapArr;
    private String[] numLavorazioneArr;
    private String[] tipoSpedizioneArr;
    private String[] destinatarioArr;
    private String[] comuneDestinatarioArr;
    private String[] provinciaDestinatarioArr;
    private String[] indirizzoDestinatarioArr;
    private String[] dataCreazioneFileArr;
    private String[] numeroRiferimentoDPTArr;

    private String line00C;
    private String lineDPT;
    private String lineDPD;
    private String lineDPC;
    private String line99C;

    private Integer counterFlussiLogici;
    private Integer counterRaccomandate;
    private Integer nextRaccomandataId;
    private Integer counterProgressivoDPT;

    private Integer randomNumber;

} // class GenAdeRSFilesDescritta
