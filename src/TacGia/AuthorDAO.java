package TacGia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Sach.BookDAO;
import util.DBConnect; // Import đúng DBConnect từ package util

public class AuthorDAO {

    // Lấy danh sách tất cả tác giả
    public List<Author> getAllAuthors() {
        List<Author> list = new ArrayList<>();
        String sql = "SELECT * FROM author";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Author author = new Author(
                    rs.getInt("author_id"),
                    rs.getString("name"),
                    rs.getString("bio")
                );
                list.add(author);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm tác giả mới
    public boolean insertAuthor(Author author) {
        String sql = "INSERT INTO author (name, bio) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, author.getName());
            stmt.setString(2, author.getBio());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin tác giả
    public boolean updateAuthor(Author author) {
        String sql = "UPDATE author SET name=?, bio=? WHERE author_id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, author.getName());
            stmt.setString(2, author.getBio());
            stmt.setInt(3, author.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa tác giả
    public boolean deleteAuthor(int authorId) {
        // Trước khi xoá tác giả, cập nhật author_id của sách liên quan thành NULL
        BookDAO bookDAO = new BookDAO();
        bookDAO.setAuthorIdToNullByAuthorId(authorId);

        String sql = "DELETE FROM author WHERE author_id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, authorId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
