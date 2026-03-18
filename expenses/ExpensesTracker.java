package expenses;

import java.sql.*;
import java.util.Scanner;

public class ExpensesTracker {
	/**
	 * Connect to the SQLite database specified by dbPath. If the database file does not exist, it will be created. If the transactions table does not exist, it will be created automatically.
	 * @param dbPath the path to the SQLite database file
	 * @return a Connection object to the SQLite database
	 * @throws SQLException if there is an error connecting to the database
	*/
	private Connection connect(String dbPath) throws SQLException {
		//create database file if it doesn't exist
		Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

		// Create the table automatically if it's missing
		String createTableSQL = "CREATE TABLE IF NOT EXISTS transactions (" +
								"id INTEGER PRIMARY KEY AUTOINCREMENT," +
								"date TEXT," +
								"item TEXT," +
								"amount REAL," +
								"type TEXT)"; // 'INCOME' or 'EXPENSE'
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(createTableSQL);
		}
		return conn;
	}

	/**
	 * Get the current date as a string in the format "yyyy-MM-dd"
	 * @return the current date as a string
	*/
	public String timeStamp() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd ");
		return sdf.format(new java.util.Date());
	}

	/**
	 * Prompt the user to enter the amount of income into the file
	 * @param fileName the name of the file to write to
	*/
	public void addIncome(String dbPath) {
		Scanner in = new Scanner(System.in);
		Double incomeAmount = null;

		// get valid amount
		while (true) {
			System.out.print("Enter amount: ");
			String lineAmount = in.nextLine().trim();
			if (lineAmount.isEmpty()) {
				System.out.println(Color.RED + "Amount cannot be empty. Please try again." + Color.RESET);
				continue;
			}
			try {
				incomeAmount = Double.parseDouble(lineAmount);
				if (incomeAmount < 0) {
					System.out.println(Color.RED + "Amount must be a non-negative number. Please try again." + Color.RESET);
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				System.out.println(Color.RED + "Please enter a valid number for amount." + Color.RESET);
			}
		}

		// insert into DB (prepareStatement to prevent SQL injection)
		String sql = "INSERT INTO transactions(date, item, amount, type) VALUES(?,?,?,?)";
		try (Connection conn = this.connect(dbPath);
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, timeStamp());
			pstmt.setString(2, "Income");
			pstmt.setDouble(3, incomeAmount);
			pstmt.setString(4, "INCOME");
			pstmt.executeUpdate();
			System.out.println(Color.GREEN + "Income added successfully." + Color.RESET);
		} catch (SQLException e) {
			System.out.println(Color.RED + "Database error: " + e.getMessage() + Color.RESET);
			e.printStackTrace();
		}
	}

	/**
	 * View all expenses and income from the file
	 * @param fileName the name of the file to read from
	*/
	public void viewExpenses(String dbPath) {
			String sql = "SELECT * FROM transactions";

			/**
			 * createStatement: used for executing simple SQL statements without parameters.
			 * 					It is not precompiled and does not support parameterized queries.
			 * 					It is suitable for executing static SQL statements that do not require user input.
			 * executeQuery: used for executing SQL statements that return a ResultSet, such as SELECT statements.
			 * 				It is used with Statement or PreparedStatement objects to execute queries and retrieve data from the database.
			*/
			try (Connection conn = this.connect(dbPath);
				 Statement stmt  = conn.createStatement();
				 ResultSet rs    = stmt.executeQuery(sql)) {

				double totalEarn = 0, totalSpend = 0;
				System.out.println("=================================================");
				System.out.printf("%-12s| %-20s| %s\n", "Date", "Item", "Amount");
				System.out.println("=================================================");

				while (rs.next()) {
					String date = rs.getString("date");
					String item = rs.getString("item");
					double amt = rs.getDouble("amount");
					String type = rs.getString("type");

					if (type.equals("INCOME")) {
						totalEarn += amt;
						System.out.printf(Color.GREEN + "%-12s  %-20s  RM+%.2f\n" + Color.RESET, date, item, amt);
					} else {
						totalSpend += amt;
						System.out.printf(Color.RED + "%-12s  %-20s  RM-%.2f\n" + Color.RESET, date, item, amt);
					}
				}
				System.out.println("=================================================");

				System.out.printf("%-34s  RM%.2f\n", "Total Income:", totalEarn);
				System.out.printf("%-34s  RM%.2f\n", "Total Expenses:", totalSpend);

				double balance = totalEarn - totalSpend;
				if (balance >= 0) {
					System.out.printf(Color.GREEN + "%-34s  RM%.2f\n" + Color.RESET, "Balance:", balance);
				} else {
					System.out.printf(Color.RED + "%-34s  RM%.2f\n" + Color.RESET, "Balance:", balance);
				}

			} catch (SQLException e) {
				System.out.println(Color.RED + "Read error: " + e.getMessage() + Color.RESET);
			}
	}

	/**
	 * Add an expense to the file, it also checks for duplicate item names
	 * @param fileName the name of the file to write to
	*/
	public void addExpenses(String dbPath) {
		Scanner in = new Scanner(System.in);
		String item;
		Double amount;

		// get valid item name and ensure no duplicate (case-insensitive)
		while (true) {
			System.out.print("Enter item name: ");
			item = in.nextLine().trim();
			if (item.isEmpty()) {
				System.out.println(Color.RED + "Item name cannot be empty. Please try again." + Color.RESET);
				continue;
			}
			if (!item.matches("[a-zA-Z0-9]+")) {
				System.out.println(Color.RED + "Item name can only contain letters and numbers. Please try again." + Color.RESET);
				continue;
			}

			String checkSql = "SELECT COUNT(*) FROM transactions WHERE LOWER(item) = LOWER(?) AND type = 'EXPENSE'";
			try (Connection conn = this.connect(dbPath);
				 PreparedStatement ps = conn.prepareStatement(checkSql)) {
				ps.setString(1, item);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						System.out.println(Color.RED + "Item already exists. Please enter a different item." + Color.RESET);
						continue;
					}
				}
			} catch (SQLException e) {
				System.out.println(Color.RED + "Database error: " + e.getMessage() + Color.RESET);
				return;
			}
			break;
		}

		// get valid amount
		while (true) {
			System.out.print("Enter amount: ");
			String lineAmount = in.nextLine().trim();
			if (lineAmount.isEmpty()) {
				System.out.println(Color.RED + "Amount cannot be empty. Please try again." + Color.RESET);
				continue;
			}
			try {
				amount = Double.parseDouble(lineAmount);
				if (amount < 0) {
					System.out.println(Color.RED + "Amount must be a non-negative number. Please try again." + Color.RESET);
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				System.out.println(Color.RED + "Please enter a valid number for amount." + Color.RESET);
			}
		}

		// insert into DB
		String sql = "INSERT INTO transactions(date, item, amount, type) VALUES(?,?,?,?)";
		try (Connection conn = this.connect(dbPath);
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, timeStamp());
			pstmt.setString(2, item);
			pstmt.setDouble(3, amount);
			pstmt.setString(4, "EXPENSE");
			pstmt.executeUpdate();
			System.out.println(Color.GREEN + "Expense added successfully." + Color.RESET);
		} catch (SQLException e) {
			System.out.println(Color.RED + "Database error: " + e.getMessage() + Color.RESET);
			e.printStackTrace();
		}
	}

	/**
	 * Remove an expense from the file
	 * @param fileName the name of the file to modify
	 * @detail prompt the user to enter the item name of the expense to remove
	*/
	public void removeExpenses(String dbPath) {
			Scanner in = new Scanner(System.in);
			System.out.print("Enter item name to remove: ");
			String itemToRemove = in.nextLine();

			String sql = "DELETE FROM transactions WHERE item = ?";

			try (Connection conn = this.connect(dbPath);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, itemToRemove);
				int deleted = pstmt.executeUpdate();
				if (deleted > 0) {
					System.out.println(Color.GREEN + "Item deleted." + Color.RESET);
				} else {
					System.out.println(Color.RED + "Item not found." + Color.RESET);
				}
			} catch (SQLException e) {
				System.out.println(Color.RED + "Delete error: " + e.getMessage() + Color.RESET);
			}
	}
}
