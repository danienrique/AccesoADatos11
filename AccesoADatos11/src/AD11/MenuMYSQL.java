package AD11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


public class MenuMYSQL {
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		int key;
		do {
			System.out.println("--- MENU ---" + "\n" +
								"1. Insertar nuevo alumno" + "\n" +
								"2. Mostrar alumnos" + "\n" + 
								"3. Guardar alumnos en un fichero (No XML, no JSon)" + "\n" +
								"4. Leer alumnos de un fichero (No XML, no JSon)" + "\n" +
								"5. Modificar nombre del graduado (Primary Key)" + "\n" +
								"6. Eliminar alumnos" + "\n" +
								"7. Eliminar alumnos por apellido" + "\n" +
								"8. Guardar todos los alumnos en XML o JSON" + "\n" +
								"9. Leer XML o JSON y guardarlo en la BBDD" + "\n" +
								"0. Salir" + "\n" );

			key = sc.nextInt();
			switch (key) {
			case 1: {
				System.out.println("Introduciremos un nuevo alumno");
				Alumno aux = crearAlumno();
				insertarAlumno(aux);
				break;
			}
			case 2: {
				break;
			}
			case 3: {
				break;
			}
			case 4: {
				break;
			}
			case 5: {
				break;
			}
			case 6: {
				break;
			}
			case 7: {
				break;
			}
			case 8: {
				break;
			}
			case 9: {
				break;
			}
			case 0: {
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + key);
			}
		}while(key !=0);
		
	}
	public static Alumno crearAlumno() {
		int nia, dia, mes, anio;
		String nombre, apellidos, ciclo, curso, grupo;
		char genero;
		System.out.println("Nia:");
		nia = sc.nextInt();
		sc.nextLine();
		System.out.println("Nombre:");
		nombre = sc.nextLine();
		System.out.println("Apellidos:");
		apellidos = sc.nextLine();
		System.out.println("Curso:");
		curso = sc.nextLine();
		System.out.println("Ciclo:");
		ciclo = sc.nextLine();
		System.out.println("Grupo:");
		grupo = sc.nextLine();
		System.out.println("Genero:");
		genero = sc.nextLine().charAt(0);
		System.out.println("Año de nacimiento:");
		anio = sc.nextInt();
		System.out.println("Mes de nacimiento:");
		mes = sc.nextInt();
		System.out.println("Dia de nacimiento:");
		dia = sc.nextInt();
		sc.nextLine();
		return new Alumno(nia, nombre, apellidos, ciclo, curso, grupo, genero, anio, mes, dia);
	}
	
	public static void insertarAlumno(Alumno aux){
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");

	        Connection conexion = DriverManager.getConnection(
	            "jdbc:mysql://localhost:3306/Alumnos01?useSSL=false&serverTimezone=UTC","ADManager","alumno");

	        Statement sentencia = conexion.createStatement();
	        String consulta = "SELECT * FROM Alumno";
	        ResultSet resul = sentencia.executeQuery(consulta);

	        while(resul.next()) {
	            System.out.println(resul.getInt(1));
	        }

	        resul.close();
	        sentencia.close();
	        conexion.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
