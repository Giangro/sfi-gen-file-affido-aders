/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.giangro.sfi.genfileaffidoaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import static org.giangro.sfi.genfileaffidoaders.GenAdeRSFilesDescritta.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author GIANGR40
 */
@Service
public class GenAdeRSFilesIndescritta extends GenAdeRSFiles {

    final static Logger logger
            = LoggerFactory.getLogger(GenAdeRSFilesIndescritta.class);
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

        lottoTerritorialeArr
                = splitElem(lottoTerritoriale);

        codAmbitoArr
                = splitElem(codAmbitoIndescr);

        tipoModelloArr
                = splitElem(tipoModelloIndescr);

        dataCreazioneFileArr
                = splitElem(dataCreazioneFileIndescr);

        dataPrevistaSpedizioneIndescrArr
                = splitElem(dataPrevistaSpedizioneIndescr);

        codiceServiceStampaIndescrArr
                = splitElem(codiceServiceStampaIndescr);

        formatoBustaIndescrArr
                = splitElem(formatoBustaIndescr);

        formatoFogliIndescrArr
                = splitElem(formatoFogliIndescr);

        codiceGestioneResoIndescrArr
                = splitElem(codiceGestioneResoIndescr);

        destinatarioIndescrArr
                = splitElem(destinatarioIndescr);

        capIndescrArr
                = splitElem(capIndescr);

        cittaIndescrArr
                = splitElem(cittaIndescr);

        provinciaIndescrArr
                = splitElem(provinciaIndescr);

        indirizzoIndescrArr
                = splitElem(indirizzoIndescr);

        line00C = "";
        lineDPT = "";
        lineDPD = "";
        lineDPC = "";
        line99C = "";

        randomNumber = 0;
        counterProgressivoDPT = startProgressivoDPT;
        idFusione = 0;

        counterFlussiLogici = 0;
        counterBusteIndescr = 0;

    }

    @Override
    public void generate() throws Exception {
        logger.info("generating ADERS Indescritta");
        logConfParams();

        BufferedReader br = null;
        PrintWriter pw = null;

        String newfilename
                = getNewFileName();

        logger.info("creating new file \"" + newfilename + "\"");

        try {
            br = new BufferedReader(new FileReader(templateFileIndescr));
            pw = new PrintWriter(new FileWriter(destinationPath + newfilename));

            String line;
            int noline = 0;
            Boolean exit = false;
            while ((line = br.readLine()) != null && !exit) {
                switch (noline) {
                    case GenAdeRSFilesIndescritta._00C:
                        logger.debug(String.valueOf(noline) + ": reading 00C section from template");
                        line00C = line;
                        break;
                    case GenAdeRSFilesIndescritta._DPT:
                        logger.debug(String.valueOf(noline) + ": reading DPT section from template");
                        lineDPT = line;
                        break;
                    case GenAdeRSFilesIndescritta._DPD:
                        logger.debug(String.valueOf(noline) + ": reading DPD section from template");
                        lineDPD = line;
                        break;
                    case GenAdeRSFilesIndescritta._DPC:
                        logger.debug(String.valueOf(noline) + ": reading DPC section from template");
                        lineDPC = line;
                        break;
                    case GenAdeRSFilesIndescritta._99C:
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

            write_00C(pw, line00C);

            for (int i = 0; i < nFlussiLogiciIndescr; i++) {
                // incremente counter progressivo DPT                    
                // genera randomicamente lotto territoriale, codice ambito, ecc..
                randomNumber = r.nextInt(maxNumValuesIndescr);
                write_DPT(pw, lineDPT);
                counterBusteIndescr = 0;
                for (int j = 0; j < nBusteIndescr; j++) {
                    counterBusteIndescr++;
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
        line_00C = StringUtils.overlay(line_00C, idFlussoIndescr, 20, 28);
        line_00C = StringUtils.overlay(line_00C, codForn, 28, 33);
        line_00C = StringUtils.overlay(line_00C, "0", 28, 29); // patch to be eliminated in the future
        pw.println(line_00C);
    }

    private void write_DPT(PrintWriter pw, String line) {

        Random r = new Random();
        counterProgressivoDPT++;

        String line_DPT = line;
        String progressivorecorddpt = String.format("%07d", PROGRESSIVO_RECORD_DPT);
        line_DPT = StringUtils.overlay(line_DPT, progressivorecorddpt, 3, 10);
        line_DPT = StringUtils.overlay(line_DPT, lottoTerritorialeArr[randomNumber], 13, 16);
        line_DPT = StringUtils.overlay(line_DPT, codAmbitoArr[randomNumber], 16, 19);
        line_DPT = StringUtils.overlay(line_DPT, tipoModelloArr[randomNumber], 19, 20);
        line_DPT = StringUtils.overlay(line_DPT, codForn, 20, 25);
        line_DPT = StringUtils.overlay(line_DPT, "0", 20, 21); // patch to be eliminated in the future
        line_DPT = StringUtils.overlay(line_DPT, dataCreazioneFileArr[randomNumber], 25, 33);
        String progressivodpt = String.format("%05d", counterProgressivoDPT);
        line_DPT = StringUtils.overlay(line_DPT, progressivodpt, 33, 38);
        line_DPT = StringUtils.overlay(line_DPT, codAmbitoArr[randomNumber], 38, 41);
        line_DPT = StringUtils.overlay(line_DPT, tipoModelloArr[randomNumber], 41, 42);
        line_DPT = StringUtils.overlay(line_DPT, lottoTerritorialeArr[randomNumber], 42, 45);
        idFusione = r.nextInt(10000000);
        String idfusionedpt = "XXX" + String.format("%07d", idFusione);
        line_DPT = StringUtils.overlay(line_DPT, idfusionedpt, 45, 55);
        line_DPT = StringUtils.overlay(line_DPT, dataPrevistaSpedizioneIndescrArr[randomNumber], 55, 63);
        line_DPT = StringUtils.overlay(line_DPT, codiceServiceStampaIndescrArr[randomNumber], 63, 68);
        line_DPT = StringUtils.overlay(line_DPT, formatoBustaIndescrArr[randomNumber], 68, 69);
        line_DPT = StringUtils.overlay(line_DPT, formatoFogliIndescrArr[randomNumber], 69, 71);
        line_DPT = StringUtils.overlay(line_DPT, codiceGestioneResoIndescrArr[randomNumber], 72, 73);

        pw.println(line_DPT);
    }

    private void write_DPD(PrintWriter pw, String line) {

        String line_DPD = line;
        String progressivorecorddpd = String.format("%07d", counterBusteIndescr + 1);
        line_DPD = StringUtils.overlay(line_DPD, progressivorecorddpd, 3, 10);
        line_DPD = StringUtils.overlay(line_DPD, getRandomID(), 10, 30);
        line_DPD = StringUtils.overlay(line_DPD, destinatarioIndescrArr[randomNumber], 30, 30 + destinatarioIndescrArr[randomNumber].length());
        line_DPD = StringUtils.overlay(line_DPD, capIndescrArr[randomNumber], 130, 135);
        line_DPD = StringUtils.overlay(line_DPD, cittaIndescrArr[randomNumber], 135, 135 + cittaIndescrArr[randomNumber].length());
        line_DPD = StringUtils.overlay(line_DPD, provinciaIndescrArr[randomNumber], 185, 187);
        line_DPD = StringUtils.overlay(line_DPD, indirizzoIndescrArr[randomNumber], 187, 187 + indirizzoIndescrArr[randomNumber].length());

        pw.println(line_DPD);
    }

    private void write_DPC(PrintWriter pw, String line) {
        String line_DPC = line;

        String progressivorecorddpc = String.format("%07d", counterBusteIndescr + 2);
        line_DPC = StringUtils.overlay(line_DPC, progressivorecorddpc, 3, 10);
        line_DPC = StringUtils.overlay(line_DPC, lottoTerritorialeArr[randomNumber], 13, 16);
        line_DPC = StringUtils.overlay(line_DPC, codAmbitoArr[randomNumber], 16, 19);
        line_DPC = StringUtils.overlay(line_DPC, tipoModelloArr[randomNumber], 19, 20);
        line_DPC = StringUtils.overlay(line_DPC, codForn, 20, 25);
        line_DPC = StringUtils.overlay(line_DPC, "0", 20, 21); // patch to be eliminated in the future
        line_DPC = StringUtils.overlay(line_DPC, dataCreazioneFileArr[randomNumber], 25, 33);
        String progressivodpt = String.format("%05d", counterProgressivoDPT);
        line_DPC = StringUtils.overlay(line_DPC, progressivodpt, 33, 38);
        progressivorecorddpc = String.format("%08d", counterBusteIndescr + 2);
        line_DPC = StringUtils.overlay(line_DPC, progressivorecorddpc, 38, 46);

        pw.println(line_DPC);

    }

    private void write_99C(PrintWriter pw, String line) {
        String line_99C = line;

        line_99C = StringUtils.overlay(line_99C, codAder, 3, 8);
        String progressivorecord99C = String.format("%08d", nFlussiLogiciIndescr * (nBusteIndescr + 2));
        line_99C = StringUtils.overlay(line_99C, progressivorecord99C, 12, 20);

        pw.print(line_99C);
    }

    private String getRandomID() {

        Random r = new Random();
        int ch;
        int i;
        int randint;
        String id = "";
        String idfusionedpt = "XXX" + String.format("%07d", idFusione);

        String template = "01234566789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (i = 0; i < 10; i++) {
            randint = r.nextInt(36);
            id += template.charAt(randint);
        } // for

        return idfusionedpt + id;
    }

    private String getNewFileName() {
        StringBuilder newfilename = new StringBuilder();
        Random r = new Random();
        String codproglav = String.format("%06d", r.nextInt(1000000));
        //String codproglav = "620000";
        newfilename
                .append(FILE_NAME_PRE)
                .append(idFlussoIndescr)
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

    private void logConfParams() {

        if (logConfParams.equals(Boolean.FALSE)) {
            return;
        } // if
        else {
            logger.debug("max_num_values_indescr: " + maxNumValuesIndescr.toString());
            logger.debug("template file: \"" + templateFileIndescr + "\"");
            logger.debug("destination path: \"" + destinationPath + "\"");
            logger.debug("filler_char: \"" + fillerChar + "\"");
            logger.debug("id_flusso_indescr: \"" + idFlussoIndescr + "\"");
            logger.debug("n_flussi_logici_indescr: \"" + nFlussiLogiciIndescr.toString() + "\"");
            logger.debug("n_buste_indescr: \"" + nBusteIndescr.toString() + "\"");
            logger.debug("cod_ader: \"" + codAder + "\"");
            logger.debug("cod_forn: \"" + codForn + "\"");
            //logger.debug("cod_prog_lav: \"" + codProgLav + "\"");
            logger.debug("date_format: \"" + dateFormat + "\"");
            logger.debug("lotto_territoriale: \"" + String.join(SEP_CONF, lottoTerritorialeArr) + "\"");
            logger.debug("cod_ambito: \"" + String.join(SEP_CONF, codAmbitoArr) + "\"");
            logger.debug("tipo_modello_indescr: \"" + String.join(SEP_CONF, tipoModelloArr) + "\"");
            logger.debug("data_creazione_file_indescr: \"" + String.join(SEP_CONF, dataCreazioneFileArr) + "\"");
            logger.debug("data_prevista_spedizione_indescr: \"" + String.join(SEP_CONF, dataPrevistaSpedizioneIndescrArr) + "\"");
            logger.debug("codice_service_stampa_indescr: \"" + String.join(SEP_CONF, codiceServiceStampaIndescrArr) + "\"");
            logger.debug("formato_busta_indescr: \"" + String.join(SEP_CONF, formatoBustaIndescrArr) + "\"");
            logger.debug("formato_fogli_indescr: \"" + String.join(SEP_CONF, formatoFogliIndescrArr) + "\"");
            logger.debug("codice_gestione_reso_indescr: \"" + String.join(SEP_CONF, codiceGestioneResoIndescrArr) + "\"");
            logger.debug("destinatario_indescr: \"" + String.join(SEP_CONF, destinatarioIndescrArr) + "\"");
            logger.debug("cap_indescr: \"" + String.join(SEP_CONF, capIndescrArr) + "\"");
            logger.debug("citta_indescr: \"" + String.join(SEP_CONF, cittaIndescrArr) + "\"");
            logger.debug("provincia_indescr: \"" + String.join(SEP_CONF, provinciaIndescrArr) + "\"");
            logger.debug("indirizzo_indescr: \"" + String.join(SEP_CONF, indirizzoIndescrArr) + "\"");
            logger.debug("start_progressivo_dpt: \"" + startProgressivoDPT + "\"");

        } // else   
    }

    @Value("${log_conf_params}")
    Boolean logConfParams;

    @Value("${filler_char}")
    private String fillerChar;

    @Value("${max_num_values_indescr}")
    private Integer maxNumValuesIndescr;

    @Value("${template_file_indescr}")
    private String templateFileIndescr;

    @Value("${destination_path}")
    private String destinationPath;

    @Value("${id_flusso_indescr}")
    private String idFlussoIndescr;

    @Value("${n_flussi_logici_indescr}")
    private Integer nFlussiLogiciIndescr;

    @Value("${n_buste_indescr}")
    private Integer nBusteIndescr;

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

    @Value("${lotto_territoriale}")
    private String lottoTerritoriale;

    @Value("${cod_ambito_indescr}")
    private String codAmbitoIndescr;

    @Value("${tipo_modello_indescr}")
    private String tipoModelloIndescr;

    @Value("${data_creazione_file_indescr}")
    private String dataCreazioneFileIndescr;

    @Value("${data_prevista_spedizione_indescr}")
    private String dataPrevistaSpedizioneIndescr;

    @Value("${codice_service_stampa_indescr}")
    private String codiceServiceStampaIndescr;

    @Value("${formato_busta_indescr}")
    private String formatoBustaIndescr;

    @Value("${formato_fogli_indescr}")
    private String formatoFogliIndescr;

    @Value("${codice_gestione_reso_indescr}")
    private String codiceGestioneResoIndescr;

    @Value("${destinatario_indescr}")
    private String destinatarioIndescr;

    @Value("${cap_indescr}")
    private String capIndescr;

    @Value("${citta_indescr}")
    private String cittaIndescr;

    @Value("${provincia_indescr}")
    private String provinciaIndescr;

    @Value("${indirizzo_indescr}")
    private String indirizzoIndescr;

    @Value("${start_progressivo_dpt}")
    private Integer startProgressivoDPT;

    private String[] lottoTerritorialeArr;
    private String[] codAmbitoArr;
    private String[] tipoModelloArr;
    private String[] dataCreazioneFileArr;
    private String[] dataPrevistaSpedizioneIndescrArr;
    private String[] codiceServiceStampaIndescrArr;
    private String[] formatoBustaIndescrArr;
    private String[] formatoFogliIndescrArr;
    private String[] codiceGestioneResoIndescrArr;
    private String[] destinatarioIndescrArr;
    private String[] capIndescrArr;
    private String[] cittaIndescrArr;
    private String[] provinciaIndescrArr;
    private String[] indirizzoIndescrArr;

    private String line00C;
    private String lineDPT;
    private String lineDPD;
    private String lineDPC;
    private String line99C;

    private Integer counterProgressivoDPT;
    private Integer randomNumber;
    private Integer idFusione;

    private Integer counterFlussiLogici;
    private Integer counterBusteIndescr;

} // GenAdeRSFilesIndescritta
