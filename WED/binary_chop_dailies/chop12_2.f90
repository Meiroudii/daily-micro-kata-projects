program chop12_2
  implicit none
  integer :: arr1(5) = [1,3,4,5,6]
  integer :: arr2(5) = [1,2,4,5,6]
  integer :: target, result

  print *, "First Experiment: target = 2"
  target = 2
  result = chop(target, arr1)
  print *, "Result:", result
  call verify(result)

  print *, "Second Experiment: target = 2"
  target = 2
  result = chop(target, arr2)
  print *, "Result:", result
  call verify(result)
contains
  integer function chop(target, array)
    implicit none
    integer, intent(in) :: target
    integer, intent(in) :: array(:)
    integer :: left, right, mid, guess

    left = 1
    right = size(array)
    chop = -1
    do while (left <= right)
       mid = (left + right) / 2
       guess = array(mid)
       if (guess == target) then
          chop = mid
          return
       else if (guess < target) then
          left = mid + 1
       else
          right = mid - 1
       end if
    end do
  end function chop

  subroutine verify(i32)
    implicit none
    integer, intent(in) :: i32
    if (i32 > 0) then
       print *, "Found!"
    else
       print *, "Can't find em"
    end if
  end subroutine verify
end program chop12_2
