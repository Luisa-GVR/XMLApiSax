import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class TableXML extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TableXML app = new TableXML();
            app.setVisible(true);
        });
    }

    private JTable table;
    private DefaultTableModel model;
    private SaxHandler saxHandler;

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
            saxHandler = new SaxHandler(model);

            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();

                saxParser.parse(file, saxHandler);

                LinkedList<String> columnNames = saxHandler.getKeys();
                model.setColumnIdentifiers(columnNames.toArray());

                ArrayList<String> rowData = saxHandler.getDatos();
                int columnCounter = columnNames.size();
                int rowCounter = rowData.size() / columnCounter;

                for (int i = 0; i < rowCounter; i++) {
                    ArrayList<String> row = new ArrayList<>();
                    for (int j = 0; j < columnCounter; j++) {
                        row.add(rowData.get(i * columnCounter + j));
                    }
                    model.addRow(row.toArray());
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar el archivo XML", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
