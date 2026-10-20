# Instruções do projeto

- Projeto estático com `index.html`, páginas de módulos em `html/`, `css/style.css`, `js/api.js` e `js/main.js`.
- Use HTML, CSS e JavaScript puro; não adicione framework, dependência ou ferramenta de build sem solicitação.
- Centralize a origem e as chamadas HTTP em `js/api.js`. O login usa `POST http://localhost:8080/login` com `login` e `senha`; confirme mudanças de contrato no Swagger.
- No DOM, renderize conteúdo recebido da API usando `textContent` e mantenha estados de carregamento, vazio e erro acessíveis.
- Não há compilação ou instalação de dependências necessária.

## Configuração concluída

- Requisitos definidos: dashboard responsivo, telas-base para módulos e login integrado com Fetch.
- Estrutura base criada na raiz aberta do workspace e páginas em `html/`.
- Integração configurada para `POST http://localhost:8080/login` conforme o contrato informado.
- Nenhuma extensão ou tarefa de build é necessária.
- O dashboard pode ser aberto diretamente; login via API requer backend ativo e CORS.
- Documentação incluída em `README.md`.
