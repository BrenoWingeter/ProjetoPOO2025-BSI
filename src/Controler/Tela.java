package Controler;

import Modelo.Personagem;
import Modelo.Caveira;
import Modelo.Hero;
import Modelo.Chaser;
import Modelo.BichinhoVaiVemHorizontal;
import Modelo.BichinhoVaiVemVertical;
import Modelo.ZigueZague;
import Modelo.Escada;
import Modelo.EscadaBloqueada;
import Modelo.Barril;
import Modelo.Pocao;
import Modelo.Moeda;
import Modelo.Chave;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.BichinhoVaiVemVertical;
import Modelo.ZigueZague;
import auxiliar.Posicao;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JButton;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Hero hero;
    private ArrayList<Personagem> faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    private CriadorDeFase criadorDeFase;
    private int faseAtualNumero = 1;
    private boolean jogoTerminado = false;

    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        this.addKeyListener(this);
        
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        criadorDeFase = new CriadorDeFase();
        faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(1));
        hero = criadorDeFase.getHeroi();
        this.atualizaCamera();
    }

    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        /*Criamos um contexto gráfico*/
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        
        /**
         * ***********Desenha cenário de fundo*************
         */
        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "piso1masmorra.png");
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        if (!this.faseAtual.isEmpty()) {
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual);
        }

        // Desenhar HUD (informações do jogador)
        desenharHUD(g2);

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    private void desenharHUD(Graphics g) {
        if (hero != null) {
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.WHITE);
            
            // Informações do herói
            g.drawString("Vida: " + hero.getVida(), 10, 25);
            g.drawString("Pontuação: " + hero.getPontuacao(), 10, 45);
            g.drawString("Moedas: " + hero.getMoedas(), 10, 65);
            g.drawString("Chaves: " + hero.getChaves(), 10, 85);
            g.drawString("Fase: " + faseAtualNumero, 10, 105);
            
            // Verificar se o herói morreu
            if (!hero.estaVivo()) {
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.setColor(Color.RED);
                g.drawString("GAME OVER", 200, 300);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString("Pressione R para reiniciar", 250, 350);
            }
        }
    }

    private void atualizaCamera() {
        if (hero != null) {
            int linha = hero.getPosicao().getLinha();
            int coluna = hero.getPosicao().getColuna();

            cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));
            cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
        }
    }
    
    public void processarAtaque(int linha, int coluna) {
        if (faseAtual == null) return;
        
        boolean atingiuAlgo = false;
        System.out.println(" Atacando posição: linha=" + linha + ", coluna=" + coluna);
        
        // SISTEMA FACILITADO: Verificar área 3x3 ao redor do ataque
        for (int deltaLinha = -1; deltaLinha <= 1; deltaLinha++) {
            for (int deltaColuna = -1; deltaColuna <= 1; deltaColuna++) {
                int linhaVerificar = linha + deltaLinha;
                int colunaVerificar = coluna + deltaColuna;
                
                // Verificar se há algum personagem nesta posição
                for (int i = faseAtual.size() - 1; i >= 1; i--) {
                    Personagem p = faseAtual.get(i);
                    
                    if (p.getPosicao().getLinha() == linhaVerificar && 
                        p.getPosicao().getColuna() == colunaVerificar) {
                        
                        System.out.println("Encontrou " + p.getClass().getSimpleName() + " próximo!");
                        
                        // Verificar se é um barril
                        if (p instanceof Barril) {
                            Barril barril = (Barril) p;
                            barril.quebrar();
                            System.out.println("Baú destruído!");
                            atingiuAlgo = true;
                            continue;
                        }
                        
                        // Verificar se é inimigo (qualquer classe mortal)
                        if (p.isbMortal() || p instanceof Chaser || p instanceof Caveira || 
                            p instanceof BichinhoVaiVemHorizontal || p instanceof BichinhoVaiVemVertical || 
                            p instanceof ZigueZague) {
                            
                            faseAtual.remove(p);
                            hero.adicionarPontuacao(75);
                            System.out.println(p.getClass().getSimpleName() + " eliminado! +75 pontos!");
                            atingiuAlgo = true;
                        }
                    }
                }
            }
        }
        
        if (!atingiuAlgo) {
            System.out.println("Nenhum inimigo próximo!");
        } else {
            System.out.println("Ataque bem-sucedido!");
        }
    }

    private void interagirComEscadas() {
        if (hero == null || faseAtual == null) return;
        
        int heroLinha = hero.getPosicao().getLinha();
        int heroColuna = hero.getPosicao().getColuna();
        
        // Verificar todas as 8 direções ao redor do herói (incluindo diagonais)
        int[] deltaLinhas = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] deltaColunas = {-1, 0, 1, -1, 1, -1, 0, 1};
        
        boolean escadaEncontrada = false;
        
        for (int i = 0; i < 8; i++) {
            int novaLinha = heroLinha + deltaLinhas[i];
            int novaColuna = heroColuna + deltaColunas[i];
            
            // Verificar se a posição está dentro dos limites do mapa
            if (novaLinha >= 0 && novaLinha < Consts.MUNDO_ALTURA && 
                novaColuna >= 0 && novaColuna < Consts.MUNDO_LARGURA) {
                
                // Procurar escada bloqueada nesta posição
                for (Personagem p : faseAtual) {
                    if (p instanceof EscadaBloqueada) {
                        EscadaBloqueada escadaBloqueada = (EscadaBloqueada) p;
                        
                        if (escadaBloqueada.getPosicao().getLinha() == novaLinha && 
                            escadaBloqueada.getPosicao().getColuna() == novaColuna) {
                            
                            if (escadaBloqueada.isBloqueada()) {
                                if (hero.getChaves() > 0) {
                                    hero.usarChave();
                                    escadaBloqueada.desbloquear();
                                    System.out.println("Escada desbloqueada com sucesso!");
                                    System.out.println("Chaves restantes: " + hero.getChaves());
                                    System.out.println("Agora você pode pisar na escada para avançar!");
                                    escadaEncontrada = true;
                                    repaint(); // Atualizar a tela imediatamente
                                } else {
                                    System.out.println("Você precisa de uma chave para desbloquear esta escada!");
                                    escadaEncontrada = true;
                                }
                            } else {
                                System.out.println("Esta escada já está desbloqueada!");
                                escadaEncontrada = true;
                            }
                        }
                    }
                }
            }
        }
        
        if (!escadaEncontrada) {
            System.out.println("Nenhuma escada bloqueada encontrada ao seu redor.");
            System.out.println("Dica: Fique perto de uma escada bloqueada e pressione E!");
        }
    }

    private void verificarEscada() {
        if (hero != null && faseAtual != null && hero.estaVivo()) {
            for (Personagem p : faseAtual) {
                // Verificar escada normal
                if (p instanceof Escada) {
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        if (faseAtualNumero == 6) {
                            // Na fase final, verificar se coletou todas as moedas
                            boolean temMoedas = false;
                            for (Personagem moeda : faseAtual) {
                                if (moeda instanceof Moeda) {
                                    temMoedas = true;
                                    break;
                                }
                            }
                            
                            if (!temMoedas) {
                                System.out.println("Parabéns! Você completou o jogo!");
                                mostrarTelaFinal();
                            } else {
                                System.out.println("Colete todas as moedas antes de finalizar o jogo!");
                            }
                        } else {
                            System.out.println("Pisando na escada normal - Avançando para próxima fase!");
                            proximaFase();
                        }
                        return;
                    }
                }
                
                // Verificar escada desbloqueada
                if (p instanceof EscadaBloqueada) {
                    EscadaBloqueada escadaBloqueada = (EscadaBloqueada) p;
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        if (!escadaBloqueada.isBloqueada()) {
                            if (faseAtualNumero == 5) {
                                // FASE 5: Ir direto para créditos
                                System.out.println("Fase 5 completada! Mostrando créditos finais!");
                                mostrarTelaFinal();
                            } else {
                                System.out.println("Pisando na escada desbloqueada - Avançando para próxima fase!");
                                proximaFase();
                            }
                            return;
                        } else {
                            System.out.println("Esta escada ainda está bloqueada! Pressione E perto dela para tentar abrir.");
                        }
                    }
                }
            }
        }
    }

    private void proximaFase() {
        if (faseAtualNumero < 6) { // Agora são 6 fases
            faseAtualNumero++;
            
            if (faseAtualNumero == 6) {
                // Fase final especial - mostrar tela de créditos após coletar todas as moedas
                mostrarFaseFinal();
                return;
            }
            
            // Manter estatísticas do herói entre as fases
            int vidaAtual = hero.getVida();
            int pontuacaoAtual = hero.getPontuacao();
            int moedasAtuais = hero.getMoedas();
            int chavesAtuais = hero.getChaves();
            
            faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(faseAtualNumero));
            hero = criadorDeFase.getHeroi();
            
            // Restaurar estatísticas
            hero.setVida(vidaAtual);
            hero.setPontuacao(pontuacaoAtual);
            hero.setMoedas(moedasAtuais);
            hero.setChaves(chavesAtuais);
            
            this.atualizaCamera();
            System.out.println("🏆 Avançou para a Fase " + faseAtualNumero + "!");
        }
    }

    private void mostrarFaseFinal() {
        // Carregar fase final
        int vidaAtual = hero.getVida();
        int pontuacaoAtual = hero.getPontuacao();
        int moedasAtuais = hero.getMoedas();
        int chavesAtuais = hero.getChaves();
        
        faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(6));
        hero = criadorDeFase.getHeroi();
        
        // Restaurar estatísticas
        hero.setVida(vidaAtual);
        hero.setPontuacao(pontuacaoAtual);
        hero.setMoedas(moedasAtuais);
        hero.setChaves(chavesAtuais);
        
        this.atualizaCamera();
        System.out.println("🎉 FASE FINAL! Colete todas as moedas e pise na escada para ver os créditos!");
    }

    private void mostrarTelaFinal() {
        jogoTerminado = true;
        TelaFinal telaFinal = new TelaFinal(hero.getPontuacao());
        telaFinal.setVisible(true);
        this.setVisible(false);
    }

    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                if (!jogoTerminado) {
                    repaint();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }

    public void keyPressed(KeyEvent e) {
        if (jogoTerminado) {
            return; // Não processar teclas se o jogo terminou
        }
        
        if (e.getKeyCode() == KeyEvent.VK_C) {
            this.faseAtual.clear();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (hero != null && hero.estaVivo()) {
                hero.moveUp();
                verificarEscada();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (hero != null && hero.estaVivo()) {
                hero.moveDown();
                verificarEscada();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (hero != null && hero.estaVivo()) {
                hero.moveLeft();
                verificarEscada();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (hero != null && hero.estaVivo()) {
                hero.moveRight();
                verificarEscada();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // Atacar
            if (hero != null && hero.estaVivo() && !hero.isAtacando()) {
                hero.atacar();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_Y) {
            // TESTE: Carregar fase de teste da Caveira
            System.out.println("Carregando fase de teste da Caveira...");
            faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(-1));
            hero = criadorDeFase.getHeroi();
            faseAtualNumero = -1;
            this.atualizaCamera();
        } else if (e.getKeyCode() == KeyEvent.VK_T) {
            // TESTE: Carregar fase de teste com Chaser
            System.out.println("🧪 Carregando fase de teste...");
            faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(0));
            hero = criadorDeFase.getHeroi();
            faseAtualNumero = 0;
            this.atualizaCamera();
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            // Interagir com escadas bloqueadas ao redor
            if (hero != null && hero.estaVivo()) {
                interagirComEscadas();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            salvarFase("fase_salva.ser");
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            carregarFase("fase_salva.ser");
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            // Reiniciar fase
            faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(faseAtualNumero));
            hero = criadorDeFase.getHeroi();
            this.atualizaCamera();
        }
        
        this.atualizaCamera();
        
        if (hero != null) {
            this.setTitle("Setas=Mover | ESPAÇO=Atacar | E=Interagir | R=Reiniciar - Fase:" + faseAtualNumero + 
                    " | Vida:" + hero.getVida() + "/100 | Pontos:" + hero.getPontuacao() + 
                    " | Chaves:" + hero.getChaves());
        } else {
            this.setTitle("POO2023-1 - Skooter - Fase " + faseAtualNumero);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (hero != null && hero.estaVivo() && !jogoTerminado) {
            int x = e.getX();
            int y = e.getY();

            this.setTitle("X: " + x + ", Y: " + y
                    + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE));

            this.hero.getPosicao().setPosicao(y / Consts.CELL_SIDE, x / Consts.CELL_SIDE);
            verificarEscada();

            repaint();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
    
    private void salvarFase(String nomeArquivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(faseAtual);
            oos.writeInt(faseAtualNumero);
            System.out.println("Fase salva com sucesso em " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar a fase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarFase(String nomeArquivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            faseAtual = (ArrayList<Personagem>) ois.readObject();
            faseAtualNumero = ois.readInt();

            // Atualiza o cenário para cada personagem
            for (Personagem p : faseAtual) {
                Desenho.setCenario(this);
            }
            // Reatribui o herói
            for (Personagem p : faseAtual) {
                if (p instanceof Hero) {
                    hero = (Hero) p;
                    break;
                }
            }
            // Atualiza a câmera com base no novo herói
            atualizaCamera();

            System.out.println("Fase carregada com sucesso!");
            repaint();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar a fase: " + e.getMessage());
            e.printStackTrace();
        }
    }
}