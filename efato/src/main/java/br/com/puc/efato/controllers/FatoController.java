package br.com.puc.efato.controllers;

import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_JF_CODIGO;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_STATUS;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_TOPICOS;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_TURMA_CODIGO;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_USUARIO_LOGADO;
import static br.com.puc.efato.constants.ServiceConstants.FEEDBACK_OK;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import br.com.puc.efato.models.api.FatoRequest;
import br.com.puc.efato.models.api.LoginRequest;
import br.com.puc.efato.models.db.Aluno;
import br.com.puc.efato.models.db.Equipe;
import br.com.puc.efato.models.db.Fato;
import br.com.puc.efato.models.db.JF;
import br.com.puc.efato.models.db.Resposta;
import br.com.puc.efato.models.db.Status;
import br.com.puc.efato.repositories.AlunosRepository;
import br.com.puc.efato.repositories.EquipeRepository;
import br.com.puc.efato.repositories.FatoRepository;
import br.com.puc.efato.repositories.JFRepository;
import br.com.puc.efato.repositories.RespostaRepository;
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

    @Autowired
    private AlunosRepository alunoRepository;
    
    @Autowired
    private RespostaRepository respostaRepository;
    
    @Autowired
    private EquipeRepository equipeRepository;

    @RequestMapping(value = "/cadastro", method = RequestMethod.GET)
    public ModelAndView visualizar(long jf_codigo, String status){
        ModelAndView modelAndView = new ModelAndView("cadastrarFato");
        modelAndView.addObject(ATRIBUTO_TOPICOS, topicoRepository.findAll());
        modelAndView.addObject(ATRIBUTO_JF_CODIGO, jf_codigo);
        if("S".equals(status))
            modelAndView.addObject(FEEDBACK_OK,"Fato cadastrado.");
        return modelAndView;
    }

    @RequestMapping(value = "/jogar", method = RequestMethod.GET)
    public ModelAndView listarFatos(@RequestParam(required = false) Integer fato_codigo, @RequestParam(required = false) String href, @RequestParam(required = false) Boolean resposta,  HttpSession session){
        ModelAndView modelAndView = new ModelAndView("fatoLider");
        
        if(session.getAttribute(ATRIBUTO_USUARIO_LOGADO) == null)
            return null;
        
        long idAluno =  getAlunoLogado(session).getCodigo();
        boolean hasJfEmExecucao = false;
        JF jfEmExecucao = new JF();

        List<JF> jfsEmExecucao =  jfRepository.findByStatus(new Status(3, "Em execução"));
        for(JF jf : jfsEmExecucao) {
            for(Aluno aluno : jf.getTurma().getAlunos()) {
                if(aluno.getCodigo() == idAluno) {
                    hasJfEmExecucao = true;
                    jfEmExecucao = jf;
                }
            }
        }

        if(hasJfEmExecucao) {
            List<Fato> fatos = fatoRepository.findByJf(jfEmExecucao);
            if(fato_codigo == null) {
                modelAndView.addObject("fato",fatos.get(0));
            } else {
            	Integer codigoEquipe = 0;
            	
            	List<Equipe> equipes = equipeRepository.findByJf(jfEmExecucao);
            	for(Equipe equipe : equipes) {
            		for(Aluno aluno : equipe.getAlunos()) {
            			if(aluno.getCodigo() == idAluno) {
            				codigoEquipe = (int) equipe.getCodigo();
            			}
            		}
            		if(equipe.getAlunoLider().getCodigo() == idAluno) {
            			codigoEquipe = (int) equipe.getCodigo();
            		}
            	}
            	
            	Resposta respostaToBeSaved = new Resposta();
            	respostaToBeSaved.setCodigoFato(fato_codigo);
            	respostaToBeSaved.setResposta(resposta);
            	respostaToBeSaved.setCodigoEquipe(codigoEquipe);
            	respostaRepository.save(respostaToBeSaved);
            	
                if(fato_codigo == fatos.size() - 1) { //Se essa condição for verdadeira, significa que o código do fato passado como parâmetro corresponde ao último fato do JF em Execução
                	return null;
                } else {
                	modelAndView.addObject("fato",fatos.get(fato_codigo + 1));
                }
                
                }
        } else {
            return null;
        }

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

    private Aluno getAlunoLogado(HttpSession session){
        return alunoRepository.findByLogin(((LoginRequest) session.getAttribute(ATRIBUTO_USUARIO_LOGADO)).getLogin());
    }
}