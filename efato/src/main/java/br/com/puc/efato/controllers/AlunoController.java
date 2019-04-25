package br.com.puc.efato.controllers;

import br.com.puc.efato.models.db.Aluno;
import br.com.puc.efato.repositories.AlunosRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

	@Autowired
	private AlunosRepository alunosRepository;

	//:TODO Esse metódo precisa ser melhorado para que o repository busque no banco de dados somente o atributo nome da tabela Aluno (Refatoração)
	@RequestMapping(value = "/buscar", method = RequestMethod.GET,produces = "application/json")
	@ResponseBody
	public Iterable<Aluno> buscarAlunos(@RequestParam(required = true) String term) throws JSONException {
		Iterable<Aluno> alunos = alunosRepository.findByNomeContaining(term);
		ArrayList nomeAlunos = new ArrayList();
		for(Aluno aluno : alunos){
			nomeAlunos.add(aluno.getNome());
		}
		return nomeAlunos;
	}


}
