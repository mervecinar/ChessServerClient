/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.fsm.tcpserver2023;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author skaya
 */
public class Frm_Server extends javax.swing.JFrame {

    /**
     * Creates new form Frm_Server
     */
    Server server;

    public static DefaultListModel lst_clients_model = new DefaultListModel();
    public static DefaultListModel lst_messagesFromClient_model = new DefaultListModel();

    public Frm_Server() {
        initComponents();
        this.lst_clients.setModel(lst_clients_model);
        this.lst_messagesFromClient.setModel(lst_messagesFromClient_model);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txt_port = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        lst_clients = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        lst_messagesFromClient = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        txta_messageToClient = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        btn_stopServer = new javax.swing.JButton();
        btn_startServer = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btn_sendMessageBroadcast = new javax.swing.JButton();
        btn_sendSelected = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_port.setText("5000");
        getContentPane().add(txt_port, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, -1, -1));

        jScrollPane1.setViewportView(lst_clients);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, 250, 180));

        jScrollPane2.setViewportView(lst_messagesFromClient);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 580, 150));

        txta_messageToClient.setColumns(20);
        txta_messageToClient.setRows(5);
        jScrollPane3.setViewportView(txta_messageToClient);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel1.setText("Port");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 37, -1));

        btn_stopServer.setText("Stop");
        btn_stopServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_stopServerActionPerformed(evt);
            }
        });
        getContentPane().add(btn_stopServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 75, -1));

        btn_startServer.setText("Start");
        btn_startServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_startServerActionPerformed(evt);
            }
        });
        getContentPane().add(btn_startServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 70, -1));

        jLabel2.setText("Message");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 80, -1));

        jLabel3.setText("Messages");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 80, -1));

        jLabel4.setText("Clients");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 80, -1));

        btn_sendMessageBroadcast.setText("Broadcast");
        btn_sendMessageBroadcast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendMessageBroadcastActionPerformed(evt);
            }
        });
        getContentPane().add(btn_sendMessageBroadcast, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 100, -1));

        btn_sendSelected.setText("Send Selected");
        btn_sendSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendSelectedActionPerformed(evt);
            }
        });
        getContentPane().add(btn_sendSelected, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_startServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_startServerActionPerformed
        // TODO add your handling code here:
        String port = txt_port.getText();
        this.server = new Server(Integer.parseInt(port));
        this.server.Listen();
    }//GEN-LAST:event_btn_startServerActionPerformed

    private void btn_stopServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_stopServerActionPerformed
        // TODO add your handling code here:
        this.server.Stop();
    }//GEN-LAST:event_btn_stopServerActionPerformed

    private void btn_sendMessageBroadcastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendMessageBroadcastActionPerformed
        try {
            // TODO add your handling code here:

            this.server.SendBroadcast(txta_messageToClient.getText());
        } catch (IOException ex) {
            Logger.getLogger(Frm_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btn_sendMessageBroadcastActionPerformed

    private void btn_sendSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendSelectedActionPerformed
        // TODO add your handling code here:
        int index = lst_clients.getSelectedIndex();
        if (index >= 0) {
            try {
                this.server.SendToClient(txta_messageToClient.getText(), index);
            } catch (IOException ex) {
                Logger.getLogger(Frm_Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }//GEN-LAST:event_btn_sendSelectedActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frm_Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frm_Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frm_Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frm_Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frm_Server().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_sendMessageBroadcast;
    private javax.swing.JButton btn_sendSelected;
    private javax.swing.JButton btn_startServer;
    private javax.swing.JButton btn_stopServer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> lst_clients;
    private javax.swing.JList<String> lst_messagesFromClient;
    private javax.swing.JTextField txt_port;
    private javax.swing.JTextArea txta_messageToClient;
    // End of variables declaration//GEN-END:variables
}
