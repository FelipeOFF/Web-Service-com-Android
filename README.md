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
├── app.js
├── config
│   ├── app.js
│   ├── app_options.json
│   └── db.js
├── controllers
│   └── product.js
├── package.json
└── README.md
```

### Ordem do Tutorial

A ordem serve para você não se perder no projeto. É equivalente a um Sumario.
Dentro de cada arquivo tem uma explicação de como funciona determinada etapa.

1. WebService > config > app.js - Configuração do WebService