use std::io;
use std::cmp::Ordering;
use rand::Rng;

pub fn guess() {
    println!("Guess it!");
    let sn = rand::thread_rng().gen_range(1..=100);
    loop {
        println!("Guess: ");
        let mut guess = String::new();
        io::stdin()
            .read_line(&mut guess)
            .expect("Cannot read the line.");
        let guess: u32 = match guess.trim().parse() {
            Ok(num) => num,
            Err(_) => continue,
        };
        println!("You guessed: {guess}");
        match guess.cmp(&sn) {
            Ordering::Less => println!("Too low!"),
            Ordering::Greater => println!("Too Large!"),
            Ordering::Equal => {
                println!("You win!");
                break;
            }
        }
    }
}
