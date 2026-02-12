package com.felipemeireles.sistemadebiblioteca.dao;

import com.felipemeireles.sistemadebiblioteca.database.ConexaoMySQL;
import com.felipemeireles.sistemadebiblioteca.entity.Aluno;
import com.felipemeireles.sistemadebiblioteca.entity.Livro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlunoDAO {

    public void adicionarAluno(Aluno aluno) {
        String sql = "INSERT INTO alunos (nome, email, cpf, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEmail());
            stmt.setString(3, aluno.getCpf());
            stmt.setString(4, aluno.getTelefone());

            stmt.executeUpdate();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sucesso!");
            alerta.setHeaderText(null);
            alerta.setContentText("Aluno adicionado com sucesso!");

            alerta.showAndWait();

        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro!");
            alerta.setHeaderText(null);
            alerta.setContentText("Algum campo preenchido errado.");
            e.printStackTrace();
        }
    }

    public void excluirAluno(int id) {
        String sql = "DELETE FROM alunos WHERE id = ?";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sucesso!");
            alerta.setHeaderText(null);
            alerta.setContentText("Aluno excluído com sucesso!");
            alerta.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText("Erro ao excluir o livro");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    public ObservableList<Aluno> listarAlunos() {
        ObservableList<Aluno> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM alunos";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Aluno aluno = new Aluno(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("cpf"),
                        rs.getString("telefone")
                );
                lista.add(aluno);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public int contarAlunos() {

        String sql = "SELECT COUNT(*) FROM alunos";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Aluno buscarPorCpf(String cpf) {

        String sql = "SELECT * FROM alunos WHERE cpf = ?";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));
                aluno.setEmail(rs.getString("email"));
                aluno.setCpf(rs.getString("cpf"));
                aluno.setTelefone(rs.getString("telefone"));
                return aluno;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean cpfJaExiste(String cpf) {
        String sql = "SELECT COUNT(*) FROM alunos WHERE cpf = ?";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
