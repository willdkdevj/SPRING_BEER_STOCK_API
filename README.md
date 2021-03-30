# Implementando API REST para Gerenciamento de Estoque de Cerveja utilizando TDD
> Este projeto consiste em uma API (Application Programming Interface) de gerenciamento de estoque de cerveja, na qual foram implementado testes unitários para cada função através da metodologia TDD para comprovar sua eficiência. 

[![Spring Badge](https://img.shields.io/badge/-Spring-brightgreen?style=flat-square&logo=Spring&logoColor=white&link=https://spring.io/)](https://spring.io/)
[![Maven Badge](https://img.shields.io/badge/-MAVEN-000?style=flat-square&logo=MAVEN&logoColor=white&link=https://maven.apache.org/)](https://maven.apache.org/)
[![MySQL Badge](https://img.shields.io/badge/-MySQL-blue?style=flat-square&logo=MySQL&logoColor=white&link=https://www.mysql.com/)](https://www.mysql.com/)


<img align="right" width="400" height="300" src="https://matheuspcarvalhoblog.files.wordpress.com/2018/05/spring-framework.png">

## Descrição da Aplicação
A aplicação consiste em operações que permitem gerenciar um determinado estoque de cervejas, realizando funções como:
* Criar o cadastro de uma cerveja;
* Excluir o cadastro de uma cerveja;
* Alterar a quantidade em estoque ao incluir mais cervejas ao estoque;
* Alterar a quantidade em estoque ao subtrair cervejas ao estoque;
* Listar as cervejas em estoque.

A API REST (*Representational State Transfer*) tem a função de administrar os comandos, protocolos e objetos a fim de agir como ponte das informações obtidas de uma arquitetura computacional voltada a aplicações interligadas por rede, na qual depende de um protocolo de comunicação de transação independente e de uma estrutura ``cliente-servidor`` utilizando o protocolo HTTP. 

Desta forma, toda vez que uma requisição, oriunda de um navegador web, é encaminhada para uma URL, que encontrasse mapeada pela API a um controle, que orienta onde deve encontrar a resposta a ser retornada ao cliente. Ela (a requisição) irá interagir com o servidor na qual está hospedada a API. A REST tem esta atribuição de tratar objetos originados do servidor como recursos CRUD (Create, Read, Update, Delete) através do protocolo HTTP utilizando o formato JSON.

Resumindo, a API REST trabalha como mensageiro, levando informações de um ponto a outro utilizando os requerimentos HTTP para formatá-las.

No decorrer deste documento é apresentado com mais detalhes sua implementação, descrevendo como foi desenvolvida a estrutura da API, suas dependências e como foi implementado o TDD para a realização dos testes unitários dos metodos na camada de negócio. Como implementamos do Spring Boot, para agilizar a análise do código e configurá-lo conforme nossas necessidades por meio dos *starters* agrupando as dependências, e o Spring Data JPA, que nos dá diversas funcionalidades tornando simples a dinamica de operações com bancos de dados e sua manutenção.

## Importação do Projeto Maven para Execução da Aplicação
O Apache Maven é uma ferramenta de apoio a equipes que trabalham com projeto Java (mas não se restringe somente a Java), possibilitando a desenvolvedores a automatizar, gerenciar e padronizar a construção e publicação de suas aplicações, permitindo maior agilidade e qualidade ao produto.
Abaixo são apresentadas as etapas para importá-lo a IDE IntelliJ, mas também é possível trabalhar com outras IDE's como Eclipsse, NetBeans, entre outras, podendo ser diferente os procedimentos realizados.

1. No menu principal, acesse File >> New >> Project from Existing Sources;
2. Selecione o diretório onde está o projeto e clique "OK";
3. Selecione o arquivo pom.xml e clique em "Next";
4. Deixe as configurações padrões e clique em "Next";
5. Selecione a versão do seu JDK e clique em "Next" (Dê preferência a mesma apontada ao criar o projeto no Initializr);
6. Para finalizar, clique no botão "Finish".
7. Para o projeto e localize o arquivo ``BeerstockApplication`` em src/main/br.com.supernova.beerstock;
8. Clique com o botão direito do *mouse* e selecione Run >> Spring Boot App, ou simplesmente *Run*.

![](/src/images/beerStock.png?w=400) 

Se tudo der certo a aplicação Spring será executada pelo seguinte endereço [http://localhost:8080/api/v1/beers](http://localhost:8080/api/v1/beers).

## Como Foi Configurado o Projeto Spring no IDEA-IntelliJ
O projeto Spring foi criado a partir do [Spring Initializr](https://start.spring.io/), que é uma ferramenta de apoio para criar uma API com um endpoint (endereço para um recurso) a fim de retornar nossos dados, mas também é possível realizar a criação do projeto através de plugin, via a IDE Eclipse, denominada Spring Tools Suíte (STS), na qual fornece as mesmas funcionalidades para estruturar as configurações pré-moldadas ao projeto.

1. Acesse [https://start.spring.io/](https://start.spring.io/);
2. Configure no campo *Project* qual é o tipo de Gerenciador de Dependências deseja utilizar e em qual linguagem de programação consiste o projeto:
	* Neste projeto foi selecionado o gerenciador ``Maven`` e a linguagem de programação selecionada ``Java``;
3. Configure no campo *Spring Boot* a versão do framework:
	* Neste projeto foi utilizada a versão ``2.4.4``;
4. Configure no campo de *Project Metadate* com as seguintes parametrizações:
	* No parâmetro *Group*, informe o  endereço de domínio de trás para frente;
	* No parâmetro *Artifact*, informe o nome do projeto novamente;
	* No parâmetro *Name*, informe o nome do projeto;
	* No parâmetro *Description*, informe um breve resumo sobre o projeto (Opcional)
	* No parâmetro *Package Name*, informe o domínio de trás para frente mais o nome do projeto;
	* Em Packaging, foi utilizada a opção Jar;
	* Para Java Version, foi selecionado a versão do Java vigente da máquina, que está com a versão do JDK na 11, mas o projeto pode ser configurado com a versão 8, que já suporta o uso do expressão Stream em Collections.
5. Configure no campo *Dependencies* a seguinte relação:
	* DevTools;
	* JPA;
	* MySQL;
	* Web.

> NOTA: No Spring Initializr, após a configuração moldada será gerado um pacote Zip que deve ser importado pela IDE IDEA-IntelliJ (JetBrains).

6. Extraia os arquivos para seu diretório de preferência;
7. Abra o IntelliJ em Import Project;
8. Selecione o arquivo pom.xml e clique em "Next";
9. Deixe as configurações padrões e clique em "Next";
10. Selecione a versão do seu JDK e clique em "Next" (Dê preferência a mesma apontada ao criar o projeto no Initializr);
11. Para finalizar, clique no botão "Finish".

Desta forma o starters já implementa um projeto praticamente funcional, onde encontramos a classe principal do nosso projeto em ``src/main/java`` com o pacote que configuramos Initializr (ou STS) ``br.com.nomedomínio.nomeprojeto``.
```sh
package br.com.supernova.beerstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeerstockApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeerstockApplication.class, args);
	}
}
```
A anotação @SpringBootApplication informa que a classe pertence as configurações do Spring, além de definir o ponto de partida para a procura de mais componentes relacionados a aplicação, desta forma, todas as classes devem seguir a partir deste pacote para serem mapeados pelo Spring.

### O Maven
O Apache Maven é uma ferramenta que auxilia a equipes a trabalharem com projetos de desenvolvimento de software, possibilitando automatizar e padronizar a construção e publicação de aplicações. Ela é uma ferramenta de gerenciamento e automação de construção de projetos na qual estimula a adoção de boas práticas por utilizar o conceito de programação orientada a convenção. Isto permite uma melhor estruturação dos diretórios que constituí o projeto, desta forma, todos os integrantes do projeto possuíram a mesma estrutura padronizada, incluindo dependências, plugins e anotações.

Abaixo segue tabela que apresenta todas as dependẽncias e plugins utilizados neste projeto:

|	Grupo		 |	Artefato				|	Versão		|
|----------------|--------------------------|---------------|
| Spring Boot 	 | Actuator					| 2.4.4 		|
| Spring Boot 	 | Data JPA					| 2.4.4 		|
| Spring Boot 	 | Validation				| 2.4.4 		|
| Spring Boot 	 | Web						| 2.4.4 		|
| Spring Boot 	 | DevTools					| 2.4.4 		|
| H2Database  	 | H2						| 1.4.200 		|
| Project Lombok | Lombok					| 1.18.18 		|
| MapStruct		 | MapStruct				| 1.4.1.Final 	|
| Spring Fox	 | Swagger					| 2.9.2 		|
| JUNIT			 | Jupiter					| 5.7.1 		|
| JUNIT			 | Mockito					| 3.6.28 		|
| JUNIT			 | Hamcrest					| 2.2 			|
| MySQL			 | MySQL					| 8.0.23 		|
| Hibernate		 | Hibernate Core 			| 5.4.30.Final 	|
| Hibernate		 | Hibernate EntityManager  | 5.4.30.Final 	|

Além de gerenciar dependências, o Maven permite acompanharmos o ciclo de vida do projeto até a concepção do software concluído. Isto inclui as seguintes funções:
* Facilitar a compilação do código, o empacotamento ([JAR], [WAR], [EAR]), a execução de testes unitários, etc;
* Unificar e automatizar o processo de geração do sistema;
* Centralizar informações organizadas do projeto, incluindo suas dependências, resultados de testes, documentação, etc;
* Reforçar boas práticas de desenvolvimento, tais como: separação de classes de teste das classes do sistema, uso de convenções de nomes e diretórios, etc;
* Ajudar no controle das versões geradas (*releases*) dos seus projetos.

### Spring FOX - Swagger
O Spring Fox é o framework que possibilita a importação do Swagger como dependência ao projeto e a integrá-la ao framework do Spring, desta forma, permitindo utilizar suas anotações para uso de suas funcionalidades para o contexto de documentação do projeto.

Para isso, se faz necessário criar uma classe de configuração com o nome SwaggerConfig (o nome padrão do framework), contendo a anotação @Configuration em conjunto com o anotação @EnableSwagger2 com o seguinte conteúdo.
```sh
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(constructorApiInfo());
	}
	
	private Predicate<RequestHandler> apis(){
		return RequestHandlerSelectors.basePackage("br.com.supernova.beerstock");
	}

	public ApiInfo constructorApiInfo() {
		return new ApiInfoBuilder()
				.title(API_TITLE)
				.description(API_DESCRIPTION)
				.version("1.0.0")
				.contact(new Contact(CONTACT_NAME, CONTACT_GITHUB, CONTACT_EMAIL))
				.build();		
	}
}
```

O método ``api`` é o responsável por estruturar o documento através das anotações que são incluídas nas classes que possibilita inserir uma nota de explicação sobre o parâmetro ou função. Ele também é um Bean para o Spring desta forma encontrasse anotado com [@Bean]. Já os dois restantes, são complementares ao método principal, na qual o método ``apis`` restringe a qual projeto deve ser coletadas as anotações e o método ``constructorApiInfo`` permite que parametrizamos com informações de identificação do projeto.

Esta classe (SwaggerConfig) precisa ficar na pasta raiz do projeto, na qual podesse criar uma subpasta denominada como ``config/`` para hospedá-la no projeto Spring.

> NOTA: Para visualizar a documentação acesse o seguinte link após subir a aplicação: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


### Estrutura do Projeto Spring
O projeto segue uma estrutura pré moldada pelo Maven quando parametrizamos o Spring Boot pelo Initializr (ou pelo STS), 



### O Uso do MySQL
Ao utilizar o JPA (através do Spring Data) podemos utilizar para configurar e até mesmo trocar o banco de dados, no ínicio do projeto foi incluso o H2, que é um banco que utiliza a memória para instanciar dados, mas foi utilizado somente para testes. Para a aplicação foi selecionado o SGBD MySQL, mas poderia ser qualquer outro como o PostgreSQL, para isso, foi implementado o arquivo pom.xml com a dependẽncia do driver JDBC abaixo:
```sh
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<scope>runtime</scope>
</dependency>
```

Para concluir, foi implementado o arquivo application.properties informado a URL de conexão do JDBC, usuário e senha, além de parametrizar o hibernate a criar e excluir o banco após reiniciar a aplicação. O caminho do arquivo encontra-se em ``src/main/resources/``: 
```sh
spring.datasource.url=jdbc:mysql://localhost/_nome_bancodados
spring.datasource.username=root
spring.datasource.password=_senha_banco
spring.jpa.hibernate.ddl-auto=create-drop
```

Utilizamos o banco ``beerstock`` criado no MySQL, sem tabelas ou qualquer outro objeto do banco. Quanto ao usuário, foi utilizado o root e a senha que foi atribuído a ele. Além disso, foi configurado a propriedade ``ddl-auto``, para recriar o banco de dados todas as vezes que o projeto se iniciar. 
> NOTA: Esta configuração não pode ser implementada em Ambiente de Produção


## Agradecimentos

Obrigado por ter acompanhado aos meus esforços para desenvolver este Projeto utilizando o Maven e a estrutura do Spring para criação de uma API REST! :octocat:

Como diria um antigo mestre:
> *"Cedo ou tarde, você vai aprender, assim como eu aprendi, que existe uma diferença entre CONHECER o caminho e TRILHAR o caminho."*
>
> *Morpheus - The Matrix*