import model.*;
import java.util.*;
public class main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Elegir paciente");
            System.out.println("2. Agregar");
            System.out.println("3. Eliminar");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    mostrarPacientes();
                    System.out.print("Ingrese el ID del paciente: ");
                    String idElegido = sc.nextLine();
                    Paciente paciente = Paciente.elegirPaciente(idElegido);
                    if (paciente == null) {
                        System.out.println("Paciente no encontrado.");
                        break;
                    }
                    boolean subMenuPaciente = true;
                    while (subMenuPaciente) {
                        System.out.println("\n--- ACCIONES PARA PACIENTE ---");
                        System.out.println("1. Crear cita");
                        System.out.println("2. Crear trasplante");
                        System.out.println("3. Volver");
                        System.out.print("Seleccione una opción: ");
                        String subOpcion = sc.nextLine();
                        switch (subOpcion) {
                            case "1":
                                System.out.println("Cita creada para paciente " + paciente.getName());
                                break;
                            case "2":
                                System.out.println("Trasplante creado para paciente " + paciente.getName());
                                break;
                            case "3":
                                subMenuPaciente = false;
                                break;
                            default:
                                System.out.println("Opción inválida.");
                        }
                    }
                    break;
                case "2":
                    System.out.println("\n--- AGREGAR ---");
                    System.out.println("1. Paciente");
                    System.out.println("2. Donante");
                    System.out.print("Seleccione una opción: ");
                    String tipoAgregar = sc.nextLine();
                    if (tipoAgregar.equals("1")) {
                        Paciente nuevo = crearPaciente(sc);
                        Paciente.añadir(nuevo);
                        System.out.println("Paciente agregado.");
                    } else if (tipoAgregar.equals("2")) {
                        Donante nuevo = crearDonante(sc);
                        Donante.añadir(nuevo);
                        System.out.println("Donante agregado.");
                    } else {
                        System.out.println("Opción inválida.");
                    }
                    break;
                case "3":
                    System.out.println("\n--- ELIMINAR ---");
                    System.out.println("1. Paciente");
                    System.out.println("2. Donante");
                    System.out.print("Seleccione una opción: ");
                    String tipoEliminar = sc.nextLine();
                    if (tipoEliminar.equals("1")) {
                        mostrarPacientes();
                        System.out.print("Ingrese el ID del paciente a eliminar: ");
                        String id = sc.nextLine();
                        Paciente p = Paciente.elegirPaciente(id);
                        if (p != null) {
                            Paciente.eliminar(p);
                            System.out.println("Paciente eliminado.");
                        } else {
                            System.out.println("Paciente no encontrado.");
                        }
                    } else if (tipoEliminar.equals("2")) {
                        mostrarDonantes();
                        System.out.print("Ingrese el ID del donante a eliminar: ");
                        String id = sc.nextLine();
                        Donante d = null;
                        for (Donante don : Donante.getDonantes()) {
                            if (don.getId().equals(id)) {
                                d = don;
                                break;
                            }
                        }
                        if (d != null) {
                            Donante.eliminar(d);
                            System.out.println("Donante eliminado.");
                        } else {
                            System.out.println("Donante no encontrado.");
                        }
                    } else {
                        System.out.println("Opción inválida.");
                    }
                    break;
                case "4":
                    running = false;
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        sc.close();
    }

    // Mostrar lista de pacientes
    private static void mostrarPacientes() {
        List<Paciente> pacientes = Paciente.getPacientes();
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        System.out.println("Pacientes disponibles:");
        for (Paciente p : pacientes) {
            System.out.println("ID: " + p.getId() + " | Nombre: " + p.getName());
        }
    }

    // Mostrar lista de donantes
    private static void mostrarDonantes() {
        List<Donante> donantes = Donante.getDonantes();
        if (donantes.isEmpty()) {
            System.out.println("No hay donantes registrados.");
            return;
        }
        System.out.println("Donantes disponibles:");
        for (Donante d : donantes) {
            System.out.println("ID: " + d.getId() + " | Nombre: " + d.getName());
        }
    }

    // Crear paciente
    private static Paciente crearPaciente(Scanner sc) {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Edad: "); byte edad = Byte.parseByte(sc.nextLine());
        System.out.print("ID: "); String id = sc.nextLine();
        System.out.print("Tipo de sangre: "); String sangre = sc.nextLine();
        System.out.print("Dirección: "); String dir = sc.nextLine();
        System.out.print("Teléfono: "); String tel = sc.nextLine();
        return new Paciente(nombre, edad, id, sangre, dir, tel, 0, 0, new ArrayList<>(), new ArrayList<>());
    }

    // Crear donante
    private static Donante crearDonante(Scanner sc) {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Edad: "); byte edad = Byte.parseByte(sc.nextLine());
        System.out.print("ID: "); String id = sc.nextLine();
        System.out.print("Tipo de sangre: "); String sangre = sc.nextLine();
        System.out.print("Dirección: "); String dir = sc.nextLine();
        System.out.print("Teléfono: "); String tel = sc.nextLine();
        return new Donante(nombre, edad, id, sangre, dir, tel, new Date(), "", "", true);
    }
}