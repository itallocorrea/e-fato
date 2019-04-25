package br.com.puc.efato.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FatoRequest {

    @JsonProperty("codigo")
    private Long codigo;

    @JsonProperty("texto")
    private String texto;

    @JsonProperty("resposta")
    private String resposta;

    @JsonProperty("ordem")
    private int ordem;

    @JsonProperty("topico_codigo")
    private int topico_codigo;

    @JsonProperty("jf_codigo")
    private int jf_codigo;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
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

    public int getTopico_codigo() {
        return topico_codigo;
    }

    public void setTopico_codigo(int topico_codigo) {
        this.topico_codigo = topico_codigo;
    }

    public int getJf_codigo() {
        return jf_codigo;
    }

    public void setJf_codigo(int jf_codigo) {
        this.jf_codigo = jf_codigo;
    }
}
