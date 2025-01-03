#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include <ctime>
#include <chrono>
#include <cstring>

using namespace std;
using namespace std::chrono;

const int N = 26000;
const int MAX_TIME = 100;   
const int MAX_PROFIT = 10000;  
const int MAX_DEADLINE = 1000;  

struct Job {
    int time;
    int profit;
    int deadline;
};

vector<Job> jobs;
int mem[N][MAX_DEADLINE];

bool compareByDeadline(const Job &a, const Job &b) {
    return a.deadline < b.deadline;
}

int memtb(int x,int cur_time){
    if(x == jobs.size())
        return 0;
    if(mem[x][cur_time])
        return mem[x][cur_time];
    if(cur_time + jobs[x].time > jobs[x].deadline){
        mem[x][cur_time] = memtb(x+1,cur_time);
        return mem[x][cur_time];
    }
    else{
        mem[x][cur_time] = max(memtb(x+1,cur_time), memtb(x+1,cur_time + jobs[x].time) + jobs[x].profit);
        return mem[x][cur_time];
    }
}

void testBench(){
    srand(time(0));   
 
    for (int i = 0; i < N; i++) {
        Job job;
        job.time = rand() % (MAX_TIME) + 1;
        job.profit = rand() % (MAX_PROFIT) + 1;
        job.deadline = rand() % (MAX_DEADLINE) + 1;
        jobs.push_back(job);
    }

    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for test bench: " << memtb(0,0) << endl;
}

int main() {
    //To check the correctness  
    jobs = {{1, 10, 2}, {2, 5, 4}, {1, 8, 3}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    memset(mem, 0, sizeof(mem));
    cout << "Maximum profit for case 1: " << memtb(0,0) << endl;
    
    jobs = {{2, 15, 3}, {1, 20, 1}, {3, 25, 2}, {2, 5, 5}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    memset(mem, 0, sizeof(mem));
    cout << "Maximum profit for case 2: " << memtb(0,0) << endl;
    
    jobs = {{1, 10, 3}, {2, 15, 5}, {1, 5, 2}, {3, 12, 6}, {2, 8, 7}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    memset(mem, 0, sizeof(mem));
    cout << "Maximum profit for case 3: " << memtb(0,0) << endl;
    
    jobs = {{1, 50, 2}, {2, 5, 4}, {3, 100, 3}, {1, 15, 5}, {2, 10, 6}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    memset(mem, 0, sizeof(mem));
    cout << "Maximum profit for case 4: " << memtb(0,0) << endl;
    
    jobs = {{1, 20, 2}, {2, 15, 2}, {1, 25, 1}, {2, 10, 3}, {1, 5, 3}, {3, 30, 4}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    memset(mem, 0, sizeof(mem));
    cout << "Maximum profit for case 5: " << memtb(0,0) << endl;
 
    jobs.clear();
    //To test the performance of memtb algorithm
    auto start = high_resolution_clock::now();
    memset(mem, 0, sizeof(mem));
    testBench();
    auto end = high_resolution_clock::now();
    duration<double> elapsed = end - start;
 
    cout << "Elapsed time for memtb: " << elapsed.count() << " seconds" << endl;

    return 0;
}

/*input: (t,p,d), output: (job order) max profit
[(1, 10, 2), (2, 5, 4), (1, 8, 3)]
(0, 2, 1) 23
[(2, 15, 3), (1, 20, 1), (3, 25, 2), (2, 5, 5)]	
(1, 0, 3) 40
[(1, 10, 3), (2, 15, 5), (1, 5, 2), (3, 12, 6), (2, 8, 7)]
(2, 0, 1, 4) 38
[(1, 50, 2), (2, 5, 4), (3, 100, 3), (1, 15, 5), (2, 10, 6)]
(2, 3, 4) 125
[(1, 20, 2), (2, 15, 2), (1, 25, 1), (2, 10, 3), (1, 5, 3), (3, 30, 4)]
(2, 5) 55
*/