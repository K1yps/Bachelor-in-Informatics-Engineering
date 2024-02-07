
/**
 * Escreva a descrição da classe Loja_Queue aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Loja_Queue extends Loja{
    private int tamanho,n_esperas; //varivavel para atualizar o tempo de espera medio
    private double tm; //tempo de espera medio

    public Loja_Queue(String cod,String n,double x,double y) {
        super(cod,n,x,y);
        this.tamanho=0;
        this.tm=0;
        this.n_esperas=0;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public int getTm() {
        return (int)tm;
    }

    private void qstatupdate(int time){
        this.tm*=n_esperas++;
        this.tm = (tm + time)/n_esperas;
    }

    public void request(String enc){
        super.request(enc);
        this.tamanho++;
    }

    public Boolean ready(String enc,int waittime){
        if (super.ready((enc))) {
            this.qstatupdate(waittime);
            tamanho--;
            return true;
        }
        return false;
    }

}
