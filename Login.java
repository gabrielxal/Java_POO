package com.mycompany.unidadepoo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.SQLException;

public class Login {

    // Método para abrir a nova janela de login
    public void abrirJanelaLogin() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300); 
        loginFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        // Definindo a fonte
        Font font = new Font("Verdana", Font.PLAIN, 14);

        // Campos de login (usuário e senha)
        JLabel usuarioLabel = new JLabel("ID:");
        usuarioLabel.setFont(font);
        JTextField usuarioField = new JTextField(15);
        usuarioField.setFont(font);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setFont(font);
        JPasswordField senhaField = new JPasswordField(15);
        senhaField.setFont(font);

        // Botão para efetuar o login
        JButton confirmarLoginButton = new JButton("Login");
        confirmarLoginButton.setFont(new Font("Verdana", Font.PLAIN, 12));
        confirmarLoginButton.setBackground(new Color(70, 130, 180)); 
        confirmarLoginButton.setForeground(Color.WHITE);
        confirmarLoginButton.setFocusPainted(false);

        confirmarLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                String senha = new String(senhaField.getPassword()); 

                // Lógica de validação do login
                int userId = validarLogin(usuario, senha);
                if (userId != -1) { 
                    JOptionPane.showMessageDialog(loginFrame, "Login efetuado com sucesso!");

                    // Fecha a janela de login
                    loginFrame.dispose();

                    // Abre a janela de registro de análise e passa o ID do usuário
                    RegistroDeAnalise registro = new RegistroDeAnalise(userId);
                    registro.abrirJanelaRegistro(loginFrame);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Usuário ou senha inválidos!");
                }
            }
        });

        // Configuração de layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginFrame.add(usuarioLabel, gbc);
        gbc.gridx = 1;
        loginFrame.add(usuarioField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginFrame.add(senhaLabel, gbc);
        gbc.gridx = 1;
        loginFrame.add(senhaField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; 
        confirmarLoginButton.setPreferredSize(new Dimension(100, 30)); 
        loginFrame.add(confirmarLoginButton, gbc);

        // Configurações da janela
        loginFrame.setLocationRelativeTo(null); 
        loginFrame.setVisible(true);
    }

    // Método de verificação de login 
    private int validarLogin(String usuario, String senha) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id FROM usuarios WHERE id = ? AND senha = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(usuario)); 
            pstmt.setString(2, senha); 
            ResultSet rs = pstmt.executeQuery();

            // Se encontrar um registro, o login é válido
            if (rs.next()) {
                return rs.getInt("id"); 
            } else {
                return -1; 
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar o login: " + e.getMessage());
            return -1; 
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID deve ser um número!");
            return -1;
        }
    }
}
