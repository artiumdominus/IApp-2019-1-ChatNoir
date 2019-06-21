
# IApp-2019-1-ChatNoir
Trabalho da disciplina de Integração de Aplicações

## Equipe:
201602511 - Pedro Basilio de Camargo Neto

## Tema do trabalho:
Serviço de mensagens

## Atividades Supervisionadas
- AS01 › Integração de Aplicações baseada na troca de arquivos com campos posicionais
- AS02 › Integração de Aplicações baseada na troca de arquivos JSON
- AS03 › Integração de Aplicações baseada na troca de arquivos XML
- AS04 › API RESTful & Interface

## Lista de requisitos:
### AS01
- O sistema deve manter perfis de usuários.
- O sistema deve manter grupos de conversação.
- O sistema deve manter as mensagens.
- O sistema deve permitir os usuários enviarem mensagens para outros usuários ou para grupos.
- O sistema deve permitir os usuários criarem grupos e adicionar outros usuários a eles.
- O sistema deve permitir os usuários removerem membros de seus grupos
- O sistema deve permitir o criador de um determinado grupo promover outros usuários a administrador.
- O sistema deve permitir o criador de um determinado grupo rebaixar adminstradores a membros comuns.

### AS02
- O sistema deve permitir os usuários recuperarem mensagens que foram enviadas para eles.
- O sistema deve permitir um usuário entrar em um grupo público.

### AS03
* --

### AS04
* --

## Manual da API RESTful:
As requisições deverão ser feitas para diretórios de um determinado endereço que por hora vamos chamar de **host/**

### Login / Logout ( api/log/ )
Para realizar algumas das operações é necessário estar logado. Para isto é necessário realizar a operação de login e obter um token que deverá ser utilizado para se identificar nestas operações restritas. Também pode ser pedido um novo token (relog) ou a destruição do token (logout) para se garantir que operações não serão feitas em seu nome até o próximo login.

- Login: POST » **host**/api/log/

*input:*
```json
{
    "username": "username",
    "password": "xxxxxxxxxxxx",
}
```
*resposta:*
```json
{
    "token": "##############################",
}
```
<br>

- Relog: PUT » **host**/api/log/

*input:*
```json
{
    "username": "username",
    "password": "xxxxxxxxxxxx",
    "token": "##############################",
}
```
*resposta:*
```json
{
    "oldtoken": "##############################",
    "newtoken": "##############################",
}
```
<br>

- Logout: DELETE » **host**/api/log/

*input:*
```json
{
    "token": "##############################",
}
```
*resposta:*
```json
{
    "username": "username",
    "deletedtoken": "##############################",
}
```

### Person ( api/persons/ )

- GET » **host**/api/persons/ : Obtém uma lista de usuários do Chat Noir
- GET » **host**/api/persons/{id}/ : Obtém informações de um usuário específico
- POST » **host**/api/persons/ : Logon (Cria uma conta no Chat Noir)
- PUT » **host**/api/persons/{id}/ : Atualiza informações de perfil
- DELETE » **host**/api/persons/{id}/ : Exclui a sua conta

| Campos          | Tipo   | input | ouput | null | update |
|-----------------|--------|:-----:|:-----:|:----:|:------:|
| id              | int    |       |   ×   |      |        |
| name            | string |   ×   |   ×   |      |    ×   |
| username        | string |   ×   |   ×   |      |    ×   |
| password        | string |   ×   |       |      |    ×   |
| telephoneNumber | string |   ×   |   ×   |      |    ×   |
| bio             | string |   ×   |   ×   |   ×  |    ×   |
| address         | int    |       |   ×   |      |        |

### Group ( api/groups/ )

- GET » **host**/api/groups/ : Obtém uma lista de grupos públicos ou que o usuário logado participe
- GET » **host**/api/groups/{id}/ : Obtém informações sobre um grupo específico
- POST » **host**/api/groups/ : Cria um grupo
- PUT » **host**/api/groups/{id}/ : Atualiza as informações do grupo
- DELETE » **host**/api/groups/{id}/ : Exclui grupo

| Campos      | Tipo          | input | ouput | null | update |
|-------------|---------------|:-----:|:-----:|:----:|:------:|
| id          | int           |       |   ×   |      |        |
| name        | string        |   ×   |   ×   |      |    ×   |
| groupname   | string        |   ×   |   ×   |      |    ×   |
| description | string        |   ×   |       |   ×  |    ×   |
| type*       | string        |   ×   |   ×   |      |    ×   |
| address     | int           |       |   ×   |      |        |
| creator     | string        |       |   ×   |      |        |
| members     | array<string> |       |   ×   |      |        |

*type can hold two possible values: "public" & "private"

### Membership ( api/memberships/ )

- GET » **host**/api/memberships/ : Obtém uma lista de participações em grupos públicos
- GET » **host**/api/memberships/{id}/ : Obtém uma participação específica
- POST » **host**/api/memberships/ : Adiciona um membro a um grupo, ou entra em um grupo público
- PUT » **host**/api/memberships/{id}/ : Promove um membro a administrador ou rebaixa a membro coum
- DELETE » **host**/api/memberships/{id}/ : Remove um membro de um grupo

| Campos | Tipo           | input | ouput | create | update |
|--------|----------------|:-----:|:-----:|:------:|:------:|
| id     | int            |       |   ×   |        |        |
| group  | → int/string → |   ×   |   ×   |    ×   |    ×   |
| person | → int/string → |   ×   |   ×   |    ×   |    ×   |
| admin  | boolean        |   ×   |   ×   |        |    ×   |

### Message ( api/messages/ )

- GET » **host**/api/messages/ : Obtém uma lista de mensagens
- GET » **host**/api/messsages/{id}/ : Obtém informações de uma mensagem específica
- POST » **host**/api/messages/ : Envia uma mensagem
- PUT » **host**/api/messages/{id}/ : Edita uma mensagem / Marca como entregue ou visualizado
- DELETE » **host**/api/messages/{id}/ : Exclui uma mensagem

| Campos   | Tipo     | input | ouput | create | update |
|----------|----------|:-----:|:-----:|:------:|:------:|
| id       | int      |       |   ×   |        |        |
| content  | string   |   ×   |   ×   |    ×   |    ×   |
| dispatch | datetime |       |   ×   |        |        |
| emitter  | string   |       |   ×   |        |        |
| receptor | id       |       |   ×   |        |        |
| status   | string   |   ×   |   ×   |        |    ×   |

### Chat ( api/chats/ )

- GET » **host**/api/chats/ : Funcionalidade inútil
- GET » **host**/api/chats/{id}/ : Funcionalidade inútil
- POST » **host**/api/chats/ : Funcionalidade inútil
- PUT » **host**/api/chats/{id}/ : Funcionalidade inútil
- DELETE » **host**/api/chats/{id}/ : Funcionalidade inútil

| Campos           | Tipo     | input | ouput | null | update |
|------------------|----------|:-----:|:-----:|:----:|:------:|
| id               | int      |       |   ×   |      |        |
| background_color | string   |   ×   |   ×   |   ×  |    ×   |
| text_color       | datetime |   ×   |   ×   |   ×  |    ×   |
