package expenses;

import java.util.Scanner;

public class Expenses {

	public static void main(String[] args) {
		String fileName = "expenses.db";
		ExpensesTracker expenseTracker = new ExpensesTracker();
		Scanner in = new Scanner(System.in);
		String header =
		  "  ______                                       _______             _             \n"
		+ " |  ____|                                     |__   __|           | |            \n"
		+ " | |__  __  ___ __   ___ _ __  ___  ___  ___     | |_ __ __ _  ___| | _____ _ __ \n"
		+ " |  __| \\ \\/ / '_ \\ / _ \\ '_ \\/ __|/ _ \\/ __|    | | '__/ _` |/ __| |/ / _ \\ '__|\n"
		+ " | |____ >  <| |_) |  __/ | | \\__ \\  __/\\__ \\    | | | | (_| | (__|   <  __/ |   \n"
		+ " |______/_/\\_\\ .__/ \\___|_| |_|___/\\___||___/    |_|_|  \\__,_|\\___|_|\\_\\___|_|   \n"
		+ "            | |                                                                 \n"
		+ "            |_|                                                                 \n";
		System.out.println(Color.PURPLE + header + Color.RESET);
		while (true) {
			System.out.println(Color.PURPLE + "\n============= Expenses Tracker Menu =============" + Color.RESET);
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
