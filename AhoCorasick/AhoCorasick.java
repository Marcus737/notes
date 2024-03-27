package AhoCorasick;

import java.io.*;
import java.util.*;

/**
 * ac自动机
 * 洛谷P5357 AC代码
 */
public class AhoCorasick {
    public static class ACNode {
        //当前节点的字符
        public char c;
        //当前节点的子节点
        public Map<Character, ACNode> next;
        //终结节点
        public boolean isEnd;
        //失败后要跳转的节点
        public ACNode fail;
        //代表当前单词
        public String word;
        //入度
        public int inDegree;
        //出现次数
        public int vis;
        public ACNode() {
            this.next = new HashMap<>();
        }
        public ACNode(char c) {
            this.c = c;
            this.next = new HashMap<>();
        }
    }

    //存所有节点
    static List<ACNode> all = new ArrayList<>();

    /**
     * 构建Trie
     * @param root 根节点
     * @param key 关键词
     */
    public static void build(ACNode root, String key){
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            //当前节点的子节点不包含当前字符，就创建一个当前节点的子节点
            if (!root.next.containsKey(c)){
                //创建节点
                ACNode node = new ACNode(c);
                //保存到所有节点中
                all.add(node);
                //添加子节点
                root.next.put(c, node);
            }
            //转到下个节点
            root = root.next.get(c);
        }
        //当前节点是终结节点，记录的单词是key
        root.isEnd = true;
        root.word = key;
    }


    /**
     * 构建失败路径
     * @param root 根节点
     */
    public static void buildFail(ACNode root){
        //bfs的队列
        LinkedList<ACNode> queue = new LinkedList<>();
        //添加第一层
        for (ACNode child : root.next.values()) {
            //第一层的失败节点是根节点
            child.fail = root;
            queue.add(child);
        }
        while (!queue.isEmpty()){
            //拿出队头元素
            ACNode head = queue.pollFirst();
            //遍历队头元素的子元素
            for (ACNode child : head.next.values()) {
                //临时节点执行队偷头元素
                ACNode temp = head;
                //如果临时节点的失败节点的子元素集合中不包含当前元素，临时节点就指向它的失败节点
                while (temp.fail != null && !temp.fail.next.containsKey(child.c)){
                    temp = temp.fail;
                }
                //临时节点不是根节点，说明找到了当前节点的失败节点，否则失败节点就是根节点
                if (temp.fail != null){
                    child.fail = temp.fail.next.get(child.c);
                    //此时child.c = child.fail.c
                }else {
                    child.fail = root;
                }
                //失败节点入度+=1
                child.fail.inDegree++;
                //当前节点入队
                queue.add(child);
            }
        }
    }


    /**
     * 拓扑排序
     * 入度为0的节点入队，遍历与其相连的元素，减少其入度，若入度为0，就入队
     * @param root 根节点
     */
    public  static void topSort(ACNode root){
        //队列
        LinkedList<ACNode> q = new LinkedList<>();
        //入度为0的入队
        all.forEach(acNode -> {
            if (acNode.inDegree == 0) q.add(acNode);
        });
        while (!q.isEmpty()){
            //取出队头
            ACNode first = q.pollFirst();
            //拿到队头的失败节点
            ACNode fail = first.fail;
            if (fail != root){
                //失败节点的出现次数加上当前节点的出现次数
                fail.vis += first.vis;
                //入度为0入队
                if (--fail.inDegree == 0){
                    q.add(fail);
                }
            }
        }

    }

    /**
     * 获取结果集合
     * @return 结果
     */
    private static Map<String, Integer> getResult() {
        HashMap<String, Integer> map = new HashMap<>();
        all.forEach(acNode -> map.put(acNode.word, acNode.vis));
        return map;
    }

    /**
     * 查询给定字符串中出现关键词的次数
     * @param root 根节点
     * @param str 字符串
     */
    public static void query(ACNode root, String str){
        //临时节点指向根节点，因为下面操作会改变根节点
        ACNode t = root;
        //遍历当前字符串
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            //当前根节点的子元素集合中包含当前字符c
            if (root.next.containsKey(c)){
                //跳到下个节点
                ACNode child = root.next.get(c);
                if (child != null){
                    //当前出现次数+1，相当于打个标记
                    child.vis++;
                }
                //转到下个节点
                root = root.next.get(c);
            }else {
                //没有包含，当前根节点指向其失败节点重新匹配
                if (root.fail != null){
                    root = root.fail;
                    i--;
                }
            }
        }
        //计算总结果，真正计算在这里，优化的操作
        topSort(t);
    }


    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        //创建根节点
        ACNode root = new ACNode();

        int sum = Integer.parseInt(br.readLine());

        List<String> keys = new ArrayList<>();
        while (sum -- > 0){
            String s = br.readLine();
            keys.add(s);
            //构建trie
            build(root, s);
        }
        //构建失败节点
        buildFail(root);
        //查询结果
        query(root, br.readLine());
        //获取结果
        Map<String, Integer>  map = getResult();
        for (String key : keys) {
            bw.write(map.get(key)+ "\n");
        }
        bw.flush();
        br.close();
    }



}


