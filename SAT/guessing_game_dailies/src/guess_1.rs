use std::cmp::Ordering;
use std::io; // This bad boi holds the input/output library
use rand::Rng;
use rand::prelude::*;


pub fn main() {
    if rand::random() {
        println!("char: {}", rand::random::<char>());
    }
    let mut rng = rand::thread_rng();
    let y: f64 = rng.r#gen();
    let mut nums: Vec<i32> = (1..100).collect();
    nums.shuffle(&mut rng);
    println!("{}", y);
    println!("{:?}",nums);

    println!("Guess the Number!");
    let secret_number = rand::thread_rng().gen_range(1..=100);
    loop {
        println!("Please input your guess.");
        let mut guess = String::new();
        io::stdin()
            .read_line(&mut guess)
            .expect("Cannot read the line.");
        let guess: u32 = match guess.trim().parse() {
            Ok(num) => num,
            Err(_) => continue,
        };
        println!("You guessed: {guess}");
        match guess.cmp(&secret_number) {
            Ordering::Less => println!("Too smal!l"),
            Ordering::Greater => println!("Too large!"),
            Ordering::Equal => {
                println!("You win!");
                break;
            }
        }
    }
}
