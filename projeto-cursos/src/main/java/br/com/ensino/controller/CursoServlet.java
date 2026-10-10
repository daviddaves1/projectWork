package br.com.ensino.controller;

import br.com.ensino.dao.CursoDAO;
import br.com.ensino.model.Curso;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cursos")
public class CursoServlet extends HttpServlet {

    private CursoDAO dao = new CursoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");
        String modalidade = request.getParameter("modalidade");

        try {
            if ("excluir".equals(acao)) {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    dao.excluir(Integer.parseInt(idStr));
                }
                response.sendRedirect("cursos");
            } else if ("exportarXml".equals(acao)) {
                exportarXML(response);
            } else {
                List<Curso> cursos = dao.listar(modalidade);
                request.setAttribute("cursos", cursos);
                request.getRequestDispatcher("/lista.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String nome = request.getParameter("nome");
        String modalidade = request.getParameter("modalidade");
        String cargaHorariaStr = request.getParameter("cargaHoraria");
        String valorStr = request.getParameter("valor");

        PrintWriter out = response.getWriter();

        try {
            if (nome == null || nome.trim().isEmpty()) {
                out.println("O nome do curso não pode ser vazio.");
                return;
            }
            if (modalidade == null || (!modalidade.equals("Presencial") && !modalidade.equals("EAD") && !modalidade.equals("Híbrido"))) {
                out.println("As modalidades permitidas são Presencial, EAD ou Híbrido.");
                return;
            }

            int cargaHoraria = 0;
            try {
                cargaHoraria = Integer.parseInt(cargaHorariaStr);
            } catch (NumberFormatException e) {
                out.println("A carga horária deve ser um número válido.");
                return;
            }

            if (cargaHoraria <= 0) {
                out.println("A carga horária deve ser maior que zero.");
                return;
            }

            BigDecimal valor;
            try {
                valor = new BigDecimal(valorStr);
            } catch (Exception e) {
                out.println("O valor deve ser um número válido.");
                return;
            }

            if (valor.compareTo(BigDecimal.ZERO) < 0) {
                out.println("O valor do curso precisa ser um número positivo maior que zero.");
                return;
            }

            Curso curso = new Curso(nome, modalidade, cargaHoraria, valor);
            dao.cadastrar(curso);

            out.println("Curso " + nome + " cadastrado com sucesso!");

        } catch (SQLException e) {
            out.println("Erro ao salvar no banco de dados: " + e.getMessage());
        }
    }

    private void exportarXML(HttpServletResponse response) throws SQLException, IOException {
        List<Curso> cursos = dao.listar(null);
        response.setContentType("text/xml;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"cursos.xml\"");
        PrintWriter out = response.getWriter();
        
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<cursos>");
        for (Curso c : cursos) {
            out.println("  <curso>");
            out.println("    <id>" + c.getId() + "</id>");
            out.println("    <nome>" + escapeXml(c.getNome()) + "</nome>");
            out.println("    <modalidade>" + c.getModalidade() + "</modalidade>");
            out.println("    <cargaHoraria>" + c.getCargaHoraria() + "</cargaHoraria>");
            out.println("    <valor>" + c.getValor() + "</valor>");
            out.println("  </curso>");
        }
        out.println("</cursos>");
    }

    private String escapeXml(String s) {
        return s.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;");
    }
}

