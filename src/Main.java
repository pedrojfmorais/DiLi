import model.data.DiLi;
import model.jdcb.ConnDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SQLException {
        //System.out.println(new DiLi().authenticate("a1234567@isec.pt", "!Qq123456789"));

        /*
        public void insertBook(String title, String author, String synopsis, String language,
                           List<String> genres, boolean availability, double costPerDownload,
                           Map<String, String> downloadLink)
         */

        /*Map<String, String> downloadLink = new HashMap<String, String>();
        downloadLink.put("pdf", "linkPdf");
        downloadLink.put("epub", "linkEpub");
        downloadLink.put("mobi", "linkOutro");
        new ConnDB().insertBook("Titulo", "autor", "sinopse", "Korean",
                Arrays.asList("Fisica", "Eletrónica"), true, 10, downloadLink);
/*
        /*downloadLink.put("azw", "linkOutro");
        downloadLink.put("mobi", "mobimobimmoobbii");
        new ConnDB().updateBook(2,"Titular", "autora", "siasdfa", "Portuguese",
                Arrays.asList("Fisicaa", "Eletrónicaa"), false, 30, downloadLink);*/
        /*
            // Correr para limpar testes
                new ConnDB().clearDB("isto", true, true, true, true, true, true);

         */

        //System.out.println((new DiLi().search("autora")).toString());



    }
}