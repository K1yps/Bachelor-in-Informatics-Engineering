package UI;



import java.util.Scanner;

import business.Client;
import business.ClientI;
/**
 * Exemplo de interface em modo texto.
 *
 * @author grupo
 */
public class TextUI {

    // O model tem a 'lógica de negócio'.
    private final ClientI model;

    // Scanner para leitura
    private final Scanner scanner;

    /**
     * Construtor.
     * <p>
     * Cria os menus e a camada de negócio.
     */
    public TextUI() {

        this.model = new Client();
        scanner = new Scanner(System.in);
    }

    /**
     * Executa o menu principal e invoca o método correspondente à opção seleccionada.
     */
    public void run() {
        System.out.println("Bem vindo ao Alarme Covid!");
        this.menuPrincipal();
        System.out.println("Até breve...");
    }

    // Métodos auxiliares - Estados da UI

    /**
     * Estado - Menu Principal
     */
    private void menuPrincipal() {
        Menu menu = new Menu(new String[]{
                "Login",
                "Registar"
        });

        // Registar pré-condições das transições
        //menu.setPreCondition(3, () -> this.model.haAlunos() && this.model.haTurmas());

        // Registar os handlers
        menu.setHandler(1, this::login);


        menu.run();
    }

    private void login(){
        System.out.println("Username: ");
        String username=scanner.nextLine();
        System.out.println("Password: ");
        String password=scanner.nextLine();

        menuAposAutenticacao();

    }

    private void registar() {

    }



    private void menuAposAutenticacao() {
        Menu menu = new Menu(new String[]{
            "Atualizar Localização",
            "Saber nº de pessoas numa localização",
            "Ligar alarme que avisa que uma localização ficou vazia",
            "Avisar teste covid positivo",
            "Mapa das localizacões (Função adicional)"
        });

        menu.setHandler(1, this::atualizarLocalizacao);
        menu.setHandler(2, this::getPessoasZona);
        menu.setHandler(3, this::alarmeZonaVazia);
        menu.setHandler(4, this::informarDoenca);
        menu.setHandler(5, this::mapaLocalizacoes);


        menu.run();
    }

    private void atualizarLocalizacao() {
        System.out.println("Insira as coordenadas x da sua nova localização: ");
        int x=scanner.nextInt();
        System.out.println("Insira a coordenada y da sua nova localização: ");
        int y=scanner.nextInt();
    }

    private void getPessoasZona() {
        System.out.println("Insira as coordenadas x da zona: ");
        int x=scanner.nextInt();
        System.out.println("Insira a coordenada y da zona: ");
        int y=scanner.nextInt();

    }

    private void alarmeZonaVazia() {
        System.out.println("Insira as coordenadas x da zona: ");
        int x=scanner.nextInt();
        System.out.println("Insira a coordenada y da zona: ");
        int y=scanner.nextInt();
    }

    private void informarDoenca() {

    }

    private void mapaLocalizacoes() {

    }

}
