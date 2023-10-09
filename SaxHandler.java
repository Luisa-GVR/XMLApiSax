import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class SaxHandler extends DefaultHandler {

    private DefaultTableModel model;
    private StringBuilder currentValue;
    private String currentElement;
    private LinkedList<String> tags;
    private ArrayList<String> rowInfo;
    public SaxHandler(DefaultTableModel model) {
        this.model = model;
        this.tags = new LinkedList<>();
        this.rowInfo = new ArrayList<>();
        this.currentValue = new StringBuilder();
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.currentElement = qName;
        this.currentValue.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.currentValue.append(ch, start, length); //
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (this.currentElement != null && this.currentValue.length() > 0) {
            String valor = this.currentValue.toString().trim();
            if (!valor.isEmpty()) {
                this.rowInfo.add(valor);
                if (!tags.contains(this.currentElement)) {
                    tags.add(this.currentElement);
                }
            }
        }
        this.currentElement = null;
        this.currentValue.setLength(0);
    }
    public ArrayList<String> getDatos() {
        return rowInfo;
    }
    public LinkedList<String> getKeys() {
        return tags;
    }
}