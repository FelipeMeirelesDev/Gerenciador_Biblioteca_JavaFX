package com.felipemeireles.sistemadebiblioteca.controller;

import com.felipemeireles.sistemadebiblioteca.dao.AlunoDAO;
import com.felipemeireles.sistemadebiblioteca.entity.Aluno;
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

public class AlunosController {

    @FXML
    private TableView<Aluno> tabelaAlunos;

    @FXML
    private TextField campoPesquisa;

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

    private ObservableList<Aluno> listaCompleta; // lista completa vinda do banco
    private FilteredList<Aluno> listaFiltrada;   // lista com filtro dinâmico

    @FXML
    private void abrirTelaAdicionarAluno() {
        Navegador.trocarTela("/com/felipemeireles/sistemadebiblioteca/view/AdicionarAluno.fxml");
    }

    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        listaCompleta = FXCollections.observableArrayList(alunoDAO.listarAlunos());
        listaFiltrada = new FilteredList<>(listaCompleta, aluno -> true); // sem filtro inicial

        tabelaAlunos.setItems(listaFiltrada);

        campoPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtro = newValue.toLowerCase();

            listaFiltrada.setPredicate(aluno -> {
                if (filtro == null || filtro.isEmpty()) {
                    return true;
                }

                return aluno.getNome().toLowerCase().contains(filtro)
                        || aluno.getEmail().toLowerCase().contains(filtro)
                        || aluno.getCpf().toLowerCase().contains(filtro)
                        || aluno.getTelefone().toLowerCase().contains(filtro);
            });
        });
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

    @FXML
    private void excluirAlunoSelecionado(ActionEvent event) {
        Aluno aluno = tabelaAlunos.getSelectionModel().getSelectedItem();

        if (aluno == null) {
            alertaAviso("Selecione um aluno.");
            return;
        }

        if (alunoDAO.alunoPossuiEmprestimos(aluno.getCpf())) {
            alertaAviso("Aluno possui empréstimos em aberto.");
            return;
        }

        boolean sucesso = alunoDAO.desativarAluno(aluno.getId());

        if (sucesso) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sucesso");
            alerta.setHeaderText(null);
            alerta.setContentText("Aluno desativado com sucesso.");
            alerta.showAndWait();

            tabelaAlunos.setItems(
                    FXCollections.observableArrayList(alunoDAO.listarAlunos())
            );
        } else {
            alertaAviso("Este aluno já está desativado ou ocorreu um erro.");
        }
    }

    private void alertaAviso(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
