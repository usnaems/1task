// Файл: Main.java
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

class BankAccount {  // Убрали public
    private final String ownerName;
    private int balance;
    private final LocalDateTime openingDate;
    private boolean isBlocked;
    private final String accountNumber;

    public BankAccount(String ownerName) {
        this.ownerName = ownerName;
        this.balance = 0;
        this.openingDate = LocalDateTime.now();
        this.isBlocked = false;
        this.accountNumber = generateAccountNumber();
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public boolean deposit(int amount) {
        if (isBlocked || amount <= 0) return false;
        balance += amount;
        return true;
    }

    public boolean withdraw(int amount) {
        if (isBlocked || amount <= 0 || amount > balance) return false;
        balance -= amount;
        return true;
    }

    public boolean transfer(BankAccount otherAccount, int amount) {
        if (this == otherAccount || isBlocked || otherAccount.isBlocked)
            return false;

        if (this.withdraw(amount)) {
            if (otherAccount.deposit(amount)) {
                return true;
            } else {
                // Возврат при ошибке зачисления
                this.balance += amount;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format(
                "Счет №%s\nВладелец: %s\nБаланс: %d руб.\nДата открытия: %s\nСтатус: %s",
                accountNumber,
                ownerName,
                balance,
                openingDate,
                isBlocked ? "ЗАБЛОКИРОВАН" : "Активен"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    // Геттеры
    public String getAccountNumber() { return accountNumber; }
    public boolean isBlocked() { return isBlocked; }
    public int getBalance() { return balance; }
}

public class Main {
    public static void main(String[] args) {
        BankAccount account1 = new BankAccount("Иван Иванов");
        BankAccount account2 = new BankAccount("Петр Петров");

        account1.deposit(10000);
        account1.transfer(account2, 3000);

        System.out.println("=== Счет 1 ===");
        System.out.println(account1);

        System.out.println("\n=== Счет 2 ===");
        System.out.println(account2);

        System.out.println("\nПеревод на тот же счет: " + account1.transfer(account1, 1000));
    }
}