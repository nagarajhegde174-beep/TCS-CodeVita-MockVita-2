import java.util.*;
import java.io.*;

public class SolveTheExpression {
    static int[] tt;
    static String[] tv;
    static int pos = 0, cnt = 0;

    static char get(String s, int i) { return i < s.length() ? s.charAt(i) : ' '; }

    static String code(String[] rows, int base, int k) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                sb.append(get(rows[base + r], k * 3 + c) != ' ' ? '1' : '0');
        return sb.toString();
    }

    static String pad(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < n; i++) sb.append('0');
        return sb.append(s).toString();
    }

    static String bin(String a, String b, boolean and) {
        int n = Math.max(a.length(), b.length());
        a = pad(a, n); b = pad(b, n);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            boolean x = a.charAt(i) == '1', y = b.charAt(i) == '1';
            sb.append((and ? (x && y) : (x || y)) ? '1' : '0');
        }
        return sb.toString();
    }

    static String andExpr() {
        String v = orExpr();
        while (pos < cnt && tt[pos] == 2) { pos++; v = bin(v, orExpr(), true); }
        return v;
    }

    static String orExpr() {
        String v = unary();
        while (pos < cnt && tt[pos] == 1) { pos++; v = bin(v, unary(), false); }
        return v;
    }

    static String unary() {
        if (tt[pos] == 3) {
            pos++;
            String s = unary();
            StringBuilder sb = new StringBuilder();
            for (char ch : s.toCharArray()) sb.append(ch == '1' ? '0' : '1');
            return sb.toString();
        }
        return primary();
    }

    static String primary() {
        if (tt[pos] == 4) {
            pos++;
            String v = andExpr();
            pos++; // closing bracket
            return v;
        }
        return tv[pos++];
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<String> lines = new ArrayList<>();
        String ln;
        while ((ln = br.readLine()) != null) lines.add(ln.replace("\r", ""));

        String[] rows = new String[9];
        boolean spaced = lines.size() >= 17;
        if (spaced)
            for (int i = 1; i <= 15; i += 2)
                if (!lines.get(i).trim().isEmpty()) { spaced = false; break; }
        for (int i = 0; i < 9; i++) {
            int idx = spaced ? 2 * i : i;
            rows[i] = idx < lines.size() ? lines.get(idx) : "";
        }

        String[] dig = new String[10];
        for (int k = 0; k < 10; k++) dig[k] = code(rows, 0, k);
        String[] op = new String[5];
        for (int k = 0; k < 5; k++) op[k] = code(rows, 3, k);

        int W = Math.max(rows[6].length(), Math.max(rows[7].length(), rows[8].length()));
        int syms = (W + 2) / 3;

        tt = new int[syms + 1];
        tv = new String[syms + 1];
        StringBuilder cur = null;
        for (int k = 0; k < syms; k++) {
            String c = code(rows, 6, k);
            int d = -1, o = -1;
            for (int i = 0; i < 10; i++) if (dig[i].equals(c)) { d = i; break; }
            if (d < 0) for (int i = 0; i < 5; i++) if (op[i].equals(c)) { o = i; break; }
            if (d >= 0) {
                if (cur == null) cur = new StringBuilder();
                cur.append(c);
            } else {
                if (cur != null) { tt[cnt] = 0; tv[cnt++] = cur.toString(); cur = null; }
                if (o >= 0) { tt[cnt] = o + 1; tv[cnt++] = ""; }
            }
        }
        if (cur != null) { tt[cnt] = 0; tv[cnt++] = cur.toString(); }

        String res = andExpr();
        res = pad(res, ((res.length() + 8) / 9) * 9);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < res.length(); i += 9) {
            String ch = res.substring(i, i + 9);
            for (int d = 0; d < 10; d++) if (dig[d].equals(ch)) { out.append(d); break; }
        }
        System.out.print(out);
    }
}
