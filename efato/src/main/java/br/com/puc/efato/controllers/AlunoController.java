package br.com.puc.efato.controllers;

import br.com.puc.efato.repositories.AlunosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

	@Autowired
	private AlunosRepository alunosRepository;


}
