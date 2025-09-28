def binary_search(list, target):
    if len(list) == 0:
        return False
    else:
        mid_point = (len(list))//2
        if list[mid_point] == target:
            return True
        else:
            if list[mid_point] < target:
                return binary_search(list[mid_point+1:], target)
            else:
                return binary_search(list[:mid_point], target)

def rbs(ls, t):
    if not ls:
        return False
    else:
        mid_point = (len(ls))//2
        if ls[mid_point] == t:
            return True
        elif ls[mid_point] < t:
            return rbs(ls[mid_point+1:], t)
        else:
            return rbs(ls[:mid_point],t)

