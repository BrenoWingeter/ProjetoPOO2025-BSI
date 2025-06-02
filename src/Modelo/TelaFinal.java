package Controler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaFinal extends JFrame {
    
    private int pontuacaoFinal;
    
    public TelaFinal(int pontuacao) {
        this.pontuacaoFinal = pontuacao;
        initComponents();
    }
    
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Fim de Jogo - Parabéns!");
        setResizable(false);
        setLayout(new BorderLayout());
        
        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Título
        JLabel titulo = new JLabel("PARABÉNS!");
        titulo.setFont(new Font("Arial", Font.BOLD, 48));
        titulo.setForeground(Color.YELLOW);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtítulo
        JLabel subtitulo = new JLabel("Você completou todas as fases!");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Pontuação
        JLabel labelPontuacao = new JLabel("Pontuação Final: " + pontuacaoFinal);
        labelPontuacao.setFont(new Font("Arial", Font.BOLD, 20));
        labelPontuacao.setForeground(Color.CYAN);
        labelPontuacao.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Créditos
        JLabel creditos1 = new JLabel("Criado por:");
        creditos1.setFont(new Font("Arial", Font.PLAIN, 18));
        creditos1.setForeground(Color.WHITE);
        creditos1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel creditos2 = new JLabel("Breno Wingeter de Castilho");
        creditos2.setFont(new Font("Arial", Font.BOLD, 24));
        creditos2.setForeground(Color.GREEN);
        creditos2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel creditos3 = new JLabel("Gabriel de Oliveira Merenciano");
        creditos3.setFont(new Font("Arial", Font.BOLD, 24));
        creditos3.setForeground(Color.GREEN);
        creditos3.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Botão para fechar
        JButton botaoFechar = new JButton("Fechar Jogo");
        botaoFechar.setFont(new Font("Arial", Font.PLAIN, 16));
        botaoFechar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Adicionar componentes
        mainPanel.add(titulo);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(subtitulo);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(labelPontuacao);
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(creditos1);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(creditos2);
        mainPanel.add(creditos3);
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(botaoFechar);
        
        add(mainPanel, BorderLayout.CENTER);
        
        setSize(600, 500);
        setLocationRelativeTo(null);
    }
}