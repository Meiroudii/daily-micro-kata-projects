def rbs(list, target):
    if not list:
        return False
    m = len(list)//2
    if list[m] == target:
        return True
    else:
        if list[m] >= target:
           return rbs(list[:m], target)
        else:
            return rbs(list[m+1:], target)
