import java.util.*;
import java.io.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    static final String ARCHIVO = "jugadores.csv";

    static final String[] simbolos = {"7", "X", "$", "*", "@", "#"};

    public static void main(String[] args) {

        mostrarTitulo();

        System.out.print("\nIngrese su nombre: ");
        String nombre = scanner.nextLine().trim();

        int saldo = cargarSaldo(nombre);

        boolean salir = false;

        while (!salir) {

            mostrarMenu();

            int opcion;

            try {

                opcion = Integer.parseInt(scanner.nextLine());

            } catch (Exception e) {

                System.out.println("⚠️ Opción inválida.");
                continue;
            }

            switch (opcion) {

                case 1:

                    System.out.print("\nIngrese apuesta: $");

                    int apuesta;

                    try {

                        apuesta = Integer.parseInt(scanner.nextLine());

                    } catch (Exception e) {

                        System.out.println("⚠️ Valor inválido.");
                        break;
                    }

                    if (apuesta <= 0) {

                        System.out.println("⚠️ La apuesta debe ser mayor que 0.");
                        break;
                    }

                    if (apuesta > saldo) {

                        System.out.println("⚠️ No tienes suficiente saldo.");
                        break;
                    }

                    saldo -= apuesta;

                    String[][] matriz = new String[3][3];

                    animarGiro(matriz);

                    int premio = calcularPremio(matriz, apuesta);

                    saldo += premio;

                    System.out.println("\n💰 Premio ganado: $" + premio);
                    System.out.println("💵 Saldo actual: $" + saldo);

                    actualizarJugador(nombre, saldo);

                    break;

                case 2:

                    System.out.println("\n💵 Saldo actual: $" + saldo);
                    break;

                case 3:

                    mostrarRanking();
                    break;

                case 4:

                    actualizarJugador(nombre, saldo);

                    System.out.println("\n👋 Gracias por jugar.");
                    salir = true;
                    break;

                default:

                    System.out.println("⚠️ Opción inválida.");
            }
        }
    }

    public static void mostrarTitulo() {

        System.out.println(
            "\n" +
            "██╗      ██╗   ██╗ ██████╗██╗  ██╗██╗   ██╗\n" +
            "██║      ██║   ██║██╔════╝██║ ██╔╝╚██╗ ██╔╝\n" +
            "██║      ██║   ██║██║     █████╔╝  ╚████╔╝ \n" +
            "██║      ██║   ██║██║     ██╔═██╗   ╚██╔╝  \n" +
            "███████╗ ╚██████╔╝╚██████╗██║  ██╗   ██║   \n" +
            "╚══════╝  ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   \n"
        );

        System.out.println("🎰 THE LUCKY ARCADE 🎰");
    }

    public static void mostrarMenu() {

        System.out.println("\n==============================");
        System.out.println("1️⃣ Apostar");
        System.out.println("2️⃣ Ver saldo");
        System.out.println("3️⃣ Ranking");
        System.out.println("4️⃣ Salir");
        System.out.println("==============================");
        System.out.print("Seleccione una opción: ");
    }

    public static int cargarSaldo(String nombre) {

        File archivo = new File(ARCHIVO);

        try {

            if (!archivo.exists()) {

                archivo.createNewFile();
            }

            BufferedReader br = new BufferedReader(new FileReader(ARCHIVO));

            String linea;

            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(",");

                if (datos[0].equalsIgnoreCase(nombre)) {

                    br.close();

                    return Integer.parseInt(datos[1]);
                }
            }

            br.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        guardarJugador(nombre, 1000);

        return 1000;
    }

    public static void guardarJugador(String nombre, int saldo) {

        try {

            BufferedWriter bw = new BufferedWriter(
                new FileWriter(ARCHIVO, true)
            );

            bw.write(nombre + "," + saldo);

            bw.newLine();

            bw.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void actualizarJugador(String nombre, int saldo) {

        List<String> lineas = new ArrayList<>();

        try {

            BufferedReader br = new BufferedReader(
                new FileReader(ARCHIVO)
            );

            String linea;

            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(",");

                if (datos[0].equalsIgnoreCase(nombre)) {

                    lineas.add(nombre + "," + saldo);

                } else {

                    lineas.add(linea);
                }
            }

            br.close();

            BufferedWriter bw = new BufferedWriter(
                new FileWriter(ARCHIVO)
            );

            for (String l : lineas) {

                bw.write(l);

                bw.newLine();
            }

            bw.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void mostrarRanking() {

        List<String[]> jugadores = new ArrayList<>();

        try {

            BufferedReader br = new BufferedReader(
                new FileReader(ARCHIVO)
            );

            String linea;

            while ((linea = br.readLine()) != null) {

                jugadores.add(linea.split(","));
            }

            br.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        jugadores.sort((a, b) ->
            Integer.parseInt(b[1]) - Integer.parseInt(a[1])
        );

        System.out.println("\n🏆 RANKING");

        for (String[] jugador : jugadores) {

            System.out.println(
                "👤 " + jugador[0] +
                " - 💵 $" + jugador[1]
            );
        }
    }

    public static void animarGiro(String[][] matriz) {

        try {

            for (int k = 0; k < 15; k++) {

                limpiarConsola();

                generarMatriz(matriz);

                System.out.println("\n🎰 GIRANDO... 🎰");

                mostrarMatriz(matriz);

                Thread.sleep(120);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void generarMatriz(String[][] matriz) {

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                int random = (int)(Math.random() * simbolos.length);

                matriz[i][j] = simbolos[random];
            }
        }
    }

    public static void mostrarMatriz(String[][] matriz) {

        System.out.println();

        System.out.println("╔═══╦═══╦═══╗");

        for (int i = 0; i < 3; i++) {

            System.out.print("║");

            for (int j = 0; j < 3; j++) {

                System.out.print(" " + matriz[i][j] + " ║");
            }

            System.out.println();

            if (i < 2) {

                System.out.println("╠═══╬═══╬═══╣");
            }
        }

        System.out.println("╚═══╩═══╩═══╝");
    }

    public static int calcularPremio(String[][] matriz, int apuesta) {

        int lineas = 0;
        int premio = 0;

        for (int i = 0; i < 3; i++) {

            if (
                matriz[i][0].equals(matriz[i][1]) &&
                matriz[i][1].equals(matriz[i][2])
            ) {

                lineas++;

                premio += valorSimbolo(
                    matriz[i][0],
                    apuesta
                );
            }
        }

        if (
            matriz[0][0].equals(matriz[1][1]) &&
            matriz[1][1].equals(matriz[2][2])
        ) {

            lineas++;

            premio += valorSimbolo(
                matriz[0][0],
                apuesta
            );
        }

        if (
            matriz[0][2].equals(matriz[1][1]) &&
            matriz[1][1].equals(matriz[2][0])
        ) {

            lineas++;

            premio += valorSimbolo(
                matriz[0][2],
                apuesta
            );
        }

        if (lineas >= 2) {

            premio *= lineas;
        }

        return premio;
    }

    public static int valorSimbolo(String simbolo, int apuesta) {

        switch (simbolo) {

            case "7":
                return apuesta * 10;

            case "X":
                return apuesta * 5;

            case "$":
                return apuesta * 4;

            case "*":
                return apuesta * 3;

            case "@":
                return apuesta * 2;

            default:
                return apuesta;
        }
    }

    public static void limpiarConsola() {

        System.out.print("\033[H\033[2J");

        System.out.flush();
    }
}