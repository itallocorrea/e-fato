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

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Objects;

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
        modelAndView.addObject("disciplinas",disciplinaRepository.findAll());
        modelAndView.addObject("cursos",cursoRepository.findAll());
        modelAndView.addObject("unidades",unidadeRepository.findAll());
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
            modelAndView.addObject("codigo",turma.getCodigo());
            modelAndView.addObject("disciplina",turma.getDisciplina().getDescricao());
            modelAndView.addObject("curso",turma.getCurso().getDescricao());
            modelAndView.addObject("unidade",turma.getUnidade().getDescricao());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return modelAndView;
        }

    }

    @RequestMapping(value = "/adicionarAluno", method = RequestMethod.POST)
    public ModelAndView adicionarAluno(TurmaRequest turmaRequest){
        ModelAndView modelAndView = new ModelAndView("adicionarAlunos");
        Turma turma;
        try{
            turma = turmaRepository.findByCodigo(turmaRequest.getCodigo());
            modelAndView.addObject("codigo",turma.getCodigo());
            modelAndView.addObject("disciplina",turma.getDisciplina().getDescricao());
            modelAndView.addObject("curso",turma.getCurso().getDescricao());
            modelAndView.addObject("unidade",turma.getUnidade().getDescricao());

            Aluno aluno = alunosRepository.findByNome(turmaRequest.getAluno_nome());
            if(aluno != null){
                turma.setAlunos(aluno);
                modelAndView.addObject("alunos",turmaRepository.findByCodigo(turma.getCodigo()).getAlunos());
                modelAndView.addObject("feedbackOk",turmaRequest.getAluno_nome() +" adicionado.");
                turmaRepository.save(turma);
            }else{
                modelAndView.addObject("alunos",turmaRepository.findByCodigo(turma.getCodigo()).getAlunos());
                modelAndView.addObject("feedbackErro",turmaRequest.getAluno_nome() +" não encontrado.");
            }

        }catch (Exception e){
            e.printStackTrace();
            modelAndView.addObject("feedbackErro",e.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/excluirAluno", method = RequestMethod.GET)
    public ModelAndView excluirAluno(@RequestParam(required = true) long codigoTurma, @RequestParam(required = true) long codigoAluno){
        ModelAndView modelAndView = new ModelAndView("adicionarAlunos");
        Turma turma;
        try{

        }catch (Exception e){
            e.printStackTrace();
            modelAndView.addObject("feedbackErro",e.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/minhasTurmas", method = RequestMethod.GET)
    public ModelAndView minhasTurmas(HttpSession session){
        ArrayList<Turma> retorno = new ArrayList<>();
        Aluno aluno = alunosRepository.findByLogin( ((LoginRequest) session.getAttribute("usuarioLogado")).getLogin() );
        //todo: melhor busca no banco de dados
        for(Turma turma : turmaRepository.findAll()) {
            if (turma.getAlunos().contains(alunosRepository.findByLogin(aluno.getLogin()))) {
                retorno.add(turma);
            }
        }
        return new ModelAndView("minhasTurmas").addObject("turmas",retorno);
    }

    @RequestMapping(value = "/pesquisarTurma", method = RequestMethod.GET)
    public ModelAndView pesquisarTurmas(@RequestParam(required = false) String disciplina_codigo,
                                        @RequestParam(required = false) String curso_codigo,
                                        @RequestParam(required = false) String unidade_codigo){
        ModelAndView modelAndView = new ModelAndView("pesquisarTurma");
        modelAndView.addObject("disciplinas",disciplinaRepository.findAll());
        modelAndView.addObject("cursos",cursoRepository.findAll());
        modelAndView.addObject("unidades",unidadeRepository.findAll());

        if((disciplina_codigo != null  || curso_codigo != null || unidade_codigo != null) && (!Objects.equals(disciplina_codigo, "0") || !Objects.equals(curso_codigo, "0") || !Objects.equals(unidade_codigo, "0"))){
            modelAndView.addObject("turmas",turmaRepository.findByFilter(disciplina_codigo,curso_codigo,unidade_codigo));
            modelAndView.addObject("feedbackOk","Filtro aplicado.");
        }else {
            modelAndView.addObject("turmas", turmaRepository.findAll());
            modelAndView.addObject("feedbackOk", "Visualizando todas.");
        }
        return modelAndView;
    }

}
