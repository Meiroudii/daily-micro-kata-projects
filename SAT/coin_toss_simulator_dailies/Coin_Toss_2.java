import java.util.*;
class Coin_Toss_2 {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
Random toss1 = new Random();
Random toss2 = new Random();
int cpuPts = 0;
int playPts = 0;
while (true){
try{
System.out.println("Press 1 to play: ");
int play1 = scan.nextInt();
int cpuToss = toss1.nextInt(1, 3);
int playToss = toss1.nextInt(1, 3);
if (cpuToss == 1){
System.out.println("CPU is: Heads");
}else{
System.out.println("CPU is: Tails");
}
if (playToss == 1){
System.out.println("Player is: Heads");
}else{
System.out.println("Player is: Tails");
}
if (playToss == cpuToss){
System.out.println("It's a tie");
}else if(playToss <= cpuToss){
System.out.println("You Win");
playPts +=1;
System.out.println("Your Score: " + playPts);
System.out.println("CPU Score: " + cpuPts);
}else{
System.out.println("You Lose!");
cpuPts ++;
System.out.println("Your Score: " + playPts);
System.out.println("CPU Score: " + cpuPts);
}
}catch(InputMismatchException e){
System.out.println("Invalid Input. Try Again");
scan.nextLine();
}
}
}
}
