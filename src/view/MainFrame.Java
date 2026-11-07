package view;

import backup.BackupService;
import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private ProdutoDAO dao = new ProdutoDAO();
    private DefaultTableModel tableModel;
    private JTable tabela;

    // Campos de texto
    private JTextField campoNome = new JTextField(15);
    private JTextField campoQtd = new JTextField(5);
    private JTextField campoCodAtualizar = new JTextField(5);
    private JTextField campoNovaQtd = new JTextField(5);
    private JTextField campoCodExcluir = new JTextField(5);

    public MainFrame() {
        dao.criarTabela();
        setTitle("Gerenciamento de Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel de ações (esquerda)
        JPanel painelAcoes = new JPanel();
        painelAcoes.setLayout(new BoxLayout(painelAcoes, BoxLayout.Y_AXIS));
        painelAcoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- INSERIR ---
        painelAcoes.add(new JLabel("=== Inserir Produto ==="));
        painelAcoes.add(new JLabel("Nome:"));
        painelAcoes.add(campoNome);
        painelAcoes.add(new JLabel("Quantidade:"));
        painelAcoes.add(campoQtd);
        JButton btnInserir = new JButton("Inserir");
        btnInserir.addActionListener(e -> inserirProduto());
        painelAcoes.add(btnInserir);

        painelAcoes.add(Box.createVerticalStrut(10));

        // --- ATUALIZAR ---
        painelAcoes.add(new JLabel("=== Atualizar Quantidade ==="));
        painelAcoes.add(new JLabel("Código:"));
        painelAcoes.add(campoCodAtualizar);
        painelAcoes.add(new JLabel("Nova Quantidade:"));
        painelAcoes.add(campoNovaQtd);
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizarProduto());
        painelAcoes.add(btnAtualizar);

        painelAcoes.add(Box.createVerticalStrut(10));

        // --- EXCLUIR ---
        painelAcoes.add(new JLabel("=== Excluir Produto ==="));
        painelAcoes.add(new JLabel("Código:"));
        painelAcoes.add(campoCodExcluir);
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirProduto());
        painelAcoes.add(btnExcluir);

        // --- BOTÕES DE LISTAGEM E BACKUP ---
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnListarTodos = new JButton("Listar Todos");
        JButton btnListarBaixo = new JButton("Estoque < 10");
        JButton btnBackup = new JButton("Fazer Backup");

        btnListarTodos.addActionListener(e -> listarTodos());
        btnListarBaixo.addActionListener(e -> listarEstoqueBaixo());
        btnBackup.addActionListener(e -> fazerBackup());

        painelBotoes.add(btnListarTodos);
        painelBotoes.add(btnListarBaixo);
        painelBotoes.add(btnBackup);

        // --- TABELA ---
        tableModel = new DefaultTableModel(
            new String[]{"Código", "Nome", "Quantidade"}, 0);
        tabela = new JTable(tableModel);

        add(painelAcoes, BorderLayout.WEST);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        setVisible(true);
        listarTodos(); // carrega ao abrir
    }

    private void inserirProduto() {
        try {
            String nome = campoNome.getText();
            int qtd = Integer.parseInt(campoQtd.getText());
            dao.inserir(new Produto(nome, qtd));
            JOptionPane.showMessageDialog(this, "Produto inserido!");
            campoNome.setText(""); campoQtd.setText("");
            listarTodos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarProduto() {
        try {
            int cod = Integer.parseInt(campoCodAtualizar.getText());
            int qtd = Integer.parseInt(campoNovaQtd.getText());
            dao.atualizar(cod, qtd);
            JOptionPane.showMessageDialog(this, "Produto atualizado!");
            listarTodos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valores inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        try {
            int cod = Integer.parseInt(campoCodExcluir.getText());
            dao.excluir(cod);
            JOptionPane.showMessageDialog(this, "Produto excluído!");
            listarTodos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarTodos() {
        try {
            tableModel.setRowCount(0);
            for (Produto p : dao.listarTodos())
                tableModel.addRow(new Object[]{p.getCodigo(), p.getNome(), p.getQuantidade()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarEstoqueBaixo() {
        try {
            tableModel.setRowCount(0);
            for (Produto p : dao.listarEstoqueBaixo())
                tableModel.addRow(new Object[]{p.getCodigo(), p.getNome(), p.getQuantidade()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fazerBackup() {
        try {
            List<Produto> produtos = dao.listarTodos();
            new BackupService().salvarEmArquivo(produtos, "backup_estoque.txt");
            JOptionPane.showMessageDialog(this, "Backup salvo em backup_estoque.txt!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}