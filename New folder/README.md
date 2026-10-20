# EcoTrack

Dashboard estático em HTML, CSS e JavaScript puro. Não requer dependências nem etapa de build.

## Áreas disponíveis

- Materiais: consulta, cadastro e atualização de materiais.
- Gestores: consulta, cadastro e gerenciamento de status.
- Entregas: consulta, registro, cancelamento e atualização de status.
- Pontos de coleta: consulta por local ou identificador.
- Empresas: consulta, cadastro e busca de gestores por CNPJ.
- Cursos: consulta, cadastro e matrícula.

As telas das áreas estão em `html/`. Gestores consome `GET /gestores`, materiais consome `GET /materiais` e entregas carrega cidades de `GET /cidades` e envia cadastros para `POST /entregas`. O formulário envia cidade/UF de origem e destino; os demais módulos permanecem como estruturas iniciais.

## Login e execução

Abra `index.html` em um navegador. A tela `html/login.html` envia `POST http://localhost:8080/login` com `login` e `senha`. O backend precisa estar ativo e permitir CORS para a origem da página. O marcador de sessão em `sessionStorage` é apenas estado visual no navegador; não substitui token ou autorização no backend.
