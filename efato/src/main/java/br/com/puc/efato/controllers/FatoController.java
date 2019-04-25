package br.com.puc.efato.controllers;

import br.com.puc.efato.models.api.FatoRequest;
import br.com.puc.efato.models.db.Fato;
import br.com.puc.efato.models.db.JF;
import br.com.puc.efato.repositories.FatoRepository;
import br.com.puc.efato.repositories.JFRepository;
import br.com.puc.efato.repositories.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/fato")
public class FatoController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private JFRepository jfRepository;
    @Autowired
    private FatoRepository fatoRepository;



    @RequestMapping(value = "/cadastro", method = RequestMethod.GET)
    public ModelAndView visualizar(long jf_codigo, String status){
        ModelAndView modelAndView = new ModelAndView("cadastrarFato");
        modelAndView.addObject("topicos",topicoRepository.findAll());
        modelAndView.addObject("jf_codigo",jf_codigo);
        if("S".equals(status))
            modelAndView.addObject("feedbackOk","Fato cadastrado.");
        return modelAndView;
    }

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    public RedirectView cadastrar(FatoRequest fatoRequest){
        Fato fato = new Fato(fatoRequest);
        JF jf = jfRepository.findByCodigo(fatoRequest.getJf_codigo());
        fato.setJf(jf);
        fato.setTopico(topicoRepository.findByCodigo(fatoRequest.getTopico_codigo()));
        fatoRepository.save(fato);

        RedirectView redirectView = new RedirectView("http://localhost:8080/jf/cadastro");
        redirectView.addStaticAttribute("jf_codigo",fatoRequest.getJf_codigo());
        redirectView.addStaticAttribute("turma_codigo",jf.getTurma().getCodigo());
        redirectView.addStaticAttribute("status","S");

        return redirectView;
    }



}
