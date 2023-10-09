import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class TableXML extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TableXML app = new TableXML();
            app.setVisible(true);
        });
    }

    private JTable table;
    private DefaultTableModel model;
    private Map<String, Integer> columnIndexes = new HashMap<>();

    public TableXML() {
        VentanaXML();
    }

    private void VentanaXML() {
        setTitle("XML");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        JButton load = new JButton("Cargar XML");
        load.addActionListener(e -> cargarXML());

        model = new DefaultTableModel();
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(load, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void cargarXML() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            cargarDatosXML(file);
        }
    }

    private void cargarDatosXML(File file) {
        model.setRowCount(0);
        model.setColumnCount(0);
        columnIndexes.clear();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                private List<String> rowData = new ArrayList<>();
                private String currentElement;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes)
                        throws SAXException {
                    currentElement = qName;
                    if (!columnIndexes.containsKey(qName)) {
                        int columnIndex = model.getColumnCount();
                        columnIndexes.put(qName, columnIndex);
                        model.addColumn(qName);
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (currentElement != null) {
                        String value = new String(ch, start, length).trim();
                        if (!value.isEmpty()) {
                            int columnIndex = columnIndexes.get(currentElement);
                            while (rowData.size() <= columnIndex) {
                                rowData.add("");
                            }
                            rowData.set(columnIndex, value);
                        }
                    }
                }

                @Override
                public void endElement(String uri, String localName, String name) throws SAXException {
                    if ("CD".equals(name)) {
                        model.addRow(rowData.toArray());
                        rowData.clear();
                    }
                    currentElement = null;
                }
            };

            saxParser.parse(file, handler);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar el archivo XML", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
