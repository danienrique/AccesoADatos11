package AD11;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class MenuMYSQL {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int key;
		try(Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/Alumnos01?useSSL=false&serverTimezone=UTC",
				"ADManager", "manager");
				Statement sentencia = conexion.createStatement();){
		do {
			System.out.println("--- MENU ---" + "\n" + "1. Insertar nuevo alumno" + "\n" + "2. Mostrar alumnos" + "\n"
					+ "3. Guardar alumnos en un fichero (No XML, no JSon)" + "\n"
					+ "4. Leer alumnos de un fichero (No XML, no JSon)" + "\n"
					+ "5. Modificar nombre del graduado (Primary Key)" + "\n" + "6. Eliminar alumnos" + "\n"
					+ "7. Eliminar alumnos por apellido" + "\n" + "8. Guardar todos los alumnos en XML o JSON" + "\n"
					+ "9. Leer XML o JSON y guardarlo en la BBDD" + "\n" + "0. Salir" + "\n");

			key = sc.nextInt();
			sc.nextLine();
			switch (key) {
			case 1: {
				System.out.println("Introduciremos un nuevo alumno");
				Alumno aux = crearAlumno();
				insertarAlumno(aux, sentencia);
				break;
			}
			case 2: {
				mostrarAlumnos(sentencia);
				break;
			}
			case 3: {
				almacenarAlumnos(sentencia);
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
		} while (key != 0);
		} catch(Exception e) {
			e.getStackTrace();
		} 
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

	public static void insertarAlumno(Alumno aux, Statement sentencia) {
		try {
			String consulta = "INSERT INTO alumnos (Nia, Nombre, Apellidos, Genero, FechaNac, Curso, Ciclo, Grupo)\n"
					+ "VALUES ('" + aux.getNia() + "', '" + aux.getNombre() + "', '" + aux.getApellidos() + "', '"
					+ aux.getGenero() + "', '" + aux.fechaNacimientoSQL() + " 00:00:00', '" + aux.getCurso() + "', '"
					+ aux.getCiclo() + "', '" + aux.getGrupo() + "');";
			int resul = sentencia.executeUpdate(consulta);
			System.out.println("Alumno creado correctamente");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void mostrarAlumnos(Statement sentencia) {
		try {
			String consulta = "SELECT Nia, Nombre, Apellidos, Genero, FechaNac, Curso, Ciclo, Grupo \n FROM alumnos";
			ResultSet resul = sentencia.executeQuery(consulta);
			while(resul.next()) {
				System.out.printf("%d, %s, %s, %s, %s, %s, %s, %s%n",resul.getInt(1), resul.getString(2),resul.getString(3), resul.getString(4), resul.getString(5)
						, resul.getString(6), resul.getString(7), resul.getString(8));
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	public static void almacenarAlumnos(Statement sentencia) {
		System.out.println("Donde desea guardarlo:");
		File f = new File(sc.nextLine());
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(f))){
			String consulta = "SELECT Nia, Nombre, Apellidos, Genero, FechaNac, Curso, Ciclo, Grupo \n FROM alumnos";
			ResultSet resul = sentencia.executeQuery(consulta);
			while(resul.next()) {
				bw.write(resul.getInt(1) + resul.getString(2) + resul.getString(3) + resul.getString(4) + resul.getString(5) + 
						resul.getString(6) + resul.getString(7) + resul.getString(8));
			}
		} catch(Exception e) {
			e.getStackTrace();
		}
	}
	
	public static void leerAlumnosArchivo(Statement sentencia) {
		System.out.println("Donde desea guardarlo:");
		File f = new File(sc.nextLine());
		try(BufferedReader br = new BufferedReader(new FileReader(f))){
			String consulta = "SELECT Nia, Nombre, Apellidos, Genero, FechaNac, Curso, Ciclo, Grupo \n FROM alumnos";
			ResultSet resul = sentencia.executeQuery(consulta);
			while(resul.next()) {
				
			}
		} catch(Exception e) {
			e.getStackTrace();
		}
	}
	
	public static void modificarNombrePK() {
		
	}
	
	public static void eliminarAlumnoPK() {
		
	}
	
	public static void eliminarAlumnosApellidos() {
		
	}
	
	public static void almacenarAlumnosXmlJson() {
		
	}
	public static void leerAlumnosArchivoXmlJson() {
		
	}
}
