package com.felipemeireles.sistemadebiblioteca.controller;

import com.felipemeireles.sistemadebiblioteca.dao.AlunoDAO;
import com.felipemeireles.sistemadebiblioteca.dao.EmprestimoDAO;
import com.felipemeireles.sistemadebiblioteca.dao.LivroDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label lblTotalLivros;
    @FXML private Label lblTotalAlunos;
    @FXML private Label lblLivrosPendentes;

    private final LivroDAO livroDAO = new LivroDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    @FXML
    public void initialize() {
        carregarDados();
    }

    private void carregarDados() {
        lblTotalLivros.setText(String.valueOf(livroDAO.contarLivros()));
        lblTotalAlunos.setText(String.valueOf(alunoDAO.contarAlunos()));
        lblLivrosPendentes.setText(
                String.valueOf(emprestimoDAO.contarEmprestimosPendentes())
        );
    }
}

