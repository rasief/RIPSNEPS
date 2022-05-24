package com.osps.rips;

import com.osps.db.DbEAdministradoras;
import com.osps.db.DbHistorial;
import com.osps.entidad.EAdministradoras;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Formulario para la consolidación de resultados de validación del RIPS
 * @author Feisar Moreno
 * @date 16/07/2012
 */
public class FrmConsolidacionRIPS extends javax.swing.JInternalFrame {
    
    private String entidadEnProceso = "";
    private final AvanceConsolidacion tareaConsolidacion = new AvanceConsolidacion();
    
    /**
     * Clase que ejecuta la consolidación
     */
    private class EjecucionConsolidacion implements Runnable {
        @Override
        public void run() {
            try {
                DbHistorial dbHistorial = new DbHistorial();
                
                //Se recorre la tabla verificando qué entidades requieren consolidación
                TableModel tablaAux = FrmConsolidacionRIPS.this.tblConsolidacion.getModel();
                boolean errorAux = false;
                for (int i = 0; i < tablaAux.getRowCount(); i++) {
                    if ((Boolean)tablaAux.getValueAt(i, 0)) {
                        FrmConsolidacionRIPS.this.entidadEnProceso = (String)tablaAux.getValueAt(i, 1);
                        int resulAux = dbHistorial.consolidarEntidad((String)tablaAux.getValueAt(i, 1));
                        if (resulAux < 0) {
                            JOptionPane.showMessageDialog(null, "Error interno al consolidar la entidad '" + (String)tablaAux.getValueAt(i, 2) + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                            errorAux = true;
                            break;
                        }
                    }
                }
                if (!errorAux) {
                    JOptionPane.showMessageDialog(null, "Consolidación realizada con éxito", "Consolidación", JOptionPane.INFORMATION_MESSAGE);
                }
            } finally {
                FrmConsolidacionRIPS.this.tareaConsolidacion.setControlCorrer(false);
                FrmConsolidacionRIPS.this.habilitarComponentes(true);
                FrmConsolidacionRIPS.this.entidadEnProceso = "";
                FrmConsolidacionRIPS.this.cargarTablaEntidades(true);
            }
        }
    }
    
    /**
     * Clase para mostrar el avance de la ejecución de la consolidación
     */
    private class AvanceConsolidacion implements Runnable {
        private boolean controlCorrer = true;
        
        @Override
        @SuppressWarnings("SleepWhileInLoop")
        public void run() {
            while (this.controlCorrer) {
                try {
                    Thread.sleep(10000);
                    FrmConsolidacionRIPS.this.cargarTablaEntidades(false);
                } catch (InterruptedException e) {}
            }
        }
        
        public void setControlCorrer(boolean controlCorrer) {
            this.controlCorrer = controlCorrer;
        }
    }
    
    /**
     * Clase para el editor de check boxes en tablas
     */
    private class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
        protected JCheckBox checkBox;
        
        public CheckBoxCellEditor() {
            checkBox = new JCheckBox();
            checkBox.setHorizontalAlignment(SwingConstants.CENTER);
            
            checkBox.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    int filaAux = FrmConsolidacionRIPS.this.tblConsolidacion.getSelectedRow();
                    TableModel tablaAux = FrmConsolidacionRIPS.this.tblConsolidacion.getModel();
                    tablaAux.setValueAt(checkBox.isSelected(), filaAux, 0);
                }
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            checkBox.setSelected((Boolean)value);
            return checkBox;
        }
        
        @Override
        public Object getCellEditorValue() {
            return checkBox.isSelected();
        }
    }
    
    /**
     * Clase para el renderer de check boxes en tablas
     */
    private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        public CheckBoxRenderer() {
            super();
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Boolean) {
                setSelected((Boolean)value);
            } else {
                return null;
            }
            
            return this;
        }
    }
    
    /**
     * Creates new form FrmConsolidacionRIPS
     */
    public FrmConsolidacionRIPS() {
        initComponents();
        
        this.cargarTablaEntidades(true);
    }
    
    /**
     * Método que carga el contenido de la tabla de consolidaciones
     * @param habilitar indica se se deben habilitar o inhabilitar los check boxes
     */
    private void cargarTablaEntidades(boolean habilitar) {
        final boolean habilitarAux = habilitar;
        
        //Nombres de las columnas
        String[] nombCols = new String[4];
        nombCols[0] = " ";
        nombCols[1] = "Código";
        nombCols[2] = "Nombre";
        nombCols[3] = "Consolidada";
        
        //Se obtienen los registros de la tabla
        DbEAdministradoras dbEAdministradoras = new DbEAdministradoras();
        ArrayList<EAdministradoras> listaEntidades = dbEAdministradoras.getListaEntidadesConsolidado();
        Object [][] cuerpoTabla = new Object[listaEntidades.size()][0];
        for (int i = 0; i < listaEntidades.size(); i++) {
            EAdministradoras entidadAux = listaEntidades.get(i);
            Object[] registroAux = new Object[4];
            if (habilitar) {
                if (entidadAux.getIndConsolidado().equals("SI")) {
                    registroAux[0] = false;
                } else {
                    registroAux[0] = true;
                }
            } else {
                registroAux[0] = false;
            }
            registroAux[1] = entidadAux.getCodEntAdm();
            registroAux[2] = entidadAux.getNombre();
            if (entidadAux.getCodEntAdm().equals(this.entidadEnProceso)) {
                registroAux[3] = "EN PROCESO";
            } else {
                registroAux[3] = entidadAux.getIndConsolidado();
            }
            
            cuerpoTabla[i] = registroAux;
        }
        
        DefaultTableModel tablaConsolidacion = new DefaultTableModel(cuerpoTabla, nombCols) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 0) {
                    return habilitarAux;
                } else {
                    return false;
                }
            }
        };
        
        tblConsolidacion.setModel(tablaConsolidacion);
        TableColumnModel columnas = tblConsolidacion.getColumnModel();
        columnas.getColumn(0).setPreferredWidth(40);
        columnas.getColumn(1).setPreferredWidth(50);
        columnas.getColumn(2).setPreferredWidth(300);
        columnas.getColumn(3).setPreferredWidth(70);
        columnas.getColumn(0).setCellEditor(new CheckBoxCellEditor());
        columnas.getColumn(0).setCellRenderer(new CheckBoxRenderer());
    }
    
    /**
     * Método privado que realiza la consolidación de cada una de las entidades seleccionadas
     */
    private void realizarConsolidacion() {
        //Se inhabilitan los componentes
        this.habilitarComponentes(false);
        
        //Se inicia el hilo que realiza el proceso
        EjecucionConsolidacion tareaEjecucion = new EjecucionConsolidacion();
        Thread hiloConsolidar = new Thread(tareaEjecucion, "ProcesarConsolidacion");
        hiloConsolidar.start();
        
        //Se inicia el hilo que muestra el avance del proceso
        Thread hiloProcesar = new Thread(this.tareaConsolidacion, "Procesando");
        hiloProcesar.start();
    }
    
    /**
     * Método que habilita o inhabilita los componentes del formulario
     * @param habilitar indica si se deben habilitar o inhabilitar los componentes
     */
    private void habilitarComponentes(boolean habilitar) {
        this.btnConsolidar.setEnabled(habilitar);
        this.chkSeleccionarTodos.setEnabled(habilitar);
        this.tblConsolidacion.setEnabled(habilitar);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblConsolidacion = new javax.swing.JTable();
        chkSeleccionarTodos = new javax.swing.JCheckBox();
        btnConsolidar = new javax.swing.JButton();

        setClosable(true);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CONSOLIDACIÓN DE RESULTADOS DE VALIDACIÓN DEL RIPS");

        tblConsolidacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblConsolidacion.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(tblConsolidacion);

        chkSeleccionarTodos.setText("Seleccionar todos");
        chkSeleccionarTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSeleccionarTodosActionPerformed(evt);
            }
        });

        btnConsolidar.setText("Consolidar");
        btnConsolidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsolidarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnConsolidar)
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(chkSeleccionarTodos)
                .addGap(382, 382, 382))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkSeleccionarTodos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConsolidar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void chkSeleccionarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSeleccionarTodosActionPerformed
        TableModel tablaAux = this.tblConsolidacion.getModel();
        for (int i = 0; i < tablaAux.getRowCount(); i++) {
            tablaAux.setValueAt(this.chkSeleccionarTodos.isSelected(), i, 0);
        }
    }//GEN-LAST:event_chkSeleccionarTodosActionPerformed

    private void btnConsolidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsolidarActionPerformed
        this.realizarConsolidacion();
    }//GEN-LAST:event_btnConsolidarActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsolidar;
    private javax.swing.JCheckBox chkSeleccionarTodos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblConsolidacion;
    // End of variables declaration//GEN-END:variables
}
