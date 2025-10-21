def binary_search(arr, target):
    left, right = 0, len(arr) - 1
    while left ___ right:
        mid = (left + right) // 2
        if arr[mid] == target:
            return mid
        if arr[mid] < target:
            ___ = mid + 1
        else:
            ___ = mid - 1
    return -1
