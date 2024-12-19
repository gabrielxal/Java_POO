package com.mycompany.unidadepoo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {

    public static void main(String[] args) {
        // Cria a janela principal
        JFrame frame = new JFrame("Sistema de Cadastro e Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        // Definindo a fonte
        Font font = new Font("Verdana", Font.PLAIN, 16); 

        // Botão de Cadastro
        JButton cadastroButton = new JButton("Cadastro");
        cadastroButton.setFont(font);
        cadastroButton.setBackground(new Color(70, 130, 180)); 
        cadastroButton.setForeground(Color.WHITE);
        cadastroButton.setFocusPainted(false); 
        cadastroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroUsuario.abrirFormularioCadastro(frame);
            }
        });

        // Botão de Login
        JButton loginButton = new JButton("Login");
        loginButton.setFont(font);
        loginButton.setBackground(new Color(70, 130, 180)); 
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                login.abrirJanelaLogin();
                frame.setVisible(false);
            }
        });

        // Botão de Fechar
        JButton fecharButton = new JButton("Fechar");
        fecharButton.setFont(font);
        fecharButton.setBackground(new Color(70, 130, 180));
        fecharButton.setForeground(Color.WHITE);
        fecharButton.setFocusPainted(false);
        fecharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });

        // Configurando o layout inicial
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(cadastroButton, gbc);
        gbc.gridy = 1;
        frame.add(loginButton, gbc);
        gbc.gridy = 2;
        frame.add(fecharButton, gbc);
        
        // Configurações da janela
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true); 
    }
}