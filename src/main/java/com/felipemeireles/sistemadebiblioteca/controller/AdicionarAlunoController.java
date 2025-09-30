package com.felipemeireles.sistemadebiblioteca.controller;

import com.felipemeireles.sistemadebiblioteca.dao.AlunoDAO;
import com.felipemeireles.sistemadebiblioteca.dao.LivroDAO;
import com.felipemeireles.sistemadebiblioteca.entity.Aluno;
import com.felipemeireles.sistemadebiblioteca.entity.Livro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdicionarAlunoController {

    @FXML
    private TextField campoNome, campoCPF, campoEmail, campoTelefone;

    @FXML private void salvarAluno() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String cpf = campoCPF.getText();
        String telefone = campoTelefone.getText();

        if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || telefone.isEmpty()){
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos vazios!");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, preencha todos os campos.");
            alerta.showAndWait();
            return;
        }

        if (!cpf.matches("\\d+")) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("CPF inválido");
            alerta.setHeaderText(null);
            alerta.setContentText("O CPF deve conter apenas números.");
            alerta.showAndWait();
            return;
        }

        if (!telefone.matches("\\d+")) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Telefone inválido");
            alerta.setHeaderText(null);
            alerta.setContentText("O telefone deve conter apenas números.");
            alerta.showAndWait();
            return;
        }

        Aluno aluno = new Aluno(nome,email,cpf,telefone);
        AlunoDAO dao = new AlunoDAO();
        dao.adicionarAluno(aluno);
        inserirDadosTabela();
    }

    @FXML
    private void limparCampos(){

        campoNome.setText(null);
        campoEmail.setText(null);
        campoTelefone.setText(null);
        campoCPF.setText(null);
    }

    @FXML
    private void voltarTela() {
        Navegador.trocarTela("/com/felipemeireles/sistemadebiblioteca/view/Alunos.fxml");
    }

    @FXML
    private TableView<Aluno> tabelaAlunos;

    @FXML
    private TableColumn<Aluno, Integer> colunaId;
    @FXML
    private TableColumn<Aluno, String> colNome;
    @FXML
    private TableColumn<Aluno, String> colEmail;
    @FXML
    private TableColumn<Aluno, String> colCPF;
    @FXML
    private TableColumn<Aluno, String> colTelefone;

    private AlunoDAO alunoDAO = new AlunoDAO();

    @FXML
    public void initialize() {
        inserirDadosTabela();
    }

    private void inserirDadosTabela(){
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        ObservableList<Aluno> alunos = FXCollections.observableArrayList(alunoDAO.listarAlunos());
        tabelaAlunos.setItems(alunos);
    }
}
