package com.carfher.consultaproc;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkFinder {
    public int cont=0;
    public List<String> lista = new ArrayList<String>();

    public List<String> linksFinder(String url){
         try {
            // Conecta y obtiene el HTML de la página
            Document doc = Jsoup.connect(url).get();
            Elements elementsWithDataRk = doc.select("[data-rk]");
            System.out.println("Enlaces encontrados:");
            for (Element element : elementsWithDataRk) {
                // Muestra el texto y la URL del enlace
                String dataRkValue = element.attr("data-rk");
                if(element.text().contains("LABORAL") && cont <=5){
                    System.out.println("data-rk: " + dataRkValue + " | Tag: " + element.tagName() + " | Texto: " + element.text());
                    lista.add(dataRkValue);
                    cont++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
}
