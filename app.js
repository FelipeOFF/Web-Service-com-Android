/* WEB SERVICE 4 - CONFIGURANDO AS ROTAS */

/**
 * Ok, depois de ter configurado nosso 
 * banco de dados e controller, esta na hora
 * de fazermos nossas rotas para cada solicitação.
 * 
 * Poderia colocar um outro arquivo separado só para 
 * as rotas, mais preferi colocalo aqui de uma vez,
 * até porque serão poucas requisições
 */

// Vamos importar aquela configuração que fizemos lá atrás
const app = require('./config/app.js')

// Aqui estamos solicitando nosso controller de produtos
const product = require('./controllers/product.js')

/**
 * Validator é uma modulo usado para validar diversas formas
 * de dados em String. É possivel com ele validar emails, datas, etc...
 * 
 * Caso você queira saber mais sobre ele acesse os sites:
 * NPM: https://www.npmjs.com/package/validator
 * GITHUB: https://github.com/chriso/validator.js
 */
const validator = require('validator')

/**
 * Vamos fazer uma requisição get agora, esta requisição
 * servira para retornar todos os produtos.
 */
app.get('/produtos', function(req, res){
    // Vamos chamar nosso objeto que conten todas as funções que fizemos
    product.list().then(result => {
        // Agora vamos enviar uma resposta de sucesso para o usuario
        res.status(result.status)
        res.json(result)
    }).catch(err => {
        // Se houver algun erro, então vamos mandar junto com o status
        res.status(err.status)
        res.json(err)
    })
})

/**
 * Aqui o usuario do server fara uma solicitação 
 * de apenas um produto, ele deve conter um id
 * na solicitação
 */
app.get('/produtos', function(req, res){
    // Vamos obter o id vindo desta solicitação
    var id = req.query.id
    if(id){
        // Se houver espaço vamos retirar, até porque não pode ter
        id = validator.trim(validator.escape(id))
        product.product(id).then(result => {
            res.status(result.status)
            res.json(result)
        }).catch(err => {
            res.status(err.status)
            res.json(err)
        })
    }else{
        // Não veio nenhum id, na solicitação, então...
        var erroObj = product.contructResult(403, 'Você deve inserir uma identificação valida')
        res.status(erroObj.status)
        res.json(erroObj)
    }
})

/**
 * Está solicitação sera usada para salvar
 * um registro
 */
app.post('/produto', function(req, res){
    /**
     * Repare que aqui não precisamos nos preocupar
     * se esta vindo ou não um nome ou um preço
     * isso porque colocamos claramente na configuração
     * do db que deve se ter um nome e preço, isso
     * é requerido pelo banco.
     * 
     * Se por ventura não vier estes valores, o banco ira nos
     * retornar um erro, este erro vira já tratado. para o usuario
     * final.
     * 
     * Esta é a melhor maneira de se fazer isso?
     * Não. O certo seria verificar aqui também se esta vindo
     * um nome e preço para o produto, mais eu resolvi deixar
     * esta verificação para o DB. 
     * Esta tratativa deveria ficar do lado do controller, e
     * poderia se usado o validator para fazer isso!
     * 
     */
    var name = req.query.name
    var price = req.query.price
    product.save(name, price).then(result => {
        res.status(result.status)
        res.json(result)
    }).catch(err => {
        res.status(err.status)
        res.json(err)
    })
})

/**
 * Esta rota servirá para fazer UPDATE de algun
 * registro.
 */
app.put('/produto', function(req, res){
    // vamos faze a mesma tratativa citada no save ↑↑↑
    var id = req.query.id
    var name = req.query.name
    var price = req.query.price
    /*
        Lembre-se que dentro do método update,
        temos uma validação, para verificar se
        de fato esta vindo alguma coisa.
    */
    product.update(id, name, price).then(result => {
        res.status(result.status)
        res.json(result)
    }).catch(err => {
        res.status(err.status)
        res.json(err)
    })
})

/**
 * Agora vamos incluir o delete.
 */
app.delete('/produto', function(req, res){
    var id = req.query.id

    product.delete(id).then(result => {
        res.status(result.status)
        res.json(result)
    }).catch(err => {
        res.status(err.status)
        res.json(err)
    })
})

/**
 * Pronto esta parte esta concluida. Vamos para o aplicativo, agora.
 * 
 * Tome cuidado com esta etápa, se por ventura você cometer algum erro
 * aqui este erro voltara na hora de fazer o aplicativo.
 * 
 */