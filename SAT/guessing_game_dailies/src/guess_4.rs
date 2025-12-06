use std::io;
use std::cmp::Ordering;
use rand::Rng;

pub fn human_binary_search() {
    println!("_______________________start:");
    let sn = rand::thread_rng().gen_range(1..=100);
    loop {
        println!("> ");
        let mut guess = String::new();
        io::stdin()
            .read_line(&mut guess)
            .expect("[!]");
        let guess: u32 = match guess.trim().parse() {
            Ok(num) => num,
            Err(_) => continue,
        };
        match guess.cmp(&sn) {
            Ordering::Less => println!("\t\t\tv"),
            Ordering::Greater => println!("\t\t\t^"),
            Ordering::Equal => {
                println!("Number Matched");
                break;
            }
        }
    }
}
