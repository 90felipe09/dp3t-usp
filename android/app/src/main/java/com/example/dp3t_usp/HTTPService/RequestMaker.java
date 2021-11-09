package com.example.dp3t_usp.HTTPService;


import android.os.AsyncTask;

// Esta classe realiza requisições HTTP com o RequestsHelper a partir de um RequestPackage
// Isso é uma tarefa assíncrona (AsyncTask). Significa que ela tem esses quatro métodos:
//  1. onPreExecute(): Faz o setup da tarefa;
//  2. doInBackground(): Cria uma thread separada só para executar a tarefa;
//      Se a nossa requisição não fosse feita aqui dentro, a interface travaria até a resposta voltar.
//  3. onProgressUpdate(): Geralmente utilizado para atualizar a view para por exemplo atualizar a
//      barra de progresso.
//  4. onPostExecute(): O resultado de retorno de doInBackground é passado por parâmetro para essa
//      função quando doInBackground termina.
public class RequestMaker extends AsyncTask<RequestPackage, String, String> {
    @Override
    protected  String doInBackground(RequestPackage... params){
        return RequestsHelper.getData(params[0]);
    }

    @Override
    protected void onPostExecute(String result){
        //implementar o comportamento sobre o que fazer após a resposta de uma requisição.
    }
}