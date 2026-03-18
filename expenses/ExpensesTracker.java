package Expenses_Tracker.expenses;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExpensesTracker {
	public String timeStamp() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd ");
		return sdf.format(new java.util.Date());
	}

	/**
	 * Prompt the user to enter the amount of income into the file
	 * @param fileName the name of the file to write to
	*/
	//TODO do exception
	public void addIncome(String fileName) {
		try {
			File fileRead = new File(fileName);
			FileWriter fileWrite = new FileWriter(fileName, true);
			Scanner in = new Scanner(System.in);
			String item = "Income";
			Double incomeAmount;

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
					System.out.println(Color.GREEN + "Income added successfully." + Color.RESET);
					break;
				} catch (NumberFormatException e) {
					System.out.println(Color.RED + "Please enter a valid number for amount." + Color.RESET);
				}
			}

			fileWrite.write(timeStamp() + "\t" + item + "\t" + incomeAmount + "\n");
			fileWrite.close();
		} catch (IOException e) {
			System.out.println(Color.RED + "An error occurred: " + e.getMessage() + Color.RESET);
			e.printStackTrace();
		}
	}

	/**
	 * View all expenses and income from the file
	 * @param fileName the name of the file to read from
	 * @detail read the file and display the date, item, and amount of each entry, as well as the total income, total expenses, and remaining balance
	*/
	public void viewExpenses(String fileName) {
		File myFile = new File(fileName);
		try (Scanner sc = new Scanner(myFile)) {
			sc.nextLine(); // Skip header line
			System.out.println("=================================================");
			System.out.printf("%-12s| %-20s| %s\n", "Date", "Item", "Amount");
			System.out.println("=================================================");
			double totalSpend = 0;
			double totalEarn = 0;
			while (sc.hasNextLine()) {
				String data = sc.nextLine();
				String[] parts = data.split("\\t");
				String date = parts[0];
				String item = parts[1];
				String amount = parts[2];
				try {
					double amt = Double.parseDouble(amount);
					if (item.equalsIgnoreCase("Income")) {
						totalEarn += amt;
						System.out.printf(Color.GREEN + "%-12s  %-20s  RM+%.2f\n" + Color.RESET, date, item, amt);
					} else {
						totalSpend += amt;
						System.out.printf(Color.RED + "%-12s  %-20s  RM-%.2f\n" + Color.RESET, date, item, amt);
					}
				} catch (NumberFormatException e) {
					System.out.printf("%-12s  %-20s  %s\n", date, item, amount);
				}
			}
			System.out.println("=================================================");
			double left = totalEarn - totalSpend;
			System.out.printf("%-34s  RM%.2f\n", "Total Earn:", totalEarn);
			System.out.printf("%-34s  RM%.2f\n", "Total Spend:", totalSpend);
			if (left >= 0) {
				System.out.printf(Color.GREEN + "%-34s  RM%.2f\n" + Color.RESET, "left:", left);
			} else {
				System.out.printf(Color.RED + "%-34s  RM%.2f\n" + Color.RESET, "left:", left);
			}
			System.out.println("=================================================");
		} catch (Exception e) {
			System.out.println(Color.RED + "An error occurred: " + e.getMessage() + Color.RESET);
			e.printStackTrace();
		}
	}

	/**
	 * Add an expense to the file
	 * @param fileName the name of the file to write to
	 * @detail prompt the user to enter the item name and amount of the expense, it also check the duplicate item name in the file.
	*/
	public void addExpenses(String fileName) {
		try {
			File fileRead = new File(fileName);
			FileWriter fileWrite = new FileWriter(fileName, true);
			Scanner in = new Scanner(System.in);

			String item;
			Double amount;

			while (true) {
				System.out.print("Enter item name: ");
				item = in.nextLine();
				if (item.isEmpty()) {
					System.out.println(Color.RED + "Item name cannot be empty. Please try again." + Color.RESET);
					continue;
				}
				if (!item.matches("[a-zA-Z0-9]+")) {
					System.out.println(Color.RED + "Item name can only contain letters and numbers. Please try again." + Color.RESET);
					continue;
				}

				//check if item already exists
				boolean exists = false;
				try (Scanner fileScan = new Scanner(fileRead)) {
					while (fileScan.hasNextLine()) {
						String data = fileScan.nextLine();
						String[] parts = data.split("\\t");
						if (parts[0].equalsIgnoreCase(item)) {
							exists = true;
							break;
						}
					}
				} catch (IOException e) {}
				if (exists) {
					System.out.println(Color.RED + "Item already exists. Please enter a different item." + Color.RESET);
					continue;
				}
				break;
			}

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

			fileWrite.write(timeStamp() + "\t" + item + "\t" + amount + "\n");
			fileWrite.close();
			System.out.println(Color.GREEN + "Expense added successfully." + Color.RESET);
		} catch (IOException e) {
			System.out.println(Color.RED + "An error occurred: " + e.getMessage() + Color.RESET);
			e.printStackTrace();
		}
	}

	/**
	 * Remove an expense from the file
	 * @param fileName the name of the file to modify
	 * @detail prompt the user to enter the item name of the expense to remove
	*/
	public void removeExpenses(String fileName) {
		File inputFile = new File(fileName);
		File tempFile = new File(fileName + ".tmp");
		boolean removed = false;

		Scanner in = new Scanner(System.in);
		System.out.print("Enter item name to remove: ");
		String itemToRemove = in.nextLine();

		try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(inputFile));
				java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(tempFile))) {

			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				String[] parts = currentLine.split("\\t");
				String item = parts[0];
				if (item.equalsIgnoreCase(itemToRemove)) {
					removed = true;
					continue; // Skip writing this line to the temp file
				}
				writer.write(currentLine);
				writer.newLine();
			}
		} catch (IOException e) {
			System.out.println(Color.RED + "An error occurred: " + e.getMessage() + Color.RESET);
			e.printStackTrace();
			return;
		}

		try {
			java.nio.file.Files.move(
				tempFile.toPath(),
				inputFile.toPath(),
				java.nio.file.StandardCopyOption.REPLACE_EXISTING
			);
			if (removed) {
				System.out.println(Color.GREEN + "Removed expense: " + itemToRemove + Color.RESET);
			} else {
				System.out.println(Color.RED + "No matching item found for: " + itemToRemove + Color.RESET);
			}
		} catch (IOException e) {
			System.out.println(Color.RED + "Failed to replace original file: " + e.getMessage() + Color.RESET);
			e.printStackTrace();
		}

	}
}
