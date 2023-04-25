/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication7;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewInterface implements ActionListener {

    private JFrame frame;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;
    private String serverName = "localhost";
    private int port = 8888;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void createGUI() {
        frame = new JFrame("Satranç Oyunu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());

        messageField = new JTextField();
        messagePanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Gönder");
        sendButton.addActionListener(this);
        messagePanel.add(sendButton, BorderLayout.EAST);

        frame.add(messagePanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void start() {
        try {
            socket = new Socket(serverName, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                messageArea.append(inputLine + "\n");
            }
        } catch (IOException e) {
            System.out.println("İstemci hatası: " + e.getMessage());
        }
    }

    // Diğer metotlar burada

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String message = messageField.getText();
            messageField.setText("");
            out.println(message);
            messageArea.append("Ben: " + message + "\n");
        }
    }
}
