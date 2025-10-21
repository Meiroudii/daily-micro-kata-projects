def rbs(list, target):
    if not list:
        return False
    else:
        mid_point = (len(list))//2
        if list[mid_point] == target:
            return True
        else:
            if list[mid_point] < target:
                return rbs(list[mid_point+1:, target)
            else:
                return rbs(list[:m], target)

def v(r):
    print("Target found: ", r)

n = [3,5,5,5,2,2,52,5,2,5]
r = rbs(n ,5)
v(r)
