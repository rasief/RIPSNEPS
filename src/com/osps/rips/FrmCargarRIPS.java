package com.osps.rips;

import com.osps.db.DbCargaArch;
import com.osps.entidad.CargaArchDeta;
import com.osps.procesos.PrCargaArchivos;
import com.osps.utilidades.Tupla;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 * Formulario de carga de archivos planos RIPS
 * @author Feisar Moreno
 * @date 21/02/2012
 */
public class FrmCargarRIPS extends javax.swing.JInternalFrame {
    private long idCarga = 0L;
    
    private class AvanceCarga implements Runnable {
        private boolean controlCorrer = true;
        private int cantPuntos = 0;
        private int faseProceso;
        
        @Override
        @SuppressWarnings("SleepWhileInLoop")
        public void run() {
            while (this.controlCorrer) {
                try {
                    Thread.sleep(1000);
                    if (idCarga > 0L) {
                        this.cargarAvance();
                    }
                } catch (InterruptedException e) {}
            }
            lblAvance.setText(" ");
        }
        
        private AvanceCarga() {
            faseProceso = 1;
        }
        
        public void setControlCorrer(boolean controlCorrer) {
            this.controlCorrer = controlCorrer;
        }
        
        public void setFaseProceso(int faseProceso) {
            this.faseProceso = faseProceso;
        }
        
        private void cargarAvance() {
            //Nombres de las columnas
            String[] nombCols = new String[4];
            nombCols[0] = "Archivo";
            nombCols[1] = "Tabla";
            nombCols[2] = "Reg. Cargados";
            nombCols[3] = "Estado";
            
            //Se obtienen los registros de detalle
            DbCargaArch dbCargaArch = new DbCargaArch();
            ArrayList<CargaArchDeta> listaCargaArch = dbCargaArch.getListaCargaArchDeta(idCarga);
            String [][] cuerpoTabla = new String[listaCargaArch.size()][0];
            for (int i = 0; i < listaCargaArch.size(); i++) {
                CargaArchDeta cargaDetaAux = listaCargaArch.get(i);
                String[] registroAux = new String[4];
                registroAux[0] = cargaDetaAux.getNomArch();
                registroAux[1] = cargaDetaAux.getCodTabla();
                registroAux[2] = "" + cargaDetaAux.getNumRegistros();
                registroAux[3] = cargaDetaAux.getNomEstado();
                
                cuerpoTabla[i] = registroAux;
            }
            
            DefaultTableModel tablaCargaDeta = new DefaultTableModel(cuerpoTabla, nombCols) {
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }
            };
            
            tblCargaDeta.setModel(tablaCargaDeta);
            TableColumnModel columnas = tblCargaDeta.getColumnModel();
            columnas.getColumn(0).setPreferredWidth(480);
            columnas.getColumn(1).setPreferredWidth(60);
            columnas.getColumn(2).setPreferredWidth(90);
            columnas.getColumn(3).setPreferredWidth(110);
            
            //Se actualiza la etiqueda de avance
            String textoAvance = "";
            switch (faseProceso) {
                case 1:
                    textoAvance = "Cargando archivos";
                    break;
                case 2:
                    textoAvance = "Ajustando datos";
                    break;
            }
            textoAvance += "...".substring(0, cantPuntos) + "   ".substring(0, 3 - cantPuntos);
            lblAvance.setText(textoAvance);
            cantPuntos++;
            cantPuntos = cantPuntos % 4;
        }
    }
    
    /**
     * Creates new form FrmCargarRIPS
     */
    public FrmCargarRIPS() {
        initComponents();
        
        //Se carga el combo de años
        int anoActual = Calendar.getInstance().get(Calendar.YEAR);
        this.cmbAno.removeAllItems();
        for (int i = anoActual - 5; i <= anoActual; i++) {
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
    
    /**
     * Método que valida si se han seleccionado todos los campos requeridos
     * @return <code>true</code> si se seleccionaron todos los campos requeridos,
     * de lo contrario <code>false</code>.
     */
    private boolean validarCampos() {
        if (this.txtCarpetaArchivos.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar la carpeta en la que se encuentran los archivos.", "Error", JOptionPane.ERROR_MESSAGE);
            this.btnBuscarCarpeta.requestFocusInWindow();
            return false;
        }
        
        return true;
    }
    
    private void habilitarComponentes(boolean habilitar) {
        this.cmbAno.setEnabled(habilitar);
        this.cmbMes.setEnabled(habilitar);
        this.btnBuscarCarpeta.setEnabled(habilitar);
        this.btnCargar.setEnabled(habilitar);
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
        jLabel6 = new javax.swing.JLabel();
        btnBuscarCarpeta = new javax.swing.JButton();
        txtCarpetaArchivos = new javax.swing.JTextField();
        btnCargar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCargaDeta = new javax.swing.JTable();
        lblAvance = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Carga de Archivos Planos del RIPS");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CARGA DE ARCHIVOS PLANOS DEL RIPS");

        jLabel4.setText("Año");

        jLabel5.setText("Mes");

        jLabel6.setText("Carpeta");

        btnBuscarCarpeta.setText("...");
        btnBuscarCarpeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCarpetaActionPerformed(evt);
            }
        });

        txtCarpetaArchivos.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtCarpetaArchivos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarCarpeta))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                    .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarCarpeta)
                    .addComponent(txtCarpetaArchivos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCargar.setText("Cargar");
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });

        tblCargaDeta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblCargaDeta.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tblCargaDeta);

        lblAvance.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAvance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAvance.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnCargar)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addComponent(lblAvance, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCargar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAvance)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
        
    private void btnBuscarCarpetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCarpetaActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int retornoSel = fileChooser.showOpenDialog(this);
        if (retornoSel == JFileChooser.APPROVE_OPTION) {
            txtCarpetaArchivos.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_btnBuscarCarpetaActionPerformed

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed
        //Se valida que se hayan diligenciado todos los campos
        if (this.validarCampos()) {
            //Se inhabilitan los componentes
            this.habilitarComponentes(false);
            
            //Se inicia el hilo que muestra el avance del proceso
            final AvanceCarga tareaCarga = new AvanceCarga();
            Thread hiloProcesar = new Thread(tareaCarga, "Procesando");
            hiloProcesar.start();
            
            Runnable procesoCarga = () -> {
                try {
                    int ano = (Integer)cmbAno.getSelectedItem();
                    int mes = ((Tupla<Integer, String>)cmbMes.getSelectedItem()).getId();
                    
                    //Se llama a la clase que realiza la carga de archivos
                    PrCargaArchivos prCargaArchivos = PrCargaArchivos.iniciarCarga(ano, mes, txtCarpetaArchivos.getText());
                    
                    if (prCargaArchivos != null) {
                        idCarga = prCargaArchivos.getIdCarga();
                        
                        //Se realiza la carga
                        prCargaArchivos.cargarArchivos();
                        
                        //Se realiza el proceso de ajuste de datos
                        tareaCarga.setFaseProceso(2);
                        String rtaAux = prCargaArchivos.ajustarDatos();
                        
                        if (rtaAux.equals("")) {
                            JOptionPane.showMessageDialog(null, "Carga de archivos finalizada.", "Carga de Archivos", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error en la carga de archivos.\n" + rtaAux, "Carga de Archivos", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error en la carga de archivos.\n" + e.getMessage(), "Carga de Archivos", JOptionPane.ERROR_MESSAGE);
                } finally {
                    tareaCarga.setControlCorrer(false);
                    habilitarComponentes(true);
                }
            };
            
            Thread hiloCarga = new Thread(procesoCarga, "procesarCarga");
            hiloCarga.start();
        }
    }//GEN-LAST:event_btnCargarActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCarpeta;
    private javax.swing.JButton btnCargar;
    private javax.swing.JComboBox cmbAno;
    private javax.swing.JComboBox cmbMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAvance;
    private javax.swing.JTable tblCargaDeta;
    private javax.swing.JTextField txtCarpetaArchivos;
    // End of variables declaration//GEN-END:variables
}
