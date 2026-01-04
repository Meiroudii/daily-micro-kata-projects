use std::io;
use std::cmp::Ordering;
use rand::Rng;

pub fn game() {
    println!("_start:");
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
            Ordering::Less => println!("low"),
            Ordering::Greater => print!("High"),
            Ordering::Equal => {
                println!("Correct");
                break;
            }
        }
    }
}
