
package br.com.fiap.zenwork.model;

/**
 * Classe modelo (POJO) que representa a entidade RecursoAjuda.
 * Cumpre o requisito de POO.
 */
public class RecursoAjuda {

    private int idRecurso;
    private String titulo;
    private String descricao;
    private String linkExterno;
    private String categoria;

    // Construtor padrão
    public RecursoAjuda() {
    }

    // Construtor parametrizado
    public RecursoAjuda(int idRecurso, String titulo, String descricao, String linkExterno, String categoria) {
        this.idRecurso = idRecurso;
        this.titulo = titulo;
        this.descricao = descricao;
        this.linkExterno = linkExterno;
        this.categoria = categoria;
    }

    // Getters e Setters
    public int getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLinkExterno() {
        return linkExterno;
    }

    public void setLinkExterno(String linkExterno) {
        this.linkExterno = linkExterno;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
