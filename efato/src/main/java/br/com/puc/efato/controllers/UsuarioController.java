package br.com.puc.efato.controllers;

import br.com.puc.efato.UsuarioLogado;
import br.com.puc.efato.models.api.LoginRequest;
import br.com.puc.efato.models.api.UsuarioRequest;
import br.com.puc.efato.models.db.Aluno;
import br.com.puc.efato.models.db.Curso;
import br.com.puc.efato.models.db.Professor;
import br.com.puc.efato.repositories.AlunosRepository;
import br.com.puc.efato.repositories.CursoRepository;
import br.com.puc.efato.repositories.ProfessorRepository;
import br.com.puc.efato.utils.Utils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private AlunosRepository alunosRepository;
	@Autowired
	private ProfessorRepository professorRepository;
	@Autowired
	private CursoRepository cursoRepository;
	@Autowired
	private UsuarioLogado usuarioLogado;

	@RequestMapping(value = "/cadastro", method = RequestMethod.GET)
	public ModelAndView criar() {
		ModelAndView modelAndView = new ModelAndView("cadastrarUsuario");
		Iterable<Curso> cursos = cursoRepository.findAll();
		modelAndView.addObject("cursos",cursos);
		return modelAndView;
	}

	@RequestMapping(value = "/cadastro/finalizar", method = RequestMethod.POST)
	public ModelAndView criarUsuario(UsuarioRequest usuarioRequest){
		ModelAndView modelAndView = new ModelAndView("cadastrarUsuario");
		try{
			if("A".equals(usuarioRequest.getTipo())){
				Aluno aluno = new Aluno(usuarioRequest);
				aluno.setCurso(cursoRepository.findByCodigo(usuarioRequest.getCurso()));
				alunosRepository.save(aluno);
			}else{
				professorRepository.save(new Professor(usuarioRequest));
			}
			if("A".equals(Utils.tipoUsuario(usuarioRequest.getLogin(),alunosRepository,professorRepository))){
				modelAndView = new ModelAndView("minhasTurmas");
			}else{
				modelAndView = new ModelAndView("pesquisarTurma");
			}
			usuarioLogado.setUsuario(new LoginRequest(usuarioRequest.getLogin(),usuarioRequest.getSenha()));
			modelAndView.addObject("feedbackOk","Cadastro realizado com sucesso, seja bem vindo !");
		}catch (Exception e ){
			e.printStackTrace();
			modelAndView.addObject("feedbackError",e.toString());
		}
		return modelAndView;
	}

	@RequestMapping(value = "/alteracao", method = RequestMethod.GET)
	public ModelAndView alterar(){
		ModelAndView modelAndView = new ModelAndView("alterarUsuario");
		try{
			Aluno aluno = alunosRepository.findByLogin(usuarioLogado.getUsuario().getLogin());
			if(aluno != null){
				modelAndView.addObject("usuario",aluno);
				modelAndView.addObject("aluno","true");
			}else{
				Professor professor = professorRepository.findByLogin(usuarioLogado.getUsuario().getLogin());
				modelAndView.addObject("usuario",professor);
				modelAndView.addObject("professor","true");
			}
			Iterable<Curso> cursos = cursoRepository.findAll();
			modelAndView.addObject("cursos",cursos);
			return modelAndView;
		}catch (Exception e ){
			e.printStackTrace();
			modelAndView.addObject("feedbackError",e.toString());
		}
		return modelAndView;
	}

	@RequestMapping(value = "/alteracao/finalizar", method = RequestMethod.POST)
	public ModelAndView alterarUsuario(UsuarioRequest usuarioRequest){
		ModelAndView modelAndView = new ModelAndView("alterarUsuario");
		try{
			if("A".equals(usuarioRequest.getTipo())){
				// se o cadastro que era professor agora é aluno, é necessário excluir o anterior
				Professor professor = professorRepository.findByLogin(usuarioRequest.getLogin());
				if(professor != null){
					professorRepository.delete(professor);
				}
				Aluno aluno = alunosRepository.findByLogin(usuarioRequest.getLogin());
				if(aluno == null){
					// novo aluno
					Aluno novoAluno = new Aluno(usuarioRequest);
					novoAluno.setCurso(cursoRepository.findByCodigo(usuarioRequest.getCurso()));
					alunosRepository.save(novoAluno);
				}else{
					// atualizar aluno
					aluno.setNome(usuarioRequest.getNome());
					aluno.setEmail(usuarioRequest.getEmail());
					aluno.setSenha(usuarioRequest.getSenha());
					aluno.setCurso(cursoRepository.findByCodigo(usuarioRequest.getCurso()));
					alunosRepository.save(aluno);
				}
			}else{
				// se o cadastro que era aluno agora é professor, é necessário excluir o anterior
				Aluno aluno = alunosRepository.findByLogin(usuarioRequest.getLogin());
				if(aluno != null){
					alunosRepository.delete(aluno);
				}
				Professor professor = professorRepository.findByLogin(usuarioRequest.getLogin());
				if(professor == null ){
					// novo professor
					professorRepository.save(new Professor(usuarioRequest));
				}else{
					// atualizar professor
					professor.setNome(usuarioRequest.getNome());
					professor.setEmail(usuarioRequest.getEmail());
					professor.setSenha(usuarioRequest.getSenha());
					professorRepository.save(new Professor(usuarioRequest));
				}
			}
			if("A".equals(Utils.tipoUsuario(usuarioRequest.getLogin(),alunosRepository,professorRepository))){
				modelAndView = new ModelAndView("minhasTurmas");
			}else{
				modelAndView = new ModelAndView("pesquisarTurma");
			}
			// atualizar usuario logado
			usuarioLogado.setUsuario(new LoginRequest(usuarioRequest.getLogin(),usuarioRequest.getSenha()));
			modelAndView = new ModelAndView("minhasTurmas");
			modelAndView.addObject("feedbackOk","Alteração realizado com sucesso!");
		}catch (Exception e ){
			e.printStackTrace();
			modelAndView.addObject("feedbackError",e.toString());
		}
		return modelAndView;
	}

	/*verificar se nome de usuario já existe*/
	@RequestMapping(value = "/existe", method = RequestMethod.GET,produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> usuarioExiste(@RequestParam(required = true) String login) throws JSONException {
		if(Utils.loginExiste(login,alunosRepository,professorRepository)){
			return new ResponseEntity<>("{\"existe\" : \"true\"}", HttpStatus.OK);
		}else return new ResponseEntity<>("{\"existe\" : \"false\"}", HttpStatus.OK);
	}

}
