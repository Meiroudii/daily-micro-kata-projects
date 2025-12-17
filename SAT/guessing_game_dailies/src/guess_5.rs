use std::io;
use std::cmp::Ordering;
use rand::Rng;

pub fn guessing_game() {
    println!("__________start:");
    let sn = rand::thread_rng().gen_range(1..=100);
    loop {
        println!("> ");
        let mut guess = String::new();
        io::stdin()
            .read_line(&mut guess)
            .expect("[!]");
        let guess: i8 = match guess.trim().parse() {
            Ok(num) => num,
            Err(_) => continue,
        };
        match guess.cmp(&sn) {
            Ordering::Less => println!("\t\t[v] Low"),
            Ordering::Greater => println!("\t\t[^] High"),
            Ordering::Equal => {
                println!("\t\tNumber Matched");
                break;
            }
        }
    }
}
