package com.print.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.print.model.User;

public class UserDAO {
	Connection con = null;
	PreparedStatement ps;

	public int registerUser(User u) {

		int i = 0;
		Timestamp date = new Timestamp(new Date().getTime());
		con = DBConnection.getConnection();
		try {
			ps = con.prepareStatement(
					"insert into user(email,password,full_name,phone,degree,branch,registration_timestamp,verified) values(?,?,?,?,?,?,?,?)");
			ps.setString(1, u.getEmail());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getName());
			ps.setString(4, u.getPhone());
			ps.setString(5, u.getDegree());
			ps.setString(6, u.getBranch());
			ps.setTimestamp(7, date);
			ps.setString(8, "No");
			i = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

	public int updateVerifiedStatus(String email) {
		int i = 0;
		con = DBConnection.getConnection();
		try {
			ps = con.prepareStatement("update user set verified=? where email=?");
			ps.setString(1, "Yes");
			ps.setString(2, email);

			i = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

	public String sendotp(String name, String email) {
		com.print.email.SendMail sm = new com.print.email.SendMail();

		String otp = sm.generateOTP();

		final String subject = "Verify Your Account | ePrint Software";
		final String messg = "Dear " + name + ",\n\n"
				+ "Thank you for choosing ePrint Software! To ensure the security of your account, we require a verification process to activate your ePrint account. Please follow the instructions below to verify your account and complete the registration process.\r\n"
				+ "\r\n" + "Step 1: Retrieve the OTP (One-Time Password)\r\n"
				+ "This email contais a six-digit OTP (One-Time Password). Please keep this email secure and do not share the OTP with anyone.\r\n"
				+ "\r\n" + "Step 2: Account Verification \nEnter the six-digit OTP you received in the email.\r\n"
				+ "Click on the \"Verify\" or \"Submit\" button to complete the verification process.\r\n"
				+ "Please note that the OTP is valid for a limited time. If you do not enter the OTP within [time frame], you will need to request a new OTP.\r\n"
				+ "\r\n" + "OTP- " + otp
				+ "\n\nIf you did not request this verification, please disregard this email. However, we highly recommend contacting our support team immediately for further assistance.\r\n"
				+ "\r\n"
				+ "If you have any questions or encounter any issues during the verification process, please don't hesitate to reach out to our customer support team at [support email/phone number].\r\n"
				+ "\r\n"
				+ "Thank you for choosing ePrint Software. We look forward to providing you with a seamless and secure printing experience!\r\n"
				+ "\r\n" + "Best regards,\r\n" + "\r\n" + "ePrint Software Team\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "\r\n" + "\r\n" + "";
		sm.sendEmail(email, messg, subject);
		return otp;
	}

	public User loginUser(String email, String password) {

		con = DBConnection.getConnection();
		ResultSet rs = null;
		User u = null;
		try {
			ps = con.prepareStatement("select * from user where email=? and password=?");
			ps.setString(1, email);
			ps.setString(2, password);
			rs = ps.executeQuery();
			while (rs.next()) {
				u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(7),
						rs.getString(8), rs.getString(5), rs.getString(9), rs.getString(10), rs.getString(11),
						rs.getString(6));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}

	public int uploadDocuments(int userId, String setName, String token, String imageFileName, int pageCount) {
		int i = 0;
		Timestamp date = new Timestamp(new Date().getTime());
		con = DBConnection.getConnection();
		int cost = pageCount * 2;
		try {
			ps = con.prepareStatement(
					"insert into document(user_id,set_name,token_no,doc_name,no_of_pages,cost) values(?,?,?,?,?,?)");
			ps.setInt(1, userId);
			ps.setString(2, setName);
			ps.setString(3, token);
			ps.setString(4, imageFileName);
			ps.setInt(5, pageCount);
			ps.setInt(6, cost);
			i = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;

	}

	public ResultSet findDocumentByUserId(int id) {
		ResultSet rs = null;
		con = DBConnection.getConnection();
		try {
			ps = con.prepareStatement("select * from document where user_id=?");
			ps.setInt(1, id);

			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public int getCountBySetName(String setName) {

		int i = 0, cnt = 0;
		ResultSet rs = null;
		con = DBConnection.getConnection();
		try {
			ps = con.prepareStatement("select count(*) from document where set_name=?");
			ps.setString(1, setName);

			rs = ps.executeQuery();

			while (rs.next()) {
				cnt = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cnt;

	}

	public int getWalletBalanceByUserId(int id) {

		int i = 0, bal = 0;
		ResultSet rs = null;
		con = DBConnection.getConnection();
		try {
			ps = con.prepareStatement("select wallet_balance from user where user_id=?");
			ps.setInt(1, id);

			rs = ps.executeQuery();

			while (rs.next()) {
				bal = rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bal;
	}

	public int getWalletBalanceByPhone(String phone) {

		int i = 0, bal = 0;
		ResultSet rs = null;
		con = DBConnection.getConnection();
		try {
			ps = con.prepareStatement("select wallet_balance from user where phone=?");
			ps.setString(1, phone);

			rs = ps.executeQuery();

			while (rs.next()) {
				bal = rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bal;
	}

	public void setWalletBalanceByUserId(int id, int nbal) {
		int i = 0, bal = 0;
		ResultSet rs = null;
		con = DBConnection.getConnection();
		try {
			System.out.println("inside set wallet bal");
			ps = con.prepareStatement("update user set wallet_balance=? where user_id=?");
			ps.setInt(1, nbal);
			ps.setInt(2, id);
			i = ps.executeUpdate();
			System.out.println("after set wallet bal");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addUserTransectionInfo(int id, int cost, String doc, String date) {
		int i = 0;
		// Timestamp date = new Timestamp(new Date().getTime());
		con = DBConnection.getConnection();
		try {
			ps = con.prepareStatement("insert into usertransection(user_id,cost,doc_name,date,type) values(?,?,?,?,?)");
			ps.setInt(1, id);
			ps.setInt(2, cost);
			ps.setString(3, doc);
			ps.setString(4, date);
			ps.setString(5, "deducted");

			i = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ResultSet getUserTransectionInfoByUserId(int id) {

		int i = 0, bal = 0;
		ResultSet rs = null;
		con = DBConnection.getConnection();
		try {

			ps = con.prepareStatement("select * from usertransection");
			rs = ps.executeQuery();
			System.out.println("after set wallet bal");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

}
