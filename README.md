# Projetos 3: Park Easy
ParkEasy é um aplicativo que conecta usuários procurando vagas para estacionar veículos à vagas disponíveis próximas cadastradas por um certo valor.

Tecnologias usadas no desenvolvimento: Java, Spring Boot, React, PostgreeSQL, Node.js, Spring Web, JavaScript, CSS3, HMTL5.

| Membro | E-mail |
| :--- | :--- |
| Bernardo Pedrosa Alves Abreu | bpaa@cesar.school |
| Diego José Arroxelas Galvão Albuquerque Maranhão | Djagam@cesar.school |
| Fabio Henrique Dantas Layme Lopes de Albuquerque | fhdlla@cesar.school |
| Francisco de Amorim Marquese | fam3@cesar.school |
| Guilherme Henrique Leite Nóbrega | ghln@cesar.school |
| Pedro Gabriel Paes da Justa Silva | pgpjs@cesar.school |
| Lucas Rocha Calado | lrc3@cesar.school |
| Maria Clara Miranda Ferraz | Mcmf2@cesar.school |

# Entrega 1
Histórias de Usuário — Plataforma Web de Estacionamento

Objetivo

Este documento apresenta as histórias de usuário da plataforma web de estacionamento, acompanhadas de cenários de validação utilizando BDD (Behavior-Driven Development).

As histórias descrevem as funcionalidades do sistema a partir da perspectiva dos usuários, enquanto os cenários BDD especificam os comportamentos esperados da aplicação diante de diferentes situações.

Os cenários utilizam a estrutura:

- Dado que — contexto inicial;
- Quando — ação realizada pelo usuário;
- Então — comportamento esperado do sistema.

A plataforma será disponibilizada por meio de uma aplicação web, permitindo que motoristas e operadores de estacionamento utilizem suas funcionalidades através de um navegador.

---

História de Usuário 1 — Filtrar vagas acessíveis (PCD)

Como motorista com deficiência,
quero filtrar vagas acessíveis (PCD),
para garantir que o local atenda às minhas necessidades.

Regras de negócio

- O sistema deve permitir que o motorista utilize um filtro para visualizar somente vagas acessíveis (PCD).
- O resultado da busca deve considerar somente as vagas identificadas como acessíveis quando o filtro estiver ativo.
- Caso não existam vagas que atendam ao filtro, o sistema deve informar que nenhuma vaga acessível foi encontrada.

Cenários BDD

Cenário 1 — Encontrar vagas acessíveis

Dado que o motorista está pesquisando vagas disponíveis
E existem vagas acessíveis (PCD) na região pesquisada
Quando o motorista ativar o filtro de vagas acessíveis
Então o sistema deve apresentar as vagas que atendem ao filtro.

Cenário 2 — Nenhuma vaga acessível disponível

Dado que o motorista está pesquisando vagas disponíveis
E não existem vagas acessíveis (PCD) na região pesquisada
Quando o motorista ativar o filtro de vagas acessíveis
Então o sistema deve informar que nenhuma vaga acessível foi encontrada.

---

História de Usuário 2 — Visualizar vagas próximas no mapa

Como motorista,
quero visualizar no mapa as vagas disponíveis próximas à minha localização atual,
para encontrar rapidamente onde estacionar.

Regras de negócio

- O sistema deve utilizar a localização atual do motorista quando o acesso à localização for autorizado pelo navegador.
- O mapa deve apresentar as vagas disponíveis próximas à localização considerada.
- As vagas exibidas devem indicar sua disponibilidade atual.
- Caso o motorista não autorize o acesso à localização, o sistema não deve assumir sua localização atual.

Cenários BDD

Cenário 1 — Visualizar vagas próximas

Dado que o motorista autorizou o acesso à sua localização pelo navegador
E existem vagas disponíveis próximas à sua localização atual
Quando o motorista acessar o mapa de vagas
Então o sistema deve apresentar as vagas disponíveis próximas à sua localização.

Cenário 2 — Localização não autorizada

Dado que o motorista não autorizou o acesso à sua localização pelo navegador
Quando ele acessar o mapa de vagas
Então o sistema não deve assumir uma localização atual sem autorização
E deve informar que a permissão de localização é necessária para utilizar a busca baseada na localização atual.

Cenário 3 — Atualização da disponibilidade

Dado que o motorista está visualizando o mapa de vagas
E uma vaga exibida altera seu status de disponibilidade
Quando o sistema atualizar as informações das vagas
Então o mapa deve refletir o novo status da vaga.

---

História de Usuário 3 — Pesquisar vagas por endereço

Como motorista,
quero pesquisar vagas próximas a um endereço específico,
para planejar onde estacionar antes de chegar ao meu destino.

Regras de negócio

- O sistema deve permitir que o motorista informe um endereço para realizar a pesquisa.
- A busca deve considerar o endereço informado, independentemente da localização atual do motorista.
- O sistema deve informar quando não houver vagas disponíveis próximas ao endereço pesquisado.
- O sistema não deve apresentar resultados para um endereço que não tenha sido localizado ou validado.

Cenários BDD

Cenário 1 — Pesquisar um endereço válido

Dado que o motorista está na página de pesquisa de vagas
Quando ele informar um endereço válido
Então o sistema deve apresentar as vagas disponíveis próximas ao endereço informado.

Cenário 2 — Endereço inválido ou inexistente

Dado que o motorista está na página de pesquisa de vagas
Quando ele informar um endereço inválido ou inexistente
Então o sistema deve informar que o endereço não foi localizado
E não deve apresentar resultados de vagas para um endereço não identificado.

Cenário 3 — Nenhuma vaga disponível no endereço pesquisado

Dado que o motorista informou um endereço válido
E não existem vagas disponíveis próximas ao endereço
Quando a pesquisa for realizada
Então o sistema deve informar que nenhuma vaga disponível foi encontrada naquela região.

---

História de Usuário 4 — Criar perfil comercial do estacionamento

Como operador de estacionamento,
quero criar um perfil comercial com os dados do estabelecimento,
para divulgar meu espaço na plataforma web.

Regras de negócio

- O operador deve poder cadastrar os dados do estabelecimento.
- O perfil deve conter as informações necessárias para apresentar o estacionamento aos motoristas, incluindo endereço, horário de funcionamento e fotos.
- O sistema não deve permitir a conclusão do cadastro enquanto informações obrigatórias estiverem ausentes.
- As informações cadastradas devem ficar disponíveis para consulta pelos motoristas conforme as regras de publicação do sistema.

Cenários BDD

Cenário 1 — Criar perfil com dados completos

Dado que o operador está na página de criação do perfil comercial
E informou o endereço, o horário de funcionamento e adicionou as fotos necessárias
Quando ele solicitar a criação do perfil
Então o sistema deve cadastrar o perfil comercial
E disponibilizar as informações cadastradas na plataforma conforme as regras de publicação.

Cenário 2 — Tentar criar perfil sem informação obrigatória

Dado que o operador está preenchendo o perfil comercial
E existe uma informação obrigatória que não foi preenchida
Quando ele tentar concluir o cadastro
Então o sistema deve informar que o preenchimento da informação é necessário
E não deve concluir o cadastro até que os dados obrigatórios sejam fornecidos.

Cenário 3 — Perfil criado corretamente

Dado que o perfil comercial foi criado com todos os dados necessários
Quando um motorista consultar os estacionamentos disponíveis na plataforma
Então o perfil deve apresentar as informações cadastradas do estabelecimento.

---

História de Usuário 5 — Visualizar valor estimado

Como motorista,
quero visualizar o valor estimado antes de utilizar a vaga,
para saber quanto poderei pagar pelo estacionamento.

Regras de negócio

- O sistema deve apresentar ao motorista uma estimativa do valor a ser pago antes da utilização da vaga.
- O valor apresentado deve ser identificado como estimado.
- Caso não seja possível calcular uma estimativa, o sistema deve informar o motorista em vez de apresentar um valor incorreto.
- A estimativa deve considerar as informações de preço e o período previsto de utilização disponíveis no sistema.

Cenários BDD

Cenário 1 — Visualizar valor estimado

Dado que o motorista selecionou uma vaga com informações de preço disponíveis
Quando ele consultar os detalhes da vaga
Então o sistema deve apresentar o valor estimado a ser pago
E deve identificá-lo como um valor estimado.

Cenário 2 — Informações insuficientes para estimativa

Dado que o motorista selecionou uma vaga
E não existem informações suficientes para calcular o valor estimado
Quando ele consultar os detalhes da vaga
Então o sistema deve informar que não foi possível calcular uma estimativa
E não deve apresentar um valor como se fosse confirmado.

Cenário 3 — Estimativa baseada no período informado

Dado que o sistema possui as informações necessárias para calcular a estimativa
E o motorista informou o período previsto de utilização
Quando o sistema calcular o valor estimado
Então o sistema deve apresentar uma estimativa correspondente às informações fornecidas.

---

História de Usuário 6 — Reportar problemas nas informações

Como motorista,
quero reportar problemas relacionados às vagas ou às informações apresentadas,
para contribuir com a qualidade dos dados da plataforma.

Regras de negócio

- O motorista deve poder informar problemas encontrados nas vagas ou nas informações apresentadas pela plataforma.
- O sistema deve permitir a identificação do tipo de problema reportado.
- Um reporte não deve ser enviado sem que o motorista informe o tipo de problema.
- O sistema deve registrar o reporte para posterior análise pelo responsável pelo estacionamento ou pela plataforma, conforme o tipo de problema.

Cenários BDD

Cenário 1 — Reportar vaga ocupada indevidamente

Dado que o motorista identificou uma vaga marcada como disponível, mas que está ocupada
Quando ele selecionar a opção de reportar problema e indicar que a vaga está ocupada indevidamente
Então o sistema deve registrar o reporte
E deve confirmar que o problema foi enviado.

Cenário 2 — Reportar informação incorreta

Dado que o motorista identificou uma informação incorreta sobre um estacionamento ou uma vaga
Quando ele selecionar a opção de reportar problema e indicar que a informação está incorreta
Então o sistema deve registrar o reporte
E deve confirmar que o problema foi enviado.

Cenário 3 — Tentar enviar reporte sem informar o problema

Dado que o motorista abriu a opção de reportar um problema
Quando ele tentar enviar o reporte sem selecionar o tipo de problema
Então o sistema deve solicitar que o tipo de problema seja informado
E não deve enviar o reporte.

---

História de Usuário 7 — Atualizar preços por horário

Como operador de estacionamento,
quero configurar preços diferentes para diferentes períodos,
para refletir minha política comercial.

Regras de negócio

- O operador deve poder definir preços diferentes para diferentes períodos.
- O sistema deve permitir a configuração de períodos como tarifa diurna, noturna ou de fim de semana.
- Os preços configurados devem ser utilizados nas informações apresentadas aos motoristas.
- O sistema não deve permitir a configuração de períodos conflitantes ou sobrepostos para uma mesma regra tarifária.

Cenários BDD

Cenário 1 — Cadastrar tarifas para diferentes períodos

Dado que o operador possui um estacionamento cadastrado
Quando ele configurar preços diferentes para períodos distintos
Então o sistema deve salvar as tarifas correspondentes a cada período
E deve utilizar a tarifa aplicável ao período consultado.

Cenário 2 — Tentar cadastrar horários sobrepostos

Dado que o operador já possui uma tarifa configurada para determinado período
Quando ele tentar cadastrar outra tarifa para um período que se sobrepõe ao período existente
Então o sistema deve informar que existe conflito entre os horários
E não deve salvar a configuração conflitante.

Cenário 3 — Alteração de preço para novas consultas

Dado que o operador atualizou a tarifa de determinado período
Quando um motorista consultar o preço aplicável a esse período
Então o sistema deve apresentar a tarifa atualmente configurada para o período.

---

Resumo das Histórias

#| História| Usuário principal| Valor entregue
1| Filtrar vagas acessíveis (PCD)| Motorista| Encontrar vagas adequadas às necessidades de acessibilidade
2| Visualizar vagas próximas no mapa| Motorista| Encontrar rapidamente onde estacionar
3| Pesquisar vagas por endereço| Motorista| Planejar o estacionamento antes de chegar ao destino
4| Criar perfil comercial do estacionamento| Operador| Divulgar o estacionamento na plataforma web
5| Visualizar valor estimado| Motorista| Saber previamente quanto poderá pagar
6| Reportar problemas nas informações| Motorista| Contribuir para a qualidade dos dados da plataforma
7| Atualizar preços por horário| Operador| Manter os preços de acordo com a política comercial

---

Validação BDD

As sete histórias de usuário possuem cenários de validação que contemplam os principais fluxos esperados do sistema, incluindo situações de sucesso, erro, restrição e ausência de dados.

A utilização de BDD permite especificar e validar o comportamento esperado da plataforma sem depender de detalhes de implementação. Dessa forma, os cenários permanecem orientados às necessidades dos usuários e aos valores entregues pelo sistema.

As funcionalidades descritas neste documento poderão ser implementadas como uma aplicação web em Java, sendo acessadas pelos usuários por meio de um navegador. A definição das tecnologias específicas de front-end, back-end, banco de dados, APIs externas e serviços de mapas deve ser apresentada na arquitetura e nos requisitos técnicos do projeto.
