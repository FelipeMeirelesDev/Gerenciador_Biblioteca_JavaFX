package com.felipemeireles.sistemadebiblioteca.controller;

import com.felipemeireles.sistemadebiblioteca.dao.AlunoDAO;
import com.felipemeireles.sistemadebiblioteca.dao.EmprestimoDAO;
import com.felipemeireles.sistemadebiblioteca.dao.LivroDAO;
import com.felipemeireles.sistemadebiblioteca.entity.Aluno;
import com.felipemeireles.sistemadebiblioteca.entity.Emprestimo;
import com.felipemeireles.sistemadebiblioteca.entity.Livro;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EmprestarLivroController {

    @FXML private TextField campoCPF,campoIdLivro;
    @FXML private Label nomeAluno,cpfAluno,tituloLivro,autorLivro,anoLivro;
    @FXML private DatePicker dataEmprestimo,dataDevolucao;

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final LivroDAO livroDAO = new LivroDAO();

    @FXML
    private void buscarAluno() {
        String cpf = campoCPF.getText().trim(); // Pega o CPF digitado

        if (cpf.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atenção");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, digite um CPF para buscar.");
            alerta.showAndWait();
            return;
        }

        Aluno aluno = alunoDAO.buscarPorCpf(cpf);

        if (aluno != null) {
            nomeAluno.setText(aluno.getNome());
            cpfAluno.setText(aluno.getCpf());
            nomeAluno.setVisible(true);
            cpfAluno.setVisible(true);
        } else {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Não encontrado");
            alerta.setHeaderText(null);
            alerta.setContentText("Nenhum aluno encontrado com esse CPF.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void buscarLivro() {
        String idTexto = campoIdLivro.getText().trim(); // Pega o ID digitado

        if (idTexto.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atenção");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, digite o ID do livro para buscar.");
            alerta.showAndWait();
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText(null);
            alerta.setContentText("O ID do livro deve ser um número válido.");
            alerta.showAndWait();
            return;
        }

        Livro livro = livroDAO.buscarPorId(id);

        if (livro != null) {
            tituloLivro.setText(livro.getTitulo());
            autorLivro.setText(livro.getAutor());
            String ano = String.valueOf(livro.getAno());
            anoLivro.setText(ano);
            tituloLivro.setVisible(true);
            autorLivro.setVisible(true);
            anoLivro.setVisible(true);
        } else {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Não encontrado");
            alerta.setHeaderText(null);
            alerta.setContentText("Nenhum livro encontrado com esse ID.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void cancelarEmprestimo(){
        campoCPF.clear();
        campoIdLivro.clear();
        nomeAluno.setText("");
        cpfAluno.setText("");
        tituloLivro.setText("");
        autorLivro.setText("");
        anoLivro.setText("");
        dataEmprestimo.setValue(null);
        dataDevolucao.setValue(null);

        nomeAluno.setVisible(false);
        cpfAluno.setVisible(false);
        tituloLivro.setVisible(false);
        autorLivro.setVisible(false);
        anoLivro.setVisible(false);
    }

    @FXML
    private void confirmarEmprestimo() {

        String cpf = cpfAluno.getText();
        if (cpf == null || cpf.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atenção");
            alerta.setHeaderText(null);
            alerta.setContentText("Busque um aluno antes de confirmar o empréstimo.");
            alerta.showAndWait();
            return;
        }

        String idTexto = campoIdLivro.getText();
        if (idTexto == null || idTexto.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atenção");
            alerta.setHeaderText(null);
            alerta.setContentText("Busque um livro antes de confirmar o empréstimo.");
            alerta.showAndWait();
            return;
        }

        int livroId;

        try {
            livroId = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText(null);
            alerta.setContentText("O ID do livro deve ser um número válido.");
            alerta.showAndWait();
            return;
        }

        if (dataEmprestimo.getValue() == null || dataDevolucao.getValue() == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atenção");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecione as datas de empréstimo e devolução.");
            alerta.showAndWait();
            return;
        }

        if (dataDevolucao.getValue().isBefore(dataEmprestimo.getValue())) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atenção");
            alerta.setHeaderText(null);
            alerta.setContentText("A data de devolução não pode ser antes da data de empréstimo.");
            alerta.showAndWait();
            return;
        }

        // Cria objeto emprestimo e salva
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
        Emprestimo emprestimo = new Emprestimo(cpf, livroId, dataEmprestimo.getValue(), dataDevolucao.getValue());

        boolean sucesso = emprestimoDAO.adicionarEmprestimo(emprestimo);
        if (sucesso) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sucesso");
            alerta.setHeaderText(null);
            alerta.setContentText("Empréstimo realizado com sucesso!");
            alerta.showAndWait();

            // Limpa campos
            campoCPF.clear();
            campoIdLivro.clear();
            nomeAluno.setText("");
            cpfAluno.setText("");
            tituloLivro.setText("");
            autorLivro.setText("");
            anoLivro.setText("");
            dataEmprestimo.setValue(null);
            dataDevolucao.setValue(null);

            nomeAluno.setVisible(false);
            cpfAluno.setVisible(false);
            tituloLivro.setVisible(false);
            autorLivro.setVisible(false);
            anoLivro.setVisible(false);
        }
    }

}

