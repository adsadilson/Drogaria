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

/*Cadastro de Categoria*/
INSERT INTO categoria(id, nome, status) VALUES (1, 'INFORMÁTICA', true);
INSERT INTO categoria(id, nome, status) VALUES (2, 'ELETRÔNICOS', true);
INSERT INTO categoria(id, nome, status) VALUES (3, 'ELETROPORTÁTEIS', true);
INSERT INTO categoria(id, nome, status) VALUES (4, 'MOVEIS', true);
INSERT INTO categoria(id, nome, status) VALUES (5, 'AUTOMOTIVO', true);


/*Cadastro de SubCategoria*/
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (1, 'NOTEBOOK', 1, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (2, 'COMPUTADORES', 1, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (3, 'TABLETE', 1, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (4, 'ACESSORIOS', 1, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (5, 'TV', 2, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (6, 'HOME THEATER', 2, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (7, 'AUDIO', 2, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (8, 'GELADEIRA E REFRIGERADOR', 3, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (9, 'MAQUINA DE LAVAR / SECADORA', 3, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (10, 'FOGOES', 3, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (11, 'MICRO-ONDAS', 3, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (12, 'FRIGOBAR', 3, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (13, 'QUARTOS', 4, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (14, 'SALA DE JANTAR', 4, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (15, 'COZINHA', 4, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (16, 'ESCRITORIO', 4, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (17, 'PNEUS', 5, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (18, 'RODAS E CALOTAS', 5, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (19, 'SOM DE CARRO', 5, true);
INSERT INTO subcategoria(id, nome, categoria_id, status) VALUES (20, 'ILUMINAÇÃO', 5, true);


/*Cadastro de produto*/
INSERT INTO produto(id, codigo_barra, lucro, marg_lucro, nome, quantidade, sku, status, tipo_produto, vlr_custo, vlr_venda, categoria_id, subcategoria_id, unidade_medida_id)
VALUES (1, '124645895145', 612, 35, 'Notebook Acer A515-51-56K6 Intel Core I5 8GB 1TB Tela LED 15.6" Windows 10 - Preto', 150, 'NO0154', true, 'REVENDA', 1750, 2362, 1, 1, 1);
INSERT INTO produto(id, codigo_barra, lucro, marg_lucro, nome, quantidade, sku, status, tipo_produto, vlr_custo, vlr_venda, categoria_id, subcategoria_id, unidade_medida_id)
VALUES (2, '3244515485455', 699.65, 35, 'Notebook Dell Inspiron i15-5566-D10P Intel Core i3 4GB 1TB Tela LED 15.6" Linux - Preto', 350, 'NO0150', true, 'REVENDA', 1999, 2698.65, 1, 1, 1);
INSERT INTO produto(id, codigo_barra, lucro, marg_lucro, nome, quantidade, sku, status, tipo_produto, vlr_custo, vlr_venda, categoria_id, subcategoria_id, unidade_medida_id)
VALUES (3, '1645154941545', 475.6, 40, 'Computador Com Monitor 18,5 Intel Dual Core 2.41GHZ 4GB HD 1TB 3GREEN Triumph Business Desktop', 350, 'CP0154', true, 'REVENDA', 1189, 1664.6, 1, 2, 1);
INSERT INTO produto(id, codigo_barra, lucro, marg_lucro, nome, quantidade, sku, status, tipo_produto, vlr_custo, vlr_venda, categoria_id, subcategoria_id, unidade_medida_id)
VALUES (4, '1645544941545', 214.75, 25, 'Computador Intel Dual Core com Monitor 15,6 LED 2GB 320GB HDMI 3green', 500, 'CP1504', true, 'REVENDA', 859, 1073.75, 1, 2, 1);
INSERT INTO produto(id, codigo_barra, lucro, marg_lucro, nome, quantidade, sku, status, tipo_produto, vlr_custo, vlr_venda, categoria_id, subcategoria_id, unidade_medida_id)
VALUES (5, '7899531819099', 134.7, 30, 'Cômoda Bartira Siena com 8 Gavetas', 500, 'CB1504', true, 'REVENDA', 314.3, 449, 4, 13, 1);
INSERT INTO produto(id, codigo_barra, lucro, marg_lucro, nome, quantidade, sku, status, tipo_produto, vlr_custo, vlr_venda, categoria_id, subcategoria_id, unidade_medida_id)
VALUES (6, '7899531817842', 56.85, 15, 'Guarda-Roupa Bartira Olímpia com 6 Portas e 3 Gavetas', 250, 'GD1500', true, 'REVENDA', 322.19, 379.05, 4, 13, 1);
INSERT INTO produto(id, codigo_barra, lucro, marg_lucro, nome, quantidade, sku, status, tipo_produto, vlr_custo, vlr_venda, categoria_id, subcategoria_id, unidade_medida_id)
VALUES (7, '7896659448972', 118.1, 25, 'Beliche Santos Andirá Havana Plus com Grade de Proteção', 312, 'BS004', true, 'REVENDA', 354.3, 472.41, 4, 13, 1);
INSERT INTO produto(id, codigo_barra, lucro, marg_lucro, nome, quantidade, sku, status, tipo_produto, vlr_custo, vlr_venda, categoria_id, subcategoria_id, unidade_medida_id)
VALUES (8, '4032344697253', 44.75, 25, 'Pneu Aro 13 Altimax General Tire RT 175/70 R13 82T by Continental', 50, 'PE0104', true, 'REVENDA', 134.25, 179, 5, 17, 1);
INSERT INTO produto(id, codigo_barra, lucro, marg_lucro, nome, quantidade, sku, status, tipo_produto, vlr_custo, vlr_venda, categoria_id, subcategoria_id, unidade_medida_id)
VALUES (9, '1000032263', 199, 25, 'Pneu General Tire Altimax RT 175/65 R14 4 Unidades by Continental', 100, 'CP1500', true, 'REVENDA', 597, 796, 5, 17, 1);
INSERT INTO produto(id, codigo_barra, lucro, marg_lucro, nome, quantidade, sku, status, tipo_produto, vlr_custo, vlr_venda, categoria_id, subcategoria_id, unidade_medida_id)
VALUES (10, '1000032253', 79.96, 20, 'Computador Intel Dual Core com Monitor 15,6 LED 2GB 320GB HDMI 3green', 500, 'CP2504', true, 'REVENDA', 319.84, 399.8, 5, 17, 1);


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









