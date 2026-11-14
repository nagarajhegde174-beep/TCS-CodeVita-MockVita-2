import java.util.*;

public class LadderProblem {
    static int M, N, K;
    static char[][] grid;

    static boolean free(int r, int c) {
        return r >= 0 && r < M && c >= 0 && c < N && grid[r][c] != 'B';
    }

    // o = 0 horizontal, o = 1 vertical; (r, c) is the top-left cell
    static boolean ladderOk(int r, int c, int o) {
        for (int k = 0; k < K; k++) {
            if (o == 0 ? !free(r, c + k) : !free(r + k, c)) return false;
        }
        return true;
    }

    static boolean squareOk(int r, int c) {
        for (int i = 0; i < K; i++)
            for (int j = 0; j < K; j++)
                if (!free(r + i, c + j)) return false;
        return true;
    }

    static int encode(int r, int c, int o) {
        return (r * N + c) * 2 + o;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) { System.out.println("Impossible"); return; }
        M = sc.nextInt();
        N = sc.nextInt();
        if (M <= 0 || N <= 0) { System.out.println("Impossible"); return; }

        // Reads cells whether or not they are separated by spaces
        StringBuilder sb = new StringBuilder();
        while (sb.length() < M * N && sc.hasNext()) sb.append(sc.next());
        grid = new char[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                grid[i][j] = sb.charAt(i * N + j);

        int sr = Integer.MAX_VALUE, scol = Integer.MAX_VALUE, sMaxR = -1;
        int dr0 = Integer.MAX_VALUE, dc0 = Integer.MAX_VALUE, dMaxR = -1;
        int sCount = 0, dCount = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 'l') {
                    sCount++;
                    sr = Math.min(sr, i); scol = Math.min(scol, j); sMaxR = Math.max(sMaxR, i);
                } else if (grid[i][j] == 'L') {
                    dCount++;
                    dr0 = Math.min(dr0, i); dc0 = Math.min(dc0, j); dMaxR = Math.max(dMaxR, i);
                }
            }
        }
        if (sCount == 0 || dCount == 0) { System.out.println("Impossible"); return; }
        K = sCount;

        int so = (sMaxR == sr) ? 0 : 1;
        int dO = (dMaxR == dr0) ? 0 : 1;
        int startCode = encode(sr, scol, so);
        int destCode = encode(dr0, dc0, dO);

        int[] dist = new int[M * N * 2];
        Arrays.fill(dist, -1);
        dist[startCode] = 0;
        Deque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{sr, scol, so});

        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1], o = cur[2];
            int code = encode(r, c, o);
            if (code == destCode) {
                System.out.println(dist[code]);
                return;
            }
            int d = dist[code];
            List<int[]> next = new ArrayList<>();

            for (int[] dir : dirs) next.add(new int[]{r + dir[0], c + dir[1], o});

            if (o == 0) {
                // horizontal -> vertical; ladder is the top or bottom row of the K x K square
                for (int top : new int[]{r, r - K + 1}) {
                    if (squareOk(top, c)) {
                        next.add(new int[]{top, c, 1});
                        next.add(new int[]{top, c + K - 1, 1});
                    }
                }
            } else {
                // vertical -> horizontal; ladder is the left or right column of the square
                for (int left : new int[]{c, c - K + 1}) {
                    if (squareOk(r, left)) {
                        next.add(new int[]{r, left, 0});
                        next.add(new int[]{r + K - 1, left, 0});
                    }
                }
            }

            for (int[] ns : next) {
                int nr = ns[0], nc = ns[1], no = ns[2];
                if (nr < 0 || nc < 0 || nr >= M || nc >= N) continue;
                if (!ladderOk(nr, nc, no)) continue;
                int nCode = encode(nr, nc, no);
                if (dist[nCode] != -1) continue;
                dist[nCode] = d + 1;
                q.add(new int[]{nr, nc, no});
            }
        }
        System.out.println("Impossible");
    }
}
