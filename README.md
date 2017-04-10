# Tutorial de como fazer uma aplicação Android escrita em Kotlin conversar com um Web Server RestFul feito em NodeJS

Este tutorial tem o intuito de sanar algumas duvidas encontradas no desenvolvimento de aplicações Android, com comunicação entre Aplicações Web Services RestFul

Sera desenvolvido uma aplicação Android simples de controle de produtos, onde devera se comunicar com um Web Service RestFul que mantera o controle destes produtos.

### Tecnologias usadas no desenvolvimento

* NodeJs - Sera a base da nosa aplicação Web Service
* Express - Um Framework nodejs, muito utilizado na web.
* MongoDB - É nossa base de dados, onde vai ficar armazenado todos os produtos.
* Android - Será a plataforma da aplicação. (É importante frizar que sera usado a linguagem de programação Kotlin, nessa etapa).

### Arquitetura do Projeto

```
.
├── app.js -> Rotas
├── config -> Aqui onde vai ficar as configurações da aplicação
│   ├── app.js -> Configurações do Express
│   ├── app_options.json -> Informaçẽos uteis, como a Porta
│   └── db.js -> Configuração do DB
├── controllers -> Aqui onde vai ficar o controller dos modelos
│   └── product.js -> Controller do modelo Product
├── package.json -> Modulos e informações do Web Server
```

### Ordem do Tutorial

A ordem serve para você não se perder no projeto. É equivalente a um Sumario.
Dentro de cada arquivo tem uma explicação de como funciona determinada etapa.

1. WebService > config > app.js - Configuração do WebService