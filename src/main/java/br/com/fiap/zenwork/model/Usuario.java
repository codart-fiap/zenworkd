
package br.com.fiap.zenwork.model;

/**
 * Classe modelo (POJO) que representa a entidade Usuario.
 * Cumpre o requisito de POO.
 */
public class Usuario {

    private int idUsuario;
    private String nome;
    private String email;
    private String senha; // Armazenaremos a senha (no mundo real, seria um hash)
    private boolean isAdmin;

    // Construtor padrão
    public Usuario() {
    }

    // Construtor parametrizado
    public Usuario(int idUsuario, String nome, String email, String senha, boolean isAdmin) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.isAdmin = isAdmin;
    }

    // Getters e Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
