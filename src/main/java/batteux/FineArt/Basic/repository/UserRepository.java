package batteux.FineArt.Basic.repository;

import batteux.FineArt.Basic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createUser(User user) {
        String sql = "INSERT INTO Users (username, login_name, email, password, is_active, phone_number, gender, birthdate) " + 
                     "VALUES (?, ?, ?, ?, 1, ?, ?, ?)";
        jdbcTemplate.update(sql, 
            user.getUsername(), 
            user.getLoginName(), 
            user.getEmail(), 
            user.getPassword(),
            user.getPhoneNumber(), 
            user.getGender(), 
            user.getBirthdate());
    }

    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapUser(rs), username, password);
        } catch (EmptyResultDataAccessException e) {
            return null;  // 결과가 없으면 null 반환
        }
    }
    
    public User findUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapUser(rs), userId);
    }

    public void updateUser(User user) {
        String sql = "UPDATE Users SET username = ?, login_name = ?, email = ?, phone_number = ?, gender = ?, birthdate = ?, password = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, 
            user.getUsername(), 
            user.getLoginName(), 
            user.getEmail(),
            user.getPhoneNumber(),
            user.getGender(),
            user.getBirthdate(),
            user.getPassword(),
            user.getUserId());
    }

    public void updatePassword(int userId, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, newPassword, userId);
    }
    
    public User findUserByEmailOrLoginName(String emailOrLoginName) {
        String sql = "SELECT * FROM Users WHERE email = ? OR login_name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapUser(rs), emailOrLoginName, emailOrLoginName);
        } catch (EmptyResultDataAccessException e) {
            return null;  
        }
    }

    // ResultSet을 User 객체로 변환
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setLoginName(rs.getString("login_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setActive(rs.getBoolean("is_active"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setGender(rs.getString("gender"));
        user.setBirthdate(rs.getDate("birthdate"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setLastActivity(rs.getTimestamp("last_activity"));
        return user;
    }
}
