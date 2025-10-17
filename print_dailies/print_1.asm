section .data
    msg db 9,10,"yo.",10        ; 9 = tab, 10 = newline
    len equ $ - msg

section .text
global _start

_start:
    mov eax, 1                  ; sys_write
    mov edi, 1                  ; fd = stdout
    lea rsi, [rel msg]          ; address of string
    mov edx, len                ; length
    syscall

    mov eax, 60                 ; sys_exit
    xor edi, edi
    syscall
