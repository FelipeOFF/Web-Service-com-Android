/* WEB SERVICE 1 - CONFIGURAÇÂO DO SERVER */

/**
 * Bom aqui é onde vai ficar a primeira etapa.
 * Dentro deste arquivo vai ficar toda a configuração
 * do WebService. Se for necessario adicionar um Plug-in
 * dentro do express sera adicionado aqui. Se for necessario
 * chamar mais modulos, que precisam ser iniciados junto com
 * a aplicação sera feito aqui.
 */

/**
 * Aqui sera chamado o express. Ele sera usado na aplicaçaõ para,
 * alén de muitas outras coisas, para definir rotas.
 * 
 * (↓↓↓)
 */
const express = require('express')

/**
 * Aqui ficara uma "instancia" do express.
 * Ela sera usada em outros momentos dentro da aplicação,
 * então foi feito um exports dela, com o nome app.
 * 
 * (Caso você tenha interesse em saber mais sobre exports em node
 * sugiro que você leia esse materiais:
 * https://www.sitepoint.com/understanding-module-exports-exports-node-js/
 * https://nodejs.org/api/modules.html)
 * 
 * Por enquanto basta você saber que o exports servira para usarmos essa mesma
 * "instancia" do express em outros momentos da aplicação.
 * (↓↓↓)
 */
const app = exports.app = express()

/**
 * O bodyParser é um middleware, que sera util para o nossos body
 */
const bodyParser = require('body-parser');

/**
 * Eu costumo criar um outro arquivo para definir informações do server.
 * como o endereço do host e a porta.
 * 
 * Quando você for disponibilizar sua aplicação Rest para uma hospedagen,
 * voce deverá definir para a mesma o host e a porta. Lei a documentação
 * da sua hospedagen para ter mais detalhes
 */

const options = require('./app_options.json');

/**
 * O allowCors servira para um criterio de segurança,
 * ele sera executado depois de cada resposta.
 * 
 * Alén é claro dele delimitar determinados acessos, 
 * ele garante que nem todo mundo tem acesso ao server.
 * 
 * @param {Request do servidor} req
 * @param {Response do servidor} res 
 * @param {Proxima etapa} next 
 */
var allowCors = function(req, res, next){
    res.header('Access-Control-Allow-Origin', `${options.HOST}:${options.PORT}`);
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
    res.header('Access-Control-Allow-Headers', 'Content-Type');
    res.header('Access-Control-Allow-Credentials', 'true');

    // (↓↓↓) Chamando o proximo metodo a executar esse request
    next();
}

// Adicionando o allowCors ao aplicação http
app.use(allowCors)

// Adicionando a funcionalidade do JSON no body com bodyParser
app.use(bodyParser.json())

// O adicionando um listen ao server para a porta presente no arquivo app_options
app.listen(options.PORT, function(){
    console.log(`Server ON! PORT => ${options.PORT}`)
})

/**
 * Esta parte foi concluida, agora vamos configurar o banco de dados.
 * 
 * Neste tutorial eu preferi usar o MongoDB.
 * 
 * Vamos até o arquivo "/config/db.js" localizado nesta mesma pasta.
 */