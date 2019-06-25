## Tests:

[×] Login: POST » host/api/log/  

[×] GET » host/api/persons/ : Obtém uma lista de usuários do Chat Noir  
[×] GET » host/api/persons/{id}/ : Obtém informações de um usuário específico  
[×] POST » host/api/persons/ : Logon (Cria uma conta no Chat Noir)  
[×] PUT » host/api/persons/{id}/ : Atualiza informações de perfil  
[×] DELETE » host/api/persons/{id}/ : Exclui a sua conta  

[×] GET » host/api/groups/ : Obtém uma lista de grupos públicos ou que o usuário logado participe  
[×] GET » host/api/groups/{id}/ : Obtém informações sobre um grupo específico  
[×] POST » host/api/groups/ : Cria um grupo  
[×] PUT » host/api/groups/{id}/ : Atualiza as informações do grupo  
[×] DELETE » host/api/groups/{id}/ : Exclui grupo  

[×] GET » host/api/memberships/ : Obtém uma lista de participações em grupos públicos  
[×] GET » host/api/memberships/{id}/ : Obtém uma participação específica  
[×] POST » host/api/memberships/ : Adiciona um membro a um grupo, ou entra em um grupo público  
[×] PUT » host/api/memberships/{id}/ : Promove um membro a administrador ou rebaixa a membro coum  
[×] DELETE » host/api/memberships/{id}/ : Remove um membro de um grupo  

[ ] GET » host/api/messages/ : Obtém uma lista de mensagens  
[ ] GET » host/api/messsages/{id}/ : Obtém informações de uma mensagem específica  
[×] POST » host/api/messages/ : Envia uma mensagem  
[ ] PUT » host/api/messages/{id}/ : Edita uma mensagem / Marca como entregue ou visualizado  
[ ] DELETE » host/api/messages/{id}/ : Exclui uma mensagem  

[-] GET » host/api/chats/ : Funcionalidade inútil  
[-] GET » host/api/chats/{id}/ : Funcionalidade inútil  
[-] POST » host/api/chats/ : Funcionalidade inútil  
[-] PUT » host/api/chats/{id}/ : Funcionalidade inútil  
[-] DELETE » host/api/chats/{id}/ : Funcionalidade inútil  

## Tarefas:

[ ] Implementar Relog  
[ ] Implementar Logout  
[ ] Corrigir PUT » host/api/persons/{id}/  
[ ] Melhorar GET » host/api/groups/  
[ ] Melhorar GET » host/api/groups/{id}/  
[ ] Implementar input/output literal do grouptype  
[ ] Filtrar Memberships em GET » host/api/memberships & GET » host/api/memberships/{id}/  
[ ] Evitar duplicação de 'Memberships'  
[ ] Melhorar GET » host/api/messages/
