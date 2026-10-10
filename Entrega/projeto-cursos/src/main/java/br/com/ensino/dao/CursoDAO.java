package br.com.ensino.dao;

import br.com.ensino.model.Curso;
import br.com.ensino.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    public void cadastrar(Curso curso) throws SQLException {
        String sql = "INSERT INTO curso (nome, modalidade, cargaHoraria, valor) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getModalidade());
            stmt.setInt(3, curso.getCargaHoraria());
            stmt.setBigDecimal(4, curso.getValor());
            stmt.executeUpdate();
        }
    }

    public List<Curso> listar(String modalidade) throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM curso";
        if (modalidade != null && !modalidade.trim().isEmpty()) {
            sql += " WHERE modalidade = ?";
        }
        sql += " ORDER BY nome";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (modalidade != null && !modalidade.trim().isEmpty()) {
                stmt.setString(1, modalidade);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Curso c = new Curso();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                    c.setModalidade(rs.getString("modalidade"));
                    c.setCargaHoraria(rs.getInt("cargaHoraria"));
                    c.setValor(rs.getBigDecimal("valor"));
                    cursos.add(c);
                }
            }
        }
        return cursos;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM curso WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
