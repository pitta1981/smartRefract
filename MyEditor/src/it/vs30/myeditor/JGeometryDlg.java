/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JGeometry.java
 *
 * Created on 29-nov-2011, 10.46.49
 */
package it.vs30.myeditor;

import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import org.myorg.myapi.FirstBrakeList;
import org.myorg.myapi.Indagine;

/**
 *
 * @author simone.pittaluga
 */
public class JGeometryDlg extends javax.swing.JDialog {

    String[] module = new String[20];
    String selezionato = "";
    int RETVALUE = 0;
    double spaz = 0.0;
    double spaz_in = 0.0;
    draw_geometria d_geo = new draw_geometria();

    /**
     * Creates new form JGeometry
     */
    public JGeometryDlg() {
        //super(parent, modal);
        initComponents();
        d_geo = new draw_geometria();
        
        this.setPreferredSize(new Dimension(600,500));
        
        jPanel3.add(d_geo);
        try {
            FileInputStream fis = new FileInputStream("shot.config");

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            int i = 1;
            String linea = br.readLine();
            // String[] module = new String[20];
            while (linea != null && i < 20) {
                module[i] = linea;
                i++;
                linea = br.readLine();
            }
            //int n=Integer.parseInt(linea);

            //jpWave.setStatus(statusMessageLabel);
            //jpWave.newSignal(16);
            //jpWave.sample[1]=new ArrayList();

            //linea = br.readLine();
            int chanel = Integer.parseInt(linea);

        } catch (Exception e1) {
            System.out.println("Geometry shot config file " + e1.getMessage());

        }

        jComboBox1.setEditable(true);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"0.5", "1", "1.25", "1.5", "1.75", "2", "2.5", "3"}));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(module));



    }
    final MyTableModel tm = new MyTableModel();
    Indagine prj;

    public void setTable(Indagine proj) {

        String geom = "";


        prj = proj;

        for (int i = 0; i < proj.stesa.size(); i++) {
            FirstBrakeList fbl = (FirstBrakeList) proj.stesa.get(i);
            //fbl.scoppio=Double.parseDouble(geom[i].replace('m', '\0'));
            tm.addData(new Data("" + (new File(fbl.fbp)).getName(), "" + fbl.scoppio));
            tm.spaz = fbl.spaz;
            tm.spaz_in = fbl.spaz_in;
            tm.ch = fbl.ch;
            geom = geom + " " + getGeom(fbl.spaz_in, fbl.spaz, fbl.scoppio, fbl.ch);
        }
        tm.setGeom(geom, module, jComboBox3,this);

        module[0] = geom;
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(module));
        jComboBox1.setSelectedItem(String.valueOf(tm.spaz));
        this.jTextField1.setText(String.valueOf(tm.spaz_in));

        //final JTable jTable1 = new JTable(tm);
        jTable1.setModel(tm);
        jTable1.setPreferredScrollableViewportSize(new Dimension(1000, 700));

        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.getModel().addTableModelListener(new MyTableModelListener(jTable1));



        //       jTable1.setFillsViewportHeight(true); 
 /*
         * JScrollPane jScrollPane2=new JScrollPane(jTable1);
         * jScrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
         * jPanel2.add(jScrollPane2);
         */
        //jPanel1.add(jTable1);
        // jTable1.setColumnModel(new TableColumnModel());

    }

    public void setTable(Indagine proj, double spaz) {



        String geom = "";


        jComboBox1.setSelectedItem(String.valueOf(spaz));
        tm.dataList.clear();
        for (int i = 0; i < proj.stesa.size(); i++) {
            FirstBrakeList fbl = (FirstBrakeList) proj.stesa.get(i);
            //fbl.scoppio=Double.parseDouble(geom[i].replace('m', '\0'));
            tm.addData(new Data("" + (new File(fbl.fbp)).getName(), "" + fbl.scoppio));
            fbl.spaz = tm.spaz;
            fbl.spaz_in = tm.spaz_in;
            tm.ch = fbl.ch;
            geom = geom + " " + getGeom(tm.spaz_in, tm.spaz, fbl.scoppio, fbl.ch);
        }
        tm.setGeom(geom, module, jComboBox3, this);

        module[0] = geom;
        //  jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(module));

        //final JTable jTable1 = new JTable(tm);
        jTable1.setModel(tm);
        jTable1.setPreferredScrollableViewportSize(new Dimension(1000, 700));

        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.getModel().addTableModelListener(new MyTableModelListener(jTable1));



        //       jTable1.setFillsViewportHeight(true); 
 /*
         * JScrollPane jScrollPane2=new JScrollPane(jTable1);
         * jScrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
         * jPanel2.add(jScrollPane2);
         */
        //jPanel1.add(jTable1);
        // jTable1.setColumnModel(new TableColumnModel());

    }

    private String getGeom(double spaz_in, double spaz, double scoppio, int ch) {


        if (scoppio < spaz_in) {
            return "" + (scoppio - spaz_in) + "m";
        } else if (scoppio >= spaz_in && scoppio <= (spaz_in + ((ch - 1) * spaz))) {
            return "" + ((scoppio - spaz_in) / spaz);
        } else if (scoppio > (spaz_in + ((ch - 1) * spaz))) {
            return "+" + (scoppio - (spaz_in + ((ch - 1) * spaz))) + "m";
        }
        return "";

    }

    private static class MyTableModel extends AbstractTableModel {

        private List<Data> dataList = new ArrayList<Data>();
        private List<String> colname = new ArrayList<String>();
        double spaz_in, spaz;
        String[] module;
        JComboBox jComboBox3;
        String geom = "";
        int ch;
        private JGeometryDlg jGeom_dlg;

        private String getGeom(double spaz_in, double spaz, double scoppio, int ch) {


            if (scoppio < spaz_in) {
                return "" + (scoppio - spaz_in) + "m";
            } else if (scoppio >= spaz_in && scoppio <= (spaz_in + ((ch - 1) * spaz))) {
                return "" + ((scoppio - spaz_in) / spaz);
            } else if (scoppio > (spaz_in + ((ch - 1) * spaz))) {
                return "+" + (scoppio - (spaz_in + ((ch - 1) * spaz))) + "m";
            }
            return "";

        }

        public int getColumnCount() {
            return 2;
        }

        public void addData(Data data) {
            dataList.add(data);
            fireTableRowsInserted(dataList.size() - 1, dataList.size() - 1);
        }

        public int getRowCount() {
            return dataList.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Data data = dataList.get(rowIndex);
            return columnIndex == 0 ? data.data1 : data.data2;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Data data = dataList.get(rowIndex);
            if (columnIndex == 0) {
                data.data1 = (String) aValue;
            } else {
                data.data2 = (String) aValue;
            }
            geom = "";
            for (int i = 0; i < dataList.size(); i++) {
                geom = geom + " " + this.getGeom(spaz_in, spaz, Double.parseDouble(((Data) dataList.get(i)).data2), ch);
            }



            module[0] = geom;
            jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(module));
            jGeom_dlg.draw_Geom_Preview();
            this.fireTableCellUpdated(rowIndex, columnIndex);
            // return columnIndex == 0 ? data.data1 : data.data2;
        }

        
        
        public String getColumnName(int pCol) {
            colname.clear();
            colname.add("File scoppio");
            colname.add("Posizione scoppio(m)");
            return colname.get(pCol);
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 1) {
                return false;
            } else {
                return true;
            }
        }

        private void setGeom(String s, String[] mod, JComboBox jCombo) {
            geom = s;
            jComboBox3 = jCombo;
            module = mod;
        }

        private void setGeom(String geom, String[] module, JComboBox jComboBox3, JGeometryDlg jGeometry_dlg) {
             //To change body of generated methods, choose Tools | Templates.
            setGeom(geom, module, jComboBox3);
            this.jGeom_dlg=jGeometry_dlg;
            
        }
    }

    private static class Data {

        public String data1;
        public String data2;

        public Data(String data1, String data2) {
            this.data1 = data1;
            this.data2 = data2;
        }
    }

    class MyTableModelListener implements TableModelListener {

        JTable table;

        MyTableModelListener(JTable table) {
            this.table = table;
        }

        @Override
        public void tableChanged(TableModelEvent e) {
            int firstRow = e.getFirstRow();
            int lastRow = e.getLastRow();
            int index = e.getColumn();

            switch (e.getType()) {
                case TableModelEvent.INSERT:
                    for (int i = firstRow; i <= lastRow; i++) {
                        System.out.println(i);
                    }
                    break;
                case TableModelEvent.UPDATE:
                    if (firstRow == TableModelEvent.HEADER_ROW) {
                        if (index == TableModelEvent.ALL_COLUMNS) {
                            System.out.println("A column was added");
                        } else {
                            System.out.println(index + "in header changed");
                        }
                    } else {
                        for (int i = firstRow; i <= lastRow; i++) {
                            if (index == TableModelEvent.ALL_COLUMNS) {
                                System.out.println("All columns have changed");
                            } else {


                                System.out.println(index);
                            }
                        }
                    }
                    break;
                case TableModelEvent.DELETE:
                    for (int i = firstRow; i <= lastRow; i++) {
                        System.out.println(i);
                    }
                    break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jComboBox3 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(650, 500));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setBackground(new java.awt.Color(102, 255, 102));
        jButton1.setText(org.openide.util.NbBundle.getMessage(JGeometryDlg.class, "JGeometryDlg.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 51, 51));
        jButton2.setText(org.openide.util.NbBundle.getMessage(JGeometryDlg.class, "JGeometryDlg.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 204));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(org.openide.util.NbBundle.getMessage(JGeometryDlg.class, "JGeometryDlg.jLabel4.text")); // NOI18N
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(153, 153, 255));
        jButton3.setText(org.openide.util.NbBundle.getMessage(JGeometryDlg.class, "JGeometryDlg.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel4)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(JGeometryDlg.class, "JGeometryDlg.jPanel2.border.title"))); // NOI18N

        jComboBox3.setEditable(true);
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
            .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jButton4.setBackground(new java.awt.Color(255, 255, 102));
        jButton4.setText(org.openide.util.NbBundle.getMessage(JGeometryDlg.class, "JGeometryDlg.jButton4.text")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(JGeometryDlg.class, "JGeometryDlg.jLabel1.text")); // NOI18N

        jTextField1.setText(org.openide.util.NbBundle.getMessage(JGeometryDlg.class, "JGeometryDlg.jTextField1.text")); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel3.setText(org.openide.util.NbBundle.getMessage(JGeometryDlg.class, "JGeometryDlg.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 44, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(JGeometryDlg.class, "JGeometryDlg.jPanel2.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            FileOutputStream fos = new FileOutputStream("shot.config", true);

            OutputStreamWriter os = new OutputStreamWriter(fos);
            selezionato = (String) jComboBox3.getSelectedItem();
            boolean esiste = false;
            for (int i = 0; i < 20; i++) {
                if (selezionato.equals(this.module[i])) {
                    esiste = true;
                }
            }
            if (!esiste) {
                os.write(selezionato + "\n");
            }

            //              os.write(fl.spaz + "\n");
            os.flush();
            os.close();

        } catch (Exception e1) {
            System.out.println("Geometry shot config file " + e1.getMessage());

        }
        this.spaz_in = Double.parseDouble((String) this.jTextField1.getText());
        this.spaz = Double.parseDouble((String) this.jComboBox1.getSelectedItem());
        this.RETVALUE = 1;
        this.setVisible(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String spazia = (String) jComboBox1.getSelectedItem();
        tm.spaz = Double.parseDouble(spazia);
        this.setTable(prj, tm.spaz);
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        this.RETVALUE = 0;
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your hansdling code here:
        String spazia = (String) jComboBox1.getSelectedItem();
        tm.spaz = Double.parseDouble(spazia);
        this.setTable(prj, tm.spaz);

        System.out.println("Selezionato " + jComboBox3.getSelectedItem());

    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        String spazia = (String) this.jTextField1.getText();

        tm.spaz_in = Double.parseDouble(spazia);
        this.setTable(prj, tm.spaz);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        try {

            java.net.URI uri = new java.net.URI("https://docs.google.com/presentation/d/1NgOLKeVOhTd8Ylty_8ND-F6d-d0vJCfI2tvTB4vyxP8/edit");
            desktop.browse(uri);
        } catch (Exception e) {

            System.err.println(e.getMessage());
        }


        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        this.setModal(false);
        this.setAlwaysOnTop(false);
        Jgeometria_av geomAV = new Jgeometria_av();
        geomAV.setTable(prj);

        geomAV.setModal(true);
        geomAV.setVisible(true);
        this.setVisible(true);


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
        this.draw_Geom_Preview();
        
        /*selezionato = (String) jComboBox3.getSelectedItem();
        this.spaz_in = Double.parseDouble((String) this.jTextField1.getText());
        this.spaz = Double.parseDouble((String) this.jComboBox1.getSelectedItem());

        int ch = ((FirstBrakeList) prj.stesa.get(0)).ch;
        while (selezionato.startsWith(" ")) {
            selezionato = selezionato.replaceFirst(" ", "");
        }
        double[] shot = new double[selezionato.split(" ").length];
        String[] geom = selezionato.split(" ");
        for (int i = 0; i < geom.length; i++) {

            if (geom[i].contains("m") && !geom[i].contains("+")) {
                shot[i] = Double.parseDouble(geom[i].replace('m', '\0')) + spaz_in;

            } else if (geom[i].contains("m") && geom[i].contains("+")) {
                shot[i] = (ch - 1) * spaz + Double.parseDouble(geom[i].replace('m', '\0')) + spaz_in;

            } else if (!geom[i].contains("m")) {
                shot[i] = (Double.parseDouble(geom[i].replace('m', '\0'))) * spaz + spaz_in;
            }


        }
        d_geo.setGeom(shot, spaz, spaz_in, ch);





        d_geo.invalidate();
        d_geo.repaint();*/
    }//GEN-LAST:event_jButton4ActionPerformed

    public void draw_Geom_Preview(){
        try{
        selezionato = (String) jComboBox3.getSelectedItem();
        this.spaz_in = Double.parseDouble((String) this.jTextField1.getText());
        this.spaz = Double.parseDouble((String) this.jComboBox1.getSelectedItem());

        int ch = ((FirstBrakeList) prj.stesa.get(0)).ch;
        while (selezionato.startsWith(" ")) {
            selezionato = selezionato.replaceFirst(" ", "");
        }
        double[] shot = new double[selezionato.split(" ").length];
        String[] geom = selezionato.split(" ");
        for (int i = 0; i < geom.length; i++) {

            if (geom[i].contains("m") && !geom[i].contains("+")) {
                shot[i] = Double.parseDouble(geom[i].replace('m', '\0')) + spaz_in;

            } else if (geom[i].contains("m") && geom[i].contains("+")) {
                shot[i] = (ch - 1) * spaz + Double.parseDouble(geom[i].replace('m', '\0')) + spaz_in;

            } else if (!geom[i].contains("m")) {
                shot[i] = (Double.parseDouble(geom[i].replace('m', '\0'))) * spaz + spaz_in;
            }


        }
        d_geo.setNoGeom(false);
        d_geo.setGeom(shot, spaz, spaz_in, ch);
        }
        catch(Exception ex){
            d_geo.setNoGeom(true);
            
        }




        d_geo.invalidate();
        d_geo.repaint();
    }
    
    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        jButton4ActionPerformed(null);
        
        
    }//GEN-LAST:event_jComboBox3ItemStateChanged
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
