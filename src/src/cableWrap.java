import java.util.*;

public class cableWrap {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < n * m && sc.hasNext()) sb.append(sc.next());
        char[][] g = new char[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) g[i][j] = sb.charAt(i * m + j);

        // rod lines: for each R cell, pick the row or column holding more R's
        int[] rc = new int[n], cc = new int[m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (g[i][j] == 'R') { rc[i]++; cc[j]++; }
        boolean[] hRow = new boolean[n], vCol = new boolean[m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (g[i][j] == 'R') {
                    if (rc[i] > cc[j]) hRow[i] = true; else vCol[j] = true;
                }

        int[] DR = {1, -1, 0, 0}, DC = {0, 0, 1, -1};

        // find a cable end: boundary C cell with exactly one link
        int er = -1, ec = -1;
        for (int i = 0; i < n && er < 0; i++)
            for (int j = 0; j < m; j++) {
                if (g[i][j] != 'C') continue;
                if (i != 0 && j != 0 && i != n - 1 && j != m - 1) continue;
                int deg = 0;
                for (int d = 0; d < 4; d++) {
                    int a = i + DR[d], b = j + DC[d];
                    if (a < 0 || b < 0 || a >= n || b >= m) continue;
                    if (g[a][b] == 'C') deg++;
                    else if (g[a][b] == 'R') {
                        int x = i - DR[d], y = j - DC[d];
                        if (x >= 0 && y >= 0 && x < n && y < m && g[x][y] == 'C') deg++;
                    }
                }
                if (deg == 1) { er = i; ec = j; break; }
            }
        if (er < 0) { System.out.print(0); return; }

        Map<Integer, Integer> sum = new HashMap<>();
        boolean[][] vis = new boolean[n][m];
        int r = er, c = ec, dr = 0, dc = 0;
        boolean first = true;
        while (true) {
            vis[r][c] = true;
            int nr = -1, nc = -1;
            if (!first) {
                int ar = r + dr, ac = c + dc;
                if (ar >= 0 && ac >= 0 && ar < n && ac < m && g[ar][ac] == 'R' && !vis[ar][ac]) {
                    nr = ar; nc = ac;
                }
            }
            if (nr < 0) {
                for (int d = 0; d < 4; d++) {
                    int a = r + DR[d], b = c + DC[d];
                    if (a < 0 || b < 0 || a >= n || b >= m) continue;
                    if (g[a][b] == 'C' && !vis[a][b]) { nr = a; nc = b; break; }
                }
            }
            if (nr < 0) break;
            dr = nr - r; dc = nc - c;
            r = nr; c = nc;
            first = false;

            int h = g[r][c] == 'R' ? -1 : 1;
            int id = -1, dir = 0;
            if (dr != 0 && hRow[r]) { id = r; dir = dr; }
            else if (dc != 0 && vCol[c]) { id = 1000 + c; dir = dc; }
            if (id >= 0) sum.merge(id, dir * h, Integer::sum);
        }

        int ans = 0;
        for (int v : sum.values()) ans += Math.abs(v) / 2;
        System.out.print(ans);
    }
}