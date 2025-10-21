def rbs(list, target):
    if not list:
        return False
    else:
        m = len(list)//2
        if list[m] == target:
            return True
        else:
            if list[m] < target:
                return rbs(list[m+1:], target)
            else:
                return rbs(list[:m], target)

def verify(r):
    return r and f"Found 'em {r}"

print(verify(rbs([1,2,3,5,2,5,2,5,3,33,555,55,2,2,56,136,3], 136)))
