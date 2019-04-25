package br.com.puc.efato.models.db;

import br.com.puc.efato.models.api.FatoRequest;

import javax.persistence.*;

@Entity(name = "fato")
public class Fato {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long codigo;

    private String texto;

    private String resposta;

    private int ordem;

    @OneToOne
    private JF jf;
    @OneToOne
    private Topico topico;

    public Fato(){

    }

    public Fato(FatoRequest fatoRequest){
        this.texto = fatoRequest.getTexto();
        this.resposta = fatoRequest.getResposta();
        this.ordem = fatoRequest.getOrdem();
    }

    public String getDescricaoTopico(){
        return this.topico.getDescricao();
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public JF getJf() {
        return jf;
    }

    public void setJf(JF jf) {
        this.jf = jf;
    }

    public Topico getTopico() {
        return topico;
    }

    public void setTopico(Topico topico) {
        this.topico = topico;
    }
}
