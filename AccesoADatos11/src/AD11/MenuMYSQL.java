package AD11;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.*;

import org.w3c.dom.DOMImplementation;

import netscape.javascript.JSObject;

public class MenuMYSQL {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		Menu();
	}
	
	public static void Menu() {
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
				leerAlumnosArchivo(sentencia);
				break;
			}
			case 5: {
				modificarNombrePK(sentencia);
				break;
			}
			case 6: {
				eliminarAlumnoPK(sentencia);
				break;
			}
			case 7: {
				eliminarAlumnosApellidos(sentencia);
				break;
			}
			case 8: {
				almacenarAlumnosXmlJson(sentencia);
				break;
			}
			case 9: {
				leerAlumnosArchivoXmlJson(sentencia);
				break;
			}
			case 0: {
				System.out.println("Saliendooo");
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
		System.out.println("¿Desea guardar los alumnos en un archivo de texto (1), o en un archivo binario (2)?");
		int respuesta = sc.nextInt();
		sc.nextLine();
		if(respuesta == 1) {
			almacenarAlumnosTexto(sentencia);
		} else if(respuesta == 2) {
			AlmacenarAlumnosBinario(sentencia);
		} else {
			System.out.println("valor incorrecto");
		}
	}
	
	public static void leerAlumnosArchivo(Statement sentencia) {
		System.out.println("De que archivo desea leerlo:");
		File f = new File(sc.nextLine());
		try(BufferedReader br = new BufferedReader(new FileReader(f))){
			String consulta = "SELECT Nia, Nombre, Apellidos, Genero, FechaNac, Curso, Ciclo, Grupo \n FROM alumnos";
			ResultSet resul = sentencia.executeQuery(consulta);
			while(resul.next()) {
				System.out.printf("%d, %s, %s, %s, %s, %s, %s, %s%n",resul.getInt(1), resul.getString(2),resul.getString(3), resul.getString(4), resul.getString(5)
						, resul.getString(6), resul.getString(7), resul.getString(8));
			}
		} catch(Exception e) {
			e.getStackTrace();
		}
	}
	
	public static void modificarNombrePK(Statement sentencia) {
		System.out.println("Indique el NIA del alumno a modificar");
		String primaryKey = sc.nextLine();
		System.out.println("Indique el nuevo nombre de la persona");
		String nombreNuevo = sc.nextLine();
		try {
			int resul = sentencia.executeUpdate("UPDATE alumnos SET Nombre = " + nombreNuevo + " WHERE Nia = "+ primaryKey);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void eliminarAlumnoPK(Statement sentencia) {
		System.out.println("Indique el NIA del alumno a eliminar");
		String primaryKey = sc.nextLine();
		try {
			int resul = sentencia.executeUpdate("DELETE FROM alumnos WHERE Nia = " + primaryKey);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void eliminarAlumnosApellidos(Statement sentencia) {
		System.out.println("Indique la palabra que contra el apellido de los alumnos que desea eliminar");
		String palabraEliminar = sc.nextLine();
		try {
			int resul = sentencia.executeUpdate("DELETE FROM alumnos WHERE Apellidos LIKE %" + palabraEliminar + "%");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void almacenarAlumnosXmlJson(Statement sentencia) {
		System.out.println("¿Desea guardar los alumnos en un fichero XML (1), o en un JSON (2)?");
		int key = sc.nextInt();
		sc.nextLine();
		switch (key) {
		case 1: {
			almacenarAlumnosXML();
			break;
		}
		case 2:{
			almacenarAlumnosJSON();
			break;
		}
		default:
			throw new IllegalArgumentException("Error: valor invalido: " + key);
		}
	}
	public static void leerAlumnosArchivoXmlJson(Statement sentencia) {
		System.out.println("¿Desea leer los alumnos de un fichero XML (1), o en un JSON (2)?");
		int key = sc.nextInt();
		sc.nextLine();
		switch (key) {
		case 1: {
			leerAlumnosXML();
			break;
		}
		case 2: {
			leerAlumnosJSON();
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + key);
		}
	}
	
	
	public static void almacenarAlumnosXML() {
		System.out.println("Donde desea guardarlo");
		File f = new File(sc.nextLine());
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dm = factory.newDocumentBuilder();
			DOMImplementation domi = dm.getDOMImplementation();
			Document document = domi.createDocument(null, "Alumnos", null);
			document.setXmlVersion("1.0");
			Alumno aux = crearAlumno();
			Element alumno = document.createElement("Alumno");
			crearAlumnoXML("Nia", Integer.toString(aux.getNia()), alumno, document);
			crearAlumnoXML("Nombre", aux.getNombre(), alumno, document);
			crearAlumnoXML("Apellido", aux.getApellidos(), alumno, document);
			crearAlumnoXML("Ciclo", aux.getCiclo(), alumno, document);
			crearAlumnoXML("Curso", aux.getCurso(), alumno, document);
			crearAlumnoXML("Grupo", aux.getGrupo(), alumno, document);
			crearAlumnoXML("Genero", Character.toString(aux.getGenero()), alumno, document);
			crearAlumnoXML("FechaNacimiento", aux.getFecha_nacimiento().toString(), alumno, document);
			Source src = new DOMSource(document);
			Result resul = new StreamResult(f);
			Transformer trf = TransformerFactory.newInstance().newTransformer();
			trf.transform(src, resul);
		} catch(Exception e){
			System.out.println("Error: "+ e.getLocalizedMessage());
		}
	}
	
	public static void crearAlumnoXML(String datoAlumno, String valor, Element alumno, Document docu) {
		Element elem = docu.createElement(datoAlumno);
		Text text = docu.createTextNode(valor);
		elem.appendChild(text);
		alumno.appendChild(elem);
	}
	public static void leerAlumnosXML() {
		System.out.println("Donde desea guardarlo");
		File f = new File(sc.nextLine());
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(f);
			document.getDocumentElement().normalize();
			System.out.println("Elemnto Raiz: " + document.getDocumentElement().getNodeName());
			NodeList alumnos = document.getElementsByTagName("Alumnos");
			
			for(int i = 0; i<alumnos.getLength();i++) {
				Node alum = alumnos.item(i);
				if(alum.getNodeType() == Node.ELEMENT_NODE){
					Element elem = (Element) alum;
					System.out.println("Nia: " + elem.getElementsByTagName("Nia").item(0).getTextContent());
					System.out.println("Nombre: " + elem.getElementsByTagName("Nombre").item(0).getTextContent());
					System.out.println("Apellidos: " + elem.getElementsByTagName("Apellidos").item(0).getTextContent());
					System.out.println("Ciclo: " + elem.getElementsByTagName("Ciclo").item(0).getTextContent());
					System.out.println("Curso: " + elem.getElementsByTagName("Curso").item(0).getTextContent());
					System.out.println("Grupo: " + elem.getElementsByTagName("Grupo").item(0).getTextContent());
					System.out.println("Genero: " + elem.getElementsByTagName("Genero").item(0).getTextContent());
					System.out.println("FechaNacimiento: " + elem.getElementsByTagName("FechaNacimiento").item(0).getTextContent());
				}
			}
		} catch(Exception e) {
			
		}
	}
	
	
	public static void almacenarAlumnosJSON() {
		System.out.println("Donde desea guardarlo");
		File f = new File(sc.nextLine());
		StringBuilder contenido = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
		    String linea;
		    while((linea = br.readLine()) != null) {
		        contenido.append(linea);
		    }
		    // 1. Si el archivo está vacío → array JSON
		    JSONArray alumnosJson;
		    if (contenido.length() == 0) {
		        alumnosJson = new JSONArray();
		    } else {
		        alumnosJson = new JSONArray(contenido.toString());
		    }
		    // 2. Crear alumno JSON
		    Alumno aux = crearAlumno();
		    JSONObject alumnoJson = new JSONObject();
		    alumnoJson.put("Nia", aux.getNia());
		    alumnoJson.put("Nombre", aux.getNombre());
		    alumnoJson.put("Apellidos", aux.getApellidos());
		    alumnoJson.put("Curso", aux.getCurso());
		    alumnoJson.put("Ciclo", aux.getCiclo());
		    alumnoJson.put("Grupo", aux.getGrupo());
		    alumnoJson.put("Genero", aux.getGenero());
		    alumnoJson.put("FechaNAc", aux.getFecha_nacimiento());

		    // 3. Lo añades a la lista
		    alumnosJson.put(alumnoJson);

		    // 4. Guardar de nuevo
		    try(PrintWriter pw = new PrintWriter(new FileWriter(f))) {
		        pw.print(alumnosJson.toString());
		    }

		} catch(Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static void leerAlumnosJSON() {
		System.out.println("De que archivo desea leerlo");
		File f = new File(sc.nextLine());
		StringBuilder contenido = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(f))){
			String linea;
			while((linea = br.readLine()) != null) {
				contenido.append(linea);
			}
			JSONObject aux = new JSONObject(contenido.toString());
		}catch(Exception e) {
			e.getStackTrace();
		}
	}
	
	
	public static void almacenarAlumnosTexto(Statement sentencia) {
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
	
	public static void AlmacenarAlumnosBinario(Statement sentencia){
		try {
		System.out.println("Donde desea guardarlo:");
		File f = new File(sc.nextLine());
		String consulta = "SELECT Nia, Nombre, Apellidos, Genero, FechaNac, Curso, Ciclo, Grupo \n FROM alumnos";
			ResultSet resul = sentencia.executeQuery(consulta);
			try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))){
				Alumno aux = crearAlumno();
				oos.writeObject(aux);
			}catch(Exception e) {
				System.out.println("Error: " + e.getLocalizedMessage());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
