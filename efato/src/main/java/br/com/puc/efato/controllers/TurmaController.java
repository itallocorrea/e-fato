package br.com.puc.efato.controllers;

import br.com.puc.efato.models.api.LoginRequest;
import br.com.puc.efato.models.api.TurmaRequest;
import br.com.puc.efato.models.db.Aluno;
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
import java.util.ArrayList;
import java.util.Objects;

import static br.com.puc.efato.constants.ServiceConstants.*;

@Controller
@RequestMapping("/turma")
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;
    
    @Autowired
    private AlunosRepository alunosRepository;
    
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private UnidadeRepository unidadeRepository;

    @RequestMapping(value = "/cadastro", method = RequestMethod.GET)
    public ModelAndView criar(){
        ModelAndView modelAndView = new ModelAndView("cadastrarTurma");
        modelAndView.addObject(ATRIBUTO_DISCIPLINAS, disciplinaRepository.findAll());
        modelAndView.addObject(ATRIBUTO_CURSOS, cursoRepository.findAll());
        modelAndView.addObject(ATRIBUTO_UNIDADES, unidadeRepository.findAll());
        return modelAndView;
    }

	@RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    public ModelAndView cadastrarTurma(TurmaRequest turmaRequest){
        ModelAndView modelAndView = new ModelAndView("adicionarAlunos");
        Turma turma = new Turma();
        
        try{
            turma.setCurso(cursoRepository.findByCodigo(turmaRequest.getCurso_codigo()));
            turma.setDisciplina(disciplinaRepository.findByCodigo(turmaRequest.getDisciplina_codigo()));
            turma.setUnidade(unidadeRepository.findByCodigo(turmaRequest.getUnidade_codigo()));
            turmaRepository.save(turma);
            modelAndView.addObject(ATRIBUTO_CODIGO, turma.getCodigo());
            modelAndView.addObject(ATRIBUTO_DISCIPLINA, turma.getDisciplina().getDescricao());
            modelAndView.addObject(ATRIBUTO_CURSO, turma.getCurso().getDescricao());
            modelAndView.addObject(ATRIBUTO_UNIDADE, turma.getUnidade().getDescricao());
        }catch (Exception e){
            e.printStackTrace();
        }
        
        	return modelAndView;
    }

    @RequestMapping(value = "/excluir", method = RequestMethod.GET)
    public RedirectView excluir(@RequestParam long codigoTurma){
        Turma turma = turmaRepository.findByCodigo(codigoTurma);
        turmaRepository.delete(turma);
        return new RedirectView("http://localhost:8080/turma/pesquisarTurma");
    }

    @RequestMapping(value = "/adicionarAluno", method = RequestMethod.POST)
    public ModelAndView adicionarAluno(TurmaRequest turmaRequest){
        ModelAndView modelAndView = new ModelAndView("adicionarAlunos");
        Turma turma;
        
        try{
            turma = turmaRepository.findByCodigo(turmaRequest.getCodigo());
            modelAndView.addObject(ATRIBUTO_CODIGO, turma.getCodigo());
            modelAndView.addObject(ATRIBUTO_DISCIPLINA, turma.getDisciplina().getDescricao());
            modelAndView.addObject(ATRIBUTO_CURSO, turma.getCurso().getDescricao());
            modelAndView.addObject(ATRIBUTO_UNIDADE, turma.getUnidade().getDescricao());

            Aluno aluno = alunosRepository.findByNome(turmaRequest.getAluno_nome());
            if(aluno != null){
                turma.setAlunos(aluno);
                modelAndView.addObject(ATRIBUTO_ALUNOS, turmaRepository.findByCodigo(turma.getCodigo()).getAlunos());
                modelAndView.addObject(FEEDBACK_OK, turmaRequest.getAluno_nome() +" adicionado.");
                turmaRepository.save(turma);
            }else{
                modelAndView.addObject(ATRIBUTO_ALUNOS, turmaRepository.findByCodigo(turma.getCodigo()).getAlunos());
                modelAndView.addObject(FEEDBACK_ERRO, turmaRequest.getAluno_nome() +" não encontrado.");
            }

        }catch (Exception e){
            e.printStackTrace();
            modelAndView.addObject(FEEDBACK_ERRO, e.getMessage());
        }
        
        return modelAndView;
    }

    @RequestMapping(value = "/excluirAluno", method = RequestMethod.GET)
    public ModelAndView excluirAluno(@RequestParam long codigoTurma, @RequestParam long codigoAluno){
        Turma turma = turmaRepository.findByCodigo(codigoTurma);
        Aluno aluno = alunosRepository.findByCodigo(codigoAluno);
        turma.getAlunos().remove(aluno);
        turmaRepository.save(turma);

        ModelAndView modelAndView = new ModelAndView("adicionarAlunos");

        modelAndView.addObject("codigo",turma.getCodigo());
        modelAndView.addObject("disciplina",turma.getDisciplina().getDescricao());
        modelAndView.addObject("curso",turma.getCurso().getDescricao());
        modelAndView.addObject("unidade",turma.getUnidade().getDescricao());
        modelAndView.addObject("alunos",turmaRepository.findByCodigo(turma.getCodigo()).getAlunos());
        modelAndView.addObject("feedbackOk",aluno.getNome() +" removido.");

        return modelAndView;
    }

    @RequestMapping(value = "/minhasTurmas", method = RequestMethod.GET)
    public ModelAndView minhasTurmas(HttpSession session){
        ArrayList<Turma> retorno = new ArrayList<>();
        Aluno aluno = alunosRepository.findByLogin( ((LoginRequest) session.getAttribute(ATRIBUTO_USUARIO_LOGADO)).getLogin() );
        //todo: melhor busca no banco de dados
        for(Turma turma : turmaRepository.findAll()) {
            if (turma.getAlunos().contains(alunosRepository.findByLogin(aluno.getLogin()))) {
                retorno.add(turma);
            }
        }
        return new ModelAndView("minhasTurmas").addObject(ATRIBUTO_TURMAS,retorno);
    }

    @RequestMapping(value = "/pesquisarTurma", method = RequestMethod.GET)
    public ModelAndView pesquisarTurmas(@RequestParam(required = false) String disciplina_codigo,
                                        @RequestParam(required = false) String curso_codigo,
                                        @RequestParam(required = false) String unidade_codigo){
        ModelAndView modelAndView = new ModelAndView("pesquisarTurma");
        modelAndView.addObject(ATRIBUTO_DISCIPLINAS, disciplinaRepository.findAll());
        modelAndView.addObject(ATRIBUTO_CURSOS, cursoRepository.findAll());
        modelAndView.addObject(ATRIBUTO_UNIDADES, unidadeRepository.findAll());

        if((disciplina_codigo != null  || curso_codigo != null || unidade_codigo != null) && (!Objects.equals(disciplina_codigo, ZERO) || !Objects.equals(curso_codigo, ZERO) || !Objects.equals(unidade_codigo, ZERO))){
            modelAndView.addObject(ATRIBUTO_TURMAS, turmaRepository.findByFilter(disciplina_codigo,curso_codigo,unidade_codigo));
            modelAndView.addObject(FEEDBACK_OK,"Filtro aplicado.");
        }else {
            modelAndView.addObject(ATRIBUTO_TURMAS, turmaRepository.findAll());
            modelAndView.addObject(FEEDBACK_OK, "Visualizando todas.");
        }
        return modelAndView;
    }

}
