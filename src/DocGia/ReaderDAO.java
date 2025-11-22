package DocGia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import MuonTra.BorrowingDAO;
import util.DBConnect;

public class ReaderDAO {

    // Lấy danh sách tất cả độc giả
    public List<Reader> getAllReaders() {
        List<Reader> list = new ArrayList<>();
        String sql = "SELECT * FROM reader";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reader reader = new Reader(
                        rs.getInt("reader_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                list.add(reader);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    // Thêm mới một độc giả

    public boolean insertReader(Reader r) {
        String sql = "INSERT INTO reader(name, address, phone, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, r.getName());
            stmt.setString(2, r.getAddress());
            stmt.setString(3, r.getPhone());
            stmt.setString(4, r.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

// Cập nhật thông tin độc giả
    public boolean updateReader(Reader r) {
        String sql = "UPDATE reader SET name=?, address=?, phone=?, email=? WHERE reader_id=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, r.getName());
            stmt.setString(2, r.getAddress());
            stmt.setString(3, r.getPhone());
            stmt.setString(4, r.getEmail());
            stmt.setInt(5, r.getId());
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

// Xóa độc giả theo ID
    public boolean deleteReader(int id) {
        // Trước khi xoá độc giả, cần xoá các phiếu mượn liên quan
        BorrowingDAO borrowingDAO = new BorrowingDAO();
        borrowingDAO.deleteBorrowingsByReaderId(id);

        String sql = "DELETE FROM reader WHERE reader_id=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
