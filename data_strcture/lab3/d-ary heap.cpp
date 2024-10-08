#include <iostream>  
#include <vector>  
#include <algorithm> // for std::max  
  
class DaryHeap {  
private:  
    std::vector<int> heap;  
    int d; // degree of the heap  
  
    // Helper function to heapify down from a given index  
    void heapifyDown(int i) {  
        int largest = i;  
        for (int j = 1; j <= d; ++j) {  
            int childIndex = d * i + j;  
            if (childIndex < heap.size() && heap[childIndex] > heap[largest]) {  
                largest = childIndex;  
            }  
        }  
        if (largest != i) {  
            std::swap(heap[i], heap[largest]);  
            heapifyDown(largest); // Recursively heapify the affected subtree  
        }  
    }  
  
    // Helper function to heapify up from a given index  
    void heapifyUp(int i) {  
        while (i > 0) {  
            int parentIndex = (i - 1) / d;  
            if (heap[i] > heap[parentIndex]) {  
                std::swap(heap[i], heap[parentIndex]);  
                i = parentIndex; // Continue heapifying up  
            } else {  
                break; // No need to further heapify up  
            }  
        }  
    }  
  
public:  
    DaryHeap(int degree) : d(degree) {}  
  
    // Insert a new key into the heap  
    void insert(int key) {  
        heap.push_back(key);  // Add the key to the end of the heap  
        heapifyUp(heap.size() - 1);  // Heapify up the new key  
    }  
  
    // Extract and return the maximum key from the heap  
    int extractMax() {  
        if (heap.empty()) {  
            throw std::runtime_error("Heap is empty");  
        }  
        int maxValue = heap[0];  
        heap[0] = heap.back();  // Move the last element to the root  
        heap.pop_back();  // Remove the last element  
        heapifyDown(0);  // Heapify down the new root  
        return maxValue;  
    }  
  
    // Increase the key at index i to be the maximum of the current key and k  
    void increaseKey(int i, int k) {  
        if (i < 0 || i >= heap.size()) {  
            throw std::out_of_range("Index out of range");  
        }  
        heap[i] = std::max(heap[i], k);  
        heapifyUp(i);  
    }  
  
    // Print the heap (for debugging)  
    void print() const {  
        for (int val : heap) {  
            std::cout << val << " ";  
        }  
        std::cout << std::endl;  
    }  
};  
  
int main() {  
    DaryHeap heap(3); // Create a 3-ary heap  
  
    // Insert elements into the heap  
    heap.insert(7);  
    heap.insert(23);  
    heap.insert(5);  
    heap.insert(30);  
    heap.insert(15);  
  
    // Print the heap  
    heap.print();  
  
    // Extract and print the maximum element  
    std::cout << "Extracted max: " << heap.extractMax() << std::endl;
    heap.print();

    heap.insert(25); // Insert a new element
    heap.print();
  
    // Increase the key at index 2 and print the heap  
    heap.increaseKey(2, 37);  
    heap.print();  
  
    return 0;  
}