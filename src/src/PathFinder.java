import java.util.*;

public class PathFinder {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        int[][] g = new int[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) g[i][j] = sc.nextInt();
        int sr = sc.nextInt() - 1, sc0 = sc.nextInt() - 1;
        int tr = sc.nextInt() - 1, tc = sc.nextInt() - 1;
        int k = sc.hasNextInt() ? sc.nextInt() : 0;
        boolean[][] blocked = new boolean[n][m];
        for (int i = 0; i < k; i++) {
            int r = sc.nextInt() - 1, c = sc.nextInt() - 1;
            blocked[r][c] = true;
        }

        int INF = Integer.MAX_VALUE / 2;
        int[][] dist = new int[n][m];
        for (int[] row : dist) Arrays.fill(row, INF);
        dist[sr][sc0] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.add(new int[]{0, sr, sc0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int d = cur[0], r = cur[1], c = cur[2];
            if (d > dist[r][c]) continue;
            if (r == tr && c == tc) break;

            // threshold: current value and the max of all unblocked neighbours
            int T = g[r][c];
            for (int dr = -1; dr <= 1; dr++)
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    int nr = r + dr, nc = c + dc;
                    if (nr < 0 || nc < 0 || nr >= n || nc >= m || blocked[nr][nc]) continue;
                    T = Math.max(T, g[nr][nc]);
                }

            for (int dr = -1; dr <= 1; dr++)
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    int nr = r + dr, nc = c + dc;
                    if (nr < 0 || nc < 0 || nr >= n || nc >= m || blocked[nr][nc]) continue;
                    // max among the other neighbours and the current cell
                    int t = g[r][c];
                    for (int er = -1; er <= 1; er++)
                        for (int ec = -1; ec <= 1; ec++) {
                            if (er == 0 && ec == 0) continue;
                            int xr = r + er, xc = c + ec;
                            if (xr < 0 || xc < 0 || xr >= n || xc >= m || blocked[xr][xc]) continue;
                            if (xr == nr && xc == nc) continue;
                            t = Math.max(t, g[xr][xc]);
                        }
                    int cost = Math.max(0, t - g[nr][nc] + 1);
                    if (d + cost < dist[nr][nc]) {
                        dist[nr][nc] = d + cost;
                        pq.add(new int[]{dist[nr][nc], nr, nc});
                    }
                }
        }
        System.out.print(dist[tr][tc]);
    }
}

