package com.felipemeireles.sistemadebiblioteca.dao;

import com.felipemeireles.sistemadebiblioteca.database.ConexaoMySQL;
import com.felipemeireles.sistemadebiblioteca.entity.Emprestimo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    public boolean adicionarEmprestimo(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimos (aluno_cpf, livro_id, data_emprestimo, data_devolucao, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emprestimo.getAlunoCpf());
            stmt.setInt(2, emprestimo.getLivroId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucao()));
            stmt.setString(5, emprestimo.getStatus());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText("Erro ao adicionar empréstimo");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
            return false;
        }
    }

    public List<Emprestimo> listarEmprestimosEmAberto() {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT * FROM emprestimos WHERE status = 'EM_ABERTO'";

        try (Connection conn = ConexaoMySQL.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Emprestimo e = new Emprestimo();
                e.setId(rs.getInt("id"));
                e.setAlunoCpf(rs.getString("aluno_cpf"));
                e.setLivroId(rs.getInt("livro_id"));
                e.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                e.setDataDevolucao(rs.getDate("data_devolucao").toLocalDate());
                e.setStatus(rs.getString("status"));
                lista.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Função para devolver o livro (atualiza status)
    public boolean devolverLivro(int idEmprestimo) {
        String sql = "UPDATE emprestimos SET status = 'DEVOLVIDO' WHERE id = ?";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmprestimo);
            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
