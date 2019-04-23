package br.com.puc.efato.utils;

import br.com.puc.efato.models.api.LoginRequest;
import br.com.puc.efato.models.db.Aluno;
import br.com.puc.efato.models.db.Professor;
import br.com.puc.efato.repositories.AlunosRepository;
import br.com.puc.efato.repositories.ProfessorRepository;

public class Utils {

    public static Boolean isLoginValido(LoginRequest loginRequest, AlunosRepository alunosRepository, ProfessorRepository professorRepository) {
        Aluno aluno = alunosRepository.findByLogin(loginRequest.getLogin());
        if(aluno != null){
            return (aluno.getSenha().equals(loginRequest.getSenha()));
        }else{
            Professor professor = professorRepository.findByLogin(loginRequest.getLogin());
            if(professor != null){
                return (professor.getSenha().equals(loginRequest.getSenha()));
            }
        }
        return false;
    }


    /*
        Verificar se login já existe
     */
    public static Boolean loginExiste(String login,AlunosRepository alunosRepository,ProfessorRepository professorRepository) {
    	return (alunosRepository.findByLogin(login) != null || professorRepository.findByLogin(login) != null);
    }


    /* tipo de usuario*/
    public static String tipoUsuario(String login,AlunosRepository alunosRepository,ProfessorRepository professorRepository){
        if(alunosRepository.findByLogin(login) != null) {
        	return "A";
        } else if (professorRepository.findByLogin(login) != null) {
        	return "B";
        } else {
        	return null;
        }
    }
}
