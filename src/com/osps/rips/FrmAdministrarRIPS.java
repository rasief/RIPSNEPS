package com.osps.rips;

import com.osps.FrmPrincipal;
import com.osps.db.DbCargaArch;
import com.osps.entidad.CargaArch;
import com.osps.entidad.CargaArchDeta;
import com.osps.procesos.PrGeneraRIPS;
import com.osps.utilidades.Tupla;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * Formulario de validación de archivos planos RIPS
 * @author Feisar Moreno
 * @date 08/03/2012
 */
public class FrmAdministrarRIPS extends javax.swing.JInternalFrame {
    private final FrmPrincipal frmPrincipal;
    private static final String COD_ENT_ADM = "EPS037";
    private long idCarga = 0L;
    
    private class AvanceProceso implements Runnable {
        private boolean controlCorrer = true;
        private int cantPuntos = 0;
        private final int tipoProceso;
        
        @Override
        @SuppressWarnings("SleepWhileInLoop")
        public void run() {
            while (this.controlCorrer) {
                try {
                    Thread.sleep(1000);
                    this.cargarAvance();
                } catch (InterruptedException e) {}
            }
            lblAvance.setText(" ");
        }
        
        private AvanceProceso(int tipoProceso) {
            this.tipoProceso = tipoProceso;
        }
        
        public void setControlCorrer(boolean controlCorrer) {
            this.controlCorrer = controlCorrer;
        }
        
        private void cargarAvance() {
            //Se actualiza la etiqueda de avance
            String textoAvance = "";
            switch (tipoProceso) {
                case 1: //Generar XLSX
                    textoAvance = "Generando archivos";
                    break;
                case 2: //Borrar carga
                    textoAvance = "Borrando carga";
                    break;
                case 3: //Borrar archivo
                    textoAvance = "Borrando archivo";
                    break;
            }
            textoAvance += "...".substring(0, cantPuntos) + "   ".substring(0, 3 - cantPuntos);
            lblAvance.setText(textoAvance);
            cantPuntos++;
            cantPuntos = cantPuntos % 4;
        }
    }
    
    /**
     * Creates new form FrmValidacionRIPS
     * @param frmPrincipal
     */
    public FrmAdministrarRIPS(FrmPrincipal frmPrincipal) {
        initComponents();
        
        this.frmPrincipal = frmPrincipal;
        
        //Se carga el combo de años
        int anoActual = Calendar.getInstance().get(Calendar.YEAR);
        this.cmbAno.removeAllItems();
        for (int i = anoActual - 2; i <= anoActual; i++) {
            this.cmbAno.addItem(i);
        }
        this.cmbAno.setSelectedItem(anoActual);
        
        //Se carga el combo de meses
        this.cmbMes.removeAllItems();
        Tupla<Integer, String> tuplaAux = new Tupla<>(1, "Enero");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(2, "Febrero");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(3, "Marzo");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(4, "Abril");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(5, "Mayo");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(6, "Junio");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(7, "Julio");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(8, "Agosto");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(9, "Septiembre");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(10, "Octubre");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(11, "Noviembre");
        this.cmbMes.addItem(tuplaAux);
        tuplaAux = new Tupla<>(12, "Diciembre");
        this.cmbMes.addItem(tuplaAux);
    }
    
    private void habilitarComponentes(boolean habilitar) {
        this.cmbAno.setEnabled(habilitar);
        this.cmbMes.setEnabled(habilitar);
        this.btnGenerarArchivos.setEnabled(habilitar);
        this.btnListarArchivos.setEnabled(habilitar);
        this.btnBorrarCarga.setEnabled(habilitar);
        this.tblArchivosCarga.setEnabled(habilitar);
    }
    
    private void cargarTablaArchivos() {
        //Nombres de las columnas
        String[] nombCols = new String[5];
        nombCols[0] = "Archivo";
        nombCols[1] = "Tabla";
        nombCols[2] = "Reg. Cargados";
        nombCols[3] = "Estado";
        nombCols[4] = "Borrar";
        
        //Se obtienen los registros de detalle
        DbCargaArch dbCargaArch = new DbCargaArch();
        ArrayList<CargaArchDeta> listaCargaArch = dbCargaArch.getListaCargaArchDeta(idCarga);
        long[] arrIdCargaDeta = new long[listaCargaArch.size()];
        String [][] cuerpoTabla = new String[listaCargaArch.size()][0];
        for (int i = 0; i < listaCargaArch.size(); i++) {
            CargaArchDeta cargaDetaAux = listaCargaArch.get(i);
            
            arrIdCargaDeta[i] = cargaDetaAux.getIdCargaDeta();
            
            String[] registroAux = new String[5];
            registroAux[0] = cargaDetaAux.getNomArch();
            registroAux[1] = cargaDetaAux.getCodTabla();
            registroAux[2] = "" + cargaDetaAux.getNumRegistros();
            registroAux[3] = cargaDetaAux.getNomEstado();
            registroAux[4] = " ... ";
            
            cuerpoTabla[i] = registroAux;
        }
        
        DefaultTableModel tablaCargaDeta = new DefaultTableModel(cuerpoTabla, nombCols) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 4;
            }
        };
        
        tblArchivosCarga.setModel(tablaCargaDeta);
        TableColumnModel columnas = tblArchivosCarga.getColumnModel();
        columnas.getColumn(0).setPreferredWidth(435);
        columnas.getColumn(1).setPreferredWidth(60);
        columnas.getColumn(2).setPreferredWidth(90);
        columnas.getColumn(3).setPreferredWidth(110);
        columnas.getColumn(4).setPreferredWidth(45);
        
        this.tblArchivosCarga.getColumn("Borrar").setCellRenderer(new ButtonRendererBorrarArchivos());
        this.tblArchivosCarga.getColumn("Borrar").setCellEditor(
            new ButtonEditorBorrarArchivos(new JCheckBox(), this.tblArchivosCarga, this, arrIdCargaDeta)
        );
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbMes = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        cmbAno = new javax.swing.JComboBox();
        btnBorrarCarga = new javax.swing.JButton();
        btnListarArchivos = new javax.swing.JButton();
        btnGenerarArchivos = new javax.swing.JButton();
        btnGenerarConsolidado = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblArchivosCarga = new javax.swing.JTable();
        lblAvance = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Administración de Periodos Cargados");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ADMINISTRACIÓN DE PERIODOS CARGADOS");

        jLabel4.setText("Año");

        jLabel5.setText("Mes");

        btnBorrarCarga.setText("Borrar carga");
        btnBorrarCarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarCargaActionPerformed(evt);
            }
        });

        btnListarArchivos.setText("Listar archivos");
        btnListarArchivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarArchivosActionPerformed(evt);
            }
        });

        btnGenerarArchivos.setText("Generar archivos");
        btnGenerarArchivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarArchivosActionPerformed(evt);
            }
        });

        btnGenerarConsolidado.setText("Generar consolidado grupos");
        btnGenerarConsolidado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarConsolidadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGenerarArchivos)
                        .addGap(18, 18, 18)
                        .addComponent(btnListarArchivos)
                        .addGap(18, 18, 18)
                        .addComponent(btnBorrarCarga)
                        .addGap(18, 18, 18)
                        .addComponent(btnGenerarConsolidado)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrarCarga)
                    .addComponent(btnListarArchivos)
                    .addComponent(btnGenerarArchivos)
                    .addComponent(btnGenerarConsolidado))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblArchivosCarga.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblArchivosCarga.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tblArchivosCarga);

        lblAvance.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAvance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAvance.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(lblAvance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblAvance)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnGenerarArchivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarArchivosActionPerformed
        int ano = (Integer)cmbAno.getSelectedItem();
        int mes = ((Tupla<Integer, String>)cmbMes.getSelectedItem()).getId();
        
        DbCargaArch dbCargaArch = new DbCargaArch();
        
        //Se valida que la carga exista
        CargaArch cargaArch = dbCargaArch.getCargaArch(COD_ENT_ADM, ano, mes);
        
        if (cargaArch != null) {
            idCarga = cargaArch.getIdCarga();
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            int retornoSel = fileChooser.showSaveDialog(this);
            if (retornoSel == JFileChooser.APPROVE_OPTION) {
                habilitarComponentes(false);
                
                //Se inicia el hilo que muestra el avance del proceso
                final AvanceProceso tareaProceso = new AvanceProceso(1);
                Thread hiloProcesar = new Thread(tareaProceso, "Procesando");
                hiloProcesar.start();
                
                Runnable procesoArchivos = () -> {
                    try {
                        PrGeneraRIPS prGeneraRIPS = new PrGeneraRIPS(cargaArch);
                        try {
                            prGeneraRIPS.generarRIPS(fileChooser.getSelectedFile().getAbsolutePath());
                            
                            JOptionPane.showMessageDialog(frmPrincipal, "Archivos generados con éxito.", "Administración de Periodos", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frmPrincipal, "Error al tratar de generar los archivos del RIPS.\n" + ex.getMessage(), "Administración de Periodos", JOptionPane.ERROR_MESSAGE);
                        }
                    } finally {
                        tareaProceso.setControlCorrer(false);
                        habilitarComponentes(true);
                    }
                };
                
                Thread hiloArchivos = new Thread(procesoArchivos, "procesarArchivos");
                hiloArchivos.start();
            }
        } else {
            JOptionPane.showMessageDialog(frmPrincipal, "Periodo no hallado.", "Administración de Periodos", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnGenerarArchivosActionPerformed

    private void btnListarArchivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarArchivosActionPerformed
        int ano = (Integer)cmbAno.getSelectedItem();
        int mes = ((Tupla<Integer, String>)cmbMes.getSelectedItem()).getId();
        
        DbCargaArch dbCargaArch = new DbCargaArch();
        
        //Se valida que la carga exista
        CargaArch cargaArch = dbCargaArch.getCargaArch(COD_ENT_ADM, ano, mes);
        
        if (cargaArch != null) {
            idCarga = cargaArch.getIdCarga();
            cargarTablaArchivos();
        } else {
            JOptionPane.showMessageDialog(frmPrincipal, "Periodo no hallado.", "Administración de Periodos", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnListarArchivosActionPerformed

    private void btnBorrarCargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarCargaActionPerformed
        int ano = (Integer)cmbAno.getSelectedItem();
        int mes = ((Tupla<Integer, String>)cmbMes.getSelectedItem()).getId();
        
        DbCargaArch dbCargaArch = new DbCargaArch();
        
        //Se valida que la carga exista
        CargaArch cargaArch = dbCargaArch.getCargaArch(COD_ENT_ADM, ano, mes);
        
        if (cargaArch != null) {
            idCarga = cargaArch.getIdCarga();
            
            int seleccionAux = JOptionPane.showConfirmDialog(frmPrincipal, "¿Está seguro de querer borrar el período de carga completo?", "Borrar Carga", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (seleccionAux == JOptionPane.YES_OPTION) {
                habilitarComponentes(false);
                
                //Se inicia el hilo que muestra el avance del proceso
                final AvanceProceso tareaProceso = new AvanceProceso(2);
                Thread hiloProcesar = new Thread(tareaProceso, "Procesando");
                hiloProcesar.start();
                
                Runnable procesoArchivos = () -> {
                    try {
                        int resultadoBorrar = dbCargaArch.borrarRegistro(idCarga);
                        switch (resultadoBorrar) {
                            case 1:
                                JOptionPane.showMessageDialog(frmPrincipal, "Período borrado con éxito.", "Borrar Archivo", JOptionPane.INFORMATION_MESSAGE);
                                cargarTablaArchivos();
                                break;
                            case -1:
                                JOptionPane.showMessageDialog(frmPrincipal, "Error interno durante el proceso de borrado.", "Borrar Archivo", JOptionPane.ERROR_MESSAGE);
                                break;
                            default:
                                JOptionPane.showMessageDialog(frmPrincipal, "Error (" + resultadoBorrar + ") durante el proceso de borrado.", "Borrar Archivo", JOptionPane.ERROR_MESSAGE);
                                break;
                        }
                    } finally {
                        tareaProceso.setControlCorrer(false);
                        habilitarComponentes(true);
                    }
                };
                
                Thread hiloArchivos = new Thread(procesoArchivos, "procesarArchivos");
                hiloArchivos.start();
            }
        } else {
            JOptionPane.showMessageDialog(frmPrincipal, "Periodo no hallado.", "Administración de Periodos", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnBorrarCargaActionPerformed

    private void btnGenerarConsolidadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarConsolidadoActionPerformed
        int ano = (Integer)cmbAno.getSelectedItem();
        int mes = ((Tupla<Integer, String>)cmbMes.getSelectedItem()).getId();
        
        DbCargaArch dbCargaArch = new DbCargaArch();
        
        //Se valida que la carga exista
        CargaArch cargaArch = dbCargaArch.getCargaArch(COD_ENT_ADM, ano, mes);
        
        if (cargaArch != null) {
            idCarga = cargaArch.getIdCarga();
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            int retornoSel = fileChooser.showSaveDialog(this);
            if (retornoSel == JFileChooser.APPROVE_OPTION) {
                habilitarComponentes(false);
                
                //Se inicia el hilo que muestra el avance del proceso
                final AvanceProceso tareaProceso = new AvanceProceso(1);
                Thread hiloProcesar = new Thread(tareaProceso, "Procesando");
                hiloProcesar.start();
                
                Runnable procesoArchivos = () -> {
                    try {
                        PrGeneraRIPS prGeneraRIPS = new PrGeneraRIPS(cargaArch);
                        try {
                            prGeneraRIPS.generarConsolidadoGrupos(fileChooser.getSelectedFile().getAbsolutePath());
                            
                            JOptionPane.showMessageDialog(frmPrincipal, "Archivo generado con éxito.", "Administración de Periodos", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frmPrincipal, "Error al tratar de generar el archivo consolidado de grupos.\n" + ex.getMessage(), "Administración de Periodos", JOptionPane.ERROR_MESSAGE);
                        }
                    } finally {
                        tareaProceso.setControlCorrer(false);
                        habilitarComponentes(true);
                    }
                };
                
                Thread hiloArchivos = new Thread(procesoArchivos, "procesarArchivos");
                hiloArchivos.start();
            }
        } else {
            JOptionPane.showMessageDialog(frmPrincipal, "Periodo no hallado.", "Administración de Periodos", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnGenerarConsolidadoActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrarCarga;
    private javax.swing.JButton btnGenerarArchivos;
    private javax.swing.JButton btnGenerarConsolidado;
    private javax.swing.JButton btnListarArchivos;
    private javax.swing.JComboBox cmbAno;
    private javax.swing.JComboBox cmbMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAvance;
    private javax.swing.JTable tblArchivosCarga;
    // End of variables declaration//GEN-END:variables
    
    class ButtonRendererBorrarArchivos extends JButton implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class ButtonEditorBorrarArchivos extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private final JTable table;
        private final FrmAdministrarRIPS frmAdministrarRIPS;
        private final long[] arrIdCargaDeta;
        
        public ButtonEditorBorrarArchivos(JCheckBox checkBox, JTable table, FrmAdministrarRIPS frmAdministrarRIPS, long[] arrIdCargaDeta) {
            super(checkBox);
            this.table = table;
            this.frmAdministrarRIPS = frmAdministrarRIPS;
            this.arrIdCargaDeta = arrIdCargaDeta;
            
            this.button = new JButton();
            this.button.setOpaque(true);
            this.button.addActionListener((ActionEvent e) -> {
                fireEditingStopped();
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                this.button.setForeground(table.getSelectionForeground());
                this.button.setBackground(table.getSelectionBackground());
            } else {
                this.button.setForeground(table.getForeground());
                this.button.setBackground(table.getBackground());
            }
            this.label = (value == null) ? "" : value.toString();
            this.button.setText(this.label);
            this.isPushed = true;
            return this.button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (this.isPushed) {
                int seleccionAux = JOptionPane.showConfirmDialog(frmPrincipal, "¿Está seguro de querer borrar el archivo seleccionado?", "Borrar Archivo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (seleccionAux == JOptionPane.YES_OPTION) {
                    habilitarComponentes(false);
                    DbCargaArch dbCargaArch = new DbCargaArch();
                    
                    //Se inicia el hilo que muestra el avance del proceso
                    final AvanceProceso tareaProceso = new AvanceProceso(3);
                    Thread hiloProcesar = new Thread(tareaProceso, "Procesando");
                    hiloProcesar.start();
                    
                    Runnable procesoArchivos = () -> {
                        try {
                            int resultadoBorrar = dbCargaArch.borrarRegistroDeta(this.arrIdCargaDeta[this.table.getSelectedRow()]);
                            switch (resultadoBorrar) {
                                case 1:
                                    JOptionPane.showMessageDialog(frmPrincipal, "Archivo borrado con éxito.", "Borrar Archivo", JOptionPane.INFORMATION_MESSAGE);
                                    cargarTablaArchivos();
                                    break;
                                case -1:
                                    JOptionPane.showMessageDialog(frmPrincipal, "Error interno durante el proceso de borrado.", "Borrar Archivo", JOptionPane.ERROR_MESSAGE);
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(frmPrincipal, "Error (" + resultadoBorrar + ") durante el proceso de borrado.", "Borrar Archivo", JOptionPane.ERROR_MESSAGE);
                                    break;
                            }
                        } finally {
                            tareaProceso.setControlCorrer(false);
                            habilitarComponentes(true);
                        }
                    };
                    
                    Thread hiloArchivos = new Thread(procesoArchivos, "procesarArchivos");
                    hiloArchivos.start();
                }
            }
            this.isPushed = false;
            return this.label;
        }
        
        @Override
        public boolean stopCellEditing() {
            this.isPushed = false;
            return super.stopCellEditing();
        }
        
        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

}
