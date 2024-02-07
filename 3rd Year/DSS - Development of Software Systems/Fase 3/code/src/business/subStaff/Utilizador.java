package business.subStaff;

public abstract class Utilizador {
    private String username, nome, codAcesso;

    public Utilizador(String username, String nome, String codAcesso) {
        this.username = username;
        this.nome = nome;
        this.codAcesso = codAcesso;
    }

    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodAcesso() {
        return codAcesso;
    }

    public void setCodAcesso(String codAcesso) {
        this.codAcesso = codAcesso;
    }

    public abstract Utilizador clone();

}
