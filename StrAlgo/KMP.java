package StrAlgo;

import java.util.Scanner;

/**
 * 从ac自动机的思路去做KMP
 * 模式串就是一个trie树，当前节点为i，父节点为i-1，子节点为i+1。
 * 求失败节点时，第一层的失败节点是根，然后子节点的失败节点是当前节点的失败节点的子节点（如果相等的话），不相等就找父节点的失败节点的失败节点，直到相同或到根为止。
 * 匹配时，j既是trie的指针也是已匹配的个数
 */
public class KMP {

    static int n, m;
    static String a, b, c;
    static int[] fail = new int[(int) (1e6 + 10)];
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = Integer.parseInt(sc.nextLine());
        a = sc.nextLine();
        m = Integer.parseInt(sc.nextLine());
        b = sc.nextLine();
        c = " " + a;

        fail[0] = -1;
        fail[1] = 0;
        for (int i = 1; i < c.length() - 1; i ++ ){
            //当前节点
            int cur = i;
            //当前节点的子节点
            int child = i + 1;
            while(fail[cur] != -1 && c.charAt(fail[cur] + 1) != c.charAt(child)){
                cur = fail[cur];
            }
            if(fail[cur] != -1){
                fail[child] = fail[cur] + 1;
            }else{
                fail[child] = 0;
            }
        }

        for (int i = 0, j = 0; i < m; i ++ ) {
            if(c.charAt(j + 1) == b.charAt(i)){
                j = j + 1;
                if(j == n){
                    System.out.print(i - (n - 1) + " ");
                    j = fail[j];
                }
            }else{
                if(fail[j] != -1){
                    j = fail[j];
                    i--;
                }
            }
        }
    }
}
