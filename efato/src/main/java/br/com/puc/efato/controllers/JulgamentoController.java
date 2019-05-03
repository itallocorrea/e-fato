package br.com.puc.efato.controllers;

import br.com.puc.efato.models.api.JulgamentoRequest;
import br.com.puc.efato.models.api.LoginRequest;
import br.com.puc.efato.models.db.*;
import br.com.puc.efato.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.puc.efato.constants.ServiceConstants.*;

@Controller
@RequestMapping("/jf")
public class JulgamentoController {

    @Autowired
    private TurmaRepository turmaRepository;
    
    @Autowired
    private EquipeRepository equipeRepository;
    
    @Autowired
    private JFRepository jfRepository;
    
    @Autowired
    private FatoRepository fatoRepository;
    
    @Autowired
    private AlunosRepository alunoRepository;
    
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    
    @Autowired
    private ProfessorRepository professorRepository;
    
    @Autowired
    private StatusRepository statusRepository;

    @RequestMapping(value = "/visualizar", method = RequestMethod.GET)
    public ModelAndView visualizar(long turma_codigo, @RequestParam(required = false) String status){
        ModelAndView modelAndView = new ModelAndView("visualizarJulgamentos");
        long status_codigo = statusRepository.findByDescricao("Em preparação").getCodigo();
        List<JF> jfs = jfRepository.findByFilterTurma(turma_codigo);
        if(!jfs.isEmpty()){
            if("P".equals(status)){
                modelAndView = new ModelAndView("visualizarJulgamentosAluno");
                modelAndView.addObject("jfs",jfRepository.findByFilterTurmaAndStatus(turma_codigo,status_codigo));
            }
            else{
                modelAndView.addObject("jfs",jfRepository.findByFilterTurma(turma_codigo));
            }
        }else{
            modelAndView.addObject(ATRIBUTO_TURMA, turmaRepository.findByCodigo(turma_codigo));
        }

        modelAndView.addObject(ATRIBUTO_TURMA,turmaRepository.findByCodigo(turma_codigo));
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
        modelAndView.addObject(ATRIBUTO_TURMA, turma);
        modelAndView.addObject(ATRIBUTO_FATOS, fatoRepository.findByJf(jf));
        modelAndView.addObject(ATRIBUTO_STATUS, statusRepository.findAll());
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
        jf.setProfessor(professorRepository.findByLogin(((LoginRequest) session.getAttribute(ATRIBUTO_USUARIO_LOGADO)).getLogin()));
        jf.setStatus(statusRepository.findByCodigo(julgamentoRequest.getStatus_codigo()));
        jfRepository.save(jf);

        RedirectView redirectView = new RedirectView("http://localhost:8080/jf/visualizar");
        redirectView.addStaticAttribute(ATRIBUTO_TURMA_CODIGO, julgamentoRequest.getTurma_codigo());
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
        redirectView.addStaticAttribute(ATRIBUTO_TURMA_CODIGO, jf.getTurma().getCodigo());
        return redirectView;
    }
    
    //Método verifica se o aluno logado está incluso ou não em uma equipe. Dependendo do resultado, ele é redirecionado para a devida página.
    @RequestMapping(value = "/hasEquipe", method = RequestMethod.GET)
    public ModelAndView alunoHasEquipe(@RequestParam long jf_codigo,HttpSession session){
    	ModelAndView modelAndView = new ModelAndView("visualizarJulgamentosAluno");
        Aluno aluno = getAlunoLogado(session);
        Turma turma = jfRepository.findByCodigo(jf_codigo).getTurma();
        List<Equipe> equipesJF = equipeRepository.findByJf(jfRepository.findByCodigo(jf_codigo));
        modelAndView.addObject("modal","modalSemEquipe");
        modelAndView.addObject("turma",turma);
        modelAndView.addObject("jfs",jfRepository.findByFilterTurma(turma.getCodigo()));
        for(Equipe equipe : equipesJF) {
        	if(equipe.getAlunoLider().getCodigo() == aluno.getCodigo() || equipe.getAlunos().contains(aluno)) {
        		modelAndView.addObject("alunoLider", equipe.getAlunoLider());
        		modelAndView.addObject("membros", equipe.getAlunos());
        		modelAndView.addObject("modal","modalComEquipe");
        	}
        }

        return modelAndView;
    }

   //Método cria uma equipe
    @RequestMapping(value = "/criarEquipe", method = RequestMethod.GET)
    public ModelAndView criarEquipe(@RequestParam long jf_codigo, @RequestParam long turma_codigo, HttpSession session){
    	Equipe equipe = new Equipe();
    	Aluno aluno =  getAlunoLogado(session);
    	equipe.setJf(jfRepository.findByCodigo(jf_codigo));
        equipe.setAlunoLider(alunoRepository.findByCodigo(aluno.getCodigo()));
        equipe.setAlunos(new ArrayList<>());
        equipeRepository.save(equipe);
        
        ModelAndView modelAndView = new ModelAndView("visualizarJulgamentosAluno");
        modelAndView.addObject("jfs", jfRepository.findByFilterTurma(turma_codigo));
        modelAndView.addObject("turma", turmaRepository.findByCodigo(turma_codigo));

        return modelAndView;
    }
    
  //Método adiciona membro em uma equipe
    @RequestMapping(value = "/adicionarMembro", method = RequestMethod.GET)
    public ModelAndView adicionarMembro(@RequestParam long codigo_equipe, @RequestParam long codigo_membro){
    	Equipe equipe = equipeRepository.findByCodigo(codigo_equipe);
    	equipe.getAlunos().add(alunoRepository.findByCodigo(codigo_membro));
    	equipeRepository.save(equipe);
    	
    	ModelAndView modelAndView = new ModelAndView("retorna a mesma tela, porém com o aluno adicionado");
    	modelAndView.addObject("membros", equipe.getAlunos());
    	modelAndView.addObject("alunoLider", equipe.getAlunoLider());

        return modelAndView;
    }
    
  //Método onde um aluno sai da equipe. Caso ele seja líder, a equipe é excluída.
    @RequestMapping(value = "/sairEquipe", method = RequestMethod.GET)
    public ModelAndView sairEquipe(@RequestParam long codigo_equipe, @RequestParam long codigo_aluno, @RequestParam long codigo_turma){
    	Equipe equipe = equipeRepository.findByCodigo(codigo_equipe);
    	
    	if(equipe.getAlunoLider().getCodigo() == codigo_aluno) {
    		equipeRepository.delete(equipe);
    	} else {
    		equipe.getAlunos().remove(alunoRepository.findByCodigo(codigo_aluno));
    	}
    	
    	ModelAndView modelAndView = new ModelAndView("retorna a tela de julgamento de fatos");
    	 modelAndView.addObject("jfs", jfRepository.findByFilterTurma(codigo_turma));
         modelAndView.addObject("turma", turmaRepository.findByCodigo(codigo_turma));

        return modelAndView;
    }


    private Aluno getAlunoLogado(HttpSession session){
        return alunoRepository.findByLogin(((LoginRequest) session.getAttribute(ATRIBUTO_USUARIO_LOGADO)).getLogin());
    }
}


