def rbs(ls,t):
    if len(ls) == 0:
        return False
    else:
        m = (len(ls))//2
        if ls[m] == t:
            return True
        else:
            if ls[m] < t:
                return rbs(ls[m+1:],t)
            else:
                return rbs(ls[:m],t)

def v(r):
    print("Target found: ", r)

n = [1,3,2,5,56,2,3,35]
r = rbs(n, 100)
v(r)
