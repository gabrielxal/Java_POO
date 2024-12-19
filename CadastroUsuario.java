package com.mycompany.unidadepoo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.SQLException;

public class CadastroUsuario {

    // Método para criar a janela de cadastro
    public static void abrirFormularioCadastro(JFrame frame) {
        JFrame cadastroFrame = new JFrame("Cadastro de Usuário");
        cadastroFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cadastroFrame.setSize(600, 600);
        cadastroFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        // Definindo a fonte
        Font font = new Font("Verdana", Font.PLAIN, 16);

        // Criar os componentes de entrada de dados
        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setFont(font);
        JTextField nomeField = new JTextField(20);
        nomeField.setFont(font);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setFont(font);
        JPasswordField senhaField = new JPasswordField(20);
        senhaField.setFont(font);

        JLabel dataLabel = new JLabel("Data Nascimento:");
        dataLabel.setFont(font);
        JTextField dataField = new JTextField(20);
        dataField.setFont(font);

        JLabel projetoLabel = new JLabel("Projeto:");
        projetoLabel.setFont(font);
        JTextField projetoField = new JTextField(20);
        projetoField.setFont(font);

        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.setFont(font);
        cadastrarButton.setBackground(new Color(70, 130, 180)); 
        cadastrarButton.setForeground(Color.WHITE);
        cadastrarButton.setFocusPainted(false);

        // Adicionar ação ao botão de cadastro
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String senha = new String(senhaField.getPassword());
                String data = dataField.getText();
                String projeto = projetoField.getText();

                // Inserir o novo usuário no banco de dados
                try (Connection conn = Database.getConnection()) {
                    String sql = "INSERT INTO usuarios (nome, senha, data_nascimento, projeto) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, nome);
                    pstmt.setString(2, senha);
                    pstmt.setString(3, data);
                    pstmt.setString(4, projeto);
                    pstmt.executeUpdate();

                    // Obter o ID gerado
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int idGerado = generatedKeys.getInt(1);
                        JOptionPane.showMessageDialog(cadastroFrame, "Usuário cadastrado com sucesso!\nID: " + idGerado);
                    } else {
                        JOptionPane.showMessageDialog(cadastroFrame, "Erro ao obter o ID do usuário.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(cadastroFrame, "Erro ao cadastrar o usuário: " + ex.getMessage());
                }

                cadastroFrame.dispose();
                frame.setVisible(true);
            }
        });

        // Configurando o layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        cadastroFrame.add(nomeLabel, gbc);
        gbc.gridx = 1;
        cadastroFrame.add(nomeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        cadastroFrame.add(senhaLabel, gbc);
        gbc.gridx = 1;
        cadastroFrame.add(senhaField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        cadastroFrame.add(dataLabel, gbc);
        gbc.gridx = 1;
        cadastroFrame.add(dataField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        cadastroFrame.add(projetoLabel, gbc);
        gbc.gridx = 1;
        cadastroFrame.add(projetoField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; 
        cadastrarButton.setPreferredSize(new Dimension(150, 40)); 
        cadastroFrame.add(cadastrarButton, gbc);

        // Configurações da janela
        cadastroFrame.setLocationRelativeTo(null); 
        cadastroFrame.setVisible(true);
        frame.setVisible(false);
    }
}
