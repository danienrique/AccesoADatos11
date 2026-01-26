package AD11;

import java.sql.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class Menu {

	private static final Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		try (Connection con = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/actividad15bd?useSSL=false&serverTimezone=UTC", "root", "alumno")) {

			int opcion;
			do {
				mostrarMenu();
				opcion = sc.nextInt();
				sc.nextLine();
				switch (opcion) {
				case 1: {
					insertarGrupo(con);
					break;
				}
				case 2: {
					insertarAlumno(con);
					break;
				}
				case 3: {
					mostrarAlumnos(con);
					break;
				}
				case 4: {
					exportarAlumnosTXT(con);
					break;
				}
				case 5: {
					importarAlumnosTXT(con);
					break;
				}
				case 6: {
					modificarNombreAlumno(con);
					break;
				}
				case 7: {
					eliminarAlumnoPK(con);
					break;
				}
				case 8: {
					eliminarAlumnosPorCurso(con);
					break;
				}
				case 9: {
					exportarGruposJSON(con);
					break;
				}
				case 10: {
					importarGruposJSON(con);
					break;
				}
				case 0: {
					System.out.println("Adiós");
					break;
				}
				default:
					System.out.println("Opción inválida");
				}
			} while (opcion != 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void mostrarMenu() {
		System.out.println("\nMENU");
		System.out.println("1. Insertar grupo");
		System.out.println("2. Insertar alumno");
		System.out.println("3. Mostrar alumnos con su grupo");
		System.out.println("4. Exportar alumnos a TXT");
		System.out.println("5. Importar alumnos desde TXT");
		System.out.println("6. Modificar nombre alumno por PK");
		System.out.println("7. Eliminar alumno por PK");
		System.out.println("8. Eliminar alumnos por curso");
		System.out.println("9. Exportar grupos a JSON");
		System.out.println("10. Importar grupos desde JSON");
		System.out.println("0. Salir");
	}

	// Grupos
	private static void insertarGrupo(Connection con) throws SQLException {
		System.out.print("Nombre del grupo: ");
		String nombre = sc.nextLine();
		System.out.print("Ciclo: ");
		String ciclo = sc.nextLine();
		System.out.print("Curso: ");
		String curso = sc.nextLine();

		PreparedStatement ps = con.prepareStatement("INSERT INTO grupos (Nombre, Ciclo, Curso) VALUES (?, ?, ?)");
		ps.setString(1, nombre);
		ps.setString(2, ciclo);
		ps.setString(3, curso);
		ps.executeUpdate();

		System.out.println("Grupo insertado correctamente");
	}

	// Alumnos
	private static void insertarAlumno(Connection con) throws SQLException {
		mostrarGrupos(con);
		System.out.print("ID del grupo: ");
		int Id_grupo = sc.nextInt();
		sc.nextLine();

		System.out.print("NIA: ");
		int Nia = sc.nextInt();
		sc.nextLine();
		System.out.print("Nombre: ");
		String Nombre = sc.nextLine();
		System.out.print("Apellidos: ");
		String Apellidos = sc.nextLine();
		System.out.print("Género (M/F): ");
		char Genero = sc.nextLine().charAt(0);
		System.out.print("Fecha nacimiento (YYYY-MM-DD): ");
		String FechaNac = sc.nextLine();

		PreparedStatement ps = con
				.prepareStatement("INSERT INTO alumnos (Nia, Nombre, Apellidos, Genero, FechaNac, Id_grupo)"
						+ " VALUES (?, ?, ?, ?, ?, ?)");
		ps.setInt(1, Nia);
		ps.setString(2, Nombre);
		ps.setString(3, Apellidos);
		ps.setString(4, String.valueOf(Genero));
		ps.setString(5, FechaNac);
		ps.setInt(6, Id_grupo);
		ps.executeUpdate();

		System.out.println("Alumno insertado correctamente");
	}

	private static void mostrarAlumnos(Connection con) throws SQLException {
		String sql = "SELECT a.Nia, a.Nombre, a.Apellidos, a.Genero, a.FechaNac, g.Nombre, g.Ciclo, g.Curso "
				+ "FROM alumnos a JOIN grupos g ON a.Id_grupo = g.Id_grupo";

		ResultSet rs = con.createStatement().executeQuery(sql);
		while (rs.next()) {
			System.out.printf("%d %s %s %s %s | Grupo: %s (%s - %s)%n", rs.getInt(1), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
		}
	}

	private static void modificarNombreAlumno(Connection con) throws SQLException {
		System.out.println("Indique el NIA del alumno a modificar");
		int Nia = sc.nextInt();
		sc.nextLine();
		System.out.println("Indique el nuevo nombre de la persona");
		String nombreNuevo = sc.nextLine();

		PreparedStatement ps = con.prepareStatement("UPDATE alumnos SET Nombre = ? WHERE Nia = ?");
		ps.setString(1, nombreNuevo);
		ps.setInt(2, Nia);
		ps.executeUpdate();
	}

	private static void eliminarAlumnoPK(Connection con) throws SQLException {
		System.out.print("NIA del alumno: ");
		int Nia = sc.nextInt();
		sc.nextLine();

		PreparedStatement ps = con.prepareStatement("DELETE FROM alumnos WHERE Nia = ?");
		ps.setInt(1, Nia);
		ps.executeUpdate();
	}

	private static void eliminarAlumnosPorCurso(Connection con) throws SQLException {
		ResultSet rs = con.createStatement().executeQuery("SELECT Id_grupo, Nombre, Ciclo, Curso FROM grupos");
		System.out.println("Cursos existentes:");
		while (rs.next()) {
			System.out.println("- " + rs.getString("Curso"));
		}

		System.out.print("Curso a eliminar: ");
		String curso = sc.nextLine();

		PreparedStatement ps = con
				.prepareStatement("DELETE a FROM alumnos a JOIN grupos g ON a.Id_grupo = g.Id_grupo WHERE g.Curso = ?");
		ps.setString(1, curso);
		ps.executeUpdate();
	}

	// Txt

	private static void exportarAlumnosTXT(Connection con) {
		System.out.print("Ruta TXT: ");
		String ruta = sc.nextLine();

		try (PrintWriter pw = new PrintWriter(new FileWriter(ruta));) {
			ResultSet rs = con.createStatement()
					.executeQuery("SELECT Nia, Nombre, Apellidos, Genero, FechaNac, Id_grupo FROM alumnos");
			while (rs.next()) {
				pw.printf("%d/%s/%s/%s/%s/%d%n", rs.getInt("Nia"), rs.getString("Nombre"), rs.getString("Apellidos"),
						rs.getString("Genero"), rs.getString("FechaNac"), rs.getInt("Id_grupo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void importarAlumnosTXT(Connection con) throws Exception {
		System.out.print("Ruta TXT: ");
		BufferedReader br = new BufferedReader(new FileReader(sc.nextLine()));
		String linea;

		while ((linea = br.readLine()) != null) {
			String[] d = linea.split("/");
			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO alumnos (Nia, Nombre, Apellidos, Genero, FechaNac, Id_grupo) VALUES (?, ?, ?, ?, ?, ?)");
			ps.setInt(1, Integer.parseInt(d[0]));
			ps.setString(2, d[1]);
			ps.setString(3, d[2]);
			ps.setString(4, d[3]);
			ps.setString(5, d[4]);
			ps.setInt(6, Integer.parseInt(d[5]));
			ps.executeUpdate();
		}
		br.close();
	}

	// JSon
	private static void exportarGruposJSON(Connection con) throws Exception {
		JSONArray gruposArr = new JSONArray();

		String sqlGrupos = "SELECT Id_grupo, Nombre, Ciclo, Curso FROM grupos";
		ResultSet rsGrupos = con.createStatement().executeQuery(sqlGrupos);

		while (rsGrupos.next()) {
			JSONObject grupoJSON = new JSONObject();
			int Id_grupo = rsGrupos.getInt("Id_grupo");

			grupoJSON.put("Id_grupo", Id_grupo);
			grupoJSON.put("Nombre", rsGrupos.getString("Nombre"));
			grupoJSON.put("Ciclo", rsGrupos.getString("Ciclo"));
			grupoJSON.put("Curso", rsGrupos.getString("Curso"));

			JSONArray alumnosArr = new JSONArray();
			PreparedStatement psAlu = con.prepareStatement(
					"SELECT Nia, Nombre, Apellidos, Genero, FechaNac FROM alumnos WHERE Id_grupo = ?");
			psAlu.setInt(1, Id_grupo);
			ResultSet rsAlu = psAlu.executeQuery();

			while (rsAlu.next()) {
				JSONObject aluJSON = new JSONObject();
				aluJSON.put("Nia", rsAlu.getInt("Nia"));
				aluJSON.put("Nombre", rsAlu.getString("Nombre"));
				aluJSON.put("Apellidos", rsAlu.getString("Apellidos"));
				aluJSON.put("Genero", rsAlu.getString("Genero"));
				aluJSON.put("FechaNac", rsAlu.getString("FechaNac"));
				alumnosArr.put(aluJSON);
			}

			grupoJSON.put("Alumnos", alumnosArr);
			gruposArr.put(grupoJSON);
		}

		try (FileWriter fw = new FileWriter("grupos.json")) {
			fw.write(gruposArr.toString(2));
		}

		System.out.println("Grupos exportados a JSON correctamente");
	}

	// Importa grupos y alumnos desde JSON usando BufferedReader
	private static void importarGruposJSON(Connection con) throws Exception {
		System.out.print("Ruta del JSON: ");
		String ruta = sc.nextLine();

		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				sb.append(linea);
			}
		}
		String contenido = sb.toString();

		JSONArray gruposArr = new JSONArray(contenido);

		for (int i = 0; i < gruposArr.length(); i++) {
			JSONObject grupoJSON = gruposArr.getJSONObject(i);

			PreparedStatement psGrupo = con.prepareStatement(
					"INSERT INTO grupos (Nombre, Ciclo, Curso) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			psGrupo.setString(1, grupoJSON.getString("Nombre"));
			psGrupo.setString(2, grupoJSON.getString("Ciclo"));
			psGrupo.setString(3, grupoJSON.getString("Curso"));
			psGrupo.executeUpdate();

			ResultSet keys = psGrupo.getGeneratedKeys();
			keys.next();
			int Id_grupo = keys.getInt(1);

			JSONArray alumnosArr = grupoJSON.getJSONArray("Alumnos");
			for (int j = 0; j < alumnosArr.length(); j++) {
				JSONObject aluJSON = alumnosArr.getJSONObject(j);

				PreparedStatement psAlu = con.prepareStatement(
						"INSERT INTO alumnos (Nia, Nombre, Apellidos, Genero, FechaNac, Id_grupo) VALUES (?, ?, ?, ?, ?, ?)");
				psAlu.setInt(1, aluJSON.getInt("Nia"));
				psAlu.setString(2, aluJSON.getString("Nombre"));
				psAlu.setString(3, aluJSON.getString("Apellidos"));
				psAlu.setString(4, aluJSON.getString("Genero"));
				psAlu.setString(5, aluJSON.getString("FechaNac"));
				psAlu.setInt(6, Id_grupo);
				psAlu.executeUpdate();
			}
		}

		System.out.println("Grupos importados desde JSON correctamente");
	}

	private static void mostrarGrupos(Connection con) throws SQLException {
		ResultSet rs = con.createStatement().executeQuery("SELECT Id_grupo, Nombre, Ciclo, Curso FROM grupos");
		System.out.println("Grupos disponibles:");
		while (rs.next()) {
			System.out.printf("%d - %s (%s - %s)%n", rs.getInt("Id_grupo"), rs.getString("Nombre"),
					rs.getString("Ciclo"), rs.getString("Curso"));
		}
	}
}
