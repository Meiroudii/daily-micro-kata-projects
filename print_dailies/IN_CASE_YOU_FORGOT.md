nasm -f elf64 print.asm
ld test.o

cobc -x print.cob
./print.cob

gfortran print.cob
