/*
    verificar se login alfanumerico de 5 a 20
 */
$(document).ready(function(){
    $("#login").focusout(function(){
        let login = document.getElementById("login").value;
        if(login === "")
            return;

        if(login.length < 5 || login.length > 20){
            $(".feedbackErro").html("Opa, login precisa ter entre 5 e 20 caracteres.");
            document.getElementById("login").value = "";
            return;
        }else{
            $(".feedbackErro").html("");
        }

        let regex = /^[a-zA-Z0-9.]*$/;
        if(!regex.test(login)){
            $(".feedbackErro").html("Opa, login pode ter somente letras, números e o caracter ponto (.)");
            document.getElementById("login").value = "";
            return;
        }else{
            $(".feedbackErro").html("");
        }

    });
});

/*
    verificar se senha alfanumerico
 */
$(document).ready(function(){
    $("#senha").focusout(function(){

    });
});


/*
    login unico
 */