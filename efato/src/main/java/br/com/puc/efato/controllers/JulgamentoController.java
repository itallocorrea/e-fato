package br.com.puc.efato.controllers;

import br.com.puc.efato.models.api.JulgamentoRequest;
import br.com.puc.efato.models.api.LoginRequest;
import br.com.puc.efato.models.db.Fato;
import br.com.puc.efato.models.db.JF;
import br.com.puc.efato.models.db.Turma;
import br.com.puc.efato.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/jf")
public class JulgamentoController {

    @Autowired
    private TurmaRepository turmaRepository;
    @Autowired
    private JFRepository jfRepository;
    @Autowired
    private FatoRepository fatoRepository;
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private StatusRepository statusRepository;


    @RequestMapping(value = "/visualizar", method = RequestMethod.GET)
    public ModelAndView visualizar(long turma_codigo, @RequestParam(required = false) String status){
        ModelAndView modelAndView = new ModelAndView("visualizarJulgamentos");
        modelAndView.addObject("turma",turmaRepository.findByCodigo(turma_codigo));
        long status_codigo = statusRepository.findByDescricao("Em preparação").getCodigo();

        List<JF> jfs = jfRepository.findByFilterTurma(turma_codigo);
        if(!jfs.isEmpty())
            if("P".equals(status))
                modelAndView.addObject("jfs",jfRepository.findByFilterTurmaAndStatus(turma_codigo,status_codigo));
            else
                modelAndView.addObject("jfs",jfRepository.findByFilterTurma(turma_codigo));
        else
            modelAndView.addObject("feedbackErro","Nada para exibir");

        return modelAndView;
    }

    @RequestMapping(value = "/cadastro", method = RequestMethod.GET)
    public ModelAndView iniciar(long turma_codigo, @RequestParam(required = false) long jf_codigo, HttpSession session){
        ModelAndView modelAndView = new ModelAndView("cadastrarJulgamentoFato");
        Turma turma = turmaRepository.findByCodigo(turma_codigo);
        JF jf = new JF();
        if(jf_codigo == 0){
            jf.setTurma(turma);
            jf = jfRepository.save(jf);
        }else{
            jf = jfRepository.findByCodigo(jf_codigo);
        }
        modelAndView.addObject("jf",jf);
        modelAndView.addObject("turma",turma);
        modelAndView.addObject("fatos",fatoRepository.findByJf(jf));
        modelAndView.addObject("status",statusRepository.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public RedirectView salvar(JulgamentoRequest julgamentoRequest, HttpSession session){
        Turma turma = turmaRepository.findByCodigo(julgamentoRequest.getTurma_codigo());
        JF jf = jfRepository.findByCodigo(julgamentoRequest.getCodigo());
        jf.setCodigo(julgamentoRequest.getCodigo());
        jf.setDescricao(julgamentoRequest.getDescricao());
        jf.setTamMaxEquipe(julgamentoRequest.getTamMaxEquipe());
        jf.setTempMax(julgamentoRequest.getTempMax());
        jf.setTurma(turma);
        jf.setDisciplina(disciplinaRepository.findByCodigo(turma.getDisciplina().getCodigo()));
        jf.setProfessor(professorRepository.findByLogin(( (LoginRequest) session.getAttribute("usuarioLogado")).getLogin()));
        jf.setStatus(statusRepository.findByCodigo(julgamentoRequest.getStatus_codigo()));
        jfRepository.save(jf);

        RedirectView redirectView = new RedirectView("http://localhost:8080/jf/visualizar");
        redirectView.addStaticAttribute("turma_codigo",julgamentoRequest.getTurma_codigo());
        return redirectView;
    }

    @RequestMapping(value = "/cancelar", method = RequestMethod.GET)
    public RedirectView cancelar(@RequestParam(required = false) long jf_codigo){
        JF jf = jfRepository.findByCodigo(jf_codigo);

        List<Fato> fatos = fatoRepository.findByJf(jfRepository.findByCodigo(jf_codigo));

        if(!fatos.isEmpty()) {

            List<Long> codigosList = fatos.stream()
                    .map(Fato::getCodigo)
                    .collect(Collectors.toList());

            fatoRepository.deleteInIdList(codigosList);
        }

        jfRepository.deleteByCodigo(jf_codigo);

        RedirectView redirectView = new RedirectView("http://localhost:8080/jf/visualizar");
        redirectView.addStaticAttribute("turma_codigo", jf.getTurma().getCodigo());
        return redirectView;
    }


}
