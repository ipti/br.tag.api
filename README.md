# br.tag.api

* **Login**
  
  Realiza o login (pai):

  ```
  @POST
  ("username"), ("password")
  /tag/parent/login
  ```
  
  Realiza o login (outros usuários):

  ```
  @POST
  ("username"), ("password")
  /tag/users/login
  ```

* **Informação do usuário**

  Retorna as informações do `username`:

  ```
  @GET
  /tag/user/info/{username}
  ```

* **Estudante**

  Retorna os filhos do usuário logado (método para os pais dos alunos) com o `username`:

  ```
  @GET
  /tag/children/parent/{username}
  ```

  Retorna os filhos do usuário logado (método para os pais dos alunos) com o `username` (APP):

  ```
  @GET
  /tag/student/parent/{username}
  ```
  
  Retorna todos os estudantes:

  ```
  @GET
  /tag/students
  ```

  Retorna o estudante com o `inep_id`:

  ```
  @GET
  /tag/student/{inep_id}
  ```
  
  Retorna todos os estudantes da turma com o `classroom_id`:

  ```
  @GET
  /tag/student/classroom/{classroom_id}
  ```
  
  Retorna o estudante com o `name`:

  ```
  @GET
  /tag/student/name/{name}
  ```
  
  Retorna o estudante com o `id` filtrando pela turma com o `classroom_id`:

  ```
  @GET
  /tag/student/{classroom_id}/{id}
  ```

* **Professor**
  
  Retorna todos os professores:

  ```
  @GET
  /tag/instructors
  ```
  
  Retorna o professor com o `inep_id`:

  ```
  @GET
  /tag/instructor/inep/{inep_id}
  ```
  
  Retorna o professor com o `id`:

  ```
  @GET
  /tag/instructor/{id}
  ```

* **Turmas**
  
  Retorna todas as turmas:

  ```
  @GET
  /tag/classrooms
  ```
  
  Retorna a turma com o `inep_id`:

  ```
  @GET
  /tag/classroom/{inep_id}
  ```
  
  Retorna as turmas do professor com o `instructor_fk` do `year` passado no parâmetro:

  ```
  @GET
  /tag/classroom/instructor/{instructor_fk}/{year}
  ```
  
  Retorna as turmas da escola com o `school_inep_fk`:

  ```
  @GET
  /tag/classroom/school/{school_inep_fk}
  ```

* **Disciplinas**

  Retorna todas as disciplinas da turma com o `id`:

  ```
  @GET
  /tag/discipline/classroom/{id}
  ```
  
  Retorna todas as disciplinas do professor com o `id`:

  ```
  @GET
  /tag/discipline/instructor/{id}
  ```

* **Escola**

  Retorna todas as escolas:

  ```
  @GET
  /tag/schools
  ```

  Retorna a escola com o `inep_id`:

  ```
  @GET
  /tag/school/{inep_id}
  ```

  Retorna todas as escolas do usuário logado (professor ou secretário) com `user_fk`:

  ```
  @GET
  /tag/school/user/{user_fk}
  ```

* **Grade**

  Retorna todas as grades:

  ```
  @GET
  /tag/grades
  ```

  Retorna a grade do aluno com o `enrollment_fk`:

  ```
  @GET
  /tag/grade/{enrollment_fk}
  ```

* **Frequência**
  
  Retorna a frequência do aluno com o `student_fk`, filtrando pela `classroom_fk` e `month`:

  ```
  @GET
  /tag/frequency/{student_fk}/{classroom_fk}/{month}
  ```
  
## Desenvolvido com

* [Eclipse Neon](http://www.eclipse.org/neon/) - IDE para desenvolvimento
* [Maven](https://maven.apache.org/) - Gerenciador de dependência
* [Glassfish](https://glassfish.java.net/public/alldownloads.html) - Servidor de aplicação

## License

  Esse projeto está licenciado pela MIT License - veja em [LICENSE.md](LICENSE.md) para mais detalhes