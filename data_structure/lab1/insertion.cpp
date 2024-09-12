#include <iostream>
#include <vector>
#include <string>

void insertionSort(int arr[], int n) {
    for (int i = 1; i < n; ++i) {
        int key = arr[i];
        int j = i - 1;

        while (j >= 0 && arr[j] > key) {  // Move elements of arr[0..i-1], that are greater
            arr[j + 1] = arr[j];   //than key, to find the position to insert key
            j = j - 1;
        }
        arr[j + 1] = key;//insert key
    }
}

void insertionSortD(double arr[], int n) {
    for (int i = 1; i < n; ++i) {
        double key = arr[i];
        int j = i - 1;

        while (j >= 0 && arr[j] > key) {  // Move elements of arr[0..i-1], that are greater
            arr[j + 1] = arr[j];   //than key, to find the position to insert key
            j = j - 1;
        }
        arr[j + 1] = key;//insert key
    }
}

void insertionSortS(std::vector<std::string>& arr) {
    int n = arr.size();
    for (int i = 1; i < n; ++i) {
        std::string key = arr[i];
        int j = i - 1;

        while (j >= 0 && arr[j] > key) {  // Move elements of arr[0..i-1], that are greater
            arr[j + 1] = arr[j];   //than key, to find the poesition to insert key
            j = j - 1;
        }
        arr[j + 1] = key;//insert key
    }
}

int main() {
    int arr[] = {64, 25, 12, 22, 11};
    int n = sizeof(arr) / sizeof(int);
    std::cout<< "Original array: \n";
    for (int i = 0; i < n; i++) {
        std::cout << arr[i] << " ";
    }
    insertionSort(arr, n);
    std::cout << "\nSorted array: \n";
    for (int i = 0; i < n; i++) {
        std::cout << arr[i] << " ";
    }
    std::cout << "\n";

    double arr1[] = {64.5, 25.2, 12.3, 22.1, 11.4,9.7};
    int n1 = sizeof(arr1) / sizeof(double);
    std::cout<< "Original array: \n";
    for (int i = 0; i < n1; i++) {
        std::cout << arr1[i] << " ";
    }
    insertionSortD(arr1, n1);
    std::cout << "\nSorted array: \n";
    for (int i = 0; i < n1; i++) {
        std::cout << arr1[i] << " ";
    }
    std::cout << "\n";

    std::vector<std::string> arr2 = {"apple", "banana", "cherry", "orange", "kiwi"};
    std::cout<< "Original array: \n";
    for (int i = 0; i < arr2.size(); i++) {
        std::cout << arr2[i] << " ";
    }
    insertionSortS(arr2);
    std::cout << "\nSorted array: \n";
    for (int i = 0; i < arr2.size(); i++) {
        std::cout << arr2[i] << " ";
    }
    std::cout << "\n";

    return 0;
}
