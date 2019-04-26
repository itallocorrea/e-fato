package br.com.puc.efato.controllers;

import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_JF_CODIGO;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_STATUS;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_TOPICOS;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_TURMA_CODIGO;
import static br.com.puc.efato.constants.ServiceConstants.FEEDBACK_OK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import br.com.puc.efato.models.api.FatoRequest;
import br.com.puc.efato.models.db.Fato;
import br.com.puc.efato.models.db.JF;
import br.com.puc.efato.repositories.FatoRepository;
import br.com.puc.efato.repositories.JFRepository;
import br.com.puc.efato.repositories.TopicoRepository;

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
        modelAndView.addObject(ATRIBUTO_TOPICOS, topicoRepository.findAll());
        modelAndView.addObject(ATRIBUTO_JF_CODIGO, jf_codigo);
        if("S".equals(status))
            modelAndView.addObject(FEEDBACK_OK,"Fato cadastrado.");
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
        redirectView.addStaticAttribute(ATRIBUTO_JF_CODIGO,fatoRequest.getJf_codigo());
        redirectView.addStaticAttribute(ATRIBUTO_TURMA_CODIGO,jf.getTurma().getCodigo());
        redirectView.addStaticAttribute(ATRIBUTO_STATUS,"S");

        return redirectView;
    }
}