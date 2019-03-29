package br.com.puc.efato.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.puc.efato.models.db.Aluno;

@RestController
public class AlunosController {
	
//	@Autowired
//	private AlunosRepository alunosRepository;
	
	@RequestMapping(value = "/alunos", method = RequestMethod.POST)
	public ResponseEntity<Aluno> listarAlunos(@RequestBody Aluno alunoRequest) {
		//alunosRepository.save(alunoRequest);
		return ResponseEntity.ok(alunoRequest);
	}

}
