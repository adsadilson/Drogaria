/*Cadastro de Grupo de Usuario*/
INSERT INTO grupo_usuario(id, descricao, nome, status) VALUES (1,'SETOR ADMINISTRATIVO', 'ADMINISTRADOR', true);
INSERT INTO grupo_usuario(id, descricao, nome, status) VALUES (2,'SETOR ESCRITORIADO', 'AUXILIAR DE ESCRITÓRIO', true);


/*Cadastro de Controle de Menu*/
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (1, 'home.xhtml', 'HOME', '/home.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (2, 'grupoUsuario.xhtml', 'CADASTRO GRUPO DE USUÁRIO', '/pages/cadastro/grupoUsuario/grupoUsuario.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (3, 'usuario.xhtml', 'CADASTRO DE USUÁRIO', '/pages/cadastro/usuario/usuario.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (4, 'trocaSenha.xhtml', 'TELE TROCA DE SENHA', '/pages/cadastro/trocaSenha/trocaSenha.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (5, 'controleMenu.xhtml', 'CADASTRO DE MENU', '/pages/cadastro/controleMenu/controleMenu.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (6, 'cliente.xhtml', 'CADASTRO DE CLIENTE', '/pages/cadastro/cliente/cliente.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (7, 'erro.xhtml', 'ERRO', '/erro.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (8, 'categoria.xhtml', 'CADASTRO DE CATEGORIA', '/pages/cadastro/categoria/categoria.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (9, 'subCategoria.xhtml', 'CADASTRO DE SUBCATEGORIA', '/pages/cadastro/subCategoria/subCategoria.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (10, 'unidadeMedida.xhtml', 'CADASTRO DE UNIDADE DE MEDIDA', '/pages/cadastro/unidadeMedida/unidadeMedida.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (11, 'produto.xhtml', 'CADASTRO DE PRODUTO', '/pages/cadastro/produto/produto.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (12, 'plantoConta.xhtml', 'CADASTRO PLANO DE CONTA', '/pages/cadastro/planoConta/planoConta.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (13, 'fornecedor.xhtml', 'CADASTRO DE FORNECEDOR', '/pages/cadastro/fornecedor/fornecedor.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (14, 'funcionario.xhtml', 'CADASTRO DE FUNCIONARIO', '/pages/cadastro/funcionario/funcionario.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (15, 'movimentacao.xhtml', 'MOVIMENTAÇÃO DE CONTAS', '/pages/financeiro/movimentacao/movimentacao.xhtml');
INSERT INTO controle_menu(id, formulario, funcao, url) VALUES (16, 'contaAPagar', 'CONTAS APAGAR', '/pages/financeiro/contaAPagar/contaAPagar.xhtml');

/*Cadastro de permissao*/
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (1,TRUE,TRUE,FALSE,TRUE,FALSE,1,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (2,TRUE,TRUE,FALSE,TRUE,FALSE,2,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (3,TRUE,TRUE,FALSE,TRUE,FALSE,3,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (4,TRUE,TRUE,FALSE,TRUE,FALSE,4,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (5,TRUE,TRUE,FALSE,TRUE,FALSE,5,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (6,FALSE,FALSE,FALSE,TRUE,FALSE,6,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (7,FALSE,FALSE,FALSE,FALSE,FALSE,7,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (8,FALSE,FALSE,FALSE,FALSE,FALSE,8,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (9,FALSE,FALSE,FALSE,FALSE,FALSE,9,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (10,FALSE,FALSE,FALSE,FALSE,FALSE,10,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (11,FALSE,FALSE,FALSE,FALSE,FALSE,11,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (12,FALSE,FALSE,FALSE,FALSE,FALSE,12,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (13,FALSE,FALSE,FALSE,FALSE,FALSE,13,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (14,FALSE,FALSE,FALSE,FALSE,FALSE,14,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (15,FALSE,FALSE,FALSE,FALSE,FALSE,15,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (16,FALSE,FALSE,FALSE,FALSE,FALSE,16,1,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (17,FALSE,FALSE,FALSE,FALSE,FALSE,1,2,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (18,FALSE,FALSE,FALSE,FALSE,FALSE,2,2,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (19,FALSE,FALSE,FALSE,FALSE,FALSE,3,2,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (20,FALSE,FALSE,FALSE,FALSE,FALSE,4,2,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (21,FALSE,FALSE,FALSE,FALSE,FALSE,5,2,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (22,FALSE,FALSE,FALSE,FALSE,FALSE,6,2,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (23,FALSE,FALSE,FALSE,FALSE,FALSE,7,2,TRUE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (24,FALSE,FALSE,FALSE,FALSE,FALSE,8,2,FALSE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (25,FALSE,FALSE,FALSE,FALSE,FALSE,9,2,FALSE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (26,FALSE,FALSE,FALSE,FALSE,FALSE,10,2,FALSE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (27,FALSE,FALSE,FALSE,FALSE,FALSE,11,2,FALSE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (28,FALSE,FALSE,FALSE,FALSE,FALSE,12,2,FALSE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (29,FALSE,FALSE,FALSE,FALSE,FALSE,13,2,FALSE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (30,FALSE,FALSE,FALSE,FALSE,FALSE,14,2,FALSE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (31,FALSE,FALSE,FALSE,FALSE,FALSE,15,2,FALSE);
INSERT INTO permissao(id, alterar, excluir, imprimir, incluir, visualizar, controle_menu_id, grupo_usuario_id, formulario) VALUES (32,FALSE,FALSE,FALSE,FALSE,FALSE,16,2,FALSE);


/*Cadastro de usuario*/
INSERT INTO usuario(id, cadastro, email, nome, senha, status) VALUES (1,'"2017-12-27 13:18:52.624"','adilson.curso@yahoo.com.br','ADILSON','c4ca4238a0b923820dcc509a6f75849b',true);
INSERT INTO usuario(id, cadastro, email, nome, senha, status) VALUES (2,'"2017-12-27 13:18:52.624"','ronaldo@gmail.com','RONALDO','c4ca4238a0b923820dcc509a6f75849b',true);
INSERT INTO usuario(id, cadastro, email, nome, senha, status) VALUES (3,'"2017-12-27 13:18:52.624"','fernandag23@bol.com.br','FERNANDA','c4ca4238a0b923820dcc509a6f75849b',true);

/*Cadastro usuario_grupo*/
INSERT INTO usuario_grupo(usuario_id, grupo_id) VALUES (1, 1);
INSERT INTO usuario_grupo(usuario_id, grupo_id) VALUES (1, 2);
INSERT INTO usuario_grupo(usuario_id, grupo_id) VALUES (2, 2);


/*Cadastro de unidade de medida*/
INSERT INTO unidade_medida(id, descricao, nome, status) VALUES (1, 'UNIDADE','UND', true);
INSERT INTO unidade_medida(id, descricao, nome, status) VALUES (2, 'KILOGRAMA','KG', true);
INSERT INTO unidade_medida(id, descricao, nome, status) VALUES (3, 'PACOTE','PC', true);
INSERT INTO unidade_medida(id, descricao, nome, status) VALUES (4, 'CAIXA','CX', true);
INSERT INTO unidade_medida(id, descricao, nome, status) VALUES (5, 'LITRO','L', true);
INSERT INTO unidade_medida(id, descricao, nome, status) VALUES (6, 'LATA','LT', true);
INSERT INTO unidade_medida(id, descricao, nome, status) VALUES (7, 'SACO','SC', true);
INSERT INTO unidade_medida(id, descricao, nome, status) VALUES (8, 'METRO','MT', true);
INSERT INTO unidade_medida(id, descricao, nome, status) VALUES (9, 'BALDE','BD', true);

/*Cadastro plano de conta*/
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (1,'S','1.00.00.00','CONTAS CORRENTES',TRUE,'CC',null);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (2,'S','2.00.00.00','RECEITAS',TRUE,'R',null);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (3,'S','3.00.00.00','DESPESAS',TRUE,'D',null);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (4,'A','1.00.00.01','CAIXA ADM',TRUE,'CC',1);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (5,'A','1.00.00.02','BCO DO BRASIL C/C: 6970-1',TRUE,'CC',1);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (6,'A','1.00.00.03','BCO DO BRADESCO C/C: 228-3',TRUE,'CC',1);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (7,'A','2.00.00.01','RECEITAS COM VENDAS',TRUE,'R',2);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (8,'A','2.00.00.02','RECEBIMENTOS NOTAS APRAZO',TRUE,'R',2);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (9,'S','3.01.00.00','DESPESAS ADM',TRUE,'D',3);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (10,'S','3.02.00.00','DESPESA COM FUNCIONARIOS',TRUE,'D',3);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (11,'S','3.03.00.00','DESPESAS COM VENDAS',TRUE,'D',3);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (12,'A','3.01.00.01','AGUA E ESGOTO',TRUE,'D',9);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (13,'A','3.01.00.02','ENERGIA ELETRICA',TRUE,'D',9);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (14,'A','3.01.00.03','CORREIOS',TRUE,'D',9);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (15,'A','3.01.00.04','TELEFONE(S)',TRUE,'D',9);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (16,'A','3.02.00.01','SALARIOS',TRUE,'D',10);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (17,'A','3.02.00.02','FERIAS',TRUE,'D',10);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (18,'A','3.02.00.03','BENEFICIOS',TRUE,'D',10);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (19,'A','3.03.00.01','COMISSAO DE VENDAS',TRUE,'D',11);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (20,'A','3.03.00.02','PROPAGANDA E PUBLICIDADE',TRUE,'D',11);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (21,'A','3.03.00.03','MATERIAL DE APOIO',TRUE,'D',11);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (22,'A','3.03.00.04','DESCONTOS CONCEDIDOS',TRUE,'D',11);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (23,'S','2.01.00.00','RECEITA COM RESSIRCAMENTOS',TRUE,'R',2);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (24,'A','2.01.00.01','RESSARCIMENTO COM VENDAS ',TRUE,'R',23);
INSERT INTO plano_conta(id, categoria, mascara, nome, status, tipo, conta_pai_id) VALUES (25,'A','2.01.00.02','RESSARCIMENTO COM COMPRAS',TRUE,'R',23);









