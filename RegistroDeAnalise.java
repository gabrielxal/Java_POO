package com.mycompany.unidadepoo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistroDeAnalise {

    private File selectedFile;
    private final int userId; 

    public RegistroDeAnalise(int userId) {
        this.userId = userId;
    }

    // Abrir frame registro
    public void abrirJanelaRegistro(JFrame mainFrame) {
        JFrame registroFrame = new JFrame("Registro de Análise");
        registroFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registroFrame.setSize(400, 300);
        registroFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        // Configurações do Frame
        Font font = new Font("Verdana", Font.PLAIN, 14);
        JLabel tituloLabel = new JLabel("Registro:");
        tituloLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        registroFrame.add(tituloLabel, gbc);
        JLabel descricaoLabel = new JLabel("Descrição:");
        descricaoLabel.setFont(font);
        gbc.gridwidth = 1; 
        gbc.gridx = 0;
        gbc.gridy = 1;
        registroFrame.add(descricaoLabel, gbc);

        JTextField descricaoField = new JTextField(20);
        descricaoField.setFont(font);
        gbc.gridx = 1;
        registroFrame.add(descricaoField, gbc);

        // Botão para selecionar arquivo PDF
        JButton selecionarArquivoButton = new JButton("Selecionar Arquivo");
        selecionarArquivoButton.setFont(font);
        selecionarArquivoButton.setBackground(new Color(70, 130, 180)); 
        selecionarArquivoButton.setForeground(Color.WHITE);
        selecionarArquivoButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; 
        registroFrame.add(selecionarArquivoButton, gbc);
        selecionarArquivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    JOptionPane.showMessageDialog(registroFrame, "Arquivo selecionado: " + selectedFile.getName());
                }
            }
        });

        // Botão para cadastrar a análise
        JButton confirmarCadastroButton = new JButton("Registrar");
        confirmarCadastroButton.setFont(font);
        confirmarCadastroButton.setBackground(new Color(70, 130, 180)); 
        confirmarCadastroButton.setForeground(Color.WHITE);
        confirmarCadastroButton.setFocusPainted(false);
        gbc.gridy = 3;
        registroFrame.add(confirmarCadastroButton, gbc);
        confirmarCadastroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String descricao = descricaoField.getText();
                if (selectedFile != null && descricao != null && !descricao.isEmpty()) {
                    try {
                        // Gerar lote e salvar no banco de dados
                        String lote = salvarAnaliseNoBancoDeDados(descricao, selectedFile, userId);
                        JOptionPane.showMessageDialog(registroFrame, "Análise cadastrada com sucesso!\nLote: " + lote);
                        
                        // Limpar os campos após o cadastro
                        descricaoField.setText("");
                        selectedFile = null; 
                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(registroFrame, "Erro ao salvar no banco de dados: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(registroFrame, "Preencha a descrição e selecione um arquivo.");
                }
            }
        });

        // Botão para voltar a tela de login
        JButton voltarButton = new JButton("Voltar para Login");
        voltarButton.setFont(font);
        voltarButton.setBackground(new Color(70, 130, 180)); 
        voltarButton.setForeground(Color.WHITE);
        voltarButton.setFocusPainted(false);
        gbc.gridy = 4;
        registroFrame.add(voltarButton, gbc);
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registroFrame.dispose();
                mainFrame.setVisible(true);
            }
        });
        registroFrame.setLocationRelativeTo(null); 
        registroFrame.setVisible(true);
     }

    // Método para salvar a análise no banco de dados e gerar o lote
    private String salvarAnaliseNoBancoDeDados(String descricao, File arquivoPDF, int userId) throws SQLException, IOException {
       
        // Gerar lote baseado no ID do usuário
        String lote = gerarLote(userId);

        // Salvar a análise e o arquivo PDF no banco de dados
        try (Connection conn = Database.getConnection(); 
             FileInputStream fis = new FileInputStream(arquivoPDF)) {
            
            String sql = "INSERT INTO analise (descricao, arquivo_pdf, id_usuario, lote) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, descricao);
            pstmt.setBinaryStream(2, fis, (int) arquivoPDF.length());
            pstmt.setInt(3, userId);  
            pstmt.setString(4, lote); 
            pstmt.executeUpdate();
        }

        return lote; 
    }

    // Método para gerar o lote
    private String gerarLote(int userId) throws SQLException {
     
        String[] sequenciaAlfabetica = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String letra = sequenciaAlfabetica[(userId - 1) % sequenciaAlfabetica.length]; 

        // Contar quantos registros o usuário já tem no banco de dados
        int numeroRegistros = contarRegistrosUsuario(userId);

        // Concatenar a ordem afalbetica com id do usuario e quantidade registro somando mais 1
        return letra + userId + "-" + (numeroRegistros + 1);  
    }

    // Método para contabilizar a quantidade de registro
    private int contarRegistrosUsuario(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM analise WHERE id_usuario = ?";
        try (Connection conn = Database.getConnection(); 
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);  
                }
            }
        }
        return 0; 
    }
}
