/* WEB SERVICE 3 - CONTROLLERS PRODUCT */

/**
 * Aqui vamos manter uma comunicação com o DB
 * e vamos mandar para cada requisição o conteudo encontrado no model.
 * 
 * Estarei usando Promises para fazer cada requisição, 
 * caso você queira saber mais sobre Promise vejá em: 
 * https://developer.mozilla.org/pt-BR/docs/Web/JavaScript/Reference/Global_Objects/Promise
 */

// Arquivo que configuramos anteriormente
const db = require('./../config/db')

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
 * Um modelo de resposta.
 * A cada resposta seja de erro ou de sucesso, sera usado este objeto
 */
var contructResult = exports.contructResult = function(status = 200, msg = "Result OK!", 
                                stackErro = null, result = null){
    var objResult = {
        "status": Number,
        "msg": String,
        "stackErro": {},
        "result": {}
    }

    objResult.status = status

    objResult.msg = msg

    objResult.stackErro = stackErro

    objResult.result = result

    return objResult;
}

/**
 * Aqui vemos um exemplo classico de como usar o exports do node
 * Isso seria equivalente a exportar uma função, para outra parte
 * do projeto que precisar desta função.
 * 
 * neste caso estou exportando uma lista de produtos vindo do DB
 */
exports.list = function(){
    return new Promise((resolve, reject) => {
        // Aqui estou requisitando todos os produtos ao DB
        db.Product.find({}, (err, products) => {
            if(err){
                // OPS: obtivemos um erro nesta etapa.
                console.log("Erro na requisição de produtos ao banco de dados \n" + err)
                reject(contructResult(403, "Obtivemos um erro na requisição dos produtos", err))
            }else{
                // Deu tudo certo, temos uma lista de produtos
                resolve(contructResult(undefined, undefined, undefined, products))
            }
        })
    })
}

/**
 * Retorna um produto com base em um ID
 */
exports.product = function(id){
    return new Promise((resolve, reject) => {

        // Aqui estou fazendo a requisição
        db.Product.findById(id, (err, product) => {
            if(err){
                // OPS: obtivemos um erro nesta etapa.
                console.log("Erro na requisição de um produto ao banco de dados \n" + err)
                reject(contructResult(403, "Obtivemos um erro na requisição do produto com id " + id, err))
            }else{
                if(product){
                    // Deu tudo certo temos nosso produto
                    resolve(contructResult(undefined, undefined, undefined, product))
                }else{
                    // Tivemos um impecilho, não há nenhum produto com este nome
                    resolve(contructResult(undefined, "Nenhum produto encontrado com este id", undefined, undefined))
                }
            }
        })
    })
}

/**
 * Este método permite salvar um produto no banco,
 * Será necessario um nome para o produto e o preço.
 */
exports.save = function(name, price){
    return new Promise((resolve, reject) => {
        new db.Product({
            "name": name,
            "price": price
        }).save((err, product) => {
            if(err){
                // OPS: obtivemos um erro nesta etapa.
                console.log("Erro no momento de salvar o produto \n" + err)
                reject(contructResult(503, "Obtivemos um erro no momento de salvamento do produto ", err))
            }else{
                resolve(contructResult(undefined, undefined, undefined, product))
            }
        })
    })
}

/**
 * Neste método estamos fazendo um update de um registro.
 * 
 * Será necessario o id do produto, e suas devidas propiedades.
 */
exports.update = function(id, name, price){
    return new Promise((resolve, reject) => {
        db.Product.findById(id, (err, product) => {
            if(err){
                console.log('Não foi possivel achar este produto, ' + err)
                reject(contructResult(403, 'Obtivemos um erro no momento de salvamento do produto ', err))
            }else{
                /**
                 * Repare que aqui estamos validando se esta 
                 * vindo algum nome ou preço. Se não estiver vindo
                 * nada, ou seja undefined, então não sera atribuido
                 * nada na propiedade corespondente.
                 */
                if(name){
                    product.name = name
                }
                if(price){
                    product.price = price
                }

                /**
                 * Depois das alterações vamos salvar
                 */
                product.save((err, productSaved) => {
                    if(err){
                        reject(contructResult(503, 'Obtivemos um erro no momento de salvamento do produto ', err))
                    }else{
                        resolve(contructResult(undefined, undefined, undefined, productSaved))
                    }
                })
            }
        })
    })
}

/**
 * Neste métodos esmos fazendo uma exclusão de um produto
 * 
 * Apenas precisamos do ID do produto para fazer esta exclusão.
 */
exports.delete = function(id){
    return new Promise((resolve, reject) => {
        db.Product.findById(id, (err, product) => {
            if(err){
                reject(contructResult(403, 'Obtivemos um erro ao realizar a consulta do produto ', err))
            }else{
                if(product){
                    product.remove((error) => {
                        if(error){
                            reject(contructResult(403, 'Obtivemos um erro ao realizar a remoção do produto ', err))
                        }else{
                            resolve(contructResult(undefined, 'Produto removido com sucesso', undefined, product))
                        }
                    })
                }else{
                    reject(contructResult(403, 'Obtivemos um erro ao realizar a consulta do produto ', err))
                }
            }
        })
    })
}

/**
 * Pronto outra etapa concluido, agora temos o controle de todo nosso CRUD
 * 
 * Vamos ver agora como fazer uma rota dentro do express para cada acesso.
 * 
 * O proximo arquivo é o /app.js
 */