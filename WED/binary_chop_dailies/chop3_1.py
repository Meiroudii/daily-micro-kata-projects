def recursive_binary_search(array, target):
    if not array:
        return False
    else:
        left = 0
        right = (len(array))-1
        mid_point = (left + (right - left))//2
        if array[mid_point] == target:
            return True
        elif array[mid_point] <= target:
            return recursive_binary_search(array[mid_point+1:], target)
        else:
            return recursive_binary_search(array[:mid_point], target)

print(recursive_binary_search([1,2,3], 2))
