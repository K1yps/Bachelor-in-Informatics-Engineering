/*
 *  DISCLAIMER: Este código foi criado para discussão e edição durante as aulas práticas de DSS, representando
 *  uma solução em construção. Como tal, não deverá ser visto como uma solução canónica, ou mesmo acabada.
 *  É disponibilizado para auxiliar o processo de estudo. Os alunos são encorajados a testar adequadamente o
 *  código fornecido e a procurar soluções alternativas, à medida que forem adquirindo mais conhecimentos.
 */
package UI;

import business.ArmazemFacade;
import business.ArmazemI;
import business.InfoPaletes;
import business.subRobos.RoboInvalidoException;
import business.subStock.Exceptions.PrateleiraJaExisteException;
import business.subStock.Exceptions.ZonaInvalidaException;
import business.subStock.Exceptions.ZonaJaExisteException;

import java.util.List;
import java.util.Scanner;

/**
 * Exemplo de interface em modo texto.
 *
 * @author grupo
 */
public class TextUI {
    // O model tem a 'lógica de negócio'.
    private final ArmazemI model;

    // Scanner para leitura
    private final Scanner scanner;

    /**
     * Construtor.
     * <p>
     * Cria os menus e a camada de negócio.
     */
    public TextUI() {

        this.model = new ArmazemFacade();
        scanner = new Scanner(System.in);
    }

    /**
     * Executa o menu principal e invoca o método correspondente à opção seleccionada.
     */
    public void run() {
        System.out.println("Bem vindo ao Sistema de Gestão do Armazém!");
        this.menuPrincipal();
        System.out.println("Até breve...");
    }

    // Métodos auxiliares - Estados da UI

    /**
     * Estado - Menu Principal
     */
    private void menuPrincipal() {
        Menu menu = new Menu(new String[]{
                "Comunicar código QR",
                "Notificar recolha de paletes",
                "Notificar entrega de paletes",
                "Consultar listagem de localizações",
                "Adicionar Robô",
                "Adicionar Prateleira",
                "Adicionar Matéria Prima",
                "Adicionar Zona",
                "Adicionar Aresta"
        });

        // Registar pré-condições das transições
        //menu.setPreCondition(3, () -> this.model.haAlunos() && this.model.haTurmas());

        // Registar os handlers
        menu.setHandler(1, this::comunicarCodigoQR);
        menu.setHandler(2, this::notificarRecolhaPaletes);
        menu.setHandler(3, this::notificarEntregaPaletes);
        menu.setHandler(4, this::consultarLocalizacoes);
        menu.setHandler(5, this::adicionarRobo);
        menu.setHandler(6, this::adicionarPrateleira);
        menu.setHandler(7, this::adicionarMateriaPrima);
        menu.setHandler(8, this::adicionarZona);
        menu.setHandler(9, this::adicionarAresta);

        menu.run();
    }

    /**
     * Adicionar Robo
     */
    private void adicionarRobo() {
        String codigo;
        boolean flag = false;
        do {
            if (flag) System.out.println("Robô já existe, tente novamente!");
            System.out.println("Insira o código do Robô: ");
            codigo = scanner.nextLine();
            flag = true;
        } while (model.existeRobo(codigo));
        System.out.println("Insira o modelo do Robô: ");
        String modelo = scanner.nextLine();
        this.model.addRobo(codigo, modelo, 0);
    }

    /**
     * Adicionar Prateleira
     */
    private void adicionarPrateleira() {
        int codigo;
        String zona;
        boolean flag = false;
        do {
            if (flag) System.out.println("Corredor não existe, tente novamente!");
            System.out.println("Insira o código do Corredor: ");
            zona = scanner.nextLine();
            flag = true;
        } while (!model.existeZona(zona));
        flag = false;
        do {
            if (flag) System.out.println("Prateleira não existente, tente novamente!");
            System.out.println("Insira o código da Prateleira: ");
            codigo = scanner.nextInt();
            flag = true;
        } while (model.existePrateleira(codigo));
        System.out.println("Insira o custo de acesso: ");
        Float custo = scanner.nextFloat();
        System.out.println("Insira a capacidade da prateleira: ");
        int capacidade = scanner.nextInt();

        try {
            this.model.adicionarPrateleira(zona, codigo, custo, capacidade);
        } catch (ZonaInvalidaException e) {
            System.out.println("Corredor Inválido!");

        } catch (PrateleiraJaExisteException n) {
            System.out.println("Prateleira já existe!");
        }
    }

    /**
     * Adicionar Materia Prima
     */
    private void adicionarMateriaPrima() {
        System.out.println("Insira o código da Matéria Prima (inteiro): ");
        int codigo = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Insira o nome da Matéria Prima: ");
        String nome = scanner.nextLine();

        this.model.addMatPrima(codigo, nome);
    }

    /**
     * Adicionar Zona
     */
    private void adicionarZona() {
        String codigo;
        boolean flag = false;
        do {
            if (flag) System.out.println("Corredor não existente, tente novamente!");
            System.out.println("Insira o código do Corredor: ");
            codigo = scanner.nextLine();
            flag = true;
        } while (model.existeZona(codigo));

        System.out.println("Insira o comprimento do corredor: ");
        float custo = scanner.nextFloat();

        try {
            this.model.adicionarZona(codigo, custo);
        } catch (ZonaJaExisteException e) {
            System.out.println("Este corredor já existe!");
        }

    }

    /**
     * Adicionar Aresta
     */
    private void adicionarAresta() {
        String origem, destino;
        boolean flag = false;
        do {
            if (flag) System.out.println("Zona não existente, tente novamente!");
            System.out.println("Insira a zona de origem: ");
            origem = scanner.nextLine();
            flag = true;
        } while (!model.existeZona(origem));
        flag = false;
        do {
            if (flag) System.out.println("Zona não existente, tente novamente!");
            System.out.println("Insira a zona de destino: ");
            destino = scanner.nextLine();
            flag = true;
        } while (!model.existeZona(destino));
        System.out.println("Insira o comprimento da aresta: ");
        float custo = scanner.nextFloat();

        try {
            this.model.adicionarAresta(origem, destino, custo);
        } catch (ZonaInvalidaException e) {
            System.out.println("Zona Invalida");
        }
    }

    /**
     * Comunicar código QR
     */
    private void comunicarCodigoQR() {
        System.out.println("Insira o código QR: ");
        String qr = scanner.nextLine();

        this.model.registarPalete(qr);
    }

    /**
     * Notificar entreta das paletes
     */
    private void notificarRecolhaPaletes() {
        String codRobo;
        boolean flag = false;
        do {
            if (flag) System.out.println("Robô não existente, tente novamente!");
            System.out.println("Insira o código do Robô: ");
            codRobo = scanner.nextLine();
            flag = true;
        } while (!model.existeRobo(codRobo));
        try {
            this.model.notificacaoCarga(codRobo);
        } catch (RoboInvalidoException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Notificar recolha das paletes
     */
    private void notificarEntregaPaletes() {

        String codRobo;
        boolean flag = false;
        do {
            if (flag) System.out.println("Robô não existente, tente novamente!");
            System.out.println("Insira o código do Robô: ");
            codRobo = scanner.nextLine();
            flag = true;
        } while (!model.existeRobo(codRobo));
        try {
            this.model.notificacaoDescarga(codRobo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Consultar listagem de localizações
     */
    private void consultarLocalizacoes() {
        List<InfoPaletes> stock = model.getStock();
        for (InfoPaletes palete : stock) {
            System.out.println(palete.toString());
        }
    }

}
