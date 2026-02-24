package com.felipemeireles.sistemadebiblioteca.controller;

import com.felipemeireles.sistemadebiblioteca.dao.LivroDAO;
import com.felipemeireles.sistemadebiblioteca.entity.Livro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class LivrosController {

    @FXML
    private TableView<Livro> tabelaLivros;

    @FXML
    private TableColumn<Livro, Integer> colunaId;
    @FXML
    private TableColumn<Livro, String> colTitulo;
    @FXML
    private TableColumn<Livro, String> colAutor;
    @FXML
    private TableColumn<Livro, Integer> colAno;
    @FXML
    private TableColumn<Livro, String> colDisponibilidade;

    @FXML
    private TextField campoPesquisa;

    private final LivroDAO livroDAO = new LivroDAO();
    private ObservableList<Livro> listaCompleta;
    private FilteredList<Livro> listaFiltrada;

    @FXML
    public void initialize() {

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colDisponibilidade.setCellValueFactory(new PropertyValueFactory<>("disponibilidade"));

        listaCompleta = FXCollections.observableArrayList(livroDAO.listarLivros());
        listaFiltrada = new FilteredList<>(listaCompleta, p -> true);

        tabelaLivros.setItems(listaFiltrada);
    }

    @FXML
    private void filtrarLivros() {
        String filtro = campoPesquisa.getText();

        listaFiltrada.setPredicate(livro -> {
            if (filtro == null || filtro.isEmpty()) {
                return true;
            }

            String filtroLower = filtro.toLowerCase();

            return livro.getTitulo().toLowerCase().contains(filtroLower)
                    || livro.getAutor().toLowerCase().contains(filtroLower)
                    || String.valueOf(livro.getAno()).contains(filtroLower)
                    || livro.getDisponibilidade().toLowerCase().contains(filtroLower);
        });
    }

    @FXML
    private void excluirLivroSelecionado(ActionEvent event) {
        Livro livroSelecionado = tabelaLivros.getSelectionModel().getSelectedItem();

        if (livroSelecionado != null) {
            livroDAO.excluirLivro(livroSelecionado.getId());

            listaCompleta.setAll(livroDAO.listarLivros());

        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Nenhum livro selecionado");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecione um livro para excluir.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void abrirTelaAdicionarLivro() {
        Navegador.trocarTela("/com/felipemeireles/sistemadebiblioteca/view/AdicionarLivro.fxml");
    }
}