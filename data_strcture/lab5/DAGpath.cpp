#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <string>
#include <fstream>
#include <sstream>

using namespace std;

int t = 1;//Cnt for gragh example
const int N = 10000;//Suggest the number of vertices is no more than 10000

// Function to find the longest path in a DAG
void DAGpath(vector<vector<int>>& gragh, vector<int>& weight, vector<int>& indegree) {
    queue<int> q;
    vector<int> res(N);
    int n = weight.size();
    
    for(int i = 0;i < n;i++)
        if(indegree[i] == 0){
            q.push(i);
            res[i] = weight[i];
        }

    while (!q.empty()) {
        int cur = q.front();
        q.pop();
        if(!gragh[cur].empty())
            for (int next : gragh[cur]) {
            if (--indegree[next] == 0) 
                q.push(next);
            res[next] = max(res[next], res[cur] + weight[next]);
            }
    }

    int longest = 0;
    for(int i = 0;i < n;i++)
        if(gragh[i].empty())
            longest = max(longest, res[i]);

    cout << "graph" << t++ << ": longest = " << longest << endl;
}

//  Auxiliary function to read a row of data
vector<int> parseIntVector(const string& line) {
    vector<int> result;
    result.clear();
    //Ignore the square brackets and comma
    stringstream data_w(line.substr(1, line.size() - 3));  
    string unit_w;
    while (getline(data_w, unit_w, ',')) 
        result.push_back(stoi(unit_w));//Parse the string to integer
    return result;
}

// Auxiliary function to read  adjacent matrix of gragh
void parseIntMatrix(const vector<string>& lines, vector<vector<int>>& gragh, vector<int>& indegree) {
    vector<int> data_r;
    for (int i = 0; i < lines.size(); i++) {
        data_r = parseIntVector(lines[i]);
        for(int j = 0;j < data_r.size();j++)
            if(data_r[j]){
                gragh[i].push_back(j);
                indegree[j]++;
            }
    }
    return;
}

int main() {
    ifstream file("gragh.txt");
    string line;
    while (getline(file, line)){
        vector<vector<int>> gragh(N);
        vector<int> weight,indegree(N);

        //Read in the weight of each vertex
        getline(file, line);
        weight = parseIntVector(line);

        // Read in the adjacent matrix of graph
        vector<string> adMatrix;
        while(getline(file, line)) {//This while loop is used to skip null string
            if (line == "edges="){
                while (getline(file, line)) {
                    if (line.substr(0,1) != "[") 
                        break;
                    adMatrix.push_back(line);
                }   
                break;
            }     
        }

        if (!adMatrix.empty()) 
            parseIntMatrix(adMatrix, gragh, indegree);

        DAGpath(gragh, weight, indegree);
    }

    file.close();

    return 0;
}
//graph1:longest=18     graph2:longest=8    graph3:longest=27   graph4:longest=16