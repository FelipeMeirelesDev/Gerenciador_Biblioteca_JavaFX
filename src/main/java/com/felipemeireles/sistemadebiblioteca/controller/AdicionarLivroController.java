package com.felipemeireles.sistemadebiblioteca.controller;

import com.felipemeireles.sistemadebiblioteca.dao.LivroDAO;
import com.felipemeireles.sistemadebiblioteca.entity.Livro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdicionarLivroController {

    @FXML private TextField campoTitulo, campoAutor, campoAno;

    @FXML
    private void salvarLivro() {

        if (campoTitulo.getText().trim().isEmpty() || campoAutor.getText().trim().isEmpty() || campoAno.getText().trim().isEmpty()){
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos vazios!");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, preencha todos os campos.");
            alerta.showAndWait();
            return;
        }
        String titulo = campoTitulo.getText();
        String autor = campoAutor.getText();
        String anoTexto = campoAno.getText();

        anoTexto = anoTexto.trim();

        if (!anoTexto.matches("\\d+")) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Ano inválido");
            alerta.setHeaderText(null);
            alerta.setContentText("O ano deve conter apenas números.");
            alerta.showAndWait();
            return;
        }

        if (Integer.parseInt(anoTexto) > 2025) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Ano inválido");
            alerta.setHeaderText(null);
            alerta.setContentText("O ano informado não pode ser superior ao ano atual.");
            alerta.showAndWait();
            return;
        }

        int ano = Integer.parseInt(anoTexto);
        Livro livro = new Livro(titulo, autor, ano);
        LivroDAO dao = new LivroDAO();
        dao.adicionarLivro(livro);
        inserirDadosTabela();
    }

    @FXML
    private void limparCampos(){

        campoTitulo.setText(null);
        campoAutor.setText(null);
        campoAno.setText(null);
    }

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

    private LivroDAO livroDAO = new LivroDAO();

    @FXML
    private void voltarTela() {
        Navegador.trocarTela("/com/felipemeireles/sistemadebiblioteca/view/Livros.fxml");
    }

    @FXML
    public void initialize() {

        inserirDadosTabela();
    }

    private void inserirDadosTabela(){
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));

        ObservableList<Livro> livros = FXCollections.observableArrayList(livroDAO.listarLivros());
        tabelaLivros.setItems(livros);
    }
}
