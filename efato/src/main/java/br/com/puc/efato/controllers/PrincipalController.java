package br.com.puc.efato.controllers;

import br.com.puc.efato.models.api.LoginRequest;
        import br.com.puc.efato.repositories.AlunosRepository;
        import br.com.puc.efato.repositories.ProfessorRepository;
        import br.com.puc.efato.utils.Utils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.servlet.ModelAndView;
        import org.springframework.web.servlet.view.RedirectView;

        import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PrincipalController {

    @Autowired
    private AlunosRepository alunosRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(@RequestParam(required = false) String li) {
        ModelAndView modelAndView = new ModelAndView("index");
        if("S".equals(li))
            modelAndView.addObject("feedbackErro","Login inválido");
        return modelAndView;
    }

    @RequestMapping(value = "/logar", method = RequestMethod.POST)
    public RedirectView login(LoginRequest loginRequest, HttpSession session){
        if(Utils.isLoginValido(loginRequest,alunosRepository,professorRepository)){
            session.setAttribute("usuarioLogado",loginRequest);
            if("A".equals(Utils.tipoUsuario(loginRequest.getLogin(),alunosRepository,professorRepository)))
                return new RedirectView("turma/minhasTurmas");
            else
                return new RedirectView("turma/pesquisarTurma");
        }
        RedirectView redirectView = new RedirectView("/");
        redirectView.addStaticAttribute("li","S");
        return redirectView;
    }

    @RequestMapping(value = "/deslogar", method = RequestMethod.GET)
    public RedirectView deslogar(LoginRequest loginRequest, HttpSession session){
        session.setAttribute("usuarioLogado",null);
        return new RedirectView("/");
    }

}
