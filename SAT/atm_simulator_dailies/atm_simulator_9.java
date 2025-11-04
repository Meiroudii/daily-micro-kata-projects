import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

class BankAccount {
  private double balance;

  public BankAccount(double initialBalance) {
    balance = initialBalance;
  }

  public double viewBalance() {
    return balance;
  }

  public boolean verifyWithdraw(double amount) {
    if (amount > balance || amount < 0) return false;
    balance -= amount;
    return true;
  }

  public boolean verifyDeposit(double amount) {
    if (amount <= 0) return false;
    balance += amount;
    return true;
  }
}

public class atm_simulator_9 extends JFrame {
  private final BankAccount guestUser;
  private final JLabel flashMessage;
  private final JLabel balanceLabel;

  public atm_simulator_9(double initialBalance) {
    guestUser = new BankAccount(initialBalance);

    setTitle("ATM Simulator 9");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400, 250);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null);

    // Header
    JLabel header = new JLabel("ATM Simulator", SwingConstants.CENTER);
    header.setFont(new Font("Monospaced", Font.BOLD, 20));
    add(header, BorderLayout.NORTH);

    // Center panel
    JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));

    balanceLabel = new JLabel("Current Balance: $ " + String.format("%.2f", guestUser.viewBalance()), SwingConstants.CENTER);
    centerPanel.add(balanceLabel);

    JButton withdrawBtn = new JButton("Withdraw");
    JButton depositBtn = new JButton("Deposit");
    JButton exitBtn = new JButton("Exit");

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(withdrawBtn);
    buttonPanel.add(depositBtn);
    buttonPanel.add(exitBtn);

    centerPanel.add(buttonPanel);

    flashMessage = new JLabel("", SwingConstants.CENTER);
    centerPanel.add(flashMessage);

    add(centerPanel, BorderLayout.CENTER);

    // Button logic (fixed without preview feature)
    withdrawBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handleWithdraw();
      }
    });

    depositBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handleDeposit();
      }
    });

    exitBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("SHUTTING DOWN...");
        System.exit(0);
      }
    });
  }

  private void handleWithdraw() {
    String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
    if (input == null) return; // cancelled
    try {
      double amount = Double.parseDouble(input);
      if (guestUser.verifyWithdraw(amount)) {
        flashMessage.setText(String.format("$%.2f withdrawn successfully", amount));
      } else {
        flashMessage.setText("Invalid Transaction.");
      }
      updateBalance();
    } catch (NumberFormatException err) {
      flashMessage.setText("Transaction Failed.");
    }
  }

  private void handleDeposit() {
    String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
    if (input == null) return; // cancelled
    try {
      double amount = Double.parseDouble(input);
      if (guestUser.verifyDeposit(amount)) {
        flashMessage.setText(String.format("$%.2f deposited successfully", amount));
      } else {
        flashMessage.setText("Nothing added.");
      }
      updateBalance();
    } catch (NumberFormatException err) {
      flashMessage.setText("Transaction Failed.");
    }
  }

  private void updateBalance() {
    balanceLabel.setText("Current Balance: $ " + String.format("%.2f", guestUser.viewBalance()));
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new atm_simulator_9(0.0).setVisible(true);
      }
    });
  }
}
