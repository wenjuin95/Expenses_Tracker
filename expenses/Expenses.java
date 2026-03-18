package expenses;

import java.util.Scanner;

public class Expenses {

	public static void main(String[] args) {
		String fileName = "expenses.db";
		ExpensesTracker expenseTracker = new ExpensesTracker();
		Scanner in = new Scanner(System.in);

		while (true) {
			System.out.println(Color.PURPLE + "=========== Expenses Tracker Menu ===========" + Color.RESET);
			System.out.print(Color.CYAN + "0 | add income\n1 | add expenses\n2 | view expenses\n3 | delete expenses\n4 | exit\nEnter your choice: " + Color.RESET);
			String choiceStr = in.nextLine();
			if (choiceStr.isEmpty()) {
				System.out.println(Color.RED + "Choice cannot be empty. Please try again." + Color.RESET);
				continue;
			}
			if (!choiceStr.matches("[0-9]")) {
				System.out.println(Color.RED + "Please enter a valid number." + Color.RESET);
				continue;
			}

			int choice = Integer.parseInt(choiceStr);
			switch (choice) {
				case 0:
					expenseTracker.addIncome(fileName);
					break;
				case 1:
					expenseTracker.addExpenses(fileName);
					break;
				case 2:
					expenseTracker.viewExpenses(fileName);
					break;
				case 3:
					expenseTracker.removeExpenses(fileName);
					break;
				case 4:
					System.out.println(Color.GREEN + "Exiting the program." + Color.RESET);
					in.close();
					return;
				default:
					System.out.println(Color.RED + "Invalid choice" + Color.RESET);
					break;
			}
		}
	}
}
