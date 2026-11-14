import java.util.*;

public class Zoobin {
    static int MAXN;

    static String key(int[][] edges) {
        int[] codes = new int[edges.length];
        for (int i = 0; i < edges.length; i++) {
            int a = Math.min(edges[i][0], edges[i][1]);
            int b = Math.max(edges[i][0], edges[i][1]);
            codes[i] = a * 1000 + b;
        }
        Arrays.sort(codes);
        return Arrays.toString(codes);
    }

    static List<int[]> cycles;
    static List<Integer>[] adj;
    static boolean[] vis;
    static int[] path;

    static void dfs(int s, int u, int len) {
        for (int v : adj[u]) {
            if (v == s && len >= 3) {
                cycles.add(Arrays.copyOf(path, len));
            } else if (v > s && !vis[v]) {
                vis[v] = true;
                path[len] = v;
                dfs(s, v, len + 1);
                vis[v] = false;
            }
        }
    }

    @SuppressWarnings("unchecked")
    static List<int[]> findCycles(int[][] edges) {
        adj = new List[MAXN + 1];
        for (int i = 0; i <= MAXN; i++) adj[i] = new ArrayList<>();
        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }
        cycles = new ArrayList<>();
        vis = new boolean[MAXN + 1];
        path = new int[MAXN + 2];
        for (int s = 1; s <= MAXN; s++) {
            vis[s] = true;
            path[0] = s;
            dfs(s, s, 1);
            vis[s] = false;
        }
        return cycles; // each cycle appears in both directions
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int E = sc.nextInt();
        int[][] cur = new int[E][2];
        int[][] tgt = new int[E][2];
        MAXN = 0;
        for (int i = 0; i < E; i++) {
            cur[i][0] = sc.nextInt(); cur[i][1] = sc.nextInt();
            MAXN = Math.max(MAXN, Math.max(cur[i][0], cur[i][1]));
        }
        for (int i = 0; i < E; i++) {
            tgt[i][0] = sc.nextInt(); tgt[i][1] = sc.nextInt();
            MAXN = Math.max(MAXN, Math.max(tgt[i][0], tgt[i][1]));
        }
        String goal = key(tgt);
        String start = key(cur);
        if (start.equals(goal)) { System.out.print(0); return; }

        Map<String, Integer> dist = new HashMap<>();
        dist.put(start, 0);
        Deque<int[][]> q = new ArrayDeque<>();
        q.add(cur);
        while (!q.isEmpty()) {
            int[][] g = q.poll();
            int d = dist.get(key(g));
            for (int[] cyc : findCycles(g)) {
                int[] p = new int[MAXN + 1];
                for (int i = 0; i <= MAXN; i++) p[i] = i;
                for (int i = 0; i < cyc.length; i++) p[cyc[i]] = cyc[(i + 1) % cyc.length];
                int[][] ng = new int[E][2];
                for (int i = 0; i < E; i++) {
                    ng[i][0] = p[g[i][0]];
                    ng[i][1] = p[g[i][1]];
                }
                String k = key(ng);
                if (dist.containsKey(k)) continue;
                dist.put(k, d + 1);
                if (k.equals(goal)) { System.out.print(d + 1); return; }
                q.add(ng);
            }
        }
        System.out.print(-1); // not reachable (the statement doesn't say what to print)
    }
}
