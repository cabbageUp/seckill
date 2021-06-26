package com.xxxx.seckill.pojo;

import java.util.*;

class Solution {
    public static void main(String[] args) {
        Solution s=new Solution();
        int[][] edges=new int[][]{{3,0},{1,3},{2,3},{4,3},{5,4}};
        s.findMinHeightTrees(6,edges);
    }
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        //bfs,遍历所有edge
        HashMap<Integer,Integer> map=new HashMap<>();
        boolean[][] con=new boolean[n][n];
        for(int i=0;i<edges.length;i++){
            con[edges[i][0]][edges[i][1]]=true;
            con[edges[i][1]][edges[i][0]]=true;
        }
        int minlen=99999999;
        for(int i=0;i<edges.length;i++){
            if(map.size()>=n) break;
            if(!map.containsKey(edges[i][0])){
                int t=bfs(edges,edges[i][0],con);
                minlen=Math.min(t,minlen);
                map.put(edges[i][0],t);
                System.out.println("key:"+edges[i][0]+", value:"+t);
            }
            if(!map.containsKey(edges[i][1])){
                int t=bfs(edges,edges[i][1],con);
                minlen=Math.min(t,minlen);
                map.put(edges[i][1],t);
                System.out.println("key:"+edges[i][1]+", value:"+t);
            }
        }
        List<Integer> res=new LinkedList<>();
        Iterator<Map.Entry<Integer,Integer>> entries=map.entrySet().iterator();
        while(entries.hasNext()){
            Map.Entry<Integer,Integer> entry=entries.next();
            if(entry.getValue()==minlen){
                res.add(entry.getKey());
            }
        }
        return res;

    }
    public int bfs(int[][] edges, int root,boolean[][] con){
        int res=1;
        LinkedList<Integer> list=new LinkedList<>();
        HashSet<Integer> set=new HashSet<>();
        set.add(root);
        list.add(root);
        while(!list.isEmpty()){
            int size=list.size();
            boolean flag=false;
            for(int k=0;k<size;k++){
                int temp=list.remove();
                for(int i=0;i<con[0].length;i++){
                    if(con[temp][i]){
                        if(!set.contains(i)){
                            flag=true;
                            set.add(i);
                            list.add(i);
                        }
                    }

                }
            }

            if(!flag){
                return res;
            }
            res++;
        }

        return res;
    }
}