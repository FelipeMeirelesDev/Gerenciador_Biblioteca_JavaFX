package com.felipemeireles.sistemadebiblioteca.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Navegador {

    private static Pane painelPrincipal;

    public static void setPainel(Pane painel) {
        painelPrincipal = painel;
    }

    public static void trocarTela(String caminhoFxml) {
        try {
            Parent tela = FXMLLoader.load(Navegador.class.getResource(caminhoFxml));
            painelPrincipal.getChildren().setAll(tela);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

