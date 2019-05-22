var recursiva = function () {
    console.log('chamou');
    setTimeout(recursiva,10000);
};

recursiva();

//verificar se login alfanumerico de 5 a 10
$(document).ready(function(){
    $("#login").focusout(function(){
        let login = document.getElementById("login").value;
        if(login === "")
            return;

        if(login.length < 5 || login.length > 10){
            $(".feedbackErro").html("Opa, login precisa ter entre 5 e 10 caracteres.");
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

//login unico
$(document).ready(function(){
    $(".loginUnico").focusout(function(){
        $.ajax({
            url: "http://localhost:8080/usuario/existe?login="+document.getElementById("login").value,
            success: function(data) {
                if(data.existe === "true"){
                    $(".feedbackErro").html("Opa, esse nome de usuário já existe. ");
                    document.getElementById("login").value = "";
                    console.log(data.existe);
                    return;
                }
            }
        });
    });
});

//mostrar campo curso ?
$(document).ready(function(){
    if(window.location.href !== 'http://localhost:8080/usuario/cadastro')
        return;

    deveMostrarCurso(document.getElementsByName("tipo")[1].checked);
    $(".tipo").click(function(){
        deveMostrarCurso(document.getElementsByName("tipo")[1].checked);
    });
});
function deveMostrarCurso(tipo) {
    if(tipo == true){
        $("#labelCurso").hide();
        $("#curso").hide();
    }else{
        $("#labelCurso").show();
        $("#curso").show();
    }
}

//verificar senha sequencia de numeros e caracteres
$(document).ready(function(){
    $("#senha").focusout(function(){
        let senha = document.getElementById("senha").value;
        if(senha === "")
            return;
        let regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{4,}$/;
        if(!regex.test(senha)){
            $(".feedbackErro").html("Opa, senha deve ter letras e números.");
            document.getElementById("senha").value = "";
            return;
        }else{
            $(".feedbackErro").html("");
        }

    });
});

function adicionarAluno () {
    let aluno = $('#adicionarAluno')[0].value;
    $('#alunosAdicionados')[0].innerHTML += '<br> <input type="text" value="aluno"> <i class="fas fa-times"></i>';
}

// get params da URI
function getParams () {
    let query = location.search.slice(1);
    let partes = query.split('&');
    let data = {};
    partes.forEach(function (parte) {
        let chaveValor = parte.split('=');
        let chave = chaveValor[0];
        let valor = chaveValor[1];
        data[chave] = valor;
    });
    return data;
}


//controle de modal julgamento aluno
$(document).ready(function(){
    let controleModal =  document.getElementById('modal');
    if(controleModal){
        $('#'+controleModal.value).modal('show');
    }
});
