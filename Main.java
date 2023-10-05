import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("cd_catalog.xml");

            NodeList cdList = doc.getElementsByTagName("CD");
            List<Double> precios = new ArrayList<>();

            // Itera a través de los elementos CD y extrae los precios
            for (int i = 0; i < cdList.getLength(); i++) {
                Element cd = (Element) cdList.item(i);
                String precioStr = cd.getElementsByTagName("PRICE").item(0).getTextContent();
                double precioF = Double.parseDouble(precioStr);
                precios.add(precioF);
            }

            double mediaPrecio = calcMed(precios);
            double desviacionPrecio = calcDesvEstd(precios);

            System.out.println("Media de precios: " + mediaPrecio);
            System.out.println("Desviación estándar de precios: " + desviacionPrecio);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    private static double calcMed(List<Double> datos) {
        double suma = 0;
        for (double dato : datos) {
            suma += dato;
        }
        return suma / datos.size();
    }

    // Función para calcular la desviación estándar
    private static double calcDesvEstd(List<Double> datos) {
        double media = calcMed(datos);
        double sumaDiferenciasCuadradas = 0;
        for (double dato : datos) {
            sumaDiferenciasCuadradas += Math.pow(dato - media, 2);
        }
        double varianza = sumaDiferenciasCuadradas / datos.size();
        return Math.sqrt(varianza);
    }
}
