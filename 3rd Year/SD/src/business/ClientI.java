package business;

public interface ClientI {

    public void login(String username, String password);

    public void registar(String username, String password);

    public void atualizarLocalizacao(int x, int y);

    public void getPessoasZona(int x);

    public void alarmeZonaVazia(int x);

    public void informarDoenca();

    public void mapaLocalizacoes();
}
