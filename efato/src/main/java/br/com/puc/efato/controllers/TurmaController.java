package br.com.puc.efato.controllers;

import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_ALUNOS;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_CODIGO;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_CURSO;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_CURSOS;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_DISCIPLINA;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_DISCIPLINAS;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_TURMAS;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_UNIDADE;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_UNIDADES;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_USUARIO_LOGADO;
import static br.com.puc.efato.constants.ServiceConstants.FEEDBACK_ERRO;
import static br.com.puc.efato.constants.ServiceConstants.FEEDBACK_OK;
import static br.com.puc.efato.constants.ServiceConstants.ZERO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.puc.efato.models.api.LoginRequest;
import br.com.puc.efato.models.api.TurmaRequest;
import br.com.puc.efato.models.db.Aluno;
import br.com.puc.efato.models.db.Turma;
import br.com.puc.efato.repositories.AlunosRepository;
import br.com.puc.efato.repositories.CursoRepository;
import br.com.puc.efato.repositories.DisciplinaRepository;
import br.com.puc.efato.repositories.TurmaRepository;
import br.com.puc.efato.repositories.UnidadeRepository;

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
        List<Aluno> alunos = turma.getAlunos();
        Aluno alunoToRemove = new Aluno();
        
        for(Aluno aluno : alunos){
            if(aluno.getCodigo() == codigoAluno)
                alunoToRemove = aluno;
        }
        alunos.remove(alunoToRemove);

        turmaRepository.save(turma);

        return new ModelAndView("adicionarAlunos");
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
