
import java.util.*;

public class UnoGame {
    static int C, limit, best = 0;
    static int[] cw, cs, conflict;
    static boolean[] banned;
    static int[] freeBest;

    static void dfs(int i, int chosen, int w, int cnt) {
        if (w > limit) return;
        if (i == C) {
            best = Math.max(best, cnt + freeBest[limit - w]);
            return;
        }
        dfs(i + 1, chosen, w, cnt);                       // skip
        if (!banned[i] && (conflict[i] & chosen) == 0)    // take
            dfs(i + 1, chosen | (1 << i), w + cw[i], cnt + cs[i]);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, Integer> id = new HashMap<>();
        for (int i = 0; i < n; i++) id.put(sc.next(), i);
        int[] skill = new int[n];
        for (int i = 0; i < n; i++) skill[i] = sc.nextInt();

        int[] unit = new int[n];
        for (int i = 0; i < n; i++) unit[i] = i;   // unit representative
        int n1 = sc.nextInt();
        for (int i = 0; i < n1; i++) {
            int a = id.get(sc.next()), b = id.get(sc.next());
            unit[b] = unit[a];                     // merge b into a's unit
        }
        int n2 = sc.nextInt();
        int[][] riv = new int[n2][2];
        boolean[] inRival = new boolean[n];
        for (int i = 0; i < n2; i++) {
            riv[i][0] = id.get(sc.next());
            riv[i][1] = id.get(sc.next());
            inRival[riv[i][0]] = true;
            inRival[riv[i][1]] = true;
        }
        limit = sc.nextInt();

        // unit weight, size, constrained flag
        int[] uw = new int[n], us = new int[n];
        boolean[] uCons = new boolean[n];
        for (int i = 0; i < n; i++) {
            int u = unit[i];
            uw[u] += skill[i];
            us[u]++;
            if (inRival[i]) uCons[u] = true;
        }

        // index constrained units
        int[] cIdx = new int[n];
        Arrays.fill(cIdx, -1);
        C = 0;
        for (int u = 0; u < n; u++) if (us[u] > 0 && uCons[u]) cIdx[u] = C++;
        cw = new int[C]; cs = new int[C];
        conflict = new int[C]; banned = new boolean[C];
        for (int u = 0; u < n; u++) if (cIdx[u] >= 0) { cw[cIdx[u]] = uw[u]; cs[cIdx[u]] = us[u]; }
        for (int[] r : riv) {
            int a = cIdx[unit[r[0]]], b = cIdx[unit[r[1]]];
            if (a == b) banned[a] = true;
            else { conflict[a] |= 1 << b; conflict[b] |= 1 << a; }
        }

        // knapsack over free units
        int NEG = -1;
        int[] dp = new int[limit + 1];
        Arrays.fill(dp, NEG);
        dp[0] = 0;
        for (int u = 0; u < n; u++) {
            if (us[u] == 0 || uCons[u]) continue;
            for (int w = limit; w >= uw[u]; w--)
                if (dp[w - uw[u]] != NEG)
                    dp[w] = Math.max(dp[w], dp[w - uw[u]] + us[u]);
        }
        freeBest = new int[limit + 1];
        int run = 0;
        for (int w = 0; w <= limit; w++) {
            run = Math.max(run, dp[w]);
            freeBest[w] = run;
        }

        dfs(0, 0, 0, 0);
        System.out.println(best);
    }
}