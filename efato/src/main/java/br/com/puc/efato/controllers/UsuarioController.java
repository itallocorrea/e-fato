package br.com.puc.efato.controllers;

import br.com.puc.efato.UsuarioLogado;
import br.com.puc.efato.models.api.UsuarioRequest;
import br.com.puc.efato.models.db.Aluno;
import br.com.puc.efato.models.db.Professor;
import br.com.puc.efato.repositories.AlunosRepository;
import br.com.puc.efato.repositories.ProfessorRepository;
import br.com.puc.efato.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private AlunosRepository alunosRepository;
	@Autowired
	private ProfessorRepository professorRepository;
	@Autowired
	private UsuarioLogado usuarioLogado;

	@RequestMapping(value = "/criar", method = RequestMethod.GET)
	public ModelAndView criar() {
		return new ModelAndView("cadastrarUsuario");
	}

	@RequestMapping(value = "/criar/finalizar", method = RequestMethod.POST)
	public ModelAndView criarUsuario(UsuarioRequest usuarioRequest){
		ModelAndView modelAndView = new ModelAndView("cadastrarUsuario");
		try{
			if("A".equals(usuarioRequest.getTipo())){
				alunosRepository.save(new Aluno(usuarioRequest));
			}else{
				professorRepository.save(new Professor(usuarioRequest));
			}
			modelAndView = new ModelAndView("cadastrarUsuario");
			modelAndView.addObject("feedbackOk","Cadastro realizado com sucesso!");
		}catch (Exception e ){
			e.printStackTrace();
			modelAndView.addObject("feedbackError",e.toString());
		}
		return modelAndView;
	}

	@RequestMapping(value = "/alterar", method = RequestMethod.GET)
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
		}catch (Exception e ){
			e.printStackTrace();
			modelAndView.addObject("feedbackError",e.toString());
		}
		return modelAndView;
	}

	//:TODO Corrigir para atualizar e não salvar novamente
	@RequestMapping(value = "/alterar/finalizar", method = RequestMethod.POST)
	public ModelAndView alterarUsuario(UsuarioRequest usuarioRequest){
		ModelAndView modelAndView = new ModelAndView("alterarUsuario");
		try{
			if("A".equals(usuarioRequest.getTipo())){
				alunosRepository.save(new Aluno(usuarioRequest));
			}else{
				professorRepository.save(new Professor(usuarioRequest));
			}
			modelAndView = new ModelAndView("cadastrarUsuario");
			modelAndView.addObject("feedbackOk","Cadastro realizado com sucesso!");
		}catch (Exception e ){
			e.printStackTrace();
			modelAndView.addObject("feedbackError",e.toString());
		}
		return modelAndView;
	}

	/*
        verificar se nome de usuario já existe
     */
	@RequestMapping(value = "/existe", method = RequestMethod.GET)
	public JSONObject usuarioExiste(@RequestParam(required = true) String login) throws JSONException {
		JSONObject json = new JSONObject();
		if(Utils.loginExiste(login,alunosRepository,professorRepository)){
			return json.put("exite",true);
		}else return json.put("exite",false);
	}

}
