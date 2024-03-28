#include <iostream>
#include <cstring>
#include <algorithm>

using namespace std;
const int N = 1e6 + 10;

int n, m;
string a, b;
int fail[N];

int main()
{
    cin >> n >> a >> m >> b;
    //trie树的第一个为根节点不存数据
    string c = " " + a;
    //0是trie树的根节点
    fail[0] = -1;
    //1是trie树的第一个节点
    fail[1] = 0;
    //求失败节点
    for (int i = 1; i < c.length() - 1; i ++ ){
        //当前节点
        int cur = i;
        //当前节点的子节点
        int child = i + 1;
        //如果当前节点的失败节点（fail[cur]）的子节点（fail[cur]+1）与子节点（child）不同，那么当前节点就为失败节点重复比较
        while(fail[cur] != -1 && c[fail[cur] + 1] != c[child]){
            cur = fail[cur];
        }
        if(fail[cur] != -1){
            fail[child] = fail[cur] + 1;
            //此时，c[child] = c[fail[child]]
        }else{
            //否则失败节点是向根节点
            fail[child] = 0;
        }

    }
    //匹配
    for (int i = 0, j = 0; i < m; i ++ ) {
        //j是trie树的根节点，j+1才是trie树的子元素即模式串的第一个元素
        if(c[j + 1] == b[i]){
            //匹配个数+1
            j = j + 1;
            if(j == n){
                //匹配到了足够的数量
                //输出当前位置
                cout << i - (n - 1) << " ";
                //更新已匹配个数
                j = fail[j];
            }
        }else{

            if(fail[j] != -1){
                //匹配失败
                //更新已匹配个数
                j = fail[j];
                //当前i不变
                i--;
            }

        }
    }

}