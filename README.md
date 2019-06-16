# IApp-2019-1-UselessChat
Trabalho da disciplina de Integração de Aplicações

## Equipe:
201602511 - Pedro Basilio de Camargo Neto

## Tema do trabalho:
Serviço de mensagens

## Atividades Supervisionadas
* AS01 › Integração de Aplicações baseada na troca de arquivos com campos posicionais
* AS02 › Integração de Aplicações baseada na troca de arquivos JSON
* AS03 › Integração de Aplicações baseada na troca de arquivos XML
* AS04 › API RESTful & Interface

## Lista de requisitos:
### AS01
* O sistema deve manter perfis de usuários.
* O sistema deve manter grupos de conversação.
* O sistema deve manter as mensagens.
* O sistema deve permitir os usuários enviarem mensagens para outros usuários ou para grupos.
* O sistema deve permitir os usuários criarem grupos e adicionar outros usuários a eles.
* O sistema deve permitir os usuários removerem membros de seus grupos
* O sistema deve permitir o criador de um determinado grupo promover outros usuários a administrador.
* O sistema deve permitir o criador de um determinado grupo rebaixar adminstradores a membros comuns.

### AS02
* O sistema deve permitir os usuários recuperarem mensagens que foram enviadas para eles.
* O sistema deve permitir um usuário entrar em um grupo público.

### AS03
* --

### AS04
* --

## Manual da API RESTful:
As requisições deversão ser feitas para diretórios de um determinado endereço que por hora vamos chamar de **host/**

### Login / Logout ‹ api/log/ ›
Para realizar algumas das operações é necessário estar logado. Para isto é necessário realizar a operação de login e obter um token que deverá ser utilizado para se identificar nestas operações restritas. Também pode ser pedido um novo token (relog) ou a destruição do token (logout) para se garantir que operações não serão feitas em seu nome até o próximo login.

#### Login: POST » **host**/api/log/
input:
```json
{
    "username": "username",
    "password": "xxxxxxxxxxxx",
}
```
resposta:
```json
{
    "token": "##############################",
}
```
#### Relog: PUT » **host**/api/log/
input:
```json
{
    "username": "username",
    "password": "xxxxxxxxxxxx",
    "token": "##############################",
}
```
resposta:
```json
{
    "oldtoken": "##############################",
    "newtoken": "##############################",
}
```
#### Logout: DELETE » **host**/api/log/
input:
```json
{
    "token": "##############################",
}
```
resposta:
```json
{
    "username": "username",
    "deletedtoken": "##############################",
}
```
