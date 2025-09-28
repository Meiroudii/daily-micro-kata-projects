def binary_search(list, target):
    left = 0
    right = len(list) - 1

    while left <= right:
        mid_point = (left + right) // 2

        if list[mid_point] == target:
            return mid_point
        elif lists[mid_point] < target:
            first = mid_point + 1
        else:
            last = mid_point - 1
    return None

def verify(index):
    if index is not None:
        print("Target found at index: ", index)

