package com.felipemeireles.sistemadebiblioteca.controller;

import com.felipemeireles.sistemadebiblioteca.dao.LivroDAO;
import com.felipemeireles.sistemadebiblioteca.entity.Livro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    private LivroDAO livroDAO = new LivroDAO();

    @FXML private TextField campoPesquisa;

    private ObservableList<Livro> listaCompleta; // lista completa vinda do banco
    private FilteredList<Livro> listaFiltrada;   // lista com filtro dinâmico


    @FXML
    private void abrirTelaAdicionarLivro() {
        Navegador.trocarTela("/com/felipemeireles/sistemadebiblioteca/view/AdicionarLivro.fxml");
    }

    @FXML
    public void initialize() {

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colDisponibilidade.setCellValueFactory(new PropertyValueFactory<>("disponibilidade"));

        ObservableList<Livro> livros = FXCollections.observableArrayList(livroDAO.listarLivros());
        listaCompleta = FXCollections.observableArrayList(livroDAO.listarLivros());
        listaFiltrada = new FilteredList<>(listaCompleta, p -> true); // Correto

        tabelaLivros.setItems(listaFiltrada); // use listaFiltrada para permitir o filtro
    }

    @FXML
    private void filtrarLivros() {
        String filtro = campoPesquisa.getText().toLowerCase();

        listaFiltrada.setPredicate(livro -> {
            if (filtro == null || filtro.isEmpty()) {
                return true;
            }

            return livro.getTitulo().toLowerCase().contains(filtro)
                    || livro.getAutor().toLowerCase().contains(filtro)
                    || String.valueOf(livro.getAno()).contains(filtro)
                    || livro.getDisponibilidade().toLowerCase().contains(filtro);
        });
    }


    @FXML
    private void excluirLivroSelecionado(ActionEvent event) {
        Livro livroSelecionado = tabelaLivros.getSelectionModel().getSelectedItem();

        if (livroSelecionado != null) {
            livroDAO.excluirLivro(livroSelecionado.getId());

            ObservableList<Livro> livrosAtualizados = FXCollections.observableArrayList(livroDAO.listarLivros());
            tabelaLivros.setItems(livrosAtualizados);

        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Nenhum livro selecionado");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecione um livro para excluir.");
            alerta.showAndWait();
        }
    }



}
