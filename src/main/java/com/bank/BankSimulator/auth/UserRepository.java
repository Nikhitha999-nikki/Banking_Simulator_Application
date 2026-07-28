package com.bank.BankSimulator.auth;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

import com.bank.BankSimulator.repository.DBConnection;

public class UserRepository {

    // Register User
    public static boolean register(String username,
                               String email,
                               String password) {

    String sql =
        "INSERT INTO users(name,email,password) VALUES(?,?,?)";

    try (
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
    ) {

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        ps.setString(1, username);
        ps.setString(2, email);
        ps.setString(3, hashedPassword);

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        }

        return false;
    }
    public static boolean updatePassword(String email, String password) {

        String sql = "UPDATE users SET password=? WHERE email=?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            ps.setString(1, hashedPassword);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // Check Email Exists
    public static boolean emailExists(String email) {

        String sql = "SELECT * FROM users WHERE email=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public static boolean validate(String identifier, String password) {

        String sql = "SELECT password FROM users WHERE name=? OR email=?";

            try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
            ) {

                ps.setString(1, identifier);
                ps.setString(2, identifier);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    String hashedPassword = rs.getString("password");

                    return BCrypt.checkpw(password, hashedPassword);

                }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
    }