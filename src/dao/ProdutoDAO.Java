package dao;

import model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    // Cria a tabela se não existir
    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS produto (" +
                     "codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "nome TEXT NOT NULL," +
                     "quantidade INTEGER NOT NULL)";
        try (Connection con = Conexao.conectar();
             Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    // INSERIR produto
    public void inserir(Produto p) throws Exception {
        // Validações
        if (p.getNome() == null || p.getNome().trim().isEmpty())
            throw new Exception("O nome do produto é obrigatório!");
        if (p.getQuantidade() < 0)
            throw new Exception("A quantidade deve ser maior ou igual a zero!");

        String sql = "INSERT INTO produto (nome, quantidade) VALUES (?, ?)";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setInt(2, p.getQuantidade());
            ps.executeUpdate();
        }
    }

    // ATUALIZAR quantidade
    public void atualizar(int codigo, int novaQuantidade) throws Exception {
        if (novaQuantidade < 0)
            throw new Exception("A quantidade deve ser maior ou igual a zero!");

        String sql = "UPDATE produto SET quantidade = ? WHERE codigo = ?";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, novaQuantidade);
            ps.setInt(2, codigo);
            ps.executeUpdate();
        }
    }

    // EXCLUIR produto
    public void excluir(int codigo) throws SQLException {
        String sql = "DELETE FROM produto WHERE codigo = ?";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
        }
    }

    // LISTAR todos
    public List<Produto> listarTodos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try (Connection con = Conexao.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Produto(
                    rs.getInt("codigo"),
                    rs.getString("nome"),
                    rs.getInt("quantidade")
                ));
            }
        }
        return lista;
    }

    // LISTAR com estoque baixo (< 10)
    public List<Produto> listarEstoqueBaixo() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE quantidade < 10";
        try (Connection con = Conexao.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Produto(
                    rs.getInt("codigo"),
                    rs.getString("nome"),
                    rs.getInt("quantidade")
                ));
            }
        }
        return lista;
    }
}