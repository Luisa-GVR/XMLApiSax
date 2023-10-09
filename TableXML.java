import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;


public class TableXML extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TableXML app = new TableXML();
                app.setVisible(true);
            }
        }
        );
    }

    private JTable table;
    private DefaultTableModel model;

    public TableXML() {
        VentanaXML();
    }

    private void VentanaXML() {
        setTitle("XML");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        JButton load = new JButton("Cargar XML");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarXML();
            }
        });

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

        try {
            DocumentBuilderFactory bdfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = bdfactory.newDocumentBuilder();
            Document doc = builder.parse(file);

            Element root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            ArrayList<String> columnNames = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i) instanceof Element) {
                    Element element = (Element) nodeList.item(i);
                    NodeList childNodes = element.getChildNodes();

                    for (int j = 0; j < childNodes.getLength(); j++) {
                        if (childNodes.item(j) instanceof Element) {
                            String columnName = childNodes.item(j).getNodeName();
                            if (!columnNames.contains(columnName)) {
                                columnNames.add(columnName);
                            }
                        }
                    }
                }
            }

            model.setColumnIdentifiers(columnNames.toArray());

            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i) instanceof Element) {
                    Element element = (Element) nodeList.item(i);
                    NodeList childNodes = element.getChildNodes();
                    Object[] rowData = new Object[columnNames.size()];

                    for (int j = 0; j < childNodes.getLength(); j++) {
                        if (childNodes.item(j) instanceof Element) {
                            String columnName = childNodes.item(j).getNodeName();
                            String columnInfo = childNodes.item(j).getTextContent();
                            int columnIndex = columnNames.indexOf(columnName);
                            rowData[columnIndex] = columnInfo;
                        }
                    }

                    model.addRow(rowData);
                }
            }
            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar el archivo XML", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
