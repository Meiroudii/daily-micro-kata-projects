class TicTacToe
  def initialize()
    @board = Hash[(1..9).map { |x| [x, '.'] }]

    puts visualize
    
  end
  # Here we make Hash with 9 dots and then use modulos 3 to make dots arranged by 3 so I used /n to make new 
  #line between dots, that makes string included our array which we have to use join at the end of array to convert string

  def visualize
    joined = @board.map do |pos, sym|
      pos % 3 == 0 ? sym +"\n" : sym
    end.join(" ")
    " " + joined
  end

  # now we should ask to play and get integer

  def valid_coordinate(player)
        the_question = "Please enter a number to play as #{player}:"
    puts the_question
    coordinate = gets.chomp.to_i
    until @board[coordinate] == "."
        puts the_question
        coordinate = gets.chomp.to_i
          
    end
    @board[coordinate] = player
  end

  def start
    turn = 1
    while turn < 10
    player = turn.odd? ? "X":"O"
    valid_coordinate(player.to_s)
    puts visualize
      if a_winner?(player)
        p "#{player.to_s}, the winner!"
        return
      end
    turn +=1
    end
  end


  #  winner process
  def a_winner?(player)
    winners_array = [
     [1,2,3],[4,5,6],[7,8,9],
     [1,4,7],[2,5,8],[3,6,9],
     [1,5,9],[3,5,7]
    ]

    memory_array = @board.select{ |k, v| v == player }.keys
    puts memory_array
    winners_array.each do |arr|
      return true if (arr - memory_array).empty?
    end 
    false
  end

  # winner proces
  def a_winner?(player)
    winners_array = [
     [1,2,3],[4,5,6],[7,8,9],
     [1,4,7],[2,5,8],[3,6,9],
     [1,5,9],[3,5,7]
    ]
    #  Players action is collected in array and controlled for each element of the winners_array by substracting if its empty then its winner!
    memory_array = @board.select{ |k, v| v == player }.keys
    winners_array.each do |arr|
      return true if (arr - memory_array).empty?
    end 
    false
  end 

end

ttt = TicTacToe.new()
ttt.start
