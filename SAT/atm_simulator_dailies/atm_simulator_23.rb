require "date"
class BankAccount
  def initialize(balance)
    @balance = balance
  end
  attr_reader :balance

  def validate_transaction(amount, transaction_type)
    case transaction_type
    when "w"
      return false if amount > @balance
      @balance -= amount
      true
    when "d"
      return false if amount <= 0
      @balance += amount
      true
    else
      puts "Access Blocked"
      false
    end
  end
end

class ATM
  def initialize(balance)
    @user = BankAccount.new(balance)
    @message = ""
  end
  def validate_otp
    generate_otp = rand(1000..9999)
    print "Your OTP [#{generate_otp}]\n\t\t>>>"

    begin
      otp_field = gets.chomp.to_i
      otp_field == generate_otp
    rescue ArgumentError
      puts "Invalid Input."
      false
    end
  end

  def show_menu
    today = Time.now
    puts "\n\n\n\n\n"
    puts "\t\t\t________________________"
    puts "\t\t\t_#{today.strftime('%a %b %d %S %Z %Y')}____"
    puts "\t\t\t_______YOKOHAMA__BANK___"
    puts "\t\t\t________________________"
    puts "\t\t\t|_--____________________"
    puts "\t\t\t| #{today.strftime('%H:%M')}"
    puts "\t\t\t| balance: $#{'%.2f' % @user.balance}"
    puts "\t\t\t| #{today.strftime('%I:%M%p')}"
    puts "\t\t\t| 1 Withdraw  2 Deposit"
    puts "\t\t\t| 3 Refresh   4 Exit"
    puts "\t\t\t|"
    puts "\t\t\t| #{@message}"
    puts "\t\t\t|=============================="
    puts "\t\t\t|"
    print "\t\t\t|->_>->_> > > > > > > > > : "
  end

  def run
    if validate_otp
      power = true
      while power
        loading(15)
        clear_screen
        show_menu
        choice = gets.chomp.strip

        case choice
        when "1"
          handle_withdraw
        when "2"
          handle_deposit
        when "3"
          puts "Refreshing..."
        when "4"
          loading(2)
          puts "Goodbye"
          power = false
        else
          @message = "Invalid Command"
        end
      end
    else
      puts "Authentication Failed."
    end
  end

  private

  def handle_withdraw
    print "[Withdraw] amount: $"
    begin
      amount = gets.chomp.to_f
      loading(25)
      if @user.validate_transaction(amount, "w")
        @message = "Withdraw Success!"
      else
        @message = "Withdraw Failed."
      end
    rescue ArgumentError
      @message = "Transaction Failed."
    end
  end

  def handle_deposit
    print "[deposit] amouont: $"
    begin
      amount = gets.chomp.to_f
      laoding(15)
      if @user.validate_transaction(amount, "d")
        @message = "Deposit Success!"
      else
        @message = "Deposit Failed"
      end
    rescue ArgumentError
      @message = "Transaction Failed."
    end
  end

  def clear_screen
    print "\033[H\033[2J"
    $stdout.flush
  end

  def loading(speed)
    buf = 50
    (0..buf).each do |index|
      percent = (index * 100) / buf
      bar = "=" * index + "-" * (buf - index)
      print "\r\t\t[#{bar}] #{percent}%"
      sleep(speed / 1000.0)
    end
  end
end

yoko_bank = ATM.new(10.0)
yoko_bank.run

