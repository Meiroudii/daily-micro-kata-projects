       IDENTIFICATION DIVISION.
       PROGRAM-ID. CHOP12.
       DATA DIVISION.
       WORKING-STORAGE SECTION.
       01  ARRAY-SIZE       PIC 9(2) VALUE 5.
       01  ARRAY.
           05  ARR-ELEM     OCCURS 5 TIMES PIC 9(2).
       01  TARGET           PIC 9(2).
       01  LEFT-IDX         PIC 9(2).
       01  RIGHT-IDX        PIC 9(2).
       01  MID              PIC 9(2).
       01  GUESS            PIC 9(2).
       01  RESULT           PIC S9(4) VALUE -1.

       PROCEDURE DIVISION.
       MAIN-LOGIC.
           *> First array (1, 3, 4, 5, 6)
           MOVE 1 TO ARR-ELEM (1)
           MOVE 3 TO ARR-ELEM (2)
           MOVE 4 TO ARR-ELEM (3)
           MOVE 5 TO ARR-ELEM (4)
           MOVE 6 TO ARR-ELEM (5)

           DISPLAY "First Experiment: target = 2"
           MOVE 2 TO TARGET
           PERFORM BINARY-SEARCH
           PERFORM VERIFY

           *> Second array (1, 2, 4, 5, 6)
           DISPLAY "Second Experiment: target = 2"
           MOVE 1 TO ARR-ELEM (1)
           MOVE 2 TO ARR-ELEM (2)
           MOVE 4 TO ARR-ELEM (3)
           MOVE 5 TO ARR-ELEM (4)
           MOVE 6 TO ARR-ELEM (5)
           MOVE 2 TO TARGET
           PERFORM BINARY-SEARCH
           PERFORM VERIFY

           STOP RUN.

       BINARY-SEARCH.
           MOVE 1 TO LEFT-IDX
           MOVE ARRAY-SIZE TO RIGHT-IDX
           MOVE -1 TO RESULT
           PERFORM UNTIL LEFT-IDX > RIGHT-IDX
               COMPUTE MID = (LEFT-IDX + RIGHT-IDX) / 2
               MOVE ARR-ELEM (MID) TO GUESS
               IF GUESS = TARGET
                   MOVE MID TO RESULT
                   EXIT PERFORM
               ELSE
                   IF GUESS < TARGET
                       ADD 1 TO MID GIVING LEFT-IDX
                   ELSE
                       SUBTRACT 1 FROM MID GIVING RIGHT-IDX
                   END-IF
               END-IF
           END-PERFORM
           DISPLAY "Result: " RESULT.

       VERIFY.
           IF RESULT > 0
               DISPLAY "Found!"
           ELSE
               DISPLAY "Can't find em"
           END-IF.
