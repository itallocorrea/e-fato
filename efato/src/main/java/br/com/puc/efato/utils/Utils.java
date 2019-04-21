package br.com.puc.efato.utils;

import br.com.puc.efato.models.api.LoginRequest;
import br.com.puc.efato.models.db.Aluno;
import br.com.puc.efato.models.db.Professor;
import br.com.puc.efato.repositories.AlunosRepository;
import br.com.puc.efato.repositories.ProfessorRepository;

public class Utils {

    public static Boolean isLoginValido(LoginRequest loginRequest,AlunosRepository alunosRepository,ProfessorRepository professorRepository) {
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
        Aluno aluno = alunosRepository.findByLogin(login);
        if(aluno != null){
            return true;
        }else{
            Professor professor = professorRepository.findByLogin(login);
            if(professor != null){
                return true;
            }
        }
        return false;
    }


    /* tipo de usuario*/
    public static String tipoUsuario(String login,AlunosRepository alunosRepository,ProfessorRepository professorRepository){
        String retorno = null;
        Aluno aluno = alunosRepository.findByLogin(login);
        if(aluno != null){
            retorno = "A";
        }else{
            Professor professor = professorRepository.findByLogin(login);
            if(professor != null){
                retorno = "B";
            }
        }
        return retorno;
    }


    //Somente para testar commit


}
