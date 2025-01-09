import java.util.Scanner;

public class ATM {
    private User currentUser;
    private Bank bank;
    private Scanner scanner;

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the ATM!");
        authenticateUser();

        boolean quit = false;
        while (!quit) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    displayTransactionHistory();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    quit = true;
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void authenticateUser() {
        while (currentUser == null) {
            System.out.print("Enter user ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            currentUser = bank.authenticateUser(userId, pin);
            if (currentUser == null) {
                System.out.println("Invalid user ID or PIN. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\nATM Menu:");
        System.out.println("1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
        System.out.print("Choose an option: ");
    }

    private void displayTransactionHistory() {
        System.out.println("\nTransaction History:");
        for (Transaction transaction : currentUser.getTransactionHistory()) {
            System.out.println(transaction);
        }
    }

    private void withdraw() {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (amount > currentUser.getBalance()) {
            System.out.println("Insufficient balance.");
        } else {
            currentUser.setBalance(currentUser.getBalance() - amount);
            currentUser.addTransaction(new Transaction("Withdrawal", amount));
            System.out.println("Withdrawal successful. Current balance: " + currentUser.getBalance());
        }
    }

    private void deposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        currentUser.setBalance(currentUser.getBalance() + amount);
        currentUser.addTransaction(new Transaction("Deposit", amount));
        System.out.println("Deposit successful. Current balance: " + currentUser.getBalance());
    }

    private void transfer() {
        bank.displayUserIds(); // Display available user IDs
        System.out.print("Enter recipient user ID: ");
        String recipientUserId = scanner.nextLine();
        User recipient = bank.getUserById(recipientUserId);

        if (recipient == null) {
            System.out.println("Recipient not found.");
            return;
        }

        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (amount > currentUser.getBalance()) {
            System.out.println("Insufficient balance.");
        } else {
            currentUser.setBalance(currentUser.getBalance() - amount);
            recipient.setBalance(recipient.getBalance() + amount);

            currentUser.addTransaction(new Transaction("Transfer to " + recipientUserId, amount));
            recipient.addTransaction(new Transaction("Transfer from " + currentUser.getUserId(), amount));

            System.out.println("Transfer successful. Current balance: " + currentUser.getBalance());
        }
    }
}
