package com.felipemeireles.sistemadebiblioteca.dao;

import com.felipemeireles.sistemadebiblioteca.controller.AdicionarLivroController;
import com.felipemeireles.sistemadebiblioteca.database.ConexaoMySQL;
import com.felipemeireles.sistemadebiblioteca.entity.Livro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LivroDAO {

    public void adicionarLivro(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, ano, disponibilidade) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getAno());
            stmt.setString(4, livro.getDisponibilidade());

            stmt.executeUpdate();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sucesso!");
            alerta.setHeaderText(null);
            alerta.setContentText("Livro adicionado com sucesso!");

            alerta.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void excluirLivro(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sucesso!");
            alerta.setHeaderText(null);
            alerta.setContentText("Livro excluído com sucesso!");
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

    public ObservableList<Livro> listarLivros() {
        ObservableList<Livro> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM livros";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano")
                );
                lista.add(livro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public int contarLivros() {

        String sql = "SELECT COUNT(*) FROM livros";

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

    public Livro buscarPorId(int id) {

        String sql = "SELECT * FROM livros WHERE id = ?";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setAno(rs.getInt("ano"));
                livro.setDisponibilidade(rs.getString("disponibilidade"));
                return livro;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
