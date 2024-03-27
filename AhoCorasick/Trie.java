package AhoCorasick;


import java.util.HashMap;
import java.util.Map;

class Node{
    char c;
    Map<Character, Node> next;
    int cnt;

    public Node() {
        this.next = new HashMap<>();
        this.cnt = 0;
    }

    public Node(char c) {
        this.c = c;
        this.next = new HashMap<>();
        this.cnt = 0;
    }
}

public class Trie {
    public static void main(String[] args) {
        Node root = new Node();
        buildTrie(root, "123");
        buildTrie(root, "abc");
        buildTrie(root, "123");
        buildTrie(root, "你好");
        buildTrie(root, "123");
        System.out.println(query(root, "abc"));
        System.out.println(query(root, "123"));
        System.out.println(query(root, "你好"));
    }

    public static void buildTrie(Node root, String key){
        for (char c : key.toCharArray()) {
            if (!root.next.containsKey(c)){
                //创建节点
                Node node = new Node(c);
                root.next.put(c, node);
            }
            //转到下个节点
            root = root.next.get(c);
        }
        //当前单词出现的次数+1
        root.cnt++;
    }


    public static int query(Node root, String str){
        for (char c : str.toCharArray()) {
            if (!root.next.containsKey(c)){
                return 0;
            }
            //转到下个节点
            root = root.next.get(c);
        }
        //返回单词出现的次数
        return root.cnt;
    }

}
