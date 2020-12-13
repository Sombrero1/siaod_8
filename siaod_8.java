package com.company;


import static java.lang.System.*;
import static java.lang.Math.*;

public class siaod_8 {
    final static int n = 5;
    final static int m = 3;
    final static double M = Double.MAX_VALUE;

    public static void main(String[] args) {
        double[] v = new double[n];
        double[][] a = new double[m][n];
        double[] cB = new double[m];
        int[] bV = new int[m];
        double[] bD = new double[m];
        
        v[0] = 1;v[1] = 0;v[2] = 0;v[3] = 0;v[4] = 0;
        a[0][0] = 1; a[0][1] =-2; a[0][2] = 1; a[0][3] = 0; a[0][4] = 0;
        a[1][0] = 1; a[1][1] =-1; a[1][2] = 0; a[1][3] = 1; a[0][4] = 0;
        a[2][0] = 1; a[2][1] = 1; a[1][2] = 0; a[1][3] = 0; a[0][4] = 1;
        cB[0] = 0;cB[1] = 0;cB[2] = 0;
        bV[0] = 2;bV[1] = 3;bV[2] = 4;
        bD[0] = 0;bD[1] = 1;bD[2] = 2;
        double[] min = simpleMethod(v, a, cB, bV, bD);
        printArray(min);
        double[] max = simplexMethodMax(v, a, cB, bV, bD);
        printArray(max);
    }

 
    static double[] simpleMethod(double[] c, double[][] a, double[] cB, int[] bV,
                                 double[] bD) {
        double[] delta = new double[n];
        double[] min = new double[m];

        out.println("\n\nПоиск минимума\n\n");
        int i, j, r = -1, s = -1;
        double[][] tmpA = new double[m][];
        double[] tmpBD = new double[m];

        int k = 0;
        while (true) {
            ++k;
            
            double deltaMin = Double.POSITIVE_INFINITY;
            r = -1;
            for (j = 0; j < n; ++j) {
                double z = 0;
                for (i = 0; i < m; ++i) {
                    z += cB[i] * a[i][j];
                }
                delta[j] = c[j] - z;
                if (deltaMin > delta[j]) {
                    deltaMin = delta[j];
                    r = j;
                }
            }
            // Условие выхода 
            if (deltaMin >= 0 || k > 100) {
                break;
            }
            // Определяем разрешающую строку
            double minRow = Double.POSITIVE_INFINITY;
            s = -1;
            for (i = 0; i < m; ++i) {
                min[i] = bD[i] / a[i][r];
                if (min[i] < 0) {
                    min[i] = Double.NaN;
                }
                if (minRow > min[i]) {
                    minRow = min[i];
                    s = i;
                }
            }
            out.println("Разрешающий столбец: r = " + r);
            out.println("Разрешающая строка:  s = " + s);
            printTable(c, a, cB, bV, bD, min);
            
            double temp = a[s][r];
            for (i = 0; i < m; ++i) {
                tmpA[i] = new double[n];
                for (j = 0; j < n; ++j) {
                    tmpA[i][j] = a[i][j];
                }
                tmpBD[i] = bD[i];
            }
            // Вносим переменную в базис,
            // Пересчитываем базисное решение и коэффицетов переменных -
            bV[s] = r;
            cB[s] = c[r];
            for (j = 0; j < n; ++j) {
                a[s][j] /= temp;
            }
            bD[s] /= temp;

            // - в остальных строках
            for (i = 0; i < m; ++i) {
                // разрешающая строка уже пересчитана
                if (i == s) {
                    continue;
                }

                double air = tmpA[i][r];

                for (j = 0; j < n; ++j) {
                    a[i][j] -= (air * tmpA[s][j]) / temp;
                }

                bD[i] -= (air * tmpBD[s]) / temp;
            }

            printDelta(delta);
            out.println("----------------------------------------------------");
        }

        out.println("Разрешающий столбец: r = " + (r + 1));
        out.println("Разрешающая строка:  s = " + (s + 1));
        printTable(c, a, cB, bV, bD, min);
        printDelta(delta);
        return print(bV, bD);
    }
    static double[] simplexMethodMax(double[] c, double[][] a, double[] cB, int[] bV,
                                     double[] bD) {
        double[] delta = new double[n];
        double[] min = new double[m];

        out.println("\n\nПоиск максимума\n\n");
        int i, j, r = -1, s = -1;
        double[][] tmpA = new double[m][];
        double[] tmpBD = new double[m];

        int k = 0;
        while (true) {
            ++k;
            
            double deltaMax = Double.NEGATIVE_INFINITY;
            r = -1;
            for (j = 0; j < n; ++j) {
                double z = 0;
                for (i = 0; i < m; ++i) {
                    z += cB[i] * a[i][j];
                }
                delta[j] = c[j] - z;
                if (deltaMax < delta[j]) {
                    deltaMax = delta[j];
                    r = j;
                }
            }

            // Условие выхода 
            if (deltaMax <= 0 || k > 100) {
                break;
            }

            // Определяем разрешающую строку (с минимальным отношением
            double minRow = Double.POSITIVE_INFINITY;
            s = -1;
            for (i = 0; i < m; ++i) {
                min[i] = bD[i] / a[i][r];
                if (min[i] < 0) {
                    min[i] = Double.NaN;
                }
                if (minRow > min[i]) {
                    minRow = min[i];
                    s = i;
                }
            }

            out.println("Разрешающий столбец: r = " + r);
            out.println("Разрешающая строка:  s = " + s);
            printTable(c, a, cB, bV, bD, min);

            double element = a[s][r];
            
            for (i = 0; i < m; ++i) {
                tmpA[i] = new double[n];
                for (j = 0; j < n; ++j) {
                    tmpA[i][j] = a[i][j];
                }
                tmpBD[i] = bD[i];
            }

            bV[s] = r;
            cB[s] = c[r];
            for (j = 0; j < n; ++j) {
                a[s][j] /= element;
            }
            bD[s] /= element;

            for (i = 0; i < m; ++i) {
                if (i == s) {
                    continue;
                }
                
                double air = tmpA[i][r];

                for (j = 0; j < n; ++j) {
                    a[i][j] -= (air * tmpA[s][j]) / element;
                }
                
                bD[i] -= (air * tmpBD[s]) / element;
            }

            printDelta(delta);
            out.println("----------------------------------------------------");
        }

        out.println("Разрешающий столбец: r = " + (r + 1));
        out.println("Разрешающая строка:  s = " + (s + 1));
        printTable(c, a, cB, bV, bD, min);
        printDelta(delta);
        return print(bV, bD);
    }
    
    static double[] print(int[] bV, double[] bD) {
        int i, j;
        out.println("Все оценки неположительны, подсчет завершен.");
        out.print("Решение x = (");
        double[] res = new double[m];
        boolean f;
        for (j = 0; j < n; ++j) {
            f = false;
            for (i = 0; i < m; ++i) {
                if (bV[i] == j) {
                    if (j < m) {
                        res[j] = bD[i];
                    }
                    out.print(round(bD[i], 2));
                    f = true;
                    break;
                }
            }
            if (!f) {
                out.print("0");
            }
            if (j < n - 1) {
                out.print(", ");
            }
        }
        out.print(")");
        return res;
    }
    
    static void printDelta(double[] delta) {
        out.print("\t\t\t\t");
        for (int j = 0; j < n; ++j) {
            out.print(round(delta[j], 2) + "\t");
        }
        out.println("delta[j]");
    }
    
    static void printTable(double[] c, double[][] a, double[] cB, int[] bV,
                           double[] bD, double[] min) {
        int i, j;
        // вывод: строка коэффициентов
        out.print("\t\t\t\t");
        for (j = 0; j < n; ++j) {
            out.print(round(c[j], 2) + "\t");
        }
        out.println("C[j]");

        // вывод: ряд x
        out.print("\tcB\tbV\tbD\t");
        for (j = 0; j < n; ++j) {
            out.print("x[" + j + "]\t");
        }
        out.println("bD[i] / a[i][r]");

        for (i = 0; i < m; ++i) {
            out.print("\t" + round(cB[i], 2) + "\tx[" + (bV[i] + 1) + "]\t"
                    + round(bD[i], 2) + "\t");
            for (j = 0; j < n; ++j) {
                out.print(round(a[i][j], 2) + "\t");
            }
            out.println(round(min[i], 2));
        }
    }

    static void printArray(double[] ar)
    {
        out.println();
        for (int i = 0; i < ar.length; ++i) {
            out.println("\t[" + i + "] = " + ar[i]);
        }
        out.println();
    }

    static String round(double n, int p) {
        if (Double.isNaN(n)) {
            return "NaN";
        }
        if (Double.isInfinite(n)) {
            return "\u221E";
        }
        double d = pow(10, p);
        return (Math.round(n * d) / d) + "";
    }
}