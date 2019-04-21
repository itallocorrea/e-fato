package br.com.puc.efato;

import br.com.puc.efato.models.api.LoginRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session",  proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UsuarioLogado {
    private static LoginRequest usuarioLogado;

    public void setUsuario(LoginRequest usuario) {
        this.usuarioLogado = usuario;
    }

    public LoginRequest getUsuario() {
        return usuarioLogado;
    }

    public void deslogar(){this.usuarioLogado = null;}

    public static boolean usuarioLogado(){
        if(usuarioLogado != null)
            return true;
        else
            return false;
    }

}
