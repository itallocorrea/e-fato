package br.com.puc.efato.controllers;

import br.com.puc.efato.models.api.LoginRequest;
import br.com.puc.efato.models.api.UsuarioRequest;
import br.com.puc.efato.models.db.Aluno;
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
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_USUARIO_LOGADO;
import static br.com.puc.efato.constants.ServiceConstants.TIPO_ALUNO;
import static br.com.puc.efato.constants.ServiceConstants.TIPO_PROFESSOR;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_TIPO;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_USUARIO;
import static br.com.puc.efato.constants.ServiceConstants.ATRIBUTO_CURSOS;
import static br.com.puc.efato.constants.ServiceConstants.FEEDBACK_ERRO;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private AlunosRepository alunosRepository;
	
	@Autowired
	private ProfessorRepository professorRepository;
	
	@Autowired
	private CursoRepository cursoRepository;

	@RequestMapping(value = "/cadastro", method = RequestMethod.GET)
	public ModelAndView criar() {
		ModelAndView modelAndView = new ModelAndView("cadastrarUsuario");
		modelAndView.addObject(ATRIBUTO_CURSOS, cursoRepository.findAll());
		return modelAndView;
	}

	@RequestMapping(value = "/cadastro/finalizar", method = RequestMethod.POST)
	public RedirectView criarUsuario(UsuarioRequest usuarioRequest, HttpSession session){
		RedirectView redirectView = null;
		
		try{
			if(TIPO_ALUNO.equals(usuarioRequest.getTipo())){
				Aluno aluno = new Aluno(usuarioRequest);
				aluno.setCurso(cursoRepository.findByCodigo(usuarioRequest.getCurso()));
				alunosRepository.save(aluno);
				redirectView = new RedirectView("http://localhost:8080/turma/minhasTurmas");
			}else{
				professorRepository.save(new Professor(usuarioRequest));
				redirectView = new RedirectView("http://localhost:8080/turma/pesquisarTurma");
			}
			session.setAttribute(ATRIBUTO_USUARIO_LOGADO, new LoginRequest(usuarioRequest.getLogin(),usuarioRequest.getSenha()));
		}catch (Exception e ){
			e.printStackTrace();
		}
		
		return redirectView;
	}

	@RequestMapping(value = "/alteracao", method = RequestMethod.GET)
	public ModelAndView alterar(HttpSession session){
		ModelAndView modelAndView = new ModelAndView("alterarUsuario");
		
		try{
			Aluno aluno = alunosRepository.findByLogin(((LoginRequest) session.getAttribute(ATRIBUTO_USUARIO_LOGADO)).getLogin());
			
			if(aluno != null){
				modelAndView.addObject(ATRIBUTO_USUARIO,aluno);
				modelAndView.addObject(ATRIBUTO_TIPO, TIPO_ALUNO);
			}else{
				Professor professor = professorRepository.findByLogin(((LoginRequest) session.getAttribute(ATRIBUTO_USUARIO_LOGADO)).getLogin());
				modelAndView.addObject(ATRIBUTO_USUARIO, professor);
				modelAndView.addObject(ATRIBUTO_TIPO, TIPO_PROFESSOR);
			}
			
			modelAndView.addObject(ATRIBUTO_CURSOS, cursoRepository.findAll());
			return modelAndView;
		}catch (Exception e ){
			e.printStackTrace();
			modelAndView.addObject(FEEDBACK_ERRO, e.toString());
		}
		
		return modelAndView;
	}

	@RequestMapping(value = "/alteracao/finalizar", method = RequestMethod.POST)
	public RedirectView alterarUsuario(UsuarioRequest usuarioRequest){
		RedirectView redirectView = null;
		
		try{
			if(TIPO_ALUNO.equals(usuarioRequest.getTipo())){
				Aluno aluno = alunosRepository.findByLogin(usuarioRequest.getLogin());
				aluno.setNome(usuarioRequest.getNome());
				aluno.setEmail(usuarioRequest.getEmail());
				aluno.setSenha(usuarioRequest.getSenha());
				aluno.setCurso(cursoRepository.findByCodigo(usuarioRequest.getCurso()));
				alunosRepository.save(aluno);
				redirectView = new RedirectView("http://localhost:8080/turma/minhasTurmas");
			}else{
				Professor professor = professorRepository.findByLogin(usuarioRequest.getLogin());
				professor.setNome(usuarioRequest.getNome());
				professor.setEmail(usuarioRequest.getEmail());
				professor.setSenha(usuarioRequest.getSenha());
				professorRepository.save(new Professor(usuarioRequest));
				redirectView = new RedirectView("http://localhost:8080/turma/pesquisarTurma");
			}
		}catch (Exception e ){
			e.printStackTrace();
		}
		
		return redirectView;
	}

	@RequestMapping(value = "/existe", method = RequestMethod.GET,produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> usuarioExiste(@RequestParam(required = true) String login) throws JSONException {
		if(Utils.loginExiste(login, alunosRepository, professorRepository)) {
			return new ResponseEntity<>("{\"existe\" : \"true\"}", HttpStatus.OK);
		 } else {
			 return new ResponseEntity<>("{\"existe\" : \"false\"}", HttpStatus.OK);
		 }
	}
		

}
