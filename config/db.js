/* WEB SERVICE 2 - CONFIGURAÇÂO DO MONGODB */

/**
 * Bom vamos a configuração da comunicação do MongoDB,
 * aqui vou usar o mongoose para fazer esta etapa.
 * 
 * Você pode saber mais sobre o MongoDB aqui:
 * Site: https://www.mongodb.com/
 * Doc: https://docs.mongodb.com/
 * 
 * Mongoose é um object modeling para o nodejs,
 * você pode ver mais sobre ele nos seguintes links:
 * Site: http://mongoosejs.com/
 * Doc: http://mongoosejs.com/docs/guide.html
 */

/**
 * Todas as informações que precisamons para criar o uma comunicação,
 * com o MongoDB, estão presentes dentro deste arquivo app_options.json
 */
const options = require('./app_options.json')

var db_string = `mongodb://${options.DB_HOST}:${options.DB_PORT}/${options.DB_NAME}`

const mongoose = require('mongoose')
mongoose.Promise = global.Promise
mongoose.connect(db_string)

// Aqui esta sendo feito a comunicação
var db = mongoose.connection

/**
 * Aqui verificamos se teve algun erro na comunicação 
 * entre o Web Service e o MongoDB
 */ 
db.on('error', console.error.bind(console, 'Erro ao conectar no banco'));

/**
 * Aqui estamos com uma conexão aberta ao MongoDB utilizando o Mongoose
 * Feito isso podemos criar nossos modelos.
 */
db.once('open', function(){

    // Modelo do documento de produto
    var productSchema = mongoose.Schema({
        // Nome do produto
        name: {type: String, required: true},
        // Preço do produto
        price: {type: Number, required: true},

        /*
            Para podermos ter um controle maior no futuro, 
            adicionamos um campo de data para sabermos quando 
            foi criado o produto
        */
        create_at: {type: Date, default: Date.now}
    })

    /*
        Vamos agora exportar esse modelo para toda a aplicação. 
        Desta forma quando quisermos que algun outro modulo acesse o produto,
        podemos apenas fazer um require, e pronto
    */
    exports.Product = mongoose.model('Product', productSchema)

});

/**
 * Esta etapa esta concluida agora vamos para os controlers.
 * O controllers sera o local onde armazenaremos os responsaveis,
 * por fazer controle e requisição para o model.
 * 
 * o proximo arquivo se encontra em /controllers/product.js
 */