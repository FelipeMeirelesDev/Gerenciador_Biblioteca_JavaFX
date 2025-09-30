package com.felipemeireles.sistemadebiblioteca.controller;

import com.felipemeireles.sistemadebiblioteca.dao.EmprestimoDAO;
import com.felipemeireles.sistemadebiblioteca.entity.Emprestimo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DevolverLivroController {

    @FXML private TableView<Emprestimo> tabelaEmprestimos;
    @FXML private TableColumn<Emprestimo, Integer> colunaId;
    @FXML private TableColumn<Emprestimo, String> colunaAlunoCpf;
    @FXML private TableColumn<Emprestimo, Integer> colunaLivroId;
    @FXML private TableColumn<Emprestimo, String> colunaDataEmprestimo;
    @FXML private TableColumn<Emprestimo, String> colunaDataDevolucao;
    @FXML private TableColumn<Emprestimo, String> colunaStatus;

    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    @FXML
    public void initialize() {
        carregarEmprestimos();
    }

    private void carregarEmprestimos() {
        colunaId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        colunaAlunoCpf.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("alunoCpf"));
        colunaLivroId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("livroId"));
        colunaDataEmprestimo.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dataEmprestimo"));
        colunaDataDevolucao.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dataDevolucao"));
        colunaStatus.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));

        ObservableList<Emprestimo> emprestimos =
                FXCollections.observableArrayList(emprestimoDAO.listarEmprestimosEmAberto());
        tabelaEmprestimos.setItems(emprestimos);
    }

    @FXML
    private void confirmarDevolucao() {
        Emprestimo emprestimoSelecionado = tabelaEmprestimos.getSelectionModel().getSelectedItem();

        if (emprestimoSelecionado == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Nenhum Empréstimo Selecionado");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecione um empréstimo para devolver.");
            alerta.showAndWait();
            return;
        }

        boolean sucesso = emprestimoDAO.devolverLivro(emprestimoSelecionado.getId());

        if (sucesso) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Devolução Confirmada");
            alerta.setHeaderText(null);
            alerta.setContentText("Livro devolvido com sucesso!");
            alerta.showAndWait();

            carregarEmprestimos(); // 🔄 atualiza a tabela e remove o devolvido
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText(null);
            alerta.setContentText("Não foi possível devolver o livro.");
            alerta.showAndWait();
        }
    }
}
