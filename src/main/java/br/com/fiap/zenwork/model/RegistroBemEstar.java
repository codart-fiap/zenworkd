
package br.com.fiap.zenwork.model;

import java.time.LocalDateTime;

/**
 * Classe modelo (POJO) que representa a entidade RegistroBemEstar.
 * Cumpre o requisito de POO.
 */
public class RegistroBemEstar {

    private int idRegistro;
    private int idUsuario; // Chave estrangeira para Usuario
    private LocalDateTime dataRegistro;
    private String emocaoSelecionada;
    private String comentario;

    // Construtor padrão
    public RegistroBemEstar() {
    }

    // Construtor parametrizado
    public RegistroBemEstar(int idRegistro, int idUsuario, LocalDateTime dataRegistro, String emocaoSelecionada, String comentario) {
        this.idRegistro = idRegistro;
        this.idUsuario = idUsuario;
        this.dataRegistro = dataRegistro;
        this.emocaoSelecionada = emocaoSelecionada;
        this.comentario = comentario;
    }

    // Getters e Setters
    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getEmocaoSelecionada() {
        return emocaoSelecionada;
    }

    public void setEmocaoSelecionada(String emocaoSelecionada) {
        this.emocaoSelecionada = emocaoSelecionada;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}