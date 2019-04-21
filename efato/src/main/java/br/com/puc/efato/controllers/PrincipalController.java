package br.com.puc.efato.controllers;

import br.com.puc.efato.UsuarioLogado;
import br.com.puc.efato.models.api.LoginRequest;
import br.com.puc.efato.repositories.AlunosRepository;
import br.com.puc.efato.repositories.ProfessorRepository;
import br.com.puc.efato.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class PrincipalController {

    @Autowired
    private AlunosRepository alunosRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private UsuarioLogado usuarioLogado;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @RequestMapping(value = "/logar", method = RequestMethod.POST)
    public ModelAndView login(LoginRequest loginRequest){
        ModelAndView modelAndView;
        if(Utils.isLoginValido(loginRequest,alunosRepository,professorRepository)){
            usuarioLogado.setUsuario(loginRequest);
            if("A".equals(Utils.tipoUsuario(loginRequest.getLogin(),alunosRepository,professorRepository))){
                return new ModelAndView("minhasTurmas");
            }else{
                return new ModelAndView("pesquisarTurma");
            }
        }
        modelAndView = new ModelAndView("index");
        modelAndView.addObject("feedbackErro","Login Inválido");
        return modelAndView;
    }

    @RequestMapping(value = "/deslogar", method = RequestMethod.GET)
    public ModelAndView deslogar(LoginRequest loginRequest){
        ModelAndView modelAndView = new ModelAndView("index");
        usuarioLogado.deslogar();
        modelAndView.addObject("feedbackOk","Volte Sempre!");
        return modelAndView;
    }

    @RequestMapping(value = "/voltar")
    String testRedirection(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader("Referer"))
                .map(requestUrl -> "redirect:" + requestUrl)
                .orElse("/");
    }



}
