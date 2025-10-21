section .data
    msg db "result: 2 + 2 = ", 0
    newline db 10, 0

section .bss
    result resb 1

section .text
global _start

_start:
    mov rax, 2        ; first number
    add rax, 2        ; add second number
    add al, '0'       ; convert to ASCII '4'
    mov [result], al

    ; print "result: 2 + 2 = "
    mov rax, 1        ; syscall: write
    mov rdi, 1        ; fd: stdout
    mov rsi, msg
    mov rdx, 15       ; length of message
    syscall

    ; print result
    mov rax, 1
    mov rdi, 1
    lea rsi, [result]
    mov rdx, 1
    syscall

    ; print newline
    mov rax, 1
    mov rdi, 1
    mov rsi, newline
    mov rdx, 1
    syscall

    ; exit
    mov rax, 60
    xor rdi, rdi
    syscall
