<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="br.com.ensino.model.Curso" %>
<html>
<head>
    <title>Lista de Cursos</title>
</head>
<body>
    <h2>Consulta de Cursos</h2>
    
    <form action="cursos" method="get">
        Filtrar por Modalidade:
        <select name="modalidade">
            <option value="">Todos</option>
            <option value="Presencial">Presencial</option>
            <option value="EAD">EAD</option>
            <option value="Híbrido">Híbrido</option>
        </select>
        <button type="submit">Buscar</button>
    </form>
    
    <br/>
    <a href="cadastro.html">Cadastrar Novo Curso</a> | 
    <a href="cursos?acao=exportarXml">Exportar em XML</a>
    <br/><br/>
    
    <table border="1" cellpadding="5" cellspacing="0">
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Modalidade</th>
            <th>Carga Horária</th>
            <th>Valor</th>
            <th>Ações</th>
        </tr>
        <% 
            List<Curso> cursos = (List<Curso>) request.getAttribute("cursos");
            if (cursos != null) {
                for (Curso c : cursos) {
        %>
        <tr>
            <td><%= c.getId() %></td>
            <td><%= c.getNome() %></td>
            <td><%= c.getModalidade() %></td>
            <td><%= c.getCargaHoraria() %></td>
            <td><%= c.getValor() %></td>
            <td>
                <a href="cursos?acao=excluir&id=<%= c.getId() %>" onclick="return confirm('Deseja realmente excluir?');">Excluir</a>
            </td>
        </tr>
        <% 
                }
            } 
        %>
    </table>
</body>
</html>
